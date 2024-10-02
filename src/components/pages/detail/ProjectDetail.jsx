import React, { useEffect, useState, useRef } from "react";
import {
  Typography,
  Card,
  CardContent,
  Button,
  IconButton,
  Divider,
  Select,
  InputLabel,
  MenuItem,
  Checkbox,
  FormControl,
} from "@mui/material";
import { styled } from "@mui/system";
import DeleteIcon from "@mui/icons-material/Delete";
import AddIcon from "@mui/icons-material/Add";
import RemoveIcon from "@mui/icons-material/Remove";
import axios from "axios";

const Container = styled("div")({
  padding: "20px",
  backgroundColor: "#f0f0f0",
  display: "flex",
});

// 상세 설명 섹션 스타일
const DetailSection = styled("div")({
  display: "flex",
  flexDirection: "column",
  marginBottom: "20px",
  flex: 2,
  maxHeight: "800px", // 최대 높이 800px
  overflow: "hidden", // 넘치는 부분 숨김
  overflowX: "auto",
});

// 선물 구성 섹션
const PackageSection = styled("div")({
  display: "flex",
  flexDirection: "column",
  marginLeft: "20px",

  width: "200px",
});

// 카드(선물구성)
const PackageCard = styled(Card)({
  backgroundColor: "#f9f9f9",
  marginBottom: "10px",
  width: "200px",
  cursor: "pointer",
});

const SelectedCard = styled(Card)({
  marginBottom: "10px",
  padding: "10px",
  backgroundColor: "#ffffff",
  display: "flex",
  alignItems: "center",
  justifyContent: "space-between",
  borderRadius: "8px",
});

const CartSection = styled("div")({
  padding: "10px",
  backgroundColor: "#fff",
  borderRadius: "8px",
  marginBottom: "20px",
});

const ImageContainer = styled("img")({
  width: "100%",
  height: "auto",
});

const CountButton = styled(IconButton)({
  padding: "2px", // 버튼 크기 축소
  margin: "0 5px", // 숫자 사이 여백 설정
});

