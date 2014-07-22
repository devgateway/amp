var fs = require('fs');
var sinon = require('../mock-api/sinon-min.js');

var cluster = fs.readFileSync(__dirname + '/data/cluster.json', 'utf8');
var clusterADM2 = fs.readFileSync(__dirname + '/data/clusterADM2.json', 'utf8');
var projectSites = fs.readFileSync(__dirname + '/data/drc-points-2000.json', 'utf8');//  drc-points-2000.json
var indicators = fs.readFileSync(__dirname + '/data/indicators.json', 'utf8');
var drcFood = fs.readFileSync(__dirname + '/data/drc-food.json', 'utf8');

// filters
var filters = fs.readFileSync(__dirname + '/data/filters.json', 'utf8');
var sectors = fs.readFileSync(__dirname + '/data/filters/sectors.json', 'utf8');

// adm boundaries:
var adminLevels = fs.readFileSync(__dirname + '/data/adminLevels.json', 'utf8');
var adm0 = fs.readFileSync(__dirname + '/data/boundaries/adm0.json', 'utf8');
var adm1 = fs.readFileSync(__dirname + '/data/boundaries/adm1.json', 'utf8');
var adm2 = fs.readFileSync(__dirname + '/data/boundaries/adm2.json', 'utf8');

var fakeServer = {
  server: null,

  init: function(){
    this.server = sinon.fakeServer.create();
    this.server.autoRespond = true;
    this.server.autoRespondAfter = 6; // delay in ms, good for latency tests.

    // Create paths for mock API
    this._addClusterPath();
    this._addPath(projectSites, '/rest/gis/project-sites');
    this._addPath(indicators, '/rest/gis/indicator-layers');
    this._addPath(drcFood, '/rest/gis/drc-food');

    // filters
    this._addPath(filters, '/rest/gis/filters');
    this._addPath(sectors, '/rest/gis/filters/sectors');

    // admins and boundaries
    this._addPath(adminLevels, '/rest/gis/adminLevels');
    this._addPath(adm0, '/rest/gis/adminBoundary/0');
    this._addPath(adm1, '/rest/gis/adminBoundary/1');
    this._addPath(adm2, '/rest/gis/adminBoundary/2');


  },

  // reg-ex can be used to pull out params and send dynamic response.
  _addClusterPath: function(){
    this.server.respondWith(/\/rest\/gis\/cluster(\S+)/,
      function (xhr, param) {
        if(param.indexOf('adminLevel=2') > -1){
          xhr.respond(200, { 'Content-Type': 'application/json' }, clusterADM2);
        } else{
          xhr.respond(200, { 'Content-Type': 'application/json' }, cluster);
        }
      });

  },

  _addPath: function(resp, path){
    this.server.respondWith('GET',
                      path,
                      [200, { 'Content-Type': 'application/json' }, resp]);

    // can do regex to get params:
    // server.respondWith(/\/todo-items\/(\d+)/, function (xhr, id) {  xhr.respond(200, { ?Content-Type?: ?application/json? }, ?[{ ?id?: ? + id + ? }]?); });
  }
};

fakeServer.init();
