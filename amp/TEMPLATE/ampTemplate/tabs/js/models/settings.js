define([ 'underscore', 'backbone' ], function(_, Backbone) {

	var Settings = Backbone.Model.extend({
		defaults : {
			formName : 'settingsForm',
			action : '/aim/reportsFilterPicker.do',
			decimalSeparators : [ ',', '.', 'Custom' ],
			decimalPlaces : [ {
				id : '-1',
				value : 'No Limit'
			}, {
				id : '0',
				value : '0'
			}, {
				id : '1',
				value : '1'
			}, {
				id : '2',
				value : '2'
			}, {
				id : '3',
				value : '3'
			}, {
				id : '4',
				value : '4'
			}, {
				id : '5',
				value : '5'
			}, {
				id : '-2',
				value : 'Custom'
			} ],
			groupSeparators : [ ',', '.', 'Custom' ],
			amountUnits : [ {
				id : 0,
				value : 'Amounts in Units'
			}, {
				id : 1,
				value : 'Amounts in Thousands (000)'
			}, {
				id : 2,
				value : 'Amounts in Millions (000 000)'
			} ],
			currencies : [ {
				id : 102,
				value : 'Australlian Dollar'
			}, {
				id : 103,
				value : 'Canadian Dollar'
			}, {
				id : 95,
				value : 'Euro'
			} ],
			calendar : [ {
				id : 4,
				value : 'Gregorian Calendar'
			} ],
			selectedDecimalSeparator : null,
			selectedDecimalPlaces : null,
			selectedUseGroupingSeparator : null,
			selectedGroupSeparator : null,
			selectedAmountUnit : null,
			selectedCurrency : null,
			selectedCalendar : null
		}
	});

	return Settings;
});