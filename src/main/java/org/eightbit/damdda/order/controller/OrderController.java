package org.eightbit.damdda.order.controller;

import lombok.RequiredArgsConstructor;
import org.eightbit.damdda.order.domain.Order;
import org.eightbit.damdda.order.dto.OrderDTO;
import org.eightbit.damdda.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SessionAttributes("supportingPackage")

@RequestMapping("/order")
///http://localhost:9000/projects/myproject/order/create
public class OrderController {

    private final OrderService orderService;

    // 주문 생성 컨트롤러
    @PostMapping("/create")
    public ResponseEntity<Map<String, Long>> createOrder(@RequestBody OrderDTO orderDTO) {
        // 데이터 잘 넘어오는지 확인
        System.out.println(orderDTO.toString());

        // 서비스 레이어에서 주문 생성 처리
        Order createdOrder = orderService.createOrder(orderDTO);

        // 생성된 주문의 ID를 응답으로 반환
        Map<String, Long> response = new HashMap<>();
        response.put("orderId", createdOrder.getOrderId());

        // HTTP 상태 코드를 200 OK로 설정
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/{orderId}/update-status")
    public ResponseEntity<Order> updatePaymentStatus(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> request) {

        // Payment status 값 가져오기
        String newStatus = request.get("paymentStatus");

        // 서비스 레이어를 통해 결제 상태 업데이트 처리
        Order updatedOrder = orderService.updatePaymentStatus(orderId, newStatus);

        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder); // 성공적으로 업데이트된 주문 반환
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 주문을 찾지 못한 경우
        }
    }

    // 주문 상세 가져오기
    @GetMapping("/details/{orderId}")
    public ResponseEntity<OrderDTO> getOrderDetails(@PathVariable Long orderId) {
        OrderDTO orderDTO = orderService.getOrderDetails(orderId);
        return ResponseEntity.ok(orderDTO);
    }

    // 주문 목록 가져오기
    @GetMapping("/my-orders/{userId}")
    public ResponseEntity<List<OrderDTO>> getMyOrders(@PathVariable Long userId) {
        List<OrderDTO> orders = orderService.getMyOrders(userId);
        return ResponseEntity.ok(orders);
    }


    //주문 취소
//    @GetMapping("/details/{orderId}")
//    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable String orderId, @RequestParam String status){
//        OrderDTO updatedorder=new OrderDTO();
//        return ResponseEntity.ok(updatedorder);
//    }


}
