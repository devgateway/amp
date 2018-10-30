import React, { Component } from 'react';
import * as AC from '../../../utils/ActivityConstants';
import FundingTotalItem from './FundingTotalItem';
import Label from '../../fields/Label';

/**
 *    
 */
class FundingTotalsSection extends Component {

  constructor(props, context) {
    super(props);
  }

  render() {
    const content = [];
    const {activity, translations, settings} = this.props;

    const measuresTotals = {};
    // Commitments, Disbursements
    AC.TRANSACTION_TYPES.forEach(trnType => {
      AC.ADJUSTMENT_TYPES.forEach(adjType => {        
        let trx = activity[AC.FUNDING_TOTALS].value[AC.TOTALS].find(t => 
          t[AC.TRANSACTION_TYPE] === trnType && t[AC.ADJUSTMENT_TYPE] === adjType);
        let value = trx ? trx[AC.AMOUNT] : 0;
        measuresTotals[`${adjType} ${trnType}`] = value;
      });      
    });

    const actualCommitments = measuresTotals[AC.ACTUAL_COMMITMENTS];
    const plannedCommitments = measuresTotals[AC.PLANNED_COMMITMENTS];
    const actualDisbursements = measuresTotals[AC.ACTUAL_DISBURSEMENTS];
    const plannedDisbursements = measuresTotals[AC.PLANNED_DISBURSEMENTS];
    const undisbursed = activity[AC.FUNDING_TOTALS].value[AC.UNDISBURSED_BALANCE];
    const rate = activity[AC.FUNDING_TOTALS].value[AC.DELIVERY_RATE_PROP] ? activity[AC.FUNDING_TOTALS].value[AC.DELIVERY_RATE_PROP] : 0;
    const options = [
      { label: translations['amp.activity-preview:totalPlannedCommitments'], value: plannedCommitments, format: true, isPercentage: false, showIfZero: false },
      { label: translations['amp.activity-preview:totalActualCommitments'], value: actualCommitments, format: true, isPercentage: false, showIfZero: false },
      { label: translations['amp.activity-preview:totalPlannedDisbursements'], value: plannedDisbursements, format: true, isPercentage: false, showIfZero: false },
      { label: translations['amp.activity-preview:totalActualDisbursements'], value: actualDisbursements, format: true, isPercentage: false, showIfZero: false },
      { label: translations['total_undisbursed_balance'], value: undisbursed, format: true, isPercentage: false, showIfZero: true},
      { label: translations['delivery_rate'], value: rate, format: false, isPercentage: true, showIfZero: true }
    ];
    //content.push(<div key={'Funding_TSL'}><Label label={translations['total']} labelClass={'header_total'} key={'TotalFundingLabel' + Math.random()} /></div>);

    for(var g in options) {
      if (options[g].value > 0 || options[g].showIfZero) {
        content.push(<FundingTotalItem
          key={'Totals_' + Math.random() }
          value={options[g].value}
          label={options[g].label.toUpperCase()}
          dontFormatNumber={!options[g].format}
          isPercentage={options[g].isPercentage}
          settings={settings} />);
      }
    }
    
    return (<div key={'Funding_Total_Section' + Math.random()}>{content}</div>);
  }
}

export default FundingTotalsSection;
