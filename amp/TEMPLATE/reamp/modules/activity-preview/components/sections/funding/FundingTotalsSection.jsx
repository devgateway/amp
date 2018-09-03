import React, { Component } from 'react';
import * as AC from '../../../utils/ActivityConstants';
import FundingTotalItem from './FundingTotalItem';
import ActivityFundingTotals from '../../activity/ActivityFundingTotals';


/**
 * @author Daniel Oliva
 */
class FundingTotalsSection extends Component {

  constructor(props, context) {
    super(props);
  }

  render() {
    const content = [];
    const translations = this.props.translations;
    const actualCommitments = ActivityFundingTotals.buildStandardMeasureTotal(null, AC.ACTUAL, AC.COMMITMENTS);
    const plannedCommitments = ActivityFundingTotals.buildStandardMeasureTotal(null, AC.PLANNED, AC.COMMITMENTS);
    const actualDisbursements = ActivityFundingTotals.buildStandardMeasureTotal(null, AC.ACTUAL, AC.DISBURSEMENTS);
    const plannedDisbursements = ActivityFundingTotals.buildStandardMeasureTotal(null, AC.PLANNED, AC.DISBURSEMENTS);
    const options = [{ label: translations['amp.activity-preview:totalActualCommitments'], value: actualCommitments },
      { label: translations['amp.activity-preview:totalPlannedCommitments'], value: plannedCommitments },
      { label: translations['amp.activity-preview:totalActualDisbursements'], value: actualDisbursements },
      { label: translations['amp.activity-preview:totalPlannedDisbursements'], value: plannedDisbursements },];
    options.forEach(g => {
      if (g.value > 0) {
        content.push(<FundingTotalItem
          key={Utils.numberRandom()}
          currency={AC.DEFAULT_CURRENCY}
          value={g.value}
          label={g.label} />);
      }
    });
    if (actualDisbursements !== 0 && plannedDisbursements !== 0) {
      content.push(<FundingTotalItem
        label={translations['amp.activity-preview:undisbursedBalance']} value={actualCommitments - actualDisbursements}
        currency={AC.DEFAULT_CURRENCY} key={Utils.numberRandom()} />);
    }
    if (actualDisbursements !== 0 && plannedDisbursements !== 0) {
      content.push(<FundingTotalItem
        currency={AC.DEFAULT_CURRENCY} key={Utils.numberRandom()}
        value={Math.round((actualDisbursements / actualCommitments) * 100)}
        label={translations['amp.activity-preview:deliveryRate']} isPercentage />);
    }
    return (<div>{content}</div>);
  }
}

export default FundingTotalsSection;
