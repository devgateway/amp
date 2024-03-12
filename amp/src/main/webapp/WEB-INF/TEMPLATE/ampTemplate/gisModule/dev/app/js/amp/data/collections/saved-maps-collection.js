var Deferred = require('jquery').Deferred;
var Backbone = require('backbone');
var SavedMapModel = require('../models/saved-map-model');

module.exports = Backbone.Collection.extend({
  DEFAULT_URL: '/rest/gis/saved-maps',
  REPORT_URL: '/rest/gis/report',
  url: '/rest/gis/saved-maps',
  model: SavedMapModel,

  initialize: function(models, options) {
    this.appData = options.appData;
  },

//TODO: if report:  this.savedReports.url = '/rest/gis/report';
  load: function(stateId, prefix) {
    var self = this;
    if (prefix === 'report/') {
      this.url = this.REPORT_URL;
    } else {
      this.url = this.DEFAULT_URL;
    }
    var deferred = new Deferred();
    var loaded = this.get(stateId);
    if (loaded) {
      //reset URL
      this.url = this.DEFAULT_URL;
      deferred.resolve(loaded);
    } else {
      var model = this.model.fromId(stateId, { appData: this.appData });
      this.add(model);  // sets up collection so the model can find a URL
      model.fetch().done(function() {
        //reset URL
        self.url = self.DEFAULT_URL;
        deferred.resolve(model);
      });
    }

    return deferred.promise();
  }

});
