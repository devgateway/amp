import React, {Component} from 'react';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import LocationActionColumn from "./LocationActionColumn";
import {geocodeLocation} from "../../../actions/geocodingAction";


function LocationFields({fields}) {
    return (
        fields.map((field) =>
            <>
            <div> / <b>{field.name}</b> | {field.value}</div>
            </>
        )
    );
}

class Locations extends Component {

    selectLocations = (activityId) => {
        return this.props.activities.filter(activity => activity.activity_id === activityId)[0].locations;
    }


    constructor(props) {
        super(props);

        this.selectLocations = this.selectLocations.bind(this);
    }

    handleAcceptLocation = (e, locationId) => {
        this.props.geocodeLocation(this.props.activityId, locationId, true)

        e.preventDefault();
        console.log('User clicked accepted:', this);

    };

    handleRejectLocation = (e, locationId) => {
        this.props.geocodeLocation(this.props.activityId, locationId, false);

        e.preventDefault();
        console.log('User clicked reject:', this);
    };

    render() {
        let locations = this.selectLocations(this.props.activityId);

        const handleLocation = {
            handleAcceptLocation: this.handleAcceptLocation.bind(this),
            handleRejectLocation: this.handleRejectLocation.bind(this),
        };

        let locationItems = locations.map((location) =>
            <>
                <tr>
                    <td className="col-10 location-name-column">
                        <span className="location-name">{location.name}</span>
                        <span>({location.administrative_level})</span>
                    </td>
                    <td className="col-70 location-field-column ">
                        <LocationFields fields={location.fields}/>
                    </td>
                    <td className="col-20">
                        <LocationActionColumn location={location} handleLocation={handleLocation} props={this.props}/>
                    </td>
                </tr>
                <tr>
                    <td colSpan="3"><div className={'border-line'}></div></td>
                </tr>
            </>
        );


        return ( <div>
            <div className={'search-result'}><b>{locations.length}</b> Search Results of <b>Locations</b></div>
            <div>
                <table className={'location-container'}>
                    <tbody>
                        {locationItems}
                    </tbody>
                </table>
            </div>
        </div>);
    }
}

const mapStateToProps = state => {
    return {
        activities: state.geocodingReducer.activities,
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    geocodeLocation: geocodeLocation,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Locations);