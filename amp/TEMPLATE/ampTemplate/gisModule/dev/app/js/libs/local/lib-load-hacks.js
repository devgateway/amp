var jQuery = require('jquery');

// underscore mixins
require('underscore-groups-by');

// backbone needs a little help finding jQuery with our browserify setup
var Backbone = require('backbone');
Backbone.$ = jQuery;

// boostrap looks for jquery in the global namespace, so put it there.
window.jQuery = jQuery;

require('bootstrap/dist/js/bootstrap');

// enable CORS
jQuery.support.cors = true;

// init leaflet plugins
var L = require('../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');
L.Icon.Default.imagePath = '/img/map-icons';
require('../../../../node_modules/leaflet.markercluster/dist/leaflet.markercluster.js');
require('leaflet-div-style-icon');

// Override backbone as needed for AMP
require('./backbone-override');

// make backbone and d3 play nice
window.d3 = require('d3-browserify');
require('nvd3');
