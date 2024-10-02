import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom'; // useNavigate와 useLocation 훅을 import
import { loadTossPayments } from "@tosspayments/payment-sdk";
import { Radio, RadioGroup, FormControlLabel, FormControl } from '@mui/material';
import kakaopay from '../../assets/kakao.png'; // 로고 파일
import tosspay from '../../assets/toss.png'; // 로고 파일
import naverpay from '../../assets/naver.png'; // 로고 파일

//완료//
import axios from 'axios';
import './payment.css';
import '../../styles/style.css'
import { Header } from "../../layout/Header";
import { Footer } from "../../layout/Footer";

function Payment() {
  const navigate = useNavigate();
  const location = useLocation(); // 이전 페이지에서 전달된 데이터 접근
  const [orderInfo, setOrderInfo] = useState(location.state || { // 전달된 state가 없을 경우 기본값 설정
    projectTitle: '',
    giftSet: '',
    options: '',
    price: 0,
    quantity: 1,
    name: '',
    phoneNumber: '',
    email: '',
    postalCode: '',
    address: '',
    extraAddress: '',
    detailAddress: '',
    request: '', // 배송 메시지 필드
  });

  const [paymentMethod, setPaymentMethod] = useState(''); // 결제 수단 상태
  const [showCustomMessageInput, setShowCustomMessageInput] = useState(false); // 배송 메시지 입력 필드 상태
  const [customMessage, setCustomMessage] = useState(''); // 사용자 입력 배송 메시지
  const userId=1;
  const projectId=2;

  useEffect(() => {
    
    console.log("OrderInfo updated: ", orderInfo);

  }, [orderInfo]);
  
  // Dynamically load Daum Postcode script when the component mounts
  useEffect(() => {
    const script = document.createElement('script');
    script.src = 'https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
    script.async = true;
    document.body.appendChild(script);
    return () => {
      document.body.removeChild(script);
    };
  }, []);

  const handleOrderChange = (e) => {
    setOrderInfo({
      ...orderInfo,
      [e.target.name]: e.target.value,
    });
  };

  // 결제 수단 변경 처리
  const handlePaymentChange = (e) => {
    setPaymentMethod(e.target.value); // 선택한 결제 수단 반영
  };

  const handleDeliveryMessageChange = (e) => {
    const selectedValue = e.target.value;
  
    // "직접 입력"이 선택되었을 때는 customMessage를 사용
    if (selectedValue === "직접 입력") {
      setShowCustomMessageInput(true); 
      setOrderInfo({
        ...orderInfo,
        request: "", // "직접 입력" 상태 유지
      });
    } else {
      // 다른 메시지를 선택할 때는 customMessage를 초기화
      setShowCustomMessageInput(false);
      setCustomMessage(""); // 직접 입력 창 비활성화 시 메시지 초기화
      setOrderInfo({
        ...orderInfo,
        request: selectedValue, // 배송 메시지를 업데이트
      });
    }
  };
  
  const handleCustomMessageChange = (e) => {
    const selectedValue = e.target.value;
  
    setCustomMessage(selectedValue); // 사용자 정의 메시지 반영
    setOrderInfo({
      ...orderInfo,
      request: "직접 입력", // 사용자 정의 입력 시 request는 "직접 입력" 상태 유지
    });
  };

  // 주문 정보 제출 및 결제 처리
const handleSubmit = async () => {
  const deliveryMessage = orderInfo.customMessage || orderInfo.request; // 사용자 입력 메시지가 있으면 우선 사용

  const orderData = {
    delivery: {
      deliveryName: orderInfo.name,
      deliveryPhoneNumber: orderInfo.phoneNumber,
      deliveryEmail: orderInfo.email,
      deliveryAddress: orderInfo.address,
      deliveryDetailedAddress: orderInfo.detailAddress,
      deliveryPostCode: orderInfo.postalCode,  // 우편번호를 문자열로 변환
      deliveryMessage: deliveryMessage, // 최종 메시지
    },
    payment: {
      paymentMethod: paymentMethod,
      paymentStatus: '결제 대기중', // 초기 상태
    },
    supportingProject: {
      user: {
        id: userId  // 사용자 ID
      },
      project: {
        id: projectId  // 프로젝트 ID
      },
      supportedAt: new Date() // 후원 날짜
  },
    supportingPackage: {
      packageName: orderInfo.giftSet, // 패키지 이름
      packagePrice: orderInfo.price * orderInfo.quantity, // 결제 금액
      packageCount: orderInfo.quantity, // 패키지 수량 추가
    },
    
  };

  try {
    
    // 주문 정보 생성 POST 요청 (결제 대기중 상태로 먼저 저장)
    console.log('Order Data:', orderData); // 서버로 전송 전에 데이터 확인
    const response = await axios.post('http://localhost:9000/order/create', orderData);
    console.log('주문생성 완료 :',response)
    
    // 서버에서 반환된 orderId 가져오기
    const createdOrderId = response.data; // response.data는 orderId 값
    console.log('주문 ID:', createdOrderId);
   
    // 결제 수단에 따른 처리
    if (paymentMethod === 'tossPay') {
      const tossPayments = await loadTossPayments('test_ck_D5GePWvyJnrK0W0k6q8gLzN97Eoq');
      tossPayments.requestPayment('카드', {
        amount: orderInfo.price * orderInfo.quantity + 3000,
        orderId: createdOrderId, // 저장된 주문 ID를 사용
        orderName: orderInfo.projectTitle || '펀딩 결제',
        customerName: orderInfo.name || '테스트 사용자',
        successUrl: `http://localhost:9000/toss/success?orderId=${createdOrderId}`, // 성공 시 orderId 전달
        failUrl: `http://localhost:9000/toss/fail?orderId=${createdOrderId}`, // 실패 시 orderId 전달
      }).then(() => {
        console.log('결제 성공');
      }).catch(function (error) {
        console.error('결제 실패:', error);

      });
    } else if (paymentMethod === 'kakaoPay') {
        // 카카오페이 결제창 호출
        axios
          .post("http://localhost:9000/payment/kakao/ready", { orderId: createdOrderId })
          .then((res) => {
            window.location.href = res.data.next_redirect_pc_url; // 카카오페이 결제 페이지로 리디렉션
          })
          .catch((error) => {
            console.error("Error initiating payment:", error);
          });
}

  } catch (error) {
    console.error('There was an error creating the order:', error);
  }
};

// 결제 성공 후에 호출되는 로직 (백엔드에서 처리)
const handlePaymentSuccess = async (orderId) => {
  try {
    // 결제 성공 시 결제 상태를 '결제 완료'로 업데이트
    await axios.put(`http://localhost:9000/order/${orderId}/update-status`, {
      paymentStatus: '결제 완료',
    });
    console.log('Payment status updated to "결제 완료"');
    navigate(`/order-success?orderId=${orderId}`); // 결제 성공 페이지로 이동
  } catch (error) {
    console.error('Error updating payment status:', error);
  }
};


  const sample6_execDaumPostcode = () => {
    new window.daum.Postcode({
      oncomplete: function (data) {
        let addr = ''; // Address
        let extraAddr = ''; // Extra information

        if (data.userSelectedType === 'R') {
          addr = data.roadAddress;
        } else {
          addr = data.jibunAddress;
        }

        if (data.userSelectedType === 'R') {
          if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
            extraAddr += data.bname;
          }
          if (data.buildingName !== '' && data.apartment === 'Y') {
            extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
          }
          if (extraAddr !== '') {
            extraAddr = ' (' + extraAddr + ')';
          }
        }
       // 우편번호와 주소 정보를 업데이트
       console.log('우편번호:', data.zonecode);  // 콘솔에 우편번호 출력
       console.log('주소:', addr);  // 콘솔에 주소 출력

        // 우편번호와 주소 정보를 업데이트
        setOrderInfo({
          ...orderInfo,
          postalCode: data.zonecode,
          address: addr,
          extraAddress: extraAddr,
        });
      },
    }).open();
  };

  return (
    <>
    <Header />
          <div className="container">

    <div className="big-container">
      <div className="container">
      <div className="title">
      결제 페이지</div>
        <div className="order-summary">
          <h1>{orderInfo.projectTitle || '프로젝트 이름'}</h1>
          <p>선물 구성/옵션: {orderInfo.giftSet}</p>
          <p>가격 (원): {orderInfo.price}</p>
          <p>수량: {orderInfo.quantity}</p>
        </div>

        <div className="form-section">
          <h1>주문자 정보 수정</h1>
          <input
            type="text"
            name="name"
            placeholder="마이페이지에 등록된 이름"
            value={orderInfo.name}
            onChange={handleOrderChange}
            className="input"
          />
          <input
            type="text"
            name="phoneNumber"
            placeholder="마이페이지에 등록된 전화번호"
            value={orderInfo.phoneNumber}
            onChange={handleOrderChange}
            className="input"
          />
          <input
            type="email"
            name="email"
            placeholder="마이페이지에 등록된 이메일"
            value={orderInfo.email}
            onChange={handleOrderChange}
            className="input"
          />
        </div>

        <div className="form-section">
          <h1>배송지 정보</h1>
          <div className="address-group">
            <input
              type="text"
              id="sample6_postcode"
              name="postalCode"
              placeholder="우편번호"
              value={orderInfo.postalCode}
              readOnly
              className="input"
            />
            <button type="button" className="button-1" onClick={sample6_execDaumPostcode}>
              우편번호 찾기
            </button>
          </div>
          <input
            type="text"
            id="sample6_address"
            name="address"
            placeholder="주소"
            value={orderInfo.address}
            readOnly
            className="input"
          />
          <input
            type="text"
            id="sample6_detailAddress"
            name="detailAddress"
            placeholder="상세 주소"
            value={orderInfo.detailAddress}
            onChange={handleOrderChange}
            className="input"
          />

        <div className="delivery-message-container">
          <h1>배송 메시지</h1>
          <select value={orderInfo.request} onChange={handleDeliveryMessageChange} className="delivery-select">
            <option value="문앞에 놓아주세요">문앞에 놓아주세요</option>
            <option value="경비실에 맡겨주세요">경비실에 맡겨주세요</option>
            <option value="직접 받을 거예요">직접 받을 거예요</option>
            <option value="직접 입력">직접 입력</option>
          </select>
        </div>

          {showCustomMessageInput && (
            <div>
              <textarea
                className="custom-textarea" // 추가된 className
                value={customMessage}
                onChange={handleCustomMessageChange}
                maxLength={100}
                placeholder="직접 입력 (최대 100자)"
              />
          </div>
          
          )}
        </div>

        <div className="form-section">
          <h1>결제 수단</h1>
          <FormControl component="fieldset" className="payment-method-form">
            <RadioGroup
              className="payment-method-radio-group"
              aria-label="payment-method"
              name="payment-method"
              value={paymentMethod}
              onChange={handlePaymentChange}
            >
              <FormControlLabel
                className="payment-method-option"
                value="naverPay"
                control={<Radio color="success" size="medium" />}
                label={<><span>네이버 페이</span><img src={naverpay} alt="네이버페이" className="payment-logo" /></>}
              />
              <FormControlLabel
                className="payment-method-option"
                value="kakaoPay"
                control={<Radio color="success" size="medium" />}
                label={<><span>카카오페이</span><img src={kakaopay} alt="카카오페이" className="payment-logo" /></>}
              />
              <FormControlLabel
                className="payment-method-option"
                value="tossPay"
                control={<Radio color="success" size="medium" />}
                label={<><span>토스 페이</span><img src={tosspay} alt="토스페이" className="payment-logo" /></>}
              />
            </RadioGroup>
          </FormControl>

        </div>
      </div>

      <div className="small-container">
        <div className="payment-summary">
          <div className="payment-summary-title">최종 펀딩 금액</div>
          <div className='payment-summary-amount'>
              <p>상품 금액: {orderInfo.price} 원</p>
              <p>배송비: 3000 원</p>
              <p>최종 금액: {orderInfo.price * orderInfo.quantity + 3000} 원</p>
          </div>
          <div className="checkbox-group">
            <label>
              <input type="checkbox" />
              전체 동의
            </label>
            <label>
              <input type="checkbox" />
              구매조건, 결제 진행 및 결제 서비스 동의
            </label>
            <label>
              <input type="checkbox" />
              개인정보 제 3자 제공 동의
            </label>
          </div>
          <button onClick={handleSubmit} className="button-2">간편 결제 하기</button>

        </div>

      </div>
    </div>
    </div>
    <Footer />
    </>

  );
}

export default Payment;
