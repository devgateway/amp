// nvd3 goes global sigh... make sure d3 is already global
require('nvd3');

// make stupid nvd3 dev logs go away >:(
window.nv.dev = false;

// load underscore mixins
require('./underscore-transpose');

// load canvg stuff
window.RGBColor = require('./lib-src/rgbcolor');
require('./lib-src/canvg');


module.exports = {
  canvg: window.canvg
};
