var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var $ = require('jquery');
var state = require('../../../services/state');

// var ProjectLayerCollection = require('../collections/project-layer-collection');

var BaseControlView = require('../../base-control/base-control-view');
var OptionView = require('./option-view');

var Template = fs.readFileSync(__dirname + '/../templates/layers-template.html', 'utf8');
var RadioOptionTemplate = fs.readFileSync(__dirname + '/../templates/radio-option-template.html', 'utf8');


module.exports = BaseControlView.extend({
  id: 'tool-layers',
  title: 'Project Data',
  iconClass: 'ampicon-projects',
  description: 'Select Points or Indicators to visualize data on the main map.',

  template: _.template(Template),
  radioOptionTemplate: _.template(RadioOptionTemplate),

  initialize: function() {
    var self = this;
    BaseControlView.prototype.initialize.apply(this, arguments);  // sets this.app

    this._initProjectLayerCollection();

    // register state:
    state.register(this, 'layers-view', {
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
          var selectedModel = self.projectLayerCollection.findWhere({'title': id}); //TODO: should be an id....
          self.projectLayerCollection.select(selectedModel);
        }
      },
      empty: null
    });
  },

  _initProjectLayerCollection: function() {
    // LEGACY
    this.projectLayerCollection = this.app.data.projectSites;
    // this.projectLayerCollection = new ProjectLayerCollection([
    //   {
    //     title: 'Projects by Region',
    //     value: 'adm-1'
    //   },
    //   {
    //     title: 'Projects by Zone',
    //     value: 'adm-2'
    //   },
    //   {
    //     title: 'Projects Sites',
    //     value: 'locations',
    //     helpText: 'See individual project sites.'
    //   }
    // ]);
  },

  render: function() {
    var self = this;
    BaseControlView.prototype.render.apply(this);

    // add content
    this.$('.content').html(this.template({title: this.title}));
    this.renderProjectList();

    return this;
  },

  renderProjectList: function() {
    var self = this;
    this.$('.layer-selector').html(this.projectLayerCollection.map(function(indicator) {
      var tmpView = new OptionView({ model: indicator });
      return tmpView.render().el;
    }));
  },

});
