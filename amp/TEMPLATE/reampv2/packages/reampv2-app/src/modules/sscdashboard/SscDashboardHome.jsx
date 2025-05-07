import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import Sidebar from './components/layout/sidebar/sidebar';
import MapContainer from './components/layout/map/MapContainer';
import { SSCTranslationContext } from './components/StartUp';
import {
  DOWNLOAD_CHART, HOME_CHART, MODALITY_CHART, SECTORS_CHART
} from './utils/constants';
import { DONOR_COUNTRY, MODALITIES, PRIMARY_SECTOR } from './utils/FieldsConstants';
import * as CallReports from './actions/callReports';

import * as LoadFilters from './actions/loadFilters';
import './utils/print.css';
import PrintDummy from './utils/PrintDummy';
import {Outlet} from "react-router-dom";

class SscDashboardHome extends Component {
  constructor(props) {
    super(props);
    this.countriesWithData = [];
    this.state = {
      showDataDownload: false,
      countriesForExport: [],
      chartSelected: HOME_CHART,
      showEmptyProjects: false,
      showLargeCountryPopin: false,
      countriesMessage: false,
      selectedFilters: {
        selectedYears: [],
        selectedCountries: [],
        selectedSectors: [],
        selectedModalities: []
      }
    };
  }

  componentDidMount() {
    const {
      loadSectorsFilters_, loadCountriesFilters_, loadModalitiesFilters_, projects
    } = this.props;
    loadSectorsFilters_();
    loadCountriesFilters_();
    loadModalitiesFilters_();

    if (projects.activitiesLoaded) {
      this.getProjectsData();
    }
    const initialData = this.getFilteredData();
    // eslint-disable-next-line react/no-did-mount-set-state
    this.setState({
      filteredProjects: initialData
    });
  }

  componentDidUpdate(prevProps) {
    if (this.props !== prevProps && this.countriesWithData.length === 0) {
      const { projects } = this.props;
      if (projects.activitiesLoaded) {
        const initialData = this.getFilteredData();
        this.countriesWithData = initialData.map(c => c.id);
        const selectedYears = [];
        selectedYears.push(projects.activities.mostRecentYear);
        this.handleSelectedYearChanged(selectedYears);
        // eslint-disable-next-line react/no-did-update-set-state
        this.setState({
          filteredProjects: initialData
        });
      }
    }
  }

  handleSelectedYearChanged(pSelectedYears) {
    this.updateFilterState('selectedYears', pSelectedYears);
  }

  handleSelectedCountryChanged(pSelectedCountries) {
    // we only keep for export the countries that are selected
    this.setState(previousState => {
      const countriesForExport = [...previousState.countriesForExport].filter(c => pSelectedCountries.includes(c));
      return { countriesForExport };
    });
    const { chartSelected } = this.state;
    if ((chartSelected === SECTORS_CHART || chartSelected === MODALITY_CHART)
      && pSelectedCountries && pSelectedCountries.length >= 1) {
      // currently we open large popin, in next tickets we will open also the popin for 2/3 countries selected
      this.setState({ showLargeCountryPopin: true });
    } else {
      this.closeLargeCountryPopin();
    }
    this.updateFilterState('selectedCountries', pSelectedCountries);
  }

  handleSelectedModalityChanged(pSelectedModalities) {
    this.updateFilterState('selectedModalities', pSelectedModalities);
  }

  handleSelectedSectorChanged(pSelectedSectors) {
    this.updateFilterState('selectedSectors', pSelectedSectors);
  }

  onChangeChartSelected(chartSelected) {
    this.setState({ chartSelected });
    if (chartSelected !== SECTORS_CHART) {
      this.closeLargeCountryPopin();
    }
    if ((chartSelected === SECTORS_CHART || chartSelected === MODALITY_CHART)) {
      this.setState((previousState) => {
        if (previousState.selectedFilters.selectedCountries.length > 0) {
          this.openLargeCountryPopin();
        }
        if (previousState.selectedFilters.selectedCountries.length > 6) {
          // select only the first 6
          console.log(this.countriesWithData);
          return ({ countriesMessage: true });
        } else {
          return ({ countriesMessage: false });
        }
      }, () => {
        const { filters } = this.props;
        const { selectedFilters } = this.state;
        const newSelectedCountries = filters.countries.countries.filter(
          c => selectedFilters.selectedCountries.includes(c.id)
        ).sort((a, b) => {
          if (a.name < b.name) {
            return -1;
          }
          if (a.name > b.name) {
            return 1;
          }
          return 0;
        }).slice(0, 6).map(c => c.id);
        this.handleSelectedCountryChanged(newSelectedCountries);
      });
    }
    if (chartSelected !== DOWNLOAD_CHART) {
      this.setState({ showDataDownload: false });
    }
  }

  onNoProjectsModalClose() {
    this.setState({ showEmptyProjects: false });
  }

  getProjectsData() {
    const { loadActivitiesDetails_, projects } = this.props;
    loadActivitiesDetails_(projects.activities.activitiesId);
  }

  getFilteredProjects() {
    const filteredProjects = this.getFilteredData();
    this.setState({ filteredProjects });
    const { selectedFilters } = this.state;
    if (filteredProjects.length === 0) {
      this.setState({ showEmptyProjects: true });
    } else {
      this.setState({ showEmptyProjects: false });
    }

    const countryWithProjects = filteredProjects.map(p => p.id);
    const intersection = selectedFilters.selectedCountries.filter(c => countryWithProjects.includes(c));
    if (!intersection || intersection.length === 0) {
      this.closeLargeCountryPopin();
    }
  }

