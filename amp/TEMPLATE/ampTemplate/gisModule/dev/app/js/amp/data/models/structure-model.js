var Backbone = require('backbone');
var _ = require('underscore');
var ActivityModel = require('./activity-model');
require('backbone-associations');

var toActivity = function(idArray) {
  var id = idArray[0];
  console.log(id);
  if (id instanceof ActivityModel) { return id; }
  if (_.isObject(id)) { return id; }

  var activs = window.app.data.activities;
  var found = activs && activs.find(function(m) {
    return m.get(m.idAttribute) === id;
  });
  return found ? found : id;
};

var PropertiesModel = Backbone.AssociatedModel.extend({
  defaults: {
    title: 'Untitled',
    activity: []
  },
  relations: [
    {
      type: Backbone.One,
      key: 'activity',
      relatedModel: ActivityModel,
      map: toActivity
    }
  ]
});

module.exports = Backbone.AssociatedModel.extend({
  defaults: {
    geometryType: 'Point',
    coordinateX: 0,
    coordinateY: 0,
    title: 'Untitled',
    activities: [],
    id: null
  },
  relations: [
    {
      type: Backbone.One,
      key: 'properties',
      relatedModel: PropertiesModel
    }
  ],

  parse: function(response) {
    /** GeoJSON
     *
     * defaults: {
     *   type: 'Feature',
     *   geometry: {
     *     type: 'Point',
     *     coordinates: []
     *   },
     *   properties: {
     *     title: 'Untitled',
     *     activity: []
     *   },
     *   id: null
     * }
     *
     **/
    var attributesFromGeoJson = {};
    if (response.type === 'Feature') {
      attributesFromGeoJson.geometryType = response.geometry.type;
      attributesFromGeoJson.coordinateX = response.geometry.coordinates[0];
      attributesFromGeoJson.coordinateY = response.geometry.coordinates[1];
      if (undefined !== response.properties.activity) {
        attributesFromGeoJson.activities = response.properties.activity;
      }
      if (undefined !== response.properties.title) {
        attributesFromGeoJson.title = response.properties.title;
      }
      attributesFromGeoJson.id = response.id;
      return attributesFromGeoJson;
    }
    else {
      return response;
    }
  },

  initialize: function() {
    console.log('structure');
  }
});

