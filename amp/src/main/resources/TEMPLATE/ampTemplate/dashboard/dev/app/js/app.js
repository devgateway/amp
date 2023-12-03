require('./ugly/lib-load-hacks');
var jquery = require('jquery');
var _ = require('underscore');
var UserModel = require('./app/models/amp-user.js');
var App = require('./app/app-class');
var app = new App({ el: '#amp-dashboard' });
window.app = app;  // for debugging convenience

//Force call to the EP that sends the saved filters if any.
app.state.saved.load();
//app.render();
