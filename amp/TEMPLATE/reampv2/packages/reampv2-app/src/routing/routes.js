import {lazy, Suspense} from "react";
import NavigationManager from "../NavigationManager";
import {Outlet} from "react-router-dom";

const SSCDashboardApp = lazy(() => import('../modules/sscdashboard'));
const AdminNDDApp = lazy(() => import('../modules/admin/ndd'));
const NDDDashboardApp = lazy(() => import('../modules/ndddashboard'));
const ReportGeneratorApp = lazy(() => import('../modules/report_generator'));
const GeocoderApp = lazy(() => import('../modules/geocoder'));
const AmpOfflineApp = lazy(() => import('../modules/ampoffline/Download'));

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
                path: "ssc/*",
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
                path: "ndd",
                element: (
                    <Suspense fallback={<div>Loading...</div>}>
                        <AdminNDDApp />
                    </Suspense>
                )
            },
            {
                path: "ndddashboard/*",
                element: (
                    <Suspense fallback={<div>Loading...</div>}>
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
            }
        ]
    }
]

export default routes;
