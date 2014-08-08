var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/legend-item-template.html', 'utf8');


module.exports = Backbone.View.extend({

  template: _.template(Template),

  render: function() {
    this.$el.html(this.template(_.extend({
      status: 'loading'
    }, this.model.toJSON())));

    if (_.has(this.model, 'indicator')) {
      var layerType = this.model.indicator.get('type');
      if (layerType === 'joinBoundaries') { // geoJSON
        this.renderGeoJSON();
      } else if (layerType === 'wms') {
        this.renderWMS();
      } else {
        console.warn('legend for indicator type not implemented: ', layerType);
      }
    }

    return this;
  },

  renderGeoJSON: function() {
    var self = this;

    this.model.indicator.load().then(function() {
      self.$el.html(self.template(_.extend({}, self.model.toJSON(), {
        status: 'loaded',
        legendType: 'colours',
        colourBuckets: self.model.palette.colours,
        unit: self.model.indicator.get('data').unit
      })));
    });
  },

  renderWMS: function() {
    var base = this.model.indicator.get('link');
    var qs = '?request=GetLegendGraphic&version=1.1.1&format=image/png%26layer=';
    var wmsLayer = this.model.indicator.get('layer');
    this.$el.html(this.template(_.extend({}, this.model.toJSON(), {
      status: 'loaded',
      legendType: 'img',
      legendSrc: base + qs + wmsLayer
    })));
  }

});
