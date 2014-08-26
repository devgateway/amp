var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');

var BaseFilterView = require('../views/base-filter-view');
var GenericFilterView = require('../views/generic-filter-view');
var GenericFilterModel = require('../models/generic-filter-model');
var TreeNodeModel= require('../models/tree-node-model');
var TreeNodeView = require('../views/tree-node-view');

require('../../../../libs/local/slider/jquery.nouislider.min.js');

var Template = fs.readFileSync(__dirname + '/../templates/generic-filter-template.html', 'utf8');


// This view assumes a NESTED tree structure.
// This means the first api call returns a list of endpoints, and then we iterate over
// each endpoint creating a tree for it.
module.exports = GenericFilterView.extend({

  className: GenericFilterView.prototype.className,
  template: _.template(Template),

  initialize: function(options) {
    var self = this;
    //intentinoally not GenericFilterView.prototype, we want to do it our way
    BaseFilterView.prototype.initialize.apply(this);

    this.model = new GenericFilterModel(options.modelValues);

    this._createTree(options.url).then(function(){
      self._updateCountInMenu();
      self.treeModel.on('change:numSelected', function(){
        self._updateCountInMenu();
      });
    });
  },

  // 1. get all children
  // 2. create root JSON, with each child endpoint as 'children'.
  // 3. when all done create tree
  _createTree: function(url){
    var self = this;
    var deferred = $.Deferred();
    var deferreds = [];

    // builds tree of views from returned data
    var rootNodeObj = {
      id : -1,
      code : '-1',
      name : self.model.get('title'),
      children: [],
      selected: true,
      expanded: false,
      isSelectable: false
    };


    //get available endpoint children
    this.childEndpoints = new Backbone.Collection();
    this.childEndpoints.url = url;
    this.childEndpoints.fetch().then(function(){
      self.childEndpoints.each(function(child){
        var tmpNode = {
          id : child.get('id'),
          code : '-1',
          name : child.get('name'),
          selected: true,
          expanded: false,
          isSelectable: false
        };

        child.url = url + '/'+ child.get('name');
        deferreds.push(
          child.fetch().then(function(data){
            tmpNode.children = data;
            rootNodeObj.children.push(tmpNode);
          })
        );
      });

      // when all are done create tree.
      $.when.apply($, deferreds).then(function(){
        self.treeModel = new TreeNodeModel(rootNodeObj);
        self.treeView = new TreeNodeView();
        deferred.resolve();
      });
    });

    return deferred;
  }
});
