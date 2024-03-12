/* global app */
var $ = require('jquery');
var fs = require('fs');
var _ = require('underscore');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var PopupTemplate = fs.readFileSync(__dirname + '/../templates/structure-cluster-popup-template.html', 'utf8');

module.exports = {

  MAXCLUSTERRADIUS: 0.001,

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
      removeOutsideVisibleBounds: true,
      chunkedLoading: true,
      chunkInterval: 300,
      // measured processing time for markerCluster on Phil's computer for different chunkIntervals
      // interval   samples of processing time     average
      //      off    863, 1282,  994, 1709, 1210   1212
      //       50   2158, 1710, 1824, 2672, 2325   2138
      //      100   1494, 1378, 1533, 1272, 1997   1535
      //      200   1939, 1093, 1147, 1358, 1100   1327
      //      300    913, 1192,  970, 1363, 1172   1122  winner
      //      400   1058, 1619,  966, 1104, 1164   1182
      //      500   1493, 1445, 1206, 1323, 1069   1307
      //      750   1475,  990, 1374, 1074, 1295   1242
      //     1000   1281, 1568, 1848, 1903,  933   1507
      //     1250   1902, 1246,  912,  975, 1584   1324
      //     1500    929, 1696, 1419,  947,  961   1190
      //     2000    961, 1222,  945, 1012, 1463   1121
      chunkProgress: function(lastCompleted, total) {
        // TODO: it would be nice to move this responsibility somewhere else...
        if (lastCompleted === total) {
          $('#map-loading').hide();
        }
      }
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
    if (self.structureMenuModel.get('filterVertical') === 'Primary Sector' &&
      self.rawData.features.length < self.maxNumberOfIcons) {

      var filterVertical = self.structureMenuModel.get('filterVertical');
      var sectorCode = 0; // 0 is 'various sectors icon'
      if (markers[0].feature) {
        var activity = markers[0].feature.properties.activity;
        if (activity.attributes.matchesFilters[filterVertical][0] instanceof Object) {
          sectorCode = activity.attributes.matchesFilters[filterVertical][0].get('code');
        } else {
          sectorCode = activity.attributes.matchesFilters[filterVertical][0];
        }
      } else if (!markers[0].feature) {
        //TODO: this sometimes happen if loads are being strange / slow....
        //  gives cluster wrong colour / icon on first load / initial zoom level.
        console.warn('markers not done loading :(, missing feature markers[0]', markers[0]);
      }

      //icons need to be abit bigger than plain circles, so bump up by 2
      var iconStyleCode = this.structureMenuModel.getSelectedIconStyleCode(sectorCode);

      marker = new L.circleDivIcon(Math.max(18, size + 2), {
        className: 'marker-cluster ',
        html: '<div class="leaflet-marker-icon"><div class="cluster-svg-map-icon svg-map-icon ' + iconStyleCode + '"></div><div class="text">' + markers.length + '</div>',
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
        console.warn('markers not done loading :(, missing feature markers[0]', markers[0]);
      }

      marker = new L.circleDivIcon(size, {
        className: 'marker-cluster' + (zoomedIn ? '' : ' marker-cluster-small'),
        html: (zoomedIn ? '<div class="text">' + markers.length + '</div>' : ''),
        color: '#444',
        fillColor: (colors[0] && colors[0].hex()),
        weight: 1,
        importantFill: true
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
    app.translator.translateDOM($('.cluster-popup'));
  }
};
