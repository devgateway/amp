require.config({
    paths: {
        backbone: 'lib/backbone.marionette/backbone',
        underscore: 'lib/backbone.marionette/underscore',
        jquery: 'lib/jquery_1.10.2',
        jqueryui: 'lib/jquery-ui.min_1.11.0',
        marionette: 'lib/backbone.marionette/backbone.marionette',
        text: 'lib/text_2.0.12',
        localStorage: 'lib/Backbone.localStorage-master/backbone.localStorage-min',
        documentModel: 'lib/backbone-documentmodel-master/backbone-documentmodel',
        documentCollection: 'lib/backbone-documentmodel-master/backbone-documentmodel'
    },
    shim: {
        jquery: {
            exports: 'jQuery'
        },
        underscore: {
            exports: '_'
        },
        backbone: {
            deps: [ 'jquery', 'underscore' ],
            exports: 'Backbone'
        },
        marionette: {
            deps: [ 'jquery', 'underscore', 'backbone' ],
            exports: 'Marionette'
        },
        jqueryui: {
            deps: [ 'jquery' ],
            exports: 'jQueryUI'
        },
        documentModel: {
            deps: ['backbone'],
            exports: 'documentModel'
        }
    }
});

require([ 'app' ]);