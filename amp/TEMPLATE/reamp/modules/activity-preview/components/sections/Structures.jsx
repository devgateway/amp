import React, { Component } from 'react';
import Section from './Section';
import * as AC from '../../utils/ActivityConstants';

require('../../styles/ActivityView.css');

/**
 * Activity Preview Structures section
 * @author Daniel Oliva
 */
class Structures extends Component {


  constructor(props) {
    super(props);
  }

  getCoordinates(structure) {
    const { buildSimpleField } = this.props;
    let isPoint = false;
    if (!structure[AC.STRUCTURES_SHAPE].value) {
      if (!structure[AC.STRUCTURES_LATITUDE].value || !structure[AC.STRUCTURES_LONGITUDE].value) {
        isPoint = false;
      } else {
        isPoint = true;
      }
    } else if (structure[AC.STRUCTURES_SHAPE].value === AC.STRUCTURES_POINT) {
      isPoint = true;
    } else {
      isPoint = false;
    }
    if (isPoint) {
      const content = [];
      content.push(buildSimpleField(structure, AC.STRUCTURES_LATITUDE, true, null, false));
      content.push(buildSimpleField(structure, AC.STRUCTURES_LONGITUDE, true, null, false));
      return content;
    } else {
      if (structure[AC.STRUCTURES_COORDINATES] && structure[AC.STRUCTURES_COORDINATES].value.length > 0) {
        const content = [];
        structure[AC.STRUCTURES_COORDINATES].value.forEach(c => {
          content.push(
            <tr>
              <td>{buildSimpleField(c, AC.STRUCTURES_LATITUDE, true, null, true)}</td>
              <td>{buildSimpleField(c, AC.STRUCTURES_LONGITUDE, true, null, true)}</td>
            </tr>);
        });
        return (
          <table className={'structures_coordinates_table'}>
            <tbody>
              <thead>
                <th><span className={'section_field_name'}>{this.props.params.translations['Coordinates']}</span></th>
              </thead>
              {content}
            </tbody>
          </table>);
      } else {
        return (<div></div>);
      }
    }
  }

  render() {    
    const { buildSimpleField } = this.props;
    const activity = this.props.params.activity;
    if (activity[AC.STRUCTURES]) {
      return (
        <div>{activity[AC.STRUCTURES].value.sort((a, b) => (a[AC.STRUCTURES_TITLE] > b[AC.STRUCTURES_TITLE])).map(s => (
          <div key={Math.random()}>
            <div className={'structure_title'}>{s[AC.STRUCTURES_TITLE].value}</div>
            {buildSimpleField(s, AC.STRUCTURES_TITLE, true, null, false)}
            {buildSimpleField(s, AC.STRUCTURES_DESCRIPTION, false, null, false)}
            {this.getCoordinates(s)}
          </div>)
        )}
        </div>
      );
    }
    return <div key={'structNodata'} className={'nodata'}>{translations['amp.activity-preview:noData']}</div>;
  }
}

export default Section(Structures, 'Structures', true, 'AcStructures');
