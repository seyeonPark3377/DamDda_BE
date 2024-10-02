import React, { useEffect, useState } from "react";
import {
  TextField,
  Button,
  Typography,
  Select,
  MenuItem,
  InputLabel,
  FormControl,
  Modal,
  Divider,
  Tabs,
  Tab,
  Chip,
  IconButton
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";  // CloseIcon은 MUI의 아이콘 컴포넌트
import { DesktopDatePicker } from "@mui/x-date-pickers";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import DetailPage from "./detailPage";
import Package from "./package";
import ProjectDocument from "./projectDocument";

import '../../styles/style.css'
import { Header } from "../../layout/Header";
import { Footer } from "../../layout/Footer";
import axios from "axios";
import { useLocation } from "react-router-dom"

const Register = () => {
  const [descriptionDetail, setDescriptionDetail] = useState("");
  
  const location = useLocation();
  const query = new URLSearchParams(location.search);
  const [projectId, setProjectId] = useState(query.get("projectId") || 1);


  const [formData, setFormData] = useState({
    category_id: "",
    subcategory: "",
    title: "",
    description: "",
    target_funding: "",
    start_date: null,
    end_date: null,
    delivery_date: null,
    tags: "",
  });
  
  const [productImages, setProductImages] = useState([]); // 태그 목록
  const [descriptionImages, setDescriptionImages] = useState([]); // 태그 목록
  const [reqDocs, setReqDocs] = useState([]); // 필수 서류
  const [certDocs, setCertDocs] = useState([]); // 인증 서류
  const [docs, setDocs] = useState([]); // 태그 목록

  const [productImagesUrl, setProductImagesUrl] = useState([]); // 태그 목록
  // const [descriptionImagesUrl, setDescriptionImagesUrl] = useState([]); // 태그 목록
  // const [docsUrl, setDocsUrl] = useState([]); // 태그 목록

  const [tags, setTags] = useState([]); // 태그 목록
  //const [selectedImage, setSelectedImage] = useState([]);
  const [aiModalOpen, setAiModalOpen] = useState(false);
  const [aiGeneratedDescription, setAiGeneratedDescription] =
    useState("DB에서 불러온 설명 내용");

  const scrollToSection = (id) => {
    const target = document.getElementById(id);
    target.scrollIntoView({ behavior: "smooth" });
  };

  const handleChange = (event) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value,
    });
  };

  const handleKeyDown = (event) => {
    if (event.key === "Enter" && formData.tags.trim() !== "") {
      event.preventDefault(); // 기본 Enter 동작 방지

      if (tags.length >= 5) {
        alert("입력 가능한 태그 개수를 초과했습니다. (최대 5개)");
      } else {
        setTags([...tags, formData.tags.trim()]); // 새로운 태그 추가
        setFormData({ ...formData, tags: "" }); // 태그 입력창 초기화
      }
    }
  };

  const handleTagDelete = (index) => {
    const updatedTags = [...tags];
    updatedTags.splice(index, 1); // 태그 삭제
    setTags(updatedTags);
  };

  const handleDateChange = (date, name) => {
    setFormData({ ...formData, [name]: date });
  };

  const handleProductImageChange = (e) => {
   // e.preventDefault(); // 기본 폼 제출 동작 방지

    const files = e.target.files; // 모든 파일을 가져옴
    const selectedFiles = []; // 선택된 파일들을 저장할 배열
    const imageUrls = []; // 새로 추가할 이미지 URL 배열
  
    for (let i = 0; i < files.length; i++) {
      if (files[i]) {
        const imageUrl = URL.createObjectURL(files[i]);
        imageUrls.push(imageUrl); // 각 파일의 URL을 배열에 추가
        selectedFiles.push(files[i]); // File 객체를 배열에 추가
      }
    }   
    setProductImagesUrl((prevImages) => [...prevImages, ...imageUrls]);
    setProductImages((prevImages) => [...prevImages, ...selectedFiles]);
  };

  const handleRemoveImage = (index) => {
    setProductImages((prevs) =>
      prevs.filter((_, i) => i !== index) // 클릭된 이미지 제거
    );
    setProductImagesUrl((prevUrls) =>
      prevUrls.filter((_, i) => i !== index) // 클릭된 이미지 제거
    );
  };



  useEffect(() => {
    setDocs([...reqDocs, ...certDocs]);
  }, [reqDocs, certDocs]);




  const openAiModal = () => {
    setAiModalOpen(true);
  };

  const closeAiModal = () => {
    setAiModalOpen(false);
  };

  const confirmDescriptionRegistration = () => {
    const confirmed = window.confirm("정말로 등록하시겠습니까?");
    if (confirmed) {
      setFormData({ ...formData, description: aiGeneratedDescription });
      closeAiModal();
    }
  };

  const saveProject = async (projectId, submit) => {
    console.log(formData);
    console.log(tags);
    
    const projectFormData  = new FormData();

  // ProjectDetailDTO 데이터
  const projectDetailDTO = {
    id: projectId, // 프로젝트 ID
    title: formData.title, // formData에서 가져오는 값 예시
    description: formData.description, // formData에서 가져오는 값 예시
    descriptionDetail: descriptionDetail,
    fundsReceive: 0,
    targetFunding: formData.target_funding, // 목표 금액
    nickName: "testNickName", // 진행자 닉네임
    startDate: new Date(formData.start_date), // 시작 날짜 (적절하게 변환 필요)
    endDate: new Date(formData.end_date), // 종료 날짜 (적절하게 변환 필요)
    supporterCnt: 0,
    likeCnt: 0,
    category: formData.category_id,
    tags: tags.map((tag) => ({name: tag, usageFrequency: -1, projectIds: [0],})),
  };

  // projectDetailDTO를 JSON 문자열로 변환하여 FormData에 추가
  // projectFormData.append("projectDetailDTO", JSON.stringify(projectDetailDTO));
  projectFormData.append("projectDetailDTO", new Blob([JSON.stringify(projectDetailDTO)], { type: 'application/json' }));
  



  console.log(productImages);
  console.log(descriptionImages);
  console.log(docs);















   // 파일 데이터 (예시로 formData에서 가져오는 경우)
  // const productImages = []; // 여기에 productImage 파일 객체를 넣어주세요
  // const descriptionImages = []; // 여기에 descriptionImage 파일 객체를 넣어주세요
  // const docs = []; // 여기에 docs 파일 객체를 넣어주세요

  // productImages 파일 추가
  productImages.forEach((file, index) => {
    projectFormData.append(`productImages`, file);
  });

  // descriptionImages 파일 추가
  descriptionImages.forEach((file, index) => {
    projectFormData.append(`descriptionImages`, file);
  });

  // docs 파일 추가
  docs.forEach((file, index) => {
    projectFormData.append(`docs`, file);
  });

  // 추가적으로 필요한 텍스트 필드 데이터
  projectFormData.append("submit", submit); // "저장" 혹은 "제출"

  try {
    const response = await axios.put(
      `http://localhost:9000/api/projects/register/${projectId}`,
      projectFormData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        withCredentials: true, // 쿠키 전송 (필요한 경우)
      }
    );
    console.log("프로젝트 업데이트 성공:", response.data);
  } catch (error) {
    console.error("프로젝트 업데이트 중 오류 발생:", error);
  }
};

  return (
    <>
    <Header />
     	 <div className="container">

    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <div style={{ padding: "20px" }}>
        <Typography variant="h5">프로젝트 등록하기</Typography>
        <Button
          fullWidth
          variant="contained"
          color="primary"
          onClick={() => saveProject(projectId, "저장")}
        >
          저장
        </Button>

        <div style={{ display: "flex", flexWrap: "wrap", marginTop: "16px" }}>
          {/* 카테고리 선택 */}
          <div style={{ flex: "1 1 50%", padding: "8px" }}>
            <FormControl fullWidth>
              <InputLabel>카테고리</InputLabel>
              <Select
                name="category_id"
                value={formData.category_id}
                onChange={handleChange}
              >
                <MenuItem value={"카테고리1"}>카테고리1</MenuItem>
                <MenuItem value={"카테고리2"}>카테고리2</MenuItem>
                <MenuItem value={"카테고리3"}>카테고리3</MenuItem>
              </Select>
            </FormControl>
          </div>

          <div style={{ flex: "1 1 50%", padding: "8px" }}>
            <FormControl fullWidth>
              <InputLabel>세부항목</InputLabel>
              <Select
                name="subcategory"
                value={formData.subcategory}
                onChange={handleChange}
              >
                <MenuItem value={"세부항목1"}>세부항목1</MenuItem>
                <MenuItem value={"세부항목2"}>세부항목2</MenuItem>
                <MenuItem value={"세부항목3"}>세부항목3</MenuItem>
              </Select>
            </FormControl>
          </div>

          <div
            style={{ display: "flex", flexDirection: "row", padding: "8px" }}
          >


            {/* 왼쪽: 이미지 미리보기 및 업로드 */}
            <div
              style={{
                flex: "2 1 100%",
                padding: "8px",
                display: "flex",
                justifyContent: "center",
              }}
            >
              <div
                style={{
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                }}
              >


<div style={{ display: "flex", flexWrap: "wrap", gap: "10px" }}>
          {productImagesUrl.length > 0 ? (
            productImagesUrl.map((url, index) => (
              <div
                key={index}
                style={{
                  position: "relative",
                  width: "150px",
                  height: "150px",
                }}
              >
                <img
                  src={url}
                  alt={`미리보기 ${index}`}
                  style={{
                    width: "100%",
                    height: "100%",
                    objectFit: "cover",
                  }}
                />
                <IconButton
                  style={{
                    position: "absolute",
                    top: "5px",
                    right: "5px",
                    backgroundColor: "white",
                    padding: "2px",
                  }}
                  onClick={() => handleRemoveImage(index)}
                >
                  <CloseIcon />
                </IconButton>
              </div>
            ))
                  ) : (
                    <div
                      style={{
                        width: "100%",
                        height: "300px",
                        backgroundColor: "#f0f0f0",
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                      }}
                    >
                      이미지 미리보기
                    </div>
                  )}
                </div>





                <div style={{ marginTop: "16px" }}>
                  <Button variant="outlined" component="label">
                    이미지 업로드
                    <input type="file" hidden multiple onChange={handleProductImageChange} /> {/* multiple */} 
                  </Button>
                </div>
              </div>
            </div>































            {/* 오른쪽: 입력 폼 */}
            <div style={{ flex: "1 1 100%", padding: "8px" }}>
              {/* 프로젝트 제목 */}
              <div>
                프로젝트 제목 :
                <TextField
                  label="프로젝트 제목"
                  name="title"
                  fullWidth
                  value={formData.title}
                  onChange={handleChange}
                />
              </div>

              {/* 프로젝트 설명 */}
              <div>
                프로젝트 설명 :
                <TextField
                  label="프로젝트 설명"
                  name="description"
                  fullWidth
                  multiline
                  rows={4}
                  value={formData.description}
                  onChange={handleChange}
                />
              </div>

              {/* 목표 금액 */}
              <div>
                목표금액 :
                <TextField
                  label="목표 금액"
                  name="target_funding"
                  fullWidth
                  value={formData.target_funding}
                  onChange={handleChange}
                />
              </div>

              {/* 일정 선택 */}
              <div style={{ display: "flex", alignItems: "center" }}>
                프로젝트 일정 :
                <DesktopDatePicker
                  label="시작일"
                  value={formData.start_date}
                  onChange={(date) => handleDateChange(date, "start_date")}
                  renderInput={(params) => <TextField {...params} fullWidth />}
                />
                ~
                <DesktopDatePicker
                  label="종료일"
                  value={formData.end_date}
                  onChange={(date) => handleDateChange(date, "end_date")}
                  renderInput={(params) => <TextField {...params} fullWidth />}
                />
              </div>

              {/* 예상 전달일 */}
              <div>
                예상전달일 :
                <DesktopDatePicker
                  label="예상 전달일"
                  value={formData.delivery_date}
                  onChange={(date) => handleDateChange(date, "delivery_date")}
                  renderInput={(params) => <TextField {...params} fullWidth />}
                />
              </div>

              {/* 태그 입력 */}
              <div>
                태그 :
                <TextField
                  label="태그"
                  name="tags"
                  fullWidth
                  value={formData.tags}
                  onChange={handleChange}
                  onKeyDown={handleKeyDown} // Enter 입력 처리
                  placeholder="태그를 입력하고 엔터를 눌러주세요"
                />
              </div>

              <div style={{ marginTop: "10px" }}>
                {tags.map((tag, index) => (
                  <Chip
                    key={index}
                    label={tag}
                    onDelete={() => handleTagDelete(index)} // 태그 삭제 처리
                    style={{ margin: "5px" }}
                  />
                ))}
              </div>

              {/* 미리보기 버튼 */}
              <div>
                <Button fullWidth variant="outlined">
                  미리보기
                </Button>
              </div>
            </div>
          </div>
        </div>

        {/* AI 도움받기 모달 */}
        <Modal open={aiModalOpen} onClose={closeAiModal}>
          <div
            style={{
              padding: "20px",
              backgroundColor: "#fff",
              margin: "auto",
              width: "400px",
              marginTop: "100px",
            }}
          >
            <Typography>AI 도움받기 결과</Typography>
            <TextField
              fullWidth
              multiline
              rows={4}
              value={aiGeneratedDescription}
              onChange={(e) => setAiGeneratedDescription(e.target.value)}
            />
            <Button onClick={closeAiModal}>닫기</Button>
            <Button
              variant="contained"
              style={{ marginLeft: "10px" }}
              onClick={confirmDescriptionRegistration}
            >
              설명으로 등록
            </Button>
          </div>
        </Modal>
      </div>
      <hr />

      {/* 상세설명 섹션 */}
      <div id="description">
        <Tabs value={0} indicatorColor="primary" textColor="primary">
          <Tab
            label="상세설명"
            onClick={() => scrollToSection("descriptionRef")}
          />
          <Tab label="선물구성" onClick={() => scrollToSection("package")} />
          <Tab label="서류제출" onClick={() => scrollToSection("document")} />
        </Tabs>
        <Typography variant="body1" style={{ marginTop: "10px" }}>
          <DetailPage setDescriptionDetail={setDescriptionDetail} setDescriptionImages={setDescriptionImages}/>
        </Typography>
      </div>

      <Divider style={{ margin: "20px 0" }} />

      {/* 패키지 섹션 */}
      <div id="package">
        <Tabs value={1} indicatorColor="primary" textColor="primary">
          <Tab
            label="상세설명"
            onClick={() => scrollToSection("descriptionRef")}
          />
          <Tab label="선물구성" onClick={() => scrollToSection("package")} />
          <Tab label="서류제출" onClick={() => scrollToSection("document")} />
        </Tabs>
        <Typography variant="body1" style={{ marginTop: "10px" }}>
          <Package />
        </Typography>
      </div>

      <Divider style={{ margin: "20px 0" }} />

      {/* 서류 섹션 */}
      <div id="document">
        <Tabs value={2} indicatorColor="primary" textColor="primary">
          <Tab
            label="상세설명"
            onClick={() => scrollToSection("descriptionRef")}
          />
          <Tab label="선물구성" onClick={() => scrollToSection("package")} />
          <Tab label="서류제출" onClick={() => scrollToSection("document")} />
        </Tabs>
        <Typography variant="body1" style={{ marginTop: "10px" }}>
          <ProjectDocument setReqDocs={setReqDocs} setCertDocs={setCertDocs} saveProject={saveProject} projectId={projectId}/>
        </Typography>
      </div>
    </LocalizationProvider>
    </div>
    <Footer />
    </>

  );
};

export default Register;
