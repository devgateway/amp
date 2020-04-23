import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import './filters.css';

class HorizontalFilters extends Component {
    render() {
        return (
          <div className="h-filter-wrapper">
          Horizontal-filers
          </div>
        );
    }
}

export default HorizontalFilters;
