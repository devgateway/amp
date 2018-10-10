import React, { Component } from 'react';
import Section from '../Section';
import FundingOrganizationSection from './FundingOrganizationSection';
import FundingTotalsSection from './FundingTotalsSection';
import SimpleField from '../../fields/SimpleField';
import * as AC from '../../../utils/ActivityConstants';
require('../../../styles/ActivityView.css');

/**
 * Total Number of Fundings section
 *    
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

  _getSources() {
    const { activity, translations } = this.props.params;
    const title = translations['FundingSources'];
    var sources = new Set();
    if (activity[AC.FUNDINGS] && activity[AC.FUNDINGS].value.length) {
      activity[AC.FUNDINGS].value.forEach(v=> {sources.add('[' + v.donor_organization_id.value + '] ')});
    }
    let value_ = sources.size + ' ';
    sources.forEach(x => value_ += x); 
    const content = <SimpleField key={title}
      title={title} value={value_} 
      useInnerHTML={false}
      inline={false}
      separator={false}
      fieldNameClass={'section_field_name'}
      fieldValueClass={this.props.styles.fieldValueClass || ''} />;
    return (<div key={'AcFundingSources'} >{content}</div>);
    
  }

  render() {
    const { activity, translations, settings }  = this.props.params;
    const fundingList = [];
    let counter = 1;
    if (activity.fundings && activity.fundings.value && activity.fundings.value.length) {
      activity.fundings.value.forEach((funding) => {
        const item = (<FundingOrganizationSection
          funding={funding} key={funding[AC.FUNDING_ID].value}
          counter={counter} comparator={this._compareFundings}
          buildSimpleField={this.props.buildSimpleField} 
          translations={translations} settings={settings}/>);
        fundingList.push(item);
        counter += 1;
      });
    }
    const totals = (<FundingTotalsSection key={'FundingTotalsSection'} activity={activity} 
      translations={translations} settings={settings}/>);
    fundingList.push(totals);

    return (
    <div className={'container_funding'}>
      
      <div>{fundingList}</div>
      
      <div className={'clear'} />
    </div>);
  }
}

export default Section(FundingSection, 'Funding', true, 'AcFunding');
