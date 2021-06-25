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
import { DOWNLOAD_CHART, HOME_CHART, MODALITY_CHART } from '../../../utils/constants';
import DataDownloadContainer from '../../dataDownload/DataDownloadContainer';
import { populateFilters } from '../../../utils/exportUtils';

class MapContainer extends Component {
  getExportData() {
    const { translations } = this.context;
    const {
      filteredProjects, countriesForExport, chartSelected, filters, projects, selectedFilters
    } = this.props;
    const { sectors } = filters.sectors;
    const { modalities } = filters.modalities;

    const { countries } = filters.countries;
    const { activitiesDetails } = projects;

    const isModalities = chartSelected === MODALITY_CHART;
    const exportData = {};
    if (isModalities) {
      exportData.title = translations['amp.ssc.dashboard:Modalities-Analysis'];
    } else {
      exportData.title = translations['amp.ssc.dashboard:Sector-Analysis'];
    }
    exportData.source = translations['amp.ssc.dashboard:page-title'];
    exportData.filters = populateFilters(translations, filters, selectedFilters);
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
    const title = translations['amp.ssc.dashboard:project-percentage']
      .replace('{filter}', translations[`amp.ssc.dashboard:${isModalities}` ? 'Modality' : 'Sector']);
    exportData.columns.push({
      headerTitle: title,
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
          projectsByGrouping = generateStructureBasedOnSectorProjectCount(p);
          chartData = getChartData(projectsByGrouping, sectors, true);
        }
        chartData.forEach(cd => {
          const row = {};
          row.country = countries.filter(c => c.id === p.id)[0].name;
          row[isModalities ? 'modality' : 'sector'] = cd.simpleLabel;
          row.percentage = cd.percentage;
          row.count = cd.value;
          row.activities = [...cd.activities].map(a => activitiesDetails.activities
            .find(ad => ad[ACTIVITY_ID] === a)[PROJECT_TITLE]).join('|');
          exportData.rows.push(row);
        });
      }
    });
    return exportData;
  }


  render() {
    const {
      filters, projects, countriesForExport, countriesForExportChanged, selectedFilters, filtersRestrictions,
      handleSelectedFiltersChange, chartSelected, showLargeCountryPopin, closeLargeCountryPopinAndClearFilter,
      filteredProjects, showEmptyProjects, onNoProjectsModalClose, showDataDownload, settings, toggleDataDownload
    } = this.props;
    const { countries } = filters.countries;
    const { sectors } = filters.sectors;
    const { modalities } = filters.modalities;
    const { activitiesDetails } = projects;

    const { translations } = this.context;
    return (
      <div className="col-md-10 col-md-offset-2 map-wrapper">
        {chartSelected !== DOWNLOAD_CHART && (<HorizontalFilters
          selectedFilters={selectedFilters}
          filtersRestrictions={filtersRestrictions}
          handleSelectedFiltersChange={handleSelectedFiltersChange}
          chartSelected={chartSelected}

        />)}
        <MapHome filteredProjects={filteredProjects} countries={countries} />
        <PopupOverlay show={showDataDownload} additionalClass={'data-container'}>
          <DataDownloadContainer
            selectedFilters={selectedFilters}
            filtersRestrictions={filtersRestrictions}
            chartSelected={chartSelected}
            filters={filters}
            settings={settings}
            toggleDataDownload={toggleDataDownload}
          />
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

        <PopupOverlay show={showEmptyProjects}>
          <SimplePopup
            message={translations['amp.ssc.dashboard:no-data']}
            onClose={onNoProjectsModalClose} />
        </PopupOverlay>
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
  onNoProjectsModalClose: PropTypes.func.isRequired,
  countriesForExport: PropTypes.array,
  projects: PropTypes.object.isRequired,
  countriesForExportChanged: PropTypes.func.isRequired,
  handleSelectedFiltersChange: PropTypes.object.isRequired,
  filters: PropTypes.object.isRequired,
  settings: PropTypes.object.isRequired,
  selectedFilters: PropTypes.shape({
    selectedYears: PropTypes.array,
    selectedCountries: PropTypes.array,
    selectedSectors: PropTypes.array,
    selectedModalities: PropTypes.array,
  }),
  filtersRestrictions: PropTypes.object.isRequired,
  showDataDownload: PropTypes.bool,
  toggleDataDownload: PropTypes.func.isRequired
};

MapContainer.defaultProps = {
  chartSelected: HOME_CHART,
  filteredProjects: [],
  countriesForExport: [],
  selectedFilters: {
    selectedYears: [],
    selectedCountries: [],
    selectedSectors: [],
    selectedModalities: [],
  },
  showDataDownload: false
};
