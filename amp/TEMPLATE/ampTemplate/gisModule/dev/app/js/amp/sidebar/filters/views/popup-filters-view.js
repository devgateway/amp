var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');

var Backbone = require('backbone');

var TopLevelFilterView = require('../views/top-level-filter-view');

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
    'click .apply': 'apply',
    'click .cancel': 'cancel'
  },

  // collection of top-level-filter views..
  filterViewsInstances:{},

  template: _.template(Template),
  titleTemplate: _.template(TitleTemplate),

  initialize:function(options) {
    this.app = options.app;

    // setup filter-panel
    this.$el.draggable({ cancel: '.panel-body, .panel-footer', cursor: 'move'  });

    // Create top level views
    this._createTopLevelFilterViews();

    this._getFilterList().done();
  },

  _createTopLevelFilterViews: function() {
    this.filterViewsInstances = [];


    this.filterViewsInstances.sectors = new TopLevelFilterView({title:'Sector'});
    this.filterViewsInstances.programs = new TopLevelFilterView({title:'Programs'});
    this.filterViewsInstances.activity = new TopLevelFilterView({title:'Activity'});
    this.filterViewsInstances.donors = new TopLevelFilterView({title:'Donor'});
    this.filterViewsInstances.others = new TopLevelFilterView({title:'Other'});
  },


  render:function() {
    var self = this;

    if (this.firstRender) {
      this.$el.html(this.template({}));
      this.$el.show();

      this._getFilterList().done(function() {
        self._setupOrgListener();
        self.renderFilters();
      });

      // setup any popovers as needed...
      this.popovers = this.$('[data-toggle="popover"]');
      this.popovers.popover();

      // Translate
      this.app.translator.translateDOM(this.el);
      this.firstRender = false;
    }

    return this;
  },

  //TODO: move to app data
  _setupOrgListener:function() {
    //  var orgFilter = this.app.data.filters.findWhere({title: 'Organizations'});
    var orgGroupFilter = this.app.data.filters.findWhere({title: 'OrganizationGroupList'});
    orgGroupFilter.getTree().then(function() {
      //TODO: setup 'Organizations' to listen to 'OrganizationGroups'
    });
  },

  renderFilters:function() {
    var self = this;

    self.$('.filter-options').html('');

    for (var filterView in this.filterViewsInstances) {
      if (this.filterViewsInstances.hasOwnProperty(filterView)) {
        var tmpFilterView = this.filterViewsInstances[filterView];
        this.$('.filter-titles').append(tmpFilterView.renderTitle().titleEl);

        // maybe...render bodies on click, not all at once...doesn't seem critical right now...
        this.$('.filter-options').append(tmpFilterView.renderFilters().el);
      }
    }

    this.$('.filter-titles a:first').tab('show');
  },



  //TODO: move to app data
  _getFilterList:function() {
    var self = this;
    var deferred =  $.Deferred();

    if (this.filterViewsInstances.others.filterCollection.length <= 0) {
      $.ajax({
        url: this.apiURL
      })
      .done(function(data) {
        _.each(data, function(APIFilter) {
          if (APIFilter.ui) {
            self.app.data.filters.add(self._createFilterModels(APIFilter));
          }
        });

        if (_.isEmpty(data)) {
          console.warn('Filters API returned empty', data);
        }

        deferred.resolve();
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
    // Assume all filters are genericView, but if we want, we can
    // use specific granular views for some filters: OrgFilterView
    // TODO: magic strings are dangerous, config somewhere...
    switch (APIFilter.name) {
      case 'ActivityStatusList':
      case 'ActivityBudgetList':
      case 'ActivityApprovalStatus':
        tmpModel = new GenericFilterModel({
          app:this.app,
          url:APIFilter.endpoint,
          title:APIFilter.name
        });
        this.filterViewsInstances.activity.filterCollection.add(tmpModel);
        break;
      case 'Programs':
        this._goOneDeeper(this.filterViewsInstances.programs.filterCollection, APIFilter.endpoint);
        break;
      case 'Dates':
        //TODO: create year model and add it to filterViewInstances..
        //was choosing between colleciton of models or views
        var yearModel = new YearsFilterModel({
          title:APIFilter.name,
          app:this.app,
          url:APIFilter.endpoint
        });
        this.filterViewsInstances.others.filterCollection.add(yearModel);
        break;
      case 'Sectors':
        this._goOneDeeper(this.filterViewsInstances.sectors.filterCollection, APIFilter.endpoint);
        break;
      case 'Organizations':
      case 'OrganizationGroupList':
        tmpModel = new GenericFilterModel({
          app:this.app,
          url:APIFilter.endpoint,
          title:APIFilter.name
        });
        this.filterViewsInstances.donors.filterCollection.add(tmpModel);
        break;
      default:
        tmpModel = new GenericFilterModel({
          app:this.app,
          url:APIFilter.endpoint,
          title:APIFilter.name
        });
        this.filterViewsInstances.others.filterCollection.add(tmpModel);
    }

    return tmpModel;
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
          app:self.app,
          url:url + '/' + APIFilter.id,
          title:APIFilter.name
        });
        targetCollection.add(tmpModel);
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

  apply:function() {
    // TODO: consider a different name to avoid collision with javascript function.apply
    // trigger common event for applying filters.
    // this.convertTreeToJSONFilter(); //implemented by child, and if not fallback to base.

    // trigger something that will serialize all....
    //TODO: move to app.data.filters, which should be turned into a special collection.
    this.app.data.filters.each(function(filter){
      console.log(filter.get('title') + ': ',filter.serialize());
    });


    this.$el.hide();
    this.trigger('close');
    //TODO: collapse accordion, or will cause issues...
  },

  cancel:function() {
    this.$el.hide();
    this.trigger('close');
    //TODO: collapse accordion, or will cause issues...
  }

});

