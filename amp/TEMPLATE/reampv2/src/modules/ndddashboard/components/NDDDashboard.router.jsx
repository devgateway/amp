import React, {Component} from 'react';
import {Route} from 'react-router-dom';
import './../../../App.css';
import NDDDashboardHome from '../NDDDashboardHome';

class NDDDashboardRouter extends Component {
    render() {
        return (
            <>
                <Route path="/ssc" exact component={NDDDashboardHome}/>
            </>
        );
    }
}

export default NDDDashboardRouter;
