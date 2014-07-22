var _ = require('underscore');
var Backbone = require('backbone');
var BaseFilterModel = require('../models/base-filter-model');


module.exports = BaseFilterModel.extend({

  initialize: function(options) {
    BaseFilterModel.prototype.initialize.apply(this);
    this.url = options.url;
    this.fetch();

  },

  // builds tree of views from returned data
  parse: function(data){

    var rootNodeObj = {
      id : -1,
      code : '-1',
      name : this.get('title'),
      sectors: data
    };

    this.tree = new TreeNodeView(rootNodeObj);  
  }

});



// TODO ...move into own file?
// TODO currently dependent on 'sector' instead of a generic term like 'children'
var TreeNodeView = Backbone.View.extend({
  initialize: function(obj){
    var self = this;
    //iterate over sectors
    if (Array.isArray(obj.sectors)) {
        this.sectors = [];
        _.each(obj.sectors, function(sector){
          self.sectors.push(new TreeNodeView(sector));
        });
    }

    // create model
    delete obj.sectors;   // remove sectors from model, already in view
    obj.selected = true; // default is selected.
    this.model = new TreeNodeModel(obj);
  }
});


var TreeNodeModel = Backbone.Model.extend({
    initialize: function() {
    }
});
