var husl = require('../../../libs/local/husl');
var _ = require('underscore');
var Backbone = require('backbone');


window.husl = husl;


module.exports = Backbone.Model.extend({

  rgbTemplate: _.template('rgb(<%= Math.round(c[0]*255) %>,'+
                              '<%= Math.round(c[1]*255) %>,'+
                              '<%= Math.round(c[2]*255) %>)'),

  defaults: {
    matchMode: null,
    // matchLow: -Infinity   <- set for matchMode 'interval'
    // matchHigh: +Infinity  <- set for matchMode 'interval'
    h: 0,
    s: 0,
    l: 0
  },

  match: function(val) {
    if (this.get('matchMode') === 'interval') {
      return val >= this.get('matchLow') && val <= this.get('matchHigh');
    } else {
      throw new Error('Cannot match colour', this, 'for matchmode', this.get('matchMode'));
    }
  },

  getHex: function() {
    return husl.toHex(this.get('h'), this.get('s'), this.get('l'));
  },

  getRGB: function() {
    var rgb = husl.toRGB(this.get('h'), this.get('s'), this.get('l'));
    return this.rgbTemplate({c: rgb});
  }

});
