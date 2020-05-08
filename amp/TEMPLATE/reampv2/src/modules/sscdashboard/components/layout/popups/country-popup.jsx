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
            <div className="country-popup-wrapper">
                <div className="container-fluid">

                  <div class="col-md-9 country-popup">
                    <CountryPopupHeader/>
                    <CountryPopupChart/>
                    <CountryPopupFooter/>
                  </div>

                </div>
            </div>
        );
    }
}

export default CountryPopup;
