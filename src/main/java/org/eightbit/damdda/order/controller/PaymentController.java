//package org.eightbit.damdda.order.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.eightbit.damdda.order.domain.Order;
//import org.eightbit.damdda.order.dto.KakaoApproveResponse;
//import org.eightbit.damdda.order.dto.KakaoCancelResponse;
//import org.eightbit.damdda.order.dto.KakaoReadyResponse;
//import org.eightbit.damdda.order.dto.TossResponse;
//import org.eightbit.damdda.order.service.KakaoPayService;
//import org.eightbit.damdda.order.service.NaverPayService;
//import org.eightbit.damdda.order.service.TossPayService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Map;
//
//
//@RestController
//@RequestMapping("/payment")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "*")  // 모든 도메인 허용
//public class PaymentController {
//
////    private final KakaoPayService kakaoPayService;
//    private final NaverPayService naverPayService;
//    private final TossPayService tossPayService;
//
//    @GetMapping("/naver")
//    public ResponseEntity<Map<String, Object>> naverPay(@RequestParam Map<String, Object> params) {
//        try {
//            Map<String, Object> res = naverPayService.naverPay(params);
//            System.out.println("NaverPay Response: " + res);  // 반환 값 로그 출력
//            return ResponseEntity.ok()
//                    .header("Content-Type", "application/json")
//                    .body(res);
//        } catch (Exception e) {
//            // 예외 처리 및 로그 남기기
//            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PostMapping("/naver/disconnect")
//    public ResponseEntity<Void> handleDisconnect(@RequestBody Map<String, Object> disconnectData) {
//        // 네이버로부터 연결 끊기 요청이 들어왔을 때 처리
//        String userId = (String) disconnectData.get("id");  // 사용자 ID 추출
//        // 사용자 데이터 비활성화 또는 삭제 처리
//        System.out.println("연결 끊기 요청이 들어왔습니다. 사용자 ID: " + userId);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/toss/success")
//    public ResponseEntity<String> tossSuccess(
//            @RequestParam("paymentKey") String paymentKey,
//            @RequestParam("orderId") String orderId,
//            @RequestParam("amount") String amount,
//            HttpServletResponse response) {
//
//        // Toss 결제 승인 처리
//        TossResponse tossResponse = tossPayService.confirmPayment(paymentKey, orderId, amount);
//        System.out.println("tossResponse: " + tossResponse);
//
//        // 결제 성공 시 React의 결제 완료 페이지로 리다이렉트
//        try {
//            response.sendRedirect("http://localhost:3000/payment/success");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return ResponseEntity.ok("Payment confirmed.");
//    }
//
//
//
//    @PostMapping("/toss/fail")
//    public ResponseEntity<String> tossFail(@RequestParam Map<String, String> params) {
//        // 결제 실패 처리
//        return ResponseEntity.ok("redirect:/tossFail");
//    }
//
//
//
//    // 카카오페이 결제 준비 요청
//    @PostMapping("/kakao/ready")
//    public ResponseEntity<KakaoReadyResponse> readyToPay(@RequestBody Order order) {
//        KakaoReadyResponse kakaoReadyResponse = kakaoPayService.kakaoPayReady(order);
//        return ResponseEntity.ok(kakaoReadyResponse);
//    }
//
//
//    // 결제 성공 시 승인 요청
//    @GetMapping("/kakao/success")
//    public ResponseEntity<KakaoApproveResponse> afterPayRequest(@RequestParam("pg_token") String pgToken, HttpServletResponse response) {
//        System.out.println("결제성공");
//        KakaoApproveResponse kakaoApproveResponse = kakaoPayService.approveResponse(pgToken);
//        // 결제 성공 시 React의 결제 완료 페이지로 리다이렉트
//        try {
//            response.sendRedirect("http://localhost:3000/payment/success");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        return ResponseEntity.ok(kakaoApproveResponse);
//    }
//    /**
//     * 환불요청
//     */
//    @PostMapping("/kakao/refund")
//    public ResponseEntity refund() {
//
//        KakaoCancelResponse kakaoCancelResponse = kakaoPayService.kakaoCancel();
//
//        return new ResponseEntity<>(kakaoCancelResponse, HttpStatus.OK);
//    }
//
//    /**
//     * 결제 진행 중 취소
//     */
////    @GetMapping("/cancel")
////    public void cancel() {
////
////        throw new BusinessLogicException(ExceptionCode.PAY_CANCEL);
////    }
////
////    /**
////     * 결제 실패
////     */
////    @GetMapping("/fail")
////    public void fail() {
////
////        throw new BusinessLogicException(ExceptionCode.PAY_FAILED);
////    }
//}
package org.eightbit.damdda.order.controller;

