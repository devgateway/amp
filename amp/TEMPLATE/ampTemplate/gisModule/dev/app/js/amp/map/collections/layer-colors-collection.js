var jssha = require('jssha');  // TODO: get a lighter library (we're not doing crypto)
var _ = require('underscore');
var Backbone = require('backbone');
var Color = require('../models/layer-color-model.js');


var ColorsCollection = Backbone.Collection.extend({
  /*
   * A customizeable collection of colours.
   */

  model: Color,

}, {
  /*
   * Color collection constructors
   */

  range: function(options) {
    if (!_.has(options, 'seed')) {
      throw new Error('Required option "seed" missing');
    }
    if (!_.has(options, 'min') || !_.has(options, 'max')) {
      throw new Error('Range mode must be provided a min and a max');
    }

    var rootHex = (new jssha(options.seed, 'TEXT')).getHash('SHA-1', 'HEX'),
        rootHue = parseInt(rootHex.slice(0, 2), 16),
        realStep = (options.max - options.min) / options.steps,
        centStep = 100 / (options.steps - 1);  // min to max, not min to (max - step)

    var colors = _.map(_.range(options.steps), function(stepNum) {
      var realIntervalLow = options.min + (realStep * stepNum),
          realIntervalHigh = realIntervalLow + realStep,
          percent = centStep * stepNum;
      return new Color({
        matchMode: 'interval',
        matchLow: realIntervalLow,
        matchHigh: realIntervalHigh,
        h: rootHue,
        s: percent,
        l: 80 - (Math.pow(percent / 100, 2) * 55)
      });
    }, this);

    var collection = new ColorsCollection(colors);
    collection.title = options.title || 'Untitled';
    return collection;
  },
});


module.exports = ColorsCollection;
