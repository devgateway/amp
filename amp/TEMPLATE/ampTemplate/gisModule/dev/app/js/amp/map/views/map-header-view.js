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
    this.listenTo(this.app.data.title, 'update', this.render);
    this.listenTo(this.app.data.filter, 'apply', this.render);
  },

  render: function() {
    var title = this.app.data.title.current;
    var selectedFilters = this.app.data.filter.serializeToModels();

    this.$el.html(this.template({title: title, filters: selectedFilters}));
    return this;
  }

});
