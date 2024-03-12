var _ = require('underscore');
var Backbone = require('backbone');
var seedrandom = require('seedrandom');
var niceBuckets = require('nice-buckets');
var Colour = require('./colour-model');
var Jenks = require('./jenks-utils');


// magic numbers
var DEFAULT = {
  H_SKEW: -36,  // degrees
  S_MIN: 45,    // percent
  S_MAX: 90,    // percent
  L_MIN: 33,    // percent
  L_MAX: 85,    // percent
  SET: [// thanks awesome http://colorbrewer2.org/
    {
      h: 243.79103959577307,
      s: 93.5281050401762,
      l: 48.101727992819335
    },
    {
      h: 126.84485704801882,
      s: 88.58742930928982,
      l: 57.84973819174576
    },
    {
      h: 12.047884070314995,
      s: 93.31646783803632,
      l: 48.27282919820934
    },
    {
      h: 29.78265489386481,
      s: 99.99999999999984,
      l: 66.61813759038522
    },
    {
      h: 280.9828886146671,
      s: 62.46783887305841,
      l: 35.32931965574711
    },
    {
      h: 27.62423428162062,
      s: 85.61992551050342,
      l: 47.633959963623596
    },
    {
      h: 225.92524050581477,
      s: 46.59601824068325,
      l: 80.34151308154121
    },
    {
      h: 112.03003811901955,
      s: 56.06236829633495,
      l: 83.71129027873012
    },
    {
      h: 12.705363439543902,
      s: 91.67945574513531,
      l: 73.23662062556197
    },
    {
      h: 50.30444261020004,
      s: 92.83936878503897,
      l: 81.16888137643387
    },
    {
      h: 293.5175982278909,
      s: 38.46499287730516,
      l: 75.34199526449905
    },
    {
      h: 85.87273351614228,
      s: 82.87185740645297,
      l: 97.72843903461742
    }
  ]
};


var Colours = Backbone.Collection.extend({
  model: Colour
});


var Palette = Backbone.Model.extend({

  initialize: function(options) {
    this.colours = new Colours();
    if (options.mode === 'range') {
      this.listenTo(this, 'change', this.generateRange);
    } else if (options.mode === 'discrete') {
      this.listenTo(this, 'change', this.generateSet);
    }
  },

  generateRange: function() {
    var stops = this.get('stops');
    //exclude null values that break Jenks.getGVF
    var values = _.reject(this.get('values'), function(value){ return value.value == null; });
    var zeroCategoryEnabled = this.get('zeroCategoryEnabled');
    
    var buckets = values.length > stops?
        Jenks.getGVF(values, stops, zeroCategoryEnabled):
        niceBuckets.minFigs(stops, [this.get('min'), this.get('max')]);
    var stopSize = stops > 1 ? stops: 1;
    var newColours = [],
        buckets = buckets,
        hStopSize = DEFAULT.H_SKEW / stopSize,
        sStopSize = (DEFAULT.S_MAX - DEFAULT.S_MIN) / stopSize,
        lStopSize = (DEFAULT.L_MAX - DEFAULT.L_MIN) / stopSize;

    function makeTest(bucket) {
      return function(value) {
        return bucket && value >= bucket[0] && value <= bucket[1];    	
      };
    }

    for (var stop = 0; stop < stops; stop++) {
      var color = this.getColour(stop, stops, sStopSize, lStopSize, hStopSize)
      newColours.push({
        h: color.h,
        s: color.s,
        l: color.l,  // dark to bright
        value: buckets[stop],
        test: makeTest(buckets[stop]),
        userSpecified: (stop === 0) ? true : false
      });
    }

    this.colours.reset(newColours);
  },
  getColour: function(stop, stops, sStopSize, lStopSize, hStopSize) {
	  if (this.get('multiColorSet') && stops <= this.get('multiColorSet').length) {
		  return {
			  h: this.get('multiColorSet')[stop][0],
			  s: this.get('multiColorSet')[stop][1],
			  l: this.get('multiColorSet')[stop][2]
		  };
	  } else {
		  return {h: (this.get('rootHue') + (hStopSize * stop) + 360) % 360,
		      s: DEFAULT.S_MIN + (sStopSize * stop),
		      l: DEFAULT.L_MAX - (lStopSize * stop)};  // dark to bright 
	  }	  
  },  
  generateSet: function() {

    var elements = this.get('elements'),
        maxNamed = DEFAULT.SET.length - 2;  // leave two for "multple" and "other"

    var localizedMultipleItem = [
                                 '<span data-i18n="amp.gis:legend-multiple">',
                                 'Multiple Projects ',
                                 '</span>'
                               ].join('');
    var localizedOthersItem = [
                                 '<span data-i18n="amp.gis:legend-others">',
                                 'Others ',
                                 '</span>'
                               ].join('');
    var topN = _(elements.slice(0, maxNamed)).map(function(el, idx) {
      function toTrueKey(o, k) {
        o[k] = true;
        return o;
      }
      return _({
        ids: [el.id],  // org id
        value: el.name,  // org name
        sites: el.sites,  // project sites
        sitesMap: _(el.sites).reduce(toTrueKey, {}),  // efficient lookup for test
        test: function(siteId) {
          return !!this.get('sitesMap')[siteId]; //double !! casts to boolean
        }
      }).extend(DEFAULT.SET[idx]);
    }, this);

    topN.push(_({
      value: localizedMultipleItem,
      test: function() {
        return false;  // don't match this unless it's asked for
      },
      multiple: true
    }).extend(DEFAULT.SET[topN.length]));

    if (elements.length > maxNamed) {
      topN.push(_({
        value: localizedOthersItem,
        test: function() {
          return true;  // fall-through default case
        }
      }).extend(DEFAULT.SET[topN.length]));
    }

    this.colours.reset(topN);
  }

});


// Constructors

function FromRange(options) {
  if (!_.has(options, 'seed')) {
    throw new Error('seed is required.');
  }

  var min = options.min || 0;
  var max = options.max || 100;
  var stops = options.stops || 5;
  var values = options.values || [];
  if (stops >= max || stops <= min) {
    // AMP-20346: If this parameter is too high or too low we can kill the browser.
    stops = 5;
  }
 
  var palette = new Palette({
    mode: 'range',
    min: min,
    max: max,
    stops: stops,
    linLog: options.linLog || 'linear',
    values: values,
    zeroCategoryEnabled: options.zeroCategoryEnabled
  });

  // after it's constructed so that its change listener will fire
  palette.set('rootHue', Math.floor(seedrandom(options.seed)() * 360));  // deterministically derive hue from seed)

  return palette;
}

function FromSet(options) {
  options = options || {};
  return new Palette({
    mode: 'discrete',
    elements: options.elements || ['a', 'b', 'c', 'd']
  });
}


module.exports = {
  FromRange: FromRange,
  FromSet: FromSet
};
