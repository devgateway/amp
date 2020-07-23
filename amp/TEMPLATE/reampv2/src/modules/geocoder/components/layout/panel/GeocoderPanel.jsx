import React, {Component} from 'react';
import {TranslationContext} from '../../AppContext';
import GeocoderHeader from "./GeocoderHeader";
import TableBootstrap from "../table/ActivityTable";

export default class GeocoderPanel extends Component {

    render() {
        const {translations} = this.context;

        return (<div className='container'>
            <div className='title-container'>
                <h3>{translations['amp.geocoder:projectList']}</h3>
            </div>
            <div className='panel panel-default'>
                <GeocoderHeader/>
                <TableBootstrap/>
            </div>
        </div>);
    }
}

GeocoderPanel.contextType = TranslationContext;
