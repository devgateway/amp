import React, { Component } from 'react';
import Section from './Section';
import * as AC from '../../utils/ActivityConstants';

/**
 *    
 */
class Identification extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const { buildSimpleField } = this.props;
    const { activity, settings } = this.props.params;
    const fieldPaths = [AC.STATUS_REASON, AC.OBJECTIVE, AC.DESCRIPTION, AC.PROJECT_COMMENTS, 
      AC.CRIS_NUMBER, AC.ACTIVITY_BUDGET];
    const budgetPaths = [AC.MINISTRY_CODE, AC.PROJECT_CODE, AC.VOTE, AC.SUB_VOTE, AC.SUB_PROGRAM];
    const translations = this.props.params.translations;
    const noDataValue = translations['amp.activity-preview:noData'];
    const inline = this.props.params.inline;
    return (
      <div>
        {fieldPaths.map(fieldPath => buildSimpleField(activity, fieldPath, settings, true, noDataValue, inline))}
        {(activity[AC.ACTIVITY_BUDGET].value === 'On Budget' || activity[AC.ACTIVITY_BUDGET].value === 'Dans le Budget') &&
          budgetPaths.map(fieldPath => buildSimpleField(activity, fieldPath, settings, true, noDataValue, inline))
        }
      </div>
    );
  }
}

export default Section(Identification, 'Identification', true, 'AcIdentification');
