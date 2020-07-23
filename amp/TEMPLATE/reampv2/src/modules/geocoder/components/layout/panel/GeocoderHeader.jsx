import React, {Component} from 'react';
import {TranslationContext} from '../../AppContext';
import './geocoder.css'
import Button from 'react-bootstrap/Button';

export default class GeocoderHeader extends Component {

    render() {
        const {translations} = this.context;

        return (
            <div className='panel-body custom-panel geocoder-header'>
                <div className={'col-md-2 text-header'}>{translations['amp.geocoder:selectProjects']}</div>
                <div className={'col-md-2 header-settings text-header'}><a href={'#geocoder'}>{translations['amp.geocoder:settings']}</a></div>
                <div className={'col-md-2 text-header'}><a href={'#geocoder'}>{translations['amp.geocoder:resetAll']}</a></div>
                <Button variant="success" className={'pull-right'}>{translations['amp.geocoder:saveAllEdits']}</Button>
            </div>);
    }
}

GeocoderHeader.contextType = TranslationContext;
