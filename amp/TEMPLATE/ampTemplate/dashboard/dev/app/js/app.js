require('./ugly/lib-load-hacks');
var App = require('./app/app-class');

var app = new App('#amp-dashboard');
window.app = app;  // for debugging convenience
app.render();