const ProjectDetail = ({descriptionDetail, descriptionImages}) => {
  const [rewardOption, setRewardOption] = useState([]);
  const [selectedPackages, setSelectedPackages] = useState([]);
  const [totalAmount, setTotalAmount] = useState(0);
  const [selectedPackage, setSelectedPackage] = useState(null); // 선택한 카드
  const [selectedOptions, setSelectedOptions] = useState({}); // 각 카드의 옵션 선택
  const [detailedDescription, setDetailedDescription] = useState([]);
  const [showMore, setShowMore] = useState(false); // 더보기 상태
  const detailRef = useRef(null);
  const [project_package, setProject_package] = useState([]);
  const [projectId, setProjectId] = useState(1);
  useEffect(() => {
    const fetchData = async () => {
      // Mock 데이터
      fetchPackage();
      const packageData = [
        {
          id: 1,
          name: "선물 1",
          description: "설명 1",
          price: 5000,
          options: ["옵션 1", "옵션 2"],
          stock: 10,
        },
        {
          id: 2,
          name: "선물 2",
          description: "설명 2",
          price: 59000,
          stock: 100,
        },
      ];

      const details = [
        {
          id: 1,
          text: "상세 설명 내용입니다. 이 내용은 800px 이하로 잘려서 보여집니다.",
          image: "https://via.placeholder.com/800",
        },
        {
          id: 2,
          text: "추가 상세 설명 내용이 여기 들어갑니다. 이 내용은 더보기 버튼을 클릭해야 보여집니다. 추가 상세 설명 내용이 여기 들어갑니다. 이 내용은 더보기 버튼을 클릭해야 보여집니다.",
          image: "https://via.placeholder.com/800",
        },
      ];

      setRewardOption(packageData);
      setDetailedDescription(details);
    };

    fetchData();
  }, []);

  useEffect(() => {
    if (detailRef.current) {
      setShowMore(detailRef.current.scrollHeight > 800);
    }
  }, [detailedDescription]);

  const handleCardClick = (pkg) => {
    setSelectedPackage(pkg);
  };

  //패키지 가져오는 기능.
  const fetchPackage = async () => {
    try {
      const response = await axios.get(`/packages/project/${projectId}`, {
        //project id 받아줘야 함.
        //project_id를 넘겨받아야 함.
        withCredentials: true,
      });

      if (!Array.isArray(response.data)) {
        console.error("API response is not an array:", response.data);
        return;
      }
      const formattedPackages = response.data.map((pac) => ({
        id: pac.id,
        name: pac.name,
        count: pac.count,
        price: pac.price,
        quantityLimited: pac.quantityLimited,
        RewardList: Array.isArray(pac.RewardList) ? pac.RewardList : [],
      }));
      console.log(formattedPackages.map((reward) => reward.RewardList));
      setProject_package(formattedPackages);
    } catch (error) {
      console.error("패키지 목록을 가져오는 중 오류 발생:", error);
    }
  };
  const handleSelectPackage = () => {
    if (selectedPackage) {
      const selectedOptionsList = Object.entries(selectedOptions) //"패키지ID-리워드인덱스": "선택된 옵션 값" 형태
        .filter(([key]) => key.startsWith(`${selectedPackage.id}-`))
        .map(([_, value]) => value);

      const exists = selectedPackages.some(
        (p) =>
          p.id === selectedPackage.id &&
          JSON.stringify(p.selectedOption) ===
            JSON.stringify(selectedOptionsList)
      );

      if (!exists) {
        setSelectedPackages([
          ...selectedPackages,
          {
            ...selectedPackage,
            count: 1,
            selectedOption: selectedOptionsList,
          },
        ]);
        setSelectedPackage(null);
        // 선택된 패키지의 옵션들만 초기화
        setSelectedOptions((prevOptions) => {
          const newOptions = { ...prevOptions };
          Object.keys(newOptions).forEach((key) => {
            if (key.startsWith(`${selectedPackage.id}-`)) {
              delete newOptions[key];
            }
          });
          return newOptions;
        });
      } else {
        alert("이미 선택된 선물과 옵션 조합입니다.");
      }
    }
  };
  const handleRemovePackage = (pkgId) => {
    setSelectedPackages(
      selectedPackages.filter(
        (pkg) => `${pkg.id}-${pkg.selectedOption}` !== pkgId
      )
    );
  };

  const handleChangeCount = (pkgId, delta) => {
    setSelectedPackages(
      selectedPackages.map((pkg) =>
        `${pkg.id}-${pkg.selectedOption}` === pkgId
          ? { ...pkg, count: Math.max(1, pkg.count + delta) }
          : pkg
      )
    );
  };

  const handleSelectOption = (pkgId, option) => {
    setSelectedOptions((prevOptions) => {
      const currentOptions = prevOptions[pkgId] || [];
      if (currentOptions.includes(option)) {
        return {
          ...prevOptions,
          [pkgId]: currentOptions.filter((opt) => opt !== option),
        };
      } else {
        return { ...prevOptions, [pkgId]: [...currentOptions, option] };
      }
    });
  };

  useEffect(() => {
    const total = selectedPackages.reduce(
      (sum, pkg) => sum + pkg.price * pkg.count,
      0
    );
    setTotalAmount(total);
  }, [selectedPackages]);

  return (
    <Container>
      {/* 상세 설명 섹션 */}
      <DetailSection ref={detailRef}>
        <Typography variant="h5" style={{ marginBottom: "10px" }}>
          상세 설명
        </Typography>

        
        <Typography variant="body1" style={{ marginTop: "10px" }}>
                  {descriptionDetail}
                </Typography>
   
{descriptionImages &&
              descriptionImages.length > 0 ? (
                descriptionImages.map((image, index) => (
                  <ImageContainer
                    key={index}
                    src={`http://localhost:9000/${image}`}
                    alt={`Product image ${index}`}
                  />
                ))
              ) : (
                <Typography variant="body2" color="textSecondary">
                  이미지가 없습니다.
                </Typography>
              )}




{/* 남희님 더보기 한번 보셔야 할 것 같아요
        {detailedDescription.map((detail, index) => (
          <div key={detail.id} style={{ marginBottom: "20px" }}>
            <Typography variant="body1" style={{ marginTop: "10px" }}>
              {detail.text}
            </Typography>
            <ImageContainer
              src={detail.image}
              alt={`Detail Image ${detail.id}`}
            />
          </div>
        ))} */}


        {/* 더보기 버튼 표시 조건 수정 */}
        {detailRef.current &&
          detailRef.current.scrollHeight > 800 &&
          !showMore && (
            <Button onClick={() => setShowMore(true)}>▽ 더보기</Button>
          )}
 {/* 상세 내용이 더보기 상태일 때 모든 내용 표시 */}
        {showMore && (
          <div>
 <Typography variant="body1" style={{ marginTop: "10px" }}>
                  {descriptionDetail}
                </Typography>

                {descriptionImages.map((image, index) => (
                  <ImageContainer
                    key={index}
                    src={`http://localhost:9000/${image}`}
                    alt={`Product image ${index}`}
                  />
                ))}
          </div>
        )}

               



                {/* <ImageContainer
                  src={detail.image}
                  alt={`Detail Image ${detail.id}`}
                /> */}


        {/* 상세 내용이 더보기 상태일 때 모든 내용 표시 */}
        {/* {showMore && (
          <div>
            {detailedDescription.map((detail, index) => (
              <div key={detail.id} style={{ marginBottom: "20px" }}>
                <Typography variant="body1" style={{ marginTop: "10px" }}>
                  {detail.text}
                </Typography>
                <ImageContainer
                  src={detail.image}
                  alt={`Detail Image ${detail.id}`}
                />
              </div>
            ))}
          </div>
        )} */}
      </DetailSection>

      <Divider orientation="vertical" flexItem />

      {/* 선물 구성 섹션 */}
      <PackageSection>
        <Typography variant="h5" style={{ marginBottom: "10px" }}>
          선물 구성 선택
        </Typography>
        {project_package.map((pkg) => (
          <PackageCard key={pkg.id} onClick={() => handleCardClick(pkg)}>
            <CardContent>
              <Typography variant="h6">{pkg.name}</Typography>
              <Typography variant="body1">
                {pkg.price.toLocaleString()}원
              </Typography>
            </CardContent>

            {/* 옵션 드롭다운 */}
            {selectedPackage?.id === pkg.id && (
              <div>
                {pkg.RewardList && pkg.RewardList.length > 0 ? (
                  pkg.RewardList.map((reward, rewardIndex) => (
                    <div key={rewardIndex}>
                      <Typography variant="subtitle1">{reward.name}</Typography>
                      {reward.OptionList && reward.OptionList.length > 0 ? (
                        <FormControl fullWidth>
                          <InputLabel
                            id={`multiple-select-label-${pkg.id}-${rewardIndex}`}
                          >
                            옵션 선택
                          </InputLabel>
                          <Select
                            labelId={`multiple-select-label-${pkg.id}-${rewardIndex}`}
                            id={`multiple-select-${pkg.id}-${rewardIndex}`}
                            value={
                              selectedOptions[`${pkg.id}-${rewardIndex}`] || ""
                            }
                            onChange={(event) => {
                              const value = event.target.value;
                              setSelectedOptions({
                                ...selectedOptions,
                                [`${pkg.id}-${rewardIndex}`]: value,
                              });
                            }}
                          >
                            {reward.OptionList.map((option) => (
                              <MenuItem
                                key={option}
                                value={option}
                                disabled={
                                  selectedOptions[
                                    `${pkg.id}-${rewardIndex}`
                                  ] === option
                                }
                              >
                                {option}
                              </MenuItem>
                            ))}
                          </Select>
                        </FormControl>
                      ) : (
                        <Typography variant="body2">
                          이 리워드에는 옵션이 없습니다.
                        </Typography>
                      )}
                    </div>
                  ))
                ) : (
                  <Typography variant="body2">리워드가 없습니다.</Typography>
                )}

                <Button
                  variant="contained"
                  onClick={handleSelectPackage}
                  disabled={
                    pkg.RewardList.some(
                      (reward, index) =>
                        reward.OptionList &&
                        reward.OptionList.length > 0 &&
                        !selectedOptions[`${pkg.id}-${index}`]
                    ) ||
                    selectedPackages.some(
                      (p) =>
                        p.id === pkg.id &&
                        JSON.stringify(p.selectedOption) ===
                          JSON.stringify(selectedOptions[pkg.id])
                    )
                  }
                  style={{ marginTop: "10px" }}
                >
                  이 선물 구성 선택하기
                </Button>
              </div>
            )}
          </PackageCard>
        ))}

        {/* 선택된 카드 목록 */}
        {selectedPackages.length > 0 && (
          <CartSection>
            <Typography variant="h6">선택한 선물</Typography>
            {selectedPackages.map((pkg) => (
              <SelectedCard key={`${pkg.id}-${pkg.selectedOption}`}>
                <div>
                  <Typography variant="h6">{pkg.name}</Typography>
                  <Typography variant="body2">
                    {pkg.price.toLocaleString()}원 × {pkg.count}개
                  </Typography>
                  {pkg.selectedOption && (
                    <Typography variant="body2">
                      옵션: {pkg.selectedOption}
                    </Typography>
                  )}
                </div>
                <div style={{ display: "flex", alignItems: "center" }}>
                  <CountButton
                    onClick={() =>
                      handleChangeCount(`${pkg.id}-${pkg.selectedOption}`, -1)
                    }
                    disabled={pkg.count === 1}
                    style={{ color: pkg.count === 1 ? "grey" : "black" }}
                  >
                    <RemoveIcon />
                  </CountButton>
                  <Typography>{pkg.count}</Typography>
                  <CountButton
                    onClick={() =>
                      handleChangeCount(`${pkg.id}-${pkg.selectedOption}`, 1)
                    }
                  >
                    <AddIcon />
                  </CountButton>
                  <IconButton
                    onClick={() => {
                      const confirmDelete = window.confirm(
                        "정말로 이 선물을 삭제하시겠습니까?"
                      );
                      if (confirmDelete) {
                        handleRemovePackage(`${pkg.id}-${pkg.selectedOption}`);
                      }
                    }}
                  >
                    <DeleteIcon />
                  </IconButton>
                </div>
              </SelectedCard>
            ))}

            <Button variant="contained" color="primary" fullWidth>
              총 {totalAmount.toLocaleString()}원 후원하기
            </Button>
          </CartSection>
        )}
      </PackageSection>
    </Container>
  );
};

export default ProjectDetail;
