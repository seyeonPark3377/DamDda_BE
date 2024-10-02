import React, { useState, useEffect } from "react";
import {
  MDBCol,
  MDBContainer,
  MDBRow,
  MDBCard,
  MDBCardBody,
  MDBTypography,
} from "mdb-react-ui-kit";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Avatar from "@mui/joy/Avatar";
import Modal from "./EditModal"; // 비밀번호 모달 컴포넌트
import axios from "axios"; // API 호출을 위해 axios를 import

export default function ProfileStatistics({ setIsEditing }) {
  const [profile, setProfile] = useState(null); // 사용자 프로필 정보 상태
  const [isModalOpen, setIsModalOpen] = useState(false); // 비밀번호 모달 상태
  const [passwordError, setPasswordError] = useState(""); // 비밀번호 에러 메시지
  const [password, setPassword] = useState("1234"); // 초기 비밀번호
  const [passwordDisplay, setPasswordDisplay] = useState(""); // 비밀번호 표시 상태

  // 프로필 데이터를 API에서 불러오는 함수 (주석 처리)
  // const fetchProfileData = async () => {
  //   try {
  //     const response = await axios.get('/api/members/profile?id=shine2462'); // 프로필 API 호출
  //     const profileData = response.data;
  //     setProfile(profileData);
  //     setPassword(profileData.password); // 비밀번호 설정
  //     setPasswordDisplay('*'.repeat(profileData.password.length)); // 비밀번호를 별표로 표시
  //   } catch (error) {
  //     console.error('프로필 데이터를 불러오는 중 오류 발생:', error);
  //   }
  // };

  // 로컬 스토리지에서 데이터를 불러오는 함수 (임시로 사용)
  const fetchProfileData = async () => {
    try {
      const savedProfileData = localStorage.getItem("profileData");
      if (savedProfileData) {
        const parsedData = JSON.parse(savedProfileData);
        setProfile(parsedData);
        setPassword(parsedData.password); // 로컬 스토리지의 비밀번호 설정
        setPasswordDisplay("*".repeat(parsedData.password.length)); // 비밀번호를 별표로 표시
      } else {
        // 로컬 스토리지에 데이터가 없을 때 초기 비밀번호 설정
        const initialProfileData = {
          loginId: "shine2462",
          name: "김철수",
          email: "shine2462@naver.com",
          nickname: "수세미",
          phoneNumber: "010-1234-5678",
          password: "00000000", // 초기 비밀번호
          address: "서울시 성동구 oo동", // 기본 주소 추가
          imageUrl: null,
        };
        setProfile(initialProfileData);
        localStorage.setItem("profileData", JSON.stringify(initialProfileData)); // 로컬 스토리지에 저장
        setPassword(initialProfileData.password); // 비밀번호 설정
        setPasswordDisplay("*".repeat(initialProfileData.password.length)); // 비밀번호를 별표로 표시
      }
    } catch (error) {
      console.error("프로필 데이터를 불러오는 중 오류 발생:", error);
    }
  };

  useEffect(() => {
    fetchProfileData(); // 프로필 데이터 로드
  }, []);

  // 프로필 수정 버튼 클릭 시 모달 열기
  const handleProfileEdit = () => {
    setIsModalOpen(true); // 모달 열기
  };

  // 비밀번호 모달에서 확인 버튼 클릭 시 처리 로직
  const handlePasswordSubmit = (inputPassword) => {
    if (inputPassword === password) {
      setPasswordError(""); // 에러 메시지 초기화
      setIsModalOpen(false); // 모달 닫기
      setIsEditing(true); // 프로필 수정 페이지로 이동
    } else {
      setPasswordError("비밀번호가 틀렸습니다. 다시 입력해주세요.");
    }
  };

  if (!profile) {
    return <div>로딩 중...</div>;
  }

  return (
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignItems: "flex-start",
        minHeight: "100vh",
        // backgroundColor: "#fff",
      }}
    >
      <MDBContainer style={{ width: "200%" }}>
        <MDBRow className="justify-content-center">
          <MDBCol md="8" xl="6" className="d-flex justify-content-center">
            <MDBCard
              style={{
                width: "100%",
                // maxWidth: "600px",
                // borderRadius: "15px",
                backgroundColor: "transparent",
                // boxShadow: "none",
              }}
            >
              <MDBCardBody
                className="text-center d-flex flex-column align-items-center"
                style={{ paddingBottom: "50px" }}
              >
                {/* 프로필 이미지 */}
                <div
                  className="mt-3 mb-4"
                  style={{
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                  }}
                >
                  <Avatar
                    sx={{ width: 100, height: 100, marginTop: "20px" }}
                    src={profile.imageUrl}
                  />
                  <MDBTypography tag="h4" className="mt-3 mb-4">
                    {profile.nickname}
                  </MDBTypography>
                </div>

                {/* 사용자 정보 입력 폼 */}
                <Box
                  component="form"
                  sx={{
                    "& .MuiTextField-root": { m: 2, width: "90%" },
                    marginTop: 3,
                    marginLeft: "10px",
                  }}
                  noValidate
                  autoComplete="off"
                >
                  <TextField
                    label="아이디"
                    value={profile.loginId}
                    size="small"
                    variant="standard"
                    fullWidth
                    InputProps={{ readOnly: true }}
                  />
                  <TextField
                    label="이름"
                    value={profile.name}
                    size="small"
                    variant="standard"
                    fullWidth
                    InputProps={{ readOnly: true }}
                  />
                  <TextField
                    label="이메일"
                    value={profile.email}
                    size="small"
                    variant="standard"
                    fullWidth
                    InputProps={{ readOnly: true }}
                  />
                  <TextField
                    label="닉네임"
                    value={profile.nickname}
                    size="small"
                    variant="standard"
                    fullWidth
                    InputProps={{ readOnly: true }}
                  />

                  <TextField
                    label="비밀번호"
                    value={passwordDisplay} // 비밀번호 길이에 맞춘 별표 표시
                    size="small"
                    variant="standard"
                    fullWidth
                    type="password" // 입력 시에도 비밀번호는 *로 표시
                    InputProps={{ readOnly: true }}
                  />
                  <TextField
                    label="휴대폰 번호"
                    value={profile.phoneNumber}
                    size="small"
                    variant="standard"
                    fullWidth
                    InputProps={{ readOnly: true }}
                  />
                  <TextField
                    label="배송지"
                    value={profile.address}
                    size="small"
                    variant="standard"
                    fullWidth
                    InputProps={{ readOnly: true }}
                  />
                </Box>

                {/* 프로필 수정 버튼 */}
                <div
                  style={{
                    display: "flex",
                    justifyContent: "center",
                    marginTop: "10px",
                  }}
                >
                  <MDBTypography
                    tag="span"
                    onClick={handleProfileEdit} // 수정 버튼 클릭 시 모달 열기
                    style={{
                      color: "#999",
                      textDecoration: "underline",
                      cursor: "pointer",
                      fontSize: "12px",
                    }}
                  >
                    프로필 수정
                  </MDBTypography>
                </div>
              </MDBCardBody>
            </MDBCard>
          </MDBCol>
        </MDBRow>
      </MDBContainer>

      {/* 비밀번호 입력 모달 */}
      <Modal
        open={isModalOpen} // 모달이 열려 있는지 여부
        onClose={() => setIsModalOpen(false)} // 모달 닫기
        onSubmit={handlePasswordSubmit} // 비밀번호 확인 로직
        currentPassword={password}
        errorMessage={passwordError} // 비밀번호 오류 메시지
      />
    </div>
  );
}
