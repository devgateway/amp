import React, {Component, useState} from 'react';
import {TranslationContext} from '../../AppContext';
import './geocoder.css'
import Button from 'react-bootstrap/Button';
import {bindActionCreators} from "redux";
import {connect, useSelector} from "react-redux";
import RunSearch from "./RunSearch";

class GeocoderHeader extends Component {

    render() {
        let {translations} = this.context;
        let isActivityTable = this.props.geocoding.status === 'COMPLETED';

        return (
            <div className='panel-body custom-panel geocoder-header'>
                <div className={'col-md-2 text-header'}>{translations['amp.geocoder:selectProjects']}</div>
                <div className={'col-md-2 header-settings text-header'}><a href={'#geocoder'}>{translations['amp.geocoder:settings']}</a></div>
                {!isActivityTable && <div className={'col-md-2 text-header'}><a href={'#geocoder'}>{translations['amp.geocoder:resetAll']}</a></div>}
                {isActivityTable
                    ? <RunSearch title={translations['amp.geocoder:runSearch']} selectedActivities={this.props.selectedActivities}/>
                    : <Button variant="success" className={'pull-right button-header'}>{translations['amp.geocoder:saveAllEdits']}</Button>
                }
            </div>);
    }
}

GeocoderHeader.contextType = TranslationContext;

const mapStateToProps = state => {
    return {
        geocoding: state.geocodingReducer.geocoding,
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(GeocoderHeader);
