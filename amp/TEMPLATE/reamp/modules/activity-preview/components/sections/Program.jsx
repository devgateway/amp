import React, { Component } from 'react';
import Section from './Section';
import PercentageList from '../fields/PercentageList';
import {
  PRIMARY_PROGRAMS,
  SECONDARY_PROGRAMS,
  PROGRAM,
  PROGRAM_PERCENTAGE
} from '../../utils/ActivityConstants';
require('../../styles/ActivityView.css');

const PrimaryProgramList = PercentageList(PRIMARY_PROGRAMS, PROGRAM, PROGRAM_PERCENTAGE, 'Primary Program');
const SecondaryProgramList = PercentageList(SECONDARY_PROGRAMS, PROGRAM, PROGRAM_PERCENTAGE, 'Secondary Program');

/**
 * Activity Preview Program section
 * @author Daniel Oliva
 */
class Program extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (<div>
      <div className={'primary_sector'}>
        <PrimaryProgramList key="primary-programs-list" {...this.props} 
        percentTitleClass={'percent_field_name'} percentValueClass={'percent_field_value'}/>
      </div>
      <div className={'secondary_sector'}>
        <SecondaryProgramList key="secondary-programs-list" {...this.props} 
        percentTitleClass={'percent_field_name'} percentValueClass={'percent_field_value'}/>
      </div>
    </div>);
  }

}

export default Section(Program, 'Program', true, 'AcProgram');
