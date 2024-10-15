package org.eightbit.damdda.noticeandqna.exception;

// 사용자가 인증되지 않은 상태에서 접근할 때 던지는 커스텀 예외.
public class UnauthenticatedMemberException extends RuntimeException {

    // 예외 메시지를 받는 생성자.
    public UnauthenticatedMemberException(String message) {
        super(message); // 부모 클래스인 RuntimeException에 메시지를 전달.
    }

    // 예외 메시지와 원인 예외(cause)를 받는 생성자.
    public UnauthenticatedMemberException(String message, Throwable cause) {
        super(message, cause); // 부모 클래스에 메시지와 원인 예외를 전달.
    }

}