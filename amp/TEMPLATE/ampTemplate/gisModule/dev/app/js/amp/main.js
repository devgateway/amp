require('./lib-load-hacks');  // set up jquery, bootstrap, etc.
var _ = require('underscore');
var State = require('./services/state');
var MainView = require('./gis/views/gis-view');

// TODO: add some comments...
function App() { return this; }
var app = new App();

exports.state = new State();

var mainView = new MainView();
mainView.render();

exports.mainView = mainView;

window.app = _.extend(app, exports);
