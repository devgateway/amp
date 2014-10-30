var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var topojsonLibrary = require('../../../libs/local/topojson.js');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var MapHeaderView = require('../views/map-header-view');
var BasemapGalleryView = require('../views/basemap-gallery-view');
var LegendView = require('../legend/legend-view');

var ProjectSitesLayerView = require('../views/project-sites-view');
var ADMClustersLayersView = require('../views/adm-clusters-view');
var IndicatorLayersView = require('../views/indicator-layers-view');

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

    this.app.state.register(this, 'map', {
      get: this._getMapView,
      set: this._setMapView,
      empty: { center: [0, 40], zoom: 2 }//{ center: [-3, 22], zoom: 6 }
    });

    this.basemaps = new Basemaps(null, { app: this.app });  // pre-loaded with hard-coded basemaps

    // init layers
    this.projectSitesLayersView = new ProjectSitesLayerView({map: this.map, app: this.app});
    this.admClustersLayersView = new ADMClustersLayersView({map: this.map, app: this.app});
    this.indicatorLayersView = new IndicatorLayersView({map: this.map, app: this.app});

    this.headerView = new MapHeaderView({app: this.app});
    this.legendView = new LegendView({app: this.app});
    this.basemapView = new BasemapGalleryView({
      map: this.map,
      collection: this.basemaps
    });

    this.listenTo(this.indicatorLayersView, 'addedToMap', this._indicatorsShown);
  },

  render: function() {
    this.$el.html(this.template({map: this.mapEl}));
    this.$el.append(this.mapEl);
    this.map.invalidateSize();

    var headerContainer = this.$('#map-header > div');
    headerContainer.append(this.headerView.render().el);
    headerContainer.append(this.basemapView.render().el);

    this.$el.append(this.legendView.render().el);

    this._renderCountryBoundary();
    this.map.invalidateSize();

    return this;
  },


        /*jshint camelcase: false */
// jscs:disable requireCamelCaseOrUpperCaseIdentifiers

  _renderCountryBoundary: function() {
    var self = this;
    this.app.data.boundaries.load().then(function() {
      var boundary0 = self.app.data.boundaries.findWhere({id:'adm-0'});

      boundary0.fetch().then(function(topoJSON) {
        var topoboundaries = topoJSON;

        //retrieve the TopoJSON index key
        var topoJsonObjectsIndex = _.keys(topoboundaries.objects)[0];

        var boundary = topojsonLibrary.feature(topoboundaries, topoboundaries.objects[topoJsonObjectsIndex]);

        self.countryBoundary = L.geoJson(boundary, {
          onEachFeature: function(feature) {

            //For the AMP GIS app, use the sidebar width as padding */
            var sidebarExpansionWidth = $('#sidebar').width();
            /*TODO(thadk): consider using mapHeader height as well, shrinks DRC for me: */
            /*var mapHeaderHeight = $('#map-header').height();*/

            var natlBounds = L.GeoJSON.geometryToLayer(feature.geometry).getBounds();

            /*
             * If current viewport is already in
             * the AMP country, then preserve the state rather that resetting.
             *
             * for the case where a state is saved which is not quite exactly inside
             * the AMP national boundary, we add 30% padding to all directions
             *
             **/
            if (!natlBounds.pad(30).contains(self.map.getBounds())) {
              self.map.fitBounds(natlBounds, {
                paddingTopLeft: new L.Point(sidebarExpansionWidth, 0)
              });
            }

          },
          style:  {color: 'blue', fillColor:'none', weight: 1, dashArray: '1'}
        }).addTo(self.map);

      });
    });
  },
  //jscs:enable

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
    this.projectSitesLayersView.bringToFront();
  }
});
