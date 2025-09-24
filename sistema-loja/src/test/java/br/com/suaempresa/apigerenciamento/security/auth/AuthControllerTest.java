package br.com.suaempresa.apigerenciamento.security.auth;

import br.com.suaempresa.apigerenciamento.security.auth.AuthController;
import br.com.suaempresa.apigerenciamento.security.auth.AuthRequestDTO;
import br.com.suaempresa.apigerenciamento.security.jwt.JwtService;
import br.com.suaempresa.apigerenciamento.user.model.Role;
import br.com.suaempresa.apigerenciamento.user.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Carrega um contexto de teste focado apenas na camada Web para o AuthController
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc; // Para simular requisições HTTP

    @Autowired
    private ObjectMapper objectMapper; // Para converter objetos em JSON

    // Mockamos as dependências do AuthController
    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    // É uma boa prática mockar o UserDetailsService também, pois a configuração de segurança
    // que o @WebMvcTest carrega pode precisar dele.
    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void quandoLoginComCredenciaisValidas_deveRetornarStatusOKeTokenJWT() throws Exception {
        // Arrange (Dado)
        AuthRequestDTO requestDTO = new AuthRequestDTO();
        requestDTO.setEmail("usuario@example.com");
        requestDTO.setSenha("senha123");

        User authenticatedUser = new User();
        authenticatedUser.setId(1L);
        authenticatedUser.setEmail(requestDTO.getEmail());
        authenticatedUser.setSenha("senhaCriptografada"); // A senha real não importa aqui
        authenticatedUser.setRole(Role.ROLE_USUARIO);


        when(userDetailsService.loadUserByUsername("usuario@example.com")).thenReturn(authenticatedUser);

        // Configura o mock do AuthenticationManager para simular que a senha está correta
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Configura o mock do JwtService para retornar um token
        when(jwtService.generateToken(any())).thenReturn("token.jwt.falso");

        // Act & Assert (Quando & Então)
        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token.jwt.falso"));
    }

    @Test
    void quandoLoginComCredenciaisInvalidas_deveRetornarStatusUnauthorized() throws Exception {
        // Arrange (Dado)
        // 1. Prepara o DTO da requisição
        AuthRequestDTO requestDTO = new AuthRequestDTO();
        requestDTO.setEmail("usuario@example.com");
        requestDTO.setSenha("senhaErrada");

        // 2. Configura o mock do AuthenticationManager para lançar uma exceção de credenciais inválidas
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        // Act & Assert (Quando & Então)
        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isUnauthorized()); // Espera um status 401 Unauthorized
    }
}