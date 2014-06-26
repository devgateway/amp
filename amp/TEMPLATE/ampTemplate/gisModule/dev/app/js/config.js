(function configureAMD() {
  // dojo's `require` (see bdframework.org) will set global amd config
  // if the first parameter is an object.
  require({
    baseUrl: '/js/',  // all relative paths will becomputed from this
    packages: [
      // 'dojo',  // this form seems to be a dojo thing not from bdframework/amd
      // 'dijit',
      // 'dojox',
      // 'esri',
      {
        'name': 'esri',
        'location': 'http://js.arcgis.com/3.8amd/js/esri'
      },
      {
        'name': 'jquery',
        'location': 'libs/amd',
        'main': 'jquery'
      },
      {
        'name': 'jqueryui',
        'location': 'libs/amd/jqueryui',
        'main': 'core'
      },
      {
        'name': 'bootstrap',
        'location': 'libs/bootstrap/js',
        'main': 'bootstrap',
      },
      {
        'name': 'underscore',
        'location': 'libs/amd',
        'main': 'underscore'
      },
      {
        'name': 'backbone',
        'location': 'libs/amd',
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
