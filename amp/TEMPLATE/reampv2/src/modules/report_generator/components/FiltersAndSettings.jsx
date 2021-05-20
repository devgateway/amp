import React, { Component } from 'react';
import { Segment } from 'semantic-ui-react';
import PropTypes from 'prop-types';
import Filters from './Filters';
import Settings from './Settings';

export default class FiltersAndSettings extends Component {
  render() {
    const { loading } = this.props;
    return (
      <>
        <Segment loading={loading} placeholder textAlign="left" className="filters_segment">
          {!loading ? (
            <>
              <Filters onApplyFilters={() => {}} />
              <Settings onApplySettings={() => {}} />
            </>
          ) : null}
        </Segment>
      </>
    );
  }
}

FiltersAndSettings.propTypes = {
  loading: PropTypes.bool
};

FiltersAndSettings.defaultProps = {
  loading: false
};
