import React, { Suspense } from 'react';
import { RouteObject, Outlet } from 'react-router-dom';
import Home from '../pages/Home';
import NavigationManager from '../components/NavigationManager';
import { REAMPV2_APP_NAME } from '../utils/constants';

const Reampv2AppLazy = React.lazy(() => import('../components/remotes/Reampv2'));
const AmpOfflineLazy = React.lazy(() => import('../components/remotes/AmpOffline'));

export const routes: RouteObject[] = [
  {
    path: '/',
    element: (
      <NavigationManager>
        <Outlet />
      </NavigationManager>
    ),
    children: [
      {
        index: true,
        element: (
            <div>
              <Home />
            </div>

        ),
      },

      {
        path: 'edit-profile',
        element: (
            <Home />
        ),
      },
      {
        path: `${REAMPV2_APP_NAME}/*`,
        element: (
            <Suspense fallback={<div>Loading...</div>}>
                <Reampv2AppLazy />
            </Suspense>
        ),
      },
      {
        path: 'ampoffline',
        element: (
            <Suspense fallback={<div>Loading...</div>}>
                <AmpOfflineLazy />
            </Suspense>
        ),
      },
    ],
  },
  {
    // not found
    path: '*',
    element: (
      <div />
    ),
  },
];
