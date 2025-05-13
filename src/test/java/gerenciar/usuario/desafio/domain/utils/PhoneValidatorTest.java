package gerenciar.usuario.desafio.domain.utils;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class PhoneValidatorTest {

    private static Validator validator;

    static class PhoneTestClass {
        @ValidPhone
        private String phone;

        public PhoneTestClass(String phone) {
            this.phone = phone;
        }
    }

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidPhone() {
        var obj = new PhoneTestClass("+55 11 99999-9999");
        var violations = validator.validate(obj);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testNullPhone() {
        var obj = new PhoneTestClass(null);
        var violations = validator.validate(obj);
        assertFalse(violations.isEmpty());
        assertEquals("Telefone não pode ser nulo ou vazio.", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidFormat() {
        var obj = new PhoneTestClass("11999999999");
        var violations = validator.validate(obj);
        assertFalse(violations.isEmpty());
        assertEquals("Formato inválido. Use: +55 11 99999-9999", violations.iterator().next().getMessage());
    }

}
