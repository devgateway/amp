import * as AC from '../../utils/ActivityConstants';

/**
 *    
 */
const dateFormat = 'YYYY-MM-DD';
const currencyHour = '00:00';

export default class ActivityFundingTotals {

  static getTotals(activity, adjType, trnType) {
    return this.buildStandardMeasureTotal(activity, adjType, trnType);
  }

  static buildStandardMeasureTotal(activity, adjType, trnType) {
    const fundingDetails = [];
    if (activity && activity.fundings && activity.fundings.value) {
      activity.fundings.value.forEach(funding => {
        if (funding.funding_details && funding.funding_details.value) {
          funding.funding_details.value.forEach(fd => {
            if (fd.adjustment_type.value === adjType && fd.transaction_type.value === trnType) {
              fundingDetails.push(fd);
            }
          });
        }
      });
    }
    let total = 0;
    if (fundingDetails.length > 0) {
      total = this.convertFundingDetailsToCurrency(fundingDetails, AC.DEFAULT_CURRENCY);
    }

    return total;
  }

  static convertFundingDetailsToCurrency(fundingDetails, currencyTo) {
    let total = 0;
    fundingDetails.forEach(fd => {
      total += this.convertTransactionAmountToCurrency(fd, currencyTo);
    });

    return total;
  }

  static convertTransactionAmountToCurrency(fundingDetail, currencyTo) {
    const fixedExchangeRate = fundingDetail[AC.FIXED_EXCHANGE_RATE].value;
    const currencyFrom = fundingDetail[AC.CURRENCY].value;
    const transactionDate = this._formatDate(fundingDetail[AC.TRANSACTION_DATE].value);
    const transactionAmount = fundingDetail[AC.TRANSACTION_AMOUNT].value;
    const currencyRate = this._convertCurrency(currencyFrom, currencyTo, transactionDate, fixedExchangeRate);
    return transactionAmount * currencyRate;
  }

  static _formatDate(date) {
    if (date) {
      return new Date(date.slice(0, 10));
    }
  }

  static _convertCurrency(currencyFrom, currencyTo, dateToFind, fixedExchangeRate) {
    return 1;
  }

}
