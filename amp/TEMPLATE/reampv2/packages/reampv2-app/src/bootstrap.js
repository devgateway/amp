import 'react-app-polyfill/ie11';
import 'react-app-polyfill/stable';
import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
// amp-filter css
import '@devgateway/amp-filter/dist/amp-filter.css';

const boilerplate = require('@devgateway/amp-boilerplate/dist/amp-boilerplate');


serviceWorker.unregister();
console.log('reampv2 app bootstrap');
const Root = () => {
    return (
        <React.StrictMode>
            <App/>
        </React.StrictMode>
    )
};

export const mount = ({el, standalone = true}) => {
    if (el) {
        ReactDOM.render(<Root/>, el);
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
}
