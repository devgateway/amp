import React, { Component } from 'react';
import Section from './Section';
import PercentageList from '../fields/PercentageList';
import {
  PRIMARY_SECTORS,
  SECONDARY_SECTORS,
  SECTOR,
  SECTOR_PERCENTAGE
} from '../../utils/ActivityConstants';


const PrimarySectorList = PercentageList(PRIMARY_SECTORS, SECTOR, SECTOR_PERCENTAGE, 'Primary Sector');
const SecondarySectorList = PercentageList(SECONDARY_SECTORS, SECTOR, SECTOR_PERCENTAGE, 'Secondary Sector');


/**
 * Activity Preview Sector section
 * @author Daniel Oliva
 */
class Sector extends Component {
  
  constructor(props) {
    super(props);
  }

  render() {
    return (<div className={'sector_container'}>
      <div className={'primary_sector'}>
        <PrimarySectorList key="primary-programs-list" {...this.props} 
        percentTitleClass={'percent_field_name'} percentValueClass={'percent_field_value'}/>
      </div>
      <div className={'secondary_sector'}>
        <SecondarySectorList key="secondary-programs-list" {...this.props} 
        percentTitleClass={'percent_field_name'} percentValueClass={'percent_field_value'}/>
      </div>
    </div>);
  }
}

export default Section(Sector, 'Sectors', true, 'AcSector');
