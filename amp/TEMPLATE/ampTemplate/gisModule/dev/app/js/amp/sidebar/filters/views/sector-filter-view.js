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


module.exports = GenericFilterView.extend({

  className: GenericFilterView.prototype.className + ' filter-years',
  template: _.template(Template),
  allowedRange: {min: 1980,max: 2015},

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

  // 1. get all schemas
  // 2. create root JSON, with each schema as a child.
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


    //get available sector schema's
    this.sectorConfigNames = new Backbone.Collection();
    this.sectorConfigNames.url = url;
    this.sectorConfigNames.fetch().then(function(){
      self.sectorConfigNames.each(function(schema){
        var tmpNode = {
          id : schema.get('id'),
          code : '-1',
          name : schema.get('name'),
          selected: true,
          expanded: false,
          isSelectable: false
        };

        schema.url = url + '/'+ schema.get('name');
        deferreds.push(
          schema.fetch().then(function(data){
            tmpNode.children = data;
            rootNodeObj.children.push(tmpNode);
          })
        );
      });

      // when all done create tree.
      $.when.apply($, deferreds).then(function(){
        self.treeModel = new TreeNodeModel(rootNodeObj);
        self.treeView = new TreeNodeView();
        deferred.resolve();
      });
    });

    return deferred;
  }
});
