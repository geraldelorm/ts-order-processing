package io.turntabl.tsops.ClientConnectivity.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class AuthErrorAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<Object> handleEmailAlreadyExist(EmailAlreadyExistException ex, WebRequest request) {
        return ResponseHandler
                .builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    @ExceptionHandler(InvalidEmailPasswordException.class)
    public ResponseEntity<Object> handleInvalidEmailPassword(InvalidEmailPasswordException ex, WebRequest request) {
        return ResponseHandler
                .builder()
                .message(ex.getMessage())
                .status(HttpStatus.FORBIDDEN)
                .build();
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Object> handleOrderNotFoundException(OrderNotFoundException ex, WebRequest request) {
        return ResponseHandler
                .builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(OrderNotOpenException.class)
    public ResponseEntity<Object> handleOrderNotOpenException(OrderNotOpenException ex, WebRequest request) {
        return ResponseHandler
                .builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_ACCEPTABLE)
                .build();
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NotNull HttpHeaders headers,
                                                                  @NotNull HttpStatus status, @NotNull WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors().stream()
                .map(err -> err.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseHandler
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .errors(errors)
                .build();
    }
}
