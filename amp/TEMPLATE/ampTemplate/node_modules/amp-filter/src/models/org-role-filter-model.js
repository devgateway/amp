var $ = require('jquery');

var GenericFilterModel = require('../models/generic-filter-model');
var TreeNodeModel = require('../tree/tree-node-model');


module.exports = GenericFilterModel.extend({


  initialize:function(options) {
    GenericFilterModel.prototype.initialize.apply(this, [options]);
    this._createTree(options.data);
  },

  //org doesn't have async part, but still use deferred, so behaves same as generic filter model
  getTree: function() {
    var self = this;
    var loaded = $.Deferred();
    loaded.resolve(self.get('tree'));

    return loaded;
  },

  _createTree:function(data) {
    var self = this;
    var rootNodeObj = {};
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
        isSelectable: false,
        createUnkowns: false
      };
    }

    var treeModel = new TreeNodeModel(rootNodeObj);
    this.set('tree', treeModel);
  }

});

