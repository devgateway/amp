import React, {Component} from 'react';
import {TranslationContext} from '../../AppContext';
import './geocoder.css'
import Button from 'react-bootstrap/Button';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import RunSearchButton from "./button/RunSearchButton";
import ResetAllButton from "./button/ResetAllButton";
import CancelGeocodingButton from "./button/CancelGeocodingButton";
import SaveAllEditsButton from "./button/SaveAllEditsButton";

const SelectedActivitiesMessage = ({title, selectedSize, size}) =>  {
    return (
        <>
            <div className={'col-md-2 text-header'}>
                <span>{title}</span>
                <span className="activities-message"><b>({selectedSize}/{size})</b></span>
            </div>
        </>
    );
}

class GeocoderHeader extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        const {translations} = this.context;
        const isActivityTable = this.props.geocoding.status === 'NOT_STARTED';

        return (
            <div className='panel-body custom-panel geocoder-header'>
                {/*<div className={'col-md-2 header-settings text-header'}><a href={'#geocoder'}>{translations['amp.geocoder:settings']}</a></div>*/}
                {isActivityTable
                    ? (<>
                        <SelectedActivitiesMessage title={translations['amp.geocoder:selectProjects']}
                                                    selectedSize={this.props.selectedActivities.length}
                                                    size={this.props.activities.length} />
                        <RunSearchButton title={translations['amp.geocoder:runSearch']}
                                 selectedActivities={this.props.selectedActivities}
                                 onRunSearch={this.onRunSearch}/>
                     </>)
                    : (<>
                        <ResetAllButton title={translations['amp.geocoder:resetAll']} />
                        <CancelGeocodingButton title={translations['amp.geocoder:cancelGeocoding']} />
                        <SaveAllEditsButton className={'pull-right button-header'} title={translations['amp.geocoder:saveAllEdits']} />
                    </>)
                }
            </div>);
    }
}

GeocoderHeader.contextType = TranslationContext;

const mapStateToProps = state => {
    return {
        geocoding: state.geocodingReducer,
        activities: state.activitiesReducer.activities,
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(GeocoderHeader);
