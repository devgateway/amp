import React, {Component} from "react";
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import FilterSector from './sector';
import FilterSupport from './support';
import FilterDownload from './download';

import './filters.css';

class SidebarFilters extends Component {
    render() {
        return (
          <div className="sidebar-filter-wrapper">
            <FilterSector/>
            <FilterSupport/>
            <FilterDownload/>
          </div>
        );
    }
}

export default SidebarFilters;
