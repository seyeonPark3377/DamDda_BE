package org.eightbit.damdda.order.controller;

import lombok.RequiredArgsConstructor;
import org.eightbit.damdda.order.dto.KakaoApproveResponse;
import org.eightbit.damdda.order.dto.KakaoReadyResponse;
import org.eightbit.damdda.order.dto.TossResponse;
import org.eightbit.damdda.order.service.KakaoPayService;
import org.eightbit.damdda.order.service.TossPayService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final KakaoPayService kakaoPayService;
    private final TossPayService tossPayService;

@GetMapping("/toss/success")
public ResponseEntity<TossResponse> tossSuccess(
        @RequestParam("paymentKey") String paymentKey,
        @RequestParam("orderId") String orderId,
        @RequestParam("amount") String amount,
        @RequestHeader(value = "x-damdda-authorization", required = false) String authorizationHeader,
        HttpServletResponse response) throws IOException {
    // Toss 결제 승인 처리
    TossResponse tossResponse = tossPayService.confirmPayment(paymentKey, orderId, amount,authorizationHeader);
    // 결제 성공 여부 확인
    try{if (tossResponse.getStatus().equals("DONE")) {
        String orderIdNew = orderId.replace("DAMDDA-ORDER-", "");
        // 결제 성공 시 /toss/success/getOrder로 리다이렉트
        response.sendRedirect("http://localhost:3000/payment/success?orderId=" + Long.parseLong(orderIdNew));
    } else {
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