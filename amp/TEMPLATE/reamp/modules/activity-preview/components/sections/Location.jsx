import React, {Component} from 'react';
import Section from './Section';
import PercentageList from '../fields/PercentageList';
import {
    IMPLEMENTATION_LEVEL,
    IMPLEMENTATION_LOCATION,
    LOCATION,
    LOCATION_PERCENTAGE,
    LOCATIONS
} from '../../utils/ActivityConstants';

require('../../styles/ActivityView.css');

/**
 *    
 */
const LocationsList = PercentageList(LOCATIONS, LOCATION, LOCATION_PERCENTAGE);
class Location extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const columnNumber = 3;
    const { activity, settings } = this.props.params;
    const noDataValue = this.props.params.translations['amp.activity-preview:noData'];
    let content = [<LocationsList key="locations-list"  {...this.props} 
      listField={LOCATIONS} percentageField={LOCATION_PERCENTAGE} 
      percentTitleClass={'percent_field_name'} percentValueClass={'percent_field_value'} 
      tablify={false} columns={columnNumber}/>];
    const topContent = [];
    topContent.push(<td key={IMPLEMENTATION_LEVEL}>{this.props.buildSimpleField(activity, IMPLEMENTATION_LEVEL, settings, noDataValue, true)} </td>);
    topContent.push(<td key={IMPLEMENTATION_LOCATION}>{this.props.buildSimpleField(activity, IMPLEMENTATION_LOCATION, settings, noDataValue, true)} </td>);
    content = content.filter(el => el !== undefined);
    let table = null;
    if ((activity[IMPLEMENTATION_LEVEL] && activity[IMPLEMENTATION_LEVEL].value !== 'National')
      || (activity[IMPLEMENTATION_LOCATION] && activity[IMPLEMENTATION_LOCATION].value !== 'Administrative Level 0')) {
      table = (<table className={'box_table2'}>
        <tbody>
          <tr><td>
            {content}
          </td></tr>
        </tbody>
      </table>);
    }
    return (<div>
      <table className={'two_box_table'}>
        <tbody>
          <tr key={'AcLocation'}>{topContent}</tr>
        </tbody>
      </table>
      {table}
    </div>);
  }
}

export default Section(Location, 'Location', true, 'AcLocation');
