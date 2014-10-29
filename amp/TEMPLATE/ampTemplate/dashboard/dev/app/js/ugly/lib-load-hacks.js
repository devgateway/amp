var jQuery = require('jquery');

// boostrap looks for jquery in the global namespace, so put it there.
window.jQuery = jQuery;
require('bootstrap/dist/js/bootstrap');

// make backbone and d3 play nice
window.d3 = require('d3');
require('nvd3');

// load underscore mixins
require('./underscore-transpose');
