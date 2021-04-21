import React, { Component } from 'react';
import PropTypes from 'prop-types';

export default class MeasuresSection extends Component {
  render() {
    const { visible } = this.props;
    return (
      <div className={!visible ? 'invisible-tab' : ''}>
        content 3
      </div>
    );
  }
}

MeasuresSection.propTypes = {
  visible: PropTypes.bool.isRequired
};
