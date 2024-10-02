import { React, useState, useEffect, useRef } from "react";
import {
  Box,
  Grid,
  Typography,
  Card,
  CardMedia,
  CardContent,
  Button,
  IconButton,
  LinearProgress,
} from "@mui/material";
import FavoriteIcon from "@mui/icons-material/Favorite";
import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import axios from "axios"; // axios를 사용하여 REST API 호출
import { BorderClear } from "@mui/icons-material";
import { padding } from "@mui/system";
import { useNavigate } from "react-router-dom";

// Individual product card component
export const ProductCard = ({ product, handleLike }) => {

  const formattedTargetFunding = new Intl.NumberFormat().format(product.targetFunding);

  // 달성률 계산 (fundsReceive / targetFunding * 100)
  const achievementRate = Math.min(
    (product.fundsReceive / product.targetFunding) * 100,
    100
  );
  
  // 현재 시간
  const currentTime = new Date();
  // product.endDate를 Date 객체로 변환
  const endDate = new Date(product.endDate);
  // 남은 시간 계산 (밀리초 기준)
  const timeDifference = endDate - currentTime;

  // 밀리초를 일(day) 단위로 변환
  const daysLeft = Math.floor(timeDifference / (1000 * 60 * 60 * 24));

  const navigate = useNavigate(); //새로운 프로젝트 눌렀을 때 이동하는 네비게이트


  return (
    <>
      <Card
        sx={{
          borderRadius: "15px",
          boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
          padding: 2,
          margin: '0px 10px',
          // position: "relative",
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          alignItems: "center",
          overflow: "visible", // 숨겨진 요소 방지

          width: "300px", // 고정된 가로 크기
          height: "480px",
          // minWidth: "280px", // 최소 크기 설정
          // maxWidth: "290px", // 최대 크기 설정
          transform: "scale(0.95)", // 전체 요소의 크기를 0.9배로 축소
          transformOrigin: "top left", // 스케일 기준점 설정
        }}
        onClick={() => navigate(`/detail?projectId=${product.id}`)}
      >
        {/* 타이틀과 서브타이틀 */}
        <Box
          sx={{
            position: "relative",
            // margin: "3px",
          }}
        >
          <CardMedia
            component="img"
            image={`http://${window.location.hostname}:9000/${product.thumbnailUrl}`} // 이미지 URL을 서버에서 호출
            // image={product.image}
            sx={{ height: "187.5px", borderRadius: "5px", width: "100%" }} // 이미지 높이 증가
          />
          <IconButton
            sx={{
              position: "absolute",
              top: 7,
              right: 7,
              color: product.liked ? "red" : "gray",
            }}
            onClick={(event) => {
              event.stopPropagation(); // Card 클릭 이벤트가 실행되지 않도록 방지
              handleLike(product); // 좋아요 처리
            }}
          >
            <FavoriteIcon />
          </IconButton>
        </Box>

        <CardContent
          sx={{
            height: "262.5px",
            display: "flex",
            flexDirection: "column",
            overflow: "visible", // 숨겨진 요소 방지
            justifyContent: "space-around",
            alignItems: "flex-start",
          }}
        >
          {/* Title */}
          <Typography
            variant="h6"
            component="div"
            sx={{ fontWeight: "bold", fontSize: "1.1rem", mb: 1 }}
          >
            {product.title}
          </Typography>

          {/* Description */}
          <Typography
            variant="body2"
            color="text.secondary"
            sx={{ fontSize: "0.85rem", mb: 1 }}
          >
            {product.description}
          </Typography>

          <Box
            sx={{
              display: "flex",
              flexDirection: "row",
              alignItems: "center",
              justifyContent: "space-between",
              width: "100%",  // 부모 Box가 전체 너비를 가지도록 설정
            }}
          >
            <Typography
              variant="body2"
              sx={{ fontWeight: "bold", fontSize: "0.8rem" }}
            >
              달성률 {achievementRate.toFixed(2)}%
            </Typography>
            <Typography
              variant="body2"
              sx={{ fontWeight: "bold", fontSize: "0.8rem" }}
            >
              {formattedTargetFunding}원
              {/* {product.targetFunding} */}
            </Typography>
          </Box>

          <Box
            sx={{
              width: "100%", // 부모 컨테이너 너비를 명시적으로 설정
              height: "auto", // 부모 컨테이너 높이가 자동으로 자식 높이를 수용
              display: "block", // 부모 컨테이너의 display 속성 확인
              overflow: "visible", // 자식 요소를 숨기지 않도록 설정
            }}
          >
            {/* Progress bar */}
            <LinearProgress
              variant="determinate"
              value={achievementRate}
              sx={{ height: 9, borderRadius: "5px", mt: 1, mb: 2 }}
            />
          </Box>

          {/* Host and Deadline */}
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              width: "100%",  // 부모 Box가 전체 너비를 가지도록 설정
            }}
          >
            <Button
              variant="contained"
              color="secondary"
              size="small"
              sx={{
                backgroundColor: "#5a87f7",
                borderRadius: "12px",
                fontSize: "0.75rem",
              }}
            >
              마감임박 D - {daysLeft}
            </Button>

            <Typography
              variant="body2"
              sx={{ fontWeight: "bold", fontSize: "0.8rem" }}
            >
              진행자: {product.nickName}
            </Typography>
          </Box>
        </CardContent>
      </Card>
    </>
  );
};

