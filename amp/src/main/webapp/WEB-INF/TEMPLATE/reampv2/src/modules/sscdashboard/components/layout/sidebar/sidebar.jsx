import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import SidebarIntro from './SidebarIntro';
import './sidebar.css';
import FiltersLink from '../filters/FiltersLink';
import { SSCTranslationContext } from '../../StartUp';
import MultiSelectionDropDown from '../filters/MultiSelectionDropDown';
import {
  DOWNLOAD_CHART, HOME_CHART, MODALITY_CHART, SECTORS_CHART
} from '../../../utils/constants';

class Sidebar extends Component {
  render() {
    const {
      chartSelected, onChangeChartSelected, toggleDataDownload, handleSelectedFiltersChange,
      filters, selectedFilters
    } = this.props;
    const { selectedSectors = [], selectedModalities = [] } = selectedFilters;
    const { handleSelectedSectorChanged, handleSelectedModalityChanged } = handleSelectedFiltersChange;
    const { sectors } = filters.sectors;
    const { modalities } = filters.modalities;

    return (
      <div className="col-md-2 sidebar">
        <div className="sidebar-filter-wrapper" id="side-accordion-filter">
          <FiltersLink
            chartSelected={chartSelected}
            onChangeChartSelect={onChangeChartSelected}
            title="amp.ssc.dashboard:Home-Page"
            chartName={HOME_CHART}
          />
          <div className="sidebar-filter-wrapper" id="side-accordion-filter">
            <MultiSelectionDropDown
              options={sectors}
              filterName="amp.ssc.dashboard:Sector-Analysis"
              filterId="ddSectorSide"
              parentId="side-accordion-filter"
              selectedOptions={selectedSectors}
              onChange={handleSelectedSectorChanged}
              label="amp.ssc.dashboard:Sectors"
              chartSelected={chartSelected}
              chartName={SECTORS_CHART}
              onChangeChartSelected={onChangeChartSelected}
            />
          </div>
          <div className="sidebar-filter-wrapper" id="side-accordion-filter">
            <MultiSelectionDropDown
              options={modalities}
              filterName="amp.ssc.dashboard:Modalities-Analysis"
              filterId="ddModalitiesSide"
              parentId="side-accordion-filter"
              selectedOptions={selectedModalities}
              onChange={handleSelectedModalityChanged}
              label="amp.ssc.dashboard:Modalities"
              chartSelected={chartSelected}
              chartName={MODALITY_CHART}
              onChangeChartSelected={onChangeChartSelected}
            />
          </div>
          <FiltersLink
            chartSelected={chartSelected}
            onLinkClicked={toggleDataDownload}
            onChangeChartSelect={onChangeChartSelected}
            title="amp.ssc.dashboard:Download-Page"
            chartName={DOWNLOAD_CHART}
          />
        </div>
        {(!chartSelected || chartSelected === HOME_CHART || chartSelected === DOWNLOAD_CHART)
        && (
          <SidebarIntro
            text={['amp.ssc.dashboard:home-text-1', 'amp.ssc.dashboard:home-text-2']} />
        )}
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
    modalities: {
      modalities: state.filtersReducer.modalities,
      modalitiesLoaded: state.filtersReducer.modalitiesLoaded
    },

  }
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Sidebar);
Sidebar
  .contextType = SSCTranslationContext;

Sidebar.propTypes = {
  filters: PropTypes.object.isRequired,
  toggleDataDownload: PropTypes.func.isRequired,
  chartSelected: PropTypes.string.isRequired,
  onChangeChartSelected: PropTypes.func.isRequired,
  selectedFilters: PropTypes.object.isRequired,
  handleSelectedFiltersChange: PropTypes.shape({
    handleSelectedModalityChanged: PropTypes.func.isRequired,
    handleSelectedCountryChanged: PropTypes.func.isRequired,
    handleSelectedYearChanged: PropTypes.func.isRequired,
    handleSelectedSectorChanged: PropTypes.func.isRequired
  }).isRequired
};
