import React, { useState } from "react";
import {
  Typography,
  Button,
  IconButton,
  Divider,
  Chip,
  TextField,
} from "@mui/material";
import { Edit, Delete } from "@mui/icons-material";

const Notice = () => {
  // 공지사항 리스트를 관리하는 상태 (처음에는 세 개의 더미 데이터로 초기화)
  const [notices, setNotices] = useState([
    {
      id: 1,
      title: "1번째 예시 공지사항 제목",
      content: "1번째 예시 공지사항 내용입니다.",
      date: new Date(2024, 6, 23).toLocaleDateString(),
      imageUrl: "https://via.placeholder.com/80", // Replace with actual image URL
    },
    {
      id: 2,
      title: "2번째 예시 공지사항 제목",
      content: "2번째 예시 공지사항 내용입니다.",
      date: new Date(2023, 7, 1).toLocaleDateString(),
      imageUrl: "https://via.placeholder.com/80",
    },
    {
      id: 3,
      title: "3번째 예시 공지사항 제목",
      content: "3번째 예시 공지사항 내용입니다.",
      date: new Date(2024, 4, 10).toLocaleDateString(),
      imageUrl: "https://via.placeholder.com/80",
    },
  ]);

  // 새롭게 추가할 공지사항의 제목과 내용을 관리하는 상태
  const [newTitle, setNewTitle] = useState("");
  const [newContent, setNewContent] = useState("");

  // 새로운 공지사항을 추가하는 함수
  const handleAddNotice = () => {
    const newNotice = {
      id: notices.length + 1,
      title: newTitle || "공지사항 제목을 입력해주세요.",
      content: newContent || "진행자가 내용을 입력합니다.",
      date: new Date().toLocaleDateString(),
      imageUrl: "https://via.placeholder.com/80",
    };
    setNotices((prevNotices) => [...prevNotices, newNotice]);
    setNewTitle("");
    setNewContent("");
  };

  // 공지사항을 삭제하는 함수 (ID를 기준으로 필터링하여 삭제)
  const handleDeleteNotice = (id) => {
    setNotices(notices.filter((notice) => notice.id !== id));
  };

  // 최신순으로 공지사항을 정렬하는 함수
  const sortedNotices = notices.sort(
    (a, b) => new Date(b.date) - new Date(a.date)
  );

  return (
    <div style={{ padding: "20px", maxWidth: "1000px", margin: "0 auto" }}>
      {/* 공지사항 입력 영역 */}
      <div
        style={{
          marginBottom: "50px", // 아래쪽에 여유 공간 추가
          maxWidth: "700px", // 입력 영역의 최대 너비 설정
          margin: "0 auto", // 가운데 정렬
        }}
      >
        {/* 공지사항 입력하기 제목 */}
        <Typography
          variant="h6"
          style={{ fontWeight: "bold", marginBottom: "20px" }}
        >
          공지사항 입력하기
        </Typography>

        {/* 제목 입력 필드 */}
        <TextField
          label="제목을 입력해주세요."
          fullWidth
          value={newTitle}
          onChange={(e) => setNewTitle(e.target.value)}
          style={{ marginBottom: "10px" }}
        />
        {/* 내용 입력 필드 */}
        <TextField
          label="내용을 입력해주세요."
          fullWidth
          multiline
          rows={4}
          value={newContent}
          onChange={(e) => setNewContent(e.target.value)}
          style={{ marginBottom: "10px" }}
        />
        {/* 공지사항 등록 버튼 */}
        <Button
          variant="contained"
          onClick={handleAddNotice}
          style={{ float: "right" }}
        >
          등록하기
        </Button>
      </div>

      {/* 등록된 공지사항 리스트 */}
      <div style={{ clear: "both" }}>
        {sortedNotices.map((notice, index) => (
          <React.Fragment key={notice.id}>
            {/* 공지사항 카드 */}
            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
                padding: "15px",
                alignItems: "center",
                borderBottom: "1px solid #e0e0e0",
              }}
            >
              <div style={{ flex: 1 }}>
                <div style={{ display: "flex", alignItems: "center" }}>
                  {/* 공지, 중요 뱃지 - 모든 공지사항에 붙음 */}
                  <Chip
                    label="공지"
                    style={{
                      marginRight: "5px",
                      backgroundColor: "#007bff",
                      color: "#fff",
                    }}
                    size="small"
                  />
                  <Chip
                    label="중요"
                    style={{
                      marginRight: "5px",
                      backgroundColor: "#ff9800",
                      color: "#fff",
                    }}
                    size="small"
                  />
                  {/* 제목 */}
                  <Typography
                    variant="h6"
                    style={{
                      fontWeight: "bold",
                      marginRight: "10px",
                      overflow: "hidden",
                      textOverflow: "ellipsis",
                      whiteSpace: "nowrap",
                    }}
                  >
                    {notice.title}
                  </Typography>
                </div>
                {/* 내용 */}
                <Typography variant="body1" style={{ marginTop: "10px" }}>
                  {notice.content}
                </Typography>
                {/* 작성일자 */}
                <Typography
                  variant="body2"
                  style={{ color: "#888", marginTop: "5px" }}
                >
                  작성자: 관리자 | {notice.date}
                </Typography>
              </div>

              {/* 이미지 */}
              <div style={{ marginLeft: "20px" }}>
                <img
                  src={notice.imageUrl}
                  alt="공지 이미지"
                  style={{ width: "80px", height: "60px", borderRadius: "5px" }}
                />
              </div>

              {/* 수정/삭제 버튼 */}
              <div style={{ marginLeft: "20px" }}>
                <IconButton>
                  <Edit fontSize="small" />
                </IconButton>
                <IconButton onClick={() => handleDeleteNotice(notice.id)}>
                  <Delete fontSize="small" />
                </IconButton>
              </div>
            </div>
            {/* 구분선 */}
            {index < notices.length - 1 && (
              <Divider style={{ margin: "0px" }} />
            )}
          </React.Fragment>
        ))}
      </div>
    </div>
  );
};

export default Notice;
