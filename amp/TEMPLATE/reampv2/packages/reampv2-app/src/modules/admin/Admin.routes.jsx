import React, {lazy} from 'react';
import {Route, Routes } from 'react-router-dom';
import OutcomeOutputManagementPage from "./indicator_manager/pages/OutcomeOutputManagementPage";
import OutputManagementPage from "./indicator_manager/pages/OutputManagementPage";

const AdminNDDApp = lazy(() => import('./ndd'));
const IndicatorManagerApp = lazy(() => import('./indicator_manager'));

const AdminRoutes = () => {
    return (
        <Routes>
            <Route path="/ndd" element={<AdminNDDApp/>} />
            <Route path="/indicator_manager" element={<IndicatorManagerApp/>} />
            <Route path="/indicator_manager/outcome-output-management" element={<OutcomeOutputManagementPage/>} />
            <Route path="/indicator_manager/output-management" element={<OutputManagementPage/>} />
        </Routes>
    );
}
export default AdminRoutes;
