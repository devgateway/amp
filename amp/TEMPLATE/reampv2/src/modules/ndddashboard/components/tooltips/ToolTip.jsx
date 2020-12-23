import React, { Component } from 'react';
import PropTypes, { number } from 'prop-types';
import './ToolTip.css';
import { NDDTranslationContext } from '../StartUp';

class ToolTip extends Component {
  render() {
    const {
      titleLabel, color, value, formattedValue, currencyCode, total
    } = this.props;
    const { translations } = this.context;
    const percentage = (value * 100) / total;
    const headerStyle = { backgroundColor: color };
    return (
      <div className="generic-tooltip">
        <div className="tooltip-header" style={headerStyle}>
          {titleLabel}
        </div>
        <div className="inner">
          <div className="" >
            <div className="element">
              <span className="formattedValue">{formattedValue}</span>
              <span className="currency">{currencyCode}</span>
            </div>
            <div className="element grey">
              {`${percentage.toFixed(2)} % ${translations['amp.ndd.dashboard:of-total']}`}
            </div>
          </div>
        </div>
      </div>
    );
  }
}

ToolTip.propTypes = {
  titleLabel: PropTypes.string.isRequired,
  color: PropTypes.string.isRequired,
  value: PropTypes.number.isRequired,
  formattedValue: PropTypes.string.isRequired,
  currencyCode: PropTypes.string.isRequired,
  total: PropTypes.number.isRequired
};
ToolTip.contextType = NDDTranslationContext;

export default ToolTip;