  getFilteredData() {
    // TODO see how we can simply or make a bit more generic this function
    const { selectedFilters } = this.state;
    const { projects } = this.props;
    const {
      selectedYears = [], selectedCountries = [], selectedSectors = [], selectedModalities = []
    } = selectedFilters;
    if (!projects.activitiesLoaded) {
      return [];
    }
    const clonedProjectsActivities = projects.activities[DONOR_COUNTRY].map(a => ({ ...a }));
    return clonedProjectsActivities.filter(p => {
      if (selectedCountries.length === 0 || selectedCountries.includes(p.id)) {
        const sectors = p[PRIMARY_SECTOR].filter(sector => {
          if (selectedSectors.length === 0 || selectedSectors.includes(sector.id)) {
            const modalities = sector[MODALITIES].filter(modality => {
              if (selectedModalities.length === 0 || selectedModalities.includes(modality.id)) {
                if (selectedYears.length > 0) {
                  const filteredProjects = modality.activities.filter(a => selectedYears.includes(a.year));
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

  updateCountriesMessage(show) {
    this.setState({ countriesMessage: show });
  }

  countriesForExportChanged(countries) {
    this.setState({ countriesForExport: countries });
  }

  updateFilterState(filterSelector, updatedSelectedFilters) {
    this.setState((currentState) => {
      const selectedFilters = { ...currentState.selectedFilters };
      selectedFilters[filterSelector] = updatedSelectedFilters;
      return { selectedFilters };
    }, this.getFilteredProjects);
  }

  closeLargeCountryPopinAndClearFilter() {
    this.handleSelectedCountryChanged([]);
  }

  openLargeCountryPopin() {
    this.setState({ showLargeCountryPopin: true });
  }

  closeLargeCountryPopin() {
    this.setState({ showLargeCountryPopin: false });
  }

  toggleDataDownload() {
    this.setState(previousSate => ({ showDataDownload: !previousSate.showDataDownload }));
  }

  render() {
    const { projects } = this.props;
    const filtersRestrictions = {
      countriesWithData: this.countriesWithData,
      mostRecentYear: projects.activities.mostRecentYear
    };

    const handleSelectedFiltersChange = {
      handleSelectedModalityChanged: this.handleSelectedModalityChanged.bind(this),
      handleSelectedCountryChanged: this.handleSelectedCountryChanged.bind(this),
      handleSelectedYearChanged: this.handleSelectedYearChanged.bind(this),
      handleSelectedSectorChanged: this.handleSelectedSectorChanged.bind(this)
    };
    const {
      chartSelected,
      selectedFilters,
      filteredProjects,
      showEmptyProjects,
      showLargeCountryPopin,
      countriesForExport,
      showDataDownload,
      countriesMessage
    } = this.state;
    return (
      <>
        <div className="container-fluid content-wrapper">
          <div className="row">
            <Sidebar
              chartSelected={chartSelected}
              toggleDataDownload={this.toggleDataDownload.bind(this)}
              onChangeChartSelected={this.onChangeChartSelected.bind(this)}
              selectedFilters={selectedFilters}
              handleSelectedFiltersChange={handleSelectedFiltersChange}
            />
            <MapContainer
              showDataDownload={showDataDownload && !showEmptyProjects}
              toggleDataDownload={this.toggleDataDownload.bind(this)}
              chartSelected={chartSelected}
              selectedFilters={selectedFilters}
              handleSelectedFiltersChange={handleSelectedFiltersChange}
              filteredProjects={filteredProjects}
              filtersRestrictions={filtersRestrictions}
              showEmptyProjects={showEmptyProjects}
              showLargeCountryPopin={showLargeCountryPopin}
              closeLargeCountryPopinAndClearFilter={this.closeLargeCountryPopinAndClearFilter.bind(this)}
              onNoProjectsModalClose={this.onNoProjectsModalClose.bind(this)}
              countriesForExport={countriesForExport}
              countriesForExportChanged={this.countriesForExportChanged.bind(this)}
              countriesMessage={countriesMessage}
              updateCountriesMessage={this.updateCountriesMessage.bind(this)}
            />
          </div>
          <PrintDummy />
        </div>
        <Outlet />
      </>
    );
  }
}

const mapStateToProps = state => ({
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
});

const mapDispatchToProps = dispatch => bindActionCreators({
  loadActivitiesDetails_: CallReports.loadActivitiesDetails,
  loadSectorsFilters_: LoadFilters.loadSectorsFilters,
  loadCountriesFilters_: LoadFilters.loadCountriesFilters,
  loadModalitiesFilters_: LoadFilters.loadModalitiesFilters
}, dispatch);

SscDashboardHome.contextType = SSCTranslationContext;

SscDashboardHome.propTypes = {
  projects: PropTypes.object.isRequired,
  loadSectorsFilters_: PropTypes.func.isRequired,
  loadCountriesFilters_: PropTypes.func.isRequired,
  loadModalitiesFilters_: PropTypes.func.isRequired,
  loadActivitiesDetails_: PropTypes.func.isRequired,
  filters: PropTypes.object.isRequired
};

export default connect(mapStateToProps, mapDispatchToProps)(SscDashboardHome);
