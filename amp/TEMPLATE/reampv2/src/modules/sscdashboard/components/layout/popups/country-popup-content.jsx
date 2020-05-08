import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import './popups.css';
import CountryPopupContent from "./country-popup-content";

class CountryPopupContent extends Component {
    render() {
        return (
            <div>
              <div className="description">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam.
              </div>
            </div>
        );
    }
}

export default CountryPopupContent;
