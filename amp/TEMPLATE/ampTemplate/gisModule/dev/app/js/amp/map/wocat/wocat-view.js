var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var WocatTable = require('./wocat-table-view');
var Template = fs.readFileSync(__dirname + '/wocat-template.html', 'utf8');

module.exports = Backbone.View.extend({

  className: 'wocat',

  template: _.template(Template),

  events: {
    'click a[href="#toggle-wocat-collapse"]': 'toggleWocat'
  },

  initialize: function(options) {
    this.app = options.app;
    _.bindAll(this, 'render');
  },

  render: function() {
    var self = this;

    self.$el.html(self.template());
    var content = new WocatTable({
        app: this.app
      }).render().el;

    self.$('.wocat-content', self.$el).html(content);
    /* TODO Reintroduce the "Loading" image in the table template */
    return this;
  },

  toggleWocat: function() {
    this.$el.toggleClass('expanded');
    return false; // stops it updating the url.
  }


});
