import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import './popups.css';
import CountryPopup from "./country-popup";
import CountryPopupOverlayTitle from "./popup-overlay-title";

class CountryPopupOverlay extends Component {
    render() {
        return (
            <div className="country-popup-wrapper">
                <div className="container-fluid">
                <CountryPopupOverlayTitle/>

                  <div class="col-md-9 country-popup">
                    <CountryPopup/>
                  </div>

                </div>
            </div>
        );
    }
}

export default CountryPopupOverlay;
