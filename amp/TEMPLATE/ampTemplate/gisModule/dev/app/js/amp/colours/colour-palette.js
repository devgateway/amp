var _ = require('underscore');
var Backbone = require('backbone');
var seedrandom = require('seedrandom');
var niceBuckets = require('nice-buckets');
var Colour = require('./colour-model');


// magic numbers
var DEFAULT = {
  H_SKEW: -36,  // degrees
  S_MIN: 45,    // percent
  S_MAX: 90,    // percent
  L_MIN: 33,    // percent
  L_MAX: 85     // percent
};


var Colours = Backbone.Collection.extend({
  model: Colour
});


var Palette = Backbone.Model.extend({

  initialize: function(options) {
    this.colours = new Colours();
    if (options.mode === 'range') {
      this.listenTo(this, 'change', this.generateRange);
    }
  },

  generateRange: function() {
    var newColours = [],
        stops = this.get('stops'),
        buckets = niceBuckets.minFigs(5, [this.get('min'), this.get('max')]),
        hStopSize = DEFAULT.H_SKEW / (stops - 1),
        sStopSize = (DEFAULT.S_MAX - DEFAULT.S_MIN) / (stops - 1),
        lStopSize = (DEFAULT.L_MAX - DEFAULT.L_MIN) / (stops - 1);
        // stopMin,
        // stopMax,
        // h,
        // s,
        // l;

    function makeTest(bucket) {
      return function(value) {
        return value >= bucket[0] && value <= bucket[1];
      };
    }

    for (var stop=0; stop < stops; stop++) {
      newColours.push({
        h: (this.get('rootHue') + (hStopSize * stop) + 360) % 360,
        s: DEFAULT.S_MIN + (sStopSize * stop),
        l: DEFAULT.L_MAX - (lStopSize * stop),  // dark to bright
        value: buckets[stop],
        test: makeTest(buckets[stop]),
        userSpecified: (stop === 0) ? true : false
      });
    }

    this.colours.reset(newColours);
  }

});


// Constructors

function FromRange(options) {
  if (! _.has(options, 'seed')) {
    throw new Error('seed is required.');
  }

  var palette = new Palette({
    mode: 'range',
    min: options.min || 0,
    max: options.max || 100,
    stops: options.stops || 5,
    linLog: options.linLog || 'linear'
  });

  // after it's constructed so that its change listener will fire
  palette.set('rootHue', Math.floor(seedrandom(options.seed)() * 360));  // deterministically derive hue from seed)

  return palette;
}

function FromSet() {
  return new Palette({
    mode: 'discrete'
  });
}


module.exports = {
  FromRange: FromRange,
  FromSet: FromSet
};
