var fs = require('fs');
var _ = require('underscore');

var RadioListCollection = require('../collections/radio-list-collection');

var BaseControlView = require('../../base-control/base-control-view');
var OptionView = require('./option-view');

var Template = fs.readFileSync(__dirname + '/../templates/layers-template.html', 'utf8');
var RadioOptionTemplate = fs.readFileSync(__dirname + '/../templates/radio-option-template.html', 'utf8');


module.exports = BaseControlView.extend({
  id: 'tool-layers',
  title: 'Project Data',
  iconClass: 'ampicon-projects',
  description: 'View where projects are being implemented throughout the country.',

  template: _.template(Template),
  radioOptionTemplate: _.template(RadioOptionTemplate),

  initialize: function() {
    var self = this;

    BaseControlView.prototype.initialize.apply(this, arguments);  // sets this.app
    this._loaded = this.app.data.admClusters.load().then(function() {
      self.projectLayerCollection = new RadioListCollection(_.union(
        self.app.data.admClusters.models,
        [self.app.data.structuresMenu]
      ));


      // register state:
      self.app.state.register(self, 'layers-view', {
        get: function() {
          var tmp = self.projectLayerCollection.getSelected().first().value();
          if (tmp) {
            return tmp.toJSON().title; //TODO: should be an id....
          } else {
            return null;
          }
        },
        set: function(id) {
          if (id) {
            var selectedModel = self.projectLayerCollection.findWhere({title: id});
            self.projectLayerCollection.select(selectedModel);
          }
        },
        empty: 'Administrative Level 0'
      });
    });
  },

  render: function() {
	  var self = this;
	  BaseControlView.prototype.render.apply(this);
	  // add content	  
	  $.when(self.app.data.generalSettings.loaded, this._loaded).then(function() {
		//check if we need to show Project Sites
		  var foundPS = self.app.data.generalSettings.get('project-sites'); 		  
		  if (foundPS !== true) {			  
			  //need to remove project-sites
			  //find the index of project-sites in projectLayerCollection
			  var index = undefined;
			  for (i = 0; i < self.projectLayerCollection.models.length; i++ ) {
				  if (self.projectLayerCollection.models[i].attributes.title === 'Project Sites') {
					  index = i;
				  }
			  }
			  self.projectLayerCollection.models.splice(index, 1);  
		  } 
		  self.$('.content', self.el).html(self.template({title: self.title}));
		  self.$('.layer-selector', self.el).html(self.projectLayerCollection.map(function(cluster) {
			  return (new OptionView({
				  model: cluster,
				  app: self.app
			  })).render().el;
		  }));
	  });
	  
	  return this;
  }

});
