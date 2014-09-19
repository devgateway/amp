var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');

var BaseFilterView = require('../views/base-filter-view');
var GenericFilterView = require('../views/generic-filter-view');
var TreeNodeModel = require('../tree/tree-node-model');
var TreeNodeView = require('../tree/tree-node-view');

require('../../../../libs/local/slider/jquery.nouislider.min.js');

var Template = fs.readFileSync(__dirname + '/../templates/generic-filter-template.html', 'utf8');


// This view assumes a NESTED tree structure.
// This means the first api call returns a list of endpoints, and then we iterate over
// each endpoint creating a tree for it.
module.exports = GenericFilterView.extend({

  className: GenericFilterView.prototype.className,
  template: _.template(Template),
  _loaded:null,

  initialize:function(options) {
    var self = this;
    //intentinoally not GenericFilterView.prototype, we want to do it our way
    BaseFilterView.prototype.initialize.apply(this, [options]);

    this.model = options.model;

    this._loaded = this._createTree(this.model.get('url')).then(function() {
      self._updateCountInMenu();
      self.model.get('tree').on('change:numSelected', function() {
        self._updateCountInMenu();
      });
    });
  },

  // TODO: move into model for generic-nested-filter-model...
  // 1. get all children
  // 2. create root JSON, with each child endpoint as 'children'.
  // 3. when all done create tree
  _createTree:function(url) {
    var self = this;
    var deferred = $.Deferred();
    var deferreds = [];

    // builds tree of views from returned data
    var rootNodeObj = {
      id: -1,
      code: '-1',
      name: self.model.get('title'),
      children: [],
      selected: true,
      expanded: false,
      isSelectable: false
    };

    //get available endpoint children
    this.childEndpoints = new Backbone.Collection();
    this.childEndpoints.url = url;
    this.childEndpoints.fetch().done(function() {
      self.childEndpoints.each(function(child) {
        var tmpNode = {
          id: child.get('id'),
          code: '-1',
          name: child.get('name'),
          selected: true,
          expanded: false,
          isSelectable: false
        };

        child.url = url + '/' + child.get('id'); //TODO: something smarter...more reliable
        deferreds.push(
          child.fetch().done(function(data) {
            if (data.id) { //not an array. hack temp solution while Julian fixes API so all obj or all array
              data = [data];
            }
            tmpNode.children = data;
            rootNodeObj.children.push(tmpNode);
          }).fail(function() {
            console.error('Failed child node of API', child.get('id'));
          })
        );
      });

      // when all are done create tree.
      $.when.apply($, deferreds).then(function() {
        self._buildTreeFromRoot(rootNodeObj);
        deferred.resolve();
      }).fail(function() {
        //this happens if just one child fails...no point in stopping whole tree.
        self._buildTreeFromRoot(rootNodeObj);
        deferred.resolve();
      });
    });

    return deferred;
  },

  _buildTreeFromRoot:function(rootNodeObj) {
    this.model.set('tree', new TreeNodeModel(rootNodeObj));
    this.treeView = new TreeNodeView();
  }
});
