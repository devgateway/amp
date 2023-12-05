var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/filters.html', 'UTF-8'));
var summaryTemplate = _.template(fs.readFileSync(
  __dirname + '/../templates/filter-summary.html', 'UTF-8'));
var detailsTemplate = _.template(fs.readFileSync(
  __dirname + '/../templates/filter-details.html', 'UTF-8'));
var filtersViewLog = require('../../../../../../../reamp/tools/log')('amp:dashboards:filters:view');


module.exports = BackboneDash.View.extend({

  events: {
    'click .show-filters': 'showFilter',
    'click .show-filter-details': 'showFilterDetails',
    'click .hide-filter-details': 'hideFilterDetails'
  },

  initialize: function(options) {
	var self = this;
    this.finishedFirstLoad = false;
    this.app = options.app;
    this.listenTo(this.app.filter, 'cancel', this.hideFilter);
    this.listenTo(this.app.filter, 'apply', this.applyFilter);
    this.app.generalSettings.load().done(_(function() {     
      this.app.filter.loaded.done(_(function() {        
        this.app.state.register(this, 'filters', {
            get: _(function() {
             return this.app.filter.serialize();            
          }).bind(this),
          set: _(function(state) {        	
            if (_.isEmpty(state)){            
            	self.app.filter.extractDates(self.app.generalSettings, state, 'dashboard-default-min-date', 'dashboard-default-max-date');
            }
            this.app.filter.deserialize(state);
            this.app.filter.finishedFirstLoad = true;
          }).bind(this),
          empty: {}
        });
      }).bind(this));
    }).bind(this));
  },
  render: function() {
    this.$el.html(template());
    this.app.filter.setElement(this.el.querySelector('#filter-popup'));
    this.hideFilter();
    this.app.filter.loaded
      .done(_(this.renderApplied).bind(this))
      .fail(_(function() {
        this.$('.applied-filters').html('<strong class="text-danger filters-err">' +
          'Failed to load filters</strong> <a href="" class="btn btn-warning btn-sm">' +
          '<span class="glyphicon glyphicon-refresh"></span> Refresh page</a>');
        this.$('button').addClass('disabled');
      }).bind(this));
    return this;
  },

  renderApplied: function() {	  
    var filterObject = this.app.filter.serializeToModels();
    var countApplied = _(filterObject.filters).keys().length;
    this.$('.applied-filters').html(summaryTemplate({ countApplied: countApplied }));
    this.app.translator.translateDOM(this.el);
  },

  showFilter: function() {
    this.app.filter.showFilters();
    this.$('#filter-popup').show();
  },

  hideFilter: function() {
    this.$('#filter-popup').hide();
  },

  applyFilter: function() {
    // todo: actually do an effect for changed filters...
    this.hideFilter();
    this.renderApplied();
  },

  showFilterDetails: function() {
	var self = this;
    var filterObject = this.app.filter.serializeToModels(); 
    var applied = _(filterObject.filters).map(function(filter, key) {
      var filterField = filterObject.filters[key];
      if(filterField.modelType === 'YEAR-SINGLE-VALUE' || filterField.modelType === 'DATE-RANGE-VALUES'){
    	  return self.getAppliedDateObject(filterObject,key);
      } else {
    	  var name = filter.filterName || key;   	   
    	  return {
    	        name: self.translate(name),
    	        id: key.replace(/[^\w]/g, ''), // remove anything non-alphanum
    	        detail: _(filter).map(function(value) {
    	          if (value.attributes !== undefined) {
    	            return value.get('name');
    	          } else {
    	            // To fix problem with dates.
    	            if (value !== key && value !== filter.filterName) {
    	              return value;
    	            }
    	          }
    	        })
    	      };  
      }
      
    });
    
    this.$('.applied-filters').html(detailsTemplate({ applied: applied }));
    this.app.translator.translateDOM(this.el);
  },
  getAppliedDateObject: function(filterObject, filterKey){
	  var filterField = filterObject.filters[filterKey];
	  var dateRangeText = '';
	  var filterName = filterField.filterName ? filterField.filterName : filterKey;
	  if(filterKey === 'date') {
		  dateRangeText = this.translate("Dates");
	  } else if(filterKey === 'computed-year') {
		  dateRangeText = this.translate("Computed Year");
	  } else {		  
		  dateRangeText = this.translate(filterName);			  		
	  }
	  var detail = filterField.modelType === 'YEAR-SINGLE-VALUE'? filterField.year: this.app.filter.formatDate(filterField.start) + '&mdash;' + this.app.filter.formatDate(filterField.end)
		
	  return {
		  id: filterKey.replace(/[^\w]/g, '-'),
		  name: dateRangeText,
		  detail: [detail]
		};	  
  },
  translate: function(name) {
	 return this.app.translator.translateSync('amp.gis:pane-subfilters-' + name.replace(/ /g,'')  , name.replace(/-/g, " "));	 
  },
  hideFilterDetails: function() {
    this.renderApplied();
  }
});
