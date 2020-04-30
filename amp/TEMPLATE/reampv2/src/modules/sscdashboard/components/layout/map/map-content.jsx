import React, {Component} from "react";
import HorizontalFilters from '../filters/horizontal-filters';
import './map.css';
import MapHome from "../../map/MapHome";

class MapContainer extends Component {
    render() {
        return (
            <div className="col-md-10 col-md-offset-2 map-wrapper">
                <HorizontalFilters/>
                <MapHome/>
            </div>
        );
    }
}

export default MapContainer;
