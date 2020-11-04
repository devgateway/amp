import React, { Component } from 'react';
import HorizontalFilters from '../filters/HorizontalFilters';
import './map.css';
import MapHome from '../../map/MapHome';
import PopupOverlay from '../popups/popupOverlay';
import CountryPopupOverlay from '../popups/CountryPopupOverlay';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { loadActivitiesDetails } from '../../../actions/callReports';
import SimplePopup from '../popups/homepopup/SimplePopup';
import { SSCTranslationContext } from '../../StartUp';
import { generateStructureBasedOnSectorProjectCount, getChartData } from '../../../utils/ProjectUtils';
import { ACTIVITY_ID, PROJECT_TITLE } from '../../../utils/FieldsConstants';

class MapContainer extends Component {
  getExportData() {
    const { translations } = this.context;
    const {
      filteredProjects, countriesForExport
    } = this.props;
    const { sectors } = this.props.filters.sectors;
    const { countries } = this.props.filters.countries;
    const { activitiesDetails } = this.props.projects;
    const exportData = {};
    exportData.title = translations['amp.ssc.dashboard:Sector-Analysis'];
    exportData.source = translations['amp.ssc.dashboard:page-title'];
    exportData.filters = this.populateFilters();
    exportData.columns = [];
    exportData.columns.push({
      headerTitle: translations['amp.ssc.dashboard:Country'],
      key: 'country',
      width: 30
    });
    exportData.columns.push({
      headerTitle: translations['amp.ssc.dashboard:Sector'],
      key: 'sector',
      width: 30
    });
    exportData.columns.push({
      headerTitle: translations['amp.ssc.dashboard:project-percentage'],
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
        const projectsBySectors = generateStructureBasedOnSectorProjectCount(p);
        const chartData = getChartData(projectsBySectors, sectors, true);
        chartData.forEach(cd => {
          const row = {};
          row.country = countries.filter(c => c.id === p.id)[0].name;
          row.sector = cd.simpleLabel;
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
    const filters = [];
    const { translations } = this.context;
    const { countries } = this.props.filters.countries;
    const { sectors } = this.props.filters.sectors;
    const { modalities } = this.props.filters.modalities;
    const {
      selectedCountries, selectedYears, selectedSectors, selectedModalities
    } = this.props.selectedFilters;
    if (selectedCountries && selectedCountries.length > 0) {
      filters.push({
        name: translations['amp.ssc.dashboard:Country'],
        values: selectedCountries.map(sc => countries.find(c => c.id === sc).name).join(' | ')
      });
    }
    if (selectedYears && selectedYears.length > 0) {
      filters.push({
        name: translations['amp.ssc.dashboard:Year'],
        values: selectedYears.join(' | ')
      });
    }
    if (selectedSectors && selectedSectors.length > 0) {
      filters.push({
        name: translations['amp.ssc.dashboard:Sector'],
        values: selectedSectors.map(sc => sectors.find(c => c.id === sc).name).join(' | ')
      });
    }
    if (selectedModalities && selectedModalities.length > 0) {
      filters.push({
        name: translations['amp.ssc.dashboard:Modalities'],
        values: selectedModalities.map(sc => modalities.find(c => c.id === sc).name).join(' | ')
      });
    }

    return filters;
  }

  render() {
    const { countries } = this.props.filters.countries;
    const { sectors } = this.props.filters.sectors;
    const { activitiesDetails } = this.props.projects;
    const {
      countriesForExport, countriesForExportChanged, selectedFilters, filtersRestrictions, handleSelectedFiltersChange, chartSelected
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
        <MapHome filteredProjects={this.props.filteredProjects} countries={countries} />
        {/* TODO refactor country popup in next story */}
        <PopupOverlay show={this.props.showEmptyProjects}>
          <SimplePopup
            message={translations['amp.ssc.dashboard:no-data']}
            onClose={this.props.onNoProjectsModalClose} />
        </PopupOverlay>
        <CountryPopupOverlay
          show={this.props.showLargeCountryPopin}
          projects={this.props.filteredProjects}
          closeLargeCountryPopinAndClearFilter={this.props.closeLargeCountryPopinAndClearFilter}
          countriesForExport={countriesForExport}
          countriesForExportChanged={countriesForExportChanged}
          sectors={sectors}
          activitiesDetails={activitiesDetails}
          countries={countries}
          getExportData={this.getExportData.bind(this)}

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
