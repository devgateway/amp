import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import HorizontalFilters from '../filters/HorizontalFilters';
import './map.css';
import MapHome from '../../map/MapHome';
import PopupOverlay from '../popups/popupOverlay';
import CountryPopupOverlay from '../popups/CountryPopupOverlay';
import { loadActivitiesDetails } from '../../../actions/callReports';
import SimplePopup from '../popups/homepopup/SimplePopup';
import { SSCTranslationContext } from '../../StartUp';
import {
  generateStructureBasedOnModalitiesProjectCount,
  generateStructureBasedOnSectorProjectCount,
  getChartData
} from '../../../utils/ProjectUtils';
import { ACTIVITY_ID, PROJECT_TITLE } from '../../../utils/FieldsConstants';
import { HOME_CHART, MODALITY_CHART } from '../../../utils/constants';

class MapContainer extends Component {
  getExportData() {
    const { translations } = this.context;
    const {
      filteredProjects, countriesForExport, chartSelected
    } = this.props;
    const { sectors } = this.props.filters.sectors;
    const { modalities } = this.props.filters.modalities;

    const { countries } = this.props.filters.countries;
    const { activitiesDetails } = this.props.projects;

    const isModalities = chartSelected === MODALITY_CHART;
    const exportData = {};
    if (isModalities) {
      exportData.title = translations['amp.ssc.dashboard:Modalities-Analysis'];
    } else {
      exportData.title = translations['amp.ssc.dashboard:Sector-Analysis'];
    }
    exportData.source = translations['amp.ssc.dashboard:page-title'];
    exportData.filters = this.populateFilters();
    exportData.columns = [];
    exportData.columns.push({
      headerTitle: translations['amp.ssc.dashboard:Country'],
      key: 'country',
      width: 30
    });
    if (isModalities) {
      exportData.columns.push({
        headerTitle: translations['amp.ssc.dashboard:Modalities'],
        key: 'modality',
        width: 30
      });
    } else {
      exportData.columns.push({
        headerTitle: translations['amp.ssc.dashboard:Sector'],
        key: 'sector',
        width: 30
      });
    }
    exportData.columns.push({
      headerTitle: translations[`amp.ssc.dashboard:${isModalities ? 'modality' : 'sector'}-project-percentage`],
      key: 'percentage',
      width: 30
    });
    exportData.columns.push({
      headerTitle: translations['amp.ssc.dashboard:project-count'],
      key: 'count',
      width: 30
    });
    exportData.columns.push({
      headerTitle: translations['amp.ssc.dashboard:project-xls'],
      key: 'activities',
      width: 100
    });
    exportData.rows = [];
    filteredProjects.forEach(p => {
      if (countriesForExport.length === 0 || countriesForExport.includes(p.id)) {

        let chartData;
        let projectsByGrouping;
        if (isModalities) {
          projectsByGrouping = generateStructureBasedOnModalitiesProjectCount(p);
          chartData = getChartData(projectsByGrouping, modalities, true);
        } else {
          chartData = getChartData(projectsByGrouping, sectors, true);
          projectsByGrouping = generateStructureBasedOnSectorProjectCount(p);
        }
        chartData.forEach(cd => {
          const row = {};
          row.country = countries.filter(c => c.id === p.id)[0].name;
          row[isModalities ? 'modality' : 'sector'] = cd.simpleLabel;
          row.percentage = cd.percentage;
          row.count = cd.value;
          row.activities = [...cd.activities].map(a => activitiesDetails.activities.find(ad => ad[ACTIVITY_ID] === a)[PROJECT_TITLE]).join('|');
          exportData.rows.push(row);
        });
      }
    });
    return exportData;
  }

