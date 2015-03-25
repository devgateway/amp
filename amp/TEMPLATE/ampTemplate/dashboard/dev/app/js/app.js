require('./ugly/lib-load-hacks');
var jquery = require('jquery');
var UserModel = require('./app/models/amp-user.js');
var App = require('./app/app-class');
var app = new App({ el: '#amp-dashboard' });
window.app = app;  // for debugging convenience
app.render();
