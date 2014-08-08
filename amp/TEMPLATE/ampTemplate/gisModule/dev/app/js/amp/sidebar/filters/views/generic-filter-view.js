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
      self._updateCountInMenu();
      self.treeModel.on('change:numSelected', function(){
        self._updateCountInMenu();
      });
    });
  },

  _updateCountInMenu: function(){
    if(this.treeModel.get('numSelected') === this.treeModel.get('numPossible') || this.treeModel.get('numSelected') === 0){
      this.$('.filter-count').text('all');
      this.$el.removeClass('active');
    } else{
      this.$('.filter-count').text(this.treeModel.get('numSelected') + '/' + this.treeModel.get('numPossible'));
      this.$el.addClass('active');
    }
  },

  renderFilters: function () {
    var self = this;
    BaseFilterView.prototype.renderFilters.apply(this);
    this.$('.filter-options').append(this.template(this.model.toJSON()));
    this.$('.tree-container').append(this.treeView.render(this.treeModel).$el);
    this.treeModel.set('selected',false);
    this.treeModel.set('expanded',true);


    // Add listeners, tried doing in 'events' but didn't work..i had issues before with
    // inheritence and events object
    this.$('.select-all').click(function(){self._selectAll();});
    this.$('.select-none').click(function(){self._selectNone();});


  },

  _selectAll: function(){
    // force trigger even if already this state (important for half-fill ui
    this.treeModel.set('selected',true,{ 'silent': true }); 
    this.treeModel.trigger('change:selected',this.treeModel, null,{propogation:false});
  },

  _selectNone: function(){
    // force trigger even if already this state (important for half-fill ui)
    this.treeModel.set('selected',false,{ 'silent': true });
    this.treeModel.trigger('change:selected',this.treeModel, null,{propogation:false});
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
        expanded: false
      };

      self.treeModel = new TreeNodeModel(rootNodeObj);
      self.treeView = new TreeNodeView();
    })
    .fail(function(jqXHR, textStatus, errorThrown){
      console.error('failed to get filter ', jqXHR, textStatus, errorThrown);
    });

  }

});

