// import React, { useState, useEffect } from "react";
// import AspectRatio from "@mui/joy/AspectRatio";
// import Card from "@mui/joy/Card";
// import CardContent from "@mui/joy/CardContent";
// import Typography from "@mui/joy/Typography";
// import Box from "@mui/joy/Box";
// import StatusButton from "./StatusButton"; // StatusButton 컴포넌트 import
// import PaymentDeliveryInfo from "./PaymentDeliveryInfo"; // 결제/배송 정보 컴포넌트
// import axios from 'axios'; // axios import 추가

// export default function ProjectCard({ project }) {
//   const [showDetails, setShowDetails] = useState(false); // 결제/배송 정보 표시 상태
//   const [paymentData, setPaymentData] = useState(null); // 결제 정보 데이터 상태
  
//   const [orderData, setOrderData] = useState([]); // 주문 데이터를 저장할 상태
//   const [loading, setLoading] = useState(true); // 로딩 상태 관리
//   const [error, setError] = useState(null); // 에러 상태 관리

//   // 주문 정보 가져오기
//   const fetchOrderData = async () => {
//     try {
//       const response = await axios.get(`http://localhost:9000/order/supportingprojects/1`);
//       setOrderData(response.data);
//       setLoading(false); // 데이터를 가져왔으므로 로딩 완료
//     } catch (err) {
//       setError(err.message);
//       setLoading(false); // 에러가 발생해도 로딩 완료
//     }
//   };

//   // 결제/배송 정보 토글
//   const toggleDetails = () => {
//     if (!showDetails && !paymentData) {
//       // 데이터가 없는 상태에서만 데이터를 가져옴
//       fetchOrderData();
//     }
//     setShowDetails(!showDetails); // 결제/배송 정보 표시 상태 토글
//   };

//   useEffect(() => {
//     fetchOrderData(); // 컴포넌트가 마운트될 때 주문 정보 가져오기
//   }, []);

//   // 로딩 중인 경우 처리
//   if (loading) {
//     return <p>로딩 중...</p>;
//   }

//   // // 에러 발생 시 처리
//   // if (error) {
//   //   return <p>에러 발생: {error}</p>;
//   // }

//   return (
//     <>
//       <Card
//         size="lg"
//         variant="outlined"
//         sx={{
//           maxWidth: "80%",
//           padding: "10px",
//           display: "flex",
//           flexDirection: "row",
//           alignItems: "center",
//           borderRadius: "16px",
//           boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
//           backgroundColor: "white",
//           justifyContent: "space-between",
//           marginBottom: "15px",
//         }}
//       >
//         {/* 왼쪽에 썸네일을 넣는 부분 */}
//         <Box sx={{ flex: "0 0 150px", marginRight: "5px" }}>
//           <AspectRatio ratio="1" sx={{ width: "100%", height: "auto" }}>
//             <img
//               src={project.thumbnailUrl}
//               alt="프로젝트 썸네일"
//               style={{ objectFit: "cover", borderRadius: "8px" }}
//             />
//           </AspectRatio>
//         </Box>

//         {/* 중앙 텍스트 정보 */}
//         <CardContent
//           sx={{ flex: 1, marginRight: "5px", padding: "8px", margin: "0px" }}
//         >
//           <Typography level="h5" fontWeight="bold" sx={{ mb: 1 }}>
//             [{project.title}]
//           </Typography>
//           <Typography level="body2" sx={{ mb: 1, color: "text.secondary" }}>
//             선물 구성
//           </Typography>
//           <Typography level="body2" sx={{ mb: 1, color: "text.secondary" }}>
//             {project.packageName}
//           </Typography>
//           <Typography level="body2" sx={{ mb: 1, color: "text.secondary" }}>
//             후원 금액: {project.packagePrice.toLocaleString()}원
//           </Typography>
//         </CardContent>

//         {/* 오른쪽 정보 (상단: 후원번호와 결제 날짜, 하단: 버튼) */}
//         <Box
//           sx={{
//             display: "flex",
//             flexDirection: "column",
//             justifyContent: "space-between",
//             alignItems: "flex-end",
//             minWidth: "160px",
//           }}
//         >
//           {/* 상단에 후원번호와 결제날짜 */}
//           <Box sx={{ textAlign: "right", marginBottom: "80px" }}>
//             <Typography
//               level="body2"
//               sx={{ mb: 1, fontSize: "0.875rem", color: "text.secondary" }}
//             >
//               후원번호: {project.supportNumber}
//             </Typography>
//             <Typography
//               level="body2"
//               sx={{ fontSize: "0.875rem", color: "text.secondary" }}
//             >
//               결제 날짜: {new Date(project.paymentDate).toLocaleString()}
//             </Typography>
//           </Box>

