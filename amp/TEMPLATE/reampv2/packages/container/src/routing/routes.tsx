import React, {Suspense} from 'react';
import  { RouteObject } from 'react-router-dom';
import NotFound from "../components/layouts/NotFound";

const AmpOfflineLazy = React.lazy(() => import('../components/AmpOffline'));

export const routes: RouteObject[] = [
    {
        path: '/',
        element: (
            <div>reampv2</div>
        ),
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
        path: 'reampv2-app',
        element: (
            <div>reampv2-app</div>
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
