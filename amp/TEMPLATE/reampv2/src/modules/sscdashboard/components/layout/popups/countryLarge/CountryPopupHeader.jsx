import React, {Component} from 'react';
import '../popups.css';
import ReactTooltip from 'react-tooltip';
import PropTypes from 'prop-types';
import {SSCTranslationContext} from '../../../StartUp';
import {SECTOR_MODALITY_LEADING_LEFT, SECTOR_MODALITY_MIN_SIZE, SECTORS_CHART} from '../../../../utils/constants';
import {calculateUpdatedValuesForDropDowns} from '../../../../utils/Utils';
import CountryFlag from '../../../utils/CountryFlag';

class CountryPopupHeader extends Component {
  onCountrySelected(e) {
    const ipSelectedCountry = parseInt(e.target.value, 10);
    const { countriesForExport, countriesForExportChanged } = this.props;
    countriesForExportChanged(calculateUpdatedValuesForDropDowns(ipSelectedCountry, countriesForExport));
  }

  render() {
    const { translations } = this.context;
    const {
      country, projectByGroupings, columnCount, countriesForExport, chartSelected
    } = this.props;
    return (
      country ? (
        <div className="header row">
          <div className={`col-md-${columnCount === 1 ? '4' : '5'}  col-xs-5 country-name`}>
            <CountryFlag countryName={country.name} />
            {country.name}
          </div>
          <div className={`col-md-${columnCount === 1 ? '2' : '3'} col-xs-3 projects`}>
            <span
              className="count">
              {projectByGroupings.uniqueProjects.size.toString()
                .padStart(SECTOR_MODALITY_MIN_SIZE, SECTOR_MODALITY_LEADING_LEFT)}
            </span>
            <span className="label">{translations['amp.ssc.dashboard:sectors-project']}</span>
          </div>
          <div className={`col-md-${columnCount === 1 ? '3' : '4'} col-xs-4 projects`}>
            <span
              className="count">
              {projectByGroupings.groupings.length.toString()
                .padStart(SECTOR_MODALITY_MIN_SIZE, SECTOR_MODALITY_LEADING_LEFT)}
            </span>
            <span
              className="label">
              {translations[`amp.ssc.dashboard:${chartSelected === SECTORS_CHART
                ? 'sectors-sector' : 'modalities-modality'}-types`]}
            </span>
            {columnCount !== 1 && (
              <div className="export-checkbox">
                <div className="custom-checkbox">
                  <input
                    type="checkbox"
                    id={`chk-country-column-${country.id}`}
                    value={country.id}
                    onClick={this.onCountrySelected.bind(this)}
                    checked={countriesForExport.includes(country.id)} />
                  <label
                    htmlFor={`chk-country-column-${country.id}`}
                    data-tip={translations['amp.ssc.dashboard:sectors-checkbox-tooltip']}
                    data-for="download-checkbox" />
                </div>
              </div>
            )}
          </div>
          <ReactTooltip
            place="bottom"
            multiline
            id="download-checkbox"
            className="download-checkbox-tooltip" />
        </div>
      ) : null
    );
  }
}

CountryPopupHeader.contextType = SSCTranslationContext;
CountryPopupHeader.propTypes = {
  columnCount: PropTypes.number.isRequired,
  countriesForExport: PropTypes.array.isRequired,
  projectByGroupings: PropTypes.object.isRequired,
  countriesForExportChanged: PropTypes.func.isRequired,
  country: PropTypes.object.isRequired,
  chartSelected: PropTypes.string.isRequired
};

export default CountryPopupHeader;
