package gerenciar.usuario.desafio.domain.usecase;

import gerenciar.usuario.desafio.adapter.input.dto.UserDTO;
import gerenciar.usuario.desafio.domain.entity.UserType;
import gerenciar.usuario.desafio.domain.exception.UserNotFoundException;
import gerenciar.usuario.desafio.port.input.IUserUseCase;
import gerenciar.usuario.desafio.port.output.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class IntegrationTest {

    @Autowired
    private IUserUseCase userUseCase;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreateUser() {
        // Arrange
        UserDTO dto = new UserDTO();
        dto.setFullName("Maria Silva");
        dto.setEmail("maria@email.com");
        dto.setPhone("+55 11999999999");
        dto.setBirthDate(LocalDate.of(1990, 5, 10));
        dto.setUserType(UserType.ADMIN);

        // Act
        UserDTO result = userUseCase.create(dto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Maria Silva", result.getFullName());
    }

    @Test
    void testFindAllUsers() {
        // Arrange – cria dois usuários
        UserDTO dto1 = new UserDTO();
        dto1.setFullName("João Oliveira");
        dto1.setEmail("joao@email.com");
        dto1.setPhone("+55 11 91111-1111");
        dto1.setBirthDate(LocalDate.of(1985, 3, 20));
        dto1.setUserType(UserType.ADMIN);

        UserDTO dto2 = new UserDTO();
        dto2.setFullName("Ana Souza");
        dto2.setEmail("ana@email.com");
        dto2.setPhone("+55 11 92222-2222");
        dto2.setBirthDate(LocalDate.of(1992, 8, 15));
        dto2.setUserType(UserType.ADMIN);

        userUseCase.create(dto1);
        userUseCase.create(dto2);

        // Act – busca todos os usuários
        List<UserDTO> allUsers = userUseCase.findAll();

        // Assert – verifica se os dois estão na lista
        assertEquals(2, allUsers.size());

        List<String> nomes = allUsers.stream()
                .map(UserDTO::getFullName)
                .collect(Collectors.toList());

        assertTrue(nomes.contains("João Oliveira"));
        assertTrue(nomes.contains("Ana Souza"));
    }

    @Test
    void testFindUserById() {
        // Arrange
        UserDTO dto = new UserDTO();
        dto.setFullName("Carlos Dias");
        dto.setEmail("carlos@email.com");
        dto.setPhone("+55 11 93333-3333");
        dto.setBirthDate(LocalDate.of(1980, 1, 1));
        dto.setUserType(UserType.ADMIN);

        UserDTO created = userUseCase.create(dto);

        // Act
        UserDTO result = userUseCase.findById(created.getId());

        // Assert
        assertNotNull(result);
        assertEquals("Carlos Dias", result.getFullName());
        assertEquals("carlos@email.com", result.getEmail());
    }

    @Test
    void testFindUserByType() {
        // Arrange – cria 2 usuários EDITOR e 1 ADMIN
        UserDTO dto1 = new UserDTO();
        dto1.setFullName("Lucas Oliveira");
        dto1.setEmail("lucas@email.com");
        dto1.setPhone("+55 11912345678");
        dto1.setBirthDate(LocalDate.of(1990, 4, 12));
        dto1.setUserType(UserType.EDITOR);

        UserDTO dto2 = new UserDTO();
        dto2.setFullName("Ana Paula");
        dto2.setEmail("ana@email.com");
        dto2.setPhone("+55 11987654321");
        dto2.setBirthDate(LocalDate.of(1985, 10, 2));
        dto2.setUserType(UserType.EDITOR);

        UserDTO dto3 = new UserDTO();
        dto3.setFullName("Rafael Lima");
        dto3.setEmail("rafael@email.com");
        dto3.setPhone("+55 11955556666");
        dto3.setBirthDate(LocalDate.of(1992, 6, 22));
        dto3.setUserType(UserType.ADMIN);

        userUseCase.create(dto1);
        userUseCase.create(dto2);
        userUseCase.create(dto3);

        // Act – busca apenas os usuários do tipo EDITOR
        List<UserDTO> editorUsers = userUseCase.findByUserType(UserType.EDITOR);

        // Assert
        assertEquals(2, editorUsers.size());

        List<String> names = editorUsers.stream()
                .map(UserDTO::getFullName)
                .toList();

        assertTrue(names.contains("Lucas Oliveira"));
        assertTrue(names.contains("Ana Paula"));
        assertFalse(names.contains("Rafael Lima"));
    }



    @Test
    void testUpdateUser() {
        // Arrange
        UserDTO dto = new UserDTO();
        dto.setFullName("Paula Mendes");
        dto.setEmail("paula@email.com");
        dto.setPhone("+55 11 94444-4444");
        dto.setBirthDate(LocalDate.of(1995, 7, 5));
        dto.setUserType(UserType.ADMIN);

        UserDTO created = userUseCase.create(dto);

        // Atualiza os dados
        created.setFullName("Paula M. Mendes");
        created.setPhone("+55 11 95555-5555");

        // Act
        UserDTO updated = userUseCase.update(created.getId(), created);

        // Assert
        assertNotNull(updated);
        assertEquals("Paula M. Mendes", updated.getFullName());
        assertEquals("+55 11 95555-5555", updated.getPhone());

    }

    @Test
    void testDeleteUser() {
        // Arrange
        UserDTO dto = new UserDTO();
        dto.setFullName("Luciana Braga");
        dto.setEmail("luciana@email.com");
        dto.setPhone("+55 11 96666-6666");
        dto.setBirthDate(LocalDate.of(1988, 12, 15));
        dto.setUserType(UserType.ADMIN);

        UserDTO created = userUseCase.create(dto);

        // Act
        userUseCase.delete(created.getId());

        // Assert
        assertFalse(userRepository.existsById(created.getId()));
    }

    @Test
    void testFindById_userNotFound_shouldThrowException() {
        Long nonexistentId = 999L;

        assertThrows(UserNotFoundException.class, () -> {
            userUseCase.findById(nonexistentId);
        });
    }

    @Test
    void testUpdate_userNotFound_shouldThrowException() {
        Long nonexistentId = 999L;

        UserDTO dto = new UserDTO();
        dto.setFullName("Usuário Falso");
        dto.setEmail("falso@email.com");
        dto.setPhone("+55 11900000000");
        dto.setBirthDate(LocalDate.of(1999, 1, 1));
        dto.setUserType(UserType.VIEWER);

        assertThrows(UserNotFoundException.class, () -> {
            userUseCase.update(nonexistentId, dto);
        });
    }

    @Test
    void testDelete_userNotFound_shouldThrowException() {
        Long nonexistentId = 999L;

        assertThrows(UserNotFoundException.class, () -> {
            userUseCase.delete(nonexistentId);
        });
    }








}
