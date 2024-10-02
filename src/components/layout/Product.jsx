import React from 'react';
import { Box, Grid, Typography, Card, CardMedia, CardContent, Button, IconButton, LinearProgress } from '@mui/material';
import FavoriteIcon from '@mui/icons-material/Favorite';
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import CoverImage from '../assets/coverImage.png';

// Example product data
const products = [
  {
    id: 1,
    title: '[마우스] 2만원대 버티컬 마우스',
    description: '무소음 클릭 X 트리플 멀티태스킹 기능',
    progress: 70,
    goal: '1,000,000원',
    daysLeft: 'D-9',
    image: CoverImage, // replace with actual image URLs
    hearted: true,
    host: '황지영',
  },
  {
    id: 2,
    title: '[멀티탭] 15구 괴물탭!',
    description: 'C타입 단자까지 품은 타워형 멀티탭',
    progress: 50,
    goal: '2,000,000원',
    daysLeft: 'D-15',
    image: CoverImage, // replace with actual image URLs
    hearted: false,
    host: '김민수',
  },
  {
    id: 3,
    title: '[충전케이블] 6-in-1 멀티 고속 충전 케이블',
    description: '독일에서 온 6-in-1 멀티 고속 충전 케이블',
    progress: 80,
    goal: '1,500,000원',
    daysLeft: 'D-5',
    image: CoverImage, // replace with actual image URLs
    hearted: true,
    host: '박지훈',
  },
  {
    id: 4,
    title: '[케이스] 아이패드에 아날로그 크래프트',
    description: '크래프트 스트리보드 케이스를 입히다.',
    progress: 60,
    goal: '3,000,000원',
    daysLeft: 'D-12',
    image: CoverImage, // replace with actual image URLs
    hearted: false,
    host: '이서연',
  },
  {
    id: 5,
    title: '[보조배터리] 충전 쉽고 가성비 좋은',
    description: '가성비 & 가심비 모두 만족 충전',
    progress: 90,
    goal: '500,000원',
    daysLeft: 'D-3',
    image: CoverImage, // replace with actual image URLs
    hearted: true,
    host: '김재원',
  },
];

// Individual product card component

// Individual product card component
export const ProductCard = ({ product }) => {
  return (<>
    
    <Card
    sx={{
      borderRadius: '15px',
      boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
      padding: 2,
      position: 'relative',
      width: '270px', // 고정된 가로 크기
      minWidth: '280px', // 최소 크기 설정
      maxWidth: '290px', // 최대 크기 설정
      transform: 'scale(0.95)', // 전체 요소의 크기를 0.9배로 축소
      transformOrigin: 'top left', // 스케일 기준점 설정
    }}>{/* 타이틀과 서브타이틀 */}
   
      <CardMedia
        component="img"
        image={product.image}
        sx={{ height: '170px', borderRadius: '5px' }} // 이미지 높이 증가
      />
      <IconButton
        sx={{ position: 'absolute', top: 20, right: 20, color: product.hearted ? 'red' : 'gray' }}
      >
        <FavoriteIcon />
      </IconButton>
      <CardContent>
        {/* Title */}
        <Typography variant="h6" component="div" sx={{ fontWeight: 'bold', fontSize: '0.9rem', mb: 1 }}>
          {product.title}
        </Typography>

        {/* Description */}
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

        {/* Progress bar */}
        <LinearProgress
          variant="determinate"
          value={product.progress}
          sx={{ height: 9, borderRadius: '5px', mt: 1, mb: 2 }}
        />

        {/* Host and Deadline */}
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Button
            variant="contained"
            color="secondary"
            size="small"
            sx={{ backgroundColor: '#5a87f7', borderRadius: '12px', fontSize: '0.75rem' }}
          >
            마감임박 {product.daysLeft}
          </Button>
          <Typography variant="body2" sx={{ fontWeight: 'bold', fontSize: '0.75rem' }}>
            진행자: {product.host}
          </Typography>
        </Box>
      </CardContent>
    </Card></>
  );
};

// Product recommendations section
export const ProductRecommendations = () => {
  return (
    <><Box sx={{ padding: 2, display: 'flex', flexDirection: 'column', alignItems: 'center', maxWidth: '100%' }}>
    {/* 타이틀과 서브타이틀 부분 */}
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        
        marginRight:160,
        height:20,

        marginTop:1,
      }}
    >
      <Typography variant="h5" component="div" sx={{ fontWeight: 'bold', mb: 1 }}>
      <p className="text"> {/* 5px 패딩 적용 */}
                                <span className="text-wrapper">[담ː따] 의 </span>
                                <span className="span">기획전</span>
                            </p> <Typography variant="body2" color="text.secondary">
        담따의 인기 상품들을 한눈에!{/* 서브타이틀 */}
      </Typography>
       </Typography>
     
    </Box>
  
    </Box>
    <Box sx={{ margin: '0 auto', padding: 2, display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', maxWidth: '100%' }}>
      {/* Title section similar to the example image */}
      
  
          {/* 상품 카드 그리드 */}   
           <Box
        sx={{
          margin: '0 auto',
          padding: 2,
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          alignItems: 'center',
          maxWidth: '100%',
        }}
      >
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            width: '100%',
            height: 'auto',
          }}
        >
             {/* 왼쪽 화살표 */}
             <IconButton sx={{ marginRight: '50px'}}>
            <ArrowBackIosNewIcon />
          </IconButton>

          <Grid container justifyContent="center" alignItems="center" spacing={2} sx={{ flexGrow: 1 }}>
            {products.map((product) => (
              <Grid item key={product.id} 
              xs={12}
              sm={6}
              md={4}
              lg={3}
              xl={2.4}
              display="flex" justifyContent="center">
                <ProductCard product={product} />
              </Grid>
            ))}
          </Grid>

          {/* 오른쪽 화살표 */}
          <IconButton sx={{ marginLeft: '50px'}}>
          <ArrowForwardIosIcon />
          </IconButton>
        </Box> </Box> </Box></>
  );
};
