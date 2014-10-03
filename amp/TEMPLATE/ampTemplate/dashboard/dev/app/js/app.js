require('./ugly/lib-load-hacks');

var _ = require('underscore');
var Deferred = require('jquery').Deferred;
var Events = require('backbone').Events;

var MainView = require('./app/views/main');
var FailView = require('./app/views/fail');


function App() {}
_.extend(App.prototype, Events);


function initMainView(app, options) {
  var initDeferrer = new Deferred();
  _.defer(function() {
    try {
      app.view = new MainView(_({ app: app }).extend(options));
      initDeferrer.resolve();
    } catch (e) {
      app.view = new FailView(_({ app: app, err: e }).extend(options));
      initDeferrer.reject(e);
    }
  });
  return initDeferrer.promise();
}

var app = new App();

initMainView(app, { el: '#amp-dashboard' })
  .always(function() {
    app.view.render();
  })
  .fail(function(e) {
    throw e;
  });


window.app = app;  // for convenient debugging
