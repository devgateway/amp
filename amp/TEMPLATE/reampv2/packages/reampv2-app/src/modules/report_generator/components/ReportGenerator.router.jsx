import React, { Component } from 'react';
import {Route, Routes} from 'react-router-dom';
import '../../../App.css';
import ReportGeneratorHome from './ReportGeneratorHome';

class ReportGeneratorRouter extends Component {
  render() {
    return (
      <Routes>
        <Route path="/" exact element={<ReportGeneratorHome />} />
        <Route path=":id" element={<ReportGeneratorHome />} />
      </Routes>
    );
  }
}

export default ReportGeneratorRouter;
