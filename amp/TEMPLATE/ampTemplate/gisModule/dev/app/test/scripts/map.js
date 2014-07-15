var frame = require('/test/util/frame.js');


module.exports = frame.testPage('/', function(QUnit, frame$, done) {

  QUnit.test('Map & Leaflet loads', function(assert) {

    assert.ok(frame$('#map-canvas').is(':visible'), 'Map Canvas DOM loaded');

    assert.ok(frame$('#map-canvas .leaflet-container').is(':visible'), 'Leaflet loaded');

    assert.ok(frame$('#map-canvas').height()>200 && frame$('#map-canvas').width()>200, 'Map is at least 200px by 200px');
  });

  QUnit.test('Map points load', function(assert) {

    assert.ok(frame$('.map-adm-icon').length > 3, 'At least 3 points loaded on map');

  });


  done();
});

