import React from 'react';
import  { RouteObject, Outlet } from 'react-router-dom';
import Home from '../pages/Home';
import NavigationManager from '../components/NavigationManager';


export const routes: RouteObject[] = [
    {
        path: `/`,
        element: (
            <NavigationManager>
                <Outlet />
            </NavigationManager>
        ),
        children: [
            {
                index: true,
                element: (
                    <Home />
                )
            }
        ]
    },
    {
        //not found
        path: '*',
        element: (
            <div />
        )
    },
]
