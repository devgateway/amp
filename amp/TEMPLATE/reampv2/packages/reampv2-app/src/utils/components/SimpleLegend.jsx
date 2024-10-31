import React, { Component } from 'react';
import ReactTooltip, { Tooltip } from 'react-tooltip';
import PropTypes from 'prop-types';
import './SimpleLegend.css';

export default class SimpleLegend extends Component {
  render() {
    const {
      data, getColor
    } = this.props;
    let index = 0;
    return (
      <div className="simple-legend">
        <div className="simple-legend-inner">
          <ul>
            {data.map(d => {
              d.index = index;
              const ret = (
                <li key={getColor(d)}>
                  <span
                    className="symbol"
                    style={{
                      border: `2px solid ${getColor(d)}`,
                      backgroundColor: `${getColor(d)}`
                    }}
                    data-tooltip-content={d.name}
                    data-tooltip-id={index.toString()}
                    data-tooltip-place="top"

                  >

                  </span>
                  <span className="label"
                        data-tooltip-content={d.name}
                        data-tooltip-id={index.toString()}
                        data-tooltip-place="top"
                        style={{cursor: 'pointer'}}
                  >
                    {d.name.substring(0, 10)}
                    <Tooltip
                        place="top"
                        effect="float"
                        style={{
                          backgroundColor: `${getColor(d)}`
                        }}
                        id={index.toString()} />
                  </span>
                </li>
              );
              index += 1;
              return ret;
            })}
          </ul>
        </div>
      </div>
    );
  }
}
SimpleLegend.propTypes = {
  data: PropTypes.array.isRequired,
  shouldSplitBig: PropTypes.bool,
  formatter: PropTypes.func,
  getColor: PropTypes.func
};
SimpleLegend.defaultProps = {
  shouldSplitBig: false,
  formatter: null,
  getColor: null
};
