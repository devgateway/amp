var when = require('jquery').when;
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');


module.exports = Backbone.Model
.extend(LoadOnceMixin).extend({

  initialize: function() {
    this.listenTo(this, 'change:selected', function(blah, show) {
      this.trigger(show ? 'show' : 'hide', this);

      // TODO: ideally only fetch if fitlers have changed, but trust cacheing...
      if (show) {
        this.fetch();
      }
    });
    this.listenTo(this, 'sync', this.updatePaletteRange);
    this.listenTo(this.collection.filter, 'apply', this.applyFilters);


  },

  // if filters change update it.
  applyFilters: function() {
    var self = this;
    if (this.get('selected')) {
      this.fetch().then(function() {
        self.trigger('refresh', self);
      });
    }
  },

  fetch: function(options) {
    var filter = {otherFilters: {}};

    // get filters
    if (this.collection.filter) {
      _.extend(filter, this.collection.filter.serialize());
    }

    filter.otherFilters.adminLevel = this._translateADMToMagicWord(this.get('value'));

    options = _.defaults((options || {}), {
      type: 'POST',
      data: JSON.stringify(filter)
    });

    return Backbone.Model.prototype.fetch.call(this, options);
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
