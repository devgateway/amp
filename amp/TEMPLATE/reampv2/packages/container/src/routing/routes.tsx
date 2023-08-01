import React, {Suspense} from 'react';
import  { RouteObject } from 'react-router-dom';
import NotFound from "../components/layouts/NotFound";

const AmpOfflineLazy = React.lazy(() => import('../components/AmpOffline'));

export const routes: RouteObject[] = [
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
