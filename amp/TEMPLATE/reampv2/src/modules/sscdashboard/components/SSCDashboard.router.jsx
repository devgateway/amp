import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import './../../../App.css';
import SssDashboardHome from '../SssDashboardHome';
import PrinterFriendly from '../utils/PrinterFriendly';

class SSCDashboardRouter extends Component {
    render() {
        return (
            <>
                <Route path="/ssc/print" exact component={PrinterFriendly}/>
                <Route path="/ssc" exact component={SssDashboardHome}/>

            </>
        );
    }
}

export default SSCDashboardRouter;
