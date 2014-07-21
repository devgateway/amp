
require('../../mock-api/fakeServer.js');


var BasemapCollection = require('../../js/amp/map/collections/basemap-collection');



QUnit.test( 'Basemap Collection test', function( assert ) {
  var basemapCollection =  new BasemapCollection();

  assert.ok( basemapCollection.length > 5, 'Has atleast 6 maps' );


  var defaultMap = basemapCollection.getBasemap();
  assert.ok( defaultMap, 'Loaded defaultMap' );
  assert.ok( defaultMap.get('esriId') === 'Gray'
        &&  defaultMap.get('source') === 'esri'
        &&  defaultMap.get('selected') == true, 'defaultMap is selected and gray' );


  var grayMap = basemapCollection.getBasemap('Gray');
  assert.ok( grayMap, 'Loaded Gray map' );
  assert.ok( grayMap.get('esriId') === 'Gray' &&  grayMap.get('source') === 'esri', 'Gray map loaded correct values' );


  var emptyBasemap = basemapCollection.getBasemap('Empty Basemap');
  assert.ok( emptyBasemap, 'Loaded  Empty Basemap' );
  assert.ok( emptyBasemap.get('id') == 'Empty Basemap'
          &&  emptyBasemap.get('source') == null
          &&  !emptyBasemap.get('selected'), 'Empty Basemap has correct values' );


  var badBasemap = basemapCollection.getBasemap('fake basemap name');
  assert.ok( !badBasemap , 'bad Basemap request returns something falsey' );


  // test select
  basemapCollection.selectBasemap('Empty Basemap');
  var newDefaultMap = basemapCollection.getBasemap();
  assert.ok( newDefaultMap.get('id') === 'Empty Basemap'
        &&  newDefaultMap.get('selected') == true, 'select empty basemap' );


});
