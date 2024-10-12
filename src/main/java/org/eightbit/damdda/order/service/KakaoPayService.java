package org.eightbit.damdda.order.service;

import lombok.RequiredArgsConstructor;
import org.eightbit.damdda.order.dto.KakaoApproveResponse;
import org.eightbit.damdda.order.dto.KakaoCancelResponse;
import org.eightbit.damdda.order.dto.KakaoReadyResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import javax.transaction.Transactional;

@PropertySource("classpath:api.properties")

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {


    @Value("${KAKAO_ADMIN_KEY}")
    private String KAKAO_ADMIN_KEY;

    static final String cid = "TC0ONETIME"; // 가맹점 테스트 코드
    private KakaoReadyResponse kakaoReady;

    // 결제 준비
    public KakaoReadyResponse kakaoPayReady(Long orderId,String authorizationHeader) {

        // 카카오페이 요청 양식
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("partner_order_id", orderId); // 전달받은 orderId를 사용
        parameters.add("partner_user_id", 0);
        parameters.add("item_name", "쿠폰");
        parameters.add("quantity", 1);
        parameters.add("total_amount", 10000);
        parameters.add("vat_amount", 100);
        parameters.add("tax_free_amount", 0);
        parameters.add("approval_url", String.format("http://localhost:9000/payment/kakao/success/%d", orderId));
        parameters.add("cancel_url", String.format("http://localhost:9000/payment/kakao/cancel?orderId=%d", orderId));
        parameters.add("fail_url", String.format("http://localhost:9000/payment/kakao/fail?orderId=%d", orderId));

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parameters,this.getHeaders(authorizationHeader));

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        kakaoReady = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                requestEntity,
                KakaoReadyResponse.class);

        return kakaoReady;
    }

    // 결제 승인
    public KakaoApproveResponse approveResponse(String pgToken, Long orderId,String authorizationHeader) {
        System.out.println("pg_token: " + pgToken);

        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", kakaoReady.getTid());
        parameters.add("partner_order_id", orderId); // 전달받은 orderId 사용
        parameters.add("partner_user_id", 0);
        parameters.add("pg_token", pgToken);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders(authorizationHeader));

        RestTemplate restTemplate = new RestTemplate();
        KakaoApproveResponse approveResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse.class);

        return approveResponse;
    }


    /* * 결제 환불
     */
//    public KakaoCancelResponse kakaoCancel() {
//
//        // 카카오페이 요청
//        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
//        parameters.add("cid", cid);
//        parameters.add("tid", kakaoReady.getTid());
//        parameters.add("cancel_amount", 10);
//        parameters.add("cancel_tax_free_amount", 0);
//        parameters.add("cancel_vat_amount", 0);
//
//        // 파라미터, 헤더
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
//
//        // 외부에 보낼 url
//        RestTemplate restTemplate = new RestTemplate();
//
//        KakaoCancelResponse cancelResponse = restTemplate.postForObject(
//                "https://kapi.kakao.com/v1/payment/cancel",
//                requestEntity,
//                KakaoCancelResponse.class);
//
//        return cancelResponse;
//    }

    /**
     * 카카오 요구 헤더값
     */
    private HttpHeaders getHeaders(String damddaAuth) {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "KakaoAK " + KAKAO_ADMIN_KEY;
        httpHeaders.set("Authorization", auth);
        httpHeaders.set("x-damdda-authorization", damddaAuth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return httpHeaders;
    }
}