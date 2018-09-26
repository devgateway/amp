import React, { Component } from 'react';
import Section from './Section';
import * as AC from '../../utils/ActivityConstants';

require('../../styles/ActivityView.css');

/**
 * Activity Preview Structures section
 *    
 */
class Structures extends Component {


  constructor(props) {
    super(props);
  }

  getCoordinates(structure, settings) {
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
      content.push(buildSimpleField(structure, AC.STRUCTURES_LATITUDE, settings, true, null, false));
      content.push(buildSimpleField(structure, AC.STRUCTURES_LONGITUDE, settings, true, null, false));
      return content;
    } else {
      if (structure[AC.STRUCTURES_COORDINATES] && structure[AC.STRUCTURES_COORDINATES].value && structure[AC.STRUCTURES_COORDINATES].value.length > 0) {
        const content = [];
        structure[AC.STRUCTURES_COORDINATES].value.forEach(c => {
          content.push(
            <tr>
              <td>{buildSimpleField(c, AC.STRUCTURES_LATITUDE, settings, true, null, true)}</td>
              <td>{buildSimpleField(c, AC.STRUCTURES_LONGITUDE, settings, true, null, true)}</td>
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
    const { activity, translations, settings } = this.props.params;
    if (activity[AC.STRUCTURES].value.length > 0) {
      return (
        <div>{activity[AC.STRUCTURES].value.sort((a, b) => (a[AC.STRUCTURES_TITLE] > b[AC.STRUCTURES_TITLE])).map(s => (
          <div key={Math.random()}>
            <div className={'structure_title'}>{s[AC.STRUCTURES_TITLE].value}</div>
            {buildSimpleField(s, AC.STRUCTURES_TITLE, settings, true, null, false)}
            {buildSimpleField(s, AC.STRUCTURES_DESCRIPTION, settings, false, null, false)}
            {this.getCoordinates(s, settings)}
          </div>)
        )}
        </div>
      );
    }
    return <div key={'structNodata'} className={'nodata'}>{translations['amp.activity-preview:noData']}</div>;
  }
}

export default Section(Structures, 'Structures', true, 'AcStructures');
