import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

class SidebarFilters extends Component {
    render() {
        return (
          <div className="h-filter-wrapper">
          Sidebar Filters
          </div>
        );
    }
}

export default SidebarFilters;
