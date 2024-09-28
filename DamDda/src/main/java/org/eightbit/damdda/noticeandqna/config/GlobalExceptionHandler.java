package org.eightbit.damdda.noticeandqna.config;

import org.eightbit.damdda.noticeandqna.exception.UnauthenticatedMemberException;
import org.eightbit.damdda.noticeandqna.exception.UnauthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

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
     * UnauthenticatedMemberException 예외를 처리하는 메서드.
     * 인증되지 않은 사용자가 보호된 리소스에 접근할 때 발생하며,
     * HTTP 401 상태 코드를 반환.
     *
     * @param ex 처리할 UnauthenticatedMemberException 예외 객체.
     * @return HTTP 상태 코드 401과 예외 메시지를 포함한 ResponseEntity.
     */
    @ExceptionHandler(UnauthenticatedMemberException.class)
    public ResponseEntity<String> handleUnauthenticatedMemberException(UnauthenticatedMemberException ex) {
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }
}
