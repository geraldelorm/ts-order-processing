package io.turntabl.tsops.ClientConnectivity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseHandler {
    private Object data;
    private Object errors;
    private HttpStatus status;
    private String message;
    private final String timeStamp = LocalDateTime.now().toString();

    private Object getErrors() {
        return errors;
    }

    private HttpStatus getStatus() {
        return status;
    }

    private String getMessage() {
        return message;
    }

    private String getTimeStamp() {
        return timeStamp;
    }

    private Object getData() {
        return data;
    }

    private ResponseHandler() {}

    public static ResponseHandler builder() {
       return new ResponseHandler();
   }

    public ResponseHandler message(String message) {
        this.message = message;
        return this;
    }

    public ResponseHandler status(HttpStatus status) {
        this.status = status;
        return this;
    }

   public ResponseHandler data(Object data) {
       this.data = data;
       return this;
   }

   public ResponseHandler errors(Object errors) {
       this.errors = errors;
       return this;
   }

   public ResponseEntity<Object> build() {
       Map<String, Object> customData = new LinkedHashMap<>();
        customData.put("data", getData());
        customData.put("errors", getErrors());
        customData.put("status", getStatus());
        customData.put("message", getMessage());
        customData.put("timestamp", getTimeStamp());
        return new ResponseEntity<>(customData, getStatus());
   }
}
