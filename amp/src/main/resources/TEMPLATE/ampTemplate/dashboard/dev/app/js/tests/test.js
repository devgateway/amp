require('../ugly/lib-load-hacks');
var _ = require('underscore');
var assert = require('chai').assert;

var App = require('../app/app-class');


var testAppOptions = {
  el: '#test-frame',
  sync: function() {
    // noop sync
    var d = new window.jQuery.Deferred();
    d.resolve();
    return d.promise();
  }
};


describe('Dashboards', function() {
  describe('initialization', function() {
    it('should not throw errors during initialization', function() {
      var app = new App(testAppOptions);
      assert.notProperty(app, 'err', app.err);
    });
    it('should not throw errors during render', function() {
      var app = new App(testAppOptions);
      if (_(app).has('err')) {
        assert.fail('has error', 'not having an error', 'app failed initialization, could not test render.');
        return;
      }
      app.render();
      assert.notProperty(app, 'err', app.err);
    });
  });

  describe('filters widget', function() {
    it('should render without error', function(finished) {
      var app = new App(testAppOptions);
      app.render();
      app.filter.showFilters();
      app.filter.view._loaded.done(finished);
    });
  });
});
