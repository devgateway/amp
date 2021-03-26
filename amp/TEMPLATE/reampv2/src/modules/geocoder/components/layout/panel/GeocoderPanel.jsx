import React, {Component} from 'react';
import {TranslationContext} from '../../AppContext';
import GeocodingTableHeader from "../table/GeocodingTableHeader";
import ActivityTable from "../table/ActivityTable";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import GeocodingTable from "../table/GeocodingTable";
import AlertError from "./AlertError";
import ActivityTableHeader from "../table/ActivityTableHeader";
import {selectActivitiesForGeocoding} from "../../../actions/activitiesAction";

const ProjectList = ({title}) => <h3>{title}</h3>;

class GeocoderPanel extends Component {

    onSelectActivity = (isSelected, activityId) => {
        let selectedActivities = Array.from(this.props.selectedActivities);
        if (isSelected) {
            selectedActivities.push(activityId);
        } else {
            selectedActivities = selectedActivities.filter(id => id !== activityId);
        }
        this.props.selectActivitiesForGeocoding(selectedActivities);
    }

    onSelectAllActivities = (isSelected, rows) => {
        let selectedActivities = Array.from(this.props.selectedActivities);
        let activityIds = rows.map(r => r.id);

        if (isSelected) {
            activityIds.forEach(activityId => selectedActivities.push(activityId));
        } else {
            selectedActivities = selectedActivities.filter(id => !activityIds.includes(id));
        }

        this.props.selectActivitiesForGeocoding(selectedActivities);
    }

    render() {
        const {translations} = this.context;

        const isGeocoding = this.props.geocoding && this.props.geocoding.status !== 'NOT_STARTED';

        let title = translations['amp.geocoder:projectList'];
        let table;
        let tableHeader;

        if (isGeocoding) {
            title = title +  ' - ' + translations['amp.geocoder:geocodedSelection'];
            tableHeader =  <GeocodingTableHeader/>;
            table = <GeocodingTable/>;
        } else {
            tableHeader =  <ActivityTableHeader selectedActivities={this.props.selectedActivities}/>;
            table = <ActivityTable onSelectActivity={this.onSelectActivity.bind(this)}
                                   onSelectAllActivities={this.onSelectAllActivities.bind(this)}/>
        }

        return (
            <div>
                {this.props.geocoding.error
                    ?  <AlertError error={this.props.geocoding.error} errorCode={this.props.geocoding.errorCode}/>
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
        selectedActivities: state.activitiesReducer.selectedActivities
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    selectActivitiesForGeocoding: selectActivitiesForGeocoding,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(GeocoderPanel);