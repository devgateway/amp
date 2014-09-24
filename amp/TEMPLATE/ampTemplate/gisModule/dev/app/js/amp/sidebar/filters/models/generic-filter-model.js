var _ = require('underscore');
var $ = require('jquery');

var BaseFilterModel = require('../models/base-filter-model');
var TreeNodeModel = require('../tree/tree-node-model');

//TODO: serialize-deserialize functions by traversing the tree.

module.exports = BaseFilterModel.extend({

  initialize:function(options) {
    BaseFilterModel.prototype.initialize.apply(this, [options]);
  },

  // load tree if needed, else return what we already have..
  getTree: function() {
    var self = this;
    var deferred = $.Deferred();
    if (this.get('tree')) {
      deferred.resolve(this.get('tree'));
    } else {
      this._createTree().then(function() {
        self.get('_loaded').resolve();
        deferred.resolve(self.get('tree'));
      });
    }

    return deferred;
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
    var tree = this.get('tree');
    if (!tree) {
      // ?Throw error?
      return false; //no tree, nothing to serialize.
    } else {
      tree.deserialize(listOfSelected);
    }
  },

  _createTree:function() {
    var self = this;
    var url = this.get('url'); //intentionall not fetch, since we want to build a tree as an atribute...

    return $.get(url).then(function(data) {
      //tmp hack solution, if it's an obj, jam it into an array first (needed for /filters/programs)
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
            selected: true,
            expanded: false,
            isSelectable: false
          };
        }

        var treeModel = new TreeNodeModel(rootNodeObj);
        self.set('tree', treeModel);
      } else {
        console.error(' _createTree got bad data', data);
      }

    })
    .fail(function(jqXHR, textStatus, errorThrown) {
      console.error('failed to get filter ', jqXHR, textStatus, errorThrown);
    });
  }

});

