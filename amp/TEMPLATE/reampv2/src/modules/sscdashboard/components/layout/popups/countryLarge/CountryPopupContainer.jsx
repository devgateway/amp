import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { nanoid } from 'nanoid';
import CountryPopupOverlayTitle from './CountryPopupOverlayTitle';
import CountryPopupExport from './CountryPopupExport';
import CountryPopup from './CountryPopup';
import { calculateColumnCount } from '../../../../utils/Utils';
import { BOOTSTRAP_COLUMNS_COUNT, COUNTRY_COLUMN } from '../../../../utils/constants';
import { SSCTranslationContext } from '../../../StartUp';

export default class CountryPopupContainer extends Component {
  getLeft(length, r) {
    if (length === 1) {
      return 'left';
    } else {
      const upperLower = this.getUpperLower(length, r);
      return upperLower ? `${upperLower}-left` : '';
    }
  }

  getRight(length, r) {
    if (length === 1) {
      return 'right';
    } else {
      const upperLower = this.getUpperLower(length, r);
      return upperLower ? `${upperLower}-right` : '';
    }
  }

  // eslint-disable-next-line class-methods-use-this
  getUpperLower(length, r) {
    const firstRow = Math.min(1, length);
    const lastRow = Math.max(1, length);
    if (r === firstRow) {
      return 'upper';
    } else if (r === lastRow) {
      return 'lower';
    } else {
      return null;
    }
  }

  getCountryPopup(rows) {
    const { countriesForExportChanged, countriesForExport, chartSelected } = this.props;
    return rows.map((r, k) => {
      const columnCount = calculateColumnCount(r.length);
      const classCount = BOOTSTRAP_COLUMNS_COUNT / columnCount;
      const left = Math.min(1, r.length);
      const right = Math.max(1, r.length);
      return (
        <div
          id={`country-row${k}`}
          key={nanoid()}
          className={`row ${k % 2 === 0 && rows.length > 1 ? ' bottomBorder' : ''}`}>
          {r.map((c, i) => {
            // eslint-disable-next-line no-nested-ternary
            const borderClass = (i + 1) === left
              ? this.getLeft(rows.length, k + 1) : ((i + 1) === right
                ? this.getRight(rows.length, k + 1) : '');

            const lineClass = (i + 1) === 1 || (i + 1 === 2 && r.length > 2) ? 'line' : '';
            return (
              <div className={`chart-column col-md-${classCount}`} key={c.id} id={`${COUNTRY_COLUMN}${c.id}`}>
                <CountryPopup
                  project={c}
                  columnCount={columnCount}
                  chartSelected={chartSelected}
                  {...(columnCount > 1 ? { borderClass } : {})}
                  {...(columnCount > 1 ? { lineClass } : {})}
                  countriesForExportChanged={countriesForExportChanged}
                  countriesForExport={countriesForExport}
                />
              </div>
            );
          })}
        </div>
      );
    });
  }

  render() {
    const { translations } = this.context;
    const {
      rows, closeLargeCountryPopinAndClearFilter,
      columnCount, countriesForExport, countriesForExportChanged, getExportData,
      chartSelected, countriesMessage, updateCountriesMessage
    } = this.props;
    return (
      <div>
        <CountryPopupOverlayTitle countriesMessage={countriesMessage} updateCountriesMessage={updateCountriesMessage} />
        <CountryPopupExport
          closeLargeCountryPopinAndClearFilter={closeLargeCountryPopinAndClearFilter}
          onlyOneCountry={columnCount === 1}
          printTitle={translations['amp.ssc.dashboard:Sector-Analysis']}
          printFilters={[]}
          printChartId="countries-charts"
          countriesForExport={countriesForExport}
          countriesForExportChanged={countriesForExportChanged}
          getExportData={getExportData}
          chartSelected={chartSelected}
        />
        <div className="countries-charts" id="countries-charts">
          {this.getCountryPopup(rows)}
        </div>
      </div>
    );
  }
}
CountryPopupContainer.contextType = SSCTranslationContext;
CountryPopupContainer.propTypes = {
  countriesForExportChanged: PropTypes.func.isRequired,
  countriesForExport: PropTypes.array.isRequired,
  chartSelected: PropTypes.string.isRequired,
  countriesMessage: PropTypes.bool,
  updateCountriesMessage: PropTypes.func.isRequired,
  rows: PropTypes.array.isRequired,
  closeLargeCountryPopinAndClearFilter: PropTypes.func.isRequired,
  getExportData: PropTypes.func.isRequired,
  columnCount: PropTypes.number.isRequired
};
CountryPopupContainer.defaultProps = {
  countriesMessage: false
};
