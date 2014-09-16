// set up jquery, bootstrap, etc.
require('../libs/local/lib-load-hacks');

var $ = require('jquery');

var GISData = require('./data/gis-data');
var App = require('./gis/views/gis-main');

var state = require('./services/state');
var translator = require('./services/translator');
var WindowTitle = require('./services/title');


// initialize everything that doesn't need to touch the DOM
var app = new App({
  data: new GISData()
});

app.data.load();


// attach a ref to services
app.state = state;
app.translator = translator;

// hook up the title
var windowTitle = new WindowTitle('AMP GIS Module');
windowTitle.listenTo(app.data.title, 'update', windowTitle.set);


$(document).ready(function() {
  // Attach to the DOM and do all the dom-y stuff
  app.setElement($('#gis-plugin')).render();
});


module.exports = window.app = app;
