import React, { Component } from 'react';
import SidebarIntro from './sidebar-intro';

import './sidebar.css';
import HomeLink from '../filters/HomeLink';
import { bindActionCreators } from 'redux';
import { loadCountriesFilters, loadModalitiesFilters, loadSectorsFilters } from '../../../actions/loadFilters';
import { connect } from 'react-redux';
import { SSCTranslationContext } from '../../StartUp';
import MultiSelectionDropDown from '../filters/MultiSelectionDropDown';
import { DOWNLOAD_CHART, HOME_CHART, MODALITY_CHART, SECTORS_CHART } from '../../../utils/constants';

class Sidebar extends Component {

    render() {
        const {chartSelected, onChangeChartSelected} = this.props;
        const {translations} = this.context;
        const {selectedSectors = [], selectedModalities = []} = this.props.selectedFilters;
        const {handleSelectedSectorChanged, handleSelectedModalityChanged} = this.props.handleSelectedFiltersChange;
        const {sectors} = this.props.filters.sectors;
        const {modalities} = this.props.filters.modalities;

        return (
            <div className="col-md-2 sidebar">
                <div className="sidebar-filter-wrapper" id="side-accordion-filter">
                    <HomeLink chartSelected={chartSelected} onChangeChartSelect={onChangeChartSelected}
                              title="amp.ssc.dashboard:Home-Page"
                              chartName={HOME_CHART}
                    />
                    <div className="sidebar-filter-wrapper" id="side-accordion-filter">
                        <MultiSelectionDropDown options={sectors}
                                                filterName='amp.ssc.dashboard:Sector-Analysis'
                                                filterId='ddSectorSide'
                                                parentId="side-accordion-filter"
                                                selectedOptions={selectedSectors}
                                                onChange={handleSelectedSectorChanged}
                                                label={'amp.ssc.dashboard:Sectors'}
                                                chartSelected={chartSelected}
                                                chartName={SECTORS_CHART}
                                                onChangeChartSelected={onChangeChartSelected}
                        />
                    </div>
                    <div className="sidebar-filter-wrapper" id="side-accordion-filter">
                        <MultiSelectionDropDown options={modalities}
                                                filterName='amp.ssc.dashboard:Modalities-Analysis'
                                                filterId='ddModalitiesSide'
                                                parentId="side-accordion-filter"
                                                selectedOptions={selectedModalities}
                                                onChange={handleSelectedModalityChanged}
                                                label={'amp.ssc.dashboard:Modalities'}
                                                chartSelected={chartSelected}
                                                chartName={MODALITY_CHART}
                                                onChangeChartSelected={onChangeChartSelected}
                        />
                    </div>
                    <HomeLink chartSelected={chartSelected} onChangeChartSelect={onChangeChartSelected}
                              title="amp.ssc.dashboard:Download-Page"
                              chartName={DOWNLOAD_CHART}
                    />
                </div>
                {(!chartSelected || chartSelected === HOME_CHART || chartSelected === DOWNLOAD_CHART) &&
                <SidebarIntro
                    text={[translations['amp.ssc.dashboard:home-text-1'], translations['amp.ssc.dashboard:home-text-2']]}/>}
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
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
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    loadSectorsFilters: loadSectorsFilters,
    loadCountriesFilters: loadCountriesFilters,
    loadModalitiesFilters: loadModalitiesFilters
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Sidebar);
Sidebar
    .contextType = SSCTranslationContext;

