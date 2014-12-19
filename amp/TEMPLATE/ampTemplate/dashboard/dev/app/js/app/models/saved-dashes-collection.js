var _ = require('underscore');
var Deferred = require('jquery').Deferred;
var BackboneDash = require('../backbone-dash');
var SavedChart = require('./saved-dash');


module.exports = BackboneDash.Collection.extend({

  url: '/rest/dashboard/saved-charts',

  model: SavedChart,

  initialize: function(models, options) {
    this.app = options.app;
  },

  load: function(stateId) {
    var deferred = new Deferred();
    var loaded = this.get(stateId);
    if (loaded) {
      deferred.resolve(loaded);
    } else {
      var model = this.model.fromId(stateId, { app: this.app });
      this.add(model);  // sets up collection so the model can find a URL
      model.fetch()
        .done(function() {
          deferred.resolve(model);
        })
        .fail(_(function() {
          this.app.report('Failed to load saved dashboard', ['Could not retrieve the saved state.']);
          deferred.reject();
        }).bind(this));
    }

    return deferred.promise();
  }
});
