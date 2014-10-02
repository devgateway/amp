require('./ugly/lib-load-hacks');

var _ = require('underscore');
var Deferred = require('jquery').Deferred;

var MainView = require('./app/views/main');
var FailView = require('./app/views/fail');


function App() {}
var app = new App();
window.app = app;  // for convenient debugging


function initMainView(options) {
  var initDeferrer = new Deferred();
  _.defer(function() {
    try {
      app.view = new MainView(options);
      initDeferrer.resolve();
    } catch (e) {
      app.view = new FailView(_({ err: e }).extend(options));
      initDeferrer.reject(e);
    }
  });
  return initDeferrer.promise();
}


initMainView({
  app: app,
  el: '#amp-dashboard'
}).always(function() {
  app.view.render();
}).fail(function(e) {
  throw e;
});
