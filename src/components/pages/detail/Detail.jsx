import React, { useState, useEffect } from "react";
import {
  Typography,
  LinearProgress,
  Divider,
  Button,
  Tabs,
  Tab,
  Modal,
  Box,
  TextField,
} from "@mui/material";
import { styled } from "@mui/system";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import ProjectDetail from "./ProjectDetail";
import Notice from "./notices";
import Qna from "./qna";

import "../../styles/style.css";
import { Header } from "../../layout/Header";
import { Footer } from "../../layout/Footer";
import { useLocation } from "react-router-dom";

import axios from "axios"; // axios를 사용하여 REST API 호출

const ProductContainer = styled("div")({
  position: "relative",
  width: "500px",
  height: "500px",
  backgroundColor: "#f0f0f0",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  borderRadius: "8px",
  overflow: "hidden",
});

const ProductImage = styled("img")({
  width: "100%",
  height: "100%",
  objectFit: "cover",
  borderRadius: "8px",
});

const Indicator = styled("div")({
  display: "flex",
  justifyContent: "space-between",
  marginTop: "10px",
});

const ModalBox = styled(Box)({
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 400,
  backgroundColor: "white",
  boxShadow: 24,
  padding: "20px",
  borderRadius: "8px",
});

const projectData = {
  category: "💄@@@@@@@@@@@@@",
  organizer_id: "@@@@@@@@@@@@@",
  title: "@@@@@@@@@@@@@@@@@",
  description:
    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
  currentAmount: 99999999999999999,
  target_funding: 999999999999999,
  start_date: "@@@@@@@@@@@@@",
  end_date: "@@@@@@@@@@@@@",
  delivery_date: 9999999999,
  liked_count: 99999,
  supporterCount: 9999999,
  product_url: "data:image/png;",
};

