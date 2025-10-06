package br.com.suaempresa.apigerenciamento.user.repository;

import br.com.suaempresa.apigerenciamento.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Método essencial para o Spring Security encontrar o usuário pelo e-mail
    Optional<User> findByEmail(String email);

    Optional<User> deleteByEmail(String email);

    // Este método IGNORA a cláusula @Where global porque a consulta é explícita.
    @Query("SELECT u FROM User u WHERE u.deleted = true")
    List<User> findAllDeleted();

    // Uma consulta para buscar TUDO, ignorando o filtro.
    @Query(value = "SELECT * FROM Users", nativeQuery = true)
    List<User> findAllIncludingDeleted();

}
