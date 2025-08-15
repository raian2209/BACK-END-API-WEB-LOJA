package br.com.suaempresa.apigerenciamento.user.controller;

import br.com.suaempresa.apigerenciamento.user.dto.UserRegistrationDTO;
import br.com.suaempresa.apigerenciamento.user.dto.UserResponseDTO;
import br.com.suaempresa.apigerenciamento.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; // Ferramenta para fazer as chamadas HTTP

    @Autowired
    private ObjectMapper objectMapper; // Para converter objetos em JSON

    @MockBean // Usa um mock do UserService para não depender da lógica de negócio real
    private UserService userService;

    @Test
    void quandoRegistrarComDadosValidos_deveRetornarStatusCreatedEUsuario() throws Exception {
        // Arrange
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setNome("Novo Usuário");
        registrationDTO.setEmail("novo@example.com");
        registrationDTO.setSenha("senhaValida123");

        UserResponseDTO responseDTO = new UserResponseDTO(); // O que o serviço deve retornar
        responseDTO.setId(1L);
        responseDTO.setEmail("novo@example.com");

        when(userService.registerUser(any(UserRegistrationDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("novo@example.com"))
                .andExpect(jsonPath("$.senha").doesNotExist()); // Garante que a senha não é retornada!
    }

    @Test
    void quandoRegistrarComSenhaCurta_deveRetornarStatusBadRequest() throws Exception {
        // Arrange
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setNome("Nome");
        registrationDTO.setEmail("email@valido.com");
        registrationDTO.setSenha("123"); // Senha inválida (curta)

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isBadRequest()); // Espera um erro 400 Bad Request
    }

    @Test
    @WithMockUser(roles = "USUARIO") // Simula um usuário logado com o papel USUARIO
    void quandoUsuarioTentaListarTodosUsuarios_deveRetornarStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden()); // Espera um erro 403 Forbidden
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Simula um usuário logado com o papel ADMIN
    void quandoAdminTentaListarTodosUsuarios_deveRetornarStatusOK() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }
}