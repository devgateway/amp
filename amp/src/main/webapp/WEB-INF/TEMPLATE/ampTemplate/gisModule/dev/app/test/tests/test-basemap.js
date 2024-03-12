var assert = require("assert"); // node.js core module
var BasemapCollection = require('../../js/amp/map/collections/basemap-collection');

var mockOptions = {
  app: { state: { register: function() {}}}
};


describe('Basemap Collection', function() {
  it('should have more than 0 basemaps', function(){
    var basemapCollection =  new BasemapCollection(null, mockOptions);
    assert.ok( basemapCollection.length > 1, 'Has some basemaps' );
  });

  it('should have gray and empty basemaps', function(){
    var basemapCollection =  new BasemapCollection(null, mockOptions);
    var grayMap = basemapCollection.getBasemap('Gray');
    assert.ok( grayMap, 'Loaded Gray map' );
    assert.ok( grayMap.get('esriId') === 'Gray' &&  grayMap.get('source') === 'esri', 'Gray map loaded correct values' );

    var emptyBasemap = basemapCollection.getBasemap('Empty Basemap');
    assert.ok( emptyBasemap, 'Loaded  Empty Basemap' );
    assert.ok( emptyBasemap.get('id') === 'Empty Basemap' &&
               emptyBasemap.get('source') === null &&
               !emptyBasemap.get('selected'), 'Empty Basemap has correct values' );


    var badBasemap = basemapCollection.getBasemap('fake basemap name');
    assert.ok( !badBasemap , 'bad Basemap request returns something falsey' );
  });


  it('should support basemap selection', function(){
    var basemapCollection =  new BasemapCollection(null, mockOptions);
    // test select
    basemapCollection.selectBasemap('Empty Basemap');
    var newDefaultMap = basemapCollection.getBasemap();
    assert.ok( newDefaultMap.get('id') === 'Empty Basemap' &&
               newDefaultMap.get('selected') === true, 'select empty basemap' );

  });
});
