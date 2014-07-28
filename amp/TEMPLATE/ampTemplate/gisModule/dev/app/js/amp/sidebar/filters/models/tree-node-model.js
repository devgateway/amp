var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var $ = require('jquery');

var TreeNodeCollection = Backbone.Collection.extend({  model:TreeNodeModel });

var TreeNodeModel = Backbone.Model.extend({
  defaults:{
    selected: true,  // default is selected.
    expanded: false,
    numSelected: 0,
    numPossible: 0,
    children:null, //type TreeNodeCollection
  },

  initialize: function(obj) {
    var self = this;
    var childrenCollection =new TreeNodeCollection();
    this.set('children', childrenCollection);

    //iterate over children
    if (Array.isArray(obj.children)) {
      _.each(obj.children, function(child){
        var newChild = new TreeNodeModel(child);
        newChild.on('change:selected', function(){
          var countTotal =  self._updateCount()
          self.set('numSelected',countTotal.selected);
          self.set('numPossible',countTotal.possible);
          self.trigger('updateCount');
        });

        newChild.on('updateCount', function(){
          var countTotal =  self._updateCount()
          self.set('numSelected',countTotal.selected);
          self.set('numPossible',countTotal.possible);
        });

        childrenCollection.add(newChild);
      });
    }

    this.on('change:selected', function (model, argument, options) {
      self._updateChildNodes();
    });


    var countTotal =  this._updateCount()
    this.set('numSelected',countTotal.selected);
    this.set('numPossible',countTotal.possible);
  },

  _updateCount: function(){ 
    var children = this.get('children');

    if(!children.isEmpty()){
      var countTotal= {
        selected: 0, 
        possible:0
      };
      children.each(function(child){
        var count = child._updateCount();
        countTotal.selected += count.selected;
        countTotal.possible += count.possible;
      });
      return countTotal;
    } else{
      return {
        selected: (this.get('selected') ? 1: 0 ), 
        possible: 1
      };
    }   
  },


  _updateChildNodes: function () {
    var self = this;
    var children = this.get('children');

    if (!children.isEmpty()) {
      children.each(function (child) {
        child.set('selected', self.get('selected'), {propagation: false});
      });
    }
  },

});


module.exports = TreeNodeModel;
