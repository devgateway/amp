import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import './popups.css';
import CountryPopupHeader from "./country-popup-header";
import CountryPopupChart from "./country-popup-chart";
import CountryPopupFooter from "./country-popup-footer";

class CountryPopup extends Component {
    render() {
        return (
          <div class="country-popup">
            <CountryPopupHeader/>
            <CountryPopupChart/>
            <CountryPopupFooter/>
          </div>
        );
    }
}

export default CountryPopup;
