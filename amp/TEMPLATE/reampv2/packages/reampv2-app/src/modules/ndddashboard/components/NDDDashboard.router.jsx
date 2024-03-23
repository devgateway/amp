import React, { Component } from 'react';
import { Route, Routes } from 'react-router-dom';
import '../../../App.css';
import NDDDashboardHome from './NDDDashboardHome';

class NDDDashboardRouter extends Component {
  render() {
    return (
      <Routes>
        <Route path="/" exact element={<NDDDashboardHome />} />
        <Route path="/:id" component={<NDDDashboardHome />} />
      </Routes>
    );
  }
}

export default NDDDashboardRouter;
