import React, {useEffect} from 'react';
import './App.css';
// import AppRoute from './App.route';
// import NavigationManager from "./NavigationManager";
// import {createHashHistory} from "history";
import AppRouter from "./routing";

const App = () => {
    // useEffect(() => {
    //     // Detect changes in params after the '#'.
    //     window.addEventListener('hashchange', () => {
    //         window.location.reload();
    //     }, false);
    // }, []);

    return (
        <AppRouter/>
    );
}

export default App;
