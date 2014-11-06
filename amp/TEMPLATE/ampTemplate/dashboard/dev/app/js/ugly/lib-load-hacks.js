// nvd3 goes global sigh... make sure d3 is already global
require('nvd3');

// load underscore mixins
require('./underscore-transpose');

// load canvg stuff
window.RGBColor = require('./lib-src/rgbcolor');
require('./lib-src/canvg');


module.exports = {
  canvg: window.canvg
};
