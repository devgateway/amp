var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var $ = require('jquery');
var APIHelper = require('../../../../libs/local/api-helper');

var GenericFilterModel = require('../models/generic-filter-model');
var TreeNodeModel= require('../models/tree-node-model');
var TreeNodeView = require('../views/tree-node-view');
var BaseFilterView = require('../views/base-filter-view');
var Template = fs.readFileSync(__dirname + '/../templates/generic-filter-template.html', 'utf8');


module.exports = BaseFilterView.extend({

  className: BaseFilterView.prototype.className + ' filter-generic',
  template: _.template(Template),

  initialize: function(options) {
    var self = this;
    BaseFilterView.prototype.initialize.apply(this);

    this.model = new GenericFilterModel(options.modelValues);

    //TODO: modify to be on demand load style, so only done the first time the user wants that filter...
    // Create tree view
    this._createTree(options.url).then(function(){
      self.$('.filter-count').text(self.treeModel.get('numSelected'));
      self.treeModel.on('updateCount', function(){
        self.$('.filter-count').text(self.treeModel.get('numSelected'));
      });
    });
  },

  renderFilters: function () {
    BaseFilterView.prototype.renderFilters.apply(this);
    this.$('.filter-options').append(this.template());
    this.$('.tree-container').append(this.treeView.render(this.treeModel).$el);
    this.treeView.expand();

  },

  _createTree: function(url){
    var self = this;
    return $.get(APIHelper.getAPIBase() + url).then(function(data){
      // builds tree of views from returned data
      var rootNodeObj = {
        id : -1,
        code : '-1',
        name : self.model.get('title'),
        children: data,
        selected: true,
        expanded: true
      };

      self.treeModel = new TreeNodeModel(rootNodeObj);
      self.treeView = new TreeNodeView();
    });

  }

});

