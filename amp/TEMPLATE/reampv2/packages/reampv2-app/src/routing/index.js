import React from 'react';
import {createBrowserRouter, createHashRouter, HashRouter, Route, RouterProvider, Routes} from "react-router-dom";
import routesArray from "./routes";

const AppRouter = () => {
    const router = createBrowserRouter(routesArray)
    return (
        <RouterProvider router={router} />
    );
}
export default AppRouter;

