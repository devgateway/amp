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

  initialize: function() {
    this.layerNames = null;
    this.listenTo(Backbone, 'MAP_LOAD_INDICATOR', this.updatedIndicator);
  },

  render: function () {
    this.$el.html(this.template({layerNames: this.layerNames}));
    return this;
  },

  updatedIndicator: function(indicator) {
    if (indicator === null) {
      this.layerNames = null;
    } else {
      this.layerNames = indicator.get('title');
    }
    this.render();
  }

});
