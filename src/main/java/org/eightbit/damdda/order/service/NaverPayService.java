package org.eightbit.damdda.order.service;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@PropertySource("classpath:api.properties")
@Service
public class NaverPayService {

    @Value("${NAVER_CLIENT_KEY}")
    private String NAVER_CLIENT_KEY;

    @Value("${NAVER_SECRET_KEY}")
    private String NAVER_SECRET_KEY;

    @Value("${NAVER_PARTNER_KEY}")
    private String NAVER_PARTNER_KEY;


    public Map<String, Object> naverPay(Map<String, Object> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id",NAVER_CLIENT_KEY);
        headers.set("X-Naver-Client-Secret",NAVER_SECRET_KEY);
        headers.set("Content-Type", "application/json");

        Map<String, Object> payParams = new HashMap<String, Object>();
        payParams.put("merchantPayKey", "KA020230318002");
        payParams.put("merchantUserKey", "사용자하나둘셋");
        payParams.put("productName", params.get("productName"));
        payParams.put("productCount", params.get("productCount"));
        payParams.put("totalPayAmount", params.get("totalPayAmount"));
        payParams.put("taxScopeAmount", params.get("taxScopeAmount"));
        payParams.put("taxExScopeAmount", params.get("taxExScopeAmount"));
        payParams.put("returnUrl", "http://localhost:9000/pay/success");

        // 상품관련정보
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("categoryType", "PRODUCT");
        item.put("categoryId", "GENERAL");
        item.put("uid", "iphone");
        item.put("name", "아이폰");
        item.put("count", 1);

        items.add(item);

        payParams.put("productItems", items);
        JSONObject jObj = new JSONObject(payParams);

        // 네이버페이 결제준비 api 요청
        HttpEntity<JSONObject> request = new HttpEntity<>(jObj, headers);

        RestTemplate template = new RestTemplate();
        String url = "https://dev.apis.naver.com/" + NAVER_PARTNER_KEY + "/naverpay/payments/v2/reserve";

        System.out.println("Request URL: " + url);
        System.out.println("Request Headers: " + headers);
        System.out.println("Request Body: " + jObj);
        System.out.println("API URL: " + url);

        // 요청결과
        try {
            Map<String, Object> res = template.postForObject(url, request, Map.class);
            System.out.println("API Response: " + res);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", "API call failed", "details", e.getMessage());
        }


    }


}


