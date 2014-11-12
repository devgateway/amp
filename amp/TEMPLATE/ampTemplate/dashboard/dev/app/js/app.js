require('./ugly/lib-load-hacks');
var boilerplate = require('amp-boilerplate');
var App = require('./app/app-class');

var h = document.querySelector('#amp-header');
h && (h.innerHTML = boilerplate.header) || console.error('no container for header');

var app = new App({ el: '#amp-dashboard' });
window.app = app;  // for debugging convenience
app.render();
