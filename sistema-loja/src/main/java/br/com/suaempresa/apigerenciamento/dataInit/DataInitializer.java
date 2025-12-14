package br.com.suaempresa.apigerenciamento.dataInit;

import br.com.suaempresa.apigerenciamento.user.model.Role;
import br.com.suaempresa.apigerenciamento.user.model.User;
import br.com.suaempresa.apigerenciamento.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        String adminEmail = "admin@suaempresa.com";

        if (userRepository.findByEmail(adminEmail).isEmpty()) {

            User admin = new User();
            admin.setNome("Admin System");
            admin.setEmail(adminEmail);

            admin.setSenha(passwordEncoder.encode("admin123"));

            admin.setRole(Role.ROLE_ADMIN);

            admin.setDeleted(false);

            userRepository.save(admin);

            System.out.println("---------------------------------");
            System.out.println("ADMIN USER CREATED SUCCESSFULLY");
            System.out.println("---------------------------------");
        } else {
            System.out.println("Admin user already exists. Skipping creation.");
        }
    }
}