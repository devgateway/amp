var Deferred = require('jquery').Deferred;
var when = require('jquery').when;
var _ = require('underscore');
var Backbone = require('backbone');


module.exports = Backbone.Model.extend({

  initialize: function() {
    // provide the promises API for a "load" method, even though we don't have to load anything
    this._dummyDeferrer = new Deferred();

    // TODO: factor this behavior into an indicator base class
    this.listenTo(this, 'change:selected', function(blah, show) {
      this.trigger(show ? 'show' : 'hide', this);
    });
  },

  load: function() {
    var self = this;
    if (this._dummyDeferrer.state() === 'pending') {
      this._dummyDeferrer.resolve();  // nothing to load, so just resolve.
    }
    this._dummyDeferrer.then(function() {
      // just go ahead and declare us loaded and processed.
      self.trigger('loaded', self);
      self.trigger('processed', self);
    });
    return this._dummyDeferrer.promise();
  }

});
