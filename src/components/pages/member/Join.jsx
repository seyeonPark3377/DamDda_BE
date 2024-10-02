import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import {
  Button,
  CssBaseline,
  TextField,
  FormControl,
  FormControlLabel,
  Checkbox,
  FormHelperText,
  Typography,
  Container,
} from "@mui/material/";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import styled from "styled-components";

import '../../styles/style.css'
import { Header } from "../../layout/Header";
import { Footer } from "../../layout/Footer";
const FormHelperTexts = styled(FormHelperText)`
  width: 100%;
  padding-left: 16px;
  font-weight: 700 !important;
  color: ${({ color }) => color || "#d32f2f"} !important;
`;

const Join = () => {
  const theme = createTheme();
  const [checked, setChecked] = useState(false);
  const [statusMessages, setStatusMessages] = useState({
    id: "",
    nickname: "",
    register: "",
  });
  const [errors, setErrors] = useState({});
  const [formData, setFormData] = useState({
    id: "",
    password: "",
    password_confirm: "",
    name: "",
    nickname: "",
    email: "",
    phone_number: "",
    address: "",
    detailed_address: "",
  });

  const navigate = useNavigate();

  useEffect(() => {
    const script = document.createElement("script");
    script.src =
      "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
    script.async = true;
    document.body.appendChild(script);

    return () => {
      document.body.removeChild(script);
    };
  }, []);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleAgree = (event) => {
    setChecked(event.target.checked);
  };

  const handleCancel = () => {
    navigate(-1);
  };

  // 아이디 유효성 체크
  const checkIdDuplicate = async (event) => {
    event.preventDefault();
    const { id } = formData;

    if (id.length < 4) {
      setStatusMessages((prev) => ({
        ...prev,
        id: "아이디는 4자 이상이어야 합니다.",
      }));
      setErrors({ id: "아이디는 4자 이상이어야 합니다." });
      return;
    }

    try {
      const response = await axios.post("/member/check-id", { id });
      const available = response.data.available;

      setStatusMessages((prev) => ({
        ...prev,
        id: available
          ? "사용 가능한 아이디입니다."
          : "이미 사용 중인 아이디입니다.",
      }));
      setErrors({ id: available ? "" : "이미 사용 중인 아이디입니다." });
    } catch (err) {
      console.error(err);
      setStatusMessages((prev) => ({
        ...prev,
        id: "아이디 확인 중 오류가 발생했습니다. 다시 시도해주세요.",
      }));
      setErrors({ id: "아이디 확인 중 오류가 발생했습니다." });
    }
  };

  const checkNickNameDuplicate = async (event) => {
    event.preventDefault(); // Prevent default action
    const { nickname } = formData;
    try {
      const response = await axios.post("/member/check-nickname", { nickname });
      setStatusMessages((prev) => ({
        ...prev,
        nickname: response.data.available
          ? "사용 가능한 닉네임입니다."
          : "이미 사용중인 닉네임입니다.",
      }));
      setErrors((prev) => ({
        ...prev,
        nickname: response.data.available ? "" : "중복된 닉네임입니다.",
      }));
    } catch (err) {
      console.error(err);
      setStatusMessages((prev) => ({
        ...prev,
        nickname: "닉네임 확인에 실패했습니다.",
      }));
    }
  };

  const handleAddressSearch = () => {
    new window.daum.Postcode({
      oncomplete: function (data) {
        let addr = ""; // 주소 변수
        let extraAddr = ""; // 참고항목 변수

        // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
        if (data.userSelectedType === "R") {
          addr = data.roadAddress; // 도로명 주소
        } else {
          addr = data.jibunAddress; // 지번 주소
        }

        // 참고항목 조합
        if (data.userSelectedType === "R") {
          if (data.bname !== "" && /[동|로|가]$/g.test(data.bname)) {
            extraAddr += data.bname;
          }
          if (data.buildingName !== "" && data.apartment === "Y") {
            extraAddr +=
              extraAddr !== "" ? `, ${data.buildingName}` : data.buildingName;
          }
          if (extraAddr !== "") {
            extraAddr = ` (${extraAddr})`;
          }
        }

        // 주소 데이터 업데이트
        setFormData({
          ...formData,
          postcode: data.zonecode,
          address: addr,
          extraAddress: extraAddr,
        });

        // 상세주소 필드로 커서 이동
        document.getElementById("detailAddress").focus();
      },
    }).open();
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const {
      id,
      password,
      password_confirm,
      name,
      nickname,
      email,
      phone_number,
      address,
      detailed_address,
    } = formData;

    const currentErrors = {
      id: id.length < 4 ? "아이디는 4자 이상이어야 합니다." : "",
      password: !/^.{8,16}$/.test(password)
        ? "비밀번호는 8-16자리 이상 입력해주세요!"
        : "",
      password_confirm:
        password !== password_confirm ? "비밀번호가 일치하지 않습니다." : "",
      name:
        /^[가-힣a-zA-Z]+$/.test(name) && name.length >= 1
          ? ""
          : "올바른 이름을 입력해주세요.",
      nickname: nickname.length >= 1 ? "" : "닉네임을 입력해주세요.",
      email: /^[A-Za-z0-9._-]+@[a-zA-Z0-9]+\.[a-zA-Z0-9]{2,6}$/.test(email)
        ? ""
        : "올바른 이메일 형식이 아닙니다.",
      phone_number: /^([0-9]{2,4})-([0-9]{3,4})-([0-9]{4})$/.test(phone_number)
        ? ""
        : "올바른 연락처를 입력해주세요.",
      address: address.length >= 1 ? "" : "주소를 입력해주세요.",
      detailed_address:
        address && detailed_address.length < 1
          ? "상세주소를 입력해주세요."
          : "",
    };

    setErrors(currentErrors);

    if (!Object.values(currentErrors).some((error) => error)) {
      onhandlePost(formData);
    }
  };

  const onhandlePost = async (data) => {
    const postData = {
      id: data.id,
      password: data.password,
      name: data.name,
      nickname: data.nickname,
      email: data.email,
      phone_number: data.phone_number,
      ...(data.address && { address: data.address }),
      ...(data.detailed_address && { detailed_address: data.detailed_address }),
    };

    try {
      const response = await axios.post("/member/join", postData);
      console.log(response, "성공");
      navigate("/login");
    } catch (err) {
      console.log(err);
      setStatusMessages((prev) => ({
        ...prev,
        register: "회원가입에 실패하였습니다. 다시 한 번 확인해 주세요.",
      }));
    }
  };

  return (
    <>
    <Header />
     	 <div className="container">
    <ThemeProvider theme={theme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Typography component="h1" variant="h5">
          회원가입
        </Typography>
        <form noValidate onSubmit={handleSubmit}>
          <FormControl component="fieldset" variant="standard">
            <div>
              <div style={{ display: "flex", alignItems: "center" }}>
                <TextField
                  required
                  id="id"
                  name="id"
                  label="아이디"
                  variant="standard"
                  value={formData.id}
                  onChange={handleChange}
                  error={Boolean(errors.id)}
                  helperText={errors.id}
                />
                <Button
                  variant="outlined"
                  onClick={checkIdDuplicate}
                  sx={{ mt: 1 }}
                >
                  중복 확인
                </Button>
              </div>
              <FormHelperTexts
                color={
                  statusMessages.id.includes("사용 가능한 아이디입니다.")
                    ? "green"
                    : "red"
                }
              >
                {statusMessages.id}
              </FormHelperTexts>

              <TextField
                required
                fullWidth
                type="password"
                id="password"
                name="password"
                label="비밀번호"
                variant="standard"
                value={formData.password}
                onChange={handleChange}
                error={Boolean(errors.password)}
                helperText={errors.password}
              />
              <TextField
                required
                fullWidth
                type="password"
                id="password_confirm"
                name="password_confirm"
                label="비밀번호 확인"
                variant="standard"
                value={formData.password_confirm}
                onChange={handleChange}
                error={Boolean(errors.password_confirm)}
                helperText={errors.password_confirm}
              />

              <TextField
                required
                fullWidth
                id="name"
                name="name"
                label="이름"
                variant="standard"
                value={formData.name}
                onChange={handleChange}
                error={Boolean(errors.name)}
                helperText={errors.name}
              />

              <div style={{ display: "flex", alignItems: "center" }}>
                <TextField
                  required
                  id="nickname"
                  name="nickname"
                  label="닉네임"
                  variant="standard"
                  value={formData.nickname}
                  onChange={handleChange}
                  error={Boolean(errors.nickname)}
                  helperText={errors.nickname}
                />
                <Button
                  variant="outlined"
                  onClick={checkNickNameDuplicate}
                  sx={{ mt: 1 }}
                >
                  중복 확인
                </Button>
              </div>
              <FormHelperTexts
                color={
                  statusMessages.nickname.includes("사용 가능한 닉네임입니다.")
                    ? "green"
                    : "red"
                }
              >
                {statusMessages.nickname}
              </FormHelperTexts>

              <TextField
                required
                fullWidth
                id="email"
                name="email"
                label="이메일 주소"
                variant="standard"
                value={formData.email}
                onChange={handleChange}
                error={Boolean(errors.email)}
                helperText={errors.email}
              />

              <TextField
                required
                fullWidth
                id="phone_number"
                name="phone_number"
                label="연락처"
                variant="standard"
                value={formData.phone_number}
                onChange={handleChange}
                error={Boolean(errors.phone_number)}
                helperText={errors.phone_number}
              />

              <div style={{ display: "flex", alignItems: "center" }}>
                <TextField
                  fullWidth
                  id="postcode"
                  name="postcode"
                  label="우편번호"
                  variant="standard"
                  value={formData.postcode}
                  onChange={handleChange}
                />
                <Button
                  variant="outlined"
                  onClick={handleAddressSearch}
                  sx={{ mb: 1, mr: 1, mt: 1 }}
                >
                  우편번호 검색
                </Button>
                
              </div>
              <br />
              <TextField
                fullWidth
                id="address"
                name="address"
                label="주소"
                variant="standard"
                value={formData.address}
                onChange={handleChange}
              />
              <br />
              <TextField
                fullWidth
                id="detailAddress"
                name="detailAddress"
                label="상세주소"
                variant="standard"
                error={Boolean(errors.detailAddress)}
                value={formData.detailAddress}
                onChange={handleChange}
              />
              <br />
            </div>
            <div
              style={{
                display: "flex",
                justifyContent: "center",
                marginTop: "16px",
              }}
            >
              <Button
                variant="outlined"
                onClick={handleCancel}
                sx={{ mb: 2, mr: 1 }}
              >
                취소
              </Button>
              <Button type="submit" variant="contained" sx={{ mb: 2 }}>
                회원가입
              </Button>
            </div>
          </FormControl>
          <FormHelperTexts>{statusMessages.register}</FormHelperTexts>
        </form>
      </Container>
    </ThemeProvider>
    </div>
    <Footer />
    </>

  );
};

export default Join;
