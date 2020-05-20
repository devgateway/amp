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
        this.countriesWithData = [];
        this.state = {
            filteredProjects: [],
            countriesWithData: [],
            selectedFilters: {
                selectedYears: [],
                selectedCountries: [],
                selectedSectors: []
            }
        };
    }

    componentDidMount(): void {
        const initialData = this.getFilteredData();
        this.countriesWithData = initialData.map(c => c.countryId);
        this.setState({
            filteredProjects: initialData
        });
    }

    handleSelectedSectorChanged(pSelectedSectors) {
        this.updateFilterState('selectedSectors', pSelectedSectors);
    }

    handleSelectedYearChanged(pSelectedYears) {
        this.updateFilterState('selectedYears', pSelectedYears);
    }

    handleSelectedCountryChanged(pSelectedCountries) {
        this.updateFilterState('selectedCountries', pSelectedCountries);
    }

    updateFilterState(filterSelector, updatedSelectedFilters) {
        this.setState((currentState) => {
            const selectedFilters = {...currentState.selectedFilters};
            selectedFilters[filterSelector] = updatedSelectedFilters;
            return {selectedFilters};
        }, this.getFilteredProjects);
    }

    getFilteredProjects() {
        this.setState({filteredProjects: this.getFilteredData()});
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
        return filteredData;
    }


    render() {
        // TODO countries are loaded
        const {countries} = this.props.filters.countries;
        const filtersRestrictions = {countriesWithData: this.countriesWithData};

        return (
            <div className="col-md-10 col-md-offset-2 map-wrapper">
                <HorizontalFilters selectedFilters={this.state.selectedFilters}
                                   filtersRestrictions={filtersRestrictions}
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

