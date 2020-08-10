import React, {Component} from 'react';
import {TranslationContext} from '../../AppContext';
import GeocoderHeader from "./GeocoderHeader";
import ActivityTable from "../table/ActivityTable";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import GeocodingTable from "../table/GeocodingTable";

function GeocodingNotAvailable(props) {
    return <h4>Geocoding process not available. User {props.user} is owner of the process in '{props.workspace}' workspace</h4>;
}

function GeocodingRunning() {
    return (
        <div>
            <h4>Geocoding is still running</h4>
        </div>
    );
}

function ProjectList(props) {
    return <h3>{props.title}</h3>;
}

class GeocoderPanel extends Component {

    render() {
        const {translations} = this.context;

        const isGeocodingNotAvailable = this.props.geocoding.status === 'NOT_AVAILABLE';
        const isGeocodingAvailable = this.props.geocoding.status === 'AVAILABLE';
        const isGeocodingRunning = this.props.geocoding.status === 'RUNNING';
        const isGeocodingCompleted = this.props.geocoding.status === 'COMPLETED';

        return (
            <div>
            {isGeocodingNotAvailable && <GeocodingNotAvailable user={this.props.geocoding.creator} workspace={this.props.geocoding.workspace}/>}
            {(isGeocodingCompleted || isGeocodingAvailable) &&
                <div>
                    <ProjectList title={translations['amp.geocoder:projectList']}/>
                    <div className='panel panel-default'>
                        <GeocoderHeader/>
                        {isGeocodingAvailable && <GeocodingTable/>}
                        {isGeocodingCompleted && <ActivityTable/>}
                    </div>
                </div>
            }
            {isGeocodingRunning && <GeocodingRunning/>}
            </div>
        );
    }
}

GeocoderPanel.contextType = TranslationContext;

const mapStateToProps = state => {
    return {
        geocoding: state.geocodingReducer.geocoding,
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(GeocoderPanel);