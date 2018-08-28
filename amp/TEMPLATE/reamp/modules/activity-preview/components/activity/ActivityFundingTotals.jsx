import * as AC from '../../utils/ActivityConstants';

/**
 * @author Daniel Oliva
 */
const currency = 'XOF';
const dateFormat = 'YYYY-MM-DD';
const currencyHour = '00:00';

export default class ActivityFundingTotals {

  static getTotals(activity, adjType, trnType) {
    return this._buildStandardMeasureTotal(activity, adjType, trnType);
  }

  static _buildStandardMeasureTotal(activity, adjType, trnType) {
    const fundingDetails = [];
    if (activity.fundings && activity.fundings.value) {
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
      total = this._convertFundingDetailsToCurrency(fundingDetails, currency);
    }

    return total;
  }

  static _convertFundingDetailsToCurrency(fundingDetails, currencyTo) {
    let total = 0;
    fundingDetails.forEach(fd => {
      total += this._convertTransactionAmountToCurrency(fd, currencyTo);
    });

    return total;
  }

  static _convertTransactionAmountToCurrency(fundingDetail, currencyTo) {
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
