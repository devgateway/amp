// configs
APP_ROOT = '/js';
API_ROOT = '';


/*
*  ArcGIS API for JavaScript uses Dojo , the following json variable configures dojo loader
*  http://dojotoolkit.org/documentation/tutorials/1.9/dojo_config/
*
*  CacheBust should be set to false for production environments
* */
dojoConfig = {
  debug: true,
  async: true,
  parseOnLoad: true,
  aliases: [
    ["text", "dojo/text"]
  ],
  packages: [
    {
      name: "backbone",
      location: APP_ROOT + '/libs/amd',
      main: 'backbone'
    },
    {
      name: "underscore",
      location: APP_ROOT + '/libs/amd',
      main: 'underscore'
    },
    {
      name: "jxdr",
      location: APP_ROOT + '/libs/amd',
      main: 'jquery.xdomainrequest.min'
    },
    {
      name: "bootstrap",
      location: APP_ROOT + '/libs/bootstrap/js',
      main: 'bootstrap.min'
    },
    {
      name: "modelBinder",
      location: APP_ROOT + '/libs/modelbinder',
      main: 'Backbone.ModelBinder'
    },
    {
      name: 'jquery',
      location: "http://code.jquery.com",
      main: 'jquery-1.11.0.min'
    },
    {
      name: 'jqueryui',
      location: APP_ROOT +"/libs/amd/jqueryui",
      main: 'core'
    },
    {
      name: 'jQueryToolbar',
      location: APP_ROOT + '/libs/toolbar',
      main: 'jquery.toolbar.min'

    },
    {
      name: 'gis',
      location: APP_ROOT + '/amp/gis/views',
      main: 'gis-view'
    }
  ],
  waitSeconds: 50,
  modulePaths: {},
  deps: ['underscore', 'backbone', 'jquery'],

  callback: function (parser) {
  },

  requestProvider: "dojo/request/registry",

  map: {
    "*": {
      "text": "dojo/text"
    }
  },

  cacheBust: true
};

console.info("Dojo config loaded.");
