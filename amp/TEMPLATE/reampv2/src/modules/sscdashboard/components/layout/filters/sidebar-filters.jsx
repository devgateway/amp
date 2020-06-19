import React, { Component } from 'react';
import FilterSector from './sector';
import FilterSupport from './support';
import FilterDownload from './download';
import FilterHome from './home';
import './filters.css';

class SidebarFilters extends Component {
    render() {
        const {chartSelected, onChangeChartSelected} = this.props;
        return (
            <div className="sidebar-filter-wrapper">
                <FilterHome chartSelected={chartSelected} onChangeChartSelect={onChangeChartSelected}/>
                <FilterSector chartSelected={chartSelected} onChangeChartSelect={onChangeChartSelected}/>
                <FilterSupport/>
                <FilterDownload/>
            </div>
        );
    }
}

export default SidebarFilters;
