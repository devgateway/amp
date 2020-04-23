import React, {Component} from "react";
import * as StartupActions from '../actions/startupAction';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import HoriztonalFilters from './horizontal-filters';

class MapContainer extends Component {
    render() {
        return (
          <div className="col-md-10 col-md-offset-2 map-wrapper">
            <HoriztonalFilters/>
          </div>
        );
    }
}

export default MapContainer;
