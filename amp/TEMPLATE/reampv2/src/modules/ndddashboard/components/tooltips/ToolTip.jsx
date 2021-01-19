import React, { Component } from 'react';
import PropTypes, { number } from 'prop-types';
import './ToolTip.css';
import { NDDTranslationContext } from '../StartUp';
import { formatNumberWithSettings } from '../../utils/Utils';

class ToolTip extends Component {
  getActualWidth(inputText) {
    const font = '16px times new roman';
    const canvas = document.createElement('canvas');
    const context = canvas.getContext('2d');
    context.font = font;
    const { width } = context.measureText(inputText);
    const formattedWidth = Math.ceil(width);
    return formattedWidth;
  }

  render() {
    const {
      titleLabel, color, value, formattedValue, currencyCode, total, minWidth, isYearTotal, globalSettings
    } = this.props;
    const { translations } = this.context;
    const percentage = total > 0 ? (value * 100) / total : 0;
    const headerStyle = { backgroundColor: color };
    const containerStyle = {};
    // Dont make it wider if the content doesnt need it.
    if (minWidth && this.getActualWidth(titleLabel) > minWidth) {
      containerStyle.minWidth = minWidth;
    }
    return (
      <div className="generic-tooltip" style={containerStyle}>
        <div className="tooltip-header" style={headerStyle}>
          {titleLabel}
        </div>
        <div className="inner">
          <div className="">
            <div className="element">
              <span className="formattedValue">{formattedValue}</span>
              <span className="currency">{currencyCode}</span>
            </div>
            {percentage > 0 ? (
              <div className="element grey">
                <span className="of-total">
                  <b>{`${formatNumberWithSettings(translations, globalSettings, percentage, false)}% `}</b>
                  {isYearTotal
                    ? translations['amp.ndd.dashboard:of-year-total']
                    : translations['amp.ndd.dashboard:of-total']}
                </span>
              </div>
            ) : null}
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
  total: PropTypes.number.isRequired,
  minWidth: PropTypes.string,
  isYearTotal: PropTypes.bool,
  globalSettings: PropTypes.object.isRequired
};
ToolTip.contextType = NDDTranslationContext;

export default ToolTip;
