package org.eightbit.damdda.order.repository;

import org.eightbit.damdda.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findAllBySupportingProject_User_Id(Long userId);
    Optional<Order> findById(Long orderId);


}
//오류는 OrderRepository에서 findAllByUserId라는 메서드를 찾으려고 하지만, Order 엔티티에 userId라는 필드가 존재하지 않기 때문에 발생합니다.
//Order 엔티티에는 직접적으로 userId 필드가 없고, SupportingProject 엔티티를 통해 Member 엔티티의 userId를 참조하고 있습니다.
//
//이를 해결하기 위해 OrderRepository에서 userId가 아닌 supportingProject.user.id를 참조하는 방식으로 변경해야 합니다.
//
//1. OrderRepository 수정
//아래와 같이 SupportingProject의 Member 객체를 통해 userId를 참조하는 방식으로 수정합니다: