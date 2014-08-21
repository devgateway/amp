var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/legend-item-template.html', 'utf8');

// classes for instanceof checks
var IndicatorJoin = require('../../data/models/indicator-join-model');
var IndicatorWMS = require('../../data/models/indicator-wms-model');
var IndicatorArcGIS = require('../../data/models/indicator-arcgis-model');
var ProjectSites = require('../../data/models/project-sites-model');
// var ADMClusters = require('../../data/models/adm-cluster-model');


module.exports = Backbone.View.extend({

  template: _.template(Template),

  render: function() {
    this.$el.html(this.template(_.extend({
      status: 'loading'
    }, this.model.toJSON())));

    if (this.model instanceof IndicatorJoin) {
      this.renderAsGeoJSON();
    } else if (this.model instanceof IndicatorWMS) {
      this.renderAsWMS();
    } else if (this.model instanceof IndicatorArcGIS) {
      this.renderAsArcGIS();
    } else if (this.model instanceof ProjectSites) {
      this.renderAsProjectSites();
    // } else if (this.model instanceof ADMClusters) {
    //   this.renderAsClusters();
    } else {
      console.warn('legend for layer type not implemented: ', this.model);
      this.renderAsUnknown(this.model);
    }

    return this;
  },

  renderAsGeoJSON: function() {
    var self = this;
    self.model.load().then(function() {
      self.$el.html(self.template(_.extend({}, self.model.toJSON(), {
        status: 'loaded',
        legendType: 'colours',
        colourBuckets: self.model.palette.colours,
        unit: self.model.get('data').unit
      })));
    });
  },

  renderAsWMS: function() {
    var base = this.model.get('link');
    var qs = '?request=GetLegendGraphic&version=1.1.1&format=image/png%26layer=';
    var wmsLayer = this.model.get('layer');
    this.$el.html(this.template(_.extend({}, this.model.toJSON(), {
      status: 'loaded',
      legendType: 'img',
      legendSrc: base + qs + wmsLayer
    })));
  },

  renderAsArcGIS: function() {
    var drawLegend = _.bind(function() {
      this.$el.html(this.template(_.extend({}, this.model.toJSON(), {
        status: 'loaded',
        legendType: 'colours',
        colourBuckets: this.model.palette.colours,
        unit: ''
      })));
    }, this);

    drawLegend();
    this.listenTo(this.model, 'change:min change:max', drawLegend);
  },

  renderAsProjectSites: function() {
    var self = this;
    self.model.load().then(function() {
      self.$el.html(self.template(_.extend({}, self.model.toJSON(), {
        status: 'loaded',
        legendType: 'colours',
        blah: true,
        colourBuckets: self.model.palette.colours
      })));
    });
  },

  renderAsUnknown: function() {
    this.$('em').html('legend not yet configured for this layer type');
  }

});
