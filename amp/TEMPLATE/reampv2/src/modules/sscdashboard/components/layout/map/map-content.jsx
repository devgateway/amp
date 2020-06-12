import React, { Component } from 'react';
import HorizontalFilters from '../filters/horizontal-filters';
import './map.css';
import MapHome from "../../map/MapHome";
import CountryPopupOverlay from "../popups/popup-overlay";
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { loadActivitiesDetails } from '../../../actions/callReports';
import SimplePopup from '../popups/homepopup/SimplePopup';
import { SSCTranslationContext } from '../../StartUp';
import { DONOR_COUNTRY, MODALITIES, PRIMARY_SECTOR } from '../../../utils/FieldsConstants';

class MapContainer extends Component {
    //TODO once we implement side filters maybe we need to move state up
    constructor(props) {
        super(props);
        this.countriesWithData = [];
        this.state = {
            showModal: false,
            filteredProjects: [],
            countriesWithData: [],
            selectedFilters: {
                selectedYears: [],
                selectedCountries: [],
                selectedSectors: [],
                selectedModalities: []
            }
        };
    }

    getProjectsData() {
        this.props.loadActivitiesDetails(this.props.projects.activities.activitiesId);
    }

    componentDidMount(): void {
        if (this.props.projects.activitiesLoaded) {
            this.getProjectsData();
        }
        const initialData = this.getFilteredData();

        this.setState({
            filteredProjects: initialData
        });
    }

    componentDidUpdate(prevProps, prevState) {
        if (this.props !== prevProps) {
            if (this.props.projects.activitiesLoaded) {
                const initialData = this.getFilteredData();
                this.countriesWithData = initialData.map(c => c.id);
                const selectedYears = [];
                selectedYears.push(this.props.projects.activities.mostRecentYear);
                //this.handleSelectedYearChanged(selectedYears);
                this.setState({
                    filteredProjects: initialData
                });
            }
        }
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
            this.setState({showModal: true});
        }
    }

    modalOnClose(e) {
        this.setState({showModal: false});
    }

    getFilteredData() {
        //TODO see how we can simply or make a bit more generic this function
        const {selectedYears = [], selectedCountries = [], selectedSectors = [], selectedModalities = []} = this.state.selectedFilters;
        if (!this.props.projects.activitiesLoaded) {
            return [];
        }
        return this.props.projects.activities[DONOR_COUNTRY].filter(p => {
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
                        if (modalities.length == 0) {
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
        const {countries} = this.props.filters.countries;
        const filtersRestrictions = {countriesWithData: this.countriesWithData};
        const {translations} = this.context;
        return (
            <div className="col-md-10 col-md-offset-2 map-wrapper">
                <HorizontalFilters selectedFilters={this.state.selectedFilters}
                                   filtersRestrictions={filtersRestrictions}
                                   handleSelectedYearChanged={this.handleSelectedYearChanged.bind(this)}
                                   handleSelectedCountryChanged={this.handleSelectedCountryChanged.bind(this)}
                                   handleSelectedSectorChanged={this.handleSelectedSectorChanged.bind(this)}
                                   handleSelectedModalityChanged={this.handleSelectedModalityChanged.bind(this)}

                />
                <MapHome filteredProjects={this.state.filteredProjects} countries={countries}/>
                {/*TODO refactor country popup in next story*/}
                <CountryPopupOverlay show={this.state.showModal}>
                    <SimplePopup message={translations['amp.ssc.dashboard:no-date']}
                                 onClose={this.modalOnClose.bind(this)}/>
                </CountryPopupOverlay>
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
        }
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    loadActivitiesDetails: loadActivitiesDetails,
}, dispatch);

MapContainer.contextType = SSCTranslationContext;
export default connect(mapStateToProps, mapDispatchToProps)(MapContainer);

