import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import './popups.css';

class CountryPopupOverlayTitle extends Component {
    render() {
        return (
          <div>
            <h2>Apercu de Secteur</h2>
          </div>
        );
    }
}

export default CountryPopupOverlayTitle;
