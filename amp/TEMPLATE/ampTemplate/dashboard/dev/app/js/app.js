require('./ugly/lib-load-hacks');
var jquery = require('jquery');
var boilerplate = require('amp-boilerplate');
var App = require('./app/app-class');

var h = document.querySelector('#amp-header');
if (h) {
  h.innerHTML = boilerplate.header;
  jquery('.dropdown-toggle').dropdown()
} else {
  console.error('no container for header');
}

var app = new App({ el: '#amp-dashboard' });
window.app = app;  // for debugging convenience
app.render();
