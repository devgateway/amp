var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/../templates/map-header-info-template.html', 'utf8');


/**
 * Pertinent details contextualizing the data on the map.
 */
module.exports = Backbone.View.extend({

  className: 'map-header navbar-header navbar-brand',

  template: _.template(Template),

  initialize: function(options) {
    this.app = options.app;
    this.listenTo(this.app.display.title, 'update', this.render);
  },

  render: function(newTitle) {
    var title = newTitle || this.app.display.title.current;
    this.$el.html(this.template({title: title}));
    return this;
  }

});
