

import React from 'react';
import { BrowserRouter as Router, Route, Routes} from 'react-router-dom';

/////////////////////////////지영////////////////////////////////////
import Home from './components/pages/mainpage/Main'; 
import Payment from './components/pages/support/payment';
import OrderPage from './components/pages/support/OrderPage';
import PaymentSuccess from "./components/pages/support/PaymentSuccess";
import MyOrders from './components/pages/support/MyOrders';
/////////////////////////////남희////////////////////////////////////
import YourPage from './components/layout/YourPage';
import Login from './components/pages/member/Login';
import FindID from'./components/pages/member/FindID';
import ResetPassword from './components/pages/member/ResetPassword';
import Join from './components/pages/member/Join';
import Detail from './components/pages/detail/Detail';
import Register from './components/pages/register/Register';
import ErrorPage from './components/pages/error/ErrorPage';

/////////////////////////////혜원////////////////////////////////////
import MyPage from './components/pages/mypage/Mypage';
import styles from './components/styles/style.css'
import ProjectDetail from './components/pages/detail/ProjectDetail';

/////////////////////////////주현////////////////////////////////////
import Entire from './components/pages/entire/Main';
function App() {
 

  return (
    <Router>
      <Routes>
      {/* /////////////////////////////지영//////////////////////////////////// */}
        <Route path="/" element={<Home />} />  

        <Route path="/order" element={<OrderPage />} />
        <Route path="/user/myorders/:userId" element={<MyOrders />} />
        <Route path="/yourpage" element={<YourPage />} />
        <Route path="/payment" element={<Payment />} />
        <Route path="/payment/success" element={<PaymentSuccess />} />
      {/* /////////////////////////////남희/////////////////////////////////// */}
        <Route path="/login" element={<Login />} />
        <Route path="/find-id" element={<FindID />} />
        <Route path="/reset-password" element={<ResetPassword />} />
        <Route path="/join" element={<Join />} />
        <Route path="/detail" element={<Detail />} />
        <Route path="/register" element={<Register />} />
        <Route path="/error" element={<ErrorPage />} />
        <Route path="/projectDetail" element={<ProjectDetail />} />

      {/* /////////////////////////////혜원/////////////////////////////////// */}
      <Route path="/mypage" element={<MyPage />} />
      {/* /////////////////////////////주현/////////////////////////////////// */}

      <Route path="/entire" element={<Entire />} />



        </Routes>
    </Router>
  );
}

export default App;
