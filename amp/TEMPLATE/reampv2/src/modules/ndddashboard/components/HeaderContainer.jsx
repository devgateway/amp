import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Filters from './Filters';
import Settings from './Settings';
import Share from './Share';

export default class HeaderContainer extends Component {
  render() {
    const { onApplyFilters } = this.props;
    return (
      <div>
        <Filters onApplyFilters={onApplyFilters} />
        <Settings />
        <Share />
      </div>
    );
  }
}

HeaderContainer.propTypes = {
  onApplyFilters: PropTypes.func.isRequired
};
