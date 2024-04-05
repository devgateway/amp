import {lazy, Suspense} from "react";
import NavigationManager from "../NavigationManager";
import { Outlet } from "react-router-dom";

const SSCDashboardApp = lazy(() => import('../modules/sscdashboard'));
const AdminApps = lazy(() => import('../modules/admin/Admin.routes'));
const NDDDashboardApp = lazy(() => import('../modules/ndddashboard'));
const GeocoderApp = lazy(() => import('../modules/geocoder'));
const AmpOfflineApp = lazy(() => import('../modules/ampoffline/Download'));
const ReportGeneratorApp = lazy(() => import('../modules/report_generator'));

/** @type {import('react-router-dom').RouteObject[]} */
const routes = [
    {
        path: "/",
        element: (
            <NavigationManager>
                <Outlet />
            </NavigationManager>
        ),
        children: [
            {
                path: "/ssc/*",
                index: true,
                element: (
                        <Suspense fallback={<div>Loading...</div>}>
                            <SSCDashboardApp />
                        </Suspense>
                )
            },
            {
                path: "ampoffline",
                element: (
                    <Suspense fallback={<div>Loading...</div>}>
                        <AmpOfflineApp />
                    </Suspense>
                )
            },
            {
                path: "ndddashboard/*",
                element: (
                    <Suspense fallback={<div className="loading"></div>}>
                        <NDDDashboardApp />
                    </Suspense>
                )
            },
            {
                path: "report_generator/*",
                element: (
                    <Suspense fallback={<div>Loading...</div>}>
                        <ReportGeneratorApp />
                    </Suspense>
                )
            },
            {
                path: "geocoder",
                element: (
                    <Suspense fallback={<div>Loading...</div>}>
                        <GeocoderApp />
                    </Suspense>
                )
            },
            {
                path: "admin/*",
                element: (
                    <Suspense fallback={<div>Loading...</div>}>
                        <AdminApps />
                    </Suspense>
                )
            }
        ]
    }
]

export default routes;
