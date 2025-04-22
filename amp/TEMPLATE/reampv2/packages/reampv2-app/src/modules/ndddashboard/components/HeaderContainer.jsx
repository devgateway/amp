import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Filters from './Filters';
import Settings from './Settings';
import Share from './Share';

export default class HeaderContainer extends Component {
  render() {
    const {
      onApplyFilters, filters, dashboardId, onApplySettings, globalSettings, settings, fundingType, selectedPrograms
    } = this.props;
    return (
      <div>
        <Filters onApplyFilters={onApplyFilters} dashboardId={dashboardId} globalSettings={globalSettings} />
        <Settings onApplySettings={onApplySettings} settings={settings} />
        <Share filters={filters} settings={settings} fundingType={fundingType} selectedPrograms={selectedPrograms} />
      </div>
    );
  }
};

HeaderContainer.propTypes = {
  onApplyFilters: PropTypes.func.isRequired,
  filters: PropTypes.object,
  dashboardId: PropTypes.string,
  onApplySettings: PropTypes.func.isRequired,
  globalSettings: PropTypes.object.isRequired,
  settings: PropTypes.object,
  fundingType: PropTypes.string.isRequired,
  selectedPrograms: PropTypes.array.isRequired
};

HeaderContainer.defaultProps = {
  filters: undefined,
  dashboardId: undefined,
  settings: undefined
};
