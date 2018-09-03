import React, { Component } from 'react';
import ActivityFundingTotals from '../../activity/ActivityFundingTotals';
import * as AC from '../../../utils/ActivityConstants';
import Tablify from '../../fields/Tablify';
import FundingTransactionTypeItem from './FundingTransactionTypeItem';
import FundingTotalItem from './FundingTotalItem';
require('../../../styles/ActivityView.css');

/**
 * @author Daniel Oliva
 */
class FundingOrganizationSection extends Component {

  constructor(props, context) {
    super(props);
  }

  _buildDonorInfo() {
    const content = [];
    const columnsNumber = 2;
    const { buildSimpleField, funding } = this.props;
    content.push(buildSimpleField(funding, AC.FUNDING_DONOR_ORG_ID, true, null, false));
    content.push(buildSimpleField(funding, AC.TYPE_OF_ASSISTANCE, true, null, false));
    content.push(buildSimpleField(funding, AC.FINANCING_INSTRUMENT, true, null, false));
    content.push(buildSimpleField(funding, AC.FUNDING_STATUS, true, null, false));
    content.push(buildSimpleField(funding, AC.ACTUAL_START_DATE, true, null, false));
    content.push(buildSimpleField(funding, AC.ACTUAL_COMPLETION_DATE, true, null, false));
    content.push(buildSimpleField(funding, AC.FINANCING_ID, true, null, false));
    
    const tableContent = Tablify.addRows('fundingOrganizationSection', content, columnsNumber);
    return tableContent;
  }


  _buildFundingDetailSection() {
    debugger;
    const content = [];
    // Group the list of funding details by adjustment_type and transaction_type.
    const fd = this.props.funding[AC.FUNDING_DETAILS] ? this.props.funding[AC.FUNDING_DETAILS].value : undefined;
    if (!fd || fd.length === 0) {
      // Dont show this section if there are no funding details.
      return null;
    }
    const groups = [];
    fd.forEach((item) => {
      const auxFd = {
        adjType: item[AC.ADJUSTMENT_TYPE].value,
        trnType: item[AC.TRANSACTION_TYPE].value,
        key: 'FOS_' + item[AC.ADJUSTMENT_TYPE].value + item[AC.TRANSACTION_TYPE].value,
        currency: item[AC.CURRENCY]
      };
      if (!groups.find(o => o.adjType === auxFd.adjType && o.trnType === auxFd.trnType)) {
        groups.push(auxFd);
      }
    });
    const sortedGroups = groups.sort(this.props.comparator);
    sortedGroups.forEach((group) => {
      content.push(<FundingTransactionTypeItem fundingDetails={fd} group={group} key={group.key} />);
    });
    return content;
  }

  _buildUndisbursedBalanceSection() {
    let totalActualDisbursements = 0;
    let totalActualCommitments = 0;
    const fd = this.props.funding[AC.FUNDING_DETAILS] ? this.props.funding[AC.FUNDING_DETAILS].value : undefined;
    if (!fd || fd.length === 0) {
      // Dont show this section if there are no funding details.
      return null;
    }
    const fdActualCommitments = fd.filter((item) =>
      item[AC.ADJUSTMENT_TYPE].value === AC.ACTUAL && item[AC.TRANSACTION_TYPE].value === AC.COMMITMENTS
    );
    totalActualCommitments = ActivityFundingTotals.convertFundingDetailsToCurrency(fdActualCommitments, AC.DEFAULT_CURRENCY);
    const fdActualDisbursements = fd.filter((item) =>
      item[AC.ADJUSTMENT_TYPE].value === AC.ACTUAL && item[AC.TRANSACTION_TYPE].value === AC.DISBURSEMENTS
    );
    totalActualDisbursements = ActivityFundingTotals.convertFundingDetailsToCurrency(fdActualDisbursements, AC.DEFAULT_CURRENCY);

    return (<div>
      <FundingTotalItem
        label={this.props.translations['amp.activity-preview:undisbursedBalance']} value={totalActualCommitments - totalActualDisbursements}
        currency={AC.DEFAULT_CURRENCY} key={'undisbursed-balance-key'} />
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
