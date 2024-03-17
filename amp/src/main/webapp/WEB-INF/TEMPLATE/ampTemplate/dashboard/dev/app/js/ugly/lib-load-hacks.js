// nvd3 goes global sigh... make sure d3 is already global
/* TODO: in this version of nvd3 v1.7.1, main is not specified in package.json,
 if we ever upgrade to 1.8+, change this back to just require(nvd3) */
require('../../../node_modules/nvd3/build/nv.d3');

// load underscore mixins
require('./underscore-transpose');

// load canvg stuff
window.RGBColor = require('./lib-src/rgbcolor');
require('./lib-src/canvg');


module.exports = {
  canvg: window.canvg
};
