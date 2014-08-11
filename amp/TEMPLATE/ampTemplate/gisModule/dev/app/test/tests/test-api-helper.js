var Qunit = require('qunitjs');
var APIHelper = require('../../js/libs/local/api-helper');


QUnit.module('API Helper');

//ToDo: ideally mock document.url then call getAPIBase instead..
QUnit.test( 'Getting the API base url', function( assert ) {

  assert.ok( APIHelper._findBase('http://amp-210-dev-tc7.ampdev.net/TEMPLATE/ampTemplate/gisModule/dist/index.html') === 'http://amp-210-dev-tc7.ampdev.net',
    'get base url for amp dev'  );

  assert.ok( APIHelper._findBase('http://localhost:3000/index.html') === '',
    'get base url for localhost:3000'  );

  assert.ok( APIHelper._findBase('http://localhost:3000/#%7B%22map%22%3A%7B%22center%22%3A%5B-0.7031073524364783%2C26.5869140625%5D%2C%22zoom%22%3A4%7D%2C%22basemap%22%3A%22Gray%22%7D') === '',
    'get base url for localhost:3000 with saved state in url'  );


});
