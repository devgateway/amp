(function configureAMD() {
  // dojo's `require` (see bdframework.org) will set global amd config
  // if the first parameter is an object.
  require({
    baseUrl: '/js/',  // all relative paths will becomputed from this
    packages: [
      {
        'name': 'dojo',
        'location': 'libs/vendor/dojo'
      },
      {
        'name': 'dojox',
        'location': 'libs/vendor/dojox'
      },
      {
        'name': 'dijit',
        'location': 'libs/vendor/dijit'
      },
      {
        'name': 'esri',
        'location': 'http://js.arcgis.com/3.8amd/js/esri'
      },
      {
        'name': 'jquery',
        'location': 'libs/vendor/jquery/dist',
        'main': 'jquery'
      },
      {
        'name': 'jqueryui',
        'location': 'libs/vendor/jquery-ui-amd/jquery-ui-1.10.0/jqueryui',
        'main': 'core'
      },
      {
        'name': 'bootstrap',
        'location': 'libs/vendor/bootstrap/dist/js',
        'main': 'bootstrap',
      },
      {
        'name': 'underscore',
        'location': 'libs/vendor/underscore-amd',
        'main': 'underscore'
      },
      {
        'name': 'backbone',
        'location': 'libs/vendor/backbone-amd',
        'main': 'backbone'
      },
      'amp',
    ],
    aliases: [
      ['domReady', 'dojo/domReady'],
      ['text', 'dojo/text']
    ]
  });
})();
