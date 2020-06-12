import React, { Component } from "react";
import './popups.css';
import CountryPopup from "./country-popup";
import CountryPopupOverlayTitle from "./popup-overlay-title";
import CountryPopupExport from "./popup-export";

class CountryPopupOverlay extends Component {
    render() {
        if (!this.props.show) {
            return null;
        }
        return (
            <div className="country-popup-wrapper">
                <div className="container-fluid">
                    {this.props.children}
                    {/* TODO will be refactored in next story <CountryPopupOverlayTitle/>
                <CountryPopupExport/>
                  <div class="col-md-9 country-popup">
                    <CountryPopup/>
                  </div>
*/}
                </div>
            </div>
        );
    }
}

export default CountryPopupOverlay;
