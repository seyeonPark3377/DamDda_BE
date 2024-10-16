package org.eightbit.damdda.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eightbit.damdda.noticeandqna.exception.UnauthenticatedMemberException;
import org.eightbit.damdda.noticeandqna.exception.UnauthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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

//    //validation에 사용하는 예외처리
//    @ExceptionHandler(MethodArgumentNotValidException.class) //특정 예외를 처리하는 메소드를 지정하는 어노테이션
//    public ResponseEntity<Object> handleValidationExceptions(
//            MethodArgumentNotValidException ex){
//        Map<String,Object> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error)->{
//            String fieldName = ((FieldError) error).getField(); //오류가 발생한 필드의 이름을 가져옴.
//            String errorMessage = error.getDefaultMessage();
//            errors.put("timestamp", LocalDateTime.now());
//            errors.put(fieldName,errorMessage);
//        });
//
//        return ResponseEntity.badRequest().body(errors);
//    }
//
//    //jsonprocessingException
//    @ExceptionHandler(JsonProcessingException.class)
//    public ResponseEntity<Object> handleJsonProcessingException(JsonProcessingException ex, WebRequest request){
//        Map<String,Object> errors = new HashMap<>();
//        errors.put("timestamp",LocalDateTime.now());
//        errors.put("message","Error processing Json Data");
//        errors.put("path",request.getDescription(false));
//        log.error("Error processing JSON data:"+ex.getMessage());
//        return ResponseEntity.badRequest().body(errors);
//    }
//
//    //그 외 다른 exception
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request){
//        Map<String,Object> errors = new HashMap<>();
//        errors.put("timestamp", LocalDateTime.now());
//        errors.put("message","An Error occured");
//        errors.put("path",request.getDescription(false)); //요청 URI만 포함한 PATH를 넘겨줌.
//
//        //중요한 정보는 로그로만 보이도록
//        log.error(ex.getMessage());
//        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
