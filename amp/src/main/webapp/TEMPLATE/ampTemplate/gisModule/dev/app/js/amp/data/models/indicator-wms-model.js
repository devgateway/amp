var Deferred = require('jquery').Deferred;
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');


module.exports = Backbone.Model
.extend(LoadOnceMixin).extend({

  initialize: function() {
    // TODO: factor this behavior into an indicator base class
    this.listenTo(this, 'change:selected', function(blah, show) {
      this.trigger(show ? 'show' : 'hide', this);
    });
  },

  fetch: function() {
    return (new Deferred()).resolve().promise();  // nothing to actually do here...
  }

});
