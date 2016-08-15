var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/../templates/map-header-gap-analysis-info-template.html', 'utf8');

module.exports = Backbone.View.extend({
	
  className: 'map-header navbar-header navbar-brand gap-analysis-bar',

  template: _.template(Template),
  
  events: {
	  'change .map-header-gap-analysis-button': 'changeGapAnalysisSelector'
  },  

  initialize: function(options) {
    this.app = options.app;
    this.listenTo(this.app.data.title, 'update', this.render);
    
    this.model.set('isGapAnalysisAvailable', false);
	this.model.set('isGapAnalysisSelected', false);
  },

  render: function() {
	  var self = this;	  
	  this.$el.html(this.template({
		  isGapAnalysisAvailable: self.model.get('isGapAnalysisAvailable'),
		  isGapAnalysisSelected: self.model.get('isGapAnalysisSelected') 
	  }));
	  if (this.model.get('isGapAnalysisAvailable') === true) {
		  $('.gap-analysis-bar').show();
	  } else {
		  $('.gap-analysis-bar').hide();
	  }
	  app.translator.translateDOM(this.$el);
	  return this;
  },
  
  refresh: function(model_) {
	  if (model_.get('type') === "Indicator Layers" || model_.get('type') === "joinBoundaries") {
		  this.model.set('isGapAnalysisAvailable', model_.get('canDoGapAnalysis') && model_.get('selected'));
		  this.render();
	  }	  
  },
  
  changeGapAnalysisSelector: function(event) {
	  var self = this;
	  this.model.set('isGapAnalysisSelected', this.$('.map-header-gap-analysis-button:checked').length > 0);
	  
	  var indicator = _.find(self.app.data.indicators.models, function(item) {
		  return item.get('selected') === true;
	  });
	  indicator.set('selectedGapAnalysis', this.model.get('isGapAnalysisSelected'));
  }

});
