import React, {Component} from 'react';
import {TranslationContext} from '../../AppContext';
import GeocodingTableHeader from "../table/GeocodingTableHeader";
import ActivityTable from "../table/ActivityTable";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import GeocodingTable from "../table/GeocodingTable";
import Modal from "react-bootstrap/Modal";
import AlertError from "./AlertError";
import ActivityTableHeader from "../table/ActivityTableHeader";

const GeocodingNotAvailable = ({user, workspace}) =>
    <h4>Geocoding process not available. User {user} is owner of the process in '{workspace}' workspace</h4>;

const GeocodingRunning = ({message, running}) => {
    return (
        <Modal show={running} animation={false}>
            <Modal.Body>{message}</Modal.Body>
        </Modal>
    )
}

const ProjectList = ({title}) => <h3>{title}</h3>;

class GeocoderPanel extends Component {

    state = {
        selectedActivities: []
    };

    onSelectActivity = (isSelected, activityId) => {
        this.setState(previousState => {
            let selectedActivities = Array.from(this.state.selectedActivities);
            if (isSelected) {
                selectedActivities.push(activityId);
            } else {
                selectedActivities = selectedActivities.filter(id => id !== activityId);
            }
            return {selectedActivities};
        });
    }

    onSelectAllActivities = (isSelected, rows) => {
        let selectedActivities = Array.from(this.state.selectedActivities);
        let activityIds = rows.map(r => r.id);

        if (isSelected) {
            activityIds.forEach(activityId => selectedActivities.push(activityId));
        } else {
            selectedActivities = selectedActivities.filter(id => !activityIds.includes(id));
        }

        this.setState({selectedActivities});
    }

    render() {
        const {translations} = this.context;

        const isGeocoding = this.props.geocoding && this.props.geocoding.status != 'NOT_STARTED';

        let title = translations['amp.geocoder:projectList'];
        let table;
        let tableHeader;

        if (isGeocoding) {
            title = title +  ' - ' + translations['amp.geocoder:geocodedSelection'];
            tableHeader =  <GeocodingTableHeader/>;
            table = <GeocodingTable/>;
        } else {
            tableHeader =  <ActivityTableHeader selectedActivities={this.state.selectedActivities}/>;
            table = <ActivityTable onSelectActivity={this.onSelectActivity.bind(this)}
                                   onSelectAllActivities={this.onSelectAllActivities.bind(this)}/>
        }


        return (
            <div>
            {/*{isGeocodingNotAvailable && <GeocodingNotAvailable user={this.props.geocoding.creator} workspace={this.props.geocoding.workspace}/>}*/}
                {this.props.geocoding.error
                    ?  <AlertError error={this.props.geocoding.error}/>
                    : <div>
                        <ProjectList title={title}/>
                        <div className='panel panel-default'>
                            {tableHeader}
                            {table}
                        </div>
                    </div>
                }
            </div>
        );
    }
}

GeocoderPanel.contextType = TranslationContext;

const mapStateToProps = state => {
    return {
        activitiesPending: state.activitiesReducer.pending,
        geocoding: state.geocodingReducer,
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(GeocoderPanel);