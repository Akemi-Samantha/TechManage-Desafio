package gerenciar.usuario.desafio.port.input;

import gerenciar.usuario.desafio.adapter.input.dto.UserDTO;
import gerenciar.usuario.desafio.domain.entity.UserType;

import java.util.List;

public interface IUserUseCase {

    UserDTO create(UserDTO dto);
    List<UserDTO> findAll();
    UserDTO findById(Long id);
    UserDTO update(Long id, UserDTO dto);
    void delete(Long id);
    List<UserDTO> findByUserType(UserType userType);
}
