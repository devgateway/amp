var Backbone = require('backbone');
var _ = require('underscore');
var when = require('jquery').when;
var ActivityModel = require('./activity-model');
require('backbone-associations');

var toActivity = function(idArray) {

  /*TODO(thadk) handle multiple */
  var id = idArray[0];

  console.log('toActivity', this);

  //if (id instanceof ActivityModel) { return id; }
  //if (_.isObject(id)) { return id; }

  when(window.app.data.relevantActivitesFetch)
    .done(function() {
      console.log(id, window.app.data.activities.get(id));
      return window.app.data.activities.get(id).attributes;
    })
    .fail(function() {
      console.log("failed toActivity");
      return id;
    });


  //var activs = window.app.data.activities;
  //var found = activs && activs.find(function(m) {
    //return m.get(m.idAttribute) === id;
  //});
  //return found ? found : id;
};

/* Structure model aka ProjectSite model */
module.exports = Backbone.AssociatedModel.extend({
  defaults: {
    geometryType: 'Point',
    lat: -180,
    lng: -180,
    title: 'Untitled',
    activities: [],
    id: null
  },
  relations: [
    {
      type: Backbone.Many,
      key: 'activities',
      relatedModel: ActivityModel,
      map: toActivity
    }
  ],

  /* Model Parse does not run for each model when a collection is fetched */
  parse: function(response) {
    /* If you don't wish this to run something after a full collection fetch,
     * you might want to check truthyness of options.collection.
     **/

    /** GeoJSON to unpack into this object attributes
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

      attributesFromGeoJson.lng = response.geometry.coordinates[0];
      attributesFromGeoJson.lat = response.geometry.coordinates[1];

      if (response.properties.activity !== undefined) {
        attributesFromGeoJson.activities = response.properties.activity;
      }
      if (response.properties.title !== undefined) {
        attributesFromGeoJson.title = response.properties.title;
      }

      attributesFromGeoJson.id = response.id;
      return attributesFromGeoJson;

    } else {
      return response;
    }
  }
});

