import React, {Suspense} from 'react';
import  { RouteObject } from 'react-router-dom';
import NotFound from "../components/layouts/NotFound";

const AmpOfflineLazy = React.lazy(() => import('../components/AmpOffline'));
const Reampv2Lazy = React.lazy(() => import('../components/Reampv2'));

export const routes: RouteObject[] = [
    {
        path: '/reampv2-app',
        element: (
            <Suspense fallback={<div className="loading"/>}>
                <Reampv2Lazy/>
            </Suspense>
        )
    },
    {
        path: 'ampoffline',
        element: (
            <Suspense fallback={<div className="loading"/>}>
                <AmpOfflineLazy/>
            </Suspense>
        )
    },
    {
        //not found
        path: '*',
        element: (
            <NotFound />
        )
    }
]
