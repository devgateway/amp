import React, {Component} from 'react';
import '../popups.css';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import CountryPopupHeader from './CountryPopupHeader';
import CountryPopupChart from './CountryPopupChart';
import CountryPopupFooter from './CountryPopupFooter';
import {
    generateStructureBasedOnModalitiesProjectCount,
    generateStructureBasedOnSectorProjectCount,
    getChartData
} from '../../../../utils/ProjectUtils';
import {SSCTranslationContext} from '../../../StartUp';
import {SECTORS_CHART} from '../../../../utils/constants';

class CountryPopup extends Component {
  render() {
    const {
      project, columnCount, borderClass, lineClass, countriesForExportChanged, countriesForExport, chartSelected
    } = this.props;
    const { countries } = this.props.filters.countries;
    const { sectors } = this.props.filters.sectors;
    const { modalities } = this.props.filters.modalities;
    const { activitiesDetails } = this.props.projects;
    let projectByGroupings;
    let groupings;

    if (chartSelected === SECTORS_CHART) {
      projectByGroupings = generateStructureBasedOnSectorProjectCount(project);
      groupings = sectors;
    } else {
      projectByGroupings = generateStructureBasedOnModalitiesProjectCount(project);
      groupings = modalities;
    }

    const chartData = getChartData(projectByGroupings, groupings);
    const country = countries.find(c => c.id === project.id);
    return (
      <div
        className={`country-popup-all country-popup-${columnCount} ${borderClass || ''} ${lineClass || ''}`}
      >
        <CountryPopupHeader
          country={country}
          projectByGroupings={projectByGroupings}
          columnCount={columnCount}
          countriesForExportChanged={countriesForExportChanged}
          countriesForExport={countriesForExport}
          chartSelected={chartSelected}
        />
        <CountryPopupChart
          columnCount={columnCount}
          chartData={chartData}
          chartSelected={chartSelected}
        />

        {columnCount === 1 && (
          <CountryPopupFooter
            projects={[...projectByGroupings.uniqueProjects]}
            activitiesDetails={activitiesDetails}/>
        )}
      </div>
    );
  }
}

const mapStateToProps = state => ({
  filters: {
    sectors: {
      sectors: state.filtersReducer.sectors,
    },
    modalities: {
      modalities: state.filtersReducer.modalities,
    },
    countries: {
      countries: state.filtersReducer.countries,
    }
  },
  projects: {
    activitiesDetails: state.reportsReducer.activitiesDetails,
  }
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

CountryPopup.contextType = SSCTranslationContext;
export default connect(mapStateToProps, mapDispatchToProps)(CountryPopup);
