var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var $ = require('jquery');

var TreeNodeView = require('../views/tree-node-view');
var BaseFilterView = require('../views/base-filter-view');
var GenericFilterModel = require('../models/generic-filter-model');
var Template = fs.readFileSync(__dirname + '/../templates/generic-filter-template.html', 'utf8');

var APIBase = require('../../../../libs/local/api-base');

module.exports = BaseFilterView.extend({

  className: BaseFilterView.prototype.className + ' filter-generic',
  template: _.template(Template),

  initialize: function(options) {
    BaseFilterView.prototype.initialize.apply(this);

    this.model = new GenericFilterModel(options.modelValues);

    //TODO: modify to be on demand load style, so only done the first time the user wants that filter...
    // Create tree view
    this._createTree(options.url);
  },

  renderFilters: function () {
    BaseFilterView.prototype.renderFilters.apply(this);
    this.$('.filter-options').append(this.template());    
    this.$('.tree-container').append(this.tree.render());
    this.tree.expand();

  },

  _createTree: function(url){
    var self = this;
    $.get(APIBase.getAPIBase() + url).then(function(data){
      // builds tree of views from returned data
      var rootNodeObj = {
        id : -1,
        code : '-1',
        name : self.model.get('title'),
        children: data,
        selected: true,
        expanded: true
      };

      self.tree = new TreeNodeView(rootNodeObj);
    });

  }

});

