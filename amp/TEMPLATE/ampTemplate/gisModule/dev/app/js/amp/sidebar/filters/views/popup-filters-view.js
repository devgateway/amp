var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');

var Backbone = require('backbone');

var GenericFilterView = require('../views/generic-filter-view');
var GenericNestedFilterView = require('../views/generic-nested-filter-view');
//var OrganizationsFilterView = require('../views/organizations-filter-view');
var YearsFilterView = require('../views/years-filter-view');

var Template = fs.readFileSync(__dirname + '/../templates/filters-content-template.html', 'utf8');
var TitleTemplate = fs.readFileSync(__dirname + '/../templates/filter-title-template.html', 'utf8');


module.exports = Backbone.View.extend({
  id: 'tool-filters',
  title: 'Filters',
  iconClass: 'ampicon-filters',
  description: 'Apply filters to the map.',
  apiURL: '/rest/filters',

  events:{
    'click .accordion-heading': 'newlaunchFilter'
  },

  // collection of child views..
  filterViewsInstances:[],

  template: _.template(Template),
  titleTemplate: _.template(TitleTemplate),

  initialize:function(options) {
    var self = this;
    this.app = options.app;

    // setup filter-panel
    this.$el.draggable({ cancel: '.panel-body, .panel-footer', cursor: 'move'  });

    // TODO: filter list should be stored in app.data.filters
    this._getFilterList().done(function(filterList) {
      self.filterViewsInstances = filterList;
    });

  },


  render:function() {
    var self = this;

    this.$el.html(this.template({}));
    this.$el.show();
    // TODO: move to events.
    this.$el.on('click', '.cancel', self.cancel);
    this.$el.on('click', '.apply', self.apply);

    this._getFilterList().done(function() {
      self.renderFilters();
    });

    // setup any popovers as needed...
    this.popovers = this.$('[data-toggle="popover"]');
    this.popovers.popover();

    // Translate
    this.app.translator.translateDOM(this.el);

    return this;
  },

  renderFilters:function() {
    var self = this;

    _.each(this.filterViewsInstances, function(filterView) {
      self.$('.filter-titles').append(self.titleTemplate({title: filterView.model.get('title')}));
    });

    //TODO: render bodies on click, not all at once...
    self.$('.filter-options').html('');
    _.each(this.filterViewsInstances, function(filterView) {
      self.$('.filter-options').append(filterView.renderFilters().el);
    });

    //TODO:???should we do bootstrap tabs or custom...bootstrap makes most sense....
    // but custom is better for dynamic laoding...or do both...

  },


//TODO use load once mixin...
  _getFilterList:function() {
    var self = this;
    var filterList = [];
    var deferred =  $.Deferred();

    $.ajax({
      url: this.apiURL
    })
    .done(function(data) {
      if (_.isEmpty(data)) {
        console.warn('Filters API returned empty', data);
      }

      _.each(data, function(APIFilter) {
        if (APIFilter.ui) {
          var view = self._createFilterView(APIFilter);
          filterList.push(view);
        }
      });

      deferred.resolve(filterList);
    })
    .fail(function(jqXHR, textStatus, errorThrown) {
      var errorMessage = 'Getting filters failed';
      console.error('Getting filters failed', jqXHR, textStatus, errorThrown);
      deferred.reject(errorMessage);
    });

    return deferred;
  },

  _createFilterView:function(APIFilter) {
    // Assume all filters are genericView, but if we want, we can
    // use specific granular views for some filters: OrgFilterView
    // TODO: magic strings are dangerous, config somewhere...
    var view = null;
    switch (APIFilter.name) {
      case 'Years':
        view = new YearsFilterView({app:this.app, url:APIFilter.endpoint, modelValues:{title:APIFilter.name}});
        break;
      case 'Sectors':
      case 'Programs':
      case 'Organizations':
        view = new GenericNestedFilterView({
          app:this.app,
          url:APIFilter.endpoint,
          modelValues:{title:APIFilter.name}
        });
        break;
      case 'Dates':
        view = new YearsFilterView({app:this.app, url:APIFilter.endpoint, modelValues:{title:APIFilter.name}});
        break;
      default:
        view = new GenericFilterView({app:this.app, url:APIFilter.endpoint, modelValues:{title:APIFilter.name}});
    }
    return view;
  },

  apply:function() {
    // TODO: consider a different name to avoid collision with javascript function.apply
    // trigger common event for applying filters.
    // this.convertTreeToJSONFilter(); //implemented by child, and if not fallback to base.
    $('#filter-popup').hide();
  },

  cancel:function() {
    $('#filter-popup').hide();
  }

});

