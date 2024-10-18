package org.eightbit.damdda.common.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eightbit.damdda.common.exception.custom.UnauthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * 공통적으로 ResponseEntity를 생성하는 메서드.
     * HTTP 상태 코드와 메시지를 받아 ResponseEntity를 생성하여 반환.
     *
     * @param status HTTP 상태 코드.
     * @param message 반환할 예외 메시지.
     * @return HTTP 상태 코드와 메시지를 담은 ResponseEntity.
     */
    private ResponseEntity<String> buildResponseEntity(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(message);
    }

    /**
     * NoSuchElementException 예외를 처리하는 메서드.
     * 주로 데이터 조회 시 해당 요소를 찾지 못했을 때 발생하며,
     * HTTP 404 상태 코드를 반환.
     *
     * @param ex 처리할 NoSuchElementException 예외 객체.
     * @return HTTP 상태 코드 404와 예외 메시지를 포함한 ResponseEntity.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * UnauthorizedAccessException 예외를 처리하는 메서드.
     * 주로 허가되지 않은 사용자가 특정 행위를 시도할 때 발생하며,
     * HTTP 403 상태 코드를 반환.
     *
     * @param ex 처리할 UnauthorizedAccessException 예외 객체.
     * @return HTTP 상태 코드 403과 예외 메시지를 포함한 ResponseEntity.
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<String> handleOrganizerMismatchException(UnauthorizedAccessException ex) {
        return buildResponseEntity(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    /**
     * IOException 처리 메서드.
     * 주로 파일 입출력 시 발생하며, HTTP 500 상태 코드를 반환.
     *
     * @param ex 처리할 IOException 예외 객체.
     * @return HTTP 상태 코드 500과 예외 메시지를 포함한 ResponseEntity.
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex) {
        log.error("IOException occurred: {}", ex.getMessage());
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "File processing error: " + ex.getMessage());
    }

    /**
     * MethodArgumentNotValidException 예외를 처리하는 메서드.
     * 입력 데이터 유효성 검사 실패 시 발생하며, HTTP 400 상태 코드를 반환.
     *
     * @param ex 처리할 MethodArgumentNotValidException 예외 객체.
     * @return HTTP 상태 코드 400과 검증 오류 메시지를 포함한 ResponseEntity.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put("LocalDateTime", LocalDateTime.now());
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * JsonProcessingException 예외를 처리하는 메서드.
     * JSON 데이터 처리 중 오류가 발생하면 HTTP 400 상태 코드를 반환.
     *
     * @param ex 처리할 JsonProcessingException 예외 객체.
     * @param request WebRequest 객체를 통해 요청 정보를 전달.
     * @return HTTP 상태 코드 400과 예외 메시지를 포함한 ResponseEntity.
     */
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<Object> handleJsonProcessingException(JsonProcessingException ex, WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("LocalDateTime", LocalDateTime.now());
        errors.put("message", "Error processing Json Data");
        errors.put("path", request.getDescription(false));

        log.error("Error processing JSON data: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * 모든 예외를 처리하는 메서드. 알려지지 않은 예외에 대해 HTTP 500 상태 코드를 반환.
     *
     * @param ex 처리할 Exception 예외 객체.
     * @param request WebRequest 객체를 통해 요청 정보를 전달.
     * @return HTTP 상태 코드 500과 예외 메시지를 포함한 ResponseEntity.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("LocalDateTime", LocalDateTime.now());
        errors.put("message", "An error occurred");
        errors.put("path", request.getDescription(false));

        log.error("An unexpected error occurred: {}", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
