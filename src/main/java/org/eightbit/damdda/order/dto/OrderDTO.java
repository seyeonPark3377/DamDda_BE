package org.eightbit.damdda.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eightbit.damdda.order.domain.Delivery;
import org.eightbit.damdda.order.domain.Payment;
import org.eightbit.damdda.order.domain.SupportingPackage;
import org.eightbit.damdda.order.domain.SupportingProject;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
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
