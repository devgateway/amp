import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {RoutingStrategies} from "./utils/Constants";
import {MountOptions} from "./types";


const Root = ({ routingStrategy, initialPathName }: { routingStrategy: RoutingStrategies, initialPathName: string }) => {
    return (
        <React.StrictMode>
            <App routingStrategy={routingStrategy} initialPathName={initialPathName}/>
        </React.StrictMode>
    )
};

export const mount = ({ mountPoint, routingStrategy , initialPathName = '/' }: MountOptions) => {
    if (mountPoint) {
        ReactDOM.createRoot(mountPoint).render(<Root routingStrategy={routingStrategy} initialPathName={initialPathName}/>)
    }

    // If you want to start measuring performance in your app, pass a function
    // to log results (for example: reportWebVitals(console.log))
    // or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
    reportWebVitals();

    //unmount the app if it is not in use
    return () => {
        if (!mountPoint) {
            ReactDOM.createRoot(mountPoint).unmount();
        }
    }
}


