var _ = require('underscore');
var Backbone = require('backbone');
var TreeNodeModel; // declare here to help with ref loop of collection and model
var TreeNodeCollection = Backbone.Collection.extend({  model:TreeNodeModel });

//TODO: propogation bug if mid level node is half filled and you 'deselect all' it won't propogate to children.

TreeNodeModel = Backbone.Model.extend({
  defaults:{
    selected: undefined,  // default is selected. change to string / trinary, for off, semi, and on
    expanded: true,
    visible: true,
    numSelected: 0,
    numPossible: 0,
    children: null,     // type TreeNodeCollection
    isSelectable: true  // is this node itself selectable (ie. should it have an 'unkown' child)
  },

  initialize:function(obj) {
    var self = this;
    var childrenCollection = new TreeNodeCollection();
    this.set('children', childrenCollection);

    //iterate over children
    if (Array.isArray(obj.children)) {
      _.each(obj.children, function(child) {
        var newChild = new TreeNodeModel(child);
        childrenCollection.add(newChild);
      });
    }

    // if we have children, then add self as a leaf node, 'unkown'
    if (!childrenCollection.isEmpty() &&  this.get('isSelectable')) {
      var unkownNode = new TreeNodeModel(self.toJSON());
      unkownNode.set('name', 'unkown: ' + this.get('name'));
      childrenCollection.add(unkownNode);
    }

    this._addListenersToChildren();

    this.on('change:selected', self._onSelectChange);

    this._updateCount();
  },


  serialize: function() {
    var tmp = [];
    var children = this.get('children');
    if (children.length > 0) {
      children.each(function(child) {
        tmp = tmp.concat(child.serialize());
      });

    } else {
      if (this.get('selected')) {
        tmp.push(this.id);
      }
    }

    return tmp;
  },

  deserialize: function(listOfSelected) {
    var children = this.get('children');
    if (children.length > 0) {
      children.each(function(child) {
        child.deserialize(listOfSelected);
      });
    }

    if (_(listOfSelected).indexOf(this.id) > -1) {
      this.set('selected', true, {propagation: true});
    } else if (children.length === 0) {
      this.set('selected', false, {propagation: true});
    }
  },


  _onSelectChange:function(model, argument, options) {
    var self = this;
    var children = this.get('children');

    if (this.get('selected')) {
      this.set('numSelected', this.get('numPossible'));
    } else {
      this.set('numSelected', 0);
    }

    if (!children.isEmpty()) {
      self._updateChildNodes(options.propagation);
    }

    if (options.propagation) {
      self.trigger('updateCount');
    }
  },


  _addListenersToChildren:function() {
    var self = this;
    var children = this.get('children');
    children.each(function(child) {
      child.on('change:visible', function() {
        // If no children are visible, then hide self.
        if (!children.findWhere({visible: true})) {
          self.set({visible: false});
        } else {
          self.set({visible: true});
        }
      });
      child.on('updateCount', function() {
        self._updateCount();
        self.trigger('updateCount');
      });
    });
  },

  _updateCount:function() {
    var children = this.get('children');
    var countTotal = {
      selected: 0,
      possible:0
    };

    if (!children.isEmpty()) {
      children.each(function(child) {
        countTotal.selected += child.get('numSelected');
        countTotal.possible += child.get('numPossible');
      });
    } else {
      countTotal = {
        selected: (this.get('selected') ? 1 : 0),
        possible: 1
      };
    }

    this.set('numSelected', countTotal.selected);
    this.set('numPossible', countTotal.possible);
  },

  _updateChildNodes:function(propagation) {
    var self = this;
    var children = this.get('children');

    if (!children.isEmpty()) {
      children.each(function(child) {
        child.set('selected', self.get('selected'), {propagation: propagation});
      });
    }
  },

  filterText: function(txt) {
    var children = this.get('children');

    if (!children.isEmpty()) {
      children.each(function(child) {
        child.filterText(txt);
      });
      // if the node itself matches, turn it on, even if no children are visible.
      if (this.get('name').toLowerCase().indexOf(txt) > -1) {
        this.set('visible', true);
      }
    } else {
      if (this.get('name').toLowerCase().indexOf(txt) > -1) {
        this.set('visible', true);
      } else {
        this.set('visible', false);
      }
    }
  }

});


module.exports = TreeNodeModel;
