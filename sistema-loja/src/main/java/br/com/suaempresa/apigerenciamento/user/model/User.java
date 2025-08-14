package br.com.suaempresa.apigerenciamento.user.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Collection;
import java.util.List;

// 1. Intercepta o DELETE e o transforma em um UPDATE
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ?")
// 2. Adiciona um filtro para todas as buscas (SELECTs)
@Where(clause = "deleted = false")
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING) // Armazena o nome do Enum no banco ("ROLE_ADMIN")
    @Column(nullable = false)
    private Role role;

    // para Soft Delete
    @Column(name = "deleted")
    private boolean deleted = false; // O valor padrão é 'false'

    // Métodos do UserDetails para integração com o Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    // Deixamos como true, mas poderíamos ter lógica para contas expiradas/bloqueadas
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}