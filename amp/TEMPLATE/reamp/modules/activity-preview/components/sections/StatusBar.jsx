import React, { Component } from 'react';
import Section from './Section';
import * as AC from '../../utils/ActivityConstants';


class StatusBar extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    const activity = this.props.activity;
    const inline = this.props.inline;
    const translations = this.props.translations;
    const noDataValue = translations['amp.activity-preview:noData'];
    const fieldPaths = [AC.AMP_ID, AC.ACTIVITY_STATUS, AC.ACTIVITY_BUDGET];
    const options = {
      fieldNameClass: this.props.fieldNameClass,
      fieldValueClass: this.props.fieldValueClass,
      titleClass: this.props.titleClass,
      groupClass: this.props.groupClass,
    };
    return (
      <div>
        {fieldPaths.map(fieldPath => Section(activity, fieldPath, true, noDataValue, inline, options))}
      </div>
    );
  }
}

export default StatusBar;
