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

// organizations need to do one API call for possible roles.
// and another call for list of all orgs.
// then combine  / nest them for ui.
// extracting values will also be tricky.
module.exports = GenericFilterView.extend({

  className: GenericFilterView.prototype.className,
  template: _.template(Template),

  initialize: function(options) {
    var self = this;
    //intentinoally not GenericFilterView.prototype, we want to do it our way
    BaseFilterView.prototype.initialize.apply(this, [options]);
    this.model = new GenericFilterModel(options.modelValues);

    this._createTree().then(function(){
      self._updateCountInMenu();
      self.treeModel.on('change:numSelected', function(){
        self._updateCountInMenu();
      });
    });
  },


  _createTree: function(){
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

    // TODO: better way of getting endpoints than hardcoding, amybe use options.url ...
    this.organizationRoles = new Backbone.Collection({});
    this.organizationRoles.url = '/rest/filters/organizations';
    this.organizations = new Backbone.Collection({});
    this.organizations.url = '/rest/filters/organizations/1';

    deferreds.push(this.organizationRoles.fetch());
    deferreds.push(this.organizations.fetch());

    $.when.apply($, deferreds).then(function(){

      // sort orgs
      self.organizations.comparator = function(model) {
        return model.get('name');
      };
      self.organizations.sort();

      self.organizationRoles.each(function(orgRole){
          var tmpRoleNode = {
            id : orgRole.get('id'),
            code : '-1',
            name : orgRole.get('name'),
            selected: true,
            expanded: false,
            isSelectable: false,
            children: self.organizations.toJSON()
          };
          rootNodeObj.children.push(tmpRoleNode);
        });

      self.treeModel = new TreeNodeModel(rootNodeObj);
      self.treeView = new TreeNodeView();
      deferred.resolve();
    });

    return deferred;
  }
});
