import React, {Component} from 'react';
import {TranslationContext} from '../../AppContext';
import './geocoder.css'
import Button from 'react-bootstrap/Button';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import {resetAllActivities, runSearch} from "../../../actions/geocodingAction";
import RunSearchButton from "./RunSearchButton";
import ResetAllButton from "./ResetAllButton";

class GeocoderHeader extends Component {

    constructor(props) {
        super(props);

        this.onRunSearch = this.onRunSearch.bind(this);
    }

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
                {isActivityTable
                    ? <RunSearchButton title={translations['amp.geocoder:runSearch']}
                                 selectedActivities={this.props.selectedActivities}
                                 onRunSearch={this.onRunSearch}/>
                    : (<>
                        <ResetAllButton title={translations['amp.geocoder:resetAll']} />
                        <Button variant="success" className={'pull-right button-header'}>{translations['amp.geocoder:saveAllEdits']}</Button>
                    </>)
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
    runSearch: runSearch,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(GeocoderHeader);
