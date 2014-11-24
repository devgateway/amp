var _ = require('underscore');
var Deferred = require('jquery').Deferred;
var BackboneDash = require('./backbone-dash');

var URLService = require('amp-url/index');
var State = require('amp-state/index');
var fs = require('fs');
var Translator = require('amp-translate');
var Filter = require('amp-filter/src/main');
var SavedDashes = require('./models/saved-dashes-collection.js');

var MainView = require('./views/main');
var FailView = require('./views/fail');


function App() {
  if (!(this instanceof App)) {
    throw new Error('App needs to be instantiated with the `new` keyword.');
  }
  this.initialize.apply(this, arguments);
}


_.extend(App.prototype, BackboneDash.Events, {

  initialize: function(options) {
    var _initDefer = new Deferred();
    this.initialized = _initDefer.promise();

    try {
      // initialize app services
      this.url = new URLService();
      this.savedDashes = new SavedDashes([], { app: this });
      this.state = new State({
        url: this.url,
        saved: this.savedDashes
      });

      var dashboardTranslateKeys = JSON.parse(fs.readFileSync(__dirname +
        '/templates/initial-translation-request.json', 'utf8'));
      this.translator = new Translator({
        defaultKeys: dashboardTranslateKeys,
        ajax: BackboneDash.wrappedAjax
      });

      this.filter = new Filter({
        draggable: true,
        sync: options.sync || BackboneDash.sync
      });

      // initialize app components
      this.view = new MainView({ app: this, el: options.el });

      _initDefer.resolve(this);
    } catch (e) {
      _.defer(function() { throw e; });
      this.view = new FailView({ app: this, el: options.el, err: e});
      this.err = e;
      _initDefer.reject(this);
    }
  },

  render: function() {
    // TODO: fix some parts of the app so we can load translations async
    this.tryAfter(this.translator.promise, this.view.render, this.view);
    // this.tryTo(this.view.render, this.view);

    /* ensure entire page--header and footer, not just this view is translated */
    // TODO: if possible, move this out of app-class
    // or at least make it more targeted than document
    this.translator.translateDOM(document);
  },

  viewFail: function(view, err) {
    view.$el.html(new FailView({ app: this, err: err }).render().el);
  },

  tryTo: function(fn, view) {
    try {
      return fn.call(view);
    } catch (e) {
      _.defer(function() { throw e; });
      this.viewFail(view, e);
    }
  },

  tryAfter: function(promise, fn, view) {
    promise
      .done(_(function() {
        this.tryTo(fn, view);
      }).bind(this))
      .fail(_(function() {
        this.viewFail(view, 'failed to load');
      }).bind(this));
  },

  report: function(title, messages) {
    this.initialized
      .done(function(app) {
        app.view.report(title, messages);
      })
      .fail(function() {
        console.warn('REPORT:', title, messages);
      });
  }

});


module.exports = App;
