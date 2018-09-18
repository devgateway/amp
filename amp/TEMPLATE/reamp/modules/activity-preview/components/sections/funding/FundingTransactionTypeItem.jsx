import React, { Component } from 'react';
import * as AC from '../../../utils/ActivityConstants';
import FundingItem from './FundingItem';
import FundingTotalItem from './FundingTotalItem';
import Label from '../../fields/Label';
import ActivityFundingTotals from '../../activity/ActivityFundingTotals';

require('../../../styles/ActivityView.css');

/**
 * @author Daniel Oliva
 */
class FundingTransactionTypeItem extends Component {

  constructor(props, context) {
    super(props);
  }

  _filterFundingDetails() {
    const trnType = this.props.group.trnType;
    const adjType = this.props.group.adjType;
    return (this.props.fundingDetails.filter(o => 
      o[AC.TRANSACTION_TYPE].value === trnType && o[AC.ADJUSTMENT_TYPE].value === adjType
    ));
  }

  _drawHeader() {    
    const label = `${this.props.group.adjType} ${this.props.group.trnType}`;
    const key = 'TTI_' + this.props.group.adjType + this.props.group.trnType;
  
    return (<div><Label label={label} labelClass={'header'} key={key} /></div>);
  }

  _drawDetail() {
    const filteredFD = this._filterFundingDetails();
    const content = [];
    filteredFD.forEach((item) => {
      content.push(<FundingItem key={'FI_' + Math.random()} 
        item={item} wsCurrency={AC.DEFAULT_CURRENCY} settings={this.props.settings} />);
    });
    return <table className={'funding_table'} >{content}</table>;
  }

  _drawSubTotalFooter() {
    let subtotal = 0;
    subtotal = ActivityFundingTotals.convertFundingDetailsToCurrency(this._filterFundingDetails(), AC.DEFAULT_CURRENCY);
    const measure = `${this.props.group.adjType} ${this.props.group.trnType}`;
    const labelTrn = `Subtotal ${measure}`.toUpperCase();
    return (<div>
      <FundingTotalItem
        value={subtotal}
        label={labelTrn}
        currency={AC.DEFAULT_CURRENCY}
        key={'FTI_' + this.props.group.adjType + this.props.group.trnType}
        settings={this.props.settings} />
    </div>);
  }

  render() {
    return (<div className={'table_container'} >
      <div>{this._drawHeader()}</div>
      <div>{this._drawDetail()}</div>
      <div>{this._drawSubTotalFooter()}</div>
    </div>);
  }
}

export default FundingTransactionTypeItem;