  populateFilters() {
    const selectedFiltersForExport = [];
    const { translations } = this.context;
    const { filters, selectedFilters } = this.props;
    const { countries } = filters.countries;
    const { sectors } = filters.sectors;
    const { modalities } = filters.modalities;
    const {
      selectedCountries, selectedYears, selectedSectors, selectedModalities
    } = selectedFilters;
    if (selectedCountries && selectedCountries.length > 0) {
      selectedFiltersForExport.push({
        name: translations['amp.ssc.dashboard:Country'],
        values: selectedCountries.map(sc => countries.find(c => c.id === sc).name).join(' | ')
      });
    }
    if (selectedYears && selectedYears.length > 0) {
      selectedFiltersForExport.push({
        name: translations['amp.ssc.dashboard:Year'],
        values: selectedYears.join(' | ')
      });
    }
    if (selectedSectors && selectedSectors.length > 0) {
      selectedFiltersForExport.push({
        name: translations['amp.ssc.dashboard:Sector'],
        values: selectedSectors.map(sc => sectors.find(c => c.id === sc).name).join(' | ')
      });
    }
    if (selectedModalities && selectedModalities.length > 0) {
      selectedFiltersForExport.push({
        name: translations['amp.ssc.dashboard:Modalities'],
        values: selectedModalities.map(sc => modalities.find(c => c.id === sc).name).join(' | ')
      });
    }

    return selectedFiltersForExport;
  }

  render() {
    const { countries } = this.props.filters.countries;
    const { sectors } = this.props.filters.sectors;
    const { modalities } = this.props.filters.modalities;
    const { activitiesDetails } = this.props.projects;
    const {
      countriesForExport, countriesForExportChanged, selectedFilters, filtersRestrictions,
      handleSelectedFiltersChange, chartSelected, showLargeCountryPopin, closeLargeCountryPopinAndClearFilter,
      filteredProjects, showEmptyProjects, onNoProjectsModalClose
    } = this.props;
    const { translations } = this.context;
    return (
      <div className="col-md-10 col-md-offset-2 map-wrapper">
        <HorizontalFilters
          selectedFilters={selectedFilters}
          filtersRestrictions={filtersRestrictions}
          handleSelectedFiltersChange={handleSelectedFiltersChange}
          chartSelected={chartSelected}

        />
        <MapHome filteredProjects={filteredProjects} countries={countries}/>
        {/* TODO refactor country popup in next story */}
        <PopupOverlay show={showEmptyProjects}>
          <SimplePopup
            message={translations['amp.ssc.dashboard:no-data']}
            onClose={onNoProjectsModalClose}/>
        </PopupOverlay>
        <CountryPopupOverlay
          show={showLargeCountryPopin}
          projects={filteredProjects}
          closeLargeCountryPopinAndClearFilter={closeLargeCountryPopinAndClearFilter}
          countriesForExport={countriesForExport}
          countriesForExportChanged={countriesForExportChanged}
          sectors={sectors}
          modalities={modalities}
          activitiesDetails={activitiesDetails}
          countries={countries}
          getExportData={this.getExportData.bind(this)}
          chartSelected={chartSelected}

        />

      </div>
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
    },
    modalities: {
      modalities: state.filtersReducer.modalities,
      modalitiesLoaded: state.filtersReducer.modalitiesLoaded
    }
  },
  projects: {
    activities: state.reportsReducer.activities,
    activitiesDetails: state.reportsReducer.activitiesDetails,
    activitiesLoaded: state.reportsReducer.activitiesLoaded,
  }
});

const mapDispatchToProps = dispatch => bindActionCreators({
  loadActivitiesDetails,
}, dispatch);

MapContainer.contextType = SSCTranslationContext;
export default connect(mapStateToProps, mapDispatchToProps)(MapContainer);
MapContainer.propTypes = {
  chartSelected: PropTypes.string,
  showLargeCountryPopin: PropTypes.bool.isRequired,
  filteredProjects: PropTypes.array,
  closeLargeCountryPopinAndClearFilter: PropTypes.func.isRequired,
  showEmptyProjects: PropTypes.bool.isRequired,
  onNoProjectsModalClose: PropTypes.func.isRequired
};

MapContainer.defaultProps = {
  chartSelected: HOME_CHART,
  filteredProjects: []
};
