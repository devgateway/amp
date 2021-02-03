import React, { Component } from 'react';
import PropTypes from 'prop-types';
import './ToolTip.css';
import { NDDTranslationContext } from '../StartUp';
import { formatNumberWithSettings } from '../../utils/Utils';

class ToolTip extends Component {
  // eslint-disable-next-line class-methods-use-this
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
      titleLabel, color, value, formattedValue, total, minWidth, isYearTotal, globalSettings
    } = this.props;
    const { translations } = this.context;
    const percentage = total > 0 ? (value * 100) / total : 0;
    const headerStyle = { backgroundColor: color };
    const containerStyle = {};
    // Dont make it wider if the content doesnt need it.
    if (minWidth && this.getActualWidth(titleLabel) > minWidth) {
      containerStyle.minWidth = minWidth;
    }
    // Never cut the amounts.
    if (this.getActualWidth(formattedValue) > containerStyle.minWidth) {
      containerStyle.minWidth = this.getActualWidth(formattedValue);
    }
    return (
      <div className="generic-tooltip" style={containerStyle}>
        <div className="tooltip-header" style={headerStyle}>
          {titleLabel}
        </div>
        <div className="inner">
          <div className="">
            <div className="element">
              <span className="formattedValue">
                {formattedValue}
              </span>
            </div>
            {percentage > 0 ? (
              <div className="element grey">
                <span className="of-total">
                  {formatNumberWithSettings('', translations, globalSettings,
                    percentage, false)}
                  <b>% </b>
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
  formattedValue: PropTypes.object.isRequired,
  total: PropTypes.number.isRequired,
  minWidth: PropTypes.number,
  isYearTotal: PropTypes.bool,
  globalSettings: PropTypes.object.isRequired
};

ToolTip.defaultProps = {
  minWidth: null,
  isYearTotal: false,
};

ToolTip.contextType = NDDTranslationContext;

export default ToolTip;
