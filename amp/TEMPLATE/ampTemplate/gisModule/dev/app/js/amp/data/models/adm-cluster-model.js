var when = require('jquery').when;
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');


module.exports = Backbone.Model
.extend(LoadOnceMixin).extend({
  url: '/rest/gis/cluster',

  initialize: function() {
    this.listenTo(this, 'change:selected', function(blah, show) {
      // TODO: ideally only fetch if fitlers have changed, but trust cacheing...

      // this is ugly, but necesesary to stop showLayers call to /load from triggering another fetch.
      if (show) {
        if (!this._loaded) {
          this.load();
        } else {
          this.fetch();
        }
      }

      // important to trigger after fetch / load.
      this.trigger(show ? 'show' : 'hide', this);
    });
    this.listenTo(this, 'sync', this.updatePaletteRange);
    this.listenTo(this.collection.filter, 'apply', this.applyFilters);
  },

  // if filters change and layer is selected update it.
  applyFilters: function() {
    if (this.get('selected')) {
      this.fetch();
    }
  },

  fetch: function(options) {
    var filter = {otherFilters: {}};

    // TODO: move lastFetchXhr code into a mixin or something...
    //Stop last fetch before doing new one.
    //request is between initialized(0) and complete(4).
    if (this.lastFetchXhr && this.lastFetchXhr.readyState > 0 && this.lastFetchXhr.readyState < 4) {
      this.lastFetchXhr.abort();
    }

    // get filters
    if (this.collection.filter) {
      _.extend(filter, this.collection.filter.serialize());
    }

    // TODO: verify settings works..
    filter.settings = this.collection.settings.serialize();

    filter.otherFilters.adminLevel = this._translateADMToMagicWord(this.get('value'));

    options = _.defaults((options || {}), {
      type: 'POST',
      data: JSON.stringify(filter)
    });

    this.lastFetchXhr = Backbone.Model.prototype.fetch.call(this, options);
    return this.lastFetchXhr;
  },

  loadBoundary: function() {
    var boundary = this.collection.boundaries.findWhere({id: this.get('value')});  // TODO ...
    if (boundary) {
      var boundaryLoaded = boundary.load();
      when(boundaryLoaded, this.load())         // Order is important!
        .done(function(boundaryModel, self) {  // ... because their return value is passed here
          self.set('boundary', boundaryModel.toJSON());
        });
      return boundaryLoaded.promise();
    } else {
      console.error('No boundary found for ' + this.get('value'));

      var failedBoundary = new $.Deferred();
      failedBoundary.reject();
      return failedBoundary.promise();

    }
  },

  loadAll: function() {
    return when(this.load(), this.loadBoundary()).promise();
  },

  _translateADMToMagicWord: function(admString) {
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
  }

});
