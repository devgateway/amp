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

  render: function () {
    this.$el.html(this.template());
    return this;
  }

});
