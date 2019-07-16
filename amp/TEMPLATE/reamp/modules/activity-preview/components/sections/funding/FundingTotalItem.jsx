import React, { Component } from 'react';
import { rawNumberToFormattedString } from '../../../utils/NumberUtils';
import * as AC from '../../../utils/ActivityConstants';
require('../../../styles/ActivityView.css');

/**
 *    
 */
class FundingTotalItem extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    let val = this.props.dontFormatNumber ? this.props.value : 
        rawNumberToFormattedString(this.props.value, false, this.props.settings);
        
    val = this.props.isPercentage ? val + '%' : val + ' ' + this.props.settings[AC.EFFECTIVE_CURRENCY][AC.CURRENCY_CODE];
    return (
    <div>
      <div key={'Total_Item_Label_' + Math.random()} className={'subtotal_footer_legend'}>
        {`${this.props.label}:`}
      </div>
      <div key={'Total_Item__Val' + Math.random()} className={'subtotal_footer_number'}>
        {`${val}`}
      </div>
    </div>);
  }
}

export default FundingTotalItem;
