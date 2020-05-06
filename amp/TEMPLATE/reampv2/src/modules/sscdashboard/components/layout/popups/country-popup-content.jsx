import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import './popups.css';

class CountryPopupContent extends Component {
    render() {
        return (
            <div>
              <div className="header">
                <div className="col-md-6 country-name">Country Name</div>
                <div className="col-md-6 projects">
                <span className="count">15</span>
                <span className="label">Projects</span>
                </div>
              </div>
              <div className="sector-list">
                sectors
              </div>
              <div className="description">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam.
              </div>
            </div>
        );
    }
}

export default CountryPopupContent;
