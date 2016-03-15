var _ = require('underscore');
var BackboneDash = require('../backbone-dash');


module.exports = BackboneDash.Model.extend({

  initialize: function(attrs, options) {
    this.app = options.app;
    this.url = options.url;    
    var _self = this;
    // In "currencies" collection we add another field with the complete copy for future use. 
    if (this.originalAllCurrencies === undefined && attrs.id === '1') {
    	this.set('originalAllCurrencies', []);
    	_.each(attrs.options, function(item) {
    		// Need to copy one by one or this list will lose elements when changing calendars.
    		_self.get('originalAllCurrencies').push(item);
    	});
    }
  },

  select: function(optionId, triggerChange) {
	var self = this;
    // unselect old
    var old = _(this.attributes.options).findWhere({selected: true});
	if(old !== null && old !== undefined) {
		delete old.selected;
	}    
    // select new
    var newOpt = _(this.attributes.options).findWhere({id: optionId});
    if(newOpt !== undefined) {
    	newOpt.selected = true;
    } else {
    	// This can happen if we are loading a saved dashboard and the currency is loaded before the calendar.
    	if (this.attributes.id === "1") {
	    	newOpt = _(this.attributes.originalAllCurrencies).findWhere({id: optionId});
	    	if (newOpt !== undefined) {
	    		newOpt.selected = true;
	    	} else {
	    		console.info('Saved currency was deleted.');
	    		// Select default currency.
	    		newOpt = _(this.attributes.originalAllCurrencies).findWhere({id: _.find(app.settings.models, function(item) {return item.id === "1";}).get('defaultId')});
	    		newOpt.selected = true;
	    	}
    	}
    }

    // If we are changing the calendar --> Update list of currencies.
    if (this.attributes.id === "2") {
    	var currenciesForThisCalendar = _.find(_.find(app.settings.models, function(item) {return item.id === "calendarCurrencies";}).get('options'), function(item2) {return item2.id === optionId}).value.split(',');
    	var allCurrencies = _.find(app.settings.models, function(item) {return item.id === '1'});
    	// 'options' is linked to the calendar select, now we clean it one by one (assigning to [] will break the view).
    	for (var i = allCurrencies.get('options').length - 1; i >= 0; i--) {
    		allCurrencies.get('options').splice(i, 1);
    	}
    	// Match the currencies for this calendar with the list of all currencies.
    	_.each(currenciesForThisCalendar, function(item) {
    		var auxCurrency = _.find(allCurrencies.get('originalAllCurrencies'), function(item2) {
    			return item2.id === item;
    		});
    		if(auxCurrency !== undefined) {
    			allCurrencies.get('options').push(auxCurrency);
    		}
    	});
    	// Check if currently selected currency is still valid for the current calendar and make sure we always have a selected currency.
    	var selectedCurrency = _.find(allCurrencies.get('options'), function(item) {return item.selected === true});
    	if (selectedCurrency !== undefined) {
    		if (_.find(allCurrencies.get('originalAllCurrencies'), function(item) {return item.id === selectedCurrency.id}) === undefined) {
    			selectedCurrency.selected = false;
    			allCurrencies.get('options')[0].selected = true;
    		}
    	} else {
    		allCurrencies.get('options')[0].selected = true;
    	}
    }
    
    // Before linking calendar with currencies triggering 'change' was the default behavior.
    if (triggerChange === true) {
    	this.trigger('change');  // sort of a hack to do this manually...
    }
  },
  
  apply: function() {
	  this.trigger('change');
  }

});
