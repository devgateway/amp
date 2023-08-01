import React from 'react';
import PropTypes from 'prop-types';
import './popups.css';
import CountryPopupContainer from './countryLarge/CountryPopupContainer';
import {calculateColumnCount} from '../../../utils/Utils';
import {SSCTranslationContext} from '../../StartUp';

const CountryPopupOverlay = ({
  show, projects, closeLargeCountryPopinAndClearFilter, countriesForExport,
  countriesForExportChanged, getExportData, chartSelected, countriesMessage,
  updateCountriesMessage
}) => {
  if (!show) {
    return null;
  }
  if (!projects || projects.length === 0) {
    return null;
  }
  const columnCount = calculateColumnCount(projects.length);
  const rows = [];
  rows.push(projects.slice(0, columnCount));
  const secondRow = projects.slice(columnCount, projects.length);
  if (secondRow.length > 0) {
    rows.push(secondRow);
  }
  return (
    <div className={`country-popup-wrapper${columnCount === 1 ? '' : ` country${columnCount}`}`}>
      <div className="container-fluid">
        <CountryPopupContainer
          countriesMessage={countriesMessage}
          updateCountriesMessage={updateCountriesMessage}
          rows={rows}
          columnCount={columnCount}
          closeLargeCountryPopinAndClearFilter={closeLargeCountryPopinAndClearFilter}
          countriesForExport={countriesForExport}
          countriesForExportChanged={countriesForExportChanged}
          getExportData={getExportData}
          chartSelected={chartSelected}
        />
      </div>
    </div>
  );
};

export default CountryPopupOverlay;
CountryPopupOverlay.contextType = SSCTranslationContext;
CountryPopupOverlay.propTypes = {
  show: PropTypes.bool.isRequired,
  projects: PropTypes.array.isRequired,
  closeLargeCountryPopinAndClearFilter: PropTypes.func.isRequired,
  countriesForExportChanged: PropTypes.func.isRequired,
  getExportData: PropTypes.func.isRequired,
  countriesForExport: PropTypes.array.isRequired,
  chartSelected: PropTypes.string.isRequired,
  countriesMessage: PropTypes.bool,
  updateCountriesMessage: PropTypes.func.isRequired
};
CountryPopupOverlay.defaultProps = {
  countriesMessage: false
};
