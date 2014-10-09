var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');

var TopLevelFilterView = require('../views/top-level-filter-view');
var AllFilterCollection = require('../collections/all-filters-collection');


var Template = fs.readFileSync(__dirname + '/../templates/filters-content-template.html', 'utf8');
var TitleTemplate = fs.readFileSync(__dirname + '/../templates/filter-title-template.html', 'utf8');

module.exports = Backbone.View.extend({
  id: 'tool-filters',
  title: 'Filters',
  iconClass: 'ampicon-filters',
  description: 'Apply filters to the map.',
  apiURL: '/rest/filters',
  firstRender: true,

  events:{
    'click .apply': 'applyFilters',
    'click .cancel': 'cancel',
    'click .reset': 'resetFilters'
  },

  // collection of top-level-filter views..
  filterViewsInstances:{},

  template: _.template(Template),
  titleTemplate: _.template(TitleTemplate),

  initialize:function(options) {
    this.translator = options.translator;
    this._loaded =  $.Deferred();

    if (options.draggable) {
      this.$el.draggable({ cancel: '.panel-body, .panel-footer', cursor: 'move'  });
    }

    this.allFilters = new AllFilterCollection();

    // Create top level views
    this._createTopLevelFilterViews();

    this.render();
  },

  _createTopLevelFilterViews: function() {
    this.filterViewsInstances = [];


    this.filterViewsInstances.sectors = new TopLevelFilterView({title:'Sector'});
    this.filterViewsInstances.programs = new TopLevelFilterView({title:'Programs'});
    this.filterViewsInstances.activity = new TopLevelFilterView({title:'Activity'});
    this.filterViewsInstances.donors = new TopLevelFilterView({title:'Donor'});
    this.filterViewsInstances.allAgencies = new TopLevelFilterView({title:'AllAgencies'});
    this.filterViewsInstances.financials = new TopLevelFilterView({title:'Financial'});
    this.filterViewsInstances.others = new TopLevelFilterView({title:'Other'});
  },


  render:function() {
    var self = this;

    if (this.firstRender) {
      this.$el.html(this.template({}));
      this.$el.show();

      this._getFilterList().done(function() {
        self._loaded.resolve();
        self.renderFilters();

        // setup any popovers as needed...
        self.popovers = self.$('[data-toggle="popover"]');
        self.popovers.popover();

        // Translate if available.
        if (self.translator) {
          self.translator.translateDOM(self.el);
        }
      });

      this.firstRender = false;
    }

    return this;
  },


  renderFilters:function() {
    var self = this;

    self.$('.filter-options').html('');

    for (var filterView in this.filterViewsInstances) {
      if (this.filterViewsInstances.hasOwnProperty(filterView)) {
        var tmpFilterView = this.filterViewsInstances[filterView];
        this.$('.filter-titles').append(tmpFilterView.renderTitle().titleEl);

        //...render bodies on click, not all at once...doesn't seem critical right now...
        this.$('.filter-options').append(tmpFilterView.renderFilters().el);
      }
    }

    this.$('.filter-titles a:first').tab('show');
  },


  _getFilterList:function() {
    var self = this;
    return this.allFilters.load().then(function() {
      self.allFilters.each(function(model) {
        self._createFilterViews(model);
      });
      return this;
    });
  },

  _createFilterViews: function(tmpModel) {

    // TODO: magic strings are dangerous, config somewhere...
    switch (tmpModel.get('group')) {
      case 'ActivityBudgetList':
      case 'TypeOfAssistanceList':
      case 'FinancingInstrumentsList':
        this.filterViewsInstances.financials.filterCollection.add(tmpModel);
        break;
      case 'ActivityStatusList':
      case 'ActivityApprovalStatus':
        this.filterViewsInstances.activity.filterCollection.add(tmpModel);
        break;
      case 'Programs':
        this.filterViewsInstances.programs.filterCollection.add(tmpModel);
        break;
      case 'Sectors':
        this.filterViewsInstances.sectors.filterCollection.add(tmpModel);
        break;
      case 'Donor':
        this.filterViewsInstances.donors.filterCollection.add(tmpModel);
        break;
      case 'Role':
        this.filterViewsInstances.allAgencies.filterCollection.add(tmpModel);
        break;
      default:
        this.filterViewsInstances.others.filterCollection.add(tmpModel);
    }

  },


  serialize: function() {
    var self = this;
    this.serializedFilters = {};

    this.allFilters.each(function(filter) {
      self.serializedFilters[filter.get('title')] = filter.serialize();
    });
    return this.serializedFilters;
  },

  deserialize: function(blob) {
    if (blob) {

      this.allFilters.each(function(filter) {
        filter.deserialize(blob[filter.get('title')]);
      });
    }
  },

  showFilters:function() {
    this.filterStash = this.serialize();

  },

  resetFilters:function() {
    this.allFilters.each(function(filter) {
      filter.reset();
    });
  },

  applyFilters:function() {
    this.serialize();
    this.trigger('apply');
  },

  cancel:function() {
    this.deserialize(this.filterStash);
    this.trigger('cancel');
  }
});

