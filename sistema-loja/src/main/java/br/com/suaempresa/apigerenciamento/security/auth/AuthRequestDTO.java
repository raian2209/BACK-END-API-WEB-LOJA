package br.com.suaempresa.apigerenciamento.security.auth;

import lombok.Getter;
import lombok.Setter;

// DTO para o request de login
@Getter
@Setter
public class AuthRequestDTO {
    private String email;
    private String senha;
}
