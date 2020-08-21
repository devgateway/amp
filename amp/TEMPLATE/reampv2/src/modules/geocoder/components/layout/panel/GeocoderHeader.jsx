import React, {Component} from 'react';
import {TranslationContext} from '../../AppContext';
import './geocoder.css'
import Button from 'react-bootstrap/Button';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import RunSearch from "./RunSearch";
import {resetAllActivities, runSearch} from "../../../actions/geocodingAction";

class GeocoderHeader extends Component {

    constructor(props) {
        super(props);

        this.handleResetAll = this.handleResetAll.bind(this);
        this.onRunSearch = this.onRunSearch.bind(this);
    }

    handleResetAll = (e) => {
        this.props.resetAllActivities();

        e.preventDefault();
        console.log('User clicked reset All:', this);
    };

    onRunSearch = () => {
        this.props.runSearch(this.props.selectedActivities);
        console.log('User clicked run search:', this.props.selectedActivities);
    }

    render() {
        const {translations} = this.context;
        const isActivityTable = this.props.geocoding.status === 'NOT_STARTED';

        return (
            <div className='panel-body custom-panel geocoder-header'>
                <div className={'col-md-2 text-header'}>{translations['amp.geocoder:selectProjects']}</div>
                <div className={'col-md-2 header-settings text-header'}><a href={'#geocoder'}>{translations['amp.geocoder:settings']}</a></div>
                {!isActivityTable && <div className={'col-md-2 text-header'}><a href={'#geocoder'}  onClick={this.handleResetAll}>{translations['amp.geocoder:resetAll']}</a></div>}
                {isActivityTable
                    ? <RunSearch title={translations['amp.geocoder:runSearch']}
                                 selectedActivities={this.props.selectedActivities}
                                 onRunSearch={this.onRunSearch}/>
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
    resetAllActivities: resetAllActivities,
    runSearch: runSearch,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(GeocoderHeader);
