var _ = require('underscore');

var BaseFilterModel = require('../models/base-filter-model');
var TreeNodeModel = require('../tree/tree-node-model');


module.exports = BaseFilterModel.extend({

  initialize:function(options) {
    BaseFilterModel.prototype.initialize.apply(this, [options]);
  },

  // load tree if needed, else return what we already have..
  getTree: function() {
    var self = this;
    var loaded = this.get('_loaded');

    if (!loaded) {
      self.set('_loaded', this._createTree().then(function() {
        return self.get('tree');
      }));
    }

    return this.get('_loaded');
  },

  serialize: function() {
    var tree = this.get('tree');
    if (!tree) {
      // ?Throw error?
      return {}; //no tree, nothing to serialize.
    } else {
      var tmpAry = tree.serialize();
      tmpAry = _.without(tmpAry, -1);
      return tmpAry;
    }
  },

  deserialize: function(listOfSelected) {
    var self = this;
    this.getTree().then(function(tree) {
      //var tree = self.get('tree');
      if (!tree) {
        // ?Throw error?
        console.warn('no tree: ' + self.get('title'), listOfSelected);
        return false; //no tree, nothing to serialize.
      } else {
        tree.deserialize(listOfSelected);
      }
    });
  },

  _createTree:function() {
    //convert to fetch, build tree in parse...
    if (!this.url) {
      this.url = this.get('endpoint');
    }

    return this.fetch({
      type: this.get('method'),
      data:'{}'
    })
    .fail(function(jqXHR, textStatus, errorThrown) {
      console.error('failed to get filter ', jqXHR, textStatus, errorThrown);
    });
  },

  parse: function(data) {
    var self = this;

    //if it's an obj, jam it into an array first, helps solve inconsistancy in API format.
    if (!_.isArray(data)) {
      data = [data];
    }

    if (_.isArray(data) && data.length > 0) {
      var rootNodeObj = null;

      // Builds tree of views from returned data
      // If data is a single element, just make it the root..
      if (data.length === 1) {
        rootNodeObj = data[0];
      } else {
        rootNodeObj = {
          id: -1,
          code: '-1',
          name: self.get('title'),
          children: data,
          selected: undefined,
          expanded: false,
          isSelectable: false
        };
      }

      var treeModel = new TreeNodeModel(rootNodeObj);
      self.set('tree', treeModel);
    } else {
      console.error(' _createTree ' + self.get('title') + 'got bad/empty data', data);
    }

    return;
  }

});

