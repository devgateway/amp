import React, { Component } from 'react';
import Section from './Section';
import PercentageList from '../fields/PercentageList';
import {
  NATIONAL_PLAN_OBJECTIVE,
  PRIMARY_PROGRAMS,
  SECONDARY_PROGRAMS,
  PROGRAM,
  PROGRAM_PERCENTAGE
} from '../../utils/ActivityConstants';
require('../../styles/ActivityView.css');

const NationalPlanList = PercentageList(NATIONAL_PLAN_OBJECTIVE, PROGRAM, PROGRAM_PERCENTAGE, 'national_plan' );
const PrimaryProgramList = PercentageList(PRIMARY_PROGRAMS, PROGRAM, PROGRAM_PERCENTAGE, 'primary_program');
const SecondaryProgramList = PercentageList(SECONDARY_PROGRAMS, PROGRAM, PROGRAM_PERCENTAGE, 'secondary_program');

/**
 * Activity Preview Program section
 *    
 */
class Program extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (<div>
      <div className={'primary_sector'}>
        <NationalPlanList key="national-plan-list" {...this.props} 
        percentTitleClass={'percent_field_name'} percentValueClass={'percent_field_value'}/>
      </div>
      <div className={'secondary_sector'}>
        <PrimaryProgramList key="primary-programs-list" {...this.props} 
        percentTitleClass={'percent_field_name'} percentValueClass={'percent_field_value'}/>
      </div>
      <div className={'primary_sector'}>
        <SecondaryProgramList key="secondary-programs-list" {...this.props} 
        percentTitleClass={'percent_field_name'} percentValueClass={'percent_field_value'}/>
      </div>
    </div>);
  }

}

export default Section(Program, 'Program', true, 'AcProgram');
