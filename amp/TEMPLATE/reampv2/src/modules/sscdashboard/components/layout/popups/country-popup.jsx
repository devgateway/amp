import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import './popups.css';
import CountryPopupContent from "./country-popup-content";

class CountryPopup extends Component {
    render() {
        return (
            <div className="country-popup-wrapper">
                <div className="container-fluid">

                  <div class="col-md-4 country-popup">
                    <CountryPopupContent/>
                  </div>
                  <div class="col-md-4 country-popup">
                    <CountryPopupContent/>
                  </div>
                  <div class="col-md-4 country-popup">
                    <CountryPopupContent/>
                  </div>

                </div>
            </div>
        );
    }
}

export default CountryPopup;
