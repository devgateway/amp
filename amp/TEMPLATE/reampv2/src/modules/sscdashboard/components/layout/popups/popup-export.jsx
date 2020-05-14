import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import './popups.css';
import CountryPopup from "./country-popup";

class CountryPopupExport extends Component {
    render() {
        return (
            <div className="export-wrapper">
                <ul>
                  <li class="jpg">jpg</li>
                  <li class="xls">xls</li>
                  <li class="print">print</li>
                  <li class="email">email</li>
                  <li class="return-link">Retour vers la carte</li>
                </ul>
            </div>
        );
    }
}

export default CountryPopupExport;
