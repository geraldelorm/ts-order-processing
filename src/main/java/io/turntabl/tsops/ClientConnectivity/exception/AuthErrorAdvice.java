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

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class AuthErrorAdvice  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<Object> handleEmailAlreadyExist(EmailAlreadyExistException ex, WebRequest request) {
       Map<String, Object> responseData = new LinkedHashMap<>();
       responseData.put("timestamp", LocalDateTime.now().toString());
       responseData.put("status", HttpStatus.BAD_REQUEST);
       responseData.put("message", ex.getMessage());

       return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEmailPasswordException.class)
    public ResponseEntity<Object> handleInvalidEmailPassword(InvalidEmailPasswordException ex, WebRequest request) {
        Map<String, Object> responseData = new LinkedHashMap<>();
        responseData.put("timestamp", LocalDateTime.now().toString());
        responseData.put("status", HttpStatus.FORBIDDEN);
        responseData.put("message", ex.getMessage());

        return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NotNull HttpHeaders headers,
                                                                  @NotNull HttpStatus status, @NotNull WebRequest request) {
       List<String> errors = ex.getBindingResult()
               .getAllErrors().stream()
               .map(err -> err.getDefaultMessage())
               .collect(Collectors.toList());

        Map<String, Object> responseData = new LinkedHashMap<>();
        responseData.put("timestamp", LocalDateTime.now().toString());
        responseData.put("status", status);
        responseData.put("errors", errors);

        return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
    }
}
