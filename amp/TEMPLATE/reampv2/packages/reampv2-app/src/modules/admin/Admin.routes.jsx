import React, {lazy} from 'react';
import {Route, Routes } from 'react-router-dom';
import OutcomeOutputManagementPage from "./indicator_manager/pages/OutcomeOutputManagementPage";
import DisaggregationManagerPage from "./indicator_manager/pages/DisaggregationManagerPage";

const AdminNDDApp = lazy(() => import('./ndd'));
const IndicatorManagerApp = lazy(() => import('./indicator_manager'));

const AdminRoutes = () => {
    return (
        <Routes>
            <Route path="/ndd" element={<AdminNDDApp/>} />
            <Route path="/indicator_manager" element={<IndicatorManagerApp/>} />
            <Route path="/indicator_manager/outcome-output-management" element={<OutcomeOutputManagementPage/>} />
            <Route path="/indicator_manager/disaggregation-manager" element={<DisaggregationManagerPage/>} />
        </Routes>
    );
}
export default AdminRoutes;
