import React, { Component } from 'react';
import * as AC from '../../../utils/ActivityConstants';
import Tablify from '../../fields/Tablify';
import FundingTransactionTypeItem from './FundingTransactionTypeItem';
import FundingTotalItem from './FundingTotalItem';
require('../../../styles/ActivityView.css');

/**
 *    
 */
class FundingOrganizationSection extends Component {

  constructor(props, context) {
    super(props);
  }

  _buildDonorInfo() {
    const content = [];
    const columnsNumber = 2;
    const { buildSimpleField, funding, settings } = this.props;
    content.push(buildSimpleField(funding, AC.SOURCE_ROLE, settings, true, null, false));
    content.push(buildSimpleField(funding, AC.FUNDING_DONOR_ORG_ID, settings, true, null, false));
    content.push(buildSimpleField(funding, AC.FUNDING_STATUS, settings, true, null, false));
    content.push(buildSimpleField(funding, AC.TYPE_OF_ASSISTANCE, settings, true, null, false));
    content.push(buildSimpleField(funding, AC.FINANCING_INSTRUMENT, settings, true, null, false));
    content.push(buildSimpleField(funding, AC.FINANCING_ID, settings, true, null, false));
    
    const tableContent = Tablify.addRows('fundingOrganizationSection', content, columnsNumber);
    return tableContent;
  }


  _buildFundingDetailSection() {
    const content = [];
    // Group the list of funding details by adjustment_type and transaction_type.
    const fd = this.props.funding[AC.FUNDING_DETAILS] ? this.props.funding[AC.FUNDING_DETAILS].value : undefined;
    if (!fd || fd.length === 0) {
      // Dont show this section if there are no funding details.
      return null;
    }
    const sortedGroups = fd.sort(this.props.comparator);
    sortedGroups.forEach((group) => {
      content.push(<FundingTransactionTypeItem translations={this.props.translations} group={group} 
        key={'FTTI_' + Math.random()} settings={this.props.settings} />);
    });
    return content;
  }

  _buildUndisbursedBalanceSection() {
    let undirbursed = this.props.funding[AC.UNDISBURSED_BALANCE].value;
    return (<div>
      <FundingTotalItem
        label={this.props.translations['amp.activity-preview:undisbursedBalance']} value={undirbursed}
        key={'undisbursed-balance-key'} settings={this.props.settings} />
    </div>);
  }

  render() {
    return (<div>
      <div className={'section_header'}> {this.props.translations['amp.activity-preview:fundingItem']} 
        {this.props.counter} 
      </div>
      <table className={'two_box_table'}>
        <tbody>{this._buildDonorInfo()}</tbody>
      </table>
      <div className={'funding_detail'}>{this._buildFundingDetailSection()}</div>
      <div>{this._buildUndisbursedBalanceSection()}</div>
    </div>);
  }
}

export default FundingOrganizationSection;
