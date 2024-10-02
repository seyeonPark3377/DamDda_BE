import React, { useState, useEffect } from "react";
import ProfileEditPage from "./ProfileEditPage";
import Myproject from "./MyProject";
import Headers from "./MypageHeader";
import MyProjectDetail from "./MyProjectDetail";
import ProfileStatistics from "./ProfileStatistics";
import SupportedProjects from "./SupportedProjects";
import TabsUnderlinePlacement from "./TabsUnderlinePlacement";
import LikeProject from "./LikeProject";
import "../../styles/style.css";
import { Header } from "../../layout/Header";
import { Footer } from "../../layout/Footer";
import MypageHeader from "./MypageHeader";

const Mypage = () => {
  const [profileData, setProfileData] = useState(null); // 프로필 데이터 상태
  const [isLoading, setIsLoading] = useState(true); // 로딩 상태
  const [selectedTab, setSelectedTab] = useState(0); // 현재 선택된 탭 상태
  const [isEditing, setIsEditing] = useState(false); // 프로필 수정 상태

  // 가짜 데이터를 사용하여 프로필 데이터 설정
  const mockFetchProfileData = () => {
    const mockData = {
      loginId: "shine2462",
      name: "김철수", // 사용자 이름
      email: "shine2462@naver.com",
      nickname: "",
      phoneNumber: "010-1234-5678",
      address: "서울시 성동구 oo동",
    };
    setProfileData(mockData);
    setIsLoading(false); // 로딩 완료
  };

  useEffect(() => {
    mockFetchProfileData(); // 가짜 데이터 로드 (백엔드 준비되면 fetchProfileData로 대체)
  }, []);

  const updateProfileData = (updateData) => {
    setProfileData(updateData); // 업데이트 된 데이터를 저장
  };

  // 데이터를 로딩 중일 때 표시할 화면
  if (isLoading) {
    return <p>로딩 중...</p>;
  }

  // 탭에 따라 페이지를 렌더링
  const renderSelectedTabContent = () => {
    if (isEditing) {
      // 수정 모드일 때는 ProfileEditPage 렌더링
      return (
        <ProfileEditPage
          profile={profileData}
          setIsEditing={setIsEditing}
          updateProfileData={updateProfileData} // 업데이트된 데이터를 전달받음
        />
      );
    }

    // 탭에 따라 페이지를 렌더링
    switch (selectedTab) {
      case 0:
        return (
          <ProfileStatistics
            profile={profileData}
            setIsEditing={setIsEditing}
          />
        );
      case 1:
        return <SupportedProjects />;
      case 2:
        return <Myproject />;
      case 3:
        return <LikeProject />;
      case 4:
        return <MyProjectDetail/>;
      default:
        return (
          <ProfileStatistics
            profile={profileData}
            setIsEditing={setIsEditing}
          />
        );
    }
  };

  return (
    <>
      <Header />
      <div className="container">
        <div>
          {/* 헤더와 탭은 공통으로 표시됩니다 */}
          {/* <Headers nickname={profileData.nickname || "사용자"} />{" "} */}
          {/* <Headers profile={profileData.nickname || "사용자"} />{" "} */}
          {/* <Headers profileData={profileData} /> */}
          <MypageHeader nickname={profileData.nickname || "사용자"} />
          <TabsUnderlinePlacement
            selectedTab={selectedTab}
            setSelectedTab={setSelectedTab}
          />
          {/* 각 탭에 맞는 콘텐츠를 조건부 렌더링 */}
          {renderSelectedTabContent()}
        </div>
      </div>
      <Footer />
    </>
  );
};

export default Mypage;
