import React, { Component } from 'react';
import PropTypes from 'prop-types';

export default class ErrorMessage extends Component {
  render() {
    const { visible, message } = this.props;
    return (
      <div className={!visible ? 'invisible-tab' : 'red_text'}>
        {message}
      </div>
    );
  }
}
ErrorMessage.propTypes = {
  visible: PropTypes.bool.isRequired,
  message: PropTypes.string.isRequired,
};
