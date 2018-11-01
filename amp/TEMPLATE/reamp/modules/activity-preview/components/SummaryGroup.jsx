import React, { Component } from 'react';
import FundingSummary from './sections/FundingSummary';
import { EFFECTIVE_CURRENCY } from '../utils/ActivityConstants';
import AdditionalInfo from './sections/AdditionalInfo';
require('../styles/ActivityView.css');

/**
 *    
 */
export default class SummaryGroup extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const params = {
      activity: this.props.params.activity,
      translations : this.props.params.translations,
      settings : this.props.params.settings,
      activityInfo : this.props.params.activityInfo
    }
    const currency = this.props.params.settings[EFFECTIVE_CURRENCY].code;
    const styles = {
      fieldNameClass : 'summary_field_name',
      fieldValueClass : 'summary_field_value',
      titleClass : 'summary_section_title', 
      groupClass : 'summary_section_group'
    }
    return (
    <div className={'summary_container'}>
      <FundingSummary params={params} styles={styles} titleExtra = {` (${currency})`} />
      
      <AdditionalInfo params={params} styles={styles}  />
      
    </div>);
  }

}
