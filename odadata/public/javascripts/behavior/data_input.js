$('div.cofunding input[type=radio].donor').livequery('change', function() {
	if ($(this).checked) {
		$(this).nextAll('select.agencies').hide();
		$(this).nextAll('select.donors').show();
	} else {
		$(this).nextAll('select.donors').hide();
		$(this).nextAll('select.agencies').show();
	}
});

$('div.cofunding input[type=radio].agency').livequery('change', function() {
	if ($(this).checked) {
		$(this).nextAll('select.donors').hide();
		$(this).nextAll('select.agencies').show();
	} else {
		$(this).nextAll('select.agencies').hide();
		$(this).nextAll('select.donors').show();
	}
});

var cloneFinancesRow = function() {
  var blueprint = $(this).parent('div').parent('div').find('fieldset:last');
  var next = blueprint.clone();
  var time = new Date().getTime();
  
  // Get the prefix
  var prefix = next.find(':text').attr('id').match(/^(.*)\_\d+\_.*$/)[1] + '_';
  
  // Replace the id and name attributes.
  next.find(':text, :checkbox, input[type=hidden], select').each(function() {
    $(this).attr('id', $(this).attr('id').replace(/\_\d+\_/, '_' + time + '_'));
    $(this).attr('name', $(this).attr('name').replace(/\]\[\d+\]\[/, '][' + time + ']['));
  });
  
  // Replate the for attributes.
  next.find('label').each(function() {
    $(this).attr('for', $(this).attr('for').replace(/\_\d+\_/, '_' + time + '_'));
  });
  
  // Remove the record id
  next.find('#' + prefix + time + '_id').remove();
  
  // Save year and reset fields 
  // (not select fields because for on/off budget it makes more sense to assume the previous year's value)
  var year_field = '#' + prefix + time + '_year';
  var year = +next.find(year_field).val();
  next.find(':text, :checkbox').val(null);
  next.find(year_field).val(year + 1);
  next.find('legend').text(next.find('legend').text().replace(year, year + 1));
  
  blueprint.after(next);
};

var currencySelector = function(expr, confirmation) {
  var input = $(expr);
  
  var storeOldValue = function() {
    input.data('oldvalue', input.val());
  }
  
  var restoreOldValue = function() {
    input.val(input.data('oldvalue'));
  }
  
  var adjustCurrencyLabels = function() {
	  if (confirm(confirmation)) {
	    $('span.currency').text($(this).val());
	    $('input.monetary').val(null);
	    storeOldValue();
	  } else {
	    restoreOldValue();
	  }
	};
	
	storeOldValue();
	input.change(adjustCurrencyLabels);
}