var fs = require('fs'); 
var sinon = require('../mock-api/sinon-min.js');

var cluster = fs.readFileSync(__dirname + '/cluster.json', 'utf8');
var projectSites = fs.readFileSync(__dirname + '/project-sites.json', 'utf8');
var filters = fs.readFileSync(__dirname + '/filters.json', 'utf8');
var indicators = fs.readFileSync(__dirname + '/indicators.json', 'utf8');

var fakeServer = {
  server: null,

  init: function(){
    this.server = sinon.fakeServer.create();
    this.server.autoRespond = true;

    // Create paths for mock API
    this._addPath(cluster, '/rest/gis/cluster');
    this._addPath(filters, '/rest/gis/filters');
    this._addPath(projectSites, '/rest/gis/project-sites');
    this._addPath(indicators, '/rest/gis/indicator-layers');

  },
  
  _addPath: function(resp, path){
    this.server.respondWith('GET', 
                      path, 
                      [200, { 'Content-Type': 'application/json' }, resp]);  
  }
};

fakeServer.init();
