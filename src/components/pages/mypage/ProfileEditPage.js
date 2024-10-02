import React, { useState, useEffect, useRef } from "react";
import {
  Box,
  Button,
  Typography,
  TextField,
  Snackbar,
  Alert,
} from "@mui/material";
import Avatar from "@mui/joy/Avatar";
import PhotoCamera from "@mui/icons-material/PhotoCamera";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Modal from "@mui/material/Modal"; // Modal 컴포넌트 import

const mockProfileData = {
  loginId: "shine2462",
  name: "김철수",
  email: "shine2462@naver.com",
  nickname: "수세미",
  phoneNumber: "010-1234-5678",
  address: "서울시 성동구 oo동",
  password: "00000000", // 현재 비밀번호
  imageUrl: null,
};

export default function ProfileEditPage({
  profile = mockProfileData, // profile이 없으면 mockProfileData 사용
  setIsEditing,
  updateProfileData,
}) {
  const [formData, setFormData] = useState(mockProfileData);
  const [profileImage, setProfileImage] = useState(null);
  const [nicknameCheck, setNicknameCheck] = useState(false); // 닉네임 중복 확인 상태
  const [nicknameError, setNicknameError] = useState(""); // 닉네임 중복 확인 에러 메시지
  const [modalOpen, setModalOpen] = useState(false); // 비밀번호 변경 모달 상태
  const [currentPassword, setCurrentPassword] = useState(""); // 현재 비밀번호
  const [newPassword, setNewPassword] = useState(""); // 새로운 비밀번호
  const [confirmPassword, setConfirmPassword] = useState(""); // 새로운 비밀번호 확인
  const [errorMessage, setErrorMessage] = useState(""); // 에러 메시지
  const [openSnackbar, setOpenSnackbar] = useState(false); // Snackbar 열림 상태
  const [isLoading, setIsLoading] = useState(true); // 로딩 상태 추가

  const fileInputRef = useRef(null);
  const navigate = useNavigate();

  const fetchProfileData = async () => {
    setIsLoading(true);
    try {
      const savedProfileData = localStorage.getItem("profileData");
      if (savedProfileData) {
        const parsedData = JSON.parse(savedProfileData);
        setFormData(parsedData);
        setProfileImage(parsedData.imageUrl);
      } else {
        localStorage.setItem("profileData", JSON.stringify(mockProfileData));
        setFormData(mockProfileData);
        setProfileImage(mockProfileData.imageUrl);
      }
    } catch (error) {
      console.error("프로필 데이터를 불러오는 중 오류 발생:", error);
    } finally {
      setIsLoading(false); // 로딩 종료
    }
  };

  useEffect(() => {
    fetchProfileData();
  }, []);

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setProfileImage(reader.result);
      };
      reader.readAsDataURL(file);
    }
  };
  const handleImageDelete = () => {
    setProfileImage(null);
    fileInputRef.current.value = "";
    return;
  };

  const handleSubmit = async () => {
    // 닉네임 중복 확인이 실패한 경우 프로필 저장을 중단
    if (!nicknameCheck) {
      setNicknameError("닉네임 중복 확인을 해주세요.");
      return; // 닉네임 중복 확인이 완료되지 않았으면 저장 중단
    }

    const updatedFormData = { ...formData, imageUrl: profileImage };

    // 부모 컴포넌트에 데이터 전달
    updateProfileData(updatedFormData);

    // 스낵바 열기
    setOpenSnackbar(true);

    try {
      // 백엔드에 데이터를 저장하는 로직 (주석 처리)
      // await axios.put('/members/profile', updatedFormData);

      // 모의 로컬 스토리지 저장
      localStorage.setItem("profileData", JSON.stringify(updatedFormData));
      setFormData(updatedFormData);

      // 스낵바가 화면에 나타날 시간을 주기 위해 지연
      setTimeout(() => {
        setIsEditing(false); // 저장 후 프로필 페이지로 이동
      }, 1000); // 1초 지연
    } catch (error) {
      console.error("프로필 저장 중 오류 발생:", error);
    }
  };

  const handleNicknameCheck = async () => {
    setNicknameError("");
    try {
      if (formData.nickname === "수세미") {
        setNicknameCheck(false);
        setNicknameError("이미 사용 중인 닉네임입니다.");
      } else {
        setNicknameCheck(true);
        setNicknameError("사용 가능한 닉네임입니다.");
      }
    } catch (error) {
      console.error("닉네임 중복 확인 중 오류 발생:", error);
      setNicknameError("닉네임 확인 중 오류가 발생했습니다.");
    }
  };

  // 비밀번호 변경 모달을 열고 닫는 함수
  const handleOpenModal = () => setModalOpen(true);
  const handleCloseModal = () => setModalOpen(false);

  const handlePasswordChange = async () => {
    setErrorMessage("");

    if (currentPassword !== formData.password) {
      setErrorMessage("현재 비밀번호가 틀립니다.");
      return;
    }

    if (newPassword.length < 8 || newPassword.length > 16) {
      setErrorMessage("비밀번호는 8자 이상 16자 이하로 입력해주세요.");
      return;
    }

    if (newPassword !== confirmPassword) {
      setErrorMessage("새 비밀번호가 일치하지 않습니다.");
      return;
    }

    try {
      setFormData((prev) => ({
        ...prev,
        password: newPassword,
      }));
      alert("비밀번호가 성공적으로 변경되었습니다.");
      handleCloseModal();
    } catch (error) {
      console.error("비밀번호 변경 중 오류 발생:", error);
    }
  };

  // Snackbar 닫기 함수
  const handleSnackbarClose = () => {
    setOpenSnackbar(false);
  };

  // 데이터를 불러오는 중일 때 화면에 표시될 메시지
  if (isLoading) {
    return <p>데이터를 불러오는 중입니다...</p>;
  }

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        marginTop: "20px",
      }}
    >
      <Avatar
        sx={{ width: 80, height: 80, marginBottom: "10px" }}
        src={profileImage}
      />

      <Button
        variant="outlined"
        component="label"
        sx={{
          backgroundColor: "#fff",
          borderColor: "#000",
          color: "#000",
          padding: "3px 12px",
          fontSize: "12px",
          width: "120px",
          "&:hover": { backgroundColor: "#f0f0f0", borderColor: "#000" },
        }}
      >
        <PhotoCamera
          sx={{ color: "#000", marginRight: "5px", fontSize: "16px" }}
        />
        프로필 변경
        <input
          type="file"
          hidden
          ref={fileInputRef}
          onChange={handleImageChange}
        />
      </Button>

      <Typography
        variant="caption"
        color="textSecondary"
        onClick={handleImageDelete}
        sx={{ cursor: "pointer", textDecoration: "underline", color: "#999" }}
      >
        현재 사진 삭제
      </Typography>

      <Box component="form" sx={{ width: "600px", mt: 3 }}>
        <TextField
          fullWidth
          label="아이디"
          name="loginId"
          value={formData.loginId}
          margin="normal"
          InputProps={{
            readOnly: true,
            sx: { backgroundColor: "#fafafa", color: "gray" },
          }}
        />
        <TextField
          fullWidth
          label="이름"
          name="name"
          value={formData.name}
          margin="normal"
          InputProps={{
            readOnly: true,
            sx: { backgroundColor: "#fafafa", color: "gray" },
          }}
        />

        <TextField
          fullWidth
          label="이메일"
          name="email"
          value={formData.email}
          onChange={(e) => setFormData({ ...formData, email: e.target.value })}
          margin="normal"
        />

        <Box sx={{ position: "relative", marginBottom: "16px" }}>
          <TextField
            fullWidth
            label="닉네임"
            name="nickname"
            value={formData.nickname}
            onChange={(e) => {
              setFormData({ ...formData, nickname: e.target.value });
              setNicknameCheck(false);
              setNicknameError("");
            }}
            margin="normal"
            InputProps={{
              endAdornment: (
                <Typography
                  variant="caption"
                  sx={{
                    position: "absolute",
                    right: 10,
                    top: "50%",
                    transform: "translateY(-50%)",
                    cursor: "pointer",
                    color: "#999",
                    textDecoration: "underline",
                  }}
                  onClick={handleNicknameCheck}
                >
                  중복 확인
                </Typography>
              ),
              sx: {
                "&:focus-within": {
                  borderColor: "transparent", // 포커스 시 테두리 색상 변경 방지
                },
                "& .MuiOutlinedInput-root.Mui-focused .MuiOutlinedInput-notchedOutline":
                  {
                    borderColor: "transparent", // 포커스 시 테두리 색상 변경 방지
                  },
              },
              disableUnderline: true, // 포커스 스타일 제거
            }}
            autoComplete="off" // 자동완성 비활성화
          />
          {nicknameError && (
            <Typography color={nicknameCheck ? "blue" : "error"}>
              {nicknameError}
            </Typography>
          )}
        </Box>

        {/* 비밀번호 변경을 위한 버튼 */}
        <TextField
          fullWidth
          label="비밀번호"
          name="password"
          type="password"
          value={formData.password}
          onClick={handleOpenModal}
          margin="normal"
          InputProps={{ readOnly: true }}
        />

        <TextField
          fullWidth
          label="전화번호"
          name="phoneNumber"
          value={formData.phoneNumber}
          onChange={(e) =>
            setFormData({ ...formData, phoneNumber: e.target.value })
          }
          margin="normal"
        />
        <TextField
          fullWidth
          label="주소"
          name="address"
          value={formData.address}
          onChange={(e) =>
            setFormData({ ...formData, address: e.target.value })
          }
          margin="normal"
        />
      </Box>

      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          marginTop: "10px",
          paddingBottom: "50px",
        }}
      >
        <Typography
          variant="caption"
          sx={{
            cursor: "pointer",
            textDecoration: "underline",
            color: "#999",
            marginRight: "20px",
          }}
          onClick={handleSubmit}
        >
          저장하기
        </Typography>
        <Typography
          variant="caption"
          sx={{ cursor: "pointer", textDecoration: "underline", color: "#999" }}
          onClick={() => setIsEditing(false)}
        >
          돌아가기
        </Typography>
      </Box>

      {/* 비밀번호 변경 모달 */}
      <Modal open={modalOpen} onClose={handleCloseModal}>
        <Box
          sx={{
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            width: 400,
            bgcolor: "background.paper",
            boxShadow: 24,
            p: 4,
          }}
        >
          <Typography id="modal-modal-title" variant="h6" component="h2">
            비밀번호 변경
          </Typography>
          <TextField
            fullWidth
            label="현재 비밀번호"
            type="password"
            value={currentPassword}
            onChange={(e) => setCurrentPassword(e.target.value)}
            margin="normal"
          />
          <TextField
            fullWidth
            label="새로운 비밀번호"
            type="password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            margin="normal"
            helperText="8-16자 이내로 입력하세요."
          />
          <TextField
            fullWidth
            label="새로운 비밀번호 확인"
            type="password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            margin="normal"
          />
          {errorMessage && (
            <Typography variant="body2" color="error" sx={{ mt: 2 }}>
              {errorMessage}
            </Typography>
          )}
          <Button
            variant="contained"
            onClick={handlePasswordChange}
            sx={{ mt: 2 }}
          >
            비밀번호 변경
          </Button>
          <Button
            variant="outlined"
            onClick={handleCloseModal}
            sx={{ mt: 2, ml: 2 }}
          >
            닫기
          </Button>
        </Box>
      </Modal>

      {/* 안내 메시지 (Snackbar) */}
      <Snackbar
        open={openSnackbar}
        autoHideDuration={1000}
        onClose={handleSnackbarClose}
      >
        <Alert
          onClose={handleSnackbarClose}
          severity="success"
          sx={{ width: "100%" }}
        >
          프로필이 성공적으로 업데이트되었습니다.
        </Alert>
      </Snackbar>
    </Box>
  );
}
