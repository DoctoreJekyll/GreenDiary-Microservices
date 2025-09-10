package org.generations.commonlib.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ApiError {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private Map<String,String> fieldErrors;//Para errores de validadcion de campo

    public ApiError(){
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(int status, String error, String message, String path, Map<String,String> fieldErrors) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.fieldErrors = fieldErrors;
    }

}
