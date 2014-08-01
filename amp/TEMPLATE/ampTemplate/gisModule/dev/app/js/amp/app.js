// set up jquery, bootstrap, etc.
require('../libs/local/lib-load-hacks');

var $ = require('jquery');

var GISData = require('./data/gis-data');
var GISDisplay = require('./display/gis-display');
var App = require('./gis/views/gis-main');

var state = require('./services/state');

// initialize everything that doesn't need to touch the DOM
var gisData = new GISData();
var app = new App({
  data: gisData,
  display: new GISDisplay({data: gisData})
});

app.data.load();

// attach a ref to services
app.state = state;


$(document).ready(function() {
  // Attach to the DOM and do all the dom-y stuff
  app.setElement($('#gis-plugin')).render();
});


module.exports = window.app = app;
