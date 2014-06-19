require(['/test/util/frame.js'], function(frame) {
  frame.testPage('/', function(QUnit, frame$, done) {

    window.setTimeout(function waitForBackBone() {

      QUnit.test('Load the AMP navbar', function(assert) {
        assert.ok(frame$('#amp-menu').is(':visible'), 'Navbar loaded');
      });

      QUnit.test('Load the map header', function(assert) {
        assert.ok(frame$('#map-header').is(':visible'), 'Map header loaded');
        assert.ok(frame$('#map-header .navbar-brand').text(), 'Map header info loaded');
        assert.ok(frame$('#basemap-gallery .dropdown').is(':visible'), 'Basemap gallery loaded');
      });

      done();
    }, 500);

  });
});
