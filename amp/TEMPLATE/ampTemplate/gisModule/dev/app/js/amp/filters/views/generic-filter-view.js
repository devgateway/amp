var fs = require('fs');
var _ = require('underscore');

// var GenericFilterModel = require('../models/generic-filter-model');
var TreeNodeView = require('../tree/tree-node-view');
var BaseFilterView = require('../views/base-filter-view');
var Template = fs.readFileSync(__dirname + '/../templates/generic-filter-template.html', 'utf8');


// This is a generic model for filters. It assumes a tree structure.
// If you don't want a tree structure just extend base-filter
module.exports = BaseFilterView.extend({

  className: BaseFilterView.prototype.className + ' filter-generic',
  template: _.template(Template),
  _loaded: null,

  events:{
    'click  .select-all': '_selectAll',
    'click  .select-none': '_selectNone'
  },

  initialize:function(options) {
    var self = this;
    BaseFilterView.prototype.initialize.apply(this, [options]);

    this.model = options.model;

    //TODO: modify to be on demand load style, so only done the first time the user wants that filter...
    // Create tree view
    //TODO: make tree loading content responsibility of model, not view...
    this._loaded = this._createTree().then(function() {
      self._updateCountInMenu();
      self.model.get('tree').on('change:numSelected', function() {
        self._updateCountInMenu();
      });
    });
  },

  _updateCountInMenu:function() {
    if (this.model.get('tree').get('numSelected') === this.model.get('tree').get('numPossible') ||
        this.model.get('tree').get('numSelected') === 0) {
      this.$titleEl.find('.filter-count').text('all');
      this.$el.removeClass('active');
    } else {

      this.$titleEl.find('.filter-count').text(this.model.get('tree').get('numSelected') +
        '/' +
        this.model.get('tree').get('numPossible'));
      this.$el.addClass('active');
    }
  },

  renderFilters:function() {
    var self = this;
    BaseFilterView.prototype.renderFilters.apply(this);

    this._loaded.then(function() {
      self.$el.html(self.template(self.model.toJSON()));
      self.$('.tree-container').append(self.treeView.render(self.model.get('tree')).$el);
      self.model.get('tree').set('expanded', true);
    });

    return this;
  },

  _selectAll:function() {
    // force trigger even if already this state (important for half-fill ui
    this.model.get('tree').set('selected', true, {silent: true });
    this.model.get('tree').trigger('change:selected', this.model.get('tree'), null, {propogation:false});
  },

  _selectNone:function() {
    // force trigger even if already this state (important for half-fill ui)
    this.model.get('tree').set('selected', false, {silent: true });
    this.model.get('tree').trigger('change:selected', this.model.get('tree'), null, {propogation:false});
  },


  _createTree:function() {
    var self = this;

    return this.model.getTree().then(function(tree) {
      self.treeModel = tree;
      self.treeView = new TreeNodeView();
    });
  }

});

