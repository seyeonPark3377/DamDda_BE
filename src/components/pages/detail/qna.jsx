import React, { useState } from "react";
import {
  Typography,
  Card,
  CardContent,
  Button,
  TextField,
  IconButton,
  Divider,
  Avatar,
  Chip,
} from "@mui/material";
import {
  Delete,
  Comment,
  Edit,
  ArrowDropDown,
  ArrowDropUp,
} from "@mui/icons-material";

const Qna = () => {
  const [questions, setQuestions] = useState([
    {
      id: 1,
      author: "배선미",
      profileImage: "https://via.placeholder.com/40",
      date: new Date(2024, 6, 23, 14, 0).toLocaleString(),
      title: "13100427편당하고 갑니다.",
      content:
        "애샛자주 마시는데 유용템이든합니다. 다른회사 크림치제품 먹어봤는데...",
      comments: [
        {
          id: 1,
          author: "NOW4U",
          profileImage: "https://via.placeholder.com/40",
          date: new Date(2024, 6, 23, 16, 0).toLocaleString(),
          content: "안녕하세요! 해산미 서포터님, 나우포유입니다!",
          replies: [], // Support for one level of replies
        },
      ],
    },
  ]);

  const [newTitle, setNewTitle] = useState("");
  const [newContent, setNewContent] = useState("");
  const [replyContent, setReplyContent] = useState({});
  const [showComments, setShowComments] = useState({});
  const [replyingTo, setReplyingTo] = useState(null); // To handle reply input state

  const handleAddQuestion = () => {
    const newQuestion = {
      id: questions.length + 1,
      author: "사용자D",
      profileImage: "https://via.placeholder.com/40",
      date: new Date().toLocaleString(),
      title: newTitle || "문의제목입니다",
      content: newContent || "문의 내용을 입력해주세요.",
      comments: [],
    };
    setQuestions((prevQuestions) => [...prevQuestions, newQuestion]);
    setNewTitle("");
    setNewContent("");
  };

  // Handle deleting a question
  const handleDeleteQuestion = (questionId) => {
    const updatedQuestions = questions.filter(
      (question) => question.id !== questionId
    );
    setQuestions(updatedQuestions);
  };

  const toggleComments = (id) => {
    setShowComments((prev) => ({
      ...prev,
      [id]: !prev[id],
    }));
  };

  const handleAddReply = (questionId, commentId) => {
    if (!replyContent[commentId]) return;

    const updatedQuestions = questions.map((question) => {
      if (question.id === questionId) {
        return {
          ...question,
          comments: question.comments.map((comment) => {
            if (comment.id === commentId) {
              return {
                ...comment,
                replies: [
                  ...comment.replies,
                  {
                    id: comment.replies.length + 1,
                    author: "현재 사용자", // Replace with the logged-in user
                    profileImage: "https://via.placeholder.com/40",
                    date: new Date().toLocaleString(),
                    content: replyContent[commentId],
                  },
                ],
              };
            }
            return comment;
          }),
        };
      }
      return question;
    });

    setQuestions(updatedQuestions);
    setReplyContent((prev) => ({ ...prev, [commentId]: "" }));
    setReplyingTo(null); // Close the reply input
  };

  const handleDeleteComment = (questionId, commentId) => {
    const updatedQuestions = questions.map((question) => {
      if (question.id === questionId) {
        return {
          ...question,
          comments: question.comments.filter(
            (comment) => comment.id !== commentId
          ),
        };
      }
      return question;
    });

    setQuestions(updatedQuestions);
  };

  const handleDeleteReply = (questionId, commentId, replyId) => {
    const updatedQuestions = questions.map((question) => {
      if (question.id === questionId) {
        return {
          ...question,
          comments: question.comments.map((comment) => {
            if (comment.id === commentId) {
              return {
                ...comment,
                replies: comment.replies.filter(
                  (reply) => reply.id !== replyId
                ),
              };
            }
            return comment;
          }),
        };
      }
      return question;
    });

    setQuestions(updatedQuestions);
  };

  return (
    <div style={{ padding: "20px", maxWidth: "1000px", margin: "0 auto" }}>
      {/* 질문 등록 폼 */}
      <div
        style={{
          marginBottom: "50px",
          maxWidth: "700px",
          margin: "0 auto",
        }}
      >
        <Typography
          variant="h6"
          style={{ fontWeight: "bold", marginBottom: "20px" }}
        >
          사용자이름
        </Typography>
        <TextField
          label="제목을 입력해주세요."
          fullWidth
          value={newTitle}
          onChange={(e) => setNewTitle(e.target.value)}
          style={{ marginBottom: "10px" }}
        />
        <TextField
          label="내용을 입력해주세요."
          fullWidth
          multiline
          rows={4}
          value={newContent}
          onChange={(e) => setNewContent(e.target.value)}
          style={{ marginBottom: "10px" }}
        />
        <Button
          variant="contained"
          onClick={handleAddQuestion}
          style={{ float: "right" }}
        >
          문의하기
        </Button>
      </div>

      {/* 등록된 질문 리스트 */}
      <div style={{ maxWidth: "1000px", margin: "0 auto" }}>
        {questions.map((question, index) => (
          <React.Fragment key={question.id}>
            <Card
              style={{
                marginBottom: "20px",
                backgroundColor: "#fff",
                borderRadius: "10px",
                boxShadow: "none",
              }}
            >
              <CardContent>
                <div style={{ display: "flex", alignItems: "flex-start" }}>
                  {/* 프로필 이미지 */}
                  <Avatar
                    src={question.profileImage}
                    alt={question.author}
                    style={{ marginRight: "10px" }}
                  />
                  <div style={{ flexGrow: 1 }}>
                    {/* 사용자 이름과 수정/삭제 버튼 */}
                    <div
                      style={{
                        display: "flex",
                        justifyContent: "space-between",
                      }}
                    >
                      <Typography
                        variant="subtitle1"
                        style={{ fontWeight: "bold" }}
                      >
                        {question.author}
                      </Typography>
                      <div>
                        <IconButton>
                          <Edit fontSize="small" />
                        </IconButton>
                        <IconButton
                          onClick={() => handleDeleteQuestion(question.id)}
                        >
                          <Delete fontSize="small" />
                        </IconButton>
                      </div>
                    </div>
                    <Typography variant="caption" color="textSecondary">
                      {question.date}
                    </Typography>

                    {/* 공지 및 질문 태그 + 내용 영역, 보더 포함 */}
                    <div
                      style={{
                        padding: "10px",
                        border: "1px solid #ddd",
                        borderRadius: "8px",
                        marginTop: "10px",
                      }}
                    >
                      <div style={{ marginBottom: "10px" }}>
                        <Chip
                          label="공지"
                          color="primary"
                          style={{ marginRight: "5px" }}
                          size="small"
                        />
                        <Chip
                          label="질문"
                          style={{
                            backgroundColor: "#6A1B9A", // Changed to a purple color for "질문"
                            color: "#fff",
                          }}
                          size="small"
                        />
                      </div>

                      {/* 제목 */}
                      <Typography variant="h6" style={{ marginBottom: "10px" }}>
                        {question.title}
                      </Typography>

                      {/* 내용 */}
                      <Typography variant="body1" color="textSecondary">
                        {question.content}
                      </Typography>
                    </div>

                    {/* 댓글 아이콘 */}
                    <div
                      style={{
                        marginTop: "10px",
                        display: "flex",
                        cursor: "pointer",
                      }}
                    >
                      <IconButton
                        size="small"
                        onClick={() => toggleComments(question.id)}
                      >
                        {showComments[question.id] ? (
                          <ArrowDropUp />
                        ) : (
                          <ArrowDropDown />
                        )}
                      </IconButton>
                      <Typography
                        variant="body2"
                        style={{ fontSize: "14px", marginLeft: "5px" }} // Font adjustment
                      >
                        {question.comments.length} 댓글
                      </Typography>
                    </div>

                    {/* 댓글 목록 */}
                    {showComments[question.id] && (
                      <div style={{ marginTop: "10px", marginLeft: "20px" }}>
                        {/* 댓글들 */}
                        {question.comments.map((comment) => (
                          <div
                            key={comment.id}
                            style={{ marginBottom: "20px" }}
                          >
                            <div
                              style={{
                                display: "flex",
                                alignItems: "flex-start",
                              }}
                            >
                              <Avatar
                                src={comment.profileImage}
                                alt={comment.author}
                                style={{ marginRight: "10px" }}
                              />
                              <div style={{ flexGrow: 1 }}>
                                <div
                                  style={{
                                    display: "flex",
                                    justifyContent: "space-between",
                                  }}
                                >
                                  <Typography
                                    variant="subtitle1"
                                    style={{ fontWeight: "bold" }}
                                  >
                                    {comment.author}
                                  </Typography>
                                  <div>
                                    <IconButton>
                                      <Edit fontSize="small" />
                                    </IconButton>
                                    <IconButton
                                      onClick={() =>
                                        handleDeleteComment(
                                          question.id,
                                          comment.id
                                        )
                                      }
                                    >
                                      <Delete fontSize="small" />
                                    </IconButton>
                                  </div>
                                </div>
                                <Typography
                                  variant="caption"
                                  color="textSecondary"
                                >
                                  {comment.date}
                                </Typography>

                                <Typography
                                  variant="body1"
                                  color="textSecondary"
                                  style={{ marginTop: "10px" }}
                                >
                                  {comment.content}
                                </Typography>

                                {/* 답글 목록 */}
                                {comment.replies.map((reply) => (
                                  <div
                                    key={reply.id}
                                    style={{
                                      marginLeft: "20px",
                                      marginTop: "10px",
                                      paddingLeft: "20px",
                                      borderLeft: "2px solid #ddd",
                                    }}
                                  >
                                    <div
                                      style={{
                                        display: "flex",
                                        alignItems: "flex-start",
                                      }}
                                    >
                                      <Avatar
                                        src={reply.profileImage}
                                        alt={reply.author}
                                        style={{ marginRight: "10px" }}
                                      />
                                      <div style={{ flexGrow: 1 }}>
                                        <div
                                          style={{
                                            display: "flex",
                                            justifyContent: "space-between",
                                          }}
                                        >
                                          <Typography
                                            variant="subtitle1"
                                            style={{ fontWeight: "bold" }}
                                          >
                                            {reply.author}
                                          </Typography>
                                          <div>
                                            <IconButton>
                                              <Edit fontSize="small" />
                                            </IconButton>
                                            <IconButton
                                              onClick={() =>
                                                handleDeleteReply(
                                                  question.id,
                                                  comment.id,
                                                  reply.id
                                                )
                                              }
                                            >
                                              <Delete fontSize="small" />
                                            </IconButton>
                                          </div>
                                        </div>
                                        <Typography
                                          variant="caption"
                                          color="textSecondary"
                                        >
                                          {reply.date}
                                        </Typography>

                                        <Typography
                                          variant="body1"
                                          color="textSecondary"
                                          style={{ marginTop: "10px" }}
                                        >
                                          {reply.content}
                                        </Typography>
                                      </div>
                                    </div>
                                  </div>
                                ))}

                                {/* 답글 입력 폼 */}
                                {replyingTo === comment.id && (
                                  <div style={{ marginTop: "10px" }}>
                                    <TextField
                                      label="답글을 작성해주세요"
                                      fullWidth
                                      multiline
                                      rows={2}
                                      value={replyContent[comment.id] || ""}
                                      onChange={(e) =>
                                        setReplyContent((prev) => ({
                                          ...prev,
                                          [comment.id]: e.target.value,
                                        }))
                                      }
                                      style={{ marginBottom: "10px" }}
                                    />
                                    <Button
                                      variant="contained"
                                      onClick={() =>
                                        handleAddReply(question.id, comment.id)
                                      }
                                    >
                                      답글 작성
                                    </Button>
                                  </div>
                                )}

                                {!replyingTo && (
                                  <Button
                                    size="small"
                                    onClick={() => setReplyingTo(comment.id)}
                                  >
                                    답글 달기
                                  </Button>
                                )}
                              </div>
                            </div>
                          </div>
                        ))}
                      </div>
                    )}
                  </div>
                </div>
              </CardContent>
            </Card>
            {/* 구분선 */}
            {index < questions.length - 1 && (
              <Divider style={{ marginBottom: "20px" }} />
            )}
          </React.Fragment>
        ))}
      </div>
    </div>
  );
};

export default Qna;
