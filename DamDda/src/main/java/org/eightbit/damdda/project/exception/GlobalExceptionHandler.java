//package org.eightbit.damdda.project.exception;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestControllerAdvice
//@Log4j2
//public class GlobalExceptionHandler {
//
//    //validation에 사용하는 예외처리
//    @ExceptionHandler(MethodArgumentNotValidException.class) //특정 예외를 처리하는 메소드를 지정하는 어노테이션
//    public ResponseEntity<Object> handleValidationExceptions(
//            MethodArgumentNotValidException ex){
//        Map<String,Object> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error)->{
//            String fieldName = ((FieldError) error).getField(); //오류가 발생한 필드의 이름을 가져옴.
//            String errorMessage = error.getDefaultMessage();
//            errors.put("timestamp",LocalDateTime.now());
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
//
//}
