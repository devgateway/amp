console.log("HII");
var profile = {
    // This needs to be set so that the build system knows where to find the packages
    // relative to this file
    basePath: 'app/js/',
    
    action: 'release',

    // strip comments and combine css files.
    //cssOptimize: 'comments',

    // optimizer engine for layer files
    //layerOptimize: 'closure',

    // optimizer engine for all non-layer modules
    //optimize: 'closure',

    // force to acme for now since that's what ESRI uses
    selectorEngine: 'acme',

    // strip all console calls
    //stripConsole: 'all',

    // build everything into one file
    layers: {
        'dojo/dojo': {
            include: [
                'dojo/i18n', // this prevents a bunch of extra requests trying to get localization
                'app/run',
                'esri/dijit/Attribution',
                'dojox/gfx/path',
                'dojox/gfx/svg',
                'dojox/gfx/shape'
            ],
            includeLocales: ['en-us'],
            customBase: true, // don't include all of the globals in dojo/main
           // boot: true // include AMD loader
        }
    },
    // This helps prevents extra requests for the widget templates even though they
    // are interned into the layer file. Ref: https://gist.github.com/stdavis/9347356
    userConfig: {
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
        'location': 'libs/local/esriAMD'
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
    ]
    }
};
