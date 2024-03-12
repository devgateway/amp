var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var IndicatorModel = require('../models/indicator-type-model');
module.exports = Backbone.Collection
.extend(LoadOnceMixin).extend({
  model: IndicatorModel,
  url: function() {
    return '/rest/indicator/indicator-types';
  }
});
