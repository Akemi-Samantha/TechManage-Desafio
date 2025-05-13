package gerenciar.usuario.desafio.adapter.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import gerenciar.usuario.desafio.domain.exception.ApiException;
import gerenciar.usuario.desafio.domain.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiException> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, HttpServletRequest request) {

        String mensagem = "Violação de integridade. Verifique se os dados informados já existem.";

        if (ex.getRootCause() != null && ex.getRootCause().getMessage().toLowerCase().contains("email")) {
            mensagem = "Já existe um usuário cadastrado com este e-mail.";
        }

        ApiException error = new ApiException(
                mensagem,
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiException> handleUsuarioNaoEncontrado(
            UserNotFoundException ex, HttpServletRequest request) {

        ApiException error = new ApiException(
                ex.getMessage(),
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiException> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getField(),
                        e -> e.getDefaultMessage(),
                        (existing, replacement) -> existing
                ));

        ApiException error = new ApiException(
                "Erros de validação",
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ApiException> handleInvalidFormatException(
            InvalidFormatException ex, HttpServletRequest request) {

        String fieldName = "";
        String acceptedValues = "";
        Map<String, String> errors = new HashMap<>();

        if (ex.getTargetType().isEnum()) {
            fieldName = ex.getPath().get(0).getFieldName();
            Object[] enumConstants = ex.getTargetType().getEnumConstants();
            acceptedValues = Arrays.toString(enumConstants);

            errors.put(fieldName, "Valor inválido. Valores aceitos: " + acceptedValues);
        }

        ApiException error = new ApiException(
                "Erro de formatação de campo",
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                errors.isEmpty() ? null : errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiException> handleEnumTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String mensagem = "Parâmetro inválido.";

        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
            String field = ex.getName();
            Object[] enumValues = ex.getRequiredType().getEnumConstants();
            mensagem = String.format("Tipo de usuário inválido. Valores aceitos: %s", Arrays.toString(enumValues));
        }

        ApiException error = new ApiException(
                mensagem,
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
