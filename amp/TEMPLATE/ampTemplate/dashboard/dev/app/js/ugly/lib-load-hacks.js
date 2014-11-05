var jQuery = require('jquery');

// boostrap looks for jquery in the global namespace, so put it there.
window.jQuery = jQuery;
require('bootstrap/dist/js/bootstrap');

// make backbone and d3 play nice
window.d3 = require('d3-browserify');
require('nvd3');

// load underscore mixins
require('./underscore-transpose');

// load canvg stuff
window.RGBColor = require('./lib-src/rgbcolor');
require('./lib-src/canvg');


module.exports = {
  canvg: window.canvg
};
