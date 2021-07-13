import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import '../../../App.css';
import ReportGeneratorHome from './ReportGeneratorHome';

class ReportGeneratorRouter extends Component {
  render() {
    return (
      <>
        <Route path="/report_generator" exact component={ReportGeneratorHome} />
        <Route path="/report_generator/:id" component={ReportGeneratorHome} />
      </>
    );
  }
}

export default ReportGeneratorRouter;
