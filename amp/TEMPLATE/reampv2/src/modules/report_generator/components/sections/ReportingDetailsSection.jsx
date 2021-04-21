import React, { Component } from 'react';
import PropTypes from 'prop-types';

export default class ReportingDetailSection extends Component {
  render() {
    const { visible } = this.props;
    return (
      <div className={!visible ? 'invisible-tab' : ''}>
        content 1
      </div>
    );
  }
}

ReportingDetailSection.propTypes = {
  visible: PropTypes.bool.isRequired
};
