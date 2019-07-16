import * as AC from './ActivityConstants';
var moment = require('moment');

const API_LONG_DATE_FORMAT = 'YYYY-MM-DDTHH:mm:ss.SSSZZ';
const API_SHORT_DATE_FORMAT = 'YYYY-MM-DD';
export default class DateUtils {
  /**
   * Configures the global locale to be used by the Moment library, e.g. in the Date Picker
   * @param lang
   */
  static setCurrentLang(lang) {
    moment.locale(lang);
  }

  static formatDateForCurrencyRates(date) {
    return DateUtils.formatDate(date, API_SHORT_DATE_FORMAT);
  }

  static isValidDateFormat(date, format) {
    const dateAsMoment = moment(date, format);
    return momentDate.isValid();
  }

  static formatDate(date, format) {
    if (date) {
      const dateAsMoment = moment(date);
      if (dateAsMoment.isValid()) {
        return dateAsMoment.utc().format(format);
      }
    }
    return '';
  }

  static createFormattedDate(date, settings) {
    let format = settings && settings[AC.DATE_FORMAT] ? settings[AC.DATE_FORMAT].toUpperCase() : API_SHORT_DATE_FORMAT;
    return DateUtils.formatDate(date, format);
  }

  static createFormattedDateTime(date) {
    let format = settings && settings[AC.DATE_FORMAT] ? settings[AC.DATE_FORMAT] : API_LONG_DATE_FORMAT;
    return DateUtils.formatDate(date, format);
  }

  /**
   * Gets a date from future or past relative to the current moment
   * @param durationStr the duration to add/substract from the current moment
   * @param isAdd if true, then adds the duration (default to false)
   * @return {moment.Moment}
   */
  static getDateFromNow(durationStr, isAdd = false) {
    const duration = moment.duration(durationStr);
    if (moment.isDuration(duration)) {
      if (isAdd) {
        return moment().add(duration);
      }
      return moment().subtract(duration);
    }
  }

  static duration(from, to) {
    // not using 'fromNow' since it doesn't provide exact difference
    let seconds = moment(to).diff(from, 'seconds');
    const minutes = Math.floor(seconds / 60);
    seconds %= 60;
    return `${minutes} min ${seconds} sec`;
  }

  static durationImproved(from, to, settings, meassure = 'months') {
    return moment(to, settings[AC.DATE_FORMAT].toUpperCase())
            .diff(moment(from, settings[AC.DATE_FORMAT].toUpperCase()), meassure);
  }

  /**
   * Formats the date according to AMP API format
   * @param date (optional, defaults to current moment)
   * @returns {string} date formatted according to API format
   */
  static getISODateForAPI(date = new Date()) {
    return DateUtils.formatDate(date, API_LONG_DATE_FORMAT);
  }
}
