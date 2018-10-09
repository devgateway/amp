import React, { Component } from 'react';
import Identification from './sections/Identification';
import InternalIds from './sections/InternalIds';
import Planning from './sections/Planning';
import Location from './sections/Location'
import Program from './sections/Program'
import Sector from './sections/Sector'
import FundingSection from './sections/funding/FundingSection'
import RelatedOrganizations from './sections/RelatedOrganizations'
import Issues from './sections/issues/Issues'
import Contacts from './sections/Contacts'
import Structures from './sections/Structures'
import RelatedDocuments from './sections/RelatedDocuments'
import * as FMC from '../utils/FeatureManagerConstants';
import * as AC from '../utils/ActivityConstants';
require('../styles/ActivityView.css');

/**
 *    
 */
export default class MainGroup extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    // TOOD check everywhere what can be taken from context so we dont pass around as props the same object
      const params = {
        activity: this.props.params.activity,
        translations: this.props.params.translations,
        settings: this.props.params.settings,
        activityFieldsManager : this.props.params.activityFieldsManager,
        featureManager : this.props.params.featureManager
    }
    const styles = {
      fieldNameClass : 'section_field_name',
      fieldValueClass : 'section_field_value',
      titleClass : 'section_title_class', 
      groupClass : 'section_group_class'
    }
    
    return (<div className={'main_group_container'}>
      <Identification params={params} styles={styles} fmPath={FMC.ACTIVITY_IDENTIFICATION}/>

      <InternalIds params={params} styles={styles} sectionPath={AC.ACTIVITY_INTERNAL_IDS} />

      <Planning params={params} styles={this._getBoxStyles()} fmPath={FMC.ACTIVITY_PLANNING} />

      <Location params={params} tablify styles={this._getBoxStyles()} sectionPath={AC.LOCATIONS}/>

      <Program params={params} styles={this._getPercentageStyles()} fmPath={FMC.ACTIVITY_PROGRAM}/>

      <Sector params={params} styles={this._getPercentageStyles()} fmPath={FMC.ACTIVITY_SECTORS}/>

      <FundingSection params={params} styles={this._getBoxStyles()} sectionPath={AC.FUNDINGS} />

      <RelatedOrganizations params={params} styles={styles} fmPath={FMC.ACTIVITY_ORGANIZATIONS}/>

      <Issues params={params} styles={styles}  sectionPath={AC.ISSUES} />

      <RelatedDocuments params={params} styles={styles}  sectionPath={AC.ACTIVITY_DOCUMENTS}/>

      <Contacts params={params} styles={styles}  fmPath={FMC.ACTIVITY_CONTACT}/>

      <Structures params={params} styles={styles}  sectionPath={AC.STRUCTURES}/>
    </div>);
  }

  _getBoxStyles() {
    return {
      inline : false,
      fieldNameClass : 'box_field_name',
      fieldValueClass : 'box_field_value',
      titleClass : 'section_title_class', 
      groupClass : 'section_group_class'
    }
  }

  _getPercentageStyles() {
    return {
      inline : false,
      fieldNameClass : 'sector_title',
      fieldValueClass : 'list_field_value',
      titleClass : 'section_title_class', 
      groupClass : 'section_group_class'
    }
  }
}
