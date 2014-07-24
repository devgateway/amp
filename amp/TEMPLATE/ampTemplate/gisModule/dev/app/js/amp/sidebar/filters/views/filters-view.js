var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');

var APIHelper = require('../../../../libs/local/api-helper');

var Backbone = require('backbone');
var BaseControlView = require('../../base-control/base-control-view');
var GenericFilterView = require('../views/generic-filter-view');
var YearsFilterView = require('../views/years-filter-view');
var Template = fs.readFileSync(__dirname + '/../templates/filters-template.html', 'utf8');


module.exports = BaseControlView.extend({
  id: 'tool-filters',
  title: 'Filters',
  iconClass: 'ampicon-filters',
  description: 'Apply filters to the map.',
  apiURL: APIHelper.getAPIBase() + '/rest/gis/filters',

  // collection of child views..
  filterViewsInstances:[],

  template: _.template(Template),

  initialize: function() {
    var self = this;
    BaseControlView.prototype.initialize.apply(this);

    this._getFilterList().done(function(filterList){
      self.filterViewsInstances = filterList;
      self.render();
    });

    //TODO: register listener for FILTER_CHANGED event, then iterate over
    //      filterViews and call createFilterJSON on each model
    //      create master filter object and pass it to the map. to call api and re-render.
  },


  render: function() {
    BaseControlView.prototype.render.apply(this);

    // add content
    this.$('.content').html(this.template({title: this.title}));

    var filtersContainer = this;
    _.each(this.filterViewsInstances, function(filterView) {
      filtersContainer.$('.filter-list').append(filterView.renderTitle().el);
    });

    return this;
  },

  _getFilterList: function(){
    var self = this;
    var filterList = [];
    var deferred =  $.Deferred();

    $.ajax({
        url: this.apiURL
      })
      .done(function(data){
        if( _.isEmpty(data) ){
          console.warn('Filters API returned empty', data);
        }

        _.each(data, function(APIFilter){
          var view = self._createFilterView(APIFilter);
          filterList.push(view);
        });

        deferred.resolve(filterList);
      })
      .fail(function(jqXHR, textStatus, errorThrown){
        var errorMessage = 'Getting filters failed';
        console.error('Getting filters failed', jqXHR, textStatus, errorThrown);
        deferred.reject(errorMessage);
      });

    return deferred;
  },

  _createFilterView: function(APIFilter){
    // Assume all filters are genericView, but if we want, we can
    // use specific granular views for some filters: OrgFilterView
    // TODO: magic strings are dangerous, config somewhere...
    var view = null;
    switch (APIFilter.name){
      case 'Years':
        view = new YearsFilterView({url:APIFilter.endpoint});
        break;
      default:
        view = new GenericFilterView({url:APIFilter.endpoint, modelValues:{title:APIFilter.name}});
    }
    return view;
  }
});

