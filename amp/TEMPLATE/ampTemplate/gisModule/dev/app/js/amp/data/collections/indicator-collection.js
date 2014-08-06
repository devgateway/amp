var _ = require('underscore');
var Backbone = require('backbone');
var APIHelper = require('../../../libs/local/api-helper');
var RadioMixin = require('../../mixins/radio-mixin');
var Indicator = require('../models/indicator-model');


// I like the semantics of "reduce" here, but it's the same as
// var mixins = _.extend({}, RadioMixin, AnotherMixin, ...);
var mixins = _.reduce([
  RadioMixin
], _.extend, {});


module.exports = Backbone.Collection.extend(mixins).extend({

  url: APIHelper.getAPIBase() + '/rest/gis/indicator-layers',

  model: Indicator,

  initialize: function(options) {
    this.boundaries = options.boundaries;
  }

});
