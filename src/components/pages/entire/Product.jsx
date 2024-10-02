import React from "react";
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
  FormControl, InputLabel, Select, MenuItem
} from "@mui/material";
import FavoriteIcon from "@mui/icons-material/Favorite";
import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import CoverImage from "../../assets/coverImage.png";
import axios from "axios"; // axios를 사용하여 REST API 호출
import { useState, useEffect } from "react";
import { useLocation, useNavigate } from 'react-router-dom';


// Individual product card component
export const ProductCard1 = ({ product, handleLike }) => {
  
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
  

  return (
    <>
      <Card
        sx={{
          borderRadius: "15px",
          boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
          padding: 2,
          position: "relative",
          width: "300px", // 고정된 가로 크기
          // minWidth: "300px", // 최소 크기 설정
          // maxWidth: "290px", // 최대 크기 설정
          transform: "scale(0.95)", // 전체 요소의 크기를 0.9배로 축소
          transformOrigin: "top left", // 스케일 기준점 설정
          height: "480px",
        }}
      >
        {/* 타이틀과 서브타이틀 */}

        <CardMedia
          component="img"
          image={`http://localhost:9000/${product.thumbnailUrl}`} // 이미지 URL을 서버에서 호출
          // image={product.image}
          sx={{ height: "170px", borderRadius: "5px" }} // 이미지 높이 증가
        />
        <IconButton
          sx={{
            position: "absolute",
            top: 20,
            right: 20,
            color: product.liked ? "red" : "gray",
          }}
          onClick={() => handleLike(product)}  // 클릭 시 좋아요 요청
        >
          <FavoriteIcon />
        </IconButton>
        <CardContent>
          {/* Title */}
          <Typography
            variant="h6"
            component="div"
            sx={{ fontWeight: "bold", fontSize: "0.9rem", mb: 1 }}
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
              alignItems: "center",
              justifyContent: "space-between",
            }}
          >
            <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
              <Typography
                variant="body2"
                sx={{ fontWeight: "bold", fontSize: "0.75rem" }}
              >
                달성률 {achievementRate}%
              </Typography>
              <Typography
                variant="body2"
                sx={{ fontWeight: "bold", fontSize: "0.75rem" }}
              >
                {product.targetFunding}
              </Typography>
            </Box>
          </Box>

          {/* Progress bar */}
          <LinearProgress
            variant="determinate"
            value={achievementRate}
            sx={{ height: 9, borderRadius: "5px", mt: 1, mb: 2 }}
          />

          {/* Host and Deadline */}
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
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
              sx={{ fontWeight: "bold", fontSize: "0.75rem" }}
            >
              진행자: {product.nickName}
            </Typography>
          </Box>
        </CardContent>
      </Card>
    </>
  );
};


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

          minWidth: "300px",
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
            image={`http://localhost:9000/${product.thumbnailUrl}`} // 이미지 URL을 서버에서 호출
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
export const ProductRecommendations = ({search, cartegory}) => {

  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지

  const [products, setProducts] = useState([]); // 서버에서 가져온 프로젝트 데이터
  const [totalProductNum, setTotalProductNum] = useState(0); // 서버에서 가져온 프로젝트 데이터
  const [totalPages, setTotalPages] = useState(1); // 전체 페이지 수
  
  const [recommendedProducts, setRecommendedProducts] = useState([]); // 서버에서 가져온 프로젝트 데이터

  const [progress, setProgress] = useState("all"); // progress 상태 관리

  const [sortCondition, setSortCondition] = useState("all");
  // const location = useLocation();
  // const query = new URLSearchParams(location.search);
  // const [cartegory, setCartegory] = useState(query.get('category') || 'all');

  const itemsPerPage = 20; // 페이지당 항목 수
  const recommendedItemPerPage = 5; //에디터 추천도 동일하게 있어야 할 듯

  // 페이지네이션 요청을 보내는 함수
  const fetchProducts = async (page, progress, sortCondition, cartegory, search) => {
    try {
      const response = await axios.get(`http://localhost:9000/api/projects/projects`, {
        params: {
          search: search, 
          category: cartegory,
          sort: sortCondition,
          memberId: 1,
          page: page,
          size: itemsPerPage,
          progress: progress, // 진행 상태 필터 적용
        },
      });
      
      if(response.data.dtoList !== null){
        setProducts(response.data.dtoList); // 서버에서 받은 프로젝트 리스트
      } else {
        setProducts([]); // 서버에서 받은 프로젝트 리스트
      }
      setTotalPages(Math.ceil(response.data.total / itemsPerPage)); // 전체 페이지 수 업데이트
      setTotalProductNum(response.data.total)
    } catch (error) {
      console.error("프로젝트 데이터를 가져오는 중 오류 발생:", error);
    }
  };
  
  const fetchRecommendedProducts = async (page, progress) => {
    try {
      const response = await axios.get(`http://localhost:9000/api/projects/projects`, {
        params: {
          page: page,
          memberId: 1,
          size: recommendedItemPerPage,
          progress: progress, // 진행 상태 필터 적용
        },
      });
      
      if(response.data.dtoList !== null){
        setRecommendedProducts(response.data.dtoList); // 서버에서 받은 프로젝트 리스트
      } else {
        setRecommendedProducts([]); // 서버에서 받은 프로젝트 리스트
      }
    } catch (error) {
      console.error("추천 프로젝트 데이터를 가져오는 중 오류 발생:", error);
    }
  };


   // 처음 마운트되었을 때 및 페이지 변경 시 데이터 가져오기
  useEffect(() => {
    fetchProducts(currentPage, progress, sortCondition, cartegory, search);
    fetchRecommendedProducts(currentPage, progress, sortCondition, cartegory, search)
  }, [currentPage, progress, sortCondition, cartegory, search]);


    // 클릭 핸들러
    const handleClick = (value) => {
      setProgress(value); // 클릭한 버튼에 따라 상태 변경
      setCurrentPage(1); // 새로운 필터로 처음 페이지부터 시작
    };


  const halfIndex = Math.ceil(products.length / 2); // 절반 인덱스 계산
  const firstHalf = products.slice(0, halfIndex); // 첫 번째 절반
  const secondHalf = products.slice(halfIndex); // 두 번째 절반


    // 페이지 번호 배열 생성
  const pageNumbers = Array.from({ length: totalPages }, (_, i) => i + 1);
  
  
    // 처음 페이지로 이동
    const handleFirstPage = () => {
      setCurrentPage(1);
    };


    // 이전 페이지로 이동
    const handlePrevPage = () => {
      if (currentPage > 1) {
        setCurrentPage(currentPage - 1);
      }
    };
  
    // 다음 페이지로 이동
    const handleNextPage = () => {
      if (currentPage < totalPages) {
        setCurrentPage(currentPage + 1);
      }
    };

    // 끝 페이지로 이동
    const handleEndPage = () => {
      setCurrentPage(totalPages);
    };

    const handleSortChange = (e) => {
      setSortCondition(e.target.value)
    }
 

    const memberId = 1;  

    // 좋아요 요청을 처리하는 함수
  const handleLike = async (project) => {
    try {
      if (project.liked) {
        // liked가 true이면 DELETE 요청
        const response = await axios.delete(`http://localhost:9000/api/projects/like`, {
          params: {
            memberId: memberId,
            projectId: project.id,
          },
        });
        console.log("좋아요 취소 성공:", response.data);
      } else {
        // liked가 false이면 POST 요청
        const response = await axios.post(`http://localhost:9000/api/projects/like`, null, {
          params: {
            memberId: memberId,
            projectId: project.id,
          },
        });
        console.log("좋아요 성공:", response.data);
      }

    // fetchProducts(currentPage, progress);
    // fetchRecommendedProducts(currentPage, progress)

      // 이후에 필요한 처리 (예: UI 업데이트)
      setProducts((prevProjects) =>
        prevProjects.map((prevProject) =>
          prevProject.id === project.id ? { ...prevProject, liked: !prevProject.liked } : prevProject
        )
      );
      // 이후에 필요한 처리 (예: UI 업데이트)
      setRecommendedProducts((prevProjects) =>
        prevProjects.map((prevProject) =>
          prevProject.id === project.id ? { ...prevProject, liked: !prevProject.liked } : prevProject
        )
      );
    } catch (error) {
      console.error("좋아요 요청 중 오류 발생:", error);
    }
  };


  return (
    <>
      <Box
        sx={{
          margin: "0 auto",
          padding: 2,
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          alignItems: "center",
          maxWidth: "100%",
        }}
      >
        {/* Title section similar to the example image */}


        {/* 상품 카드 그리드 */}
        <Box
          sx={{
            margin: "0 auto",
            padding: 2,
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
            width: 1600,
            maxWidth: "100%",
          }}
        >
          
            {/* 중간 텍스트 */}
            <Box
              sx={{
                paddingLeft: 2, // 왼쪽으로 살짝 이동 (2는 16px)
                textAlign: "left",
                fontSize: "0.875rem", // 글씨 크기 조정 (1rem = 16px -> 0.875rem = 14px)
                width : "100%"
              }}
            >
              <Box
  sx={{
    display: "flex",
    justifyContent: "space-between", // 요소들을 좌우로 배치
    alignItems: "center", // 수직 가운데 정렬
    width: "100%", // 컨테이너 너비를 100%로 설정
    marginBottom: 2, // 아래쪽 여백
  }}
>
  {/* 좌측 타이틀 */}
  <Box>
    <h2 style={{ fontSize: "1.7rem", marginBottom: 20 }}>{cartegory} 프로젝트</h2>
    <h4 style={{ fontSize: "1.1rem", margin: 5, marginBottom: 20 }}>
      {totalProductNum}개의 프로젝트가 있습니다.
    </h4>
  </Box>

  {/* 우측 드롭다운 (정렬 기준) */}
  <FormControl sx={{ minWidth: 200 }}>
    <InputLabel id="sort-select-label">정렬 기준</InputLabel>
    <Select
      labelId="sort-select-label"
      id="sort-select"
      value={sortCondition} // 현재 선택된 정렬 조건
      label="정렬 기준"
      onChange={handleSortChange} // 선택 시 호출
    >
      <MenuItem value="all">----------</MenuItem>
      <MenuItem value="fundsReceive">달성률순</MenuItem>
      <MenuItem value="endDate">마감 임박순</MenuItem>
      <MenuItem value="viewCnt">최다 조회순</MenuItem>
      <MenuItem value="createdAt">등록순</MenuItem>
      <MenuItem value="targetFunding">최다 후원금액순</MenuItem>
      <MenuItem value="supporterCnt">최대 후원자순</MenuItem>
      <MenuItem value="likeCnt">좋아요순</MenuItem>
    </Select>
  </FormControl>
</Box>

            
              <Box            
          sx={{
            marginBottom: 3, // 아래쪽 여백
          }}
              >

      {/* 전체 프로젝트 버튼 */}
      <Button
        onClick={() => handleClick("all")}
        variant={progress === "all" ? "contained" : "outlined"} // 상태에 따라 variant 변경
        // color="secondary"
        size="small"
        sx={{
          // backgroundColor: progress === "all" ? "#5a87f7" : "transparent", // 배경색도 동적으로
          borderRadius: "12px",
          fontSize: "0.75rem",
          marginRight: "20px",
        }}
      >
        전체 프로젝트
      </Button>

      {/* 진행중인 프로젝트 버튼 */}
      <Button
        onClick={() => handleClick("ongoing")}
        variant={progress === "ongoing" ? "contained" : "outlined"} // 상태에 따라 variant 변경
        // color="secondary"
        size="small"
        sx={{
          // backgroundColor: progress === "progress" ? "#5a87f7" : "transparent", // 배경색도 동적으로
          borderRadius: "12px",
          fontSize: "0.75rem",
          marginRight: "20px",
        }}
      >
        진행중인 프로젝트
      </Button>

      {/* 종료된 프로젝트 버튼 */}
      <Button
        onClick={() => handleClick("completed")}
        variant={progress === "completed" ? "contained" : "outlined"} // 상태에 따라 variant 변경
        // color="secondary"
        size="small"
        sx={{
          // backgroundColor: progress === "completed" ? "#5a87f7" : "transparent", // 배경색도 동적으로
          borderRadius: "12px",
          fontSize: "0.75rem",
        }}
      >
        종료된 프로젝트
      </Button>
    </Box>
    

              {/* 글씨 크기 줄이기 */}
            </Box>
          <Box
            sx={{
              // display: "flex",
              justifyContent: "center",
              alignItems: "center",
              width: "100%",
              height: "auto",
            }}
          >



            <Grid
              container
              justifyContent="center"
              alignItems="center"
              spacing={2}
              sx={{ flexGrow: 0 }}
            >
              {firstHalf.map((product) => (
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
              ))}
            </Grid>

            {/* 중간 텍스트 */}
            <Box
              sx={{
                paddingLeft: 2, // 왼쪽으로 살짝 이동 (2는 16px)
                textAlign: "left",
                fontSize: "0.875rem", // 글씨 크기 조정 (1rem = 16px -> 0.875rem = 14px)
              }}
            >
              <h2 style={{ fontSize: "1.25rem", margin: 0 }}>에디터 추천 상품</h2>{" "}
              {/* 글씨 크기 줄이기 */}
            </Box>

            {/* 회색 배경과 추천 상품 */}
            <Box
              sx={{
                width: "100%",
                backgroundColor: "#f0f0f0", // 회색 배경
                padding: 4,
                marginY: 2, // 위아래 여백
              }}
            >
              <Grid
                container
                justifyContent="center"
                alignItems="center"
                spacing={2}
              >
                {recommendedProducts.map((product) => (
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
                ))}
              </Grid>
            </Box>
            {/* 두 번째 카드 그룹 */}
            <Grid
              container
              justifyContent="center"
              alignItems="center"
              spacing={2}
              sx={{ flexGrow: 0 }}
            >
              {secondHalf.map((product) => (
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
              ))}
            </Grid>

      {/* 페이지네이션 버튼 */}
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          marginTop: 2,
        }}
      >

        <Button
          onClick={handleFirstPage}
          disabled={currentPage === 1}
        >
          처음으로
        </Button>

        <Button
          onClick={handlePrevPage}
          disabled={currentPage === 1}
        >
          이전
        </Button>
        
        <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", marginTop: 0 }}>
      {pageNumbers.map((pageNumber) => (
        <Button
          key={pageNumber}
          onClick={() => setCurrentPage(pageNumber)} // 페이지 변경
          variant={currentPage === pageNumber ? "contained" : "outlined"} // 현재 페이지 스타일
          sx={{ mx: 1.0 ,
            minWidth: 40,  // 최소 너비
            minHeight: 40,  // 최소 높이
            fontSize: "0.8rem",  // 폰트 크기 조절
            }} // 좌우 간격
          
        >
          {pageNumber}
        </Button>
      ))}
    </Box>

        <Button
          onClick={handleNextPage}
          disabled={currentPage === totalPages}
        >
          다음
        </Button>
        
        <Button
          onClick={handleEndPage}
          disabled={currentPage === totalPages}
        >
          끝으로
        </Button>

      </Box>
          </Box>
        </Box>
        
      </Box>
    </>
  );
};
