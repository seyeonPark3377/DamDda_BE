import * as React from "react";
import { useState, useEffect } from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import Typography from "@mui/material/Typography";
import Menu from "@mui/material/Menu";
import Container from "@mui/material/Container";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import Tooltip from "@mui/material/Tooltip";
import MenuItem from "@mui/material/MenuItem";
import AdbIcon from "@mui/icons-material/Adb";
import TextField from "@mui/material/TextField"; // 검색바를 위해 추가
import { Card, CardMedia, CardContent, Grid } from "@mui/material"; // MUI 컴포넌트 사용
import CloseIcon from "@mui/icons-material/Close"; // 프로젝트 삭제 버튼을 위한 CloseIcon
import { Link, useNavigate, useLocation } from "react-router-dom";
import logo from "../assets/logo.png"; // 로고 파일
import { SearchBar } from "./SearchBar";
import axios from "axios"; // axios를 사용하여 REST API 호출

// const pages = ['카테고리'];

export function Header({ search, setSearch }) {
  const [anchorElNav, setAnchorElNav] = useState(null);
  const [anchorElUser, setAnchorElUser] = useState(null);
  const [showProfileCard, setShowProfileCard] = useState(false); // 프로필 카드 표시 여부
  const [showProjects, setShowProjects] = useState(false); // 프로젝트 목록 표시 여부
  const [projects, setProjects] = useState([
    { name: "프로젝트 이름 1", id: 1 },
    { name: "프로젝트 이름 2", id: 2 },
  ]);
  const location = useLocation();

  const [isLoggedIn, setIsLoggedIn] = React.useState(false); // 로그인 상태 관리

  const memberId = 1;

  useEffect(() => {
    if (location.state?.id) {
      setIsLoggedIn(true);
    }
  }, [location.state?.id]);

  const handleOpenNavMenu = (event) => {
    setAnchorElNav(event.currentTarget);
  };
  const handleOpenUserMenu = () => {
    setShowProfileCard(!showProfileCard); // 클릭 시 프로필 카드 표시 토글
    if (!showProfileCard) {
      setShowProjects(false); // 프로젝트 등록 토글을 닫음
    }
  };
  const handleCloseNavMenu = () => {
    setAnchorElNav();
  };
  const handleCloseUserMenu = () => {
    setAnchorElNav();
  };

  const handleShowProjects = () => {
    setShowProjects(!showProjects); // 버튼을 누를 때마다 프로젝트 리스트 표시 여부 토글
    if (!showProjects) {
      setShowProfileCard(false); // 프로필 카드를 닫음
    }
  };

  const handleDeleteProject = (id) => {
    // 프로젝트 삭제 기능
    setProjects(projects.filter((project) => project.id !== id));
  };

  const navigate = useNavigate(); //새로운 프로젝트 눌렀을 때 이동하는 네비게이트

  const navigateRegister = async () => {
    navigate("/register");
    // const formData = new FormData();
    
    // // 텍스트 데이터 추가
    // formData.append("memberId", memberId);
    // formData.append("submit", "저장");
    
    // // 파일 데이터 추가 (productImages, descriptionImages, docs)
    // // 예시로서 파일을 직접 추가하는 경우:
    // // formData.append("productImages", productImageFile); // productImageFile는 파일 객체
    // // formData.append("descriptionImages", descriptionImageFile); // descriptionImageFile도 파일 객체
    // // formData.append("docs", docFile); // docFile도 파일 객체
  
    // try {
    //   const response = await axios.post(
    //     'http://localhost:9000/api/projects/register',
    //     formData, // FormData 객체를 전송
    //     {
    //       headers: {
    //         'Content-Type': 'multipart/form-data', // 헤더 설정
    //       },
    //       withCredentials: true, // 인증 쿠키 전송
    //     }
    //   );
      
    //   console.log("프로젝트 등록 성공:", response.data);
    // } catch (error) {
    //   console.error("프로젝트 등록 중 오류 발생:", error);
    // }
  };


  return (
    <AppBar position="static" sx={{ bgcolor: "white", color: "black" }}>
      <Container
        maxWidth="1520px" // maxWidth를 false로 설정하여 100%가 기본값이 되지 않도록 설정
        sx={{
          width: "70%", // 네브바의 너비를 전체의 70%로 설정
          margin: "0 auto", // 네브바를 중앙에 배치
        }}
      >
        <Toolbar
          disableGutter
          sx={{
            display: "flex",
            justifyContent: "space-between",
            margin: "0px auto",
          }}
        >
          <Typography
            variant="h6"
            noWrap
            component="a"
            href="#app-bar-with-responsive-menu"
            sx={{
              mr: 2,
              display: { xs: "none", md: "flex" },
              fontFamily: "monospace",
              fontWeight: 700,
              letterSpacing: ".3rem",
              color: "inherit",
              textDecoration: "none",
            }}
          >
            <Link to="/">
              <img
                className="link"
                alt="Link"
                src={logo}
                style={{ width: "200px", height: "80px" }}
              />
            </Link>
          </Typography>

          <Box sx={{ flexGrow: 1, display: { xs: "flex", md: "none" } }}>
            <Menu
              id="menu-appbar"
              anchorEl={anchorElNav}
              anchorOrigin={{
                vertical: "bottom",
                horizontal: "left",
              }}
              keepMounted
              transformOrigin={{
                vertical: "top",
                horizontal: "left",
              }}
              open={Boolean(anchorElNav)}
              onClose={handleCloseNavMenu}
              sx={{ display: { xs: "block", md: "none" } }}
            >
              <MenuItem onClick={handleCloseNavMenu}>
                <Typography sx={{ textAlign: "center", fontWeight: 400 }}>
                  카테고리
                </Typography>
              </MenuItem>
            </Menu>
          </Box>

          {/* <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' }, marginLeft: 2, mt: 2, alignItems: 'center' }}>
            {pages.map((page) => (
              <Button
                key={page}
                onClick={handleCloseNavMenu}
                sx={{
                  my: 2,
                  color: 'black',
                  display: 'block',
                  fontWeight: '900', // 폰트를 엄청 진하게 설정
                  fontSize: '1.2rem', // 폰트 크기도 키움
                  textTransform: 'none', // 버튼 텍스트의 대문자 자동 변환 방지
                }}
              >
                {page}
              </Button>
            ))}
          </Box> */}

          {/* 검색 바 */}
          <Box
            sx={{
              width: { xs: "200px", sm: "250px", md: "350px", lg: "500px" },
            }}
          >
            <SearchBar search={search} setSearch={setSearch}></SearchBar>
          </Box>

          <Box
            sx={{ display: "flex", flexGrow: 0, mr: 2, position: "relative" }}
          >
            {/* 프로젝트 등록 버튼 및 프로젝트 목록 표시 */}
            <Box sx={{ flexGrow: 0, mr: 2, position: "relative" }}>
              <Button
                variant="contained"
                onClick={handleShowProjects}
                sx={{
                  backgroundColor: "#7a82ed",
                  color: "white",
                  fontWeight: "bold",
                  borderRadius: "10px",
                  padding: "8px 16px",
                  boxShadow: "none",
                  "&:hover": {
                    backgroundColor: "#33C2E2",
                  },
                }}
              >
                프로젝트 등록
              </Button>

              {showProjects && (
                <Box
                  sx={{
                    marginTop: "10px",
                    padding: "10px",
                    border: "1px solid #ccc",
                    borderRadius: "10px",
                    backgroundColor: "white",
                    boxShadow: "0px 4px 12px rgba(0,0,0,0.1)",
                    position: "absolute", // 버튼 아래에 위치시키기 위해 절대 위치 지정
                    top: "100%", // 버튼 바로 아래에 위치
                    left: "50%", // 수평 중앙 정렬
                    transform: "translateX(-50%)", // 중앙 정렬 보정
                    width: "200px", // 원하는 너비로 설정
                    zIndex: 1000, // 다른 요소보다 상위에 위치하도록 zIndex를 크게 설정
                  }}
                >
                  <Typography
                    variant="subtitle1"
                    sx={{ fontWeight: "bold", marginBottom: "10px" }}
                    onClick={navigateRegister} //() => navigate("/register")} // 클릭 시 이동
                    style={{ cursor: "pointer" }} // 클릭 가능한 텍스트로 설정
                  >
                    + 새로운 프로젝트
                  </Typography>
                  {projects.map((project) => (
                    <Box
                      key={project.id}
                      sx={{
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center",
                        marginBottom: "5px",
                      }}
                    >
                      <Typography>{project.name}</Typography>
                      <IconButton
                        size="small"
                        onClick={() => handleDeleteProject(project.id)}
                      >
                        <CloseIcon sx={{ color: "#f44e38" }} />
                      </IconButton>
                    </Box>
                  ))}
                </Box>
              )}
            </Box>

            {/* 프로필 카드 부분 */}
            <Box sx={{ position: "relative" }}>
              {isLoggedIn ? (
                // 로그인 후 프로필 카드
                <Tooltip title="Open settings">
                  <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                    <Avatar
                      alt="Remy Sharp"
                      src="/static/images/avatar/2.jpg"
                    />
                  </IconButton>
                </Tooltip>
              ) : (
                // 로그인 전에는 로그인 버튼
                <Button
                  variant="contained"
                  color="primary"
                  onClick={() => navigate("/login")} // 로그인 페이지로 이동
                  sx={{
                    backgroundColor: "#7a82ed",
                    color: "white",
                    fontWeight: "bold",
                    borderRadius: "10px",
                    padding: "8px 16px",
                    "&:hover": {
                      backgroundColor: "#33C2E2",
                    },
                  }}
                >
                  로그인
                </Button>
              )}

              {showProfileCard && isLoggedIn && (
                <Box
                  sx={{
                    position: "absolute",
                    top: "100%",
                    left: "50%",
                    transform: "translateX(-50%)",
                    zIndex: 1000,
                    backgroundColor: "white",
                    borderRadius: "15px",
                    width: 240,
                    padding: 2,
                  }}
                >
                  <Card sx={{ width: "100%", borderRadius: "15px", p: 2 }}>
                    <CardMedia
                      component="img"
                      image="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava2-bg.webp"
                      alt="Profile image"
                      sx={{
                        borderRadius: "50%",
                        width: "80px",
                        height: "80px",
                        margin: "auto",
                        marginTop: 2,
                      }}
                    />
                    <CardContent>
                      <Typography
                        gutterBottom
                        variant="h6"
                        component="div"
                        textAlign="center"
                        sx={{ fontWeight: "bold" }}
                      >
                        {location.state?.id} 님
                      </Typography>

                      <Box
                        sx={{
                          display: "flex",

                          flexDirection: "column",

                          justifyContent: "center",

                          alignItems: "center",

                          gap: 1,

                          mt: 2,
                        }}
                      >
                        <Button
                          variant="outlined"
                          sx={{
                            borderRadius: 20,

                            width: "120px",

                            fontWeight: "bold",
                          }}
                          onClick={() => navigate("/mypage")}
                        >
                          마이페이지
                        </Button>

                        <Button
                          variant="outlined"
                          sx={{
                            borderRadius: 20,

                            width: "100px",

                            fontWeight: "bold",
                          }}
                          onClick={() =>
                            navigate("/mypage", {
                              state: { activeTab: "likeProject" },
                            })
                          }
                        >
                          ❤️관심프로젝트
                        </Button>

                        <Typography
                          variant="body2"
                          color="text.secondary"
                          textAlign="center"
                          sx={{
                            fontWeight: 800,

                            cursor: "pointer",
                          }}
                          onClick={() => {
                            setIsLoggedIn(false);

                            navigate("/");
                          }}
                        >
                          로그아웃
                        </Typography>
                      </Box>
                    </CardContent>
                  </Card>
                </Box>
              )}
            </Box>
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
}
