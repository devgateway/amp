import React, { Component } from 'react';
import {Outlet, Route, Routes} from 'react-router-dom';
import '../../../App.css';
import SssDashboardHome from '../SscDashboardHome';
import PrinterFriendly from '../utils/PrinterFriendly';

const SSCDashboardRouter = () => {
    return (
      <Routes>
        <Route path="/print"  element={<PrinterFriendly />} />
        <Route path="/" element={<SssDashboardHome />} />
      </Routes>
    );
}

export default SSCDashboardRouter;
