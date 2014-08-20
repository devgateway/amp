var fs = require('fs');
var _ = require('underscore');
var sinon = require('../mock-api/sinon-min.js');


var FakeServer = function FakeServer() {
  this.server = null;
  this.init.apply(this);
};

_.extend(FakeServer.prototype, {

  init: function() {
    this.server = sinon.fakeServer.create();
    this.server.autoRespond = true;
    this.server.autoRespondAfter = 6; // delay in ms, good for latency tests.
    window.XMLHttpRequest.useFilters = true;  // let us pass through some requests
    window.XMLHttpRequest.addFilter(this.filterRequests);
    this.routeMockFiles();
    this._addClusterPath();
  },

  filterRequests: function(method, url) {
    var PASS = true,
        FAKE = false,
        fakeIf = '/rest/';
    return (url.substring(0, fakeIf.length) === fakeIf) ? FAKE : PASS;
  },

  routeMockFiles: function() {
    _.each(this.apiMockFileMap, function(resp, method_path) {
      var methodPathList = method_path.split(' ');
      this.server.respondWith(methodPathList[0], methodPathList[1],
        function (xhr, param) {
          console.info("route fired", methodPathList[1]);
          xhr.respond(200, { 'Content-Type': 'application/json' },resp);
        });
    }, this);
  },

  apiMockFileMap: {
    // API: https://docs.google.com/a/developmentgateway.org/document/d/13F0YHVIHw3DSBV9P_WRw0ltmqIgMPuWa7ZaCGzbAkDY/
    // this object maps '/rest/api/path': 'json/mock/file/path'

    // Filters
    'GET /rest/filters':         fs.readFileSync(__dirname + '/data/filters/list.json', 'utf8'),
    'GET /rest/filters/orgs':     fs.readFileSync(__dirname + '/data/filters/orgs.json', 'utf8'),
    'GET /rest/filters/sectors':  fs.readFileSync(__dirname + '/data/filters/sectors.json', 'utf8'),
    'GET /rest/filters/sectors/Primary':  fs.readFileSync(__dirname + '/data/filters/sectors-primary.json', 'utf8'),
    'GET /rest/filters/sectors/Secondary':  fs.readFileSync(__dirname + '/data/filters/sectors-secondary.json', 'utf8'),

    // Boundaries
    'GET /rest/gis/boundaries':      fs.readFileSync(__dirname + '/data/boundaries/list.json', 'utf8'),
    'GET /rest/gis/boundaries/adm-0': fs.readFileSync(__dirname + '/data/boundaries/adm-0.json', 'utf8'),
    'GET /rest/gis/boundaries/adm-1': fs.readFileSync(__dirname + '/data/boundaries/adm-1.json', 'utf8'),
    'GET /rest/gis/boundaries/adm-2': fs.readFileSync(__dirname + '/data/boundaries/adm-2.json', 'utf8'),

    // Clusters... can we just join with ADM client-side?

    // Activities
    // 'GET /rest/...': fs.readFileSync(__dirname + '/data/....json', 'utf8'),

    // Project Sites
    //'GET /rest/gis/structures': fs.readFileSync(__dirname + '/data/structures/drc-points-2000.json', 'utf8'),
    'POST /rest/gis/structures': fs.readFileSync(__dirname + '/data/structures/wb-data.json', 'utf8'),
    //'GET /rest/gis/structures': fs.readFileSync(__dirname + '/data/structures/random.json', 'utf8'),

    // Indicator Layers
    'GET /rest/gis/indicator-layers':         fs.readFileSync(__dirname + '/data/indicator-layers/list.json', 'utf8'),
    'GET /rest/gis/indicator-layers/drc-food':fs.readFileSync(__dirname + '/data/indicator-layers/drc-food.json', 'utf8'),
    'GET /rest/gis/indicator-layers/drc-geojson':fs.readFileSync(__dirname + '/data/indicator-layers/drc-geojson.json', 'utf8'),

    // Config
    // 'GET /rest/gis/config': fs.readFileSync(__dirname + '/data/config/....json', 'utf8'),

    // Saved Map States
    'GET /rest/gis/states':  fs.readFileSync(__dirname + '/data/saved-maps/list.json', 'utf8'),
    // 'GET /rest/gis/saved-maps/0': fs.readFileSync(__dirname + '/data/saved-maps/0.json', 'utf8'),
    // 'GET /rest/gis/saved-maps/1': fs.readFileSync(__dirname + '/data/saved-maps/1.json', 'utf8'),
    // 'POST /rest/gis/saved-maps/ ...

    // Translations
     'POST /rest/translations/label-translations': fs.readFileSync(__dirname + '/data/label-translations/sample-en.json', 'utf8'),
     'POST /rest/translations/label-translations/en': fs.readFileSync(__dirname + '/data/label-translations/sample-en.json', 'utf8'),
     'POST /rest/translations/label-translations/fr': fs.readFileSync(__dirname + '/data/label-translations/sample-fr.json', 'utf8'),

  },

  _addClusterPath: function() {
    this.server.respondWith('POST', '/rest/gis/cluster', function (xhr, param) {
      if (_.isNull(xhr.requestBody)) {
        console.warn('no request body posted to cluster');
      }
      console.log('cluster ', xhr.requestBody);
      if(xhr.requestBody.indexOf('Region') > -1){
        xhr.respond(200, { 'Content-Type': 'application/json' },
          fs.readFileSync(__dirname + '/data/clusters/clusterADM2.json', 'utf8'));
      } else{
        xhr.respond(200, { 'Content-Type': 'application/json' },
          fs.readFileSync(__dirname + '/data/clusters/cluster.json', 'utf8'));
      }
    });
  }

});


window.server = new FakeServer();
