package dev.atim0r.taskflow.config;

import dev.atim0r.taskflow.task.TaskNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            TaskNotFoundException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.of(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request.getRequestURI()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        Map<String, String> fields = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> fields.put(error.getField(), error.getDefaultMessage()));

        ApiError error = ApiError.of(
                HttpStatus.BAD_REQUEST,
                "Request contains invalid fields",
                request.getRequestURI()
        );
        error.setDetails(fields);
        return ResponseEntity.badRequest().body(error);
    }

    public static class ApiError {
        private final Instant timestamp = Instant.now();
        private int status;
        private String error;
        private String message;
        private String path;
        private Map<String, String> details;

        static ApiError of(HttpStatus status, String message, String path) {
            ApiError apiError = new ApiError();
            apiError.status = status.value();
            apiError.error = status.getReasonPhrase();
            apiError.message = message;
            apiError.path = path;
            return apiError;
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public int getStatus() {
            return status;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }

        public String getPath() {
            return path;
        }

        public Map<String, String> getDetails() {
            return details;
        }

        public void setDetails(Map<String, String> details) {
            this.details = details;
        }
    }
}
