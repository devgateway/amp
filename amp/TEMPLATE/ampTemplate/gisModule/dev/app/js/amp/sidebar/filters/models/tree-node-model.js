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
    var childrenCollection = new TreeNodeCollection();
    this.set('children', childrenCollection);

    //iterate over children
    if (Array.isArray(obj.children)) {
      _.each(obj.children, function(child){
        var newChild = new TreeNodeModel(child);
        childrenCollection.add(newChild);
      });
    }

    // if we have children, then add self as a 'unkown' child
    if(!childrenCollection.isEmpty()){
      var unkownNode = new TreeNodeModel(self.toJSON());
      unkownNode.set('name', 'unkown');
      childrenCollection.add(unkownNode);
    }

    this._addListenersToChildren();

    this.on('change:selected', function (model, argument, options) {
      if (!childrenCollection.isEmpty()) {
        self._updateChildNodes();
      } else {
        self._updateCount();
        self.trigger('updateCount'); 
      }
    });

    var countTotal =  this._updateCount();
  },

  _addListenersToChildren: function(){
    var self = this;
    var children = this.get('children');
    children.each(function(child){
      child.on('updateCount', function(){
        var countTotal =  self._updateCount();
        self.trigger('updateCount'); 
      });

    });
  },

  _updateCount: function(){ 
    var children = this.get('children');
    var countTotal= {
      selected: 0, 
      possible:0
    };
    if(!children.isEmpty()){     
      children.each(function(child){
        var count = child._updateCount();
        countTotal.selected += count.selected;
        countTotal.possible += count.possible;
      });
    } else{
      countTotal = {
        selected: (this.get('selected') ? 1: 0 ), 
        possible: 1
      };
    }

    this.set('numSelected',countTotal.selected);
    this.set('numPossible',countTotal.possible);
    return  countTotal;
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
