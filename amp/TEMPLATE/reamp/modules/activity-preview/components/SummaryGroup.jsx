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
    const activity = this.props.activity;
    const translations = this.props.translations;
    return (
    <div className={'summary_container'}>
      <FundingSummary
        activity={activity} translations={translations} 
        titleClass={'summary_section_title'}
        groupClass={'summary_section_group'}
        fieldNameClass={'summary_field_name'}
        fieldValueClass={'summary_field_value'} />
      
    </div>);
  }

}
