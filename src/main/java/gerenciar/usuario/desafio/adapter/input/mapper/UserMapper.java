package gerenciar.usuario.desafio.adapter.input.mapper;

import gerenciar.usuario.desafio.adapter.input.dto.UserDTO;
import gerenciar.usuario.desafio.adapter.input.request.UserRequest;
import gerenciar.usuario.desafio.adapter.input.response.UserResponse;
import gerenciar.usuario.desafio.domain.entity.User;
import gerenciar.usuario.desafio.domain.entity.UserType;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserDTO toDTO(UserRequest request) {
        return UserDTO.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .birthDate(request.getBirthDate())
                .userType(UserType.valueOf(request.getUserType()))
                .build();
    }

    public static User toEntity(UserDTO dto) {
        return User.builder()
                .id(dto.getId())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .birthDate(dto.getBirthDate())
                .userType(dto.getUserType())
                .build();
    }

    public static UserDTO toDTO(User entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .birthDate(entity.getBirthDate())
                .userType(entity.getUserType())
                .build();
    }

    public static UserResponse toResponse(UserDTO dto) {
        return UserResponse.builder()
                .id(dto.getId())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .birthDate(dto.getBirthDate())
                .userType(dto.getUserType())
                .build();
    }
}

