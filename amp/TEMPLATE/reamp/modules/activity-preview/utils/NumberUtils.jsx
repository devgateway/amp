
import numeral from 'numeral';

const DEFAULT_GLOBAL_SETTINGS = {
    decimalSeparator: '.',
    groupSeparator: ',',
    format: '0,0',
    amountsInThousands: 0
  };

export default class NumberUtils {
  
  static rawNumberToFormattedString(number, forceUnits = false, settings) {
    let format = settings && settings['number-format'] ? 
          settings['number-format'].replace('#,##', '0,').replace(/#/g, '0') : 
          DEFAULT_GLOBAL_SETTINGS.format;

    let divider = settings && settings['number-divider'] ? 
          settings['number-divider'] : 
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