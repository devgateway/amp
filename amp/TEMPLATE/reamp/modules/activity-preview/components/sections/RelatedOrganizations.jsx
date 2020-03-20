import React, { Component } from 'react';
import Section from './Section';
import * as AC from '../../utils/ActivityConstants';


/**
 *    
 */
class RelatedOrganizations extends Component {

  constructor(props) {
    super(props);
  }

  _buildRelatedOrganizations() {
    const { activity, translations, settings } = this.props.params;
    const dOrg = activity[AC.DONOR_ORGANIZATION] && activity[AC.DONOR_ORGANIZATION].value ? 
      activity[AC.DONOR_ORGANIZATION].value : undefined;
    const rOrg = activity[AC.RESPONSIBLE_ORGANIZATION] && activity[AC.RESPONSIBLE_ORGANIZATION] ? 
      activity[AC.RESPONSIBLE_ORGANIZATION].value : undefined;
    const eAge = activity[AC.EXECUTING_AGENCY] && activity[AC.EXECUTING_AGENCY].value ? 
      activity[AC.EXECUTING_AGENCY].value : undefined;

    const relatedOrganizations = [];
    if (dOrg) {
      for(var id in activity[AC.DONOR_ORGANIZATION].value)
      relatedOrganizations.push(this.props.buildSimpleField(dOrg, AC.ORGANIZATION, settings, true, false, false, translations ['DonorOrganization']));
    }
    if (rOrg) {
      relatedOrganizations.push(this.props.buildSimpleField(rOrg, AC.ORGANIZATION, settings, true, false, false, translations ['ResponsibleOrganization']));
    }
    if (eAge) {
      relatedOrganizations.push(this.props.buildSimpleField(eAge, AC.ORGANIZATION, settings, true, false, false, translations ['ExecutingAgency']));
    }
    if (!dOrg && !rOrg && !eAge){
      relatedOrganizations.push(<div key={'relatedOrgNodata'} 
        className={'nodata'}>{translations['amp.activity-preview:noData']}</div>);
    }

    return relatedOrganizations;
  }

  render() {
    return <div>{this._buildRelatedOrganizations()}</div>;
  }
}

export default Section(RelatedOrganizations, 'RelatedOrganizations', true, 'AcRelatedOrganizations');
