import React, { Component } from 'react';
import Section from './Section';
import * as AC from '../../utils/ActivityConstants';
import NumberUtils from '../../utils/NumberUtils'
import DateUtils from '../../utils/DateUtils'
import SimpleField from '../fields/SimpleField';

/**
 *    
 */
class FundingSummary extends Component {

  constructor(props) {
    super(props);
  }

  /**
   * Builds "Funding Information" section by following AMP Activity Preview rules
   * @return {Section}
   * @private
   */
  _buildFundingInformation() {
    const { activity, translations, settings } = this.props.params;
    if (!activity[AC.FUNDING_TOTALS]) {
      return (<div></div>);
    }
    const measuresTotals = {};
    // Commitments, Disbursements
    AC.TRANSACTION_TYPES.forEach(trnType => {
      AC.ADJUSTMENT_TYPES.forEach(adjType => {        
        let trx = activity[AC.FUNDING_TOTALS].value[AC.TOTALS].find(t => 
          t[AC.TRANSACTION_TYPE] === trnType && t[AC.ADJUSTMENT_TYPE] === adjType);
        let value = trx ? trx[AC.AMOUNT] : 0;
        measuresTotals[`${adjType} ${trnType}`] = NumberUtils.rawNumberToFormattedString(value, false, this.props.params.settings);
      });      
    });
    
    // Other measures
    let undisbursed = activity[AC.FUNDING_TOTALS].value[AC.UNDISBURSED_BALANCE];
    measuresTotals[translations['undisbursed_balance']] = NumberUtils.rawNumberToFormattedString(undisbursed, false, this.props.params.settings);
    let rate = activity[AC.FUNDING_TOTALS].value[AC.DELIVERY_RATE_PROP] ? activity[AC.FUNDING_TOTALS].value[AC.DELIVERY_RATE_PROP] + '%' : '0%';
    measuresTotals[translations['delivery_rate']] = rate;
    
    let endDateHelper = activity[AC.ACTUAL_COMPLETION_DATE].value ? activity[AC.ACTUAL_COMPLETION_DATE].value : activity[AC.PROPOSED_COMPLETION_DATE].value;
    let startDate = DateUtils.createFormattedDate(activity[AC.ACTUAL_START_DATE].value, settings);
    let endDate = DateUtils.createFormattedDate(endDateHelper, settings);
    let duration = translations['amp.activity-preview:noData'];
    if (startDate && endDate) {
      duration = DateUtils.durationImproved(startDate, endDate, settings) + ' ' + translations['months'];
    }
    measuresTotals[translations['Duration']] = duration;

    return this._buildTotalFields(measuresTotals);
  }

  _buildTotalFields(measuresTotals) {
    const { translations} = this.props.params;
    const measuresOrder = [
      { trn: AC.PLANNED_COMMITMENTS, total: true },
      { trn: AC.ACTUAL_COMMITMENTS, total: true },
      { trn: AC.PLANNED_DISBURSEMENTS, total: true },
      { trn: AC.ACTUAL_DISBURSEMENTS, total: true },
      { trn: translations['undisbursed_balance'], total: false },
      { trn: translations['delivery_rate'], total: false },
      { trn: translations['Duration'], total: false }];
    const fundingInfoSummary = [];
    measuresOrder.forEach(measure => {
      let value = measuresTotals[measure.trn];
      if (value !== undefined) {
        let title = measure.trn;
        if(measure.total){
          title = translations[ 'amp.activity-preview:total' + title.replace(/\s/g, '')];
        }
        const key = `Summary-Total-${measure.trn}`;
        fundingInfoSummary.push(<SimpleField
          key={key} title={title} value={value} separator={false}
          fieldNameClass={this.props.styles.fieldNameClass} fieldValueClass={this.props.styles.fieldValueClass} />);
      }
    });

    return fundingInfoSummary;
  }


  render() {
    return <div>{this._buildFundingInformation()}</div>;
  }
}

export default Section(FundingSummary, 'FundingInformation', true, 'AcFundingSummary');
