import React, { Component } from 'react';
import ToggleSwitch from '../../../utils/ToggleSwitch';
import EllipsisText from 'react-ellipsis-text';
import './HomePopup.css';
import { SSCTranslationContext } from '../../../StartUp';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import * as FieldsConstants from '../../../../utils/FieldsConstants';
import { PROJECT_LENGTH_HOME_PAGE } from '../../../../utils/constants';

class HomePopup extends Component {


    generateStructureBasedOnSector() {
        const {objectData} = this.props.data;
        const sectors = [];
        objectData.sectors.forEach(s => {
            const sector = {};
            sector.id = s.sectorId;
            sector.projects = new Set();
            s.modalities.forEach(m => {
                    m.projects.forEach(p => {
                        sector.projects.add(p.projectId);
                    });
                }
            );
            sectors.push(sector);
        });
        return sectors;
    }

    generateStructureBasedOnModalities() {
        const {objectData} = this.props.data;
        const modalities = new Map();
        objectData.sectors.forEach(s => {
            s.modalities.forEach(m => {
                    if (!modalities.has(m.modalityId)) {
                        const modality = {};
                        modality.id = m.modalityId;
                        modality.projects = new Set();
                        modalities.set(m.modalityId, modality);
                    }
                    m.projects.forEach(p => {
                        modalities.get(m.modalityId).projects.add(p.projectId)
                    });
                }
            );
        });
        return ([...modalities.values()]);
    }

    getModalityName(modalityId) {
        const {modalities} = this.props.filters;
        const {translations} = this.context;
        if (modalities.modalitiesLoaded) {
            return modalities.modalities.find(s => s.id === modalityId).name;
        } else {
            return translations['amp.ssc.dashboard:NA'];
        }
    }

    getSectorName(sectorId) {
        const {sectors} = this.props.filters;
        const {translations} = this.context;
        if (sectors.sectorsLoaded) {
            return sectors.sectors.find(s => s.id === sectorId).name;
        } else {
            return translations['amp.ssc.dashboard:NA'];
        }
    }

    getTableData(showSector) {
        const data = showSector ? this.generateStructureBasedOnSector() : this.generateStructureBasedOnModalities();
        return data.map(m => {
            return (<div className="content-row" key={m.id}>
                <div><span
                    className="title filter-element">
                    {showSector ? this.getSectorName(m.id) : this.getModalityName(m.id)}
                </span>
                </div>
                <div className="project-list">
                    <ul>{this.getProjects(m.projects, m.id)}</ul>
                </div>
            </div>);
        });
    }

    getProjects(projects, elementId) {
        return [...projects].map(p => {
            const project = this.getProject(p);
            const projectName = project
                ? project[FieldsConstants.PROJECT_TITLE] : this.context.translations['amp.ssc.dashboard:NA'];
            const ampUrl = project
                ? project.ampUrl : "/";
            return (<li key={`prj_list_${elementId}_${p}`}><a href={ampUrl} target="_blank">
                <EllipsisText
                    text={projectName}
                    length={PROJECT_LENGTH_HOME_PAGE}/></a>
            </li>)
        })
    }

    getProject(projectId) {
        const {activitiesDetails, activitiesDetailLoaded} = this.props.projects;
        if (activitiesDetailLoaded) {
            return activitiesDetails.activities.find(a => a[FieldsConstants.ACTIVITY_ID] === projectId);
        }

    }

    render() {

        const {translations} = this.context;
        const {data, showSector, handleChangeDataToShow} = this.props;
        const flag = require(`../../../../images/flags/${data.objectName.toLowerCase().replace(/ /g, "_")}.svg`);
        return (
            <div className="homepage-popup">
                <div className="header">
                    <div className="country-name">
                        <img src={flag}/>
                        <span>{data.objectName}</span>
                        <ToggleSwitch big defaultChecked={showSector} id='sectorsToggle'
                                      text={[translations['amp.ssc.dashboard:Sector'],
                                          translations['amp.ssc.dashboard:Modalities']]}
                                      onChange={handleChangeDataToShow}/>
                    </div>
                </div>
                <div className="content">
                    {this.getTableData(showSector)}
                </div>
            </div>
        )
    }
}

HomePopup.contextType = SSCTranslationContext;

const mapStateToProps = state => {
    return {
        filters: {
            sectors: {
                sectors: state.filtersReducer.sectors,
                sectorsLoaded: state.filtersReducer.sectorsLoaded
            },
            modalities: {
                modalities: state.filtersReducer.modalities,
                modalitiesLoaded: state.filtersReducer.modalitiesLoaded
            }
        },
        projects: {
            activitiesDetails: state.reportsReducer.activitiesDetails,
            activitiesDetailLoaded: state.reportsReducer.activitiesDetails,
        }
    };
};
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(HomePopup);
