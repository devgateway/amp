// dojo's `require` (see bdframework.org) will set global amd config
// if the first parameter is an object.
require({
  baseUrl: '/js/',  // all relative paths will becomputed from this
  packages: [
    {
      name: 'dgrid',
      location: 'libs/jso/dgrid'
    },
    {
      name: 'dijit',
      location: 'libs/jso/dijit'
    },
    {
      name: 'dojo',
      location: 'libs/jso/dojo'
    },
    {
      name: 'dojox',
      location: 'libs/jso/dojox'
    },
    {
      name: 'esri',
      location: 'libs/jso/esri'
    },
    {
      name: 'put-selector',
      location: 'libs/jso/put-selector'
    },
    {
      name: 'xstyle',
      location: 'libs/jso/xstyle'
    },
    {
      name: 'jquery',
      location: 'libs/vendor/jquery/dist',
      main: 'jquery'
    },
    {
      name: 'jqueryui',
      location: 'libs/vendor/jquery-ui-amd/jquery-ui-1.10.0/jqueryui',
      main: 'core'
    },
    {
      name: 'bootstrap',
      location: 'libs/vendor/bootstrap/dist/js',
      main: 'bootstrap',
    },
    {
      name: 'underscore',
      location: 'libs/vendor/underscore-amd',
      main: 'underscore'
    },
    {
      name: 'backbone',
      location: 'libs/vendor/backbone-amd',
      main: 'backbone'
    },
    'amp',
  ],
  aliases: [
    ['domReady', 'dojo/domReady'],
    ['text', 'dojo/text']
  ]
});
