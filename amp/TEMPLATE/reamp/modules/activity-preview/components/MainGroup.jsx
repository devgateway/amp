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
require('../styles/ActivityView.css');

/**
 *    
 */
export default class MainGroup extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const params = {
      activity: this.props.params.activity,
      translations : this.props.params.translations,
      settings : this.props.params.settings
    }
    const styles = {
      fieldNameClass : 'section_field_name',
      fieldValueClass : 'section_field_value',
      titleClass : 'section_title_class', 
      groupClass : 'section_group_class'
    }
    
    return (<div className={'main_group_container'}>
      <Identification params={params} styles={styles} />

      <InternalIds params={params} styles={styles}/>

      <Planning params={params} styles={this._getBoxStyles()} />

      <Location params={params} tablify styles={this._getBoxStyles()} />

      <Program params={params} styles={this._getPercentageStyles()} />

      <Sector params={params} styles={this._getPercentageStyles()} />

      <FundingSection params={params} styles={this._getBoxStyles()}/>

      <RelatedOrganizations params={params} styles={styles} />

      <Issues params={params} styles={styles}  />

      <RelatedDocuments params={params} styles={styles}  />

      <Contacts params={params} styles={styles}  />

      <Structures params={params} styles={styles}  />
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
