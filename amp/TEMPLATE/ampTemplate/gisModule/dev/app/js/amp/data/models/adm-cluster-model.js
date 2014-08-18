var Deferred = require('jquery').Deferred;
var _ = require('underscore');
var Backbone = require('backbone');


module.exports = Backbone.Model.extend({

  initialize: function() {

    this._dataLoaded = null;
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
      var filter = {adminLevel: this.get('value')};
      this.fetch({
        data: JSON.stringify(filter),
        type: 'POST'
      }).then(self._dataLoaded.resolve);
    }

    this._dataLoaded.then(function() {
      self.trigger('loaded processed', self);
    });

    return this._dataLoaded.promise();
  }

});
