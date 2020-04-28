import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
import boilerplate from '/Users/juliandeanquin/dev/code/AmpIntelliJ/amp/TEMPLATE/ampTemplate/node_modules/amp-boilerplate/dist/amp-boilerplate.js';


ReactDOM.render(
    <React.StrictMode>
        <App />
    </React.StrictMode>,
    document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
new boilerplate.layout({});
serviceWorker.unregister();

