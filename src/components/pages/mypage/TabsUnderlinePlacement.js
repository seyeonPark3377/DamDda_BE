import React from "react";
import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import Box from "@mui/material/Box";

export default function TabsUnderlinePlacement({
  selectedTab,
  setSelectedTab,
}) {
  const handleChange = (event, newValue) => {
    setSelectedTab(newValue);
  };

  return (
    <Box sx={{ width: "150%", display: "flex", justifyContent: "center" }}>
      <Box sx={{ borderBottom: "none", paddingRight: "-100px", marginRight: "800px"}}>
        <Tabs
          value={selectedTab}
          onChange={handleChange}
          aria-label="Tabs with underline"
          textColor="primary"
          TabIndicatorProps={{
            style: {
              backgroundColor: "blue", // 선택된 탭에 파란색 밑줄
            },
          }}
        >
          <Tab label="Tab 1. 프로필" sx={{ minWidth: "150px", fontSize: "14px" }} />
          <Tab label="Tab 2. 후원한 프로젝트" sx={{ minWidth: "150px", fontSize: "14px" }} />
          <Tab label="Tab 3. 나의 프로젝트" sx={{ minWidth: "150px", fontSize: "14px" }} />
          <Tab label="Tab 4. 관심 프로젝트" sx={{ minWidth: "150px", fontSize: "14px" }} />
          <Tab label="Tab 5. 협업하기" sx={{ minWidth: "150px", fontSize: "14px" }} />
          <Tab label="Tab 6. 탈퇴하기" sx={{ minWidth: "150px", fontSize: "14px" }} />

        </Tabs>
      </Box>
      </Box>
      )}