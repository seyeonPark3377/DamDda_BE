import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import axios from 'axios';
import styles from './PaymentSuccess.module.css';  // CSS Modules import

import '../../styles/style.css'
import { Header } from "../../layout/Header";
import { Footer } from "../../layout/Footer";

import cart from '../../assets/cart.png'
const PaymentSuccess = () => {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const orderId = queryParams.get('orderId');  // URL 쿼리에서 orderId 가져옴

  const [orderData, setOrderData] = useState([]); // 주문 데이터를 저장할 상태
  const [loading, setLoading] = useState(true); // 로딩 상태 관리
  const [error, setError] = useState(null); // 에러 상태 관리

  // 주문 정보를 가져오는 함수
  const fetchOrderData = async () => {
    try {
      // if (!orderId) {
      //   throw new Error('주문 ID가 없습니다.');
      // }
      // orderId로 주문 정보 요청
      // const response = await axios.get(`http://localhost:9000/order/details/${orderId}`);

      const response = await axios.get(`http://localhost:9000/order/details/${orderId}`);
      setOrderData(response.data);
      setLoading(false); // 데이터를 가져왔으므로 로딩 완료
    } catch (err) {
      setError(err.message);
      setLoading(false); // 에러가 발생해도 로딩 완료
    }
  };

  // 컴포넌트가 마운트될 때 주문 정보 가져오기
  useEffect(() => {
    fetchOrderData();
  }, []);

  if (loading) {
    return <p>로딩 중...</p>; // 로딩 중일 때 표시할 내용
  }

  if (error) {
    return <p>에러 발생: {error}</p>; // 에러 발생 시 표시할 내용
  }


  if (!orderData || !orderData.supportingPackage || !orderData.delivery || !orderData.payment) {
    return <p>주문 정보를 불러오는 중입니다...</p>;
  }
  
  return (
    <>
      <Header />
      <div className="container">
        <div className={styles['success-container']}>
          <div className={styles['success-header']}>
            <img src={cart} alt="Cart Icon" className={styles['success-image']} />
            <h1>주문이 완료되었습니다!</h1>
            <p>선물은 정상 접수 완료되었으며 배송을 시작합니다!</p>
            <div className={styles['success-buttons']}>
              <button className={styles['my-orders-btn']}>마이페이지</button>
              <button className={styles['other-projects-btn']}>후원한 프로젝트 보기</button>
            </div>
          </div>

          <div className={styles['order-summary-section']}>
            <div className={styles['order-title']}>주문 상품</div>
            <table>
              <thead>
                <tr>
                  <th>상품명</th>
                  <th>주문 일자</th>
                  <th>수량</th>
                  <th>결제 금액</th>
                </tr>
              </thead>
              <tbody>
                  {orderData.supportingPackage && orderData.supportingProject ? (
                    <tr>
                      <td>{orderData.supportingPackage.packageName}</td>
                      <td>{new Date(orderData.supportingProject.supportedAt).toLocaleDateString()}</td>
                      <td>{orderData.supportingPackage.packageCount}</td>
                      <td>{parseInt(orderData.supportingPackage.packagePrice).toLocaleString()}원</td> {/* packagePrice로 수정 */}
                    </tr>
                  ) : (
                    <tr>
                      <td colSpan="4">주문 상품이 없습니다.</td>
                    </tr>
                  )}
                </tbody>

            </table>
          </div>

          <div className={styles['details-section']}>
            <div className={styles['shipping-info']}>
              <div className={styles['order-title']}>배송지 정보</div>
              <div className={styles['detail-section-content']}>
                <p>이름: {orderData.delivery.deliveryName}</p>
                <p>전화번호: {orderData.delivery.deliveryPhoneNumber}</p>
                <p>배송지 주소: {orderData.delivery.deliveryAddress+"  ("+orderData.delivery.deliveryDetailedAddress+")"}</p>
              </div>
            </div>

            <div className={styles['payment-info']}>
              <div className={styles['order-title']}>결제 정보</div>
              <div className={styles['detail-section-content']}>
                <p>결제 수단: {orderData.payment.paymentMethod}</p>
                <p>결제 금액: {parseInt(orderData.supportingPackage.packagePrice).toLocaleString()}원</p>
                </div>
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default PaymentSuccess;
