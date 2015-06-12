var _ = require('underscore');
var Deferred = require('jquery').Deferred;
var BackboneDash = require('../backbone-dash');
var SavedChart = require('./saved-dash');


module.exports = BackboneDash.Collection.extend({

  url: '/rest/dashboard/saved-charts',

  model: SavedChart,
  timer: undefined,
  initializedOnce: false,

  initialize: function(models, options) {
    this.app = options.app;
  },

  load: function(stateId) {
	var self = this;
	if (self.initializedOnce !== false) { return; }
	self.initializedOnce = true;
	 
    var deferred = new Deferred();
    var loaded = this.get(stateId);
    if (loaded) {
      deferred.resolve(loaded);
    } else {
      var model = this.model.fromId(stateId, { app: this.app });
      this.add(model);  // sets up collection so the model can find a URL
      model.fetch().done(function() {
          deferred.resolve(model);
          
          // AMP-19803 and AMP-20206: Here we wait until the filter widget has been loaded to trigger the 'apply' event and force each chart to redraw with the saved filters.
	      // Tried to do something similar before we reach this stage (ie: in app-class.js, chart-view-base.js, charts.js, etc) but without luck because the render is triggered automatically.
	      // TODO: We need more time to evaluate a solution using this.app.filter.loaded promise but that didnt work consistently on IE.          
          if (self.timer === undefined) {
        	  self.timer = setInterval(function() {
		    	  if (this.app !== undefined && this.app.filter !== undefined && this.app.filter.finishedFirstLoad === true) {
		    		  clearInterval(self.timer);
		    		  this.app.filter.trigger('apply');			    		  
		    		  // Only make 1 render call to the main app view, this will prevent other bugs (ie: the double and triple chart rendering).
		    		  if (this.app.rendered === false) {
		    			  this.app.rendered = true;
		    			  app.render();
		    		  }
		    	  }
		      }, 100);
          }
        }).fail(_(function() {
          this.app.report('Failed to load saved dashboard', ['Could not retrieve the saved state.']);
          deferred.reject();
        }).bind(this));
    }

    return deferred.promise();
   }
});