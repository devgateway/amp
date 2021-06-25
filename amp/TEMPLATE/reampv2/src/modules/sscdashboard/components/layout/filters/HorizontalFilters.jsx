import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import MultiSelectionDropDown from './MultiSelectionDropDown';
import CountryCarousel from './CountryCarousel';

import './filters.css';
import {
  DOWNLOAD_CHART, HOME_CHART, MODALITY_CHART,
  SECTORS_CHART
} from '../../../utils/constants';
import { EXTRA_INFO, GROUP_ID } from '../../../utils/FieldsConstants';
import { SSCTranslationContext } from '../../StartUp';

import { generateYearsFilters, getCategoryForCountry } from '../../../utils/Utils';

class HorizontalFilters extends Component {
// TODO move to utility file
  generateCountriesCategories(countries) {
    const categories = [...new Set(countries.map(c => c[EXTRA_INFO][GROUP_ID]))];
    const { filters } = this.props;
    return filters.countries.countries.filter(c => categories.includes(c.id)).map(c => {
      const category = {};
      category.id = c.id;
      category.name = c.description;
      category.tooltip = c.name;
      return category;
    });
  }

  clearFilters() {
    const { handleSelectedFiltersChange } = this.props;
    handleSelectedFiltersChange.handleSelectedCountryChanged([]);
    handleSelectedFiltersChange.handleSelectedSectorChanged([]);
    handleSelectedFiltersChange.handleSelectedModalityChanged([]);
    handleSelectedFiltersChange.handleSelectedYearChanged([]);
  }

  render() {
    const { translations } = this.context;
    const {
      chartSelected, settings, filters, selectedFilters, filtersRestrictions, handleSelectedFiltersChange
    } = this.props;
    const years = [];
    const {
      selectedYears, selectedCountries, selectedSectors, selectedModalities
    } = selectedFilters;
    const { sectors } = filters.sectors;
    let { countries } = filters.countries;
    const { modalities } = filters.modalities;
    let categoriesSelection = [];
    if (countries.length > 0 && filtersRestrictions.countriesWithData.length > 0) {
      countries = countries.filter(c => filtersRestrictions.countriesWithData.includes(c.id));
      categoriesSelection = this.generateCountriesCategories(countries);
    }
    const {
      handleSelectedYearChanged,
      handleSelectedCountryChanged,
      handleSelectedSectorChanged,
      handleSelectedModalityChanged
    } = handleSelectedFiltersChange;
    generateYearsFilters(years, settings);
    const additionalCss = chartSelected === DOWNLOAD_CHART ? ' disable-filter' : '';
    return (
      <div className="h-filter-wrapper">
        {(chartSelected === SECTORS_CHART || chartSelected === MODALITY_CHART || chartSelected === DOWNLOAD_CHART)
        && (
          <div className="carousel-filters-wrapper" id="country-accordion-filter">
            <div className={`col-md-6 ${additionalCss}`}>
              <CountryCarousel
                options={countries}
                onChange={handleSelectedCountryChanged}
                selectedOptions={selectedCountries}
              />
            </div>
            <div
              className="col-md-6 country-year-search-wrapper first-element dropdown"
              id="country-accordion-filter">
              <div className="wide-dropdown">
                <MultiSelectionDropDown
                  options={countries}
                  filterName="amp.ssc.dashboard:search-by-country"
                  filterId="ddCountryCarousel"
                  parentId="country-accordion-filter"
                  categoryFetcher={getCategoryForCountry}
                  sortData
                  categoriesSelection={categoriesSelection}
                  selectedOptions={selectedCountries}
                  onChange={handleSelectedCountryChanged}
                  selectionLimit={6}
                  disabled={chartSelected === DOWNLOAD_CHART}
                />
              </div>
              <div className="wide-dropdown">
                <MultiSelectionDropDown
                  options={years}
                  selectedOptions={selectedYears}
                  filterName="amp.ssc.dashboard:search-by-year"
                  filterId="ddYearCarousel"
                  parentId="country-accordion-filter"
                  onChange={handleSelectedYearChanged}
                  disabled={chartSelected === DOWNLOAD_CHART}
                />
              </div>
              <div className="wide-dropdown">
                <button
                  className="btn btn-primary"
                  type="button"
                  onClick={this.clearFilters.bind(this)}>
                  {translations['amp.ssc.dashboard:reset']}
                </button>
              </div>
          </div>
          </div>
        )}
        {chartSelected === HOME_CHART
        && (
          <div className="row inner">
            <div id="accordion-filter" className="home-filter">
              <div className="col-md-3" id="accordion-filter">
                <MultiSelectionDropDown
                  options={countries}
                  filterName="amp.ssc.dashboard:Country"
                  filterId="ddCountry"
                  parentId="accordion-filter"
                  categoryFetcher={getCategoryForCountry}
                  sortData
                  categoriesSelection={categoriesSelection}
                  selectedOptions={selectedCountries}
                  onChange={handleSelectedCountryChanged}

                />
              </div>
              <div className="col-md-3" id="accordion-filter">
                <MultiSelectionDropDown
                  options={sectors}
                  filterName="amp.ssc.dashboard:Sector"
                  filterId="ddSector"
                  parentId="accordion-filter"
                  selectedOptions={selectedSectors}
                  onChange={handleSelectedSectorChanged}
                />
              </div>
              <div className="col-md-2" id="accordion-filter">
                <MultiSelectionDropDown
                  options={modalities}
                  selectedOptions={selectedModalities}
                  filterName="amp.ssc.dashboard:Modalities"
                  filterId="ddModalities"
                  parentId="accordion-filter"
                  onChange={handleSelectedModalityChanged}
                />
              </div>

              <div className="col-md-2" id="accordion-filter">
                <MultiSelectionDropDown
                  options={years}
                  selectedOptions={selectedYears}
                  filterName="amp.ssc.dashboard:Year"
                  filterId="ddYear"
                  parentId="accordion-filter"
                  onChange={handleSelectedYearChanged}
                  columnsCount={2}
                />
              </div>
              <div className="reset col-md-2">
                <button
                  className="btn btn-primary"
                  type="button"
                  onClick={this.clearFilters.bind(this)}>
                  {translations['amp.ssc.dashboard:reset']}
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    );
  }
}

const mapStateToProps = state => ({
  settings: {
    settings: state.startupReducer.settings,
    settingsLoaded: state.startupReducer.settingsLoaded
  },
  filters: {
    sectors: {
      sectors: state.filtersReducer.sectors,
      sectorsLoaded: state.filtersReducer.sectorsLoaded
    },
    countries: {
      countries: state.filtersReducer.countries,
      countriesLoaded: state.filtersReducer.countriesLoaded
    },
    modalities: {
      modalities: state.filtersReducer.modalities,
      modalitiesLoaded: state.filtersReducer.modalitiesLoaded
    },

  }
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(HorizontalFilters);
HorizontalFilters
  .contextType = SSCTranslationContext;
HorizontalFilters.propTypes = {
  handleSelectedFiltersChange: PropTypes.object.isRequired,
  filters: PropTypes.object.isRequired,
  settings: PropTypes.object.isRequired,
  selectedFilters: PropTypes.shape({
    selectedYears: PropTypes.array,
    selectedCountries: PropTypes.array,
    selectedSectors: PropTypes.array,
    selectedModalities: PropTypes.array
  }),
  chartSelected: PropTypes.string.isRequired,
  filtersRestrictions: PropTypes.object.isRequired
};
HorizontalFilters.defaultProps = {
  selectedFilters: {
    selectedYears: [],
    selectedCountries: [],
    selectedSectors: [],
    selectedModalities: [],
  }
};
