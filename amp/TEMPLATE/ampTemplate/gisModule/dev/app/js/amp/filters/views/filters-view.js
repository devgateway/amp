var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');

var TopLevelFilterView = require('../views/top-level-filter-view');
var AllFilterCollection = require('../collections/all-filters-collection');


var Template = fs.readFileSync(__dirname + '/../templates/filters-content-template.html', 'utf8');
var TitleTemplate = fs.readFileSync(__dirname + '/../templates/filter-title-template.html', 'utf8');
var GenericFilterModel = require('../models/generic-filter-model');
var YearsFilterModel = require('../models/years-filter-model');

module.exports = Backbone.View.extend({
  id: 'tool-filters',
  title: 'Filters',
  iconClass: 'ampicon-filters',
  description: 'Apply filters to the map.',
  apiURL: '/rest/filters',
  firstRender: true,

  events:{
    'click .apply': 'applyFilters',
    'click .cancel': 'cancel'
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
        self._setupOrgListener();
        self.renderFilters();
      });

      // setup any popovers as needed...
      this.popovers = this.$('[data-toggle="popover"]');
      this.popovers.popover();

      // Translate if available.
      if (this.translator) {
        this.translator.translateDOM(this.el);
      }
      this.firstRender = false;
    }

    return this;
  },

  //TODO: move to app data
  _setupOrgListener:function() {
    //  var orgFilter = this.allFilters.findWhere({title: 'Organizations'});
    // var orgGroupFilter = this.allFilters.findWhere({title: 'OrganizationGroupList'});
    // orgGroupFilter.getTree().then(function() {
    //   //TODO: setup 'Organizations' to listen to 'OrganizationGroups'
    // });
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
    var deferred =  $.Deferred();

    if (this.filterViewsInstances.others.filterCollection.length <= 0) {
      $.ajax({
        url: this.apiURL
      })
      .done(function(data) {
        var deferreds = [];

        _.each(data, function(APIFilter) {
          if (APIFilter.ui) {
            deferreds.push(self._createFilterModels(APIFilter));
          }
        });

        if (_.isEmpty(data)) {
          console.warn('Filters API returned empty', data);
        }

        // when all child calls are done resolve.
        $.when.apply($, deferreds).then(function() {
          deferred.resolve();
        });

      })
      .fail(function(jqXHR, textStatus, errorThrown) {
        var errorMessage = 'Getting filters failed';
        console.error('Getting filters failed', jqXHR, textStatus, errorThrown);
        deferred.reject(errorMessage);
      });
    } else {
      deferred.resolve();
    }

    return deferred;
  },

  _createFilterModels: function(APIFilter) {
    var tmpModel = null;
    var deferred =  $.Deferred();
    // Assume all filters are genericView, but if we want, we can
    // use specific granular views for some filters: OrgFilterView
    // TODO: magic strings are dangerous, config somewhere...
    switch (APIFilter.name) {
      case 'ActivityBudgetList':
      case 'TypeOfAssistanceList':
      case 'FinancingInstrumentsList':
        tmpModel = new GenericFilterModel({
          url:APIFilter.endpoint,
          title:APIFilter.name
        });
        this.filterViewsInstances.financials.filterCollection.add(tmpModel);
        break;
      case 'ActivityStatusList':
      case 'ActivityApprovalStatus':
        tmpModel = new GenericFilterModel({
          url:APIFilter.endpoint,
          title:APIFilter.name
        });
        this.filterViewsInstances.activity.filterCollection.add(tmpModel);
        break;
      case 'Programs':
        deferred = this._goOneDeeper(this.filterViewsInstances.programs.filterCollection, APIFilter.endpoint);
        break;
      case 'Dates':
        tmpModel = new YearsFilterModel({
          title:APIFilter.name,
          url:APIFilter.endpoint
        });
        this.filterViewsInstances.others.filterCollection.add(tmpModel);
        break;
      case 'Sectors':
        deferred = this._goOneDeeper(this.filterViewsInstances.sectors.filterCollection, APIFilter.endpoint);
        break;
      case 'Organizations':
      case 'OrganizationGroupList':
      case 'OrgTypesList':
      case 'organizationsRoles':
        tmpModel = new GenericFilterModel({
          url:APIFilter.endpoint,
          title:APIFilter.name
        });
        this.filterViewsInstances.donors.filterCollection.add(tmpModel);
        break;
      default:
        tmpModel = new GenericFilterModel({
          url:APIFilter.endpoint,
          title:APIFilter.name
        });
        this.filterViewsInstances.others.filterCollection.add(tmpModel);
    }

    if (tmpModel) {
      this.allFilters.add(tmpModel);
      deferred.resolve(tmpModel);
    }


    return deferred;
  },

  // get endpoint's children and load them into targetCollection...
  _goOneDeeper: function(targetCollection, url) {
    var self = this;
    var deferred = $.Deferred();
    var tmpModel = null;

    $.ajax({
      url: url
    })
    .done(function(data) {
      _.each(data, function(APIFilter) {
        tmpModel = new GenericFilterModel({
          url:url + '/' + APIFilter.id,
          title:APIFilter.name
        });
        targetCollection.add(tmpModel);
        if (tmpModel) {
          self.allFilters.add(tmpModel);
        }
      });

      deferred.resolve();

      if (_.isEmpty(data)) {
        console.warn('Filters API returned empty', data);
      }
    })
    .fail(function(jqXHR, textStatus, errorThrown) {
      var errorMessage = 'Getting filters failed';
      console.error('Getting filters failed', jqXHR, textStatus, errorThrown);
      deferred.reject(errorMessage);
    });

    return deferred;
  },

  applyFilters:function() {
    this.serialize();
    this.trigger('apply');
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

  cancel:function() {
    this.deserialize(this.filterStash);
    this.trigger('cancel');
  }
});

