package gerenciar.usuario.desafio.domain.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhone {
    String message() default "Telefone inválido. Use o formato +55 11 99999-9999";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}