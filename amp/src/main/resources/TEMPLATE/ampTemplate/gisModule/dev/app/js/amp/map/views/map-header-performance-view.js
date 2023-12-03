var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/../templates/map-header-performance-info-template.html', 'utf8');

module.exports = Backbone.View.extend({
	
  className: 'map-header navbar-header navbar-brand performance-bar',

  template: _.template(Template),
  
  events: {
	  'change .map-header-performance-button': 'onPerformanceToggleChange'
  },  

  initialize: function(options) {
    this.app = options.app;
    this.listenTo(this.app.data.title, 'update', this.render);
    this.listenTo(this.model, 'change:isPerformanceToggleAvailable', this.render);
    this.model.set('isPerformanceToggleSelected', null);  
  },
  render: function() {
	  var self = this;	  
	  this.$el.html(this.template({
		  isPerformanceToggleAvailable: self.model.get('isPerformanceToggleAvailable'),
		  isPerformanceToggleSelected: self.model.get('isPerformanceToggleSelected') 
	  }));
	  if (this.model.get('isPerformanceToggleAvailable') === true) {
		  $('.performance-bar').show();
	  } else {
		  $('.performance-bar').hide();
	  }	  
      app.translator.translateDOM(this.$el);
      this.$el.find('[data-toggle="tooltip"]').tooltip();
	  return this;
  },
  
   
  onPerformanceToggleChange: function(event) {
	  var self = this;
	  this.model.set('isPerformanceToggleSelected', this.$('.map-header-performance-button:checked').length > 0 ? true : null);	  
  }

});
