import React, { useState } from 'react';
import { Box, Grid, Typography, Card, CardMedia, CardContent, Button, IconButton, LinearProgress } from '@mui/material';
import FavoriteIcon from '@mui/icons-material/Favorite';
import Pagination from '@mui/material/Pagination';
import Stack from '@mui/material/Stack';
import StatusButton from './StatusButton';
// import axios from 'axios'; // 나중에 백엔드 연결 시 주석 해제

// 가짜 프로젝트 데이터 배열(11개의 데이터로 페이징 처리 확인 가능)
const projectList = Array.from({ length: 11 }, (_, index) => ({
  id: index + 1,
  title: `[프로젝트 ${index + 1}] 제목`,
  description: `${index + 1}번째 프로젝트의 설명입니다.`,
  progress: Math.ceil(Math.random() * 100),
  goal: `${(index + 1) * 500000}원`,
  daysLeft: `D-${index + 5}`,
  image: `https://example.com/project${index + 1}.jpg`,
  hearted: index % 2 === 0,
  host: `호스트 ${index + 1}`,
  approval: index % 3 === 0 ? 1 : index % 3 === 1 ? 0 : -1, // 승인상태
}));

// ProductCard 컴포넌트 정의
const ProductCard = ({ product }) => (
  <Card sx={{ borderRadius: '15px', boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)', padding: 2, position: 'relative', width: '100%' }}>
    <CardMedia
      component="img"
      image={product.image}
      sx={{ height: '180px', borderRadius: '10px', objectFit: 'cover' }}
    />
    <IconButton
      sx={{ position: 'absolute', top: 10, right: 10, color: product.hearted ? 'red' : 'gray' }}
    >
      <FavoriteIcon />
    </IconButton>
    <CardContent>
      <Typography variant="h6" component="div" sx={{ fontWeight: 'bold', fontSize: '1rem', mb: 1 }}>
        {product.title}
      </Typography>
      <Typography variant="body2" color="text.secondary" sx={{ fontSize: '0.85rem', mb: 1 }}>
        {product.description}
      </Typography>
      <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          <Typography variant="body2" sx={{ fontWeight: 'bold', fontSize: '0.75rem' }}>
            달성률 {product.progress}%
          </Typography>
          <Typography variant="body2" sx={{ fontWeight: 'bold', fontSize: '0.75rem' }}>
            {product.goal}
          </Typography>
        </Box>
      </Box>
      <LinearProgress variant="determinate" value={product.progress} sx={{ height: 8, borderRadius: '5px', mt: 1, mb: 1 }} />
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Button variant="contained" color="secondary" size="small" sx={{ backgroundColor: '#5a87f7', borderRadius: '12px', fontSize: '0.75rem' }}>
          마감임박 {product.daysLeft}
        </Button>
        <Typography variant="body2" sx={{ fontWeight: 'bold', fontSize: '0.75rem' }}>
          진행자: {product.host}
        </Typography>
      </Box>
    </CardContent>
  </Card>
);

// Likeproject 컴포넌트
const Likeproject = () => {
  const itemsPerPage = 10; // 한 페이지에 10개의 항목을 표시
  const [page, setPage] = useState(1); // 현재 페이지 상태
  const totalProducts = projectList.length; // 전체 프로젝트 개수

  // 페이지 변경 핸들러
  const handlePageChange = (event, value) => {
    setPage(value);
  };

  // 현재 페이지에서 보여줄 프로젝트들만 추출
  const displayedProducts = projectList.slice((page - 1) * itemsPerPage, page * itemsPerPage);

  return (
    <Box sx={{ margin: '0 auto', padding: 2, display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', maxWidth: '100%' }}>
      <Grid container justifyContent="center" alignItems="center" spacing={4} sx={{ flexGrow: 1 }}>
        {displayedProducts.map((product) => (
          <Grid item key={product.id} xs={12} sm={6} md={2.4} display="flex" justifyContent="center">
            <ProductCard product={product} />
          </Grid>
        ))}
      </Grid>

      {/* Pagination 컴포넌트 추가 */}
      <Stack spacing={2} sx={{ marginTop: '20px' }}>
        <Pagination
          count={Math.ceil(totalProducts / itemsPerPage)} // 페이지 수 계산
          page={page} // 현재 페이지
          onChange={handlePageChange} // 페이지 변경 핸들러
          showFirstButton
          showLastButton
        />
      </Stack>
    </Box>
  );
};

export default Likeproject;
