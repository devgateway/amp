import React, { Component } from 'react';
import Section from './Section';
import * as AC from '../../utils/ActivityConstants';

/**
 * @author Daniel Oliva
 */
class StatusBar extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { buildSimpleField } = this.props;
    const {activity, settings} = this.props.params;
    const translations = this.props.params.translations;
    const noDataValue = translations['amp.activity-preview:noData'];
    const fieldPaths = [AC.AMP_ID, AC.ACTIVITY_STATUS, AC.ACTIVITY_BUDGET];
    const inline = this.props.styles.inline;
    return (
      <div>
        {fieldPaths.map(fieldPath => buildSimpleField(activity, fieldPath, settings, true, noDataValue, inline))}
      </div>
    );
  }
}

export default Section(StatusBar);
