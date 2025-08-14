package br.com.suaempresa.apigerenciamento.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDTO {

    @NotBlank(message = "O nome não pode ser vazio.")
    private String nome;

    @Email(message = "O formato do e-mail é inválido.")
    @NotBlank(message = "O e-mail não pode ser vazio.")
    private String email;

    @NotBlank(message = "A senha não pode ser vazia.")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
    private String senha;
}