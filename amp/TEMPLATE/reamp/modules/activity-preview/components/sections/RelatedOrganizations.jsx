import React, { Component } from 'react';
import Section from './Section';
import * as AC from '../../utils/ActivityConstants';


/**
 * @author Daniel Oliva
 */
class RelatedOrganizations extends Component {

  constructor(props) {
    super(props);
  }

  _buildRelatedOrganizations() {
    const { activity, translations } = this.props.params;
    const dOrg = activity[AC.DONOR_ORGANIZATION] && activity[AC.DONOR_ORGANIZATION].value ? 
      activity[AC.DONOR_ORGANIZATION].value[0] : undefined;
    const rOrg = activity[AC.RESPONSIBLE_ORGANIZATION] && activity[AC.RESPONSIBLE_ORGANIZATION] ? 
      activity[AC.RESPONSIBLE_ORGANIZATION].value[0] : undefined;
    const eAge = activity[AC.EXECUTING_AGENCY] && activity[AC.EXECUTING_AGENCY].value ? 
      activity[AC.EXECUTING_AGENCY].value[0] : undefined;

    const relatedOrganizations = [];
    if (dOrg) {
      relatedOrganizations.push(this.props.buildSimpleField(dOrg, AC.ORGANIZATION, true, false, false, translations ['DonorOrganization']));
    }
    if (rOrg) {
      relatedOrganizations.push(this.props.buildSimpleField(rOrg, AC.ORGANIZATION, true, false, false, translations ['ResponsibleOrganization']));
    }
    if (eAge) {
      relatedOrganizations.push(this.props.buildSimpleField(eAge, AC.ORGANIZATION, true, false, false, translations ['ExecutingAgency']));
    }

    return relatedOrganizations;
  }

  render() {
    return <div>{this._buildRelatedOrganizations()}</div>;
  }
}

export default Section(RelatedOrganizations, 'RelatedOrganizations', true, 'AcRelatedOrganizations');
