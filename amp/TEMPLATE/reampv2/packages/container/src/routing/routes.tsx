import React, {Suspense} from 'react';
import  { RouteObject } from 'react-router-dom';
import {REAMPV2_APP_NAME, USER_MANAGER_APP_NAME} from "../utils/constants";
import CrossNavigationManager from "../components/CrossNavigationManager";

const AmpOfflineLazy = React.lazy(() => import('../components/AmpOffline'));
const Reampv2Lazy = React.lazy(() => import('../components/Reampv2'));
const UserManagerLazy = React.lazy(() => import('../components/UserManager'));

export const routes: RouteObject[] = [
    {
        path: 'ampoffline',
        element: (
            <CrossNavigationManager>
                <Suspense fallback={<div className="loading"/>}>
                    <AmpOfflineLazy/>
                </Suspense>
            </CrossNavigationManager>

        )
    },
    {
        path: `/${REAMPV2_APP_NAME}/*`,
        element: (
            <CrossNavigationManager>
                <Suspense fallback={<div className="loading"/>}>
                    <Reampv2Lazy/>
                </Suspense>
            </CrossNavigationManager>

        )
    },
    {
        path: `${USER_MANAGER_APP_NAME}/*`,
        element: (
            <CrossNavigationManager>
                <Suspense fallback={<div className="loading"/>}>
                    <UserManagerLazy/>
                </Suspense>
            </CrossNavigationManager>
        )
    },
    {
        //not found
        path: '*',
        element: (
            <CrossNavigationManager>
                <div />
            </CrossNavigationManager>

        )
    },
]
