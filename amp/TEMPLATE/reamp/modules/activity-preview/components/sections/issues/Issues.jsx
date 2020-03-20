import React, { Component } from 'react';
import Section from '../Section';
import { createFormattedDate } from '../../../utils/DateUtils';
import * as AC from '../../../utils/ActivityConstants';
import Measure from './Measure';
require('../../../styles/ActivityView.css');

/**
 *    
 */
class Issues extends Component {

  constructor(props) {
    super(props);
  }

  _buildIssues(activity, settings) {
    let content = [];
    if (activity[AC.ISSUES]) {
      activity[AC.ISSUES].value.forEach((issue) => {
      let date = '';
      if (issue[AC.ISSUE_DATE]) {
          date = ` ${createFormattedDate(issue[AC.ISSUE_DATE].value, settings)}`;
        }
        content.push(<div key={'Issue_' + issue.name.value} className={'issues'}>{`${issue.name.value || ''}${date}`}</div>);
        issue[AC.MEASURES].value.forEach((measure) => {
          content.push(<Measure key={'Measure_' + Math.random()} measure={measure} settings={settings} />);
        });
      });
      if (content.length === 0) {
        content = (<div className={'nodata'}>{this.props.params.translations['amp.activity-preview:noData']}</div>);
      }
    }
    return content;
  }

  render() {
    const {activity, translations, settings} = this.props.params;
    if (activity[AC.ISSUES]) {
      return <div key={'RenderIssue_' + Math.random()} >{this._buildIssues(activity, settings)}</div>;
    } else {
      return <div className={'nodata'}>{translations['amp.activity-preview:noData']}</div>;
    }
  }
}

export default Section(Issues, 'Issues', true, 'AcIssues');
