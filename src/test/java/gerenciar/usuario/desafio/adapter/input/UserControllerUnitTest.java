package gerenciar.usuario.desafio.adapter.input;

import gerenciar.usuario.desafio.adapter.input.dto.UserDTO;
import gerenciar.usuario.desafio.adapter.input.mapper.UserMapper;
import gerenciar.usuario.desafio.adapter.input.request.UserRequest;
import gerenciar.usuario.desafio.adapter.input.response.UserResponse;

import gerenciar.usuario.desafio.domain.entity.UserType;
import gerenciar.usuario.desafio.port.input.IUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerUnitTest {

    private IUserUseCase userUseCase;
    private UserController controller;

    @BeforeEach
    void setup() {
        userUseCase = mock(IUserUseCase.class);
        controller = new UserController(userUseCase, null); // mapper via static
    }

    @Test
    void testCreateUser() {
        UserRequest request = new UserRequest();
        request.setFullName("João");
        request.setEmail("joao@email.com");
        request.setPhone("+5511999999999");
        request.setUserType(String.valueOf(UserType.ADMIN));

        UserDTO dto = new UserDTO();
        dto.setFullName(request.getFullName());
        dto.setEmail(request.getEmail());
        dto.setPhone(request.getPhone());
        dto.setUserType(UserType.valueOf(request.getUserType()));

        UserDTO created = new UserDTO();
        created.setId(1L);
        created.setFullName(dto.getFullName());
        created.setEmail(dto.getEmail());
        created.setPhone(dto.getPhone());
        created.setUserType(dto.getUserType());

        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setFullName(created.getFullName());
        response.setEmail(created.getEmail());
        response.setPhone(created.getPhone());
        response.setUserType(created.getUserType());

        when(userUseCase.create(dto)).thenReturn(created);

        try (MockedStatic<UserMapper> mocked = mockStatic(UserMapper.class)) {
            mocked.when(() -> UserMapper.toDTO(request)).thenReturn(dto);
            mocked.when(() -> UserMapper.toResponse(created)).thenReturn(response);

            var result = controller.createUser(request);
            assertEquals(201, result.getStatusCodeValue());

            var body = result.getBody();
            assertNotNull(body);
            assertEquals("Usuário cadastrado com sucesso!", body.get("message"));

            UserResponse resultUser = (UserResponse) body.get("result");
            assertEquals("João", resultUser.getFullName());
        }
    }

    @Test
    void testGetAllUsers() {
        UserDTO dto1 = new UserDTO();
        dto1.setId(1L);
        dto1.setFullName("João");
        dto1.setEmail("joao@email.com");
        dto1.setPhone("+5511999999999");
        dto1.setUserType(UserType.ADMIN);

        UserDTO dto2 = new UserDTO();
        dto2.setId(2L);
        dto2.setFullName("Maria");
        dto2.setEmail("maria@email.com");
        dto2.setPhone("+5511988888888");
        dto2.setUserType(UserType.ADMIN);

        UserResponse resp1 = new UserResponse();
        resp1.setId(1L);
        resp1.setFullName(dto1.getFullName());
        resp1.setEmail(dto1.getEmail());
        resp1.setPhone(dto1.getPhone());
        resp1.setUserType(dto1.getUserType());

        UserResponse resp2 = new UserResponse();
        resp2.setId(2L);
        resp2.setFullName(dto2.getFullName());
        resp2.setEmail(dto2.getEmail());
        resp2.setPhone(dto2.getPhone());
        resp2.setUserType(dto2.getUserType());

        when(userUseCase.findAll()).thenReturn(List.of(dto1, dto2));

        try (MockedStatic<UserMapper> mocked = mockStatic(UserMapper.class)) {
            mocked.when(() -> UserMapper.toResponse(dto1)).thenReturn(resp1);
            mocked.when(() -> UserMapper.toResponse(dto2)).thenReturn(resp2);

            var result = controller.getAllUsers();
            assertEquals(200, result.getStatusCodeValue());
            var body = result.getBody();
            assertEquals(2, body.size());
            assertEquals("João", body.get(0).getFullName());
            assertEquals("Maria", body.get(1).getFullName());
        }
    }

    @Test
    void testGetUserById() {
        Long id = 1L;
        UserDTO dto = new UserDTO();
        dto.setId(id);
        dto.setFullName("João");

        UserResponse resp = new UserResponse();
        resp.setId(id);
        resp.setFullName("João");

        when(userUseCase.findById(id)).thenReturn(dto);

        try (MockedStatic<UserMapper> mocked = mockStatic(UserMapper.class)) {
            mocked.when(() -> UserMapper.toResponse(dto)).thenReturn(resp);
            var result = controller.getUserById(id);
            assertEquals(200, result.getStatusCodeValue());
            assertEquals("João", result.getBody().getFullName());
        }
    }

    @Test
    void testGetUsersByType() {
        UserType type = UserType.ADMIN;

        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setFullName("João");
        dto.setUserType(type);

        UserResponse resp = new UserResponse();
        resp.setId(1L);
        resp.setEmail("João");
        resp.setUserType(type);

        when(userUseCase.findByUserType(type)).thenReturn(List.of(dto));

        try (MockedStatic<UserMapper> mocked = mockStatic(UserMapper.class)) {
            mocked.when(() -> UserMapper.toResponse(dto)).thenReturn(resp);
            var result = controller.getUsersByType(type);
            assertEquals(200, result.getStatusCodeValue());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void testUpdateUser() {
        Long id = 1L;

        UserRequest request = new UserRequest();
        request.setFullName("Novo Nome");
        request.setEmail("novo@email.com");
        request.setPhone("+5511911111111");
        request.setUserType(String.valueOf(UserType.ADMIN));

        UserDTO dto = new UserDTO();
        dto.setId(id);
        dto.setFullName(request.getFullName());
        dto.setEmail(request.getEmail());
        dto.setPhone(request.getPhone());
        dto.setUserType(UserType.valueOf(request.getUserType()));

        UserResponse response = new UserResponse();
        response.setId(id);
        response.setFullName(dto.getFullName());
        response.setEmail(dto.getEmail());
        response.setPhone(dto.getPhone());
        response.setUserType(dto.getUserType());

        when(userUseCase.update(id, dto)).thenReturn(dto);

        try (MockedStatic<UserMapper> mocked = mockStatic(UserMapper.class)) {
            mocked.when(() -> UserMapper.toDTO(request)).thenReturn(dto);
            mocked.when(() -> UserMapper.toResponse(dto)).thenReturn(response);

            var result = controller.updateUser(id, request);
            assertEquals(200, result.getStatusCodeValue());

            var body = result.getBody();
            assertEquals("Usuário atualizado com sucesso!", body.get("message"));

            UserResponse resultUser = (UserResponse) body.get("result");
            assertEquals("Novo Nome", resultUser.getFullName());
        }
    }

    @Test
    void testDeleteUser() {
        Long id = 1L;
        doNothing().when(userUseCase).delete(id);

        var result = controller.deleteUser(id);
        assertEquals(204, result.getStatusCodeValue());
        assertNull(result.getBody());
    }
}
