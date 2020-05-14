import React, { Component } from 'react';
import { HomeButton } from './home-button';
import SidebarIntro from './sidebar-intro';
import SidebarFilters from '../filters/sidebar-filters';

import './sidebar.css';


class Sidebar extends Component {
    render() {
        return (
            <div className="col-md-2 sidebar">
                <HomeButton/>
                <SidebarFilters/>
                <SidebarIntro/>
            </div>
        );
    }
}

export default Sidebar;
