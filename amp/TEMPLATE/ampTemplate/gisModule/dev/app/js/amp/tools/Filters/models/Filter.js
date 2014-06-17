define(
  [
    "underscore",
    "backbone"
  ],
  function (_, backbone ) {
    'use strict';


    // TODO: figure out best way fo doing this inheritence.
    // This is the parent model for filters, each one will implement things differently.
    // maybe each filter type has its own model and view...with methods like: 
    //   renderFullFilter
    //   createFilterJSON
    // on change they should emit a 'FILTER_CHANGED' event.
    // need to balance customization and avoiding repition...


    var Model = Backbone.Model.extend({      
        defaults: {
            title: "Filter Name",
            APIparam: "org",
            totalCount: 375,
        },
        
        initialize: function(){
            console.log("init a Filter Model");
        },
        

    });

    return Model;
  });
