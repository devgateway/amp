var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/legend-item-indicator-wms.html', 'utf8');


module.exports = Backbone.View.extend({

  template: _.template(Template),
  className: 'indicatorwms-legend', //TODO: rename to legend-indicator-wms consistant with others,
  initialize: function(options) {
    this.app = options.app;
  },

  render: function() {
    var base = this.model.get('link');
    var qs = '?request=GetLegendGraphic&version=1.1.1&format=image/png&layer=';
    var wmsLayer = this.model.get('layer');
    var self = this;


    self.app.translator.translateDOM(this.template(_.extend({}, this.model.toJSON(), {
      status: 'loaded',
      legendSrc: base + qs + wmsLayer
    }))).then(function(legend) {
      self.$el.html(legend);
    });

    return this;
  }

});
