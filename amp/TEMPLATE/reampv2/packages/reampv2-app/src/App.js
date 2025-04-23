import React, {useEffect} from 'react';
import './App.css';
import {RouterProvider} from "react-router-dom";
import {createRouter} from "./routing";

const App = ({ routingStrategy, initialPathName }) => {
    const router = createRouter({ routingStrategy, initialPathName });
    return (
        <RouterProvider router={router} />
    );
}

export default App;
