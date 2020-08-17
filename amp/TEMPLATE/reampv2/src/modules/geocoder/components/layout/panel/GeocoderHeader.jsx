import React, {Component} from 'react';
import {TranslationContext} from '../../AppContext';
import './geocoder.css'
import Button from 'react-bootstrap/Button';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import RunSearch from "./RunSearch";
import {resetAllActivities} from "../../../actions/geocodingAction";

class GeocoderHeader extends Component {

    constructor(props) {
        super(props);

        this.handleResetAll = this.handleResetAll.bind(this);
    }

    handleResetAll = (e) => {
        this.props.resetAllActivities();

        e.preventDefault();
        console.log('User clicked reset All:', this);
    };

    render() {
        let {translations} = this.context;
        let isActivityTable = this.props.geocoding.status === 'COMPLETED';

        return (
            <div className='panel-body custom-panel geocoder-header'>
                <div className={'col-md-2 text-header'}>{translations['amp.geocoder:selectProjects']}</div>
                <div className={'col-md-2 header-settings text-header'}><a href={'#geocoder'}>{translations['amp.geocoder:settings']}</a></div>
                {!isActivityTable && <div className={'col-md-2 text-header'}><a href={'#geocoder'}  onClick={this.handleResetAll}>{translations['amp.geocoder:resetAll']}</a></div>}
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
        geocoding: state.geocodingReducer,
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    resetAllActivities: resetAllActivities
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(GeocoderHeader);
