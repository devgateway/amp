import React, { Component } from 'react';
import EllipsisText from 'react-ellipsis-text';
import PropTypes from 'prop-types';
import { formatOnlyNumber } from '../../modules/ndddashboard/utils/Utils';

export default class CustomLegend extends Component {
  render() {
    const {
      data, colorMap, shouldSplitBig, formatter, translations, settings, currency
    } = this.props;
    return (
      <div className="custom-legend">
        <div className="custom-legend-inner">
          <ul className={data.length > 3 && shouldSplitBig ? 'two-rows' : ''}>
            {data.map(d => (
              <li key={colorMap.get(d.code)}>
                <div className="row row-eq-height" >
                  <div className="col-md-1 col-xs-1 symbol-container">
                    <span
                      className="symbol"
                      style={{
                        border: `2px solid ${colorMap.get(d.code)}`,
                        backgroundColor: `${colorMap.get(d.code)}`
                      }} />
                  </div>
                  <div className="col-md-7 col-xs-7 label">
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
                  </div>
                  {d.amount
                  && (
                    <div className="col-md-4 col-xs-4 label vertical-center" style={{ textAlign: 'right' }}>
                      <span
                        className="label amount">
                        {/* eslint-disable-next-line no-nested-ternary */}
                        {translations && settings && currency
                          ? formatOnlyNumber(settings, d.amount)
                          : formatter
                            ? formatter.format(d.amount)
                            : d.amount}
                      </span>
                    </div>
                  )}
                </div>
              </li>
            ))}
          </ul>
        </div>
      </div>
    );
  }
}
CustomLegend.propTypes = {
  data: PropTypes.array.isRequired,
  colorMap: PropTypes.object.isRequired,
  shouldSplitBig: PropTypes.bool,
  formatter: PropTypes.object,
  translations: PropTypes.object,
  settings: PropTypes.object,
  currency: PropTypes.string
};

CustomLegend.defaultProps = {
  shouldSplitBig: false,
  formatter: null,
  translations: null,
  settings: null,
  currency: null
};
