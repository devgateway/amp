
import numeral from 'numeral';

const DEFAULT_GLOBAL_SETTINGS = {
    decimalSeparator: '.',
    groupSeparator: ',',
    format: '0,0',
    amountsInThousands: 0
  };

export default class NumberUtils {
  
  static rawNumberToFormattedString(number, forceUnits = false) {
    const formatted = numeral(forceUnits ? number : NumberUtils.calculateInThousands(number))
      .format(DEFAULT_GLOBAL_SETTINGS.format).replace(/,/g, ' ');
    return formatted;
  }

  static formattedStringToRawNumber(numberString) {
    return numeral(numberString).value();
  }

  static calculateInThousands(number) {
    switch (DEFAULT_GLOBAL_SETTINGS.amountsInThousands) {
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