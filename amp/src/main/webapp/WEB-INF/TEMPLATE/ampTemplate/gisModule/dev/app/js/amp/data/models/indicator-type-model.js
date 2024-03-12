var LoadOnceMixin = require('../../mixins/load-once-mixin');
var Backbone = require('backbone');
module.exports = Backbone.Model
.extend(LoadOnceMixin).extend({
  initialize: function() {
  }
});