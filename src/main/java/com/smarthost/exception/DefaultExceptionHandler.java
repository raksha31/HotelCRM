package com.smarthost.exception;

import com.smarthost.dto.ExceptionResponseDTO;
import com.smarthost.exception.custom.FileEmptyException;
import com.smarthost.exception.custom.InvalidInputException;
import com.smarthost.exception.custom.NoAvailableRoomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
@Component("ServiceExceptionHandlerAdvice")
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    private static final long serialVersionUID = -8704528029951956646L;

    private ResponseEntity<Object> errorResponse(HttpStatus status, HttpHeaders headers, String message) {
        final ExceptionResponseDTO apiError = new ExceptionResponseDTO(message, status.value());
        return ResponseEntity.status(status).headers(headers).body(apiError);
    }

    @ExceptionHandler(NoAvailableRoomException.class)
    final ResponseEntity<Object> handleNoAvailableRoomException(NoAvailableRoomException ex) {
        return errorResponse(HttpStatus.BAD_REQUEST, new HttpHeaders(), "No Room Available");
    }

    @ExceptionHandler(InvalidInputException.class)
    final ResponseEntity<Object> handleInvalidInputException(InvalidInputException ex) {
        return errorResponse(HttpStatus.NOT_ACCEPTABLE, new HttpHeaders(), "Rooms cannot be negative");
    }

    @ExceptionHandler(FileEmptyException.class)
    final ResponseEntity<Object> handleFileEmptyException(FileEmptyException ex) {
        return errorResponse(HttpStatus.NO_CONTENT, new HttpHeaders(), "Input file is empty");
    }

    @ExceptionHandler(IOException.class)
    final ResponseEntity<Object> handleIOException(FileEmptyException ex) {
        return errorResponse(HttpStatus.NO_CONTENT, new HttpHeaders(), "Exception while reading the file");
    }
}
