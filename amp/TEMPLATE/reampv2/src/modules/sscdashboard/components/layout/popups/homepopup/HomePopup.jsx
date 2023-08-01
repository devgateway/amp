import React, {Component} from 'react';
import ToggleSwitch from '../../../utils/ToggleSwitch';
import './HomePopup.css';
import {SSCTranslationContext} from '../../../StartUp';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import * as FieldsConstants from '../../../../utils/FieldsConstants';
import {PROJECT_LENGTH_HOME_PAGE, UNDEFINED_FILTER} from '../../../../utils/constants';
import * as Utils from '../../../../utils/Utils';
import {Img} from 'react-image';
import {generateStructureBasedOnSector, getProjects} from '../../../../utils/ProjectUtils';

class HomePopup extends Component {
  generateStructureBasedOnModalities() {
    const { objectData } = this.props.data;
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
          modalities.get(m.id).activities.add(p.id);
        });
      });
    });
    return ([...modalities.values()]);
  }

  getModalityName(modalityId) {
    const { modalities } = this.props.filters;
    const { translations } = this.context;
    if (modalities.modalitiesLoaded && modalityId !== UNDEFINED_FILTER) {
      return modalities.modalities.find(s => s.id === modalityId).name;
    } else {
      return translations['amp.ssc.dashboard:NA'];
    }
  }

  getSectorName(sectorId) {
    const { sectors } = this.props.filters;
    const { translations } = this.context;
    if (sectors.sectorsLoaded) {
      return sectors.sectors.find(s => s.id === sectorId).name;
    } else {
      return translations['amp.ssc.dashboard:NA'];
    }
  }

  getTableData(showSector) {
    const data = showSector ? generateStructureBasedOnSector(this.props.data.objectData) : this.generateStructureBasedOnModalities();
    const { activitiesDetails } = this.props.projects;

    return data.map(m => {
      m.description = showSector ? this.getSectorName(m.id) : this.getModalityName(m.id);
      return m;
    }).sort((a, b) => (a.description > b.description ? 1 : -1)).map(m => (
      <div className="content-row" key={m.id}>
        <div>
          <span className={`title filter-element${showSector ? '' : ' alternative'}`}>{m.description}</span>
        </div>
        <div className="project-list">
          <ul>
            {getProjects(m.activities, m.id, activitiesDetails,
              PROJECT_LENGTH_HOME_PAGE, this.context.translations['amp.ssc.dashboard:NA'])}
          </ul>
        </div>
      </div>
    ));
  }

  render() {
    const { translations } = this.context;
    const { data, showSector, handleChangeDataToShow } = this.props;
    return (
      <div className="homepage-popup">
        <div className="header">
          <div className="country-name">
            <Img
              src={Utils.getCountryFlag(data.objectName)} />
            <span>{data.objectName}</span>
            <ToggleSwitch
              big
              defaultChecked={showSector}
              id="sectorsToggle"
              text={[translations['amp.ssc.dashboard:Sector'],
                translations['amp.ssc.dashboard:Modalities']]}
              onChange={handleChangeDataToShow} />
          </div>
        </div>
        <div className="content">
          {this.getTableData(showSector)}
        </div>
      </div>
    );
  }
}

HomePopup.contextType = SSCTranslationContext;

const mapStateToProps = state => ({
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
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(HomePopup);
