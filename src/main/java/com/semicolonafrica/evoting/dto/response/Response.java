package com.semicolonafrica.evoting.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Response {
    private HttpStatus status;
    private String message;
    private Long id;
}
