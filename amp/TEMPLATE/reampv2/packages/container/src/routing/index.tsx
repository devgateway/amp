import React from 'react';
import {RouterProvider, createHashRouter} from 'react-router-dom';
import {routes} from "./routes";

const router = createHashRouter(routes);

const Router = () => {
    return (
        <>
            <RouterProvider router={router}/>
        </>

    )
}
export default Router;
