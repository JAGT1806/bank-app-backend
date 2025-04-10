package com.jagt.hexagonal.bankapp.common.infrastructure.web.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.jagt.hexagonal.bankapp.account.domain.exception.AccountNotActivatedException;
import com.jagt.hexagonal.bankapp.account.domain.exception.AccountNotFoundException;
import com.jagt.hexagonal.bankapp.client.domain.exception.AgeRestrictionException;
import com.jagt.hexagonal.bankapp.client.domain.exception.ClientHasAccountsException;
import com.jagt.hexagonal.bankapp.client.domain.exception.ClientNotFoundException;
import com.jagt.hexagonal.bankapp.client.domain.exception.InvalidBirthdayException;
import com.jagt.hexagonal.bankapp.common.infrastructure.web.response.ErrorResponse;
import com.jagt.hexagonal.bankapp.transaction.domain.exception.TransactionNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Cuenta no encontrada", ex.getMessage());
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClientNotFoundException(ClientNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Cliente no encontrado", ex.getMessage());
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransactionNotFoundException(TransactionNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Transacción no encontrada", ex.getMessage());
    }

    @ExceptionHandler(AgeRestrictionException.class)
    public ResponseEntity<ErrorResponse> handleAgeRestrictionException(AgeRestrictionException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Restricción de edad: El cliente debe tener al menos 18 años", ex.getMessage());
    }

    @ExceptionHandler(ClientHasAccountsException.class)
    public ResponseEntity<ErrorResponse> handleClientHasAccountsException(ClientHasAccountsException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "El cliente tiene cuentas asociadas", ex.getMessage());
    }

    @ExceptionHandler(AccountNotActivatedException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotActivatedException(AccountNotActivatedException ex) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Cuenta no activada", ex.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la base de datos", getSafeDatabaseErrorMessage(ex));
    }

    @ExceptionHandler(InvalidBirthdayException.class)
    public ResponseEntity<ErrorResponse> handleInvalidBirthdayException(InvalidBirthdayException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Fecha de nacimiento inválida: Debe ser una fecha en el pasado", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Datos de la solicitud inválidos", ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Operación inválida", ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();

        if(cause instanceof InvalidFormatException formatException) {
            if (formatException.getTargetType().isEnum()) {
                List<String> validValues = Stream.of(formatException.getTargetType().getEnumConstants())
                        .map(Object::toString)
                        .toList();

                String fieldName = formatException.getPath() != null && !formatException.getPath().isEmpty()
                        ? formatException.getPath().getFirst().getFieldName()
                        : "desconocido";

                String message = "Valor inválido para el campo: '" + fieldName + "'. Opciones válidas: " + validValues;
                return buildErrorResponse(HttpStatus.BAD_REQUEST, "Formato inválido", message);
            }
        }

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Error de formato", "Error en los datos enviados.");
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Error de validación", errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected system error", ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String error, String message) {
        return buildErrorResponse(status, error, message != null ? List.of(message) : List.of());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String error, List<String> messages) {
        ErrorResponse errorResponse = new ErrorResponse(
                String.valueOf(status.value()), error, messages, LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(errorResponse);
    }

    private String getSafeDatabaseErrorMessage(DataAccessException ex) {
        if (ex.getCause() != null) {
            String message = ex.getCause().getMessage();
            if (message.contains("Detail:")) {
                String detail = message.split("Detail:")[1].trim();
                int bracketIndex = detail.indexOf("]");
                if (bracketIndex != -1) {
                    detail = detail.substring(0, bracketIndex).trim();
                }
                return detail;
            }
        }
        return "Ha ocurrido un error interno en la base de datos";
    }
}
