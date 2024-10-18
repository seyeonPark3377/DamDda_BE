package org.eightbit.damdda.order.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.security.user.User;
import org.eightbit.damdda.order.domain.Order;
import org.eightbit.damdda.order.dto.OrderDTO;
import org.eightbit.damdda.order.dto.ProjectStatisticsDTO;
import org.eightbit.damdda.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor

@SessionAttributes("supportingPackage")
@Log4j2
@RequestMapping("/order")

public class OrderController {

    private final OrderService orderService;

    //주문 생성
    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO, @AuthenticationPrincipal User user){
        orderDTO.getSupportingProject().getUser().setId(user.getMemberId());
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    // 결제 성공창 - 결제 정보 불러오기
    @GetMapping("/details/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        Optional<OrderDTO> orderDTOOptional = orderService.getOrderById(orderId);
        return orderDTOOptional.map(orderDTO -> new ResponseEntity<>(orderDTO, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //SupportedProjects - 주문 정보들 모두 가져오기
    @GetMapping("/supportingprojects")
    public List<OrderDTO> getOrdersByUserId(@AuthenticationPrincipal User user) {
        Long userId = user.getMemberId();
        return orderService.getOrdersWithPaymentByUserId(userId);
    }

    // PaymentSuccess.jsx - 결제 완료
    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updatePaymentStatus(@PathVariable Long orderId, @RequestBody Map<String, String> requestBody) {
        try {
            String status = requestBody.get("paymentStatus");
            orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok("Payment status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update payment status");
        }
    }

    @PutMapping("/{paymentId}/cancel")
    public ResponseEntity<String> cancelPayment(@PathVariable Long paymentId,@RequestBody Map<String, Object> requestBody) {
        try {
            String status = (String) requestBody.get("paymentStatus");
            // 서비스에서 결제 상태 취소 처리**********
            /*paymentId -> paymentId가 들어가서 repository바꿈.*/
            String message=orderService.cancelPayment(paymentId, status);
            return ResponseEntity.ok("Payment canceled successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to cancel payment");
        }
    }

    // Supporting 모든 주문 정보 - 모든 주문 정보를 가져오는 API 엔드포인트
    @GetMapping("/all")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    //프로젝트 id 가져오기
    @GetMapping("/user/project")
    public ResponseEntity<Long> getUserProject(@AuthenticationPrincipal User user) {
        Long memberId = user.getMemberId();
        Long projectId = orderService.getUserProjectId(memberId);
        if (projectId != null) {
            return new ResponseEntity<>(projectId, HttpStatus.OK); // projectId만 반환
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 프로젝트가 없을 경우 404 반환
        }
    }

    // ProjectStatistics 후원 프로젝트의 시작일, 마감일, 달성률, 총 후원 금액, 후원자 수, 남은 기간을 가져옴
    @GetMapping("/statistics/{projectId}")
    public ResponseEntity<ProjectStatisticsDTO> getProjectStatistics(@PathVariable Long projectId) {
        ProjectStatisticsDTO statistics = orderService.getProjectStatistics(projectId);
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    @GetMapping("/{projectId}/supporters/excel")
    public ResponseEntity<String> generateAndGetSupportersExcel(@PathVariable Long projectId) throws IOException {
        // Generate the presigned URL by calling the service method
        String presignedUrl = orderService.generateUploadAndGetPresignedUrlForSupportersExcel(projectId);
        // Return the presigned URL as a response
        return ResponseEntity.ok(presignedUrl);
    }

}



