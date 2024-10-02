import React, { useState, useEffect } from "react";
import axios from "axios";
import "./package.css";
import { useLocation } from "react-router-dom";
const Package = () => {
  const [reward_name, setReward_name] = useState("");
  const [optionType, setOptionType] = useState("none");
  const [OptionInput, setOptionInput] = useState("");
  const [Options, setOptions] = useState([]);
  const [project_package, setProject_package] = useState([]);
  const [package_name, setPackage_name] = useState("");
  const [selected_reward, setSelected_reward] = useState([]);
  const [package_limit, setPackage_limit] = useState(1); // 제한 수량 기본값 0
  const [isLimitEnabled, setIsLimitEnabled] = useState(false); // 제한 수량 있음 여부
  const [package_price, setPackage_price] = useState("");
  const [reward_list, setReward_list] = useState([]);
  const [isEditing, setIsEditing] = useState(false);
  const [editingIndex, setEditingIndex] = useState(null);
  const [Snackbar, setSnackbar] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [currentPackageId, setCurrentPackageId] = useState(null);

  const location = useLocation();
  const query = new URLSearchParams(location.search);
  const [projectId, setProjectId] = useState(query.get("projectId") || 1);

  useEffect(() => {
    fetchGifts();
    fetchPackage();
    getProjectId();
  }, []);

  const getProjectId = async () => {
    try {
      const response = await axios.get();
    } catch (error) {
      console.log(error);
    }
  };
  const closeSnackbar = () => {
    setTimeout(() => {
      setSnackbar(false);
      setSnackbarMessage("");
    }, 3000);
  };

  //선물 가져오기 기능
  const fetchGifts = async () => {
    try {
      const response = await axios.get(
        `/packages/rewards/project/${projectId}`, //project_id를 넘겨받아야 함.
        { withCredentials: true }
      );
      console.log(response.data)

      const formattedGifts = response.data.map((gift) => ({
        id: gift.id,
        name: gift.name,
        count: gift.count,
        OptionList: gift.OptionList,
        optionType: gift.optionType,
      }));

      setReward_list(formattedGifts);
    } catch (error) {
      console.error("선물 목록을 가져오는 중 오류 발생:", error);
    }
  };

  //패키지 가져오는 기능.
  const fetchPackage = async () => {
    try {
      const response = await axios.get(`/packages/project/${projectId}`, {
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

      setProject_package(formattedPackages);
    } catch (error) {
      console.error("패키지 목록을 가져오는 중 오류 발생:", error);
    }
  };

  //선물 추가 기능
  const handleGiftAdd = async () => {
    if (!reward_name || (optionType === "select" && Options.length === 0)) {
      alert("선물 이름과 옵션을 모두 입력해주세요.");
      return;
    }

    const newGift = {
      name: reward_name,
      count: 1, // 기본값 설정
      OptionList: Options,
      optionType: optionType,
    };
    setReward_list([...reward_list, newGift]);

    try {
      const response = await axios.post(
        `/packages/rewards/register/${projectId}`,
        newGift,
        {
          withCredentials: true,
        }
      );

      setSnackbarMessage("선물이 추가되었습니다.");
      setSnackbar(true);

      // 서버로부터 새로운 선물 목록을 가져옵니다.
      await fetchGifts();

      closeSnackbar();

      // 입력 필드 초기화
      setReward_name("");
      setOptions([]);
      setOptionType("none");
    } catch (error) {
      console.error("Error details:", error);
    }
  };
  //선물 삭제 기능
  const handleGiftDelete = async (giftId, index) => {
    if (window.confirm("정말로 삭제하시겠습니까?")) {
      try {
        await axios.delete(`/packages/rewards/delete/${giftId}`, {
          withCredentials: true,
        });
        const updatedGifts = [...reward_list];
        updatedGifts.splice(index, 1);
        setReward_list(updatedGifts);
        setSnackbarMessage("선물이 삭제되었습니다.");
        setSnackbar(true);
        closeSnackbar();
        fetchPackage();
      } catch (error) {
        console.log("삭제 중 에러 " + error);
      }
    }
  };
  //선택된 선물 삭제 기능
  const handleSelectedGiftDelete = (index) => {
    console.log(selected_reward.map((reward) => reward.id));
    setSelected_reward(selected_reward.filter((reward) => reward.id !== index));
  };
  //옵션 추가 기능
  const handleOptionAdd = () => {
    if (OptionInput) {
      setOptions((prevOptions) => {
        const currentOptions = Array.isArray(prevOptions) ? prevOptions : [];
        return [...currentOptions, OptionInput];
      });
      setOptionInput("");
    }
  };

  const handleSelectReward = (rewardName) => {
    setSelected_reward((prev) => {
      if (!Array.isArray(prev)) {
        // prev가 배열이 아니면 새로운 배열을 시작합니다.
        prev = [];
      }
      const existingReward = prev.find((reward) => reward.name === rewardName);
      if (existingReward) {
        return prev;
      }

      const selectedReward = reward_list.find(
        (reward) => reward.name === rewardName
      );
      return [
        ...prev,
        {
          id: selectedReward.id,
          name: rewardName,
          count: 1,
          optionType: selectedReward.optionType,
          OptionList: selectedReward.OptionList,
        },
      ];
    });
  };

  const handleCount = (rewardName, increment) => {
    setSelected_reward((prev) => {
      return prev.map((reward) =>
        reward.name === rewardName
          ? { ...reward, count: Math.max(1, reward.count + increment) } // Ensure count doesn’t go below 1
          : reward
      );
    });
  };

  const handleOptionDelete = (index) => {
    const updatedOptions = [...Options];
    updatedOptions.splice(index, 1);
    setOptions(updatedOptions);
  };

  const handleConfigAdd = async () => {
    if (!package_name || !selected_reward || !package_price) {
      alert("구성 이름, 선물, 가격을 모두 입력해주세요.");
      return;
    }
    if (!selected_reward || selected_reward.length === 0) {
      alert("최소한 하나의 선물을 선택해주세요.");
      return;
    }
    const newConfig = {
      id: currentPackageId,
      name: package_name,
      RewardList: selected_reward.map((reward) => ({
        id: reward.id,
        name: reward.name,
        count: reward.count,
        OptionList: reward.OptionList,
        optionType: reward.optionType,
      })),
      quantityLimited: isLimitEnabled ? package_limit : 0, // 제한 수량이 없으면 '무제한'
      price: parseInt(package_price.replace(/,/g, "")),
    };

    setProject_package([...project_package, newConfig]);

    if (isEditing) {
      try {
        await axios.put(`/packages/modify?projectId=${projectId}`, newConfig, {
          withCredentials: true,
        });

        const updatedPackages = [...project_package];
        updatedPackages[editingIndex] = newConfig;
        setProject_package(updatedPackages);
        setSnackbarMessage("구성이 수정되었습니다.");
        setIsEditing(false);
        setEditingIndex(null);
      } catch (error) {
        console.error("구성 수정 중 오류 발생:", error);
        setSnackbarMessage("구성 수정 중 오류가 발생했습니다.");
      }
    } else {
      try {
        await axios.post(`/packages/register/${projectId}`, newConfig, {
          //projectId 받아와야 함
          withCredentials: true,
          headers: {
            "Content-Type": "application/json",
          },
        });

        await fetchPackage();

        setSnackbarMessage("구성이 추가되었습니다.");
      } catch (error) {
        console.error("Error details:", error);
      }
    }
    setSnackbar(true);
    closeSnackbar();

    // 초기화
    resetForm();
  };

  const handleCountChange = (increment) => {
    setPackage_limit((prev) => (prev + increment < 1 ? 1 : prev + increment));
  };
  //패키지 수정 기능
  const handleEdit = async (index) => {
    //fetchPackage에서 들고옴.
    const selectedPackage = project_package[index];
    setPackage_name(selectedPackage.name);
    setCurrentPackageId(selectedPackage.id);
    setSelected_reward(
      selectedPackage.RewardList.map((reward) => ({
        id: reward.id,
        name: reward.name,
        count: reward.count,
        optionType: reward.optionType,
        OptionList: reward.OptionList,
      }))
    );
    setPackage_limit(selectedPackage.quantityLimited);
    setIsLimitEnabled(selectedPackage.quantityLimited !== -1);
    setPackage_price(selectedPackage.price.toLocaleString());

    setIsEditing(true);
    setEditingIndex(index);
  };
  //패키지 삭제 기능
  const handleConfigDelete = async (packageId, index) => {
    if (window.confirm("정말로 삭제하시겠습니까?")) {
      try {
        await axios.delete(`/packages/delete/${packageId}`, {
          withCredentials: true,
        });
        const updatedPackages = [...project_package];
        updatedPackages.splice(index, 1);
        setProject_package(updatedPackages);
        setSnackbarMessage("구성이 삭제되었습니다.");
        setSnackbar(true);
        closeSnackbar();
      } catch (error) {
        console.log(error);
      }
    }
  };

  const resetForm = () => {
    setPackage_name("");
    setSelected_reward([]);
    setPackage_limit(0);
    setIsLimitEnabled(false);
    setPackage_price("");
    setIsEditing(false);
    setEditingIndex(null);
  };
  const formatQunatity = (quantity) => {
    return quantity == 0 ? "무제한" : `${quantity}개 남음`;
  };

  return (
    <div className="package-page">
      <div className="package-section1">
        <h2>선물 옵션</h2>
        <div>
          <label>선물 이름: </label>
          <input
            type="text"
            value={reward_name}
            onChange={(e) => setReward_name(e.target.value)}
          />
        </div>

        <div>
          <label>옵션 조건: </label>
          <button onClick={() => setOptionType("none")}>없음</button>
          <button onClick={() => setOptionType("select")}>선택식</button>
        </div>

        {optionType === "select" && (
          <div>
            <input
              type="text"
              value={OptionInput}
              onChange={(e) => setOptionInput(e.target.value)}
              placeholder="옵션을 입력해주세요"
            />
            <button onClick={handleOptionAdd}>추가</button>
          </div>
        )}

        <div className="options-list">
          {Options != undefined &&
            Options.map((option, index) => (
              <div key={index}>
                {option}{" "}
                <button onClick={() => handleOptionDelete(index)}>삭제</button>
              </div>
            ))}
        </div>

        <button onClick={handleGiftAdd}>선물 추가</button>

        <div className="gift-list">
          {reward_list.length > 0 &&
            reward_list.map((gift, index) => (
              <div key={gift.id}>
                {gift.name}
                {gift.OptionList && gift.OptionList.length > 0 && (
                  <span> ({gift.OptionList.join(", ")})</span>
                )}
                <button onClick={() => handleGiftDelete(gift.id, index)}>
                  삭제
                </button>
              </div>
            ))}
        </div>

        <h2>선물 구성</h2>
        <div>
          <label>구성 이름: </label>
          <input
            type="text"
            value={package_name}
            onChange={(e) => setPackage_name(e.target.value)}
          />
        </div>

        <label>선물 선택: </label>
        <select value="" onChange={(e) => handleSelectReward(e.target.value)}>
          <option value="">선물 선택</option>
          {reward_list.map((gift, index) => (
            <option key={index} value={gift.name}>
              {gift.name}
            </option>
          ))}
        </select>

        <div>
          {selected_reward != undefined &&
            selected_reward.map((reward, index) => (
              <div key={index}>
                <span>선택된 선물: {reward.name}</span>
                <button
                  onClick={() => handleCount(reward.name, -1)}
                  disabled={reward.count <= 1}
                >
                  -
                </button>
                <span>{reward.count}</span>
                <button onClick={() => handleCount(reward.name, 1)}>+</button>
                <button onClick={() => handleSelectedGiftDelete(reward.id)}>
                  삭제
                </button>
              </div>
            ))}
        </div>

        <div>
          <label>제한 수량: </label>
          <button onClick={() => setIsLimitEnabled(false)}>없음</button>
          <button onClick={() => setIsLimitEnabled(true)}>있음</button>
          {isLimitEnabled && (
            <div>
              <button
                onClick={() => handleCountChange(-1)}
                disabled={package_limit <= 0}
              >
                -
              </button>
              <span>{package_limit}</span>
              <button onClick={() => handleCountChange(1)}>+</button>
            </div>
          )}
        </div>

        <div>
          <label>가격: </label>
          <input
            type="text"
            value={package_price}
            onChange={(e) =>
              setPackage_price(
                e.target.value
                  .replace(/[^\d]/g, "") //이전 상태에 쉼표가 포함됨 -> 쉼표 제거
                  .replace(/\B(?=(\d{3})+(?!\d))/g, ",")
              )
            }
          />
          <span> 원</span>
        </div>

        <button onClick={handleConfigAdd}>
          {isEditing ? "구성 수정" : "구성 추가"}
        </button>

        {Snackbar && <div className="snackbar">{snackbarMessage}</div>}
      </div>

      <div className="package-section2">
        <h3>내가 만든 선물구성</h3>
        {project_package.length > 0 ? (
          project_package.map((config, index) => (
            <div key={index} className="package-card">
              <p>{formatQunatity(config.quantityLimited)}</p>
              <h3>{config.price.toLocaleString()}원</h3>
              {config.name}
              <ul>
                {config.RewardList.map((reward, rewardIndex) => (
                  <div key={rewardIndex}>
                    <h3>{reward.name}</h3>
                    <p>{reward.count}개</p>
                    <ul>
                      {reward.OptionList.map((opt, optIndex) => (
                        <li key={optIndex}>{opt}</li>
                      ))}
                    </ul>
                  </div>
                ))}
              </ul>

              <button onClick={() => handleEdit(index)}>수정</button>
              <button onClick={() => handleConfigDelete(config.id, index)}>
                삭제
              </button>
            </div>
          ))
        ) : (
          <p>추가된 선물 구성이 없습니다.</p>
        )}
      </div>
    </div>
  );
};

export default Package;