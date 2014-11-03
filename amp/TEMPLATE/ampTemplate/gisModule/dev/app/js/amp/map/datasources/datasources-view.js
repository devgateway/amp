var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var DatasourcesItem = require('./datasources-item-view');
var Template = fs.readFileSync(__dirname + '/datasources-template.html', 'utf8');

module.exports = Backbone.View.extend({

  className: 'datasources',

  template: _.template(Template),

  events: {
    'click a[href="#toggle-datasources-collapse"]': 'toggleDatasources'
  },

  initialize: function(options) {
    this.app = options.app;
    this.listenTo(this.app.data, 'show hide', this.render);
  },

  render: function() {
    this.$el.html(this.template());

    var content = this.app.data.getAllVisibleLayers().map(function(layer) {
      return (new DatasourcesItem({ model: layer, app: this.app })).render().el;
    }).value();

    if (!_.isEmpty(content)) {
      this.$el.addClass('expanded');  // always expand when new layers are added
      this.$('.datasources-content').html(content);
    }

    return this;
  },

  toggleDatasources: function() {
    this.$el.toggleClass('expanded');
  }

});
