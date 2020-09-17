import React, { Component } from 'react';
import Sidebar from './components/layout/sidebar/sidebar';
import MapContainer from './components/layout/map/MapContainer';
import { SSCTranslationContext } from './components/StartUp';
import { HOME_CHART, SECTORS_CHART } from './utils/constants';
import { DONOR_COUNTRY, MODALITIES, PRIMARY_SECTOR } from './utils/FieldsConstants';
import { bindActionCreators } from 'redux';
import { loadActivitiesDetails } from './actions/callReports';
import { connect } from 'react-redux';
import { loadCountriesFilters, loadModalitiesFilters, loadSectorsFilters } from './actions/loadFilters';
import './utils/print.css';
import PrintDummy from './utils/PrintDummy';

class SssDashboardHome extends Component {

    constructor(props) {
        super(props);
        this.countriesWithData = [];
        this.state = {
            countriesForExport: [],
            chartSelected: HOME_CHART,
            showEmptyProjects: false,
            showLargeCountryPopin: false,
            selectedFilters: {
                selectedYears: [],
                selectedCountries: [],
                selectedSectors: [],
                selectedModalities: []
            }
        };

    }


    countriesForExportChanged(countries) {
        this.setState({countriesForExport: countries});
    }

    componentDidMount() {

        this.props.loadSectorsFilters();
        this.props.loadCountriesFilters();
        this.props.loadModalitiesFilters();

        if (this.props.projects.activitiesLoaded) {
            this.getProjectsData();
        }
        const initialData = this.getFilteredData();

        this.setState({
            filteredProjects: initialData
        });
    }

    componentDidUpdate(prevProps, prevState) {

        if (this.props !== prevProps && this.countriesWithData.length === 0) {
            //TOD check
            if (this.props.projects.activitiesLoaded) {
                const initialData = this.getFilteredData();
                this.countriesWithData = initialData.map(c => c.id);
                const selectedYears = [];
                selectedYears.push(this.props.projects.activities.mostRecentYear);
                this.handleSelectedYearChanged(selectedYears);
                this.setState({
                    filteredProjects: initialData
                });
            }
        }
    }

    onChangeChartSelected(chartSelected) {
        this.setState({chartSelected});
        if (chartSelected !== SECTORS_CHART) {
            this.closeLargeCountryPopin();

        }
    }

    closeLargeCountryPopinAndClearFilter() {
        this.handleSelectedCountryChanged([]);
    }

    closeLargeCountryPopin() {
        this.setState({showLargeCountryPopin: false});
    }

    handleSelectedSectorChanged(pSelectedSectors) {
        this.updateFilterState('selectedSectors', pSelectedSectors);
    }

    handleSelectedYearChanged(pSelectedYears) {
        this.updateFilterState('selectedYears', pSelectedYears);
    }

    handleSelectedCountryChanged(pSelectedCountries) {
        //we only keep for export the countries that are selected
        this.setState(previousState => {
            const countriesForExport = [...previousState.countriesForExport].filter(c => pSelectedCountries.includes(c));
            return {countriesForExport};

        })
        if (this.state.chartSelected === SECTORS_CHART && pSelectedCountries && pSelectedCountries.length >= 1) {
            //currently we open large popin, in next tickets we will open also the popin for 2/3 countries selected
            this.setState({showLargeCountryPopin: true});
        } else {
            this.closeLargeCountryPopin();
        }
        this.updateFilterState('selectedCountries', pSelectedCountries);
    }

    handleSelectedModalityChanged(pSelectedModalities) {
        this.updateFilterState('selectedModalities', pSelectedModalities);
    }

    updateFilterState(filterSelector, updatedSelectedFilters) {
        this.setState((currentState) => {
            const selectedFilters = {...currentState.selectedFilters};
            selectedFilters[filterSelector] = updatedSelectedFilters;
            return {selectedFilters};
        }, this.getFilteredProjects);
    }

    getFilteredProjects() {
        const filteredProjects = this.getFilteredData();
        this.setState({filteredProjects});
        if (filteredProjects.length === 0) {
            this.setState({showEmptyProjects: true});
        } else {
            this.setState({showEmptyProjects: false});
        }

        const countryWithProjects = filteredProjects.map(p => p.id);
        const intersection = this.state.selectedFilters.selectedCountries.filter(c => countryWithProjects.includes(c));
        if (!intersection || intersection.length === 0) {
            this.closeLargeCountryPopin();
        }
    }

