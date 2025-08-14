package br.com.suaempresa.apigerenciamento.user.controller;

import br.com.suaempresa.apigerenciamento.user.dto.UserRegistrationDTO;
import br.com.suaempresa.apigerenciamento.user.dto.UserResponseDTO;
import br.com.suaempresa.apigerenciamento.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint PÚBLICO para registro
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        UserResponseDTO createdUser = userService.registerUser(registrationDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Endpoint PROTEGIDO para listar todos os usuários
    // Apenas usuários com o papel 'ADMIN' podem acessar
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> listAllUsers() {
        List<UserResponseDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }
}
