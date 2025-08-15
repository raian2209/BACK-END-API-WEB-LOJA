package br.com.suaempresa.apigerenciamento.security.auth;

import lombok.Getter;
import lombok.Setter;

// DTO para a resposta com o token
@Getter @Setter
public class AuthResponseDTO {
    private String token;
    public AuthResponseDTO(String token) { this.token = token; }
}
