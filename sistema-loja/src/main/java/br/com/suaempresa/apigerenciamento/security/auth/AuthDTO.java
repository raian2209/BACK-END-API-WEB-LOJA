package br.com.suaempresa.apigerenciamento.security.auth;

import lombok.Getter;
import lombok.Setter;

// DTO para o request de login
@Getter @Setter
class AuthRequestDTO {
    private String email;
    private String senha;
}

// DTO para a resposta com o token
@Getter @Setter
class AuthResponseDTO {
    private String token;
    public AuthResponseDTO(String token) { this.token = token; }
}
