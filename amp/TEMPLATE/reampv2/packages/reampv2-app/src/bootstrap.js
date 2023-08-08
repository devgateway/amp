import 'react-app-polyfill/ie11';
import 'react-app-polyfill/stable';
import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
// amp-filter css
import '@tmugo/amp-filter/dist/amp-filter.css';
import reportWebVitals from "ampoffiline/src/reportWebVitals";
const boilerplate = require('@tmugo/amp-boilerplate/dist/amp-boilerplate');

function getParameterByName(name, url = window.location.href) {
  name = name.replace(/[[\]]/g, '\\$&');
  const regex = new RegExp(`[?&]${name}(=([^&#]*)|&|#|$)`);
  const results = regex.exec(url);
  if (!results) return null;
  if (!results[2]) return '';
  return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
if (!getParameterByName('embedded')) {
  new boilerplate.layout({});
}

serviceWorker.unregister();

const Root = () => (
    <React.StrictMode>
      <App />
    </React.StrictMode>
);

export const mount = (el) => {
  if (el) {
    ReactDOM.render(<Root />, el);
  }

  // If you want to start measuring performance in your app, pass a function
  // to log results (for example: reportWebVitals(console.log))
  // or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
  reportWebVitals();
}
