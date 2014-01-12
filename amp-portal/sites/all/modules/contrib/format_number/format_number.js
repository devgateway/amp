(function ($) {

/**
 * Create our own namespace in the global Drupal object.
 */
Drupal.numericElement = Drupal.numericElement || {};

/**
 * Attach Drupal behavior to numeric input elements.
 */
Drupal.behaviors.numericElement = {
  attach: function(context, settings) {
    Drupal.numericElement.attach(context, settings);
  }
};

/**
 * Format a number with (site default or user defined) thousands separator
 * and decimal point.
 *
 * Formatting options are expected to be at Drupal.settings.format_number.
 *
 * @param float number
 *   The number being formatted.
 * @param int decimals
 *   Number of decimal digits. Use -1 for any number of decimals.
 * @param boolean truncate
 *   TRUE to trucate the decimal part (default). FALSE to round the result.
 * @return string
 *   The formatted number.
 */
Drupal.formatNumber = function(number, decimals, truncate) {
  if (typeof(number) != 'number') {
    number = 0;
  }
  if (typeof(decimals) != 'number') {
    decimals = 0;
  }
  if (typeof(truncate) == 'undefined') {
    truncate = true;
  }

  // Round the number to the specified number of decimals if requested to.
  // Otherwise, the decimal part will be trucated.
  if (decimals > 0 && !truncate) {
    number = Math.round(number * Math.pow(10, decimals)) / Math.pow(10, decimals);
  }

  // Obtain the sign and separate integer/decimal parts.
  var minus_sign = (number < 0 ? '-' : '');
  var number_parts = (Math.abs(number) + '').split('.');

  // Get the integer part of the number.
  var integer_part = (number_parts[0].length > 0 ? number_parts[0] : '0');

  // Insert thousands separator when necessary.
  if (Drupal.settings.format_number.thousands_sep.length > 0) {
    // Reverse the interger part into an array.
    var digits = integer_part.split('').reverse();
    integer_part = '';
    // Add thousands separator every 3 digits.
    for (var i = 0; i < digits.length; i++) {
      integer_part += ((i % 3) == 0 && i > 0 ? Drupal.settings.format_number.thousands_sep : '') + digits[i];
    }
    // Reverse back the integer part.
    integer_part = integer_part.split('').reverse().join('');
  }
  number = minus_sign + integer_part;

  // Build the decimal part of the number.
  if (decimals > 0) {
    var decimal_part = (number_parts.length <= 1 ? '0' : number_parts[1]);
    if (decimal_part.length > decimals) {
      decimal_part = decimal_part.substr(0, decimals);
    }
    else if (decimal_part.length < decimals) {
      while (decimal_part.length < decimals) {
        decimal_part += '0';
      }
    }
    number += Drupal.settings.format_number.decimal_point + decimal_part;
  }

  // When no decimals have been specified, we allow any. This is used for
  // min/max fields in CCK field settings.
  else if (decimals < 0 && number_parts.length > 1) {
    number += Drupal.settings.format_number.decimal_point + number_parts[1];
  }

  return number;
};

/**
 * Parse a number with (site default or user defined) thousands separator
 * and decimal point.
 *
 * Formatting options are expected to be at Drupal.settings.format_number.
 *
 * @param string number
 *   A number formatted with localized thousands separator and decimal point.
 * @param boolean required
 *   FALSE to return '' when input is empty string. Otherwise, result is always
 *   returned as a valid number. Default is TRUE.
 * @return number
 *   A valid number.
 */
Drupal.parseNumber = function(number, required) {
  if (typeof(number) != 'string') {
    return (typeof(number) == 'number' ? number : 0);
  }
  if (required == undefined) {
    required = true;
  }

  // Get rid of leading/trailing whitespaces.
  if ((number = number.replace(/^\s+|\s+$/g, '')) == '') {
    return (required ? 0 : '');
  }

  // Extract sign and temporarily remove it from input.
  var is_negative = (number[0] == '-');
  if (is_negative) {
    number = number.substr(1);
  }

  // Remove thousands separators, if any.
  if (Drupal.settings.format_number.thousands_sep.length > 0) {
    var thsep = Drupal.settings.format_number.thousands_sep;
    if (thsep == '\u00A0') {
      thsep += ' ';
    }
    number = number.replace(new RegExp('[' + thsep + ']', 'g'), '');
  }

  // Translate decimal point, if necessary.
  if (Drupal.settings.format_number.decimal_point != '.') {
    number = number.replace(new RegExp('[' + Drupal.settings.format_number.decimal_point + ']', 'g'), '.');
  }

  // Truncate from first non-numeric character (at this point only 0-9 and
  // just one dot are allowed).
  // This should also restore back the sign (if necessary) and convert the
  // string into a pure javascript number (integer or float).
  return number.replace(/^([0-9]*\.?[0-9]+)?.*$/, '$1') * (is_negative ? -1 : 1);
};

/**
 * Attach Drupal behavior to numeric input elements.
 *
 * Numeric elements are processed during the following events:
 * - on page load     : All numeric elements are formatted with site/user defined options.
 * - on element focus : Thousands separator are removed, if any.
 * - on element blur  : Numeric elements are formatted again with site/user defined options.
 * - on form submit   : Thousands separator are removed to prevent from triggering
 *                      maxlength error during Forms API validation.
 */
Drupal.numericElement.attach = function(context, settings) {
  $('input.form-numeric:not(.form-numeric-processed)', context).addClass('form-numeric-processed').each(function() {
    var $element = $(this);

    // Number of decimal places for this element.
    var decimals = $element.attr('decimals');
    decimals = (decimals == undefined ? -1 : Math.max(-1, parseInt(decimals)));

    // The element is properly formatted on page load.
    Drupal.numericElement.formatElement($element, decimals);

    // Bind element events.
    $element.bind('focus', function() {
      Drupal.numericElement.clearThousandsSep($element);
    }).bind('blur', function() {
      Drupal.numericElement.formatElement($element, decimals);
    });

    // Bind submit event callback to the form.
    $element.parents('form:not(.form-numeric-processed)').addClass('form-numeric-processed').each(function() {
      // Clear thousands separators before submitting. This is not strictly
      // necessary because input is always validated on the server, but it
      // prevents from getting "field cannot be longer than max" errors issued
      // by Forms API.
      $(this).bind('submit', function() {
        $('input.form-numeric', this).each(function() {
          Drupal.numericElement.clearThousandsSep($(this));
        });
      });
    });
  });
};

/**
 * Clear thousands separators from the given input element.
 *
 * @param $element
 *   The input element.
 */
Drupal.numericElement.clearThousandsSep = function($element) {
  var number = $element.val();
  if (number.length > 0 && Drupal.settings.format_number.thousands_sep.length > 0) {
    var thsep = Drupal.settings.format_number.thousands_sep;
    if (thsep == '\u00A0') {
      thsep += ' ';
    }
    number = number.replace(new RegExp('['+ thsep +']', 'g'), '');
    $element.val(number);
  }
};

/**
 * Format the number in the given element with site/user defined options.
 *
 * @param $element
 *   The input element.
 * @param decimals
 *   Number of decimal digits.
 */
Drupal.numericElement.formatElement = function($element, decimals) {
  var number = $element.val();
  if (number.length > 0) {
    number = Drupal.parseNumber(number, false);
    if (typeof(number) == 'number') {
      number = Drupal.formatNumber(number, decimals);
    }
    $element.val(number);
  }
};

})(jQuery);
