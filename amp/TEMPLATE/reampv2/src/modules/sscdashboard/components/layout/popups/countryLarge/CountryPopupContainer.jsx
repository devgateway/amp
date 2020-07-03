import React, { Component } from 'react';
import CountryPopupOverlayTitle from './popup-overlay-title';
import CountryPopupExport from './popup-export';
import CountryPopup from './country-popup';

export default class CountryPopupContainer extends Component {
    render() {
        const {projects} = this.props;
        if (!projects || projects.length === 0) {
            return null;
        }
        return (<div>
            <CountryPopupOverlayTitle/>
            <CountryPopupExport/>
            <div class="col-md-12 country-popup">
                <CountryPopup project={projects[0]}/>
            </div>
        </div>)
    }
}
