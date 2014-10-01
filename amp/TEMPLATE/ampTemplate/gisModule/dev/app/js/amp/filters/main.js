var Backbone = require('backbone');
var _ = require('underscore');

var FilterView = require('./views/filters-view');



function Widget() {
  this.initialize.apply(this, arguments);
}

_.extend(Widget.prototype, Backbone.Events, {

  initialize: function(options) {
    var self = this;
    this.view = new FilterView(options);

    // loaded deferred to allow for other code to wait for filters to finish loading.
    this.loaded = this.view._loaded;

    this.view.on('cancel', function() {
      self.trigger('cancel');
    });

    this.view.on('apply', function() {
      self.trigger('apply');
    });
  },

  showFilters: function(){
    this.view.showFilters();
  },

  serialize: function() {
    return this.view.serialize();
  },

  deserialize: function(stateBlob) {
    return this.view.deserialize(stateBlob);
  }
});



module.exports = Widget;
