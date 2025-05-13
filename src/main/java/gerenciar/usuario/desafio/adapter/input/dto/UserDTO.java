package gerenciar.usuario.desafio.adapter.input.dto;

import gerenciar.usuario.desafio.domain.entity.UserType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private UserType userType;
}