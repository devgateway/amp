import React, { Component } from 'react';
import FilterCountries from './countries';
import MultiSelectionDropDown from './years';
import CountryCarousel from './carousel';
import CountrySearch from './country-search';
import './filters.css';
import { DASHBOARD_DEFAULT_MAX_YEAR_RANGE, DASHBOARD_DEFAULT_MIN_YEAR_RANGE } from '../../../utils/constants';
import { bindActionCreators } from 'redux';
import { loadSectorsFilters } from '../../../actions/loadFilters';
import { connect } from 'react-redux';

class HorizontalFilters extends Component {
    componentDidMount(): void {
        this.props.loadSectorsFilters();
    }

    render() {
        const years = [];
        const {sectors} = this.props.filters.sectors;
        this._generateYearsFilters(years);
        return (
            <div className="h-filter-wrapper">
                <div className="carousel-filters-wrapper" style={{display: "none"}}>
                    <CountryCarousel/>
                    <CountrySearch/>
                </div>
                <div className="row inner">
                    <div id="dropdown-filters-wrapper" id="accordion-filter">
                        <div className="col-md-3" id="accordion-filter"><FilterCountries/></div>
                        <div className="col-md-4" id="accordion-filter">
                            <MultiSelectionDropDown options={sectors}
                                                    filterName='amp.ssc.dashboard:Sector'
                                                    filterId='ddSector'
                                                    parentId="accordion-filter"/>

                        </div>
                        <div className="col-md-3" id="accordion-filter">
                            <MultiSelectionDropDown options={years}
                                                    filterName='amp.ssc.dashboard:Year'
                                                    filterId='ddYear'
                                                    parentId="accordion-filter"/></div>
                    </div>
                </div>
            </div>
        );
    }

    _generateYearsFilters(years) {
        const {settings, settingsLoaded} = this.props.settings;
        if (settingsLoaded) {
            const minYear = parseInt(settings[DASHBOARD_DEFAULT_MIN_YEAR_RANGE]);
            const maxYear = parseInt(settings[DASHBOARD_DEFAULT_MAX_YEAR_RANGE]);
            for (let i = maxYear; i >= minYear; i--) {
                years.push({id: i, name: i});
            }
        }
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
            }
        }
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    loadSectorsFilters: loadSectorsFilters
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(HorizontalFilters);
