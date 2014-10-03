var jQuery = require('jquery');

// backbone needs a little help finding jQuery with our browserify setup
var Backbone = require('backbone');
Backbone.$ = jQuery;

// boostrap looks for jquery in the global namespace, so put it there.
window.jQuery = jQuery;
require('bootstrap/dist/js/bootstrap');

// make backbone and d3 play nice
window.d3 = require('d3');
require('nvd3');
