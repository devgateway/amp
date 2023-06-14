import React from 'react';
import Filters from './Filters';
import Settings from './Settings';
import Share from './Share';

interface HeaderContainerProps {
  onApplyFilters: (...args: any) => any;
  dashboardId: any;
  onApplySettings: (...args: any) => any;
  globalSettings: any;
  settings: any;
};

const HeaderContainer: React.FC<HeaderContainerProps> = (props: any) => {
  const {
    onApplyFilters, dashboardId, onApplySettings, globalSettings, settings
  } = props;

  return (
    <div>
      <Filters onApplyFilters={onApplyFilters} dashboardId={dashboardId} globalSettings={globalSettings} />
      <Settings onApplySettings={onApplySettings} settings={settings} />
      <Share />
    </div>
  );
}

export default HeaderContainer;
