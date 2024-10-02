import React, { useEffect, useRef, useState } from "react";

const Game = () => {
  const canvasRef = useRef(null);
  const [intervalId, setIntervalId] = useState(null);
  const [gameOver, setGameOver] = useState(false);

  const ballRadius = 10;
  const [x, setX] = useState(250);
  const [y, setY] = useState(270);
  const [dx, setDx] = useState(2);
  const [dy, setDy] = useState(-2);

  const paddleHeight = 10;
  const paddleWidth = 75;
  const [paddleX, setPaddleX] = useState((300 - paddleWidth) / 2);
  const [rightPressed, setRightPressed] = useState(false);
  const [leftPressed, setLeftPressed] = useState(false);

  const brickRowCount = 3;
  const brickColumnCount = 5;
  const brickWidth = 75;
  const brickHeight = 20;
  const brickPadding = 10;
  const brickOffsetTop = 30;
  const brickOffsetLeft = 30;

  const [bricks, setBricks] = useState(
    Array.from({ length: brickColumnCount }, () =>
      Array.from({ length: brickRowCount }, () => ({ isVisible: true }))
    )
  );

  useEffect(() => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext("2d");

    const drawBall = () => {
      ctx.beginPath();
      ctx.arc(x, y, ballRadius, 0, Math.PI * 2);
      ctx.fillStyle = "#0095DD";
      ctx.fill();
      ctx.closePath();
    };

    const drawPaddle = () => {
      ctx.beginPath();
      ctx.rect(
        paddleX,
        canvas.height - paddleHeight,
        paddleWidth,
        paddleHeight
      );
      ctx.fillStyle = "#0095DD";
      ctx.fill();
      ctx.closePath();
    };

    const drawBricks = () => {
      for (let c = 0; c < brickColumnCount; c++) {
        for (let r = 0; r < brickRowCount; r++) {
          if (bricks[c][r].isVisible) {
            let brickX = c * (brickWidth + brickPadding) + brickOffsetLeft;
            let brickY = r * (brickHeight + brickPadding) + brickOffsetTop;
            ctx.beginPath();
            ctx.rect(brickX, brickY, brickWidth, brickHeight);
            ctx.fillStyle = "#0095DD";
            ctx.fill();
            ctx.closePath();
          }
        }
      }
    };

    const draw = () => {
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      drawBricks();
      drawBall();
      drawPaddle();

      // 벽 충돌
      if (x + dx > canvas.width - ballRadius || x + dx < ballRadius) {
        setDx(-dx);
      }
      if (y + dy < ballRadius) {
        setDy(-dy);
      } else if (y + dy > canvas.height - ballRadius) {
        if (x > paddleX && x < paddleX + paddleWidth) {
          setDy(-dy);
        } else {
          setGameOver(true);
          clearInterval(intervalId);
          alert("GAME OVER");
          return; // 게임 오버 후 더 이상 그리지 않음
        }
      }

      // 패들 이동
      if (rightPressed && paddleX < canvas.width - paddleWidth) {
        setPaddleX(paddleX + 7);
      } else if (leftPressed && paddleX > 0) {
        setPaddleX(paddleX - 7);
      }

      setX(x + dx);
      setY(y + dy);
    };

    const handleKeyDown = (e) => {
      if (e.key === "Right" || e.key === "ArrowRight") {
        setRightPressed(true);
      } else if (e.key === "Left" || e.key === "ArrowLeft") {
        setLeftPressed(true);
      }
    };

    const handleKeyUp = (e) => {
      if (e.key === "Right" || e.key === "ArrowRight") {
        setRightPressed(false);
      } else if (e.key === "Left" || e.key === "ArrowLeft") {
        setLeftPressed(false);
      }
    };

    document.addEventListener("keydown", handleKeyDown);
    document.addEventListener("keyup", handleKeyUp);

    // 게임이 시작된 경우에만 draw 호출
    if (intervalId) {
      const newIntervalId = setInterval(draw, 10);
      return () => clearInterval(newIntervalId);
    }
  }, [x, y, paddleX, rightPressed, leftPressed, dx, dy, bricks, intervalId]);

  const startGame = () => {
    setGameOver(false);
    setX(250);
    setY(270);
    setDx(2);
    setDy(-2);
    setPaddleX((300 - paddleWidth) / 2);
    setBricks(
      Array.from({ length: brickColumnCount }, () =>
        Array.from({ length: brickRowCount }, () => ({ isVisible: true }))
      )
    );

    // 게임 시작 시 interval 설정
    if (!intervalId) {
      const newIntervalId = setInterval(() => {}, 10); // 기존 interval 유지
      setIntervalId(newIntervalId);
    }
  };

  return (
    <div>
      <div>
        <canvas
          ref={canvasRef}
          width={300}
          height={300}
          style={{ border: "1px solid #0095DD" }}
        />
      </div>
      <div>
        <button onClick={startGame} disabled={!gameOver && !!intervalId}>
          게임 시작
        </button>
        {gameOver && <button onClick={startGame}>재시작</button>}
      </div>
    </div>
  );
};

export default Game;
