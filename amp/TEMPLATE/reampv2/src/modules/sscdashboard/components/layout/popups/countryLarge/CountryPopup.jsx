import React, { Component } from "react";
import '../popups.css';
import CountryPopupHeader from "./CountryPopupHeader";
import CountryPopupChart from "./CountryPopupChart";
import CountryPopupFooter from "./CountryPopupFooter";
import { generateStructureBasedOnSectorProjectCount } from '../../../../utils/ProjectUtils';
import { bindActionCreators } from 'redux';
import { SSCTranslationContext } from '../../../StartUp';
import { connect } from 'react-redux';

class CountryPopup extends Component {

    render() {
        const {project, columnCount, borderClass, lineClass, countriesForExportChanged, countriesForExport} = this.props;
        const {countries} = this.props.filters.countries;
        const {sectors} = this.props.filters.sectors;
        const {activitiesDetails} = this.props.projects;
        const projectsBySectors = generateStructureBasedOnSectorProjectCount(project);
        const country = countries.find(c => c.id === project.id);
        return (
            <div
                className={`country-popup-all country-popup-${columnCount} ${borderClass ? borderClass : ''} ${lineClass ? lineClass : ''}`}
            >
                <CountryPopupHeader country={country} projectsBySectors={projectsBySectors} columnCount={columnCount}
                                    countriesForExportChanged={countriesForExportChanged}
                                    countriesForExport={countriesForExport}/>
                <CountryPopupChart projectsBySectors={projectsBySectors} sectors={sectors} columnCount={columnCount}/>

                {columnCount === 1 && <CountryPopupFooter projects={[...projectsBySectors.uniqueProjects]}
                                                          activitiesDetails={activitiesDetails}/>}
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
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
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

CountryPopup.contextType = SSCTranslationContext;
export default connect(mapStateToProps, mapDispatchToProps)(CountryPopup);


