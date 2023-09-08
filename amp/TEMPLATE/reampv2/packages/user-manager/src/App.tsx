import React from 'react';
import { RouterProvider } from 'react-router-dom';
import { Provider } from 'react-redux';
import { RoutingStrategies } from './utils/constants';
import { createRouter } from './routing';
import { store } from './reducers/store';
import './App.css';

interface AppProps {
  routingStrategy: RoutingStrategies;
  initialPathName: string;
}

const App: React.FC<AppProps> = (props) => {
  const { routingStrategy, initialPathName } = props;

  const router = createRouter({ routingStrategy, initialPathName });

  return (
      <Provider store={store}>
              <RouterProvider router={router} />
      </Provider>

  );
};

export default App;
