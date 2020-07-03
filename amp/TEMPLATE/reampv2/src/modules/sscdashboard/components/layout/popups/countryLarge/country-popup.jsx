import React, { Component } from "react";
import '../popups.css';
import CountryPopupHeader from "./country-popup-header";
import CountryPopupChart from "./country-popup-chart";
import CountryPopupFooter from "./country-popup-footer";
import { generateStructureBasedOnSectorProjectCount } from '../../../../utils/ProjectUtils';
import { bindActionCreators } from 'redux';
import { SSCTranslationContext } from '../../../StartUp';
import { connect } from 'react-redux';

class CountryPopup extends Component {

    render() {
        const {project} = this.props;
        const {countries} = this.props.filters.countries;
        const {sectors} = this.props.filters.sectors;
        const {activitiesDetails} = this.props.projects;
        const projectsBySectors = generateStructureBasedOnSectorProjectCount(project);
        const country = countries.find(c => c.id === project.id);
        return (
            <div class="country-popup">
                <CountryPopupHeader country={country} projectsBySectors={projectsBySectors}/>
                <CountryPopupChart projectsBySectors={projectsBySectors} sectors={sectors}/>

                <CountryPopupFooter projects={[...projectsBySectors.uniqueProjects]}
                                    activitiesDetails={activitiesDetails}/>
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


