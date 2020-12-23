import React, { Component } from 'react';
import PropTypes from 'prop-types';
import './ToolTip.css';
import { NDDTranslationContext } from '../StartUp';
class ToolTip extends Component {
  render() {
    const { titleLabel, color, value, formattedValue, id, total } = this.props;
    const { translations } = this.context;
    const percentage = (value * 100) / total;
    const headerStyle = { backgroundColor: color };
    return (
      <div className="generic-tooltip">
        <div className="tooltip-header" style={headerStyle}>
          {titleLabel}
        </div>
        <div className="inner">
          <div className="row" key={id}>
            <div className="col-md-12">
              {formattedValue}
            </div>
            <div className="col-md-12">
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
  formattedValue: PropTypes.string.isRequired
};
ToolTip.contextType = NDDTranslationContext;

export default ToolTip;
