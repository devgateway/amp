var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/../templates/radio-option-template.html', 'utf8');


module.exports = Backbone.View.extend({

  app: null,
  template: _.template(Template),

  events: {
    'change input': 'toggleSelect'
  },

  initialize: function(options) {
    this.app = options.app;
    this.listenTo(this.model, 'change:selected', this.render);
    this.listenTo(this.model, 'change:selectedGapAnalysis', this.render);
  },

  render: function() {
    this.$el.html(this.template(this.model.attributes));
    this.app.translator.translateDOM(this.el); /* After to catch disabled */
    this.$el.find('[data-toggle="tooltip"]').tooltip();
    return this;
  },

  toggleSelect: function() {
    this.model.trigger('toggleSelect', this.model);
    if (this.model.get('type') === "Indicator Layers" || this.model.get('type') === "joinBoundaries") {
    	this.app.mapView.headerGapAnalysisView.refresh(this.model);
    }    
    this.app.translator.translateDOM(this.el);
  }

});