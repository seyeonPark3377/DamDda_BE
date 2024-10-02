import React, { useState } from "react";
import { TextField, Button, Link as MuiLink } from "@mui/material";
import { Link, useNavigate } from "react-router-dom";

import "../../styles/style.css";
import { Header } from "../../layout/Header";
import { Footer } from "../../layout/Footer";

const Login = () => {
  const [formData, setFormData] = useState({ id: "", password: "" });
  const [idError, setIdError] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const [loginError, setLoginError] = useState(""); // 로그인 오류 메시지 추가
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    setLoginError(""); // 입력할 때마다 로그인 오류 메시지 초기화
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    let valid = true;

    if (!formData.id) {
      setIdError("아이디를 입력해주세요.");
      valid = false;
    } else {
      setIdError("");
    }

    if (!formData.password) {
      setPasswordError("비밀번호를 입력해주세요.");
      valid = false;
    } else {
      setPasswordError("");
    }

    // 모든 필드가 입력되었을 때만 검증 진행
    if (valid) {
      // 로그인 검증 (백앤드)
      /*
      fetch('/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      })
      .then(response => response.json())
      .then(data => {
        if (data.success) {
          navigate("/"); // 로그인 성공 후 페이지 이동
        } else {
          setLoginError("로그인 정보가 틀렸습니다. 다시 입력해주세요.");
        }
      })
      .catch(error => {
        console.error('Error:', error);
      });
      */

      // 예시로 간단히 ID와 비밀번호가 "admin"일 경우만 성공으로 처리
      if (formData.id === "aaaa" && formData.password === "aaaa") {
        console.log("로그인 성공");
        // 로그인 성공 후 페이지 이동
        navigate("/", { state: { id: formData.id } });
        // navigate("/");
      } else {
        // 로그인 실패 시 에러 메시지 표시
        setLoginError("로그인 정보가 틀렸습니다. 다시 입력해주세요.");
      }
    }
  };

  const handleJoinClick = () => {
    navigate("/join"); // 회원가입 페이지로 이동
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
            flexDirection: "column",
          }}
        >
          <h2>로그인</h2>
          <form onSubmit={handleSubmit} style={{ width: "400px" }}>
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
              id="password"
              name="password"
              label="비밀번호"
              type="password"
              variant="standard"
              value={formData.password}
              onChange={handleChange}
              error={Boolean(passwordError)}
              helperText={passwordError}
              margin="normal"
            />

            {/* 로그인 에러 메시지 */}
            {loginError && (
              <div style={{ color: "red", marginTop: "10px" }}>
                {loginError}
              </div>
            )}

            <div
              style={{
                marginTop: "20px",
                display: "flex",
                justifyContent: "center",
              }}
            >
              <Button
                variant="outlined"
                color="primary"
                onClick={handleJoinClick}
                sx={{ mb: 2, mr: 2 }}
              >
                회원가입
              </Button>
              <Button
                type="submit"
                variant="contained"
                color="primary"
                sx={{ mb: 2, mr: 1 }}
              >
                로그인
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
                비밀번호를 잊어버리셨나요?{" "}
                <MuiLink component={Link} to="/reset-password" variant="body2">
                  비밀번호 재설정하기
                </MuiLink>
              </div>
            </div>
          </form>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default Login;
