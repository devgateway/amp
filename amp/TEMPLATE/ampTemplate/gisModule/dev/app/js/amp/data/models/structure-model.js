var Backbone = require('backbone');
var _ = require('underscore');


/* Structure model aka ProjectSite model */
module.exports = Backbone.Model.extend({
  defaults: {
    geometryType: 'Point',
    lat: -180,
    lng: -180,
    title: 'Untitled',
    activities: [],
    id: null
  },
  /*
   * relations: [
   *  {
   *    type: backbone.many,
   *    key: 'activities',
   *    collectiontype: activities
   *  }
   * ],
   *
   */
  initialize: function() {
    _.bindAll(this, 'joinModelHelper');
  },

  /* Adds references to collectionB into collectionA joining on given foreign key
   * TODO: troubleshoot for model -> collection joining
  */
  joinModelHelper: function(modelA, collectionB, keyForForeignID, keyForCollectionDestination) {
    var idsToJoin = modelA.get(keyForForeignID);

    var tempCollection = collectionB.filter(function(modelB) {
      return _.indexOf(idsToJoin, modelB.get('id')) >= 0;
    });
    modelA.set(keyForCollectionDestination, tempCollection);
  },


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

      if (response.properties) {
        _.extend(attributesFromGeoJson, response.properties);
      }


      if (response.properties.activity !== undefined) {
        attributesFromGeoJson.activities = response.properties.activity;
        attributesFromGeoJson.activityZero = response.properties.activity[0];
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

