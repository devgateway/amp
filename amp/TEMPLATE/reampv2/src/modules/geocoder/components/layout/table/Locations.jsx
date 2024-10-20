import React, {Component} from 'react';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import Highlighter from "react-highlight-words";
import LocationActionColumn from "./LocationActionColumn";
import {geocodeLocation} from "../../../actions/geocodingAction";
import {TranslationContext} from "../../AppContext";


function LocationFields({fields}) {
    return (
        fields.map((field) =>
            <>
            <div> / <b>{field.label}</b> |
                <span className={'location-text'}>
                    <Highlighter
                        searchWords={[field.entity]}
                        autoEscape={true}
                        textToHighlight={field.value}
                    />
                </span>
            </div>
            </>
        )
    );
}

class Locations extends Component {

    selectLocations = (ampId) => {
        return this.props.activities.filter(activity => activity.amp_id === ampId)[0].locations;
    }

    constructor(props) {
        super(props);

        this.selectLocations = this.selectLocations.bind(this);
    }

    handleAcceptLocation = (e, locationId) => {
        this.props.geocodeLocation(this.props.ampId, locationId, true)

        e.preventDefault();
    };

    handleRejectLocation = (e, locationId) => {
        this.props.geocodeLocation(this.props.ampId, locationId, false);

        e.preventDefault();
    };

    render() {
        let locations = this.selectLocations(this.props.ampId);

        const handleLocation = {
            handleAcceptLocation: this.handleAcceptLocation.bind(this),
            handleRejectLocation: this.handleRejectLocation.bind(this),
        };

        const {translations} = this.context;

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
            <div className={'search-result'}><b>{locations.length}</b> {translations['amp.geocoder:searchResults']} <b>{translations['amp.geocoder:locations']}</b></div>
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

Locations.contextType = TranslationContext;

const mapStateToProps = state => {
    return {
        activities: state.geocodingReducer.activities,
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    geocodeLocation: geocodeLocation,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Locations);