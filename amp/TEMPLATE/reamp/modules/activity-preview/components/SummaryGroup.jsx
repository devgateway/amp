import React, { Component } from 'react';
import * as AC from '../utils/ActivityConstants';
import FundingSummary from './sections/FundingSummary';
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
      fieldValueClass : 'section_field_value',
      titleClass : 'summary_field_value', 
      groupClass : 'summary_section_group'
    }
    return (
    <div className={'summary_container'}>
      <FundingSummary params={params} styles={styles} />
      
    </div>);
  }

}
