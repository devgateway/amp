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
    const { activity, settings, activityFieldsManager, featureManager } = this.props.params;
    const fieldPaths = [AC.STATUS_REASON, AC.OBJECTIVE, AC.DESCRIPTION, AC.PROJECT_COMMENTS, 
      AC.CRIS_NUMBER, AC.ACTIVITY_BUDGET];
    const budgetPaths = [AC.MINISTRY_CODE, AC.PROJECT_CODE, AC.VOTE, AC.SUB_VOTE, AC.SUB_PROGRAM];
    const translations = this.props.params.translations;
    const noDataValue = translations['amp.activity-preview:noData'];
    const inline = this.props.params.inline;
      // TODO check why we have on budget and dans le budget hardcoded should be the value non translated
      // if not in spanish wont work. We probably need the original value and to have it
      // non translated so we dont have to change the whole code.
    return (
        <div>
            {fieldPaths.map(fieldPath => buildSimpleField(activity, fieldPath, settings, true, noDataValue, inline,
                undefined, activityFieldsManager , featureManager))}

            {(activity[AC.ACTIVITY_BUDGET].value === 'On Budget' || activity[AC.ACTIVITY_BUDGET].value
                === 'Dans le Budget') && budgetPaths.map(fieldPath =>
                buildSimpleField(activity, fieldPath, settings, true, noDataValue, inline,undefined,
                    activityFieldsManager, featureManager))
            }
        </div>
    );
  }
}

export default Section(Identification, 'Identification', true, 'AcIdentification');
