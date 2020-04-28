import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import HorizontalFilters from '../filters/horizontal-filters';
import './map.css';

class MapContainer extends Component {
    render() {
        return (
          <div className="col-md-10 col-md-offset-2 map-wrapper">
            <HorizontalFilters/>
          </div>
        );
    }
}

export default MapContainer;
