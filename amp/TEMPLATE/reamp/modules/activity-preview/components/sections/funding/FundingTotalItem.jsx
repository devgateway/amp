import React, { Component } from 'react';
import { rawNumberToFormattedString } from '../../../utils/NumberUtils';
require('../../../styles/ActivityView.css');

/**
 * @author Daniel Oliva
 */
class FundingTotalItem extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    let val = this.props.dontFormatNumber ? this.props.value : 
        rawNumberToFormattedString(this.props.value, false, this.props.settings);
        
    val = this.props.isPercentage === true ? `${val}%` : val;
    return (<div>
      <div className={'subtotal_footer_legend'}>
        {`${this.props.label}:`}
      </div>
      <div className={'subtotal_footer_number'}>
        {`${val} ${this.props.currency || ''}`}
      </div>
    </div>);
  }
}

export default FundingTotalItem;
