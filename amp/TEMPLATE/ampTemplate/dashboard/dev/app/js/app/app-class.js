var _ = require('underscore');
var Deferred = require('jquery').Deferred;
var BackboneDash = require('./backbone-dash');
var supportCheck = require('./check-support');
var URLService = require('amp-url/index');
var State = require('amp-state/index');
var StateLoadError = require('amp-state/index').StateLoadError;
var fs = require('fs');
var Translator = require('amp-translate');
var Filter = require('amp-filter/src/main');
var Settings = require('amp-settings/src/index');
var UserModel = require('./models/amp-user.js');
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
	
  rendered: false,

  initialize: function(options) {
    var _initDefer = new Deferred(),
        missingFeatures;
    this.initialized = _initDefer.promise();

    try {
    	
    	this.user = new UserModel()

      // check our support level
      this.browserIssues = supportCheck();
      _(this.browserIssues).chain()
        .groupBy('severity')
        .each(function(severityGroup, severity) {
          missingFeatures = _(severityGroup).pluck('feature').join(', ');
          if (severity === 'critical') {
            throw {
              name: 'Incompatible Web Browser',
              message: 'Dashboards cannot work without these features, which are ' +
                       'not supported by your web browser: ' + missingFeatures + '. ' +
                       'Any <a href="http://browsehappy.com/">modern browser</a> will work.',
              toString: function() { return this.name + ': ' + this.message; }
            };
          } else if (severity === 'major') {
            this.report('Limited support for old web browsers', [
              'Your browser does not provide some features used by Dashboards: ' +
              missingFeatures + '.',
              'Some features may not work correctly, however any ' +
              '<a href="http://browsehappy.com/">modern browser</a> will provide ' +
              'a better experience.']);
          } else if (severity === 'minor') {
            console.warn('This browser is missing support for', missingFeatures);
          }
        }, this);

      // inject downloadify if we have no download but have flash (IE)
      if (this.hasIssue('download') && !this.hasIssue('flash')) {
        var swfObj = document.createElement('script'),
            downloadify = document.createElement('script');
        swfObj.src = '/TEMPLATE/ampTemplate/commonJs/swfobject-2.2.js';
        downloadify.src = '/TEMPLATE/ampTemplate/commonJs/downloadify-0.2.js';
        document.body.appendChild(swfObj);
        document.body.appendChild(downloadify);
      }

      // initialize app services
      this.url = new URLService();
      this.savedDashes = new SavedDashes([], { app: this });
      this.state = new State({
        url: this.url,
        saved: this.savedDashes
      });
      
      // try to load an initial state from the url
      try {
    	  this.state.urlMaybeLoad();
      } catch (e) {
          if (e instanceof StateLoadError) {
        	  this.report('Could not load saved dashboard', ['If you are trying to load a shared link, please make sure the entire URL was copied']);
        	  this.url.hash('');  // clear the bad saved-state hash
          } else {
        	  throw e;
          }
      }

      var dashboardTranslateKeys = JSON.parse(fs.readFileSync(__dirname +
        '/templates/initial-translation-request.json', 'utf8'));
      this.translator = new Translator({
        defaultKeys: dashboardTranslateKeys,
        ajax: BackboneDash.wrappedAjax
      });
      
      this.initSettings();
      
      // TODO: handle translations load failure      ​
      this.filter = new Filter({
          draggable: true,
          sync: options.sync || BackboneDash.sync,
          caller: 'DASHBOARD'
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

  hasIssue: function(featureName) {
    return !!_(this.browserIssues).findWhere({feature: featureName});
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
        app.modal(title, {messages: messages, tone: 'warning'});
      })
      .fail(function() {
        console.warn('REPORT:', title, messages);
      });
  },

  modal: function(title, options) {
    options = _({}).extend(options, {tone: 'primary'});
    var modalReady = new Deferred();
    this.initialized
      .done(function(app) {
        var modalEl = app.view.modal(title, options);
        modalReady.resolve(modalEl);
      })
      .fail(function() {
        console.warn('failed to show modal because the app views did not initialize', title);
        modalReady.reject('app views did not init');
      });
    return modalReady.promise();
  },
  initSettings: function(){
	this.settingsWidget = new Settings.SettingsWidget({
	  		draggable : true,
	  		caller : 'DASHBOARDS',
	  		isPopup: true,
	  		definitionUrl: '/rest/settings-definitions/dashboards'
	});	
	this.generalSettings = new Settings.GeneralSettings();
	this.generalSettings.load();	
  }
});

module.exports = App;
