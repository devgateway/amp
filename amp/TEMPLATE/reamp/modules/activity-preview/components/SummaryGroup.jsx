import React, { Component } from 'react';
import FundingSummary from './sections/FundingSummary';
import AdditionalInfo from './sections/AdditionalInfo';
require('../styles/ActivityView.css');

/**
 * @author Daniel Oliva
 */
export default class SummaryGroup extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const params = {
      activity: this.props.params.activity,
      translations : this.props.params.translations
    }
    const styles = {
      fieldNameClass : 'summary_field_name',
      fieldValueClass : 'summary_field_value',
      titleClass : 'summary_section_title', 
      groupClass : 'summary_section_group'
    }
    return (
    <div className={'summary_container'}>
      <FundingSummary params={params} styles={styles} />
      
      <AdditionalInfo params={params} styles={styles} />
      
    </div>);
  }

}
