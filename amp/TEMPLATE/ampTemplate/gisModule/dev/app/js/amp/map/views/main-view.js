var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');

var MapHeaderView = require('../views/map-header-view');
var BasemapGalleryView = require('../views/basemap-gallery-view');
var LegendView = require('../views/legend-view');
var admLayerView = require('../views/adm-layer-view');
var Template = fs.readFileSync(path.join(__dirname, '../templates/map-container-template.html'));


module.exports = Backbone.View.extend({

  template: _.template(Template),

  render: function () {

    this.$el.html(this.template({}));

    var headerContainer = this.$('#map-header > div');

    // Render map header
    var headerView = new MapHeaderView();
    headerContainer.append(headerView.render().el);

    // Render BasemapGallery
    var basemapView = new BasemapGalleryView();
    headerContainer.append(basemapView.el);
    basemapView.render();  // the Basemap widget is special, it needs an
                           // on-page DOM node to work.

    // Render Legend
    var legendView = new LegendView();
    this.$el.append(legendView.render().el);

    return this;
  }
});
