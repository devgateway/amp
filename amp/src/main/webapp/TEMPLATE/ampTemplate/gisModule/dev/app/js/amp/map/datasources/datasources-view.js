var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var DatasourcesTable = require('./datasources-table-view');
var Template = fs.readFileSync(__dirname + '/datasources-template.html', 'utf8');

module.exports = Backbone.View.extend({

  className: 'datasources',

  template: _.template(Template),

  events: {
    'click a[href="#toggle-datasources-collapse"]': 'toggleDatasources'
  },

  initialize: function(options) {
    this.app = options.app;
    _.bindAll(this, 'render');
  },

  render: function() {
    var self = this;

    self.$el.html(self.template());
    var content = new DatasourcesTable({
        app: this.app
      }).render().el;

    self.$('.datasources-content', self.$el).html(content);
    /* TODO Reintroduce the "Loading" image in the table template */
    return this;
  },

  toggleDatasources: function() {
    this.$el.toggleClass('expanded');
    return false; // stops it updating the url.
  }


});
