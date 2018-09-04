import React, { Component } from 'react';
import Section from './Section';
import * as AC from '../../utils/ActivityConstants';

/**
 * @author Daniel Oliva
 */
class Identification extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const { buildSimpleField } = this.props;
    const activity = this.props.params.activity;
    const fieldPaths = [AC.STATUS_REASON, AC.OBJECTIVE, AC.DESCRIPTION, AC.PROJECT_COMMENTS, 
      AC.CRIS_NUMBER, AC.ACTIVITY_BUDGET];
    const translations = this.props.params.translations;
    const noDataValue = translations['amp.activity-preview:noData'];
    const inline = this.props.params.inline;
    return (
      <div>
        {fieldPaths.map(fieldPath => buildSimpleField(activity, fieldPath, true, noDataValue, inline))}
      </div>
    );
  }
}

export default Section(Identification, 'Identification', true, 'AcIdentification');
