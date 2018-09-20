import React, { Component } from 'react';
import * as AC from '../../../utils/ActivityConstants';
import FundingItem from './FundingItem';
import FundingTotalItem from './FundingTotalItem';
import Label from '../../fields/Label';

require('../../../styles/ActivityView.css');

/**
 * @author Daniel Oliva
 */
class FundingTransactionTypeItem extends Component {

  constructor(props, context) {
    super(props);
  }

  _drawHeader() {    
    const label = `${this.props.group.adjustment_type.value} ${this.props.group.transaction_type.value}`;
    const key = 'TTI_' + this.props.group.adjustment_type.value + this.props.group.transaction_type.value;
  
    return (<div><Label label={label} labelClass={'header'} key={key} /></div>);
  }

  _drawDetail() {
    const content = [];
    this.props.group[AC.TRANSACTIONS].value.forEach((item) => {
      content.push(<FundingItem key={'FI_' + Math.random()} item={item} 
        adjustment_type={this.props.group[AC.ADJUSTMENT_TYPE].value} transaction_type={this.props.group[AC.ADJUSTMENT_TYPE].value} 
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
