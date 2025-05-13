package gerenciar.usuario.desafio.adapter.input;

import gerenciar.usuario.desafio.adapter.input.mapper.UserMapper;
import gerenciar.usuario.desafio.adapter.input.request.UserRequest;
import gerenciar.usuario.desafio.adapter.input.response.UserResponse;
import gerenciar.usuario.desafio.domain.entity.UserType;
import gerenciar.usuario.desafio.port.input.IUserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserUseCase userUseCase;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody @Valid UserRequest request) {
        var dto = userMapper.toDTO(request);
        var created = userUseCase.create(dto);
        var response = new HashMap<String, Object>();
        response.put("message", "Usuário cadastrado com sucesso!");
        response.put("result", UserMapper.toResponse(created));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        var listDTO = userUseCase.findAll();
        var responseList = listDTO.stream()
                .map(UserMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        var dto = userUseCase.findById(id);
        var response = UserMapper.toResponse(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<UserResponse>> getUsersByType(@RequestParam("type") UserType userType) {
        var listDTO = userUseCase.findByUserType(userType);
        var responseList = listDTO.stream()
                .map(UserMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable @Valid Long id,
            @RequestBody @Valid UserRequest request) {

        var dto = UserMapper.toDTO(request);
        dto.setId(id);
        var updated = userUseCase.update(id, dto);
        var response = new HashMap<String, Object>();
        response.put("message", "Usuário atualizado com sucesso!");
        response.put("result", UserMapper.toResponse(updated));
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

}
