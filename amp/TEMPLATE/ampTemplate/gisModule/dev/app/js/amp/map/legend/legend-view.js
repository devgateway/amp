var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var LegendItem = require('./legend-item-view');
var Template = fs.readFileSync(__dirname + '/legend-template.html', 'utf8');
require('jquery-ui/resizable');

module.exports = Backbone.View.extend({

  className: 'legend',

  template: _.template(Template),


  events: {
    'click a[href="#toggle-legend-collapse"]': 'toggleLegend'
  },
 
  initialize: function(options) {
    var self = this;
    this.app = options.app;
    
    //attach legend listeners after filter and app state loaded.
    $.when(this.app.data.filter.loaded, this.app.data._stateWait).then(function() {
      self.listenTo(self.app.data, 'show hide refresh sync valuesChanged', self.render);
    });
  },

  render: function() {	
    var self = this;
    // Chained Translate: returns el
    this.app.translator.translateDOM(this.template())
      .then(function(newEl) {
        self.$el.html(newEl);
      });

    var content = this.app.data.getAllVisibleLayers().map(function(layer) {
      return (new LegendItem({ model: layer, app: this.app })).render().el;
    }).value();


    if (!_.isEmpty(content)) {
      this.$el.addClass('expanded');  // always expand when new layers are added
      this.$('.legend-content').html(content);
    }    
        
    this.makeLegendDraggable();
    this.makeLegendResizable();
    
    return this;
  },  
  makeLegendDraggable: function() {
	  $('.legend').draggable({
    	  containment : "parent",
    	  stop: function( event, ui ) {
    		  if (event.originalEvent.target.id === 'legend-title'){
    			this.$el.toggleClass('expanded'); //hack to prevent legend from collapsing when dragging stops.  
    		  }    		 
    	  }.bind(this)
     });
  },
  makeLegendResizable: function() {
	  this.$('.legend-content').resizable({
	    	alsoResize: ".legend",
	        handles: 's, e, se'       
	    });      
  },
  toggleLegend: function() {
    this.$el.toggleClass('expanded');
    
    if (this.$el.hasClass('expanded')) {    	
    	this.$el.removeClass('legend-collapsed');
    } else {
    	this.$el.addClass('legend-collapsed')
    }
    return false; // stops it updating the url.
  }

});
