var fs = require('fs');
var _ = require('underscore');
var RadioListCollection = require('../collections/radio-list-collection');
var Backbone = require('backbone');
var OptionView = require('./option-view');
var Template = fs.readFileSync(__dirname + '/../templates/layers-template.html', 'utf8');

module.exports = Backbone.View.extend({
  
  template: _.template(Template),

  initialize: function(options) {
    var self = this;
    this.id = options.config.id;
    this.title = options.config.title;
    this.iconClass = options.config.iconClass;
    this.description = options.config.description;
    this.filterLayers = options.config.filterLayers;

    this.app = options.app;

    /* For mutual exclusion, we use this reference to the parent */
    this.parentMultisectionControl = options.parent;

    
    this.collection = new RadioListCollection(this.filterLayers(this.app.data.indicators), {
      siblingGroupList: this.parentMultisectionControl.radioButtonGroup
    });
   
    this.listenTo(this.app.data.indicators, 'add', this.render);
    this.listenTo(this.app.data.indicators, 'remove', this.render);
    this.listenTo(this.app.data.indicators, 'reset', this.render);
    this._registerSerializer();
  },   
  render: function() {	 
    // TODO: find a better way to keep our proxy collection up to date
    // Thad do you know a good pattern for this?
    this.collection.reset(this.filterLayers(this.app.data.indicators));
    this.$el.empty();
    if(this.collection.length > 0){
    	this.$el.html(this.template({title: this.title}));
        this.app.translator.translateDOM(this.el); /* After to catch disabled */
        this.$('.layer-selector', this).html(this.collection.map(function(indicator) {
          return (new OptionView({
            model: indicator,
            app: this.app
          })).render().el;
        }));
    }
    return this;
  },

  _registerSerializer: function() {
    var self = this;

    // register state:
    self.app.state.register(self, self.id, {
      get: function() {
        var tmp = self.collection.getSelected().first().value();
        if (tmp) {
          return {id: tmp.toJSON().id, selectedGapAnalysis: tmp.get('selectedGapAnalysis')}; //TODO: should be an id....
        } else {
          return null;
        }
      },
      set: function(id) {
    	var auxId = id;
    	// We need to check if id is a number or an object for old saved data (before we added Gap Analysis, "id" was a number).
    	if (id instanceof Object && id.id !== undefined) {
    		auxId = id.id;
    	}
    	if (auxId) {
            var selectedModel = self.collection.findWhere({id: auxId});
        	if (selectedModel) {				
				// Refresh the Gap Analysis area first because we need the parameter for the indicator EP.
				if (selectedModel.get('canDoGapAnalysis') === true) {
					self.app.mapView.headerGapAnalysisView.model.set('isGapAnalysisAvailable', true);				
					if (id.selectedGapAnalysis === true) {
						self.app.mapView.headerGapAnalysisView.model.set('isGapAnalysisSelected', true);
					}
				}
				self.collection.select(selectedModel);
          	}          
        }
      },
      empty: null
    });
  }

});
