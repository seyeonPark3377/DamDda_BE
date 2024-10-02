/*
We're constantly improving the code you see. 
Please share your feedback here: https://form.asana.com/?k=uvp-HPgd3_hyoXRBw1IcNg&d=1152665201300829
*/

import PropTypes from "prop-types";
import React from "react";
import "./style.css";

export const ProgressBar = ({ value, className, trackClassName, filledClassName }) => {
  return (
    <div className={`progress-bar ${className}`}>
      <div className={`track ${trackClassName}`}>
        <div className={`filled ${value} ${filledClassName}`} />
      </div>
    </div>
  );
};

ProgressBar.propTypes = {
  value: PropTypes.oneOf([
    "sixty",
    "zero",
    "thirty",
    "eighty",
    "twenty",
    "one-hundred",
    "fifty",
    "ten",
    "forty",
    "ninety",
    "seventy",
  ]),
};
