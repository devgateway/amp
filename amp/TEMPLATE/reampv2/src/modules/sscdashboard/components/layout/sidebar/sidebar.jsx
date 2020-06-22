import React from 'react';
import SidebarIntro from './sidebar-intro';
import SidebarFilters from '../filters/sidebar-filters';

import './sidebar.css';


const Sidebar = props => {
    //TODO this is temporal until side bar is addressed
    const {chartSelected, onChangeChartSelected} = props;
    return (
        <div className="col-md-2 sidebar">
            <SidebarFilters chartSelected={chartSelected} onChangeChartSelected={onChangeChartSelected}/>
            <SidebarIntro/>
        </div>
    );
}

export default Sidebar;
