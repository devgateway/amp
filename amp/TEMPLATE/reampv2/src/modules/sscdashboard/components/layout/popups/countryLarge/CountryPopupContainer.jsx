import React, { Component } from 'react';
import CountryPopupOverlayTitle from './popup-overlay-title';
import CountryPopupExport from './popup-export';
import CountryPopup from './country-popup';

export default class CountryPopupContainer extends Component {
    render() {
        return (<div>
            <CountryPopupOverlayTitle/>
            <CountryPopupExport/>
            <div class="col-md-9 country-popup">
                <CountryPopup/>
            </div>
        </div>)
    }
}
