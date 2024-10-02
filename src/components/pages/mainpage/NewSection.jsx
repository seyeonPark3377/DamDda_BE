import React from 'react';
import { Box, Grid, Typography, Button, Card, CardContent, CardMedia } from '@mui/material';
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';

// Example data for cards
const cardData = [
  {
    title: '펀딩 뉴스',
    description: '1억 목표 달성! 성공적인 펀딩 프로젝트의 비결을 확인하세요.',
    buttonText: '자세히 보기',
    imageUrl: 'https://via.placeholder.com/300x200', // Replace with actual image URL
  },
  {
    title: '프로모션',
    description: '최대 50% 할인! 지금 펀딩에 참여하고 특별 혜택을 누리세요.',
    buttonText: '자세히 보기',
    imageUrl: 'https://via.placeholder.com/300x200', // Replace with actual image URL
  },
  {
    title: '전통 문화 성공 사례',
    description: '혁신적인 아이디어로 3천만원 펀딩을 달성한 프로젝트 소개.',
    buttonText: '자세히 보기',
    imageUrl: 'https://via.placeholder.com/300x200', // Replace with actual image URL
  },
  {
    title: 'K-POP 새로운 프로젝트',
    description: '최신 펀딩 프로젝트에 지금 참여하세요!',
    buttonText: '자세히 보기',
    imageUrl: 'https://via.placeholder.com/300x200', // Replace with actual image URL
  },
];


const CardComponent = ({ title, description, buttonText, imageUrl }) => (
  <Card sx={{ borderRadius: '15px', boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.1)', height: '100%' }}>
    <CardMedia component="img" image={imageUrl} alt={title} sx={{ height: 180 }} />
    <CardContent>
      <Typography variant="h6" component="div" sx={{ fontWeight: 'bold', marginBottom: 1 }}>
        {title}
      </Typography>
      <Typography variant="body2" color="text.secondary" sx={{ marginBottom: 2 }}>
        {description}
      </Typography>
      <Button variant="text" sx={{ color: '#0071e3' }}>
        {buttonText} &rarr;
      </Button>
    </CardContent>
  </Card>
);

const NewSection = () => (
  <Box sx={{ width: '80%', margin: 'auto', marginTop: 4,maxWidth:1500,height:500 }}>
    <Typography variant="h5" sx={{ fontWeight: 'bold', marginBottom: 4 }}>
      담따의 최신 소식을 확인하세요
    </Typography>
    <Box sx={{ display: 'flex', alignItems: 'center' }}>
      <Grid container spacing={6} sx={{ flexGrow: 1 }}>
        {cardData.map((card, index) => (
          <Grid item xs={12} sm={6} md={3} key={index}>
            <CardComponent {...card} />
          </Grid>
        ))}
      </Grid>
    </Box>
  </Box>
);

export default NewSection;