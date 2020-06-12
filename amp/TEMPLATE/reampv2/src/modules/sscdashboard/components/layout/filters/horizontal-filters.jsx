import React, { Component } from 'react';
import MultiSelectionDropDown from './MultiSelectionDropDown';
import CountryCarousel from './carousel';
import CountrySearch from './country-search';
import './filters.css';
import { DASHBOARD_DEFAULT_MAX_YEAR_RANGE, DASHBOARD_DEFAULT_MIN_YEAR_RANGE } from '../../../utils/constants';
import { EXTRA_INFO, GROUP_ID } from '../../../utils/FieldsConstants';
import { bindActionCreators } from 'redux';
import { loadSectorsFilters, loadCountriesFilters, loadModalitiesFilters } from '../../../actions/loadFilters';
import { connect } from 'react-redux';

class HorizontalFilters extends Component {

    componentDidMount(): void {
        this.props.loadSectorsFilters();
        this.props.loadCountriesFilters();
        this.props.loadModalitiesFilters();
    }

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

    render() {
        const years = [];
        const {selectedYears = [], selectedCountries = [], selectedSectors = [], selectedModalities = []} = this.props.selectedFilters;
        const {sectors} = this.props.filters.sectors;
        let {countries} = this.props.filters.countries;
        const {modalities} = this.props.filters.modalities;
        let categoriesSelection = [];
        if (countries.length > 0 && this.props.filtersRestrictions.countriesWithData.length > 0) {
            countries = countries.filter(c => this.props.filtersRestrictions.countriesWithData.includes(c.id));
            categoriesSelection = this.generateCountriesCategories(countries);
        }

        this._generateYearsFilters(years);

        return (
            <div className="h-filter-wrapper">
                <div className="carousel-filters-wrapper" style={{display: "none"}}>
                    <CountryCarousel/>
                    <CountrySearch/>
                </div>
                <div className="row inner">
                    <div id="accordion-filter">
                        <div className="col-md-3" id="accordion-filter">
                            <MultiSelectionDropDown options={countries}
                                                    filterName='amp.ssc.dashboard:Country'
                                                    filterId='ddCountry'
                                                    parentId="accordion-filter"
                                                    categoryFetcher={this.getCategoryForCountry}
                                                    sortData={true}
                                                    categoriesSelection={categoriesSelection}
                                                    selectedOptions={selectedCountries}
                                                    onChange={this.props.handleSelectedCountryChanged}

                            /></div>
                        <div className="col-md-3" id="accordion-filter">
                            <MultiSelectionDropDown options={sectors}
                                                    filterName='amp.ssc.dashboard:Sector'
                                                    filterId='ddSector'
                                                    parentId="accordion-filter"
                                                    selectedOptions={selectedSectors}
                                                    onChange={this.props.handleSelectedSectorChanged}
                            />
                        </div>
                        <div className="col-md-4" id="accordion-filter">
                            <MultiSelectionDropDown options={modalities}
                                                    selectedOptions={selectedModalities}
                                                    filterName='amp.ssc.dashboard:Modalities'
                                                    filterId='ddModalities'
                                                    parentId="accordion-filter"
                                                    onChange={this.props.handleSelectedModalityChanged}
                                                    columnsCount={2}
                            /></div>

                        <div className="col-md-2" id="accordion-filter">
                            <MultiSelectionDropDown options={years}
                                                    selectedOptions={selectedYears}
                                                    filterName='amp.ssc.dashboard:Year'
                                                    filterId='ddYear'
                                                    parentId="accordion-filter"
                                                    onChange={this.props.handleSelectedYearChanged}
                                                    columnsCount={3}
                            /></div>
                    </div>
                </div>
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

const mapDispatchToProps = dispatch => bindActionCreators({
    loadSectorsFilters: loadSectorsFilters,
    loadCountriesFilters: loadCountriesFilters,
    loadModalitiesFilters: loadModalitiesFilters
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(HorizontalFilters);
