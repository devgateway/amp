var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/filters.html', 'UTF-8'));
var summaryTemplate = _.template(fs.readFileSync(
  __dirname + '/../templates/filter-summary.html', 'UTF-8'));
var detailsTemplate = _.template(fs.readFileSync(
  __dirname + '/../templates/filter-details.html', 'UTF-8'));


module.exports = BackboneDash.View.extend({

  events: {
    'click .show-filters': 'showFilter',
    'click .show-filter-details': 'showFilterDetails',
    'click .hide-filter-details': 'hideFilterDetails'
  },
  
  initialize: function(options) {
    this.finishedFirstLoad = false;
	this.app = options.app;
    this.listenTo(this.app.filter, 'cancel', this.hideFilter);
    this.listenTo(this.app.filter, 'apply', this.applyFilter);

    this.app.settings.load().done(_(function() {
    	// Extract default dates from Global Settings.
    	var blob = {};
    	
    	// AMP-19254, AMP-20537: override the "date" range with the Dashboards-specific one from the settings blob (a hack...) 
    	this.app.filter.extractDates(this.app.settings.models, blob, 'dashboard-default-min-date', 'dashboard-default-max-date');
    	
	    this.app.filter.loaded.done(_(function() {
	    	console.error('filters loaded');
	      this.app.state.register(this, 'filters', {
	        // namespace serialized filters so we can hook in extra state to store
	        // later if desired (anything dashboards-ui related, for example)
	        get: _(function() { 
	        	return {filter: this.app.filter.serialize() }; 
	        }).bind(this),
	        set: _(function(state) {
	        	if (state.filter.otherFilters !== undefined && state.filter.otherFilters.date !== undefined) {
	        		console.log('Using saved filters.');	        		
	        	} else {
	        		console.log('Using default filter dates.');
	        		state.filter = blob;
	        	}
	        	this.app.filter.deserialize(state.filter);
	        	this.app.filter.finishedFirstLoad = true;
	        }).bind(this),
	        empty: {filter: {}}
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
    var filters = this.app.filter.serializeToModels();
    var countApplied = _(filters.columnFilters).keys().length;
    countApplied += !!filters.otherFilters;
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
    var filters = this.app.filter.serializeToModels();
    var applied = _(filters.columnFilters).map(function(filter, key) {
      return {
        name: filter.filterName || key,
        id: key.replace(/[^\w]/g, ''),  // remove anything non-alphanum
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
    });
    if (filters.otherFilters) {
      // Currently assumes that any otherFilters just implies Date Range
      // ... there is no obvious way to get nice strings out.
      var dateRange = filters.otherFilters.date;
      var dateRangeText = app.translator.translateSync("amp.dashboard:date-range","Date Range");
	  applied.push({
        name: dateRangeText,
        detail: [dateRange.start + '&mdash;' + dateRange.end]
      });
    }
    this.$('.applied-filters').html(detailsTemplate({ applied: applied }));
    this.app.translator.translateDOM(this.el);
  },

  hideFilterDetails: function() {
    this.renderApplied();
  }

});
