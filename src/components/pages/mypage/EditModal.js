import React, { useState } from 'react'; 
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField, Typography } from '@mui/material';

const EditModal = ({ open, onClose, onSubmit, currentPassword }) => {
  const [password, setPassword] = useState('');
  const [error, setError] = useState(''); // 에러 메시지 상태 추가

  const handleSubmit = () => {
    // currentPassword와 입력된 비밀번호 비교
    if (password === currentPassword) {  // 올바른 비밀번호는 부모 컴포넌트에서 전달된 currentPassword로 비교
      onSubmit(password);  // 비밀번호가 맞으면 onSubmit 호출
      setError('');  // 에러 메시지 초기화
      onClose();  // 모달 닫기
    } else {
      setError('비밀번호가 틀렸습니다. 다시 입력해주세요.');  // 에러 메시지 설정
    }
  };

  return (
    <Dialog open={open} onClose={onClose} aria-labelledby="form-dialog-title">
      <DialogTitle id="form-dialog-title">
        <Typography variant="h6" align="center">
          🔒 접근 암호 인증
        </Typography>
      </DialogTitle>
      <DialogContent>
        <Typography align="center">
          암호 입력 후 프로필 수정 페이지로 이동할 수 있습니다.
        </Typography>
        <TextField
          autoFocus
          margin="dense"
          id="password"
          label="Password"
          type="password"
          fullWidth
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        {/* 에러 메시지 표시 */}
        {error && (
          <Typography color="error" align="center" style={{ marginTop: '10px' }}>
            {error}
          </Typography>
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="secondary">
          취소
        </Button>
        <Button onClick={handleSubmit} color="primary">
          확인
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default EditModal;
