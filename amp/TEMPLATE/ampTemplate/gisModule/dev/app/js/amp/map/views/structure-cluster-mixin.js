var fs = require('fs');
var _ = require('underscore');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var PopupTemplate = fs.readFileSync(__dirname + '/../templates/structure-cluster-popup-template.html', 'utf8');

module.exports = {

  projectListTemplate: _.template(PopupTemplate),

  initCluster: function() {
    var self = this;

    this.layerLoadState = 'pending';  // 'loading', 'loaded'.

    var model = self.app.data.structures;

    //TODO: checkout prune cluster, supposedly way faster...
    // may also be worth doing manually since we don't want updates on zoom
    this.markerCluster = new L.markerClusterGroup({
      maxClusterRadius: this.MAXCLUSTERRADIUS,
      iconCreateFunction: function(cluster) {return self._createCluster(cluster, model);},
      zoomToBoundsOnClick: false,
      showCoverageOnHover: false,
      spiderfyOnMaxZoom: false,
      removeOutsideVisibleBounds: true
    });
  },

  _createCluster: function(cluster, model) {
    var self = this;
    var markers = cluster.getAllChildMarkers();
    var size = (markers.length / self.maxClusterCount) * self.MAX_CLUSTER_SIZE;
    size = Math.min(self.MAX_CLUSTER_SIZE, size);
    size = Math.max(self.BIG_ICON_RADIUS, size);

    var zoomedIn = (self.map.getZoom() >= self.ZOOM_BREAKPOINT);
    if (zoomedIn) {
      size += self.BIG_ICON_RADIUS;
    }

    var marker = null;

    //Try and show icons if looking at sectors
    if (self.structureMenuModel.get('filterVertical') === 'Primary Sector Id' &&
      self.rawData.features.length < self.MAX_NUM_FOR_ICONS) {

      var filterVertical = self.structureMenuModel.get('filterVertical');
      var sectorCode = 0; // 0 is 'various sectors icon'

      if (markers[0].feature) {
        var activity = markers[0].feature.properties.activity;
        sectorCode = activity.attributes.matchesFilters[filterVertical][0].get('code');
      } else if (!markers[0].feature) {
        //TODO: this sometimes happen if loads are being strange / slow....
        //  gives cluster wrong colour / icon on first load / initial zoom level.
        console.log('markers not done loading :(, missing feature markers[0]', markers[0]);
      }

      //icons need to be abit bigger than plain circles, so bump up by 2
      marker = new L.circleDivIcon(Math.max(18, size + 2), {
        className: 'marker-cluster ',
        html: '<img src="img/map-icons/' +
          self.structureMenuModel.iconMappings[sectorCode] +
          '"><div class="text">' + markers.length + '</div>',
        color: '#444',
        fillColor: '#fff',
        weight: 0
      });
    } else {  // no Icon normal circle

      var colors = _(markers)
        .chain()
        .map(function(m) {
          //self.structureMenuModel.structuresCollection
          var colour = model.palette.colours.find(function(c) {
            return c.get('test').call(c, (m.feature ? m.feature.properties.id : -1));
          });
          return colour;
        })
        .uniq()
        .value();

      if (colors.length > 1) {
        colors = [model.palette.colours.find(function(c) {
          return c.get('multiple') === true;
        })];
      } else if (!markers[0].feature) {
        //TODO: this sometimes happen if loads are being strange / slow....
        //  gives cluster wrong colour / icon on first load / initial zoom level.
        console.log('markers not done loading :(, missing feature markers[0]', markers[0]);
      }

      marker = new L.circleDivIcon(size, {
        className: 'marker-cluster' + (zoomedIn ? '' : ' marker-cluster-small'),
        html: (zoomedIn ? '<div class="text">' + markers.length + '</div>' : ''),
        color: '#444',
        fillColor: (colors[0] && colors[0].hex()),
        weight: 1
      });
    }
    return marker;
  },

  clusterClick: function(a) {
    var self = this;
    //TODO: seems silly to bind on every click...
    // Once we have nailed down desired functionality it will be worth writing our own clusterer since it only
    // needs to run once on project site load and that is it. (not each zoom etc.)
    var parsedProjectSitesList = _.chain(a.layer.getAllChildMarkers())
      .pluck('feature')
      .map(this.app.data.structures.model.prototype.parse)
      .value();


    a.layer.bindPopup(
      self.projectListTemplate({ structures: parsedProjectSitesList}),
      {
        maxWidth: 500,
        offset: new L.Point(0, -6)
      });

    a.layer.openPopup(self.map);
  },


  // Create pop-ups
  _onEachFeature: function(feature, layer) {
    var self = this;

    /* TODO(thadk) switch individual feature to this standard parsed model input*/
    /*var parsedProjectSitesList = this.app.data.structures.model.prototype.parse(feature);*/

    if (feature.properties) {
      var activityJSON = feature.properties.activity.toJSON();
      layer.bindPopup(self.structureTemplate({
        activityJSON: activityJSON,
        properties: feature.properties
      }),
      {
        maxWidth: 450,
        offset: new L.Point(0, -2)
      });
    }

    layer.on('click', function(evt) {
      var feature = evt.target.feature;
      if (feature) {
        var projectId = feature.properties.activity.id;
        self._hilightProject(projectId);
      }
    });

    layer.on('popupclose', function(evt) {
      var feature = evt.target.feature;
      if (feature) {
        var projectId = feature.properties.activity.id;
        self._dehilightProject(projectId);
      }
    });
  }
};
