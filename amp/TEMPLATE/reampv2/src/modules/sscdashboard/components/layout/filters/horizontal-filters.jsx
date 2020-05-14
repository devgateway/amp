import React, { Component } from 'react';
//import FilterCountries from './countries';
import MultiSelectionDropDown from './MultiSelectionDropDown';
import CountryCarousel from './carousel';
import CountrySearch from './country-search';
import './filters.css';
import { DASHBOARD_DEFAULT_MAX_YEAR_RANGE, DASHBOARD_DEFAULT_MIN_YEAR_RANGE } from '../../../utils/constants';
import { bindActionCreators } from 'redux';
import { loadSectorsFilters, loadCountriesFilters } from '../../../actions/loadFilters';
import { connect } from 'react-redux';

class HorizontalFilters extends Component {

    componentDidMount(): void {
        this.props.loadSectorsFilters();
        this.props.loadCountriesFilters();
    }

    render() {
        const years = [];
        const {selectedYears = [], selectedCountries = [], selectedSectors = []} = this.props.selectedFilters;
        const {sectors} = this.props.filters.sectors;
        const {countries} = this.props.filters.countries;
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
                                                    sortData={true}
                                                    selectedOptions={selectedCountries}
                                                    onChange={this.props.handleSelectedCountryChanged}
                            /></div>
                        <div className="col-md-4" id="accordion-filter">
                            <MultiSelectionDropDown options={sectors}
                                                    filterName='amp.ssc.dashboard:Sector'
                                                    filterId='ddSector'
                                                    parentId="accordion-filter"
                                                    selectedOptions={selectedSectors}
                                                    onChange={this.props.handleSelectedSectorChanged}
                            />
                        </div>
                        <div className="col-md-3" id="accordion-filter">
                            <MultiSelectionDropDown options={years}
                                                    selectedOptions={selectedYears}
                                                    filterName='amp.ssc.dashboard:Year'
                                                    filterId='ddYear'
                                                    parentId="accordion-filter"
                                                    onChange={this.props.handleSelectedYearChanged}
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
            }

        }
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    loadSectorsFilters: loadSectorsFilters,
    loadCountriesFilters: loadCountriesFilters
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(HorizontalFilters);
