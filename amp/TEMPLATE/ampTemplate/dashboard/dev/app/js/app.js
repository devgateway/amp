require('./ugly/lib-load-hacks');
var jquery = require('jquery');
var boilerplate = require('amp-boilerplate');
var UserModel = require('./app/models/amp-user.js');
var App = require('./app/app-class');

var $header = jquery('#amp-header');
if ($header) {
  $header.html(boilerplate.header);
  jquery('.dropdown-toggle').dropdown();
} else {
  console.error('no container for header');
}

var app = new App({ el: '#amp-dashboard' });
window.app = app;  // for debugging convenience
app.render();
