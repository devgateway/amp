import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Filters from './Filters';
import Settings from './Settings';
import Share from './Share';

export default class HeaderContainer extends Component {
  render() {
    const { onApplyFilters, filters, dashboardId } = this.props;
    return (
      <div>
        <Filters onApplyFilters={onApplyFilters} dashboardId={dashboardId} />
        <Settings />
        <Share filters={filters} />
      </div>
    );
  }
}

HeaderContainer.propTypes = {
  onApplyFilters: PropTypes.func.isRequired,
  filters: PropTypes.object,
  dashboardId: PropTypes.number
};
