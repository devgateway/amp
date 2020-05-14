import React, { Component } from 'react';
import HorizontalFilters from '../filters/horizontal-filters';
import './map.css';
import MapHome from "../../map/MapHome";
import CountryPopupOverlay from "../popups/popup-overlay";
import mapData from './mapData';
import { bindActionCreators } from 'redux';
import { loadCountriesFilters, loadSectorsFilters } from '../../../actions/loadFilters';
import { connect } from 'react-redux';

class MapContainer extends Component {
    //TODO once we implement side filters maybe we need to move state up
    constructor(props) {
        super();
        this.state = {
            filteredProjects: [],
            selectedFilters: {
                selectedYears: [],
                selectedCountries: [],
                selectedSectors: []
            }
        };
    }

    componentDidMount(): void {
        this.getFilteredData();
    }

    handleSelectedSectorChanged(pSelectedSector) {
        this.updateFilterState('selectedSectors', parseInt(pSelectedSector));
    }

    handleSelectedYearChanged(pSelectedYear) {
        this.updateFilterState('selectedYears', parseInt(pSelectedYear));
    }

    handleSelectedCountryChanged(pSelectedCountry) {
        this.updateFilterState('selectedCountries', parseInt(pSelectedCountry));
    }

    updateFilterState(filterSelector, ipSelectedFilter) {
        this.setState((currentState) => {
            const oneSelectedFilters = [...currentState.selectedFilters[filterSelector]]
            const selectedFilters = {...currentState.selectedFilters}
            if (oneSelectedFilters.includes(ipSelectedFilter)) {
                selectedFilters[filterSelector] = oneSelectedFilters.filter(sc => sc !== ipSelectedFilter);
            } else {
                oneSelectedFilters.push(ipSelectedFilter);
                selectedFilters[filterSelector] = oneSelectedFilters;
            }
            return {selectedFilters};
        }, this.getFilteredData);
    }


    getFilteredData() {
        const {selectedYears = [], selectedCountries = [], selectedSectors = []} = this.state.selectedFilters;
        const projects = mapData.countries;
        const filteredData = projects.filter(p => {
            if (selectedCountries.length === 0 || selectedCountries.includes(p.countryId)) {
                const sectors = p.sectors.filter(sector => {
                    if (selectedSectors.length === 0 || selectedSectors.includes(sector.sectorId)) {
                        if (selectedYears.length > 0) {
                            const filteredProjects = sector.projects.filter(p => selectedYears.includes(p.year));
                            if (filteredProjects.length === 0) {
                                return false;
                            } else {
                                sector.projects = filteredProjects;
                                return true;
                            }
                        } else {
                            return true;
                        }
                    } else {
                        return false;
                    }
                });
                if (sectors.length === 0) {
                    return false;
                }
                p.sectors = sectors;
                return true;
            } else {
                return false;
            }
        });

        this.setState({filteredProjects: filteredData});
    }


    render() {
        // TODO countries are loaded
        const {countries} = this.props.filters.countries;

        return (
            <div className="col-md-10 col-md-offset-2 map-wrapper">
                <HorizontalFilters selectedFilters={this.state.selectedFilters}
                                   handleSelectedYearChanged={this.handleSelectedYearChanged.bind(this)}
                                   handleSelectedCountryChanged={this.handleSelectedCountryChanged.bind(this)}
                                   handleSelectedSectorChanged={this.handleSelectedSectorChanged.bind(this)}
                />
                <MapHome filteredProjects={this.state.filteredProjects} countries={countries}/>
                {/* TODO commented out until we implement the popin
                <CountryPopupOverlay/>
                */}
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
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

export default connect(mapStateToProps, mapDispatchToProps)(MapContainer);

