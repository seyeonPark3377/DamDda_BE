import React, { useState, useEffect } from "react";
import ProjectCard from "./ProjectCard"; // ProjectCard 컴포넌트
import "./css/SupportedProjects.css"; // CSS 파일 경로 수정
import axios from "axios";

export default function SupportedProjects() {
  const [projects, setProjects] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const[order,setOrder]=useState(true);
  const memberId=1;


  // 데이터 가져오기
  const fetchOrders = async () => {
    try {
      const response = await axios.get(`http://localhost:9000/order/supportingprojects?userId=${memberId}`);
      setProjects(response.data);
      setLoading(false);
    } catch (err) {
      setError(err.message);
      setLoading(false);
    }
  };

  //  useEffect(() => {
  //   fetchOrders();
  // }, [userId]);

  useEffect(() => {
    fetchOrders();
  }, 2);

  

  
  // 백엔드에서 후원한 프로젝트 목록을 가져오는 함수 (주석 처리)
  /*
  const fetchSupportedProjects = async () => {
    try {
      const response = await axios.get(`/orders/list/${memberId}`, {
        params: {
          page: 1,
          size: 2
        }
      });
      setProjects(response.data); // 받아온 데이터를 상태로 설정
      setIsLoading(false); // 로딩 완료
    } catch (error) {
      console.error('후원한 프로젝트 데이터를 불러오는 중 오류 발생:', error);
      setIsLoading(false); // 오류 발생 시 로딩 종료
    }
  };
  */

  // if (isLoading) {
  //   return <p>로딩 중...</p>;
  // }
  // if (error) {
  //   return <p>에러 발생: {error}</p>;
  // }
  return (
    <div className="supported-projects-container">
      <div className="projects-list">
        {projects.map((project, index) => (
          <ProjectCard key={index} project={project} />
        ))}
      </div>
    </div>
  );
}
