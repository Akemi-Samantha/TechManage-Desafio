package gerenciar.usuario.desafio.domain.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private static final String REGEX = "^\\+\\d{1,3}\\s\\d{2}\\s\\d{4,5}-\\d{4}$";

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isBlank()) {
            buildConstraintViolation(context, "Telefone não pode ser nulo ou vazio.");
            return false;
        }

        if (!phone.matches(REGEX)) {
            buildConstraintViolation(context, "Formato inválido. Use: +55 11 99999-9999");
            return false;
        }

        if (phone.length() < 16 || phone.length() > 20) {
            buildConstraintViolation(context, "Tamanho inválido para telefone. Verifique se está no padrão internacional.");
            return false;
        }

        return true;
    }

    private void buildConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
