import {React, useState} from 'react';
import { TextField, InputAdornment, Box,Button } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';

export const SearchBar = ({search, setSearch}) => {
  const [searchText, setSearchText] = useState(search || '');
  const handleSearchChange = (event) => {
    setSearchText(event.target.value); // 상태를 업데이트
  };

  const handleKeyDown = (event) => {
    if (event.key === 'Enter' ) { // && searchText.trim() !== '') {
      enterSearch(searchText); // Enter 키가 눌리면 검색 실행
    }
  };

  const enterSearch = () => {
    // if(searchText.trim() === ''){
    //   setSearch(null);
    //   console.log('검색어: null', searchText); // 여기서 검색 API 호출 등을 실행할 수 있음
    // } else {
      setSearch(searchText); // searchText를 검색 상태로 설정
      console.log('검색어:', searchText); // 여기서 검색 API 호출 등을 실행할 수 있음
    // }   
  };

  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        margin: '50px auto',
        width: "100%",
        maxWidth: 500,
        border: null,
      }}
    >
      <TextField
        placeholder="새로운 일상이 필요하신가요?" // Placeholder text from the image
        value={searchText}
        onChange={handleSearchChange}
        onKeyDown={handleKeyDown} // 엔터 키 입력 시 검색 실행
        // fullWidth
        InputProps={{
          endAdornment: (
              <InputAdornment position="end" sx={{ mt: '-8px', mr:'8px' }}>
                <Button onClick={enterSearch} sx={{ minWidth: 0, padding: 0 }}>
                  <SearchIcon />
                </Button>
              </InputAdornment>

          ),
          style: {
            borderRadius: '50px', // Full rounded border
            padding: '0 12px', // Adjust padding to center content
            height: '100%', // Ensure the height matches the container
            display: 'flex',
            width: '100%',
            justifyContent: 'space-between',
            alignItems: 'center', // Vertically center the content
            //marginTop: '5px',
          },
        }}
        sx={{
          width: "100%",
          border: '2px solid #7a82ed', // Teal border color
          borderRadius: '50px', // Full rounded corners
          backgroundColor: '#fff', // White background
          height: '56px', // Height to match the input field in the image
          fontSize: '1rem', // Increase font size for placeholder
          '& .MuiInputBase-input::placeholder': {
            color: '#ffffff', // Gray placeholder color
          },
          '& .MuiOutlinedInput-root': {
            '& fieldset': {
              borderColor: 'transparent', // Hide default border
            },
            '&:hover fieldset': {
              borderColor: 'transparent', // Hide default border
            },
            '&.Mui-focused fieldset': {
              borderColor: 'transparent', // Hide border when focused
            },
          },
        }}
      />
    </Box>
  );
};
