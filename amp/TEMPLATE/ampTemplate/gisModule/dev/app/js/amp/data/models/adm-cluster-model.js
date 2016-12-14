var when = require('jquery').when;
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');


module.exports = Backbone.Model
.extend(LoadOnceMixin).extend({
  url: '/rest/gis/cluster',

  initialize: function() {

  },

  attachListeners: function() {
    this.listenTo(this, 'change:selected', function(blah, show) {
      this.trigger(show ? 'show' : 'hide', this);
    });

    this.listenTo(this.collection.filter, 'apply', this.refreshModel);
    //this.listenTo(this.collection.settings, 'change:selected', this.refreshModel);
    this.listenTo(this.collection.settingsWidget, 'applySettings', this.refreshModel);
  },

  // if filters change and layer is selected update it.
  refreshModel: function() {
    // this forces next 'load' call to do a fresh fetch.
    delete this._loaded;

    if (this.get('selected')) {
      this.load();
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
    filter.settings = this.collection.settingsWidget.toAPIFormat();

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
  }
});