// Product recommendations section
export const ProductRecommendations = ({ sortCondition, title, subTitle }) => {
  const [products, setProducts] = useState([]); // 서버에서 가져온 프로젝트 데이터
  const memberId = 1;

  const itemsPerPage = 10; // 페이지당 항목 수

  // 페이지네이션 요청을 보내는 함수
  const fetchProducts = async () => {
    try {
      const response = await axios.get(
        `http://${window.location.hostname}:9000/api/projects/projects`,
        {
          params: {
            memberId: memberId,
            page: 1,
            sort: sortCondition,
            size: itemsPerPage,
          },
        }
      );

      if (response.data.dtoList !== null) {
        setProducts(response.data.dtoList); // 서버에서 받은 프로젝트 리스트
      } else {
        setProducts([]); // 서버에서 받은 프로젝트 리스트
      }
    } catch (error) {
      console.log(window.location.hostname);
      console.error("프로젝트 데이터를 가져오는 중 오류 발생:", error);
    }
  };

  // 처음 마운트되었을 때 및 페이지 변경 시 데이터 가져오기
  useEffect(() => {
    fetchProducts();
  }, []);

  // 좋아요 요청을 처리하는 함수
  const handleLike = async (project) => {
    try {
      if (project.liked) {
        // liked가 true이면 DELETE 요청
        const response = await axios.delete(
          `http://${window.location.hostname}:9000/api/projects/like`,
          {
            params: {
              memberId: memberId,
              projectId: project.id,
            },
          }
        );
        console.log("좋아요 취소 성공:", response.data);
      } else {
        // liked가 false이면 POST 요청
        const response = await axios.post(
          `http://${window.location.hostname}:9000/api/projects/like`,
          null,
          {
            params: {
              memberId: memberId,
              projectId: project.id,
            },
          }
        );
        console.log("좋아요 성공:", response.data);
      }

      // fetchProducts(currentPage, progress);
      // fetchRecommendedProducts(currentPage, progress)

      // 이후에 필요한 처리 (예: UI 업데이트)
      setProducts((prevProjects) =>
        prevProjects.map((prevProject) =>
          prevProject.id === project.id
            ? { ...prevProject, liked: !prevProject.liked }
            : prevProject
        )
      );
    } catch (error) {
      console.error("좋아요 요청 중 오류 발생:", error);
    }
  };

  const scrollContainerRef = useRef(null);

  const handleScroll = (direction) => {
    if (direction === "left") {
      scrollContainerRef.current.scrollLeft -= 648; // 왼쪽으로 200px 스크롤
    } else {
      scrollContainerRef.current.scrollLeft += 648; // 오른쪽으로 200px 스크롤
    }
  };
  console.log(products);

  return (
    <>
      {/* 타이틀과 서브타이틀 부분 */}
      <Box
        sx={{
          margin: "0 auto",

          display: "flex",
          flexDirection: "column",
          flexWrap: "nowrap",
          alignItems: "center",
          justifyContent: "center",

          padding: 0,
          // height: 20,
          width: "80%",
          // minWidth: '600px',

          marginTop: 1,
        }}
      >
        <Box
          sx={{
            margin: "0 auto",
            padding: 2,
            display: "flex",
            flexDirection: "column",
            flexWrap: "nowrap",
            justifyContent: "flex-start",
            alignItems: "flex-start",
            width: "100%",
            // maxWidth: "100%",
          }}
        >
          <Typography
            variant="h5"
            component="div"
            sx={{ fontWeight: "bold", mb: 1 }}
          >
            <p className="text">
              {" "}
              {/* 5px 패딩 적용 */}
              <span className="text-wrapper">[담ː따] 의 </span>
              <span className="span">{title}</span>
            </p>{" "}
            <Typography variant="body2" color="text.secondary">
              {subTitle}
              {/* 서브타이틀 */}
            </Typography>
          </Typography>
        </Box>

        {/* Title section similar to the example image */}
        {/* 상품 카드 그리드 */}
        <Box
          sx={{
            display: "flex",
            flexDirection: "row",
            justifyContent: "space-between",
            alignItems: "center",
            width: "100%",
            height: "auto",
          }}
        >
          {/* 왼쪽 화살표
             <IconButton sx={{ marginRight: '50px'}}>
            <ArrowBackIosNewIcon />
          </IconButton> */}

          {/* 왼쪽 화살표 */}
          <IconButton
            onClick={() => handleScroll("left")}
            sx={{ marginRight: "10px" }}
          >
            <ArrowBackIosNewIcon />
          </IconButton>

          {/* <Grid container justifyContent="center" alignItems="center" spacing={2} sx={{ flexGrow: 1 }}>
            {products.map((product) => (
              <Grid item key={product.id} 
              xs={12}
              sm={6}
              md={4}
              lg={3}
              xl={2.4}
              display="flex" justifyContent="center">
                <ProductCard product={product} handleLike={handleLike}/>
              </Grid>
            ))}
      </Grid> */}

          {/* 카드들을 감싸는 박스 */}
          <Box
            sx={{
              display: "flex",
              overflowX: "hidden", // 스크롤 감추기
              scrollBehavior: "smooth", // 스크롤 부드럽게
              maxWidth: "90%", // 한 줄로 제한
              width: "90%",
            }}
            ref={scrollContainerRef}
          >
            {products.map((product) => (
              <Box
                key={product.id}
                // sx={{ flex: "0 0 auto", width: "300px", margin: "0px 10px" }}
              >
                <ProductCard product={product} handleLike={handleLike} />
              </Box>
              
            ))}
          </Box>

          {/* 오른쪽 화살표
          <IconButton sx={{ marginLeft: '50px'}}>
          <ArrowForwardIosIcon />
          </IconButton> */}

          {/* 오른쪽 화살표 */}
          <IconButton
            onClick={() => handleScroll("right")}
            sx={{ marginLeft: "10px" }}
          >
            <ArrowForwardIosIcon />
          </IconButton>
        </Box>
      </Box>
    </>
  );
};