//           {/* 하단에 진행 상태와 결제 정보 버튼 */}
//           <Box
//             sx={{
//               display: "flex",
//               gap: "5px",
//               justifyContent: "flex-end",
//               marginTop: "auto",
//             }}
//           >
//             <StatusButton
//               status={project.status}
//               label={project.status === "진행중" ? "진행중" : "마감"}
//             />
//             <StatusButton
//               status="결제/배송 정보"
//               label="결제/배송 정보"
//               onClick={toggleDetails}
//             />
//           </Box>
//         </Box>
//       </Card>
//       {/* 결제/배송 정보 표시 */}
//       {showDetails && <PaymentDeliveryInfo project={orderData} />} {/* 실제 데이터를 보여주기 */}
//     </>
//   );
// }
import React, { useState } from "react";
import AspectRatio from "@mui/joy/AspectRatio";
import Card from "@mui/joy/Card";
import CardContent from "@mui/joy/CardContent";
import Typography from "@mui/joy/Typography";
import Box from "@mui/joy/Box";
import StatusButton from "./StatusButton"; // StatusButton 컴포넌트 import
import PaymentDeliveryInfo from "./PaymentDeliveryInfo"; // 결제/배송 정보 컴포넌트

export default function ProjectCard({ project }) {
  const [showDetails, setShowDetails] = useState(false); // 결제/배송 정보 표시 상태

  // 결제/배송 정보 표시 토글
  const toggleDetails = () => {
    setShowDetails(!showDetails); // 토글
  };

  return (
    <>
      <Card
        size="lg"
        variant="outlined"
        sx={{
          maxWidth: "80%",
          padding: "10px",
          display: "flex",
          flexDirection: "row",
          alignItems: "center",
          borderRadius: "16px",
          boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
          backgroundColor: "white",
          justifyContent: "space-between",
          marginBottom: "15px",
        }}
      >
        {/* 왼쪽에 썸네일을 넣는 부분 */}
        <Box sx={{ flex: "0 0 150px", marginRight: "5px" }}>
          <AspectRatio ratio="1" sx={{ width: "100%", height: "auto" }}>
            <img
              // src={project.supportingProject.project.thumbnailUrl}
              alt="프로젝트 썸네일"
              style={{ objectFit: "cover", borderRadius: "8px" }}
            />
          </AspectRatio>
        </Box>

        {/* 중앙 텍스트 정보 */}
        <CardContent
          sx={{ flex: 1, marginRight: "5px", padding: "8px", margin: "0px" }}
        >
          <Typography level="h5" fontWeight="bold" sx={{ mb: 1 }}>
            [{project.supportingProject.project.title}]
          </Typography>
          <Typography level="body2" sx={{ mb: 1, color: "text.secondary" }}>
            선물 구성: {project.supportingPackage.packageName}
          </Typography>
          <Typography level="body2" sx={{ mb: 1, color: "text.secondary" }}>
            후원 금액: {parseInt(project.supportingPackage.packagePrice).toLocaleString()}원
          </Typography>
        </CardContent>

        {/* 오른쪽 정보 (상단: 후원번호와 결제 날짜, 하단: 버튼) */}
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            justifyContent: "space-between",
            alignItems: "flex-end",
            minWidth: "160px",
          }}
        >
          {/* 상단에 후원번호와 결제날짜 */}
          <Box sx={{ textAlign: "right", marginBottom: "80px" }}>
            <Typography
              level="body2"
              sx={{ mb: 1, fontSize: "0.875rem", color: "text.secondary" }}
            >
              후원번호: {project.delivery.deliveryId}
            </Typography>
            <Typography
              level="body2"
              sx={{ fontSize: "0.875rem", color: "text.secondary" }}
            >
              결제 날짜: {new Date(project.supportingProject.supportedAt).toLocaleString()}
            </Typography>
          </Box>

          {/* 하단에 진행 상태와 결제 정보 버튼 */}
          <Box
            sx={{
              display: "flex",
              gap: "5px",
              justifyContent: "flex-end",
              marginTop: "auto",
            }}
          >
            <StatusButton
              status={project.status}
              label={project.status === "진행중" ? "진행중" : "마감"}
            />
            <StatusButton
              status="결제/배송 정보"
              label="결제/배송 정보"
              onClick={toggleDetails}
            />
          </Box>
        </Box>
      </Card>
      {/* 결제/배송 정보 표시 */}
      {showDetails && <PaymentDeliveryInfo project={project} />} {/* 실제 데이터를 보여주기 */}
    </>
  );
}
