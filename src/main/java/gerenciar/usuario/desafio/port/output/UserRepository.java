package gerenciar.usuario.desafio.port.output;

import gerenciar.usuario.desafio.domain.entity.User;
import gerenciar.usuario.desafio.domain.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> findByUserType(UserType userType);

}
