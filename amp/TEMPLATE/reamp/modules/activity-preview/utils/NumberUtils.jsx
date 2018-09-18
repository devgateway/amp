
import numeral from 'numeral';
import * as AC from './ActivityConstants';

const DEFAULT_GLOBAL_SETTINGS = {
    decimalSeparator: '.',
    groupSeparator: ',',
    format: '0,0',
    amountsInThousands: 0
  };

export default class NumberUtils {
  
  static rawNumberToFormattedString(number, forceUnits = false, settings) {
    let format = settings && settings[AC.NUMBER_FORMAT] ? 
          settings[AC.NUMBER_FORMAT].replace('#,##', '0,').replace(/#/g, '0') : 
          DEFAULT_GLOBAL_SETTINGS.format;

    let divider = settings && settings[AC.NUMBER_DIVIDER] ? 
          settings[AC.NUMBER_DIVIDER] : 
          DEFAULT_GLOBAL_SETTINGS.amountsInThousands;

    const formatted = numeral(forceUnits ? number : NumberUtils.calculateInDivider(number, divider)).format(format);
    return formatted;
  }

  static formattedStringToRawNumber(numberString) {
    return numeral(numberString).value();
  }

  static calculateInDivider(number, divider) {
    switch (divider) {
      case 0:
        return number;
      case 1:
        return number / 1000;
      case 2:
        return number / 1000 / 1000;
      case 3:
        return number / 1000 / 1000 / 1000;
      default:
        return number;
    }
  }
}