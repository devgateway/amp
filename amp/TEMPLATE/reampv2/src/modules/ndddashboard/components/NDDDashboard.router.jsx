import React, {Component} from 'react';
import {Route} from 'react-router-dom';
import '../../../App.css';
import NDDDashboardHome from './NDDDashboardHome';

class NDDDashboardRouter extends Component {
  render() {
    return (
      <>
        <Route path="/ndddashboard" exact component={NDDDashboardHome} />
        <Route path="/ndddashboard/:id" component={NDDDashboardHome} />
      </>
    );
  }
}

export default NDDDashboardRouter;
