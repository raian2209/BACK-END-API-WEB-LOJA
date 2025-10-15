package br.com.suaempresa.apigerenciamento.user.service;

import br.com.suaempresa.apigerenciamento.exception.EmailAlreadyExistsException;
import br.com.suaempresa.apigerenciamento.exception.ForbiddenException;
import br.com.suaempresa.apigerenciamento.user.dto.UserRegistrationDTO;
import br.com.suaempresa.apigerenciamento.user.dto.UserResponseDTO;
import br.com.suaempresa.apigerenciamento.user.model.Role;
import br.com.suaempresa.apigerenciamento.user.model.User;
import br.com.suaempresa.apigerenciamento.user.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService  implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,@Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDTO registerUser(UserRegistrationDTO registrationDTO) {
        userRepository.findByEmail(registrationDTO.getEmail()).ifPresent(user -> {
            throw new EmailAlreadyExistsException("E-mail já cadastrado: " + registrationDTO.getEmail());
        });

        // 2. Lógica: Cria e salva o novo usuário
        User newUser = new User();
        newUser.setNome(registrationDTO.getNome());
        newUser.setEmail(registrationDTO.getEmail());
        // CRUCIAL: Codifica a senha antes de salvar!
        newUser.setSenha(passwordEncoder.encode(registrationDTO.getSenha()));
        // Por padrão, novos registros são do tipo USUARIO
        newUser.setRole(Role.ROLE_USUARIO);

        User savedUser = userRepository.save(newUser);

        // 3. Mapeamento: Converte a entidade para um DTO de resposta
        return mapToResponseDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO registerFornecedor(UserRegistrationDTO registrationDTO) {
        // 1. Validação: Verifica se o e-mail já existe
        userRepository.findByEmail(registrationDTO.getEmail()).ifPresent(user -> {
            throw new EmailAlreadyExistsException("E-mail já cadastrado: " + registrationDTO.getEmail());
        });

        // 2. Lógica: Cria e salva o novo usuário
        User newUser = new User();
        newUser.setNome(registrationDTO.getNome());
        newUser.setEmail(registrationDTO.getEmail());
        // CRUCIAL: Codifica a senha antes de salvar!
        newUser.setSenha(passwordEncoder.encode(registrationDTO.getSenha()));
        // Por padrão, novos registros são do tipo USUARIO
        newUser.setRole(Role.ROLE_FORNECEDOR);

        User savedUser = userRepository.save(newUser);

        // 3. Mapeamento: Converte a entidade para um DTO de resposta
        return mapToResponseDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO registerAdmin(UserRegistrationDTO registrationDTO) {
        userRepository.findByEmail(registrationDTO.getEmail()).ifPresent(user -> {
            throw new EmailAlreadyExistsException("E-mail já cadastrado: " + registrationDTO.getEmail());
        });

        // 2. Lógica: Cria e salva o novo usuário
        User newUser = new User();
        newUser.setNome(registrationDTO.getNome());
        newUser.setEmail(registrationDTO.getEmail());
        // CRUCIAL: Codifica a senha antes de salvar!
        newUser.setSenha(passwordEncoder.encode(registrationDTO.getSenha()));
        // Por padrão, novos registros são do tipo USUARIO
        newUser.setRole(Role.ROLE_ADMIN);

        User savedUser = userRepository.save(newUser);

        // 3. Mapeamento: Converte a entidade para um DTO de resposta
        return mapToResponseDTO(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Método utilitário para mapear Entidade -> DTO
    private UserResponseDTO mapToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }


    @Transactional
    public UserResponseDTO updateUsuario(UserRegistrationDTO userRegistrationDTO, User currentUser) {
        User usuarioSecao = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + userRegistrationDTO.getEmail()));

        if (!usuarioSecao.getId().equals(currentUser.getId())) {
            throw new ForbiddenException("Você só pode alterar dados de sua conta");
        }

        if (userRepository.findByEmail(userRegistrationDTO.getEmail()).isPresent() && !userRegistrationDTO.getEmail().equals(currentUser.getEmail())) {
            throw new EmailAlreadyExistsException("Esse email " + userRegistrationDTO.getEmail() + " já está cadastrado");
        }


        usuarioSecao.setEmail(userRegistrationDTO.getEmail());
        usuarioSecao.setSenha(passwordEncoder.encode(userRegistrationDTO.getSenha()));
        usuarioSecao.setNome(userRegistrationDTO.getNome());

        userRepository.save(usuarioSecao);

        return this.mapToResponseDTO(usuarioSecao);
    }

    @Transactional
    public UserResponseDTO deleteUsuario(User currentUser) {

        User usuarioSecao = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + currentUser.getEmail()));

        userRepository.deleteByEmail(usuarioSecao.getEmail());

        return this.mapToResponseDTO(usuarioSecao);
    }



    public List<UserResponseDTO> listarTodosUsuariosInativos() {
        return userRepository.findAllDeleted().stream().map(this::mapToResponseDTO).toList();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + username));
    }
}
