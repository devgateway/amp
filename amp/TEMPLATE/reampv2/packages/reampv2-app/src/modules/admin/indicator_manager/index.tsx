// eslint-disable
import React from 'react';
import { Provider } from 'react-redux';
// eslint-disable-next-line import/no-unresolved
import StartUp from './components/StartUp';
import defaultTrnPack from './config/initialTranslations.json';
import './index.css';
import '../../../open-sans.css';
import InidcatorTable from './components/table/IndicatorTable';
import { store } from './reducers/store';

const AdminIndicatorManagerApp = () => {
  return (
    <Provider store={store}>
      <StartUp defaultTrnPack={defaultTrnPack}>
        <InidcatorTable />
      </StartUp>
    </Provider>
  );
};

export default AdminIndicatorManagerApp;
