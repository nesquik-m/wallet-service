package com.example.exception;

import com.example.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, HttpStatus status, ErrorCode code, String message) {
        log.error(ex.getMessage(), ex);
        ErrorResponse.Error error = new ErrorResponse.Error(code.name(), message);
        return ResponseEntity.status(status).body(new ErrorResponse(error));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFound(EntityNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, ErrorCode.OBJECT_NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    public ResponseEntity<ErrorResponse> notEnoughBalance(NotEnoughBalanceException ex) {
        return buildErrorResponse(ex, HttpStatus.PAYMENT_REQUIRED, ErrorCode.NOT_ENOUGH_BALANCE, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> unknownType(HttpMessageNotReadableException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, ErrorCode.UNKNOWN_OPERATION_TYPE,
                "Invalid operation type. Allowed values: WITHDRAW, DEPOSIT.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> notValid(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errorMessages = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        String errorMessage = String.join("; ", errorMessages);

        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, errorMessage);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorResponse {

        private Error error;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Error {

            private String code;

            private String message;

        }
    }
}