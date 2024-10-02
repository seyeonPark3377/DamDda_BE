import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const OrderPage = () => {
    const [orderInfo, setOrderInfo] = useState({
        projectTitle: '',
        giftSet: '',
        options: '',
        price: 0,
        quantity: 0
    });
//완료
    const navigate = useNavigate();

    const handleChange = (e) => {
        setOrderInfo({
            ...orderInfo,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = () => {
        // 페이지 이동 시 선택된 정보를 state로 전달
        navigate('/payment', { state: orderInfo });
    };

    return (
        <div>
            <input name="projectTitle" placeholder="Project Title" onChange={handleChange} />
            <input name="giftSet" placeholder="Gift Set" onChange={handleChange} />
            <input name="options" placeholder="Options" onChange={handleChange} />
            <input name="price" type="number" placeholder="Price" onChange={handleChange} />
            <input name="quantity" type="number" placeholder="Quantity" onChange={handleChange} />
            <button onClick={handleSubmit}>Go to Review</button>
        </div>
    );
};

export default OrderPage;
