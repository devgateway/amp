import React, { Component } from 'react';
import '../popups.css';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import CountryPopupHeader from './CountryPopupHeader';
import CountryPopupChart from './CountryPopupChart';
import CountryPopupFooter from './CountryPopupFooter';
import { generateStructureBasedOnSectorProjectCount, getChartData } from '../../../../utils/ProjectUtils';
import { SSCTranslationContext } from '../../../StartUp';

class CountryPopup extends Component {
  render() {
    const {
      project, columnCount, borderClass, lineClass, countriesForExportChanged, countriesForExport
    } = this.props;
    const { countries } = this.props.filters.countries;
    const { sectors } = this.props.filters.sectors;
    const { activitiesDetails } = this.props.projects;
    const projectsBySectors = generateStructureBasedOnSectorProjectCount(project);

    const chartData = getChartData(projectsBySectors, sectors);
    const country = countries.find(c => c.id === project.id);
    return (
      <div
        className={`country-popup-all country-popup-${columnCount} ${borderClass || ''} ${lineClass || ''}`}
            >
        <CountryPopupHeader
          country={country}
          projectsBySectors={projectsBySectors}
          columnCount={columnCount}
          countriesForExportChanged={countriesForExportChanged}
          countriesForExport={countriesForExport} />
        <CountryPopupChart
          columnCount={columnCount}
          chartData={chartData} />

        {columnCount === 1 && (
        <CountryPopupFooter
          projects={[...projectsBySectors.uniqueProjects]}
          activitiesDetails={activitiesDetails} />
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
