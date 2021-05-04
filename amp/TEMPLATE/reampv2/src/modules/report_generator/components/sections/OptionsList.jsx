import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';

export default class OptionsList extends Component {
  render() {
    const {
      title, tooltip, isRequired, children, className
    } = this.props;
    const tooltipText = tooltip ? (
      <Tooltip id={tooltip}>
        {tooltip}
      </Tooltip>
    ) : null;
    return (
      <>
        <div className="option-list-title">
          {isRequired ? <span className="red_text">* </span> : null}
          <span>{title}</span>
          {tooltip ? (
            <OverlayTrigger trigger={['hover', 'focus']} overlay={tooltipText}>
              <img
                alt="info-icon"
                className="info-icon"
                src="/TEMPLATE/reamp/modules/admin/data-freeze-manager/styles/images/icon-information.svg" />
            </OverlayTrigger>
          ) : null}
        </div>
        <div className={`option-list-content ${className || ''}`}>
          {children}
        </div>
      </>
    );
  }
}

OptionsList.propTypes = {
  title: PropTypes.string.isRequired,
  isRequired: PropTypes.bool,
  tooltip: PropTypes.string,
  children: PropTypes.object,
  className: PropTypes.string,
};

OptionsList.defaultProps = {
  isRequired: false,
  tooltip: null,
  children: undefined,
  className: null,
};
