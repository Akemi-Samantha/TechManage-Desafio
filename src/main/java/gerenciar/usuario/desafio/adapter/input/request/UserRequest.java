package gerenciar.usuario.desafio.adapter.input.request;

import gerenciar.usuario.desafio.domain.entity.UserType;
import gerenciar.usuario.desafio.domain.utils.ValidEnum;
import gerenciar.usuario.desafio.domain.utils.ValidPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "Nome completo é obrigatório")
    private String fullName;

    @Email(message = "Formato de e-mail inválido. Ex: exemplo@dominio.com")
    @NotBlank(message = "E-mail é obrigatório")
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    @ValidPhone // Validação personalizada, estilo do seu exemplo
    private String phone;

    @NotNull(message = "Data de nascimento é obrigatória")
    private LocalDate birthDate;

    @NotNull(message = "Tipo de usuário é obrigatório")
    @ValidEnum(enumClass = UserType.class, message = "Tipo de usuário inválido. Utilize estes usuários para o cadastro(ADMIN, EDITOR, VIEWER)")
    private String userType;
}