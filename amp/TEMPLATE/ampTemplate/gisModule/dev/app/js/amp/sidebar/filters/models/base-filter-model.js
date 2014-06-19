define(
  [
    'underscore',
    'backbone'
  ],
  function (_, backbone ) {
    'use strict';

    // Parent model for fitlers.
    var Model = Backbone.Model.extend({      
        defaults: {
            title: 'Filter Name',
            totalCount: 375,
            activeCount: 375
        },
        
        initialize: function(){

        },
        
    });

    return Model;
  });
