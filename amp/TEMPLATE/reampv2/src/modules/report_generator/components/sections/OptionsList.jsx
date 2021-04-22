import React, { Component } from 'react';
import PropTypes from 'prop-types';

export default class OptionsList extends Component {
  render() {
    const {
      title, tooltip, isRequired, children
    } = this.props;
    return (
      <div>
        <div className="option-list-title">
          {isRequired ? <span className="red_text">* </span> : null}
          <span>{title}</span>
          {tooltip ? (
            <img
              alt="info-icon"
              className="info-icon"
              src="/TEMPLATE/reamp/modules/admin/data-freeze-manager/styles/images/icon-information.svg" />
          ) : null}
        </div>
        <div className="option-list-content">
          {children}
        </div>
      </div>
    );
  }
}

OptionsList.propTypes = {
  title: PropTypes.string.isRequired,
  isRequired: PropTypes.bool,
  tooltip: PropTypes.string,
  children: PropTypes.object,
};

OptionsList.defaultProps = {
  isRequired: false,
  tooltip: null,
  children: undefined
};
