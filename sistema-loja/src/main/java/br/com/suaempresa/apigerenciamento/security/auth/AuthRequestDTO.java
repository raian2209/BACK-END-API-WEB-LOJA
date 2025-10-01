package br.com.suaempresa.apigerenciamento.security.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

// DTO para o request de login
@Getter
@Setter
public class AuthRequestDTO {

    @NotEmpty(message = "Insira algum email")
    @Email(message = "email invalido")
    private String email;

    @NotEmpty(message = "Digite sua senha")
    private String senha;
}
