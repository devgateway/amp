import React, { Component } from 'react';
import Section from './Section';
import SimpleField from '../fields/SimpleField';
import Tablify from '../fields/Tablify';
import * as AC from '../../utils/ActivityConstants';
import DateUtils from '../../utils/DateUtils';
require('../../styles/ActivityView.css');

/**
 *    
 */
class Planning extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const columnNumber = 3;
    const { activity, translations, settings } = this.props.params;
    const inline = this.props.styles.inline;
    let content = [];
    const fieldPaths = [
      AC.PROPOSED_PROJECT_LIFE,
      AC.PROPOSED_APPROVAL_DATE, 
      AC.PROPOSED_COMPLETION_DATE, 
      AC.ACTUAL_COMPLETION_DATE, 
      AC.ACTUAL_APPROVAL_DATE, 
      AC.PROPOSED_START_DATE,
      AC.ACTUAL_START_DATE];

    const showIfNotAvailable = new Set([AC.PROPOSED_PROJECT_LIFE, AC.PROPOSED_APPROVAL_DATE, AC.ACTUAL_APPROVAL_DATE, 
      AC.PROPOSED_START_DATE, AC.ACTUAL_START_DATE, AC.PROPOSED_COMPLETION_DATE, AC.ACTUAL_COMPLETION_DATE]);

    let endDateHelper = activity[AC.ACTUAL_COMPLETION_DATE].value ? activity[AC.ACTUAL_COMPLETION_DATE].value : activity[AC.PROPOSED_COMPLETION_DATE].value;
    let startDate = DateUtils.createFormattedDate(activity[AC.ACTUAL_START_DATE].value, settings);
    let endDate = DateUtils.createFormattedDate(endDateHelper, settings);
    let duration = translations['amp.activity-preview:noData'];
    if (startDate && endDate) {
      duration = DateUtils.durationImproved(startDate, endDate, settings) + ' ' + translations['months'];
    }
    content.push(
      <SimpleField key={'Duration'} 
      title={translations['Duration']} value={duration} inline={inline} separator={false}
      fieldNameClass={this.props.styles.fieldNameClass || ''}
      fieldValueClass={this.props.styles.fieldValueClass || ''} />
    );
    
    content = content.concat(fieldPaths.map(fieldPath =>
      this.props.buildSimpleField(activity, fieldPath, settings, showIfNotAvailable.has(fieldPath), inline, false)
    ).filter(data => data !== undefined));

    const tableContent = Tablify.addRows('Planning', content, columnNumber);
    return <div><table className={'box_table'}><tbody>{tableContent}</tbody></table></div>;
  }

}

export default Section(Planning, 'Planning', true, 'AcPlanning');
