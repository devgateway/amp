require('./lib-load-hacks');  // set up jquery, bootstrap, etc.

var GISView = require('./gis/views/gis-view');
var gisView = new GISView();
gisView.render();
