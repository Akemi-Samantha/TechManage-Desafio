package gerenciar.usuario.desafio.port.output;

import gerenciar.usuario.desafio.domain.entity.User;
import gerenciar.usuario.desafio.domain.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByUserType(UserType userType);

}
