import React, { Component } from 'react';
import Section from '../Section';
import FundingOrganizationSection from './FundingOrganizationSection';
import * as AC from '../../../utils/ActivityConstants';
require('../../../styles/ActivityView.css');

/**
 * Total Number of Fundings section
 * @author Daniel Oliva
 */
class FundingSection extends Component {

  constructor(props) {
    super(props);
  }

  _compareFundings(f1, f2) {
    let f1String = '';
    let f2String = '';
    switch (f1.transaction_type.value) {
      case AC.COMMITMENTS:
        f1String += 'a';
        break;
      case AC.DISBURSEMENTS:
        f1String += 'b';
        break;
      default:
        f1String += 'c';
        break;
    }
    switch (f1.adjustment_type.value) {
      case AC.PLANNED:
        f1String += 'a';
        break;
      case AC.ACTUAL:
        f1String += 'b';
        break;
      default:
        f1String += 'c';
        break;
    }
    switch (f2.transaction_type.value) {
      case AC.COMMITMENTS:
        f2String += 'a';
        break;
      case AC.DISBURSEMENTS:
        f2String += 'b';
        break;
      default:
        f2String += 'c';
        break;
    }
    switch (f2.adjustment_type.value) {
      case AC.PLANNED:
        f2String += 'a';
        break;
      case AC.ACTUAL:
        f2String += 'b';
        break;
      default:
        f2String += 'c';
        break;
    }
    return f1String > f2String ? 1 : -1;
  }

  render() {
    const { activity, translations, settings }  = this.props.params;
    const fundingList = [];
    let counter = 1;
    if (activity.fundings && activity.fundings.value && activity.fundings.value.length) {
      activity.fundings.value.forEach((funding) => {
        const item = (<FundingOrganizationSection
          funding={funding} key={funding[AC.FUNDING_DONOR_ORG_ID].value}
          counter={counter} comparator={this._compareFundings}
          buildSimpleField={this.props.buildSimpleField} 
          translations={translations} settings={settings}/>);
        fundingList.push(item);
        counter += 1;
      });
    }
    return (<div className={'container_funding'}>
      <div>{fundingList}</div>
      
      <div className={'clear'} />
    </div>);
  }
}

export default Section(FundingSection, 'Funding', true, 'AcFunding');
