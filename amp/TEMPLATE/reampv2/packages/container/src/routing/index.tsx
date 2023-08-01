import React from 'react';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { routes } from "./routes";

const browserRouter = createBrowserRouter(routes);

const Router = () => {
    return (
        <RouterProvider router={browserRouter} />
    )
}
export default Router;
