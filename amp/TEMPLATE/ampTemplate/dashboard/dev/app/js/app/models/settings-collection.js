var Deferred = require('jquery').Deferred;
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var Setting = require('./setting');


function isIntStr(n) {
  // test whether a string starts with is a base-10 int
  return !isNaN(parseInt(n, 10))
}


function tagIf(test, tag) {
  return function(setting) {
    if (test(setting)) {
      setting[tag] = true;
    }
    return setting;
  }
}


module.exports = BackboneDash.Collection.extend({

  url: '/rest/amp/settings',

  model: Setting,

  comparator: 'id',
  
  firstTime: true,

  initialize: function(models, options) {
    this.app = options.app;
    this._loaded = new Deferred();
    _.bindAll(this, 'toAPI', 'fromState');
    if(!this.app.hasIssue('localStorage')){
      //this should be "once", but this collection gets synced 2 times for some reason
      //TODO: find the reason for above and replace this with "once"
      this.on("sync", _(function(){
        try{
          var settings = JSON.parse(localStorage.settings);
          if("object" == typeof settings && null !== settings){
            if(settings[1]){
              this.findWhere({id: "1"}).select(settings[1], false);              
            }
            if(settings[2]){
              this.findWhere({id: "2"}).select(settings[2], false);
            }
          }
        }
        //we're catching because it will throw if localStorage.settings is empty or corrupted
        catch(e){}
      }).bind(this));
    }
  },

  parse: function(settings) {
	this.extractNumberFormatSettings(settings);
    return _(settings).chain()
      // mark weird options with non-int keys hidden
      .map(tagIf(function(setting) { return !isIntStr(setting.id); }, 'ignore'))
      // mark funding type setting hidden, since it's set per-chart...
      .map(tagIf(function(setting) { return setting.id === '0'; }, 'ignore'))
      // ...but also flag it so the charts can see the the funding type options
      .map(tagIf(function(setting) { return setting.id === '0'; }, 'ftype'))
      // mark all options as selected per the defaults provided
      .map(function(setting) {
        return _(setting).extend({
          options: _(setting.options).map(tagIf(function(option) {
            return option.id === setting.defaultId;
          }, 'selected'))
        })
      })
      .value();
  },

  load: function() {
	if(this.firstTime) {
		this.firstTime = false;
		
		if (this._loaded.state() !== 'pending') { return this._loaded.promise(); }

	    this.fetch({app: this.app })
	      .then(_(function() {
	        this._loaded.resolve();
	      }).bind(this))
	      .fail(_(function() {
	        this.app.report('Failed to load dashboard settings',
	          ['Could not connect to the server.']);
	        this._loaded.reject();
	      }).bind(this));
	}    
    return this._loaded.promise();
  },

  toAPI: function(overrides) {
    // format selected filter options the way the api wants
    // ignored filters are skipped (overrides are never removed though)
    // overrides should be in the {settingId: settingOptionId} format the api expects
    if (this.length === 0) { return {}; }  // cop out early if we don't have settings yet
    return this.chain()
      .map(function(model) { return model.toJSON(); })
      .filter(function(setting) { return !setting.ignore; })
      .reduce(function(apiFormatted, setting) {
        apiFormatted[setting.id] = _(setting.options)
          .findWhere({selected: true}).id;
        return apiFormatted;
      }, {})
      .extend(overrides || {})
      .value();
  },

  fromState: function(state) {
    // select options from an array with the same format we send to the api
    _(state).each(function(optId, settingId) {
      this.get(settingId).select(optId, true);
    }, this);
  },

  getVisible: function() {
    return this.filter(function(setting) { return !setting.get('ignore'); });
  },
  
  extractNumberFormatSettings: function(settings) {
	  var numberFormat = {}; 
	  var foundNF =_.find(settings, function(item) {return item.id === 'number-format'});
	  numberFormat.numberFormat = _.find(foundNF.options, function(item) { return item.id === foundNF.defaultId}).name || '#,#.#';

	  // If the format pattern doesnt have thousands grouping then ignore 'number-group-separator' param or it will 
	  // be used by JS to group by thousands (ie: in the 'Others' columns).
	  if(numberFormat.numberFormat.indexOf(',') !== -1) {
		  var foundNGS =_.find(settings, function(item) {return item.id === 'number-group-separator'});
		  numberFormat.groupSeparator = _.find(foundNGS.options, function(item) { return item.id === foundNGS.defaultId}).name || ',';
	  } else {
		  numberFormat.groupSeparator = '';
	  }
	  var foundDS =_.find(settings, function(item) {return item.id === 'number-decimal-separator'});
	  numberFormat.decimalSeparator = _.find(foundDS.options, function(item) { return item.id === foundDS.defaultId}).name || '.';
	  this.app.settings.numberFormatSettings = numberFormat;
	  
	  var foundNM =_.find(settings, function(item) {return item.id === 'number-multiplier'});
	  this.app.settings.numberMultiplier = _.find(foundNM.options, function(item) { return item.id === foundNM.defaultId});
	  if (this.app.settings.numberMultiplier.name === '1.0') {
		  this.app.settings.numberMultiplierDescription = 'amp.dashboard:chart-tops-inunits';
	  } else if(this.app.settings.numberMultiplier.name === '0.001') {
		  this.app.settings.numberMultiplierDescription = 'amp.dashboard:chart-tops-inthousands';
	  } else {
		  this.app.settings.numberMultiplierDescription = 'amp.dashboard:chart-tops-inmillions';
	  }
  }

});