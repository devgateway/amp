import 'react-app-polyfill/ie11';
import 'react-app-polyfill/stable';
import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
import '@devgateway/amp-filter/dist/amp-filter.css';
const boilerplate = require('@devgateway/amp-boilerplate/dist/amp-boilerplate');

const Root = ({ routingStrategy }) => {
    return (
        <React.StrictMode>
            <App routingStrategy={routingStrategy}/>
        </React.StrictMode>
    )
};

export const mount = ({ mountPoint, routingStrategy , standalone = true}) => {
    if (mountPoint) {
        ReactDOM.render(<Root routingStrategy={routingStrategy }/>, mountPoint);
    }

    if (standalone) {
        function getParameterByName(name, url = window.location.href) {
            name = name.replace(/[[\]]/g, '\\$&');
            const regex = new RegExp(`[?&]${name}(=([^&#]*)|&|#|$)`);
            const results = regex.exec(url);
            if (!results) return null;
            if (!results[2]) return '';
            return decodeURIComponent(results[2].replace(/\+/g, ' '));
        }


        if (!getParameterByName('embedded')) {
            new boilerplate.layout({});
        }
    }

    serviceWorker.register();
    //unmount the app if it is not in use
    return () => {
        if (!mountPoint) {
            ReactDOM.unmountComponentAtNode(mountPoint);
        }
    }
}
