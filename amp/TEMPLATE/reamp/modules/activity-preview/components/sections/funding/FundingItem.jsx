import React, { Component } from 'react';
import * as AC from '../../../utils/ActivityConstants';
import { createFormattedDate } from '../../../utils/DateUtils';
import { rawNumberToFormattedString } from '../../../utils/NumberUtils';
import ActivityFundingTotals from '../../activity/ActivityFundingTotals';


/**
 * @author Daniel Oliva
 */
class FundingItem extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const convertedAmount = ActivityFundingTotals.convertTransactionAmountToCurrency(this.props.item,
      this.props.wsCurrency);
    return (
      <tbody>
        <tr className={'row'}>
          <td className={'left_text'}>{this.props.item[AC.ADJUSTMENT_TYPE].value}</td>
          <td className={'right_text'}>{createFormattedDate(this.props.item[AC.TRANSACTION_DATE].value, this.props.settings)}</td>
          <td className={'right_text'}>{`${rawNumberToFormattedString(convertedAmount, false, this.props.settings)} 
            ${this.props.wsCurrency}`}</td>
        </tr>
      </tbody>);
  }
}

export default FundingItem;
