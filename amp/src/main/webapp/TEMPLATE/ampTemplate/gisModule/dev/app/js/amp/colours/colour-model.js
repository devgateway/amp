var husl = require('husl');
var _ = require('underscore');
var Backbone = require('backbone');

module.exports = Backbone.Model.extend({

  alphaTemplate: _.template('rgba(<%= r %>, <%= g %>, <%= b %>, <%= a %>)'),

  defaults: {
    h: undefined,
    s: undefined,
    l: undefined,
    userSpecified: false,  // userSpecified colours are respected more than generated ones
                          // which may be changed, eg. if the user changes the root hue, the
                          // non-userSpecified colours will follow (managed at the collection)
    value: null  // [from, to] for range, or an arbitrary value for discrete
  },

  hex: function() {
    return husl.toHex(this.get('h'), this.get('s'), this.get('l'));
  },

  rgba: function(alpha) {
    var rgb = husl.toRGB(this.get('h'), this.get('s'), this.get('l'));
    return this.alphaTemplate({
      r: rgb[0] * 255,
      g: rgb[1] * 255,
      b: rgb[2] * 255,
      a: alpha
    });
  }

});
