import React, { Component } from "react";
import './popups.css';

class CountryPopupHeader extends Component {
    render() {
        return (
            <div className="header">
                <div className="col-md-4 country-name">
                    <img
                        src={require('../../../images/flags/brazil.svg')}
                        alt="Brazil"/>
                    Bresil
                </div>
                <div className="col-md-2 projects">
                    <span className="count">15</span>
                    <span className="label">Projects</span>
                </div>
                <div className="col-md-2 projects">
                    <span className="count">06</span>
                    <span className="label">Secteur Type</span>
                </div>
            </div>
        );
    }
}

export default CountryPopupHeader;
