import React, { Component } from 'react';
import { createFormattedDate } from '../../../utils/DateUtils';
import * as AC from '../../../utils/ActivityConstants';
import Actor from './Actor';
require('../../../styles/ActivityView.css');


/**
 * @author Daniel Oliva
 */
export default class Measures extends Component {
 
  constructor(props) {
    super(props);
  }

  _buildMeasure(measure) {
    const content = [];
    let date = '';
    if (measure[AC.MEASURE_DATE] && measure[AC.MEASURE_DATE].value) {
      date = ` ${createFormattedDate(measure[AC.MEASURE_DATE].value)}`;
    }
    const measureName = `${measure[AC.MEASURE_NAME].value || ''}${date}`;
    content.push(<div key={'Measure' + Math.random()} className={'measures'}>{measureName}</div>);
    if (measure[AC.ACTORS] && measure[AC.ACTORS].values) {
      measure[AC.ACTORS].values.forEach((actor) => {
        content.push(<Actor key={'Actor'} actor={actor} />);
      });
    }
    return content;
  }

  render() {
    if (this.props.measure) {
      return <div key={'RenderMeasure' + Math.random()}>{this._buildMeasure(this.props.measure)}</div>;
    } else {
      return null;
    }
  }
}
