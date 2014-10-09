require('./ugly/lib-load-hacks');
var App = require('./app/app-class');

var app = new App({ el: '#amp-dashboard' });
window.app = app;  // for debugging convenience
app.render();
