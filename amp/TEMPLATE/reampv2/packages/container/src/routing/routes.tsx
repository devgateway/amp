import React, {Suspense} from 'react';
import  { RouteObject } from 'react-router-dom';
import NotFound from "../components/layouts/NotFound";
import {REAMPV2_APP_NAME} from "../utils/constants";

const AmpOfflineLazy = React.lazy(() => import('../components/AmpOffline'));
const Reampv2Lazy = React.lazy(() => import('../components/Reampv2'));

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
        path: `/${REAMPV2_APP_NAME}/*`,
        element: (
            <Suspense fallback={<div className="loading"/>}>
                <Reampv2Lazy/>
            </Suspense>
        )
    },
    {
        //not found
        path: '*',
        element: (
            <div />
        )
    },
]
