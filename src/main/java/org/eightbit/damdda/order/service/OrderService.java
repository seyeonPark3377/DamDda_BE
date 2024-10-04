package org.eightbit.damdda.order.service;

import lombok.RequiredArgsConstructor;
import org.eightbit.damdda.admin.repository.AdminProjectRepository;
import org.eightbit.damdda.order.domain.*;
import org.eightbit.damdda.order.dto.OrderDTO;
import org.eightbit.damdda.order.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final PaymentRepository paymentRepository;
    private final SupportingProjectRepository supportingProjectRepository;
    private final SupportingPackageRepository supportingPackageRepository;
    @Autowired
    private AdminProjectRepository projectRepository;


    //주문 저장
    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        // 연관된 엔티티 생성 및 저장

        Delivery delivery = deliveryRepository.save(orderDTO.getDelivery());
        Payment payment = paymentRepository.save(orderDTO.getPayment());
        SupportingProject supportingProject = new SupportingProject();
        supportingProject.setSupportedAt(LocalDateTime.now()); // 후원 시간 설정
        supportingProject.setDelivery(delivery);
        supportingProject.setPayment(payment);
        supportingProject.setProject(orderDTO.getSupportingProject().getProject());
        supportingProject.setUser(orderDTO.getSupportingProject().getUser());

        supportingProject = supportingProjectRepository.save(supportingProject);

        SupportingPackage supportingPackage = supportingPackageRepository.save(orderDTO.getSupportingPackage());

        // Order 엔티티 생성 및 저장
        Order order = Order.builder()
                .delivery(delivery)
                .payment(payment)
                .supportingProject(supportingProject)
                .supportingPackage(supportingPackage)
                .createdAt(LocalDateTime.now())
                .build();

        Order savedOrder=orderRepository.save(order);
        //후원 프로젝트 업데이트
//        Project project=savedOrder.getProject();
//        if (project != null) {
//            // 후원금액 추가
//            project.setFundsReceive(project.getFundsReceive() + order.getAmount());
//
//            // 후원자 수 추가
//            project.setSupporterCount(project.getSupporterCount() + 1);
//
//            // 프로젝트 업데이트
//            projectRepository.save(project);
//        }
        return savedOrder;

    }

    // 결제 상태 업데이트 메서드
    public Order updatePaymentStatus(Long orderId, String newStatus) {
        // orderId로 주문 정보 조회
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            // 결제 상태 업데이트
            order.getPayment().setPaymentStatus(newStatus);
            // 업데이트된 주문 정보 저장
            return orderRepository.save(order);
        }
        return null; // 주문을 찾지 못한 경우
    }

    // Order 조회 메서드
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null); // 주문이 존재하지 않으면 null 반환
    }

    // 주문 목록을 DTO로 변환하여 가져오는 메서드
    public List<OrderDTO> getMyOrders(Long userId) {
        List<Order> orders = orderRepository.findAllBySupportingProject_User_Id(userId);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 주문 상세 정보를 DTO로 반환하는 메서드
    public OrderDTO getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));
        return convertToDTO(order);
    }

    // 엔티티를 DTO로 변환하는 메서드
    private OrderDTO convertToDTO(Order order) {
        // DTO를 생성하면서 필요한 정보는 엔티티에서 가져옵니다.
        return new OrderDTO(
                order.getDelivery(),             // Delivery 엔티티
                order.getPayment(),              // Payment 엔티티
                order.getSupportingProject(),    // SupportingProject 엔티티
                order.getSupportingPackage()     // SupportingPackage 엔티티
        );

    }





}
