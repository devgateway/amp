import React, {useEffect} from 'react';
import './App.css';
import {RouterProvider} from "react-router-dom";
import {createRouter} from "./routing";

const App = ({ routingStrategy, initialPathName }) => {
    useEffect(() => {
        window.addEventListener('openUserModal', (e) => {
            console.log('User Modal Event inide react reampv2 mfe.jsx', e)
        });
    }, []);
    const router = createRouter({ routingStrategy, initialPathName });
    return (
        <RouterProvider router={router} />
    );
}

export default App;
