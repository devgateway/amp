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

const ProjectList = ({title}) => <div className={'geocoder-title'}>{title}</div>;

class GeocoderPanel extends Component {

    onSelectActivity = (isSelected, ampId) => {
        let selectedActivities = Array.from(this.props.selectedActivities);
        if (isSelected) {
            selectedActivities.push(ampId);
        } else {
            selectedActivities = selectedActivities.filter(id => id !== ampId);
        }
        this.props.selectActivitiesForGeocoding(selectedActivities);
    }

    onSelectAllActivities = (isSelected, rows) => {
        let selectedActivities = Array.from(this.props.selectedActivities);
        let ampIds = rows.map(r => r.id);

        if (isSelected) {
            ampIds.forEach(ampId => selectedActivities.push(ampId));
        } else {
            selectedActivities = selectedActivities.filter(id => !ampIds.includes(id));
        }

        this.props.selectActivitiesForGeocoding(selectedActivities);
    }

    render() {
        const {translations} = this.context;

        const isGeocoding = this.props.geocoding && this.props.geocoding.status !== 'NOT_STARTED';

        let title = translations['amp.geocoder:projectList'];
        let table;
        let tableHeader;

        let pending;

        if (isGeocoding) {
            pending = this.props.geocoding.pending || this.props.geocoding.geocodeShouldRun;
            title = title +  ' - ' + translations['amp.geocoder:geocodedSelection'];
            tableHeader =  <GeocodingTableHeader/>;
            table = <GeocodingTable/>;
        } else {
            pending = this.props.activitiesPending;
            tableHeader =  <ActivityTableHeader selectedActivities={this.props.selectedActivities}/>;
            table = <ActivityTable onSelectActivity={this.onSelectActivity.bind(this)}
                                   onSelectAllActivities={this.onSelectAllActivities.bind(this)}/>
        }

        return (
            <div>
                {this.props.geocoding.error
                    ?  <AlertError error={this.props.geocoding.error} errorCode={this.props.geocoding.errorCode}/>
                    : <div>
                        {!pending && <ProjectList title={title}/>}
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