import React from 'react';
import './App.css';
import {RouterProvider} from "react-router-dom";
import {createRouter} from "./routing";

const App = ({ routingStrategy }) => {
    const router = createRouter({ routingStrategy });
    return (
        <RouterProvider router={router} />
    );
}

export default App;
