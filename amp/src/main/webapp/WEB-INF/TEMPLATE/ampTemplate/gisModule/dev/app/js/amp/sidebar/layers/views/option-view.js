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
  createTooltip: function(obj){
	  var tooltip  = '';
	  if(obj.description){
	     tooltip += obj.description + '. ' ;
	  }	     
	  if(obj.createdOn){
	     tooltip += (obj.description ? "</br>" : '') + this.app.translator.translateSync('amp.gis-layers-manager:tooltip-created-on','Created on') + ' ' + obj.createdOn;
	  }
	  if(obj.createdBy){
		 tooltip += ' ' + this.app.translator.translateSync('amp.gis-layers-manager:tooltip-created-by','by') + ' ' + obj.createdBy;
	  }
	  return tooltip;
  },
  render: function() {
    this.$el.html(this.template({obj: this.model.attributes}));
    this.app.translator.translateDOM(this.el); /* After to catch disabled */
    this.$el.find('[data-toggle="tooltip"]').tooltip({
      content: this.createTooltip(this.model.attributes)
    });
    return this;
  },
  toggleSelect: function() {
    this.model.trigger('toggleSelect', this.model);
    if (this.model.get('type') === "Indicator Layers"
        || this.model.get('type') === "joinBoundaries"
        || this.model.get('type') === "wms"
    ) {
        debugger;
    	this.app.mapView.headerGapAnalysisView.refresh(this.model);
    }    
    this.app.translator.translateDOM(this.el);
  }

});
