import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import reportWebVitals from './reportWebVitals';
//@ts-ignore
const boilerplate = require('@devgateway/amp-boilerplate/dist/amp-boilerplate');

const Root = () => (
    <React.StrictMode>
        <App />
    </React.StrictMode>
);

console.log('Loading reampv2 app +++++++++++++')

function getParameterByName(name: any, url = window.location.href) {
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

export const mount = (el?: HTMLElement) => {
    if (el) {
        ReactDOM.createRoot(el).render(<Root />);

        // If you want to start measuring performance in your app, pass a function
        // to log results (for example: reportWebVitals(console.log))
        // or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
        reportWebVitals();
    };
};
