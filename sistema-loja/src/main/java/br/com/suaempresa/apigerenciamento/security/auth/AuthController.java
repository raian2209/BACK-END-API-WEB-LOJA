package br.com.suaempresa.apigerenciamento.security.auth;

import br.com.suaempresa.apigerenciamento.security.jwt.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        // 1. O AuthenticationManager usa o UserDetailsService e o PasswordEncoder para validar o usuário
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        var user = (br.com.suaempresa.apigerenciamento.user.model.User) authentication.getPrincipal();

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(
                new AuthResponseDTO(
                        user.getId().toString(),
                        user.getEmail(),
                        user.getRole().name(),
                        token
                )
        );
    }
}
