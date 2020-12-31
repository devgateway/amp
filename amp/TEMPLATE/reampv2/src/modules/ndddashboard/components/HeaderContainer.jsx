import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Filters from './Filters';
import Settings from './Settings';
import Share from './Share';

export default class HeaderContainer extends Component {
  render() {
    const {
      onApplyFilters, filters, dashboardId, onApplySettings, globalSettings
    } = this.props;
    return (
      <div>
        <Filters onApplyFilters={onApplyFilters} dashboardId={dashboardId} globalSettings={globalSettings} />
        <Settings onApplySettings={onApplySettings} />
        <Share filters={filters} />
      </div>
    );
  }
}

HeaderContainer.propTypes = {
  onApplyFilters: PropTypes.func.isRequired,
  filters: PropTypes.object,
  dashboardId: PropTypes.number,
  onApplySettings: PropTypes.func.isRequired,
  globalSettings: PropTypes.object.isRequired
};
