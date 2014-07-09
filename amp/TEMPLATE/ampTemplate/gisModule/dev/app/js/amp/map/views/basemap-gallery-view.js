var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/../templates/basemap-gallery-template.html', 'utf8');


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

    // TODO: create basemap from this: http://esri.github.io/esri-leaflet/examples/switching-basemaps.html
    //  if possible do it in re-usable modular way, as a mini lib that can be used as a standalone plugin...
    
    return this;
  }

});
