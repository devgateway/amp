import React, { Component, PropTypes } from 'react';
import Section from './Section';
import Tablify from '../fields/Tablify';
import * as AC from '../../utils/ActivityConstants';
require('../../styles/ActivityView.css');

/**
 *    
 */
const InternalIds = (isSeparateSection) => class extends Component {
  
  constructor(props) {
    super(props);
  }

  _getActInternalIdContent(actIntId, showInternalId) {
    let intId;
    if (actIntId[AC.INTERNAL_ID] && actIntId[AC.INTERNAL_ID].value ) {
      intId = <span className={'tableValue'}>{actIntId[AC.INTERNAL_ID].value}</span>;
    }
    return (
      <div key={actIntId[AC.ORGANIZATION].value}>
        <span>[{ actIntId[AC.ORGANIZATION].value }]</span>
        { intId }
      </div>);
  }

  buildContent() {
    const activity = this.props.params.activity;
    let orgIds = [];
    if (activity[AC.ACTIVITY_INTERNAL_IDS] && activity[AC.ACTIVITY_INTERNAL_IDS].value && activity[AC.ACTIVITY_INTERNAL_IDS].value.length > 0) {
      activity[AC.ACTIVITY_INTERNAL_IDS].value.forEach(actIntId => orgIds.push(this._getActInternalIdContent(actIntId)));
    }
    return orgIds.length > 0 ? orgIds : null;
  }

  render() {
    const columnNumber = 3;
    let content = this.buildContent();
    if (isSeparateSection === true) {
      // make sure content exists before formatting
      const noData = <tr><td>{this.props.params.translations['amp.activity-preview:noData']}</td></tr>;
      const tableContent = content ? Tablify.addRows('AcInternalIds', content, columnNumber) : noData;
      content = <div><table className={'box_table'}><tbody>{tableContent}</tbody></table></div>;
    } else if (content || this.props.showIfEmpty) {
      return (
        <div key="InternalIdsFromIdentificationSection">
          <ul>
            {content && content.map(orgData => (<li key={orgData.key}>{orgData}</li>))}
          </ul>
        </div>
      );
    }
    return content;
  }
};

export default Section(InternalIds(true), 'InternalIds', true, 'AcInternalIds');