    getProjectsData() {
        this.props.loadActivitiesDetails(this.props.projects.activities.activitiesId);
    }

    onNoProjectsModalClose() {
        this.setState({showEmptyProjects: false});
    }

    getFilteredData() {
        //TODO see how we can simply or make a bit more generic this function
        const {selectedYears = [], selectedCountries = [], selectedSectors = [], selectedModalities = []} = this.state.selectedFilters;
        if (!this.props.projects.activitiesLoaded) {
            return [];
        }
        const clonedProjectsActivities = this.props.projects.activities[DONOR_COUNTRY].map(a => ({...a}));
        return clonedProjectsActivities.filter(p => {
            if (selectedCountries.length === 0 || selectedCountries.includes(p.id)) {
                const sectors = p[PRIMARY_SECTOR].filter(sector => {
                    if (selectedSectors.length === 0 || selectedSectors.includes(sector.id)) {
                        const modalities = sector[MODALITIES].filter(modality => {
                            if (selectedModalities.length === 0 || selectedModalities.includes(modality.id)) {
                                if (selectedYears.length > 0) {
                                    const filteredProjects = modality.activities.filter(p => selectedYears.includes(p.year));
                                    if (filteredProjects.length === 0) {
                                        return false;
                                    } else {
                                        modality.activities = filteredProjects;
                                        return true;
                                    }
                                } else {
                                    return true;
                                }
                            } else {
                                return false;
                            }
                        });
                        if (modalities.length === 0) {
                            return false;
                        } else {
                            sector[MODALITIES] = modalities;
                            return true;
                        }
                    } else {
                        return false;
                    }
                });
                if (sectors.length === 0) {
                    return false;
                }
                p[PRIMARY_SECTOR] = sectors;
                return true;
            } else {
                return false;
            }
        });
    }


    render() {
        const filtersRestrictions = {countriesWithData: this.countriesWithData};

        const handleSelectedFiltersChange = {
            handleSelectedModalityChanged: this.handleSelectedModalityChanged.bind(this),
            handleSelectedCountryChanged: this.handleSelectedCountryChanged.bind(this),
            handleSelectedYearChanged: this.handleSelectedYearChanged.bind(this),
            handleSelectedSectorChanged: this.handleSelectedSectorChanged.bind(this)
        };
        return (<div className="container-fluid content-wrapper">
                <div className="row">
                    <Sidebar chartSelected={this.state.chartSelected}
                             onChangeChartSelected={this.onChangeChartSelected.bind(this)}
                             selectedFilters={this.state.selectedFilters}
                             handleSelectedFiltersChange={handleSelectedFiltersChange}
                    />
                    <MapContainer chartSelected={this.state.chartSelected}
                                  selectedFilters={this.state.selectedFilters}
                                  handleSelectedFiltersChange={handleSelectedFiltersChange}
                                  filteredProjects={this.state.filteredProjects}
                                  filtersRestrictions={filtersRestrictions}
                                  showEmptyProjects={this.state.showEmptyProjects}
                                  showLargeCountryPopin={this.state.showLargeCountryPopin}
                                  closeLargeCountryPopinAndClearFilter={this.closeLargeCountryPopinAndClearFilter.bind(this)}
                                  onNoProjectsModalClose={this.onNoProjectsModalClose.bind(this)}
                                  countriesForExport={this.state.countriesForExport}
                                  countriesForExportChanged={this.countriesForExportChanged.bind(this)}
                    />
                </div>
                <PrintDummy/>
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
        },
        projects: {
            activities: state.reportsReducer.activities,
            activitiesLoaded: state.reportsReducer.activitiesLoaded,
            activitiesDetailsLoaded: state.reportsReducer.activitiesDetailsLoaded,
        },
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    loadActivitiesDetails: loadActivitiesDetails,
    loadSectorsFilters: loadSectorsFilters,
    loadCountriesFilters: loadCountriesFilters,
    loadModalitiesFilters: loadModalitiesFilters
}, dispatch);

SssDashboardHome.contextType = SSCTranslationContext;
export default connect(mapStateToProps, mapDispatchToProps)(SssDashboardHome);

