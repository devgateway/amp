var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(path.join(__dirname, '../templates/basemap-gallery-template.html'));


module.exports = Backbone.View.extend({

  template: _.template(Template),

  tagName: 'ul',
  id: 'basemap-gallery',
  className: 'nav navbar-nav navbar-right dropdown',

  initialize: function(options) {
    _.extend(this, options);  // stores a reference to the arcgis map
  },

  render: function() {
    this.$el.html(this.template());

    return this;
  }

});
