define([ 'underscore', 'backbone' ], function(_, Backbone) {

	var Settings = Backbone.Model.extend({
		defaults : {
			formName : 'settingsForm',
			action : '/aim/reportsFilterPicker.do',
			decimalSeparators : [ {
				id : ',',
				value : ','
			}, {
				id : '.',
				value : '.'
			}, {
				id : 'CUSTOM',
				value : 'Custom'
			} ],
			selectedDecimalSeparator : ',',
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
			selectedDecimalPlaces : '2'
		}
	});

	return Settings;
});