const Detail = () => {
  const {
    category,
    organizer_id, //진행자 닉네임
    title,
    description,
    currentAmount, //후원금액(현재까지)
    target_funding,
    start_date,
    end_date,
    delivery_date,
    // liked_count,
    // supporterCount,
    product_url,
  } = projectData;

  
  // 페이지네이션 요청을 보내는 함수
  const fetchProducts = () => {
    axios
      .get(
        `http://${window.location.hostname}:9000/api/projects/${projectId}`,
        {
          params: {
            memberId: 2,
          },
        }
      )
      .then((response) => {
        console.log(response.data);
        if (response.data !== null) {
          setProductDetail(response.data);
          setIsHearted(response.data.liked);
          setLiked_count(response.data.likeCnt)
        } else {
          setProductDetail({});
        }
        console.log(productDetail);
      })
      .catch((error) => {
        console.log(window.location.hostname);
        console.error("프로젝트 데이터를 가져오는 중 오류 발생:", error);
      });
  };


  useEffect(() => {
    fetchProducts();
    
     // HTML 요소에 대한 참조
     const html = document.documentElement;

     // 기존 scroll-behavior 값을 저장
     const originalScrollBehavior = window.getComputedStyle(html).scrollBehavior;
 
     // 부드러운 스크롤 비활성화
     html.style.scrollBehavior = "auto";
 
     // 부드러운 스크롤 비활성화가 적용된 후에 스크롤 이동을 실행하기 위해 약간의 지연을 추가
     setTimeout(() => {
       window.scrollTo(0, 0); // 즉시 스크롤을 맨 위로 이동
 
       // 이후 원래 scroll-behavior 복원
       setTimeout(() => {
         html.style.scrollBehavior = originalScrollBehavior;
       }, 100); // 스크롤 이동 후에 100ms 대기 후 복원
     }, 0); // scrollTo를 실행하기 전에 CSS가 적용되도록 지연을 줌

  }, []);

  const [remainingDays, setRemainingDays] = useState(0);
  const progress = (currentAmount / target_funding) * 100;
  const [supporterCount, setSupporterCount] = useState(
    projectData.supporterCount
  );
  const [liked_count, setLiked_count] = useState(projectData.liked_count); // 좋아요 초기값
  const [isHearted, setIsHearted] = useState(projectData.liked); // 사용자가 좋아요를 눌렀는지
  console.log(isHearted, liked_count)

  const [modalOpen, setModalOpen] = useState(false);
  const [collabDetails, setCollabDetails] = useState({
    name: "",
    phone: "",
    email: "",
    message: "",
    files: [],
  });
  const [errors, setErrors] = useState({
    name: false,
    phone: false,
    email: false,
    message: false,
  });

  const location = useLocation();
  const query = new URLSearchParams(location.search);
  const [projectId, setProjectId] = useState(query.get("projectId") || "");
  const [productDetail, setProductDetail] = useState({});
  console.log(projectId);


  // const fetchProducts = async () => {
  //   try {
  //     const response = await axios.get(
  //       `http://${window.location.hostname}:9000/api/projects/${projectId}`,
  //       {
  //         params: {
  //           memberId: 1,
  //         },
  //       }
  //     );
  //     console.log(response.data.dto);
  //     if (response.data !== null) {
  //       setProductDetail(response.data);
  //     } else {
  //       setProductDetail({});
  //     }
  //     console.log(productDetail);
  //   } catch (error) {
  //     console.log(window.location.hostname);
  //     console.error("프로젝트 데이터를 가져오는 중 오류 발생:", error);
  //   }
  // };


  // 이거어어어어어어어어어어
  // const calculateRemainingDays = () => {
  //   const endDate = new Date(end_date);
  //   const today = new Date();
  //   const timeDiff = endDate - today;
  //   const daysRemaining = Math.ceil(timeDiff / (1000 * 3600 * 24));
  //   setRemainingDays(daysRemaining >= 0 ? daysRemaining : 0);
  // };

  // calculateRemainingDays();

  // const handleLike = async (project) => {
  //   try {
  //     if (project.liked) {
  //       // liked가 true이면 DELETE 요청
  //       const response = await axios.delete(`http://localhost:9000/api/projects/like`, {
  //         params: {
  //           memberId: memberId,
  //           projectId: project.id,
  //         },
  //       });
  //       console.log("좋아요 취소 성공:", response.data);
  //     } else {
  //       // liked가 false이면 POST 요청
  //       const response = await axios.post(`http://localhost:9000/api/projects/like`, null, {
  //         params: {
  //           memberId: memberId,
  //           projectId: project.id,
  //         },
  //       });
  //       console.log("좋아요 성공:", response.data);
  //     }

  //   // fetchProducts(currentPage, progress);
  //   // fetchRecommendedProducts(currentPage, progress)

  //     // 이후에 필요한 처리 (예: UI 업데이트)
  //     setProducts((prevProjects) =>
  //       prevProjects.map((prevProject) =>
  //         prevProject.id === project.id ? { ...prevProject, liked: !prevProject.liked } : prevProject
  //       )
  //     );
  //   } catch (error) {
  //     console.error("좋아요 요청 중 오류 발생:", error);
  //   }
  // };


  const memberId = 2;

  const handleHeartClick = async (prev) => {
    const newHeartedStatus = !prev; // 하트 상태 반전
  
    try {
      if (prev) {
        // 좋아요 취소 요청
        const response = await axios.delete(`http://localhost:9000/api/projects/like`, {
          params: {
            memberId: memberId,
            projectId: productDetail.id,
          },
        });
        console.log("좋아요 취소 성공:", response.data);
        setLiked_count(liked_count - 1);
        // setLiked_count((prevCount) => prevCount - 1); // 함수형 업데이트로 좋아요 수 감소
      } else {
        // 좋아요 추가 요청
        const response = await axios.post(`http://localhost:9000/api/projects/like`, null, {
          params: {
            memberId: memberId,
            projectId: productDetail.id,
          },
        });
        console.log("좋아요 성공:", response.data);
        setLiked_count((prevCount) => prevCount + 1); // 함수형 업데이트로 좋아요 수 증가
      }      
      // 좋아요 상태 업데이트
      setIsHearted(newHeartedStatus);
  
    } catch (error) {
      console.error("좋아요 처리 중 오류 발생:", error);
    }
  
    return newHeartedStatus; // 새로운 상태 반환
  };

  const scrollToSection = (id) => {
    const target = document.getElementById(id);
    target.scrollIntoView({ behavior: "smooth" });
  };

  const handleCollabClick = () => {
    setModalOpen(true);
  };

  const handleModalClose = () => {
    setModalOpen(false);
    setCollabDetails({
      name: "",
      phone: "",
      email: "",
      message: "",
      files: [],
    });
    setErrors({ name: false, phone: false, email: false, message: false });
    const confirmation = window.confirm("창을 닫으시겠습니까?");
    if (confirmation) {
      setModalOpen(false);
      setCollabDetails({
        name: "",
        phone: "",
        email: "",
        message: "",
        files: [],
      });
      setErrors({ name: false, phone: false, email: false, message: false });
    }
  };

  const handleCollabSubmit = () => {
    const newErrors = {
      name: !collabDetails.name,
      phone: !collabDetails.phone,
      email: !collabDetails.email,
      message: !collabDetails.message,
    };

    setErrors(newErrors);

    if (
      !newErrors.name &&
      !newErrors.phone &&
      !newErrors.email &&
      !newErrors.message
    ) {
      alert("협업 요청이 전송되었습니다.");
      handleModalClose();
    }
  };

  const handleSponsorClick = () => {
    const giftSelected = true; // 실제 로직으로 변경
    if (!giftSelected) {
      alert("선물구성을 선택하세요.");
    } else {
      const confirmation = window.confirm("이 프로젝트를 후원하시겠습니까?");
      if (confirmation) {
        setSupporterCount((prev) => prev + 1); // 후원자 수 증가
        alert("결제 창으로 이동합니다.");
      }
    }
  };

  const handleFileChange = (event) => {
    const files = Array.from(event.target.files);
    if (collabDetails.files.length + files.length <= 3) {
      setCollabDetails({
        ...collabDetails,
        files: [...collabDetails.files, ...files],
      });
    } else {
      alert("최대 3개의 파일만 첨부할 수 있습니다.");
    }
  };

  const handleFileDelete = (index) => {
    const confirmation = window.confirm("정말로 삭제하시겠습니까?");
    if (confirmation) {
      const newFiles = collabDetails.files.filter((_, i) => i !== index);
      setCollabDetails({ ...collabDetails, files: newFiles });
    }
  };

// 달성률 계산 (fundsReceive / targetFunding * 100)
const achievementRate = Math.min(
  (productDetail.fundsReceive / productDetail.targetFunding) * 100,
  100
);

// 현재 시간
const currentTime = new Date();
// product.endDate를 Date 객체로 변환
const endDate = new Date(productDetail.endDate);
// 남은 시간 계산 (밀리초 기준)
const timeDifference = endDate - currentTime;

// 밀리초를 일(day) 단위로 변환
const daysLeft = Math.floor(timeDifference / (1000 * 60 * 60 * 24));

// 날짜 형식을 변환하는 함수
const formatDate = (dateString) => {
  const date = new Date(dateString);
  // return date.toISOString().slice(0, 10); // YYYY-MM-DD 형식으로 변환
  return date.toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  });
  // return dateString;
};


  return (
    <>
      <Header />

      <div className="container">
        <div style={{ padding: "20px" }}>
          <div style={{ marginBottom: "20px" }}>
            <Typography variant="category">{productDetail.category}</Typography>
            <br />
            <Typography variant="organizer">
              {productDetail.nickName}
            </Typography>
            <Typography variant="h6">{productDetail.title}</Typography>
            <Typography variant="body2">{productDetail.description}</Typography>
          </div>

          {/* 
          <CardMedia
          component="img"
          image={`http://localhost:9000/${product.thumbnailUrl}`} // 이미지 URL을 서버에서 호출
          // image={product.image}
          sx={{ height: "170px", borderRadius: "5px" }} // 이미지 높이 증가
        /> */}

          {/* {firstHalf.map((product) => (
                <Grid
                  item
                  key={product.id}
                  xs={12}
                  sm={6}
                  md={4}
                  lg={3}
                  xl={2.4}
                  display="flex"
                  justifyContent="center"
                >
                  <ProductCard product={product} handleLike={handleLike} />
                </Grid>
              ))} */}

          <div style={{ display: "flex", width: "7500px" }}>
            <ProductContainer>
              {productDetail.productImages &&
              productDetail.productImages.length > 0 ? (
                productDetail.productImages.map((image, index) => (
                  <ProductImage
                    key={index}
                    src={`http://localhost:9000/${image}`}
                    alt={`Product image ${index}`}
                  />
                ))
              ) : (
                <Typography variant="body2" color="textSecondary">
                  이미지가 없습니다.
                </Typography>
              )}

              {/* {product_url ? (
                <ProductImage src={product_url} alt="Project Product" />
              ) : (
                <Typography variant="body2" color="textSecondary">
                  이미지가 없습니다.
                </Typography>
              )} */}
              <div
                style={{
                  position: "absolute",
                  top: "50%",
                  left: "10px",
                  zIndex: 1,
                  display: "flex",
                  gap: "5px",
                }}
              >
                <Button>
                  <ArrowBackIcon />
                </Button>
                <Button>
                  <ArrowForwardIcon />
                </Button>
              </div>
              <Indicator>
                <div
                  style={{
                    width: "100%",
                    backgroundColor: "#ccc",
                    height: "5px",
                  }}
                >
                  <div
                    style={{
                      width: `${achievementRate}%`, //progress
                      backgroundColor: "#3f51b5",
                      height: "100%",
                    }}
                  />
                </div>
              </Indicator>
            </ProductContainer>

            <div style={{ marginLeft: "20px", flex: 1, width: "5000px" }}>
              <Typography variant="h5" style={{ marginTop: "20px" }}>
                후원금액 (진행률)
                <br />
                {productDetail.fundsReceive}원 ({achievementRate.toFixed(2)}%)
              </Typography>
              <LinearProgress
                variant="determinate"
                value={progress}
                style={{ margin: "10px 0", width: "300px" }}
              />
              <Typography variant="h5">남은 기간: {daysLeft}일</Typography>
              <Typography variant="h5">
                후원자 수: {productDetail.supporterCnt}명
              </Typography>
              <Divider style={{ margin: "20px 0", width: "700px" }} />
              <Typography variant="body2">
                목표금액: {productDetail.targetFunding}원
              </Typography>
              <Typography variant="body2">
                펀딩 기간: {formatDate(productDetail.startDate)} ~ {formatDate(productDetail.endDate)}
              </Typography>
              <Typography variant="body2">
                예상 전달일: 프로젝트 종료일로부터 30일
                {/* {projectData.delivery_date}일 */}
                이내
              </Typography>
              <div style={{ marginTop: "20px" }}>
                <Button variant="contained" onClick={handleSponsorClick}>
                  이 프로젝트에 후원하기
                </Button>
                <p>
                  <Button
                    variant="outlined"
                    onClick={() => handleHeartClick(isHearted)}
                    style={{ marginLeft: "10px" }}
                  >
                    {isHearted ? "♥" : "♡"} <br /> {liked_count}명
                  </Button>
                  <Button
                    variant="outlined"
                    onClick={handleCollabClick}
                    style={{ marginLeft: "10px" }}
                  >
                    협업하기
                  </Button>
                </p>
              </div>
            </div>
          </div>

          <Divider style={{ margin: "20px 0", width: "1220px" }} />

          <div id="details">
            <Tabs value={0} indicatorColor="primary" textColor="primary">
              <Tab
                label="상세설명"
                onClick={() => scrollToSection("details")}
              />
              <Tab
                label="공지사항"
                onClick={() => scrollToSection("notices")}
              />
              <Tab label="Q&A" onClick={() => scrollToSection("qna")} />
            </Tabs>
            <Typography variant="body1" style={{ marginTop: "10px" }}>
              <ProjectDetail descriptionDetail={productDetail.descriptionDetail} descriptionImages={productDetail.descriptionImages}/>
            </Typography>
          </div>

          <Divider style={{ margin: "20px 0" }} />

          <div id="notices">
            <Tabs value={1} indicatorColor="primary" textColor="primary">
              <Tab
                label="상세설명"
                onClick={() => scrollToSection("details")}
              />
              <Tab
                label="공지사항"
                onClick={() => scrollToSection("notices")}
              />
              <Tab label="Q&A" onClick={() => scrollToSection("qna")} />
            </Tabs>
            <Notice />
          </div>

          <Divider style={{ margin: "20px 0" }} />

          <div id="qna">
            <Tabs value={2} indicatorColor="primary" textColor="primary">
              <Tab
                label="상세설명"
                onClick={() => scrollToSection("details")}
              />
              <Tab
                label="공지사항"
                onClick={() => scrollToSection("notices")}
              />
              <Tab label="Q&A" onClick={() => scrollToSection("qna")} />
            </Tabs>
            <Qna />
          </div>

          {/* 협업 모달 */}
          <Modal open={modalOpen} onClose={handleModalClose}>
            <ModalBox>
              <Typography variant="h6" component="h2">
                협업 요청
              </Typography>
              <TextField
                label="이름"
                variant="outlined"
                fullWidth
                margin="normal"
                value={collabDetails.name}
                onChange={(e) =>
                  setCollabDetails({ ...collabDetails, name: e.target.value })
                }
                error={errors.name}
                helperText={errors.name ? "이름을 입력하세요." : ""}
                InputProps={{
                  style: {
                    borderColor: errors.name ? "red" : "inherit",
                  },
                }}
              />
              <TextField
                label="전화번호"
                variant="outlined"
                fullWidth
                margin="normal"
                value={collabDetails.phone}
                onChange={(e) =>
                  setCollabDetails({ ...collabDetails, phone: e.target.value })
                }
                error={errors.phone}
                helperText={errors.phone ? "전화번호를 입력하세요." : ""}
                InputProps={{
                  style: {
                    borderColor: errors.phone ? "red" : "inherit",
                  },
                }}
              />
              <TextField
                label="이메일"
                variant="outlined"
                fullWidth
                margin="normal"
                value={collabDetails.email}
                onChange={(e) =>
                  setCollabDetails({ ...collabDetails, email: e.target.value })
                }
                error={errors.email}
                helperText={errors.email ? "이메일을 입력하세요." : ""}
                InputProps={{
                  style: {
                    borderColor: errors.email ? "red" : "inherit",
                  },
                }}
              />
              <TextField
                label="협업 내용"
                variant="outlined"
                fullWidth
                margin="normal"
                multiline
                rows={4}
                value={collabDetails.message}
                onChange={(e) =>
                  setCollabDetails({
                    ...collabDetails,
                    message: e.target.value,
                  })
                }
                error={errors.message}
                helperText={errors.message ? "협업 내용을 입력하세요." : ""}
                InputProps={{
                  style: {
                    borderColor: errors.message ? "red" : "inherit",
                  },
                }}
              />
              <Button
                variant="contained"
                component="label"
                fullWidth
                margin="normal"
              >
                파일 첨부
                <input
                  type="file"
                  hidden
                  multiple
                  onChange={handleFileChange}
                />
              </Button>
              {collabDetails.files.map((file, index) => (
                <div
                  key={index}
                  style={{
                    marginTop: "10px",
                    display: "flex",
                    justifyContent: "space-between",
                  }}
                >
                  <Typography variant="body2">
                    첨부된 파일: {file.name}
                  </Typography>
                  <Button
                    variant="outlined"
                    onClick={() => handleFileDelete(index)}
                    style={{ marginLeft: "10px" }}
                  >
                    삭제
                  </Button>
                </div>
              ))}
              <div
                style={{
                  display: "flex",
                  justifyContent: "flex-end",
                  marginTop: "20px",
                }}
              >
                <Button
                  variant="outlined"
                  onClick={handleModalClose}
                  style={{ marginRight: "10px" }}
                >
                  닫기
                </Button>
                <Button variant="contained" onClick={handleCollabSubmit}>
                  협업 요청하기
                </Button>
              </div>
            </ModalBox>
          </Modal>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default Detail;
