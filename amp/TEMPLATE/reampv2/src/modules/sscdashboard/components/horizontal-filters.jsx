import React, {Component} from "react";
import * as StartupActions from '../actions/startupAction';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

class HorizonalFilters extends Component {
    render() {
        return (
          <div className="h-filter-wrapper">
          Horizontal-filers
          </div>
        );
    }
}

export default HorizonalFilters;
