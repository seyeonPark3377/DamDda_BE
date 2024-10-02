import React, { useState } from "react";
import Box from "@mui/joy/Box";
import StatusButton from "./StatusButton";
import Typography from "@mui/joy/Typography";
import axios from "axios";



const PaymentDeliveryInfo = ({ project }) => {
  const [paymentStatus, setPaymentStatus] = useState(project.payment.paymentStatus); // 결제 상태 상태 추가

  // 결제 취소 로직
  const handleCancelPayment = async () => {
    const orderId = 5; // 임의로 설정한 orderId
    const status = '결제 취소'; // 예시로 '취소' 상태 설정
    try {
      const response = await axios.put(`http://localhost:9000/order/${orderId}/cancel`, null, {
        params: { status } // 요청 파라미터로 상태 전달
      });
        if (response.status === 200) {
        // 성공적으로 결제가 취소됨
        alert("결제가 취소되었습니다.");
        // 필요한 경우 UI를 업데이트하거나 상태를 새로 고침하는 로직 추가
      }
    } catch (error) {
      console.error('결제 취소 중 오류 발생:', error);
      alert("결제 취소에 실패했습니다.");
    }
  };

  return (
    <Box sx={{ padding: "20px" }}>
      {/* 배송 정보 */}
      <Box sx={{ marginBottom: "20px" }}>
        <Typography
          variant="h6"
          sx={{
            marginBottom: "10px",
            fontWeight: "bold",
            color: "black",
            fontSize: "20px",
          }}
        >
          배송 정보
        </Typography>
        <Box sx={{ display: "flex", marginBottom: "5px" }}>
          <Typography
            sx={{ fontWeight: "bold", color: "gray", minWidth: "80px" }}
          >
            수령인:
          </Typography>
          <Typography sx={{ marginLeft: "50px" }}>
           {project.supportingProject.user.name}
          </Typography>
        </Box>
        <Box sx={{ display: "flex", marginBottom: "5px" }}>
          <Typography
            sx={{ fontWeight: "bold", color: "gray", minWidth: "80px" }}
          >
            휴대폰:
          </Typography>
          <Typography sx={{ marginLeft: "50px" }}>
          {project.supportingProject.user.phoneNumber}
          </Typography>
        </Box>
        <Box sx={{ display: "flex", marginBottom: "5px" }}>
          <Typography
            sx={{ fontWeight: "bold", color: "gray", minWidth: "80px" }}
          >
            주소:
          </Typography>
          <Typography sx={{ marginLeft: "50px" }}>
          {project.supportingProject.delivery.deliveryAddress}
          (  {project.supportingProject.delivery.deliveryDetailedAddress}  )

          </Typography>
        </Box>
        <Box sx={{ display: "flex", marginBottom: "5px" }}>
          <Typography
            sx={{ fontWeight: "bold", color: "gray", minWidth: "80px" }}
          >
            배송 요청 사항:
          </Typography>
          <Typography sx={{ marginLeft: "20px" }}>
          {project.supportingProject.delivery.deliveryMessage}
          </Typography>
        </Box>
      </Box>

      {/* 구분선 */}
      <Box
        sx={{
          borderBottom: "1px solid #e0e0e0",
          marginBottom: "20px",
          width: "1000px",
        }}
      />

      {/* 결제 정보 */}
      <Box sx={{ marginBottom: "20px" }}>
        <Typography
          variant="h6"
          sx={{
            marginBottom: "10px",
            fontWeight: "bold",
            color: "black",
            fontSize: "20px",
          }}
        >
          결제 내역
        </Typography>
        <Box sx={{ display: "flex", marginBottom: "5px" }}>
          <Typography
            sx={{ fontWeight: "bold", color: "gray", minWidth: "80px" }}
          >
            결제 방법:
          </Typography>
          <Typography sx={{ marginLeft: "50px" }}>
            {project.supportingProject.payment.paymentMethod}
          </Typography>
        </Box>
        <Box sx={{ display: "flex", marginBottom: "5px" }}>
          <Typography
            sx={{ fontWeight: "bold", color: "gray", minWidth: "80px" }}
          >
            총 상품 금액:
          </Typography>
          <Typography sx={{ marginLeft: "38px" }}>
          {project.supportingPackage.packagePrice}
          </Typography>
        </Box>
        <Box sx={{ display: "flex", marginBottom: "5px" }}>
          <Typography
            sx={{ fontWeight: "bold", color: "gray", minWidth: "80px" }}
          >
            결제 상태:
          </Typography>
          <Typography sx={{ marginLeft: "50px" }}>
          {paymentStatus} {/* 상태를 직접 보여줌 */}
          </Typography>
        </Box>
      </Box>

     {/* 결제 취소 버튼 */}
      <Box sx={{ textAlign: "right", position: "relative", top: "-60px", left: "-180px" }}>
        <StatusButton
          status="결제 취소"
          label="결제 취소"
          onClick={() => handleCancelPayment(3)} // orderId를 3으로 고정
          // onClick={() => handleCancelPayment(3)} // orderId 전달

          sx={{
            backgroundColor: "white", // 아주 연한 회색 배경색
            color: "red",
            fontSize: "12px", // 폰트 크기 줄이기
            width: "90px", // 버튼 너비 줄이기
            height: "30px", // 버튼 높이 줄이기
          }}
        />
      </Box>
    </Box>
  );
};

export default PaymentDeliveryInfo;

