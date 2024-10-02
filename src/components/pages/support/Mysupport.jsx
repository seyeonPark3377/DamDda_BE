import React, { useState, useEffect } from 'react';
import axios from 'axios'; // axios를 import

const MySupport = ({ deliveryId }) => {
  const [order, setOrder] = useState(null); // 단일 주문 데이터를 저장할 state
  const [error, setError] = useState(null); // 에러 핸들링을 위한 state

  // 주문 데이터를 가져오는 함수
  const fetchOrder = async (deliveryId) => {
    try {
      const response = await axios.get("http://localhost:9000/order/myOrders/${deliveryId}");
      setOrder(response.data); // 주문 데이터를 state에 저장
    } catch (error) {
      setError(error.message); // 에러 메시지를 저장
    }
  };

  useEffect(() => {
    fetchOrder(deliveryId); // 컴포넌트가 마운트될 때 주문 데이터를 가져옴
  }, [deliveryId]);

  if (error) {
    return <div>Error: {error}</div>; // 에러가 발생하면 에러 메시지를 출력
  }

  if (!order) {
    return <div>Loading...</div>; // 주문 데이터를 불러오는 동안 로딩 메시지 출력
  }

  // UI에 주문 데이터 출력
  return (
    <div className="order-container">
      <h2>Order Details</h2>
      <p><strong>Delivery ID:</strong> {order.deliveryId}</p>
      <p><strong>Name:</strong> {order.deliveryName}</p>
      <p><strong>Phone Number:</strong> {order.deliveryPhoneNumber}</p>
      <p><strong>Email:</strong> {order.deliveryEmail}</p>
      <p><strong>Address:</strong> {order.deliveryAddress}</p>
      <p><strong>Detailed Address:</strong> {order.deliveryDetailedAddress}</p>
      <p><strong>Post Code:</strong> {order.deliveryPostCode}</p>
      <p><strong>Message:</strong> {order.deliveryMessage}</p>

      <h3>Project Details</h3>
      <p><strong>Project Title:</strong> {order.title}</p>

      <h3>Reward Details</h3>
      <p><strong>Reward Name:</strong> {order.rewardName}</p>

      <h3>Package Details</h3>
      <p><strong>Package Name:</strong> {order.packageName}</p>
      <p><strong>Package Price:</strong> {order.packagePrice}</p>
      <p><strong>Package Count:</strong> {order.packageCount}</p>

      <h3>Payment Details</h3>
      <p><strong>Payment Method:</strong> {order.paymentMethod}</p>
      <p><strong>Payment Status:</strong> {order.paymentStatus}</p>
    </div>
  );
};

export default MySupport;
