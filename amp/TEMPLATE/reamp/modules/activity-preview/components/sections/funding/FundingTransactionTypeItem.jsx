import React, { Component } from 'react';
import * as AC from '../../../utils/ActivityConstants';
import FundingItem from './FundingItem';
import FundingTotalItem from './FundingTotalItem';
import Label from '../../fields/Label';

require('../../../styles/ActivityView.css');

/**
 *    
 */
class FundingTransactionTypeItem extends Component {

  constructor(props, context) {
    super(props);
  }

  _sortTrxDateAsc(t1, t2) {
    return t1[AC.TRANSACTION_DATE].value > t2[AC.TRANSACTION_DATE].value ? 1 : -1;
  }

  _sortTrxDateDesc(t1, t2) {
    return t1[AC.TRANSACTION_DATE].value > t2[AC.TRANSACTION_DATE].value ? -1 : 1;
  }  

  _sortTrxIdAsc(t1, t2) {
    return t1[AC.TRANSACTION_ID].value > t2[AC.TRANSACTION_ID].value ? 1 : -1;
  }  

  _sortTrxIdDesc(t1, t2) {
    return t1[AC.TRANSACTION_ID].value > t2[AC.TRANSACTION_ID].value ? -1 : 1;
  }

  _sortTrxs(trxs) {
    let reorderId = this.props.settings[AC.REORDER_TRX];
    switch (reorderId) {
      case 1:
        trxs = trxs.sort(this._sortTrxDateDesc);
        break;
      case 2:
        trxs = trxs.sort(this._sortTrxDateAsc);
        break;
      case 3:
        trxs = trxs.sort(this._sortTrxIdDesc);
        break;      
      case 4:
        trxs = trxs.sort(this._sortTrxIdAsc);
        break;
    }
    return trxs;
  }

  _drawHeader() {    
    const label = `${this.props.group.adjustment_type.value} ${this.props.group.transaction_type.value}`;
    const key = 'TTI_' + this.props.group.adjustment_type.value + this.props.group.transaction_type.value;
  
    return (<div><Label label={label} labelClass={'header'} key={key} /></div>);
  }

  _drawDetail() {
    const content = [];
    let trxs = this.props.group[AC.TRANSACTIONS].value;
    const sortedTrxs = this._sortTrxs(trxs);
    sortedTrxs.forEach((item) => {
      content.push(<FundingItem key={'FI_' + Math.random()} item={item} 
        adjustment_type={this.props.group[AC.ADJUSTMENT_TYPE].value} 
        transaction_type={this.props.group[AC.ADJUSTMENT_TYPE].value} 
        settings={this.props.settings} />);
    });
    return <table className={'funding_table'} >{content}</table>;
  }

  _drawSubTotalFooter() {
    let subtotal = this.props.group[AC.SUBTOTAL].value;
    const measure = `${this.props.group.adjustment_type.value} ${this.props.group.transaction_type.value}`;
    const labelTrn = `${this.props.translations['subtotal']} ${measure}`.toUpperCase();
    return (<div>
      <FundingTotalItem
        value={subtotal}
        label={labelTrn}
        currency={AC.DEFAULT_CURRENCY}
        key={'FTI_' + this.props.group.adjustment_type.value + this.props.group.transaction_type.value}
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
