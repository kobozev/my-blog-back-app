package ru.practicum.kobozevva.blog.controller.advice;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.kobozevva.blog.dto.error.ErrorResponseDto;
import ru.practicum.kobozevva.blog.exception.CommentNotFoundException;
import ru.practicum.kobozevva.blog.exception.ImageProcessingException;
import ru.practicum.kobozevva.blog.exception.PostNotFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponseDto buildError(HttpStatus status, String message, HttpServletRequest request) {
        return new ErrorResponseDto(
                status.value(),
                message,
                request.getRequestURI(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {

        String message = "Invalid value for parameter '" +
                ex.getName() + "': " + ex.getValue();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, message, request));
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            ValidationException.class
    })
    public ResponseEntity<ErrorResponseDto> handleBadRequest(
            RuntimeException ex,
            HttpServletRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request));
    }

    @ExceptionHandler({
            PostNotFoundException.class,
            CommentNotFoundException.class
    })
    public ResponseEntity<ErrorResponseDto> handleNotFound(
            RuntimeException ex,
            HttpServletRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request));
    }

    @ExceptionHandler(ImageProcessingException.class)
    public ResponseEntity<ErrorResponseDto> handleImageProcessing(
            ImageProcessingException ex,
            HttpServletRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleInternal(
            Exception ex,
            HttpServletRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Internal server error",
                        request
                ));
    }
}