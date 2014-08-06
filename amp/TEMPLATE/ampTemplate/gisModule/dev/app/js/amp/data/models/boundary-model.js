var Deferred = require('jquery').Deferred;
var Backbone = require('backbone');

module.exports = Backbone.Model.extend({

  initialize: function() {
    this._geoJsonDeferrer = new Deferred();
  },

  loadGeoJSON: function() {
    if (this._geoJsonDeferrer.state() === 'pending') {
      var self = this;
      this.fetch().then(function(geoJSON) {
        self._geoJsonDeferrer.resolve(geoJSON);
      });
    }
    return this._geoJsonDeferrer.promise();
  }

});
