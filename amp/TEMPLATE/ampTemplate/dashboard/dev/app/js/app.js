require('./ugly/lib-load-hacks');
var jquery = require('jquery');
var boilerplate = require('amp-boilerplate');
var UserModel = require('./app/models/amp-user.js');
var App = require('./app/app-class');

var h = document.querySelector('#amp-header');
if (h) {
  h.innerHTML = boilerplate.header;
  var $header = jquery('#amp-header');

  /* Prepare the user data for the appropriate header */
  var userModel = new UserModel();
  window.userTest = userModel;

  $header.html(boilerplate.header);

  userModel.fetch().then(function(user) {
    if (user.email) {
      jquery('.container-fluid', $header).toggleClass('ampUserLoggedIn');
      jquery('#header-workspace', $header).text(user.workspace);
      jquery('#header-name #header-first-name', $header).text(user.firstName);
      jquery('#header-name #header-last-name', $header).text(user.lastName);
    }
  });

  jquery('.dropdown-toggle').dropdown();
} else {
  console.error('no container for header');
}

var app = new App({ el: '#amp-dashboard' });
window.app = app;  // for debugging convenience
app.render();
