package gerenciar.usuario.desafio.domain.usecase;

import gerenciar.usuario.desafio.adapter.input.dto.UserDTO;
import gerenciar.usuario.desafio.adapter.input.mapper.UserMapper;
import gerenciar.usuario.desafio.domain.entity.User;
import gerenciar.usuario.desafio.domain.entity.UserType;
import gerenciar.usuario.desafio.domain.exception.UserNotFoundException;
import gerenciar.usuario.desafio.port.output.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserUseCase useCase;

    private User user;
    private UserDTO dto;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Nome Teste", "email@teste.com", "+55 11 99999-0000", LocalDate.of(1990, 1, 1), UserType.ADMIN);
        dto = new UserDTO(null, "Nome Teste", "email@teste.com", "+55 11 99999-0000", LocalDate.of(1990, 1, 1), UserType.ADMIN);
    }

    @Test
    void testCreate() {
        when(repository.save(any(User.class))).thenReturn(user);

        UserDTO result = useCase.create(dto);

        assertNotNull(result);
        assertEquals(dto.getEmail(), result.getEmail());
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(List.of(user));

        List<UserDTO> result = useCase.findAll();

        assertEquals(1, result.size());
        assertEquals(user.getEmail(), result.get(0).getEmail());
    }

    @Test
    void testFindById_found() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = useCase.findById(1L);

        assertEquals(user.getFullName(), result.getFullName());
    }

    @Test
    void testFindById_notFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.findById(2L));
    }

    @Test
    void testFindByUserType() {
        when(repository.findByUserType(UserType.ADMIN)).thenReturn(List.of(user));

        List<UserDTO> result = useCase.findByUserType(UserType.ADMIN);

        assertEquals(1, result.size());
        assertEquals(UserType.ADMIN, result.get(0).getUserType());
    }

    @Test
    void testUpdate_found() {
        UserDTO newDto = new UserDTO(null, "Novo Nome", "novo@email.com", "+55 11 88888-0000", LocalDate.of(1991, 2, 2), UserType.EDITOR);

        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.save(any(User.class))).thenReturn(user);

        UserDTO result = useCase.update(1L, newDto);

        assertEquals("novo@email.com", result.getEmail());
        assertEquals("Novo Nome", result.getFullName());
    }

    @Test
    void testUpdate_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.update(1L, dto));
    }

    @Test
    void testDelete_found() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        assertDoesNotThrow(() -> useCase.delete(1L));
    }

    @Test
    void testDelete_notFound() {
        when(repository.existsById(2L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> useCase.delete(2L));
    }
}
