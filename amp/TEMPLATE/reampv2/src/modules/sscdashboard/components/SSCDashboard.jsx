import React, {Component} from "react";
import * as StartupActions from '../actions/startupAction';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import Sidebar from './sidebar';
import MapContainer from './map-content';

class SSCDashboard extends Component {

    constructor(props) {
        super(props);
    }
    componentDidMount() {
        this.props.startupAction();
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

const mapStateToProps = state => ({...state});

const mapDispatchToProps = dispatch => bindActionCreators(Object.assign({}, StartupActions), dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(SSCDashboard);
