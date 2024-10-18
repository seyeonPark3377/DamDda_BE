package org.eightbit.damdda.order.service;

import org.eightbit.damdda.order.dto.KakaoApproveResponse;
import org.eightbit.damdda.order.dto.KakaoReadyResponse;

public interface KakaoPayService {
    // 결제 준비
    KakaoReadyResponse kakaoPayReady(Long orderId, String authorizationHeader);

    // 결제 승인
    KakaoApproveResponse approveResponse(String pgToken, Long orderId, String authorizationHeader);
}
