// set up jquery, bootstrap, etc.
require('../libs/local/lib-load-hacks');

var $ = require('jquery');

var GISData = require('./data/gis-data');
var App = require('./gis/views/gis-main');

var State = require('amp-state/index'); //require('./services/state');
var translator = require('./services/translator');
var WindowTitle = require('./services/title');
var URLService = require('./services/url');


var data = new GISData();
var url = new URLService();
var state = new State({ url: url, saved: data.savedMaps, autoinit: true });

// initialize everything that doesn't need to touch the DOM
var app = new App({
  url: url,
  data: data,
  state: state
});

app.data.load({state: state});

// attach a ref to services
app.translator = translator;



// hook up the title
var windowTitle = new WindowTitle('AMP GIS Module');
windowTitle.listenTo(app.data.title, 'update', windowTitle.set);


$(document).ready(function() {
  // Attach to the DOM and do all the dom-y stuff
  app.setElement($('#gis-plugin')).render();
});


module.exports = window.app = app;
