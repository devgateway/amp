import React, { Component } from 'react';
import Section from './Section';
import SimpleField from '../fields/SimpleField';
import Tablify from '../fields/Tablify';
import * as AC from '../../utils/ActivityConstants';
import DateUtils from '../../utils/DateUtils';
require('../../styles/ActivityView.css');

/**
 * @author Daniel Oliva
 */
class Planning extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const columnNumber = 3;
    const activity = this.props.params.activity;
    const inline = this.props.styles.inline;
    let content = [];
    const fieldPaths = [
      AC.PROPOSED_APPROVAL_DATE, 
      AC.CREATION_DATE, 
      AC.PROPOSED_COMPLETION_DATE, 
      AC.ACTUAL_COMPLETION_DATE, 
      AC.ACTUAL_APPROVAL_DATE, 
      AC.PROPOSED_START_DATE,
      AC.ACTUAL_START_DATE];

    const showIfNotAvailable = new Set([AC.PROPOSED_APPROVAL_DATE, AC.ACTUAL_APPROVAL_DATE, AC.PROPOSED_START_DATE,
      AC.ACTUAL_START_DATE, AC.CREATION_DATE, AC.PROPOSED_COMPLETION_DATE, AC.ACTUAL_COMPLETION_DATE]);

    let startDate = DateUtils.createFormattedDate(activity[AC.ACTUAL_START_DATE].value);
    let endDate = DateUtils.createFormattedDate(activity[AC.ACTUAL_COMPLETION_DATE].value);
    let duration = this.props.params.translations['amp.activity-preview:noData'];
    if (startDate && endDate) {
      let res = DateUtils.durationImproved(startDate, endDate).split(' ');
      if (res.length > 0 ) {
        duration = res[0] + ' ' + this.props.params.translations[res[1]];
      }

    }
    content.push(
      <SimpleField key={'Duration'} 
      title={this.props.params.translations['Duration']} value={duration} inline={inline} separator={false}
      fieldNameClass={this.props.styles.fieldNameClass || ''}
      fieldValueClass={this.props.styles.fieldValueClass || ''} />
    );
    
    content = content.concat(fieldPaths.map(fieldPath =>
      this.props.buildSimpleField(activity, fieldPath, showIfNotAvailable.has(fieldPath), inline, false)
    ).filter(data => data !== undefined));

    const tableContent = Tablify.addRows('Planning', content, columnNumber);
    return <div><table className={'box_table'}><tbody>{tableContent}</tbody></table></div>;
  }

}

export default Section(Planning, 'Planning', true, 'AcPlanning');
