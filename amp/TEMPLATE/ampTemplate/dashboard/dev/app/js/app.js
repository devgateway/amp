require('./ugly/lib-load-hacks');
var App = require('./app/app-class');

var app = new App({ el: '#amp-dashboard' });
app.render();
window.app = app;  // for convenient debugging
