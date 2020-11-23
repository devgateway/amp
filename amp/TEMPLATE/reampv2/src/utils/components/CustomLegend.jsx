import React, { Component } from 'react';
import EllipsisText from 'react-ellipsis-text';

export default class CustomLegend extends Component {

  render() {
    const {
      data, colorMap, shouldSplitBig, formatter
    } = this.props;
    return (
      <div className="custom-legend">
        <div className="custom-legend-inner">
          <ul className={data.length > 3 && shouldSplitBig ? 'two-rows' : ''}>
            {data.map(d => (
              <li key={colorMap.get(d.code)}>
                <div className="row">
                  <div className="col-md-1 col-xs-1">
                    <span
                      className="symbol"
                      style={{
                        border: `2px solid ${colorMap.get(d.code)}`,
                        backgroundColor: `${colorMap.get(d.code)}`
                      }} />
                  </div>
                  <div className="col-md-11 col-xs-11 label">
                    <EllipsisText
                      text={d.simpleLabel}
                      length={100}
                      tail="" />
                    {d.percentage
                    && (
                      <span
                        className="label percentage">
                        {`${d.percentage}%`}
                      </span>
                    )}
                    {d.amount
                    && (
                      <span
                        className="label amount">
                        {formatter ? formatter.format(d.amount) : d.amount}
                      </span>
                    )}
                  </div>
                </div>
              </li>
            ))}
          </ul>
        </div>
      </div>
    );
  }
}
