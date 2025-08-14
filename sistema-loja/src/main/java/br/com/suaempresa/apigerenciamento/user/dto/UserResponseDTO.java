package br.com.suaempresa.apigerenciamento.user.dto;

import br.com.suaempresa.apigerenciamento.user.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private Role role;
}
