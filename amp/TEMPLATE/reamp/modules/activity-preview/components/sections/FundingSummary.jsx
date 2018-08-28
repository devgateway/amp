import React, { Component } from 'react';
import Section from './Section';
import * as AC from '../../utils/ActivityConstants';
import ActivityFundingTotals from '../activity/ActivityFundingTotals';
import SimpleField from '../fields/SimpleField';


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
    const activity = this.props.activity[0];
    const measuresTotals = {};
    let actualCommitmentsAreEnabled = false;
    let actualDisbursementsAreEnabled = false;
    // Commitments, Disbursements
    AC.TRANSACTION_TYPES.forEach(trnType => {
      AC.ADJUSTMENT_TYPES.forEach(adjType => {
        if (adjType && trnType) {
          const value = ActivityFundingTotals.getTotals(activity, adjType, trnType);
          measuresTotals[`${adjType} ${trnType}`] = value;
        }
        // Save these 2 flags for "Delivery Rate".
        if (trnType === AC.COMMITMENTS && adjType === AC.ACTUAL) {
          actualCommitmentsAreEnabled = true;
        }
        if (trnType === AC.DISBURSEMENTS && adjType === AC.ACTUAL) {
          actualDisbursementsAreEnabled = true;
        }
      });      
    });
    
    // Other measures: "Delivery rate".
    if (actualCommitmentsAreEnabled && actualDisbursementsAreEnabled
      && measuresTotals[`${AC.ACTUAL} ${AC.DISBURSEMENTS}`] !== '0'
      && measuresTotals[`${AC.ACTUAL} ${AC.COMMITMENTS}`] !== '0') {
      let value = (measuresTotals[`${AC.ACTUAL} ${AC.DISBURSEMENTS}`]) / (measuresTotals[`${AC.ACTUAL} ${AC.COMMITMENTS}`]);
      value *= 100;
      value = `${value}%`;
      measuresTotals[AC.DELIVERY_RATE] = value;
    } else {
      measuresTotals[AC.DELIVERY_RATE] = '0%';
    }
    

    return this._buildTotalFields(measuresTotals);
  }

  _buildTotalFields(measuresTotals) {
    const measuresOrder = [
      { trn: AC.ACTUAL_COMMITMENTS, total: true },
      { trn: AC.PLANNED_COMMITMENTS, total: true },
      { trn: AC.ACTUAL_DISBURSEMENTS, total: true },
      { trn: AC.PLANNED_DISBURSEMENTS, total: true },
      { trn: AC.DELIVERY_RATE, total: false }];
    const fundingInfoSummary = [];
    measuresOrder.forEach(measure => {
      const value = measuresTotals[measure.trn];
      if (value !== undefined) {
        let title = measure.trn;
        if (measure.total) {
          title = `Total ${title}`;
        }
        const key = `Summary-Total-${measure.trn}`;
        fundingInfoSummary.push(<SimpleField
          key={key} title={title} value={value} separator={false}
          fieldNameClass={this.props.fieldNameClass} fieldValueClass={this.props.fieldValueClass} />);
      }
    });
    return fundingInfoSummary;
  }


  render() {
    return <div>{this._buildFundingInformation()}</div>;
  }
}

export default Section(FundingSummary, 'Funding Information');
