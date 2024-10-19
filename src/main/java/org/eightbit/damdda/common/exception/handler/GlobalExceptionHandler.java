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
     * 주어진 상태, 메시지, 세부 정보를 포함하는 ResponseEntity를 생성합니다.
     *
     * @param status HTTP 상태 코드.
     * @param message 오류 메시지.
     * @param details 응답 본문에 추가할 세부 정보.
     * @return 주어진 상태와 메시지를 포함한 ResponseEntity.
     */
    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message, Map<String, Object> details) {
        Map<String, Object> responseBody = new HashMap<>(details);
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", status.value());
        responseBody.put("error", status.getReasonPhrase());
        responseBody.put("message", message);
        return ResponseEntity.status(status).body(responseBody);
    }

    /**
     * NoSuchElementException을 처리하고 404 상태를 반환합니다.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        log.error("NoSuchElementException 발생: ", ex);
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage(), new HashMap<>());
    }

    /**
     * UnauthorizedAccessException을 처리하고 403 상태를 반환합니다.
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Object> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        log.error("UnauthorizedAccessException 발생: ", ex);
        return buildResponseEntity(HttpStatus.FORBIDDEN, ex.getMessage(), new HashMap<>());
    }

    /**
     * IOException을 처리하고 500 상태를 반환합니다.
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleIOException(IOException ex) {
        log.error("IOException 발생: ", ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "파일 처리 오류", new HashMap<>());
    }

    /**
     * MethodArgumentNotValidException을 처리하고 400 상태를 반환합니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.error("유효성 검사 오류: ", ex);
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "유효성 검사 실패", errors);
    }

    /**
     * JsonProcessingException을 처리하고 400 상태를 반환합니다.
     */
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<Object> handleJsonProcessingException(JsonProcessingException ex, WebRequest request) {
        Map<String, Object> details = new HashMap<>();
        details.put("path", request.getDescription(false));

        log.error("JSON 데이터 처리 오류: ", ex);
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "JSON 데이터 처리 오류", details);
    }

    /**
     * 모든 기타 예외를 처리하고 500 상태를 반환합니다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        Map<String, Object> details = new HashMap<>();
        details.put("path", request.getDescription(false));

        log.error("예기치 않은 오류 발생: ", ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "오류가 발생했습니다.", details);
    }
}
