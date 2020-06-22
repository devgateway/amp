import React, { Component } from 'react';
import ToggleSwitch from '../../../utils/ToggleSwitch';
import EllipsisText from 'react-ellipsis-text';
import './HomePopup.css';
import { SSCTranslationContext } from '../../../StartUp';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import * as FieldsConstants from '../../../../utils/FieldsConstants';
import { FLAG_DEFAULT, FLAGS_DIRECTORY, PROJECT_LENGTH_HOME_PAGE } from '../../../../utils/constants';
import * as Utils from '../../../../utils/Utils';
import { Img } from 'react-image';

class HomePopup extends Component {


    generateStructureBasedOnSector() {

        const {objectData} = this.props.data;
        const sectors = [];
        objectData[FieldsConstants.PRIMARY_SECTOR].forEach(s => {
            const sector = {};
            sector.id = s.id;
            sector.activities = new Set();
            s[FieldsConstants.MODALITIES].forEach(m => {
                    m.activities.forEach(p => {
                        sector.activities.add(p.id);
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
        objectData[FieldsConstants.PRIMARY_SECTOR].forEach(s => {
            s[FieldsConstants.MODALITIES].forEach(m => {
                    if (!modalities.has(m.id)) {
                        const modality = {};
                        modality.id = m.id;
                        modality.activities = new Set();
                        modalities.set(m.id, modality);
                    }
                    m.activities.forEach(p => {
                        modalities.get(m.id).activities.add(p.id)
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
            m.description = showSector ? this.getSectorName(m.id) : this.getModalityName(m.id);
            return m;
        }).sort((a, b) => a.description > b.description ? 1 : -1).map(m => {
            return (<div className="content-row" key={m.id}>
                <div><span className={`title filter-element${showSector ? '' : ' alternative'}`}>{m.description}</span>
                </div>
                <div className="project-list">
                    <ul>{this.getProjects(m.activities, m.id)}</ul>
                </div>
            </div>);
        });
    }

    getProjects(projects, elementId) {
        return [...projects].map(p => {
            const project = this.getProject(p);
            const prj = {};
            prj.projectName = project
                ? project[FieldsConstants.PROJECT_TITLE] : this.context.translations['amp.ssc.dashboard:NA'];
            prj.ampUrl = project
                ? project.ampUrl : "/";
            prj.id = p;
            return prj;

        }).sort((a, b) => a.projectName > b.projectName ? 1 : -1).map(p => {

            return (<li key={`prj_list_${elementId}_${p.id}`}><a href={p.ampUrl} target="_blank">
                <EllipsisText
                    text={p.projectName}
                    length={PROJECT_LENGTH_HOME_PAGE}/></a>
            </li>)
        })
    }

    getProject(projectId) {
        const {activitiesDetails, activitiesDetailsLoaded} = this.props.projects;
        if (activitiesDetailsLoaded) {
            return activitiesDetails.activities.find(a => a[FieldsConstants.ACTIVITY_ID] === projectId);
        }

    }

    render() {
        const {translations} = this.context;
        const {data, showSector, handleChangeDataToShow} = this.props;
        return (
            <div className="homepage-popup">
                <div className="header">
                    <div className="country-name">
                        <Img
                            src={Utils.getCountryFlag(data.objectName)}/>
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
            activitiesDetailsLoaded: state.reportsReducer.activitiesDetailsLoaded,
        }
    };
};
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(HomePopup);
