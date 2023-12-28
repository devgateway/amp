var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var topojsonLibrary = require('../../../libs/local/topojson.js');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var MapHeaderView = require('../views/map-header-view');
var MapHeaderGapAnalysisView = require('../views/map-header-gap-analysis-view');
var MapHeaderPerformanceToggle = require('../views/map-header-performance-view');
var BasemapGalleryView = require('../views/basemap-gallery-view');
var LegendView = require('../legend/legend-view');
var DataSourcesView = require('../datasources/datasources-view');

var ProjectSitesLayerView = require('../views/structures-view');
var ADMClustersLayersView = require('../views/adm-clusters-view');
var IndicatorLayersView = require('../views/indicator-layers-view');
var MessageView = require('../views/message-view');

var Basemaps = require('../collections/basemap-collection');
var Template = fs.readFileSync(__dirname + '/../templates/map-container-template.html', 'utf8');


module.exports = Backbone.View.extend({

  template: _.template(Template),

  initialize: function(options) {
    this.app = options.app;
    this.mapEl = $('<div id="map-canvas">');
    this.map = L.map(this.mapEl[0], {
      maxZoom: 19 /* TODO: greater than 14 mostly only useful for imagery, consider limiting by basemap */
    });
    this.map.attributionControl.setPosition('bottomleft');
    this.map.zoomControl.setPosition('topright');
    this.map.panBy([-10,0]);

    this.app.state.register(this, 'map', {
      get: this._getMapView,
      set: this._setMapView,
      empty: { center: [0, 40], zoom: 1 }
    });

    this.basemaps = new Basemaps(null, { app: this.app });  // pre-loaded with hard-coded basemaps

    // init layers
    this.structuresLayersView = new ProjectSitesLayerView({map: this.map, app: this.app});
    this.admClustersLayersView = new ADMClustersLayersView({map: this.map, app: this.app});
    this.indicatorLayersView = new IndicatorLayersView({map: this.map,
      app: this.app,
      admClustersLayersView: this.admClustersLayersView
    });

    this.headerView = new MapHeaderView({app: this.app});
    this.GapViewModel = Backbone.Model.extend({defaults: {isGapAnalysisAvailable: false, isGapAnalysisSelected: false}});
    
    this.headerGapAnalysisView = new MapHeaderGapAnalysisView({app: this.app, model: new this.GapViewModel()});
    this.headerPerformanceToggle = new MapHeaderPerformanceToggle({app: this.app, model: this.app.data.performanceToggleModel});
    this.legendView = new LegendView({app: this.app});
    this.datasourcesView = new DataSourcesView({app: this.app});
    this.basemapView = new BasemapGalleryView({
      map: this.map,
      collection: this.basemaps
    });
    this.messageView = MessageView.getInstance();

    this.listenTo(this.indicatorLayersView, 'addedToMap', this._indicatorsShown);
  },

  render: function() {
    this.$el.html(this.template({map: this.mapEl}));
    this.$el.append(this.mapEl);
    this.map.invalidateSize();

    var headerContainer = this.$('#map-header > div');
    headerContainer.append(this.headerView.render().el);
    headerContainer.append(this.headerGapAnalysisView.render().el);
    headerContainer.append(this.headerPerformanceToggle.render().el);
    headerContainer.append(this.basemapView.render().el);

    this.$el.append(this.legendView.render().el);
    this.$el.append(this.datasourcesView.render().el);
    this.$el.append(this.messageView.render().el);

    this.$el.append('<div id="map-loading" style="position: absolute;left: 50%;top: 50%;">' +
      '<img src="img/loading-icon.gif"></div>');

     this._renderCountryBoundary();
     this.map.invalidateSize();
     return this;
  },

  _renderCountryBoundary: function() {
    var self = this;
    this.app.data.boundaries.load().then(function() {
      var adm0Boundaries = self.app.data.boundaries.where({admLevel: 'adm-0'});

      var promises = adm0Boundaries.map(function (b) {return b.load();});
      $.when.apply($, promises).then(function() {
        var outerBounds = null;

        var updateOuterBounds = function(feature) {
          var bounds = L.GeoJSON.geometryToLayer(feature.geometry).getBounds();
          if (!outerBounds) {
            outerBounds = bounds;
          } else {
            outerBounds = outerBounds.extend(bounds);
          }
        };

        for (var i = 0; i < arguments.length; i++) {
          var topoboundaries = arguments[i].toJSON();

          //retrieve the TopoJSON index key
          var topoJsonObjectsIndex = _.keys(topoboundaries.objects)[0];

          var boundary = topojsonLibrary.feature(topoboundaries, topoboundaries.objects[topoJsonObjectsIndex]);

          self.countryBoundary = L.geoJson(boundary, {
            onEachFeature: updateOuterBounds,
            style:  {color: '#29343F', fillColor:'none', weight: 1.4, dashArray: '1'}
          }).addTo(self.map);
        }

        if (outerBounds) {
          //For the AMP GIS app, use the sidebar width as padding */
          var sidebarExpansionWidth = $('#sidebar').width();

          var paddingToLeft = {
            paddingTopLeft: new L.Point(sidebarExpansionWidth, 0)
          };

          /*
           * If current viewport is already in
           * the AMP country, then preserve the state rather that resetting.
           *
           * for the case where a state is saved which is not quite exactly inside
           * the AMP national boundarybox, we add 30% padding to all directions
           *
           **/
          if (!outerBounds.pad(0.3).contains(self.map.getBounds())) {
            self.map.fitBounds(outerBounds, paddingToLeft);
          }
        }
      });
    });
  },

  _getMapView: function() {
    var center = this.map.getCenter();
    return {
      center: [
        center.lat,
        center.lng
      ],
      zoom: this.map.getZoom()
    };
  },
  _setMapView: function(stateBlob) {
    this.map.setView(stateBlob.center, stateBlob.zoom);
  },

  // when indicator is shown, bring project sites to the front...
  // should be able to do better once panes and leaflet v.08 are in
  _indicatorsShown: function() {
    this.structuresLayersView.bringToFront();
  }
});
