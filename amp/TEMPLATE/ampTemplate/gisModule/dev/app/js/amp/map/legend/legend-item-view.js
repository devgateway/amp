var _ = require('underscore');
var Backbone = require('backbone');

// classes for instanceof checks
var IndicatorJoin = require('../../data/models/indicator-join-model');
var HilightFunding = require('../../data/models/hilight-funding-model');
var IndicatorWMS = require('../../data/models/indicator-wms-model');
var IndicatorArcGISDynamic = require('../../data/models/indicator-arcgis-dynamicLayer-model');
var IndicatorArcGISFeature = require('../../data/models/indicator-arcgis-featureLayer-model');
var structuresMenu = require('../../data/models/structures-menu-model');
var ADMClusters = require('../../data/models/adm-cluster-model');

// model-specific item views
var IndicatorJoinView = require('./legend-item-indicator-join');
var IndicatorArcGISDynamicView = require('./legend-item-indicator-arcgis-dynamic');
var IndicatorArcGISFeatureView = require('./legend-item-indicator-arcgis-feature');
var IndicatorWMSView = require('./legend-item-indicator-wms');
var ProjectSitesView = require('./legend-item-structures');
var ADMClustersView = require('./legend-item-adm-clusters');


var Unknown = Backbone.View.extend({

  template: _.template('<h3><%= title %></h3>' +
                       '<em>legend not yet configured for this layer type</em>'),

  render: function() {
    this.$el.html(this.template(this.model.toJSON()));
    return this;
  }

});


module.exports = function(options) {
  if (options.model instanceof ADMClusters) {
    return new ADMClustersView(options);
  } else if (options.model instanceof IndicatorArcGISFeature) {
    return new IndicatorArcGISFeatureView(options);
  } else if (options.model instanceof IndicatorArcGISDynamic) {
    return new IndicatorArcGISDynamicView(options);
  } else if (options.model instanceof IndicatorWMS) {
    return new IndicatorWMSView(options);
  } else if (options.model instanceof structuresMenu) {
    return new ProjectSitesView(options);
  } else if (options.model instanceof IndicatorJoin || options.model instanceof HilightFunding ||
      options.model.attributes.type === 'FeatureCollection') {
    // drs: checking for 'FeatureCollection' because check for instanceof hilightfunding failed
    // very dirty, try to fix.
    return new IndicatorJoinView(options);
  } else {
    console.error('legend for layer type not implemented: ', options.model);
    return new Unknown(options);
  }
};
