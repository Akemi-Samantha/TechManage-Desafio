package gerenciar.usuario.desafio.domain.usecase;

import gerenciar.usuario.desafio.adapter.input.dto.UserDTO;
import gerenciar.usuario.desafio.adapter.input.mapper.UserMapper;
import gerenciar.usuario.desafio.domain.entity.User;
import gerenciar.usuario.desafio.domain.entity.UserType;
import gerenciar.usuario.desafio.domain.exception.UserNotFoundException;
import gerenciar.usuario.desafio.port.input.IUserUseCase;
import gerenciar.usuario.desafio.port.output.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserUseCase implements IUserUseCase {

    private final UserRepository repository;

    @Override
    public UserDTO create(UserDTO dto) {
        User user = UserMapper.toEntity(dto);
        repository.save(user);
        return UserMapper.toDTO(user);
    }

    @Override
    public List<UserDTO> findAll() {
        return repository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        return UserMapper.toDTO(user);
    }

    @Override
    public List<UserDTO> findByUserType(UserType userType) {
        return repository.findByUserType(userType)
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO update(Long id, UserDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setBirthDate(dto.getBirthDate());
        user.setUserType(dto.getUserType());

        repository.save(user);
        return UserMapper.toDTO(user);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException("Usuário não encontrado");
        }
        repository.deleteById(id);
    }
}
