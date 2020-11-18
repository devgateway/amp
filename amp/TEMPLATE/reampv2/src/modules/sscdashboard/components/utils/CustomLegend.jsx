import React, { Component } from 'react';
import './customLegend.css';
import { COLOR_MAP, SECTOR_COLOR_MAP } from '../../utils/constants';

export default class CustomLegend extends Component {
  render() {
    const { data, chartSelected } = this.props;
    return (
      <div className="custom-legend">
        <div className="custom-legend-inner">
          <ul className={data.length > 3 ? 'two-rows' : ''}>
            {data.map(d => (
              <li key={COLOR_MAP.get(chartSelected).get(d.code)}>
                <div className="row">
                  <div className="col-md-1 col-xs-1">
                    <span
                      className="symbol"
                      style={{ border: `2px solid ${COLOR_MAP.get(chartSelected).get(d.code)}` }}/>
                  </div>
                  <div className="col-md-9 col-xs-9 label">
                    {d.simpleLabel}
                    <span
                      className="label percentage">
                      {`${d.percentage}%`}
                    </span>
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
