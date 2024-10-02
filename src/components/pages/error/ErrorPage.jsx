import React from "react";
import Game from "./Game";

import '../../styles/style.css'
import { Header } from "../../layout/Header";
import { Footer } from "../../layout/Footer";

const ErrorPage = () => {
  const handleGoBack = () => {
    window.history.back();
  };

  const handleGoHome = () => {
    window.location.href = "/"; // 메인 페이지 URL로 변경
  };

  return (
    <>
    <Header />
     	 <div className="container">

    <div className="error-container">
      <h1>잘못된 접근입니다.</h1>
      <p>
        <Game />
      </p>
      <p>불편드려 죄송합니다.</p>
      <p>계속 이 페이지가 나온다면 아래 메일로 연락주시기 바랍니다.</p>
      <p>이메일: example@example.com</p>
      <div className="button-container">
        <button onClick={handleGoBack}>이전 페이지로 가기</button>
        <button onClick={handleGoHome}>메인으로 가기</button>
      </div>
    </div>
    </div>
    <Footer />
    </>

  );
};

export default ErrorPage;
