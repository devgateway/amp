var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var Activity = require('../models/activity-model');


module.exports = Backbone.Collection
  .extend(LoadOnceMixin)
  .extend({

  url: '/rest/gis/activities',

  model: Activity

});
