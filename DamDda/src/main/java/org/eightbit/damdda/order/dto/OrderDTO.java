package org.eightbit.damdda.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eightbit.damdda.order.domain.Delivery;
import org.eightbit.damdda.order.domain.Payment;
import org.eightbit.damdda.order.domain.SupportingPackage;
import org.eightbit.damdda.order.domain.SupportingProject;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {
    private Delivery delivery;  // 연관된 Delivery 객체
    private Payment payment;  // 연관된 Payment 객체
    private SupportingProject supportingProject;  // 연관된 SupportingProject 객체
    private SupportingPackage supportingPackage;  // 연관된 SupportingPackage 객체

    @Override
    public String toString() {
        return "OrderDTO{" +
                "delivery=" + delivery +
                ", payment=" + payment +
                ", supportingProject=" + supportingProject +
                ", supportingPackage=" + supportingPackage +
                '}';
    }

}

//public class OrderDTO {
//
//    // SupportingProject-Delivery 관련 필드
//    private Long deliveryId;                  // 주문(후원) 번호
//    private String deliveryName;              // 이름
//    private String deliveryPhoneNumber;       // 전화번호
//    private String deliveryEmail;             // 이메일
//    private String deliveryAddress;           // 주소
//    private String deliveryDetailedAddress;   // 상세주소
//    private Integer deliveryPostCode;         // 우편번호
//    private String deliveryMessage;           // 메시지
//
//    // SupportProject 및 Project 관련 필드
//    private String title;                     // 프로젝트명
//
//    // Option 관련 필드
//    private String rewardName;                // 리워드 명
//
//    // ProjectPackage 관련 필드
//    private String packageName;               // 패키지 이름
//    private Integer packagePrice;             // 결제 금액
//
//    // SupportingProject-Payment 관련 필드
//    private String paymentMethod;             // 결제 방법
//    private String paymentStatus;             // 결제 상태
//
//    // SupportingPackage 관련 필드
//    private Integer packageCount;             // 패키지 수량
//
//}
