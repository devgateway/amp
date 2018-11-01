import React, { Component } from 'react';
import * as AC from '../../../utils/ActivityConstants';
import { createFormattedDate } from '../../../utils/DateUtils';
import { rawNumberToFormattedString } from '../../../utils/NumberUtils';

/**
 *    
 */
class FundingItem extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const amount = this.props.item[AC.TRANSACTION_AMOUNT].value;
    return (
      <tbody>
        <tr className={'row'}>
          <td className={'left_text'}>{this.props.adjustment_type}</td>
          <td className={'right_text'}>{createFormattedDate(this.props.item[AC.TRANSACTION_DATE].value, this.props.settings)}</td>
          <td className={'right_text'}>{`${rawNumberToFormattedString(amount, false, this.props.settings)} 
            ${this.props.settings[AC.EFFECTIVE_CURRENCY][AC.CURRENCY_CODE] || ''}`}</td>
        </tr>
      </tbody>);
  }
}

export default FundingItem;
