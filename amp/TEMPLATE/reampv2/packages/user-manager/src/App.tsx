import React from 'react';
import {createRouter} from './routing';
import {RouterProvider} from "react-router-dom";
import {RoutingStrategies} from "./utils/Constants";

interface AppProps {
  routingStrategy: RoutingStrategies;
  initialPathName: string;
}

const App: React.FC<AppProps> = (props) => {
  const { routingStrategy, initialPathName } = props;

  const router = createRouter({ routingStrategy, initialPathName });

  return (
      <RouterProvider router={router}/>
  );
};

export default App;
