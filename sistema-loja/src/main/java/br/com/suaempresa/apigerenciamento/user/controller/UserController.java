package br.com.suaempresa.apigerenciamento.user.controller;

import br.com.suaempresa.apigerenciamento.order.dto.PedidoRequestDTO;
import br.com.suaempresa.apigerenciamento.user.dto.UserRegistrationDTO;
import br.com.suaempresa.apigerenciamento.user.dto.UserResponseDTO;
import br.com.suaempresa.apigerenciamento.user.model.User;
import br.com.suaempresa.apigerenciamento.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PostMapping("/usuario")
    public ResponseEntity<UserResponseDTO> registerUsuario(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        UserResponseDTO createdUser = userService.registerUser(registrationDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/fornecedor")
    public ResponseEntity<UserResponseDTO> registerFornecedor(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        UserResponseDTO createdUser = userService.registerFornecedor(registrationDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/dono")
    public ResponseEntity<UserResponseDTO> registerDono(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        UserResponseDTO createdUser = userService.registerAdmin(registrationDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // TODO DELETE
    @DeleteMapping
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<UserResponseDTO> deleteUser( @Valid @RequestBody UserRegistrationDTO userDTO,
                                             @AuthenticationPrincipal User currentUser) {
        UserResponseDTO responseDTO = userService.deleteUsuario(userDTO);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<UserResponseDTO> updateUser( @Valid @RequestBody UserRegistrationDTO userDTO,
                                                       @AuthenticationPrincipal User currentUser) {
        UserResponseDTO responseDTO = userService.updateUsuario(userDTO);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
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
