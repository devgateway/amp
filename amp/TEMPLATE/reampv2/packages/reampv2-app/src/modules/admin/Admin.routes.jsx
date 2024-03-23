import React, {lazy} from 'react';
import {Route, Routes } from 'react-router-dom';

const AdminNDDApp = lazy(() => import('./ndd'));
const IndicatorManagerApp = lazy(() => import('./indicator_manager'));

const AdminRoutes = () => {
    return (
        <Routes>
            <Route path="/ndd" element={<AdminNDDApp/>} />
            <Route path="/indicator_manager" element={<IndicatorManagerApp/>} />
        </Routes>
    );
}
export default AdminRoutes;
