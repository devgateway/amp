import React, {Component} from "react";
  // import * as StartupActions from './actions/startupAction';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import Sidebar from './layout/sidebar/sidebar';
import MapContainer from './layout/map/map-content';

class SSCDashboard extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (

          <div className="container-fluid content-wrapper">
            <div className="row">
              <Sidebar/>
              <MapContainer/>
            </div>
          </div>);
    }
}

export default SSCDashboard;
