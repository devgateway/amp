import React from 'react';
import {RouterProvider, createHashRouter, createBrowserRouter} from 'react-router-dom';
import {routes} from "./routes";



const Router = () => {
    const router = createBrowserRouter(routes);
    return (
        <>
            <RouterProvider router={router}/>
        </>

    )
}
export default Router;
