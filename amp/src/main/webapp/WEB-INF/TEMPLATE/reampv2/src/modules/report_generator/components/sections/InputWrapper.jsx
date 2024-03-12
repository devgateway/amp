import * as React from 'react';
import PropTypes from 'prop-types';

export default class InputWrapper extends React.Component {
  handleKeyDown = (event) => {
    const { keyPress, keyEvent } = this.props;
    if (event.key === keyPress) {
      keyEvent(event);
    }
  }

  render() {
    const { children } = this.props;
    return <div onKeyDown={this.handleKeyDown}>{children}</div>;
  }
}

InputWrapper.propTypes = {
  children: PropTypes.object.isRequired,
  keyPress: PropTypes.string.isRequired,
  keyEvent: PropTypes.func.isRequired,
};
