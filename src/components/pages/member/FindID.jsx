import React, { useState } from "react";
import { TextField, Button } from "@mui/material";
import { Link } from "react-router-dom";
import { Link as MuiLink } from "@mui/material";

import '../../styles/style.css'
import { Header } from "../../layout/Header";
import { Footer } from "../../layout/Footer";
const FindID = () => {
  const [formData, setFormData] = useState({ name: "", email: "" });
  const [nameError, setNameError] = useState("");
  const [emailError, setEmailError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
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

    if (formData.name && formData.email) {
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
        <h2>아이디 찾기</h2>
        <form onSubmit={handleSubmit}>
          {/* 이름 입력 */}
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

          {/* 이메일 입력 */}
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
              아이디 찾기
            </Button>
          </div>

          <div
            style={{ margin: "20px 0", borderBottom: "1px solid lightgray" }}
          />

          <div style={{ marginTop: "20px", textAlign: "right" }}>
            <div>
              비밀번호를 잊어버리셨나요?{" "}
              <MuiLink component={Link} to="/reset-password" variant="body2">
                비밀번호 재설정하기
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

export default FindID;
