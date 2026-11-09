package me.satyajit.gatekeeper.management.exception;


import jakarta.servlet.http.HttpServletRequest;
import me.satyajit.gatekeeper.management.dto.exception.ExceptionResponseDto;
import me.satyajit.gatekeeper.management.dto.exception.ValidationExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionResponseDto> validationExceptionHandler(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ){

        Map<String, String> fieldErrors = new HashMap<>();

        e.getBindingResult().getFieldErrors()
                .forEach(error ->
                        fieldErrors.put(
                        error.getField(),
                        error.getDefaultMessage()
                )
        );

        ValidationExceptionResponseDto response = new ValidationExceptionResponseDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation Failed",
                request.getRequestURI(),
                fieldErrors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> resourceNotFoundExceptionHandler(
            ResourceNotFoundException e,
            HttpServletRequest request
    ){

        ExceptionResponseDto response = new ExceptionResponseDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionResponseDto> invalidCredentialsExceptionHandler(
            InvalidCredentialsException e,
            HttpServletRequest request
    ){

        ExceptionResponseDto response = new ExceptionResponseDto(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ExceptionResponseDto> apiExceptionHandler(
            ApiException e,
            HttpServletRequest request
    ){
        ExceptionResponseDto response = new ExceptionResponseDto(
                LocalDateTime.now(),
                e.getHttpStatus().value(),
                e.getHttpStatus().getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(response);
    }

}
