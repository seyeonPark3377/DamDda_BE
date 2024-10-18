package org.eightbit.damdda.order.service;

import org.eightbit.damdda.order.domain.Order;
import org.eightbit.damdda.order.domain.SupportingProject;
import org.eightbit.damdda.order.dto.OrderDTO;
import org.eightbit.damdda.order.dto.ProjectStatisticsDTO;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    //주문 저장
    @Transactional
    OrderDTO createOrder(OrderDTO orderDTO);

    // userId로 주문 목록을 조회하는 메서드
    List<OrderDTO> getOrdersByUserId(Long userId);

    // 특정 주문 정보 가져오기 (orderId로 조회)
    Optional<OrderDTO> getOrderById(Long orderId);

    // 사용자의 모든 주문 정보 및 결제 정보 가져오기
    List<OrderDTO> getOrdersWithPaymentByUserId(Long userId);

    //결제 완료
    void updatePaymentStatus(Long orderId, String paymentStatus);

    void updateOrderStatus(Long orderId, String paymentStatus);

    String cancelPayment(Long paymentId, String paymentStatus);

    //order 테이블 가져오기
    List<Order> getOrdersBySupportingProject(SupportingProject supportingProject);

    OrderDTO convertToOrderDTO(Order order);

    //SupportingProject - 모든 주문 정보를 가져오는 서비스 메서드
    List<OrderDTO> getAllOrders();

    //member id를 통해 프로젝트 id 가져오기
    Long getUserProjectId(Long memberId);

    // ProjectStatistics 후원 프로젝트의 시작일, 마감일, 달성률, 총 후원 금액, 후원자 수, 남은 기간을 가져옴
    //프로젝트 통계 정보를 가져오는 서비스 메서드
    ProjectStatisticsDTO getProjectStatistics(Long projectId);

    String generateUploadAndGetPresignedUrlForSupportersExcel(Long projectId) throws IOException;
}