var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');
var BaseControlView = require('../../base-control/base-control-view');
var Template = fs.readFileSync(__dirname + '/../templates/settings-template.html', 'utf8');
var OptionTemplate = fs.readFileSync(__dirname + '/../templates/settings-option-template.html', 'utf8');



module.exports = BaseControlView.extend({
  id: 'tool-settings',
  title:  'Settings',
  iconClass:  'ampicon-settings',
  description:  '',
  selectedCurrency: null,
  template:  _.template(Template),
  templateOption: _.template(OptionTemplate),
  events:{
    'change select': 'optionChanged',
    'click .apply-settings': 'applySettings'
  },

  initialize: function(options) {
    var self = this;
    BaseControlView.prototype.initialize.apply(this, arguments);
    this.app = options.app;
    this.mutableFields = ['0', '1', '2']; // the ids of settings the user can change

    this.app.data.settings.load().then(function() {
      // register for state serialization
      self.app.state.register(self, 'settings', {
        get: function() { return self.app.data.settings.serialize();},
        set: function(state) { return self.app.data.settings.deserialize(state);},
        empty: null //todo use defaultId
      });

    });
  },

  render: function() {
    var self = this;
    BaseControlView.prototype.render.apply(this);

    self.$('.content').html(this.template({title: this.title}));

    // add content
    this.app.data.settings.load().then(function() {
      self.$('.settings').html('');
      self.app.data.settings.each(function(setting) {
        /* Show only fields that are changable by the user */
        if (_.contains(self.mutableFields, setting.id)) {
          self.$('.settings').append(self.templateOption(setting.toJSON()));
        }
      });
      self.calendarCurrencies = app.data.settings.get('calendarCurrencies').get('options');
      self.availableCurrenciesForCalendar = _.uniq(_.findWhere(self.calendarCurrencies, {id: $('#2').val()}).value.split(","));
    });

    return this;
  },

  // update the settings collection with the latest selected one.
  optionChanged: function(evt) {
	var silent = true;
	var self = this;
	// Update the list of currencies after changing the calendar.
	if ($(evt.currentTarget).attr('id') === '2') {
		// Update list of currencies and select the first one.
		var currencyModel = this.app.data.settings.get('1');
		var allCurrencies = currencyModel.get('copyOfAllCurrencies');
				
		//TODO: Find a way to refresh the select automatically through backbone.
		currencyModel.set('options', []);
		var currencySelect = $('#1');
		$(currencySelect).empty();			
		$.each(self.availableCurrenciesForCalendar, function(index, object) {   
        	$(currencySelect)
                 .append($("<option></option>")
                 .attr("value", object)
                 .text(_.find(allCurrencies, function(item){return item.id === object}).name));
        	app.data.settings.get('1').get('options').push(_.find(allCurrencies, function(item){return item.id === object}));
        });
		
		// when calendar changes reset currency to first currency on available currencies
		this.selectedCurrency = this.availableCurrenciesForCalendar[0];
		//app.data.settings.get('1').set('selected', this.availableCurrenciesForCalendar[0]);
		currencySelect.val(this.availableCurrenciesForCalendar[0]);
	}	  
	
	if ($(evt.currentTarget).attr('id') === '1') {
		this.selectedCurrency = $(evt.currentTarget).val();
	}else{
		 var settingID = $(evt.currentTarget).attr('id');
		 var selectedID = $(evt.currentTarget).val();
		 this.updateSelected(settingID, selectedID, silent);
	}
   
  },

  updateSelected: function(settingID, selectedID, silent) {
    var setting = this.app.data.settings.get(settingID);
    if (setting) {
      setting.set('selected', selectedID, {silent: silent});
    } else {
      console.error('setting not found...');
    }
  },
  
  applySettings: function() {
	  // We set the selected currency here (not in optionChanged') to avoid showing something in the infowindows before hitting Apply button.
	  if(this.selectedCurrency){
		  app.data.settings.get('1').set('selected', this.selectedCurrency); 
	  }     

	  // Trigger update in settings-collection.js
	  $('#map-loading').show();
	  this.app.data.settings.trigger('applySettings');
  }
});

