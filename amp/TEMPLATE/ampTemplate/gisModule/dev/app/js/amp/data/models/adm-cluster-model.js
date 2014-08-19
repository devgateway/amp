var Deferred = require('jquery').Deferred;
var when = require('jquery').when;
var _ = require('underscore');
var Backbone = require('backbone');


module.exports = Backbone.Model.extend({

  initialize: function() {

      // private load/processing state tracking
    this._dataLoaded = null;
    this._boundaryLoaded = null;
    this._boundaryJoined = new Deferred();  // resolves after processing

    this.listenTo(this, 'change:selected', function(blah, show) {
      this.trigger(show ? 'show' : 'hide', this);
    });
  },

  load: function() {
    var self = this;

    // don't load more than once
    if (_.isNull(this._dataLoaded)) {
      this._dataLoaded = new Deferred();

      // TODO: admin clusters should get url endpoints, not query endpoints.
      var filter = {adminLevel: self._translateADMToMagicWord(this.get('value'))};
      this.fetch({
        data: JSON.stringify(filter),
        type: 'POST'
      }).then(self._dataLoaded.resolve);
    }

    if (_.isNull(this._boundaryLoaded)) {
      this._boundaryLoaded = new Deferred();

      var boundary = this.collection.boundaries.findWhere({id: this.get('value')});
      if (! boundary) {  // sanity check
        throw new Error('No boundary found for indicator layer ' + this.get('value'));
      }
      boundary.loadGeoJSON()
        .then(function(geoJSON) {
          self._boundaryLoaded.resolve(geoJSON);
        });
    }

    this._dataLoaded.then(function() {
      self.trigger('loaded', self);
      self.updatePaletteRange();
    });

    when(this._boundaryLoaded, this._dataLoaded).then(function(geoJSON) {
      // TODO: see indicator-join-model (geoJSON here is sketch)
      self._joinDataWithBoundaries(geoJSON);
    });

    this._boundaryJoined.then(function() {
      self.trigger('processed', self);
    });

    return this._dataLoaded.promise();
  },

  _translateADMToMagicWord: function(admString){
    var magicWords = {
      'adm-0': 'Country',
      'adm-1': 'Region',
      'adm-2': 'Zone',
      'adm-3': 'District'
    };
    
    return magicWords[admString];
  },


  updatePaletteRange: function() {
    // TODO...
  },

  _joinDataWithBoundaries: function(boundaryGeoJSON) {
    if (this._boundaryJoined.state() === 'pending') {
      this.set('boundary', boundaryGeoJSON);
      this._boundaryJoined.resolve(boundaryGeoJSON);
    }
    return this._boundaryJoined.promise();
  }

});
