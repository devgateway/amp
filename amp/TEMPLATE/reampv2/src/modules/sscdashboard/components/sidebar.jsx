import React, {Component} from "react";
import * as StartupActions from '../actions/startupAction';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import HomeButton from './home-button';
import SidebarIntro from './sidebar-intro';

class Sidebar extends Component {
    render() {
        return (
          <div className="col-md-2 sidebar">
            <HomeButton/>
            <SidebarIntro/>
          </div>
        );
    }
}

export default Sidebar;
