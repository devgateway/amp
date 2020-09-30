import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import MultiSelectionDropDown from './MultiSelectionDropDown';
import CountryCarousel from './CountryCarousel';

import './filters.css';
import {
    DASHBOARD_DEFAULT_MAX_YEAR_RANGE,
    DASHBOARD_DEFAULT_MIN_YEAR_RANGE, HOME_CHART,
    SECTORS_CHART
} from '../../../utils/constants';
import { EXTRA_INFO, GROUP_ID } from '../../../utils/FieldsConstants';
import { SSCTranslationContext } from '../../StartUp';

class HorizontalFilters extends Component {

    getCategoryForCountry(country) {
        if (country && country[EXTRA_INFO]) {
            return country[EXTRA_INFO][GROUP_ID];
        } else {
            return null;
        }

    }

    generateCountriesCategories(countries) {
        const categories = [...new Set(countries.map(c => c[EXTRA_INFO][GROUP_ID]))];
        return this.props.filters.countries.countries.filter(c => {
            return categories.includes(c.id);
        }).map(c => {
            const category = {};
            category.id = c.id;
            category.name = c.description;
            category.tooltip = c.name;
            return category;
        });
    }

    clearFilters(event) {
        this.props.handleSelectedFiltersChange.handleSelectedCountryChanged([]);
        this.props.handleSelectedFiltersChange.handleSelectedSectorChanged([]);
        this.props.handleSelectedFiltersChange.handleSelectedModalityChanged([]);
        this.props.handleSelectedFiltersChange.handleSelectedYearChanged([]);
    }

    render() {
        const {translations} = this.context;
        const years = [];
        const {selectedYears = [], selectedCountries = [], selectedSectors = [], selectedModalities = []} = this.props.selectedFilters;
        const {sectors} = this.props.filters.sectors;
        let {countries} = this.props.filters.countries;
        const {modalities} = this.props.filters.modalities;
        let categoriesSelection = [];
        if (countries.length > 0 && this.props.filtersRestrictions.countriesWithData.length > 0) {
            //countries = countries.filter(c => this.props.filtersRestrictions.countriesWithData.includes(c.id));
            categoriesSelection = this.generateCountriesCategories(countries);
        }
        const {
            handleSelectedYearChanged,
            handleSelectedCountryChanged,
            handleSelectedSectorChanged,
            handleSelectedModalityChanged
        } = this.props.handleSelectedFiltersChange;
        this._generateYearsFilters(years);
        return (
            <div className="h-filter-wrapper">
                {this.props.chartSelected === SECTORS_CHART &&
                <div className="carousel-filters-wrapper" id={"country-accordion-filter"}>
                    <div className="col-md-6">
                        <CountryCarousel options={countries} onChange={handleSelectedCountryChanged}
                                         selectedOptions={selectedCountries}
                        />
                    </div>
                    <div className="col-md-2 country-year-search-wrapper first-element dropdown"
                         id={"country-accordion-filter"}>
                        <MultiSelectionDropDown options={countries}
                                                filterName='amp.ssc.dashboard:search-by-country'
                                                filterId='ddCountryCarousel'
                                                parentId="country-accordion-filter"
                                                categoryFetcher={this.getCategoryForCountry}
                                                sortData={true}
                                                categoriesSelection={categoriesSelection}
                                                selectedOptions={selectedCountries}
                                                onChange={handleSelectedCountryChanged}

                        />
                    </div>
                    <div className="col-md-2 country-year-search-wrapper dropdown" id={"country-accordion-filter"}>
                        <MultiSelectionDropDown options={years}
                                                selectedOptions={selectedYears}
                                                filterName='amp.ssc.dashboard:search-by-year'
                                                filterId='ddYearCarousel'
                                                parentId="country-accordion-filter"
                                                onChange={handleSelectedYearChanged}
                                                columnsCount={2}
                        />

                    </div>
                    <div className="reset col-md-2">
                        <div>
                            <button className="btn btn-primary"
                                    type="button"
                                    onClick={this.clearFilters.bind(this)}>{translations['amp.ssc.dashboard:reset']}</button>
                        </div>
                    </div>
                </div>
                }
                {this.props.chartSelected === HOME_CHART &&
                <div className="row inner">
                    <div id="accordion-filter" className="home-filter">
                        <div className="col-md-3" id="accordion-filter">
                            <MultiSelectionDropDown options={countries}
                                                    filterName='amp.ssc.dashboard:Country'
                                                    filterId='ddCountry'
                                                    parentId="accordion-filter"
                                                    categoryFetcher={this.getCategoryForCountry}
                                                    sortData={true}
                                                    categoriesSelection={categoriesSelection}
                                                    selectedOptions={selectedCountries}
                                                    onChange={handleSelectedCountryChanged}

                            /></div>
                        <div className="col-md-3" id="accordion-filter">
                            <MultiSelectionDropDown options={sectors}
                                                    filterName='amp.ssc.dashboard:Sector'
                                                    filterId='ddSector'
                                                    parentId="accordion-filter"
                                                    selectedOptions={selectedSectors}
                                                    onChange={handleSelectedSectorChanged}
                            />
                        </div>
                        <div className="col-md-2" id="accordion-filter">
                            <MultiSelectionDropDown options={modalities}
                                                    selectedOptions={selectedModalities}
                                                    filterName='amp.ssc.dashboard:Modalities'
                                                    filterId='ddModalities'
                                                    parentId="accordion-filter"
                                                    onChange={handleSelectedModalityChanged}
                            /></div>

                        <div className="col-md-2" id="accordion-filter">
                            <MultiSelectionDropDown options={years}
                                                    selectedOptions={selectedYears}
                                                    filterName='amp.ssc.dashboard:Year'
                                                    filterId='ddYear'
                                                    parentId="accordion-filter"
                                                    onChange={handleSelectedYearChanged}
                                                    columnsCount={2}
                            /></div>
                        <div className="reset col-md-2">
                            <button className="btn btn-primary"
                                    type="button"
                                    onClick={this.clearFilters.bind(this)}>{translations['amp.ssc.dashboard:reset']}</button>
                        </div>
                    </div>
                </div>
                }
            </div>
        );
    }

    _generateYearsFilters(years) {
        const {settings, settingsLoaded} = this.props.settings;
        if (settingsLoaded) {
            const minYear = parseInt(settings[DASHBOARD_DEFAULT_MIN_YEAR_RANGE]);
            const maxYear = parseInt(settings[DASHBOARD_DEFAULT_MAX_YEAR_RANGE]);
            for (let i = maxYear; i >= minYear; i--) {
                years.push({id: i, name: i});
            }
        }
    }
}

const mapStateToProps = state => {
    return {
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
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(HorizontalFilters);
HorizontalFilters
    .contextType = SSCTranslationContext;
