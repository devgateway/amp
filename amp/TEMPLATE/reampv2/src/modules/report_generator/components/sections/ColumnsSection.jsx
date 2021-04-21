import React, { Component } from 'react';
import PropTypes from 'prop-types';

export default class ColumnsSection extends Component {
  render() {
    const { visible } = this.props;
    return (
      <div className={!visible ? 'invisible-tab' : ''}>
        content 2
      </div>
    );
  }
}

ColumnsSection.propTypes = {
  visible: PropTypes.bool.isRequired
};
