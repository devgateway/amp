import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { RoutingStrategies } from './utils/constants';
import { MountOptions } from './types';
import 'semantic-ui-css/semantic.min.css';
import 'react-toastify/dist/ReactToastify.css';

const boilerplate = require('@devgateway/amp-boilerplate/dist/amp-boilerplate');

function Root({ routingStrategy, initialPathName }: { routingStrategy: RoutingStrategies, initialPathName: string }) {
  return (
    <React.StrictMode>
      <App routingStrategy={routingStrategy} initialPathName={initialPathName} />
    </React.StrictMode>
  );
}

export const mount = ({
  mountPoint, routingStrategy, initialPathName = '/', standalone = true,
} : MountOptions) => {
  if (mountPoint) {
    ReactDOM.createRoot(mountPoint).render(<Root routingStrategy={routingStrategy} initialPathName={initialPathName} />);
  }

  if (standalone) {
    // @ts-ignore
    // eslint-disable-next-line no-inner-declarations
    function getParameterByName(name : string, url = window.location.href) {
      name = name.replace(/[[\]]/g, '\\$&');
      const regex = new RegExp(`[?&]${name}(=([^&#]*)|&|#|$)`);
      const results = regex.exec(url);
      if (!results) return null;
      if (!results[2]) return '';
      return decodeURIComponent(results[2].replace(/\+/g, ' '));
    }

    if (!getParameterByName('embedded')) {
      // @ts-ignore
      // eslint-disable-next-line new-cap,no-new
      new boilerplate.layout({});
    }
  }

  // If you want to start measuring performance in your app, pass a function
  // to log results (for example: reportWebVitals(console.log))
  // or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
  reportWebVitals();
};
