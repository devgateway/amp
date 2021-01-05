import React, { Component } from 'react';
import './GenericTooltip.css';

class GenericToolTip extends Component {
  render() {
    const { titleLabel, color, values } = this.props;
    const headerStyle = { backgroundColor: color };
    return (
      <div className="generic-tooltip">
        <div className="tooltip-header" style={headerStyle}>
          {titleLabel}
        </div>
        {values && (
        <div className="inner">
          {values.map(v => (
            <div className="row" key={v.id}>
              <div className="col-md-12">
                {`${v.simpleLabel}`}
                <span
                  className="percentage">
                  {v.percentage}
                  %
                </span>
              </div>
            </div>
          ))}
        </div>
        )}
      </div>
    );
  }
}

export default GenericToolTip;
