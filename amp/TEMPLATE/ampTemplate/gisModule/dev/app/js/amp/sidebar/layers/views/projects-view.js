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
    this.projectLayerCollection = new RadioListCollection(_.union(
      this.app.data.admClusters.models,
      [this.app.data.projectSites]
    ));

    // register state:
    this.app.state.register(this, 'layers-view', {
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
          var selectedModel = self.projectLayerCollection.findWhere({title: id}); //TODO: should be an id....
          self.projectLayerCollection.select(selectedModel);
        }
      },
      empty: null
    });
  },

  render: function() {
    BaseControlView.prototype.render.apply(this);

    // add content
    this.$('.content').html(this.template({title: this.title}));
    this.$('.layer-selector').html(this.projectLayerCollection.map(function(cluster) {
      return (new OptionView({ model: cluster })).render().el;
    }));

    return this;
  }

});
