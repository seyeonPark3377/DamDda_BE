// src/components/MainContent.jsx
import {React, useState, useRef, useEffect} from 'react';
import { useNavigate } from 'react-router-dom';
import { Footer } from '../../layout/Footer';
import NewSection from './NewSection'; // NewsSection 대신 NewSection으로 변경
import { Category } from '../../layout/Category';
import { Header } from '../../layout/Header';
import { CarouselComponent } from './Carousel';
import {CollaborationSection} from './Collaboration'
import {ServiceCards} from './ServiceCards'
import { Box } from "@mui/material";

// import { Payment } from '../support/payment';
// import Banner1 from '../../assets/banner-1.png'
import Banner2 from '../../assets/Banner2.png'
import {ProductRecommendations} from './Product';
import { SearchBar } from '../../layout/SearchBar';
import "./MainBanner.css";
import "../../styles/style.css"
function Main() {
  const navigate = useNavigate();
  
  const [cartegory, setCartegory] = useState(' 전체');
  const [search, setSearch] = useState('');
  const isFirstRender = useRef(true); // 처음 렌더링 여부 추적

  // cartegory 또는 search가 바뀔 때 실행되는 useEffect
  useEffect(() => {
    if (isFirstRender.current) {
      isFirstRender.current = false; // 첫 렌더링 이후로는 false로 설정
      return;
    }

    const fetchData = () => {
      navigate(`/entire?category=${cartegory}&search=${search}`);
      
      // 이곳에 데이터 요청이나 다른 비동기 작업을 수행하면 됩니다.
    };

    fetchData();
  }, [cartegory, search]); // 의존성 배열에 cartegory와 search 추가


  // useEffect(() => {
  //   if (isFirstRender.current) {
  //     // 처음 렌더링 시에는 실행되지 않도록 함
  //     isFirstRender.current = false;
  //     return;
  //   }
    
  //   // 이후 상태가 변경될 때만 navigate 호출
  //   navigate(`/entire?category=${cartegory}&search=${search}`);
  // }, [cartegory, search, navigate]);
  
  return (
    <>
      <Header search={search} setSearch={setSearch}/>

      
    <Box
        sx={{
          margin: '0 auto',
          width: "80%",
          maxWidth: "1750px",
          minWidth: '600px',
        }}
      >
      
      <CarouselComponent />
      <Category setCartegory={setCartegory}/>
      <SearchBar search={search} setSearch={setSearch}/>

      <ServiceCards></ServiceCards>
      
      <ProductRecommendations sortCondition={"likeCnt"} title={"인기 프로젝트"} subTitle={"좋아요가 가장 많은 프로젝트"}></ProductRecommendations>
      <ProductRecommendations sortCondition={"endDate"} title={"마감 임박 프로젝트"} subTitle={"마감임박! 마지막 기회 놓치지 말아요!"}></ProductRecommendations>

      <div className="banner-container2">
            <img
              src={Banner2}
              alt="Banner"
              className="banner-image2"
            />
          </div>
      <ProductRecommendations sortCondition={"all"} title={"사용자 추천 프로젝트"} subTitle={"나에게 딱 맞는 프로젝트."}></ProductRecommendations>
      <ProductRecommendations sortCondition={"viewCnt"} title={"최다 조회 프로젝트"} subTitle={"많은 사람들이 구경한 프로젝트"}></ProductRecommendations>

      <ProductRecommendations sortCondition={"targetFunding"} title={"최다 후원 프로젝트"} subTitle={"많은 사람들의 이유있는 후원! 후원금이 가장 많은 프로젝트!"}></ProductRecommendations>
      <NewSection />

      <CollaborationSection></CollaborationSection>
     
      <Footer />
      {/* <Payment /> */}
      </Box>
      

    </>
  );
}

export default Main;
