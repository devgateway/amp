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
    const activity = this.props.params.activity;
    const translations = this.props.params.translations;
    const noDataValue = translations['amp.activity-preview:noData'];
    const fieldPaths = [AC.AMP_ID, AC.ACTIVITY_STATUS, AC.ACTIVITY_BUDGET];
    const inline = this.props.styles.inline;
    const options = {
      fieldNameClass: this.props.styles.fieldNameClass,
      fieldValueClass: this.props.styles.fieldValueClass,
      titleClass: this.props.styles.titleClass,
      groupClass: this.props.styles.groupClass
    };
    return (
      <div>
        {fieldPaths.map(fieldPath => buildSimpleField(activity, fieldPath, true, noDataValue, inline, options))}
      </div>
    );
  }
}

export default Section(StatusBar);
