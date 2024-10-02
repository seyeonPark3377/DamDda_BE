import React from 'react';
import { Box, Typography, Grid, Card, CardContent } from '@mui/material';
import InsertDriveFileIcon from '@mui/icons-material/InsertDriveFile'; // Placeholder for document icon
import AttachMoneyIcon from '@mui/icons-material/AttachMoney'; // Placeholder for money icon
import StarIcon from '@mui/icons-material/Star';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';

const services = [
  {
    title: '협업하기',
    description: '진행자와 함께 협업하고 성공적인 프로젝트를 만들어보세요.',
    icon: <InsertDriveFileIcon sx={{ fontSize: 50, color: 'white' }} />, 
    backgroundColor: '#ff914d',
  },
  {
    title: '프로젝트 등록하기',
    description: '새로운 프로젝트를 등록하고 펀딩을 시작하세요.',
    icon: <AddCircleOutlineIcon sx={{ fontSize: 50, color: 'white' }} />,
    backgroundColor: '#4b87f5',
  },
  {
    title: '인기 프로젝트 가기',
    description: '가장 인기 있는 프로젝트에 참여하고 후원하세요.',
    icon: <StarIcon sx={{ fontSize: 50, color: 'white' }} />,
    backgroundColor: '#ffc107',
  },
];


export const ServiceCards = () => {
  return (

    <Box sx={{ width: '100%', padding: '40px 0', backgroundColor: '#f8f8f8', display: 'flex',justifyContent: 'center' ,margin: 'auto 0'  }}>
      <Grid container justifyContent="center" spacing={5} sx={{ maxWidth: '1400px',textAlign: 'center'}}>
        {services.map((service, index) => (
          <Grid item xs={12} sm={6} md={4} key={index}>
            <Card
              sx={{
                display: 'flex',
                alignItems: 'center',
                borderRadius: '20px',
                backgroundColor: service.backgroundColor,
                color: 'white',
                padding: '20px',
              }}
            >
              <Box sx={{ flexGrow: 1 }}>
                <Typography variant="subtitle2" sx={{ fontWeight: 'bold' }}>
                  {service.title}
                </Typography>
                <Typography variant="body1" sx={{ marginTop: 1 }}>
                  {service.description}
                </Typography>
              </Box>
              <Box sx={{ marginLeft: 2 }}>{service.icon}</Box>
              
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default ServiceCards;
