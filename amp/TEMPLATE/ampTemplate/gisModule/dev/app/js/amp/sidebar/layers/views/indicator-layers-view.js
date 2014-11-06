var fs = require('fs');
var _ = require('underscore');
var RadioListCollection = require('../collections/radio-list-collection');
var Backbone = require('backbone');
var OptionView = require('./option-view');
var Template = fs.readFileSync(__dirname + '/../templates/layers-template.html', 'utf8');

module.exports = Backbone.View.extend({
  id: 'tool-layers-sd-indicator',
  title: 'Country indicators',
  iconClass: 'ampicon-layers',
  description: 'View indicators on sub-national needs.',

  template: _.template(Template),

  initialize: function(options) {
    var self = this;
    this.app = options.app;

    /* For mutual exclusion, we use this reference to the parent */
    this.parentMultisectionControl = options.parent;


    this.collection = new RadioListCollection(this.app.data.indicators.models, {
      siblingGroupList: this.parentMultisectionControl.radioButtonGroup
    });

    this.app.data.indicators.load().then(function() {
      self._registerSerializer();
    });

    this.listenTo(this.app.data.indicators, 'add', this.render);
  },

  render: function() {
    // TODO: find a better way to keep our proxy collection up to date
    // Thad do you know a good pattern for this?
    this.collection.reset(this.app.data.indicators.models);

    this.$el.html(this.template({title: this.title}));
    this.$('.layer-selector', this).html(this.collection.map(function(indicator) {
      return (new OptionView({ model: indicator })).render().el;
    }));

    return this;
  },

  _registerSerializer: function() {
    var self = this;

    // register state:
    self.app.state.register(self, self.id, {
      get: function() {
        var tmp = self.collection.getSelected().first().value();
        if (tmp) {
          return tmp.toJSON().id; //TODO: should be an id....
        } else {
          return null;
        }
      },
      set: function(id) {
        if (id) {
          console.log('self.collection', self.collection.length, id);
          var selectedModel = self.collection.findWhere({id: id});
          if (selectedModel) {
            self.collection.select(selectedModel);
          }
        }
      },
      empty: null
    });
  }

});
