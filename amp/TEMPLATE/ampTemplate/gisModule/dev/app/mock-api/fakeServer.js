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
    'GET /rest/filters/organizations/1':     fs.readFileSync(__dirname + '/data/filters/orgs.json', 'utf8'),
    'GET /rest/filters/organizations':     fs.readFileSync(__dirname + '/data/filters/orgRoles.json', 'utf8'),
    'GET /rest/filters/programs':     fs.readFileSync(__dirname + '/data/filters/programs.json', 'utf8'),
    'GET /rest/filters/programs/1':     fs.readFileSync(__dirname + '/data/filters/programs-1.json', 'utf8'),
    'GET /rest/filters/dates':     fs.readFileSync(__dirname + '/data/filters/years.json', 'utf8'),
    'GET /rest/filters/activityStatus':  fs.readFileSync(__dirname + '/data/filters/activity-status.json', 'utf8'),
    'GET /rest/filters/sectors':  fs.readFileSync(__dirname + '/data/filters/sectors.json', 'utf8'),
    'GET /rest/filters/sectors/1':  fs.readFileSync(__dirname + '/data/filters/sectors-1.json', 'utf8'),
    'GET /rest/filters/sectors/2':  fs.readFileSync(__dirname + '/data/filters/sectors-2.json', 'utf8'),

    // Boundaries
    'GET /rest/gis/boundaries':      fs.readFileSync(__dirname + '/data/boundaries/list.json', 'utf8'),
    'GET /rest/gis/boundaries/adm-0': fs.readFileSync(__dirname + '/data/boundaries/adm-0.json', 'utf8'),
    'GET /rest/gis/boundaries/adm-1': fs.readFileSync(__dirname + '/data/boundaries/adm-1.json', 'utf8'),
    'GET /rest/gis/boundaries/adm-2': fs.readFileSync(__dirname + '/data/boundaries/adm-2.json', 'utf8'),

    // Clusters

    // Activities
    'GET /rest/gis/activities': fs.readFileSync(__dirname + '/data/activities/list.json', 'utf8'),
    'GET /rest/gis/activities?id=1120098711456': fs.readFileSync(__dirname + '/data/activities/1120098711456.json', 'utf8'),
    'GET /rest/gis/activities?id=1120098711885': fs.readFileSync(__dirname + '/data/activities/1120098711885.json', 'utf8'),
    'GET /rest/gis/activities?id=1120098711430': fs.readFileSync(__dirname + '/data/activities/1120098711430.json', 'utf8'),
    'GET /rest/gis/activities?id=P104497': fs.readFileSync(__dirname + '/data/activities/P104497.json', 'utf8'),
    'GET /rest/gis/activities?id=P088751': fs.readFileSync(__dirname + '/data/activities/P088751.json', 'utf8'),
    'GET /rest/gis/activities?id=P069258': fs.readFileSync(__dirname + '/data/activities/P069258.json', 'utf8'),
    'GET /rest/gis/activities?id=1120098730335': fs.readFileSync(__dirname + '/data/activities/1120098730335.json', 'utf8'),
    'GET /rest/gis/activities?id=1120098735424': fs.readFileSync(__dirname + '/data/activities/1120098735424.json', 'utf8'),
    'GET /rest/gis/activities?id=P105654': fs.readFileSync(__dirname + '/data/activities/P105654.json', 'utf8'),
    'GET /rest/gis/activities?id=1120098710366': fs.readFileSync(__dirname + '/data/activities/1120098710366.json', 'utf8'),
    'GET /rest/gis/activities?id=P088619': fs.readFileSync(__dirname + '/data/activities/P088619.json', 'utf8'),
    'GET /rest/gis/activities?id=P096414': fs.readFileSync(__dirname + '/data/activities/P096414.json', 'utf8'),
    'GET /rest/gis/activities?id=P114782': fs.readFileSync(__dirname + '/data/activities/P114782.json', 'utf8'),
    'GET /rest/gis/activities?id=P120709': fs.readFileSync(__dirname + '/data/activities/P120709.json', 'utf8'),
    'GET /rest/gis/activities?id=1120098711434': fs.readFileSync(__dirname + '/data/activities/1120098711434.json', 'utf8'),
    'GET /rest/gis/activities?id=P097201': fs.readFileSync(__dirname + '/data/activities/P097201.json', 'utf8'),
    'GET /rest/gis/activities?id=P122398': fs.readFileSync(__dirname + '/data/activities/P122398.json', 'utf8'),
    'GET /rest/gis/activities?id=11200958532178': fs.readFileSync(__dirname + '/data/activities/11200958532178.json', 'utf8'),
    'GET /rest/gis/activities?id=P100620': fs.readFileSync(__dirname + '/data/activities/P100620.json', 'utf8'),
    'GET /rest/gis/activities?id=P101745': fs.readFileSync(__dirname + '/data/activities/P101745.json', 'utf8'),
    'GET /rest/gis/activities?id=P092537': fs.readFileSync(__dirname + '/data/activities/P092537.json', 'utf8'),
    'GET /rest/gis/activities?id=P122251': fs.readFileSync(__dirname + '/data/activities/P122251.json', 'utf8'),
    'GET /rest/gis/activities?id=1120098711433': fs.readFileSync(__dirname + '/data/activities/1120098711433.json', 'utf8'),
    'GET /rest/gis/activities?id=P086874': fs.readFileSync(__dirname + '/data/activities/P086874.json', 'utf8'),
    'GET /rest/gis/activities?id=P083813': fs.readFileSync(__dirname + '/data/activities/P083813.json', 'utf8'),
    'GET /rest/gis/activities?id=P115318': fs.readFileSync(__dirname + '/data/activities/P115318.json', 'utf8'),
    'GET /rest/gis/activities?id=1120098711438': fs.readFileSync(__dirname + '/data/activities/1120098711438.json', 'utf8'),
    'GET /rest/gis/activities?id=11200988730': fs.readFileSync(__dirname + '/data/activities/11200988730.json', 'utf8'),
    'GET /rest/gis/activities?id=P091092': fs.readFileSync(__dirname + '/data/activities/P091092.json', 'utf8'),
    'GET /rest/gis/activities?id=1120098711443': fs.readFileSync(__dirname + '/data/activities/1120098711443.json', 'utf8'),
    'GET /rest/gis/activities?id=P090872': fs.readFileSync(__dirname + '/data/activities/P090872.json', 'utf8'),
    'GET /rest/gis/activities?id=1120098711426': fs.readFileSync(__dirname + '/data/activities/1120098711426.json', 'utf8'),
    'GET /rest/gis/activities?id=1120098710054': fs.readFileSync(__dirname + '/data/activities/1120098710054.json', 'utf8'),
    'GET /rest/gis/activities?id=P117382': fs.readFileSync(__dirname + '/data/activities/P117382.json', 'utf8'),
    'GET /rest/gis/activities?id=1120098711462': fs.readFileSync(__dirname + '/data/activities/1120098711462.json', 'utf8'),
    'GET /rest/gis/activities?id=P120898': fs.readFileSync(__dirname + '/data/activities/P120898.json', 'utf8'),
    'GET /rest/gis/activities?id=1120098711440': fs.readFileSync(__dirname + '/data/activities/1120098711440.json', 'utf8'),


    // Project Sites
    //'POST /rest/gis/structures': fs.readFileSync(__dirname + '/data/structures/drc-points-2000.json', 'utf8'),
    'POST /rest/gis/structures': fs.readFileSync(__dirname + '/data/structures/wb-data.json', 'utf8'),
    //'POST /rest/gis/structures': fs.readFileSync(__dirname + '/data/structures/random.json', 'utf8'),

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
     'POST /rest/translations/label-translations': fs.readFileSync(__dirname + '/../js/amp/services/initial-translation-request.json', 'utf8'),
     'POST /rest/translations/label-translations/en': fs.readFileSync(__dirname + '/../js/amp/services/initial-translation-request.json', 'utf8'),
     'POST /rest/translations/label-translations/fr': fs.readFileSync(__dirname + '/data/label-translations/sample-fr.json', 'utf8'),

  },

  _addClusterPath: function() {
    this.server.respondWith('POST', '/rest/gis/cluster', function (xhr, param) {
      if (_.isNull(xhr.requestBody)) {
        console.warn('no request body posted to cluster');
      }
      console.log('cluster ', xhr.requestBody);
      if(xhr.requestBody.indexOf('Country') > -1){
        xhr.respond(200, { 'Content-Type': 'application/json' },
          fs.readFileSync(__dirname + '/data/clusters/clusterADM0.json', 'utf8'));
      } else if(xhr.requestBody.indexOf('Zone') > -1){
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
