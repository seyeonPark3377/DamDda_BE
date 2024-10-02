import React, { useState } from "react";
import { TextField, Button } from "@mui/material";
import { Link } from "react-router-dom";
import { Link as MuiLink } from "@mui/material";

import '../../styles/style.css'
import { Header } from "../../layout/Header";
import { Footer } from "../../layout/Footer";

const ResetPassword = () => {
  const [formData, setFormData] = useState({ id: "", name: "", email: "" });
  const [idError, setIdError] = useState("");
  const [nameError, setNameError] = useState("");
  const [emailError, setEmailError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!formData.id) {
      setIdError("아이디를 입력해주세요.");
    } else {
      setIdError("");
    }

    if (!formData.name) {
      setNameError("이름을 입력해주세요.");
    } else {
      setNameError("");
    }

    if (!formData.email) {
      setEmailError("이메일을 입력해주세요.");
    } else {
      setEmailError("");
    }

    if (formData.id && formData.name && formData.email) {
      console.log("Form submitted", formData);
    }
  };

  return (
    <>
    <Header />
     	 <div className="container">

    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        minHeight: "100vh",
        padding: "20px",
      }}
    >
      <div style={{ width: "100%", maxWidth: "400px" }}>
        <h2>비밀번호 재설정하기</h2>
        <form onSubmit={handleSubmit}>
          <TextField
            required
            fullWidth
            id="id"
            name="id"
            label="아이디"
            variant="standard"
            value={formData.id}
            onChange={handleChange}
            error={Boolean(idError)}
            helperText={idError}
            margin="normal"
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
            error={Boolean(nameError)}
            helperText={nameError}
            margin="normal"
          />

          <TextField
            required
            fullWidth
            id="email"
            name="email"
            label="이메일"
            type="email"
            variant="standard"
            value={formData.email}
            onChange={handleChange}
            error={Boolean(emailError)}
            helperText={emailError}
            margin="normal"
          />

          <div
            style={{
              display: "flex",
              justifyContent: "center",
              marginTop: "16px",
            }}
          >
            <Button type="submit" variant="contained" color="primary">
              비밀번호 재설정하기
            </Button>
          </div>

          <div
            style={{ margin: "20px 0", borderBottom: "1px solid lightgray" }}
          />

          <div style={{ marginTop: "20px", textAlign: "right" }}>
            <div>
              아이디를 잊어버리셨나요?{" "}
              <MuiLink component={Link} to="/find-id" variant="body2">
                아이디 찾기
              </MuiLink>
            </div>

            <div>
              가입을 원하시나요?{" "}
              <MuiLink component={Link} to="/join" variant="body2">
                회원가입하러 가기
              </MuiLink>
            </div>
          </div>
        </form>
      </div>
    </div>
    </div>
    <Footer />
    </>

  );
};

export default ResetPassword;
