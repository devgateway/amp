import React, { Component } from 'react';
import './popups.css';
import PropTypes from "prop-types";
import CountryPopupOverlay from "./CountryPopupOverlay";

const PopupOverlay = ({ additionalClass, show, children }) => {
  if (!show) {
    return null;
  }
  return (
    <div className={`country-popup-wrapper ${additionalClass}`}>
      <div className="container-fluid">
        {children}
      </div>
    </div>
  );
}
export default PopupOverlay;
PopupOverlay.propTypes = {
  show: PropTypes.bool.isRequired,
  additionalClass: PropTypes.string
}
PopupOverlay.defaultProps = {
  additionalClass: ''
}
