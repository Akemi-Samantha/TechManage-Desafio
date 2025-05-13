package gerenciar.usuario.desafio.domain.utils;


import gerenciar.usuario.desafio.domain.entity.UserType;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumValidatorTest {

    private static Validator validator;

    static class EnumTestClass {
        @ValidEnum(enumClass = UserType.class, message = "Tipo de usuário inválido.")
        private String userType;

        public EnumTestClass(String userType) {
            this.userType = userType;
        }
    }

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidEnum() {
        var obj = new EnumTestClass("ADMIN");
        var violations = validator.validate(obj);
        assertTrue(violations.isEmpty());
    }


    @Test
    void testInvalidEnum() {
        var obj = new EnumTestClass("INVALIDO");
        var violations = validator.validate(obj);
        assertFalse(violations.isEmpty());
        assertEquals("Tipo de usuário inválido.", violations.iterator().next().getMessage());
    }

    @Test
    void testNullEnum() {
        var obj = new EnumTestClass(null);
        var violations = validator.validate(obj);
        assertFalse(violations.isEmpty());
        assertEquals("Tipo de usuário inválido.", violations.iterator().next().getMessage());
    }
}