import lombok.RequiredArgsConstructor;
import org.eightbit.damdda.order.dto.KakaoApproveResponse;
import org.eightbit.damdda.order.dto.KakaoCancelResponse;
import org.eightbit.damdda.order.dto.KakaoReadyResponse;
import org.eightbit.damdda.order.dto.TossResponse;
import org.eightbit.damdda.order.service.KakaoPayService;
import org.eightbit.damdda.order.service.NaverPayService;
import org.eightbit.damdda.order.service.TossPayService;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor


public class PaymentController {

    private final KakaoPayService kakaoPayService;
    private final NaverPayService naverPayService;
    private final TossPayService tossPayService;

    @GetMapping("/naver")
    public ResponseEntity<Map<String, Object>> naverPay(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> res = naverPayService.naverPay(params);
            System.out.println("NaverPay Response: " + res);  // 반환 값 로그 출력
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(res);
        } catch (Exception e) {
            // 예외 처리 및 로그 남기기
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/naver/disconnect")
    public ResponseEntity<Void> handleDisconnect(@RequestBody Map<String, Object> disconnectData) {
        // 네이버로부터 연결 끊기 요청이 들어왔을 때 처리
        String userId = (String) disconnectData.get("id");  // 사용자 ID 추출
        // 사용자 데이터 비활성화 또는 삭제 처리
        System.out.println("연결 끊기 요청이 들어왔습니다. 사용자 ID: " + userId);
        return ResponseEntity.ok().build();
    }


//    @GetMapping("/toss/success")
//    //http://localhost:9000/toss/success?
//    // paymentType=NORMAL&
//    // orderId=DAMDDA-ORDER-108&
//    // paymentKey=tgen_20241007115154V0qF3
//    // &amount=100000
//    public ResponseEntity<String> tossSuccess(
//            @RequestParam("paymentType") String paymentType,
//            @RequestParam("orderId") String orderIdFromToss,  // Toss에서 보내주는 orderId
//            @RequestParam("paymentKey") String paymentKey,  // Toss 결제 키
//            @RequestParam("amount") String amount,  // 결제 금액
////            @RequestParam("customOrderId") String orderId,  // 찐 Order ID
//            HttpServletResponse response) throws IOException {
//
//        System.out.println("Toss에서 받은 orderId: " + orderIdFromToss);
//        System.out.println("결제 유형: " + paymentType);
//        System.out.println("결제 키: " + paymentKey);
//        System.out.println("결제 금액: " + amount);
////        System.out.println("찐 Order ID: " + orderId);
//
//        // 결제 승인 처리 로직 (예: TossPayService를 사용하여 결제 승인 로직 구현)
//
//        TossResponse tossResponse = tossPayService.confirmPayment(paymentKey, orderIdFromToss, amount);
//        System.out.println("Toss 결제 응답: " + tossResponse);
//
//        // 결제 성공 후 React의 결제 완료 페이지로 리다이렉트
//        if (tossResponse.getStatus().equals("DONE")) {
//            // 결제 성공 시 처리 로직 (DB 업데이트 등)
//            System.out.println("결제 성공!");
//            response.sendRedirect("http://localhost:3000/payment/success");
//        } else {
//            // 결제 실패 시 처리 로직
//            System.out.println("결제 실패!");
//            response.sendRedirect("http://localhost:3000/payment/fail");
//        }
//
//        return ResponseEntity.ok("Payment confirmed.");
//    }
//
//
//    @PostMapping("/toss/fail")
//    public ResponseEntity<String> tossFail(@RequestParam Map<String, String> params) {
//        // 결제 실패 처리
//        return ResponseEntity.ok("redirect:/tossFail");
//    }
@CrossOrigin(origins = "http://localhost:3000") // 특정 도메인에서만 허용
@GetMapping("/toss/success")
public ResponseEntity<TossResponse> tossSuccess(
        @RequestParam("paymentKey") String paymentKey,
        @RequestParam("orderId") String orderId,
        @RequestParam("amount") String amount,
        @RequestHeader(value = "x-damdda-authorization", required = false) String authorizationHeader,
        HttpServletResponse response) throws IOException {

    // 결제 승인 처리 로그 출력
    System.out.println("Toss에서 받은 orderId: " + orderId);
    System.out.println("결제 키: " + paymentKey);
    System.out.println("결제 금액: " + amount);
    System.out.println(authorizationHeader+"//////");

    // Toss 결제 승인 처리
    TossResponse tossResponse = tossPayService.confirmPayment(paymentKey, orderId, amount,authorizationHeader);

    // 결제 성공 여부 확인
    try{if (tossResponse.getStatus().equals("DONE")) {
        System.out.println("결제 성공!");
        String orderIdNew = orderId.replace("DAMDDA-ORDER-", "");

        // 결제 성공 시 /toss/success/getOrder로 리다이렉트
        response.sendRedirect("http://localhost:3000/payment/success?orderId=" + Long.parseLong(orderIdNew));
    } else {
        System.out.println("결제 실패!");
        // 결제 실패 시 /toss/success/getOrder로 실패 ID 1로 리다이렉트
        response.sendRedirect("http://localhost:3000/payment/success?orderId=" + orderId);
    }}catch (IOException e){
        e.printStackTrace();
    }
    return ResponseEntity.ok(tossResponse);

}


    // 카카오페이 결제 준비 요청
//    @PostMapping("/kakao/ready")
//    public ResponseEntity<KakaoReadyResponse> readyToKakaoPay() {
//        KakaoReadyResponse kakaoReadyResponse = kakaoPayService.kakaoPayReady();
//        return ResponseEntity.ok(kakaoReadyResponse);
//    }
    @PostMapping("/kakao/ready")
    public ResponseEntity<KakaoReadyResponse> readyToKakaoPay(@RequestBody Map<String, Object> requestData,
                                                              @RequestHeader(value = "x-damdda-authorization", required = false) String authorizationHeader) {
        Long orderId = Long.parseLong(requestData.get("orderId").toString());
        System.out.println("x-damdda-authorization"+"//////");
        System.out.println(orderId+"************");
        KakaoReadyResponse kakaoReadyResponse = kakaoPayService.kakaoPayReady(orderId,"x-damdda-authorization");
        return ResponseEntity.ok(kakaoReadyResponse);
    }


    // 결제 성공 시 승인 요청
//    @GetMapping("/kakao/success")
//    public ResponseEntity<KakaoApproveResponse> afterPayRequest(@RequestParam("pg_token") String pgToken, HttpServletResponse response) {
//        System.out.println("결제성공");
//        KakaoApproveResponse kakaoApproveResponse = kakaoPayService.approveResponse(pgToken);
//        // 결제 성공 시 React의 결제 완료 페이지로 리다이렉트
//        try {
//            response.sendRedirect("http://localhost:3000/payment/success");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        return ResponseEntity.ok(kakaoApproveResponse);
//    }
    // 결제 성공 시 승인 요청
    @CrossOrigin(origins = "http://localhost:3000") // 특정 도메인에서만 허용
    @GetMapping("/kakao/success/{order_id}")
    public ResponseEntity<KakaoApproveResponse> afterPayRequest(
            @RequestParam("pg_token") String pgToken,
            @PathVariable("order_id") Long orderId,  // PathVariable로 order_id 받음
            @RequestHeader(value = "x-damdda-authorization", required = false) String authorizationHeader, HttpServletResponse response) {



        System.out.println("결제 성공. Order ID: " + orderId + ", PG Token: " + pgToken);
        System.out.println(authorizationHeader+"//////");

        // pg_token과 orderId를 사용하여 결제 승인 요청
        KakaoApproveResponse kakaoApproveResponse = kakaoPayService.approveResponse(pgToken, orderId,authorizationHeader);

        // 결제 성공 시 React의 결제 완료 페이지로 리다이렉트
        try {
            response.sendRedirect("http://localhost:3000/payment/success?orderId=" + orderId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(kakaoApproveResponse);
    }



    /**
     * 환불요청
     */
//    @PostMapping("/kakao/refund")
//    public ResponseEntity refund() {
//
//        KakaoCancelResponse kakaoCancelResponse = kakaoPayService.kakaoCancel();
//
//        return new ResponseEntity<>(kakaoCancelResponse, HttpStatus.OK);
//    }

    /**
     * 결제 진행 중 취소
     */
//    @GetMapping("/cancel")
//    public void cancel() {
//
//        throw new BusinessLogicException(ExceptionCode.PAY_CANCEL);
//    }
//
//    /**
//     * 결제 실패
//     */
//    @GetMapping("/fail")
//    public void fail() {
//
//        throw new BusinessLogicException(ExceptionCode.PAY_FAILED);
//    }
}