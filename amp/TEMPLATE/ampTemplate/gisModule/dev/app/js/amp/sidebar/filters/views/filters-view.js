var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');

var BaseControlView = require('../../base-control/base-control-view');
var GenericFilterView = require('../views/generic-filter-view');
var GenericNestedFilterView = require('../views/generic-nested-filter-view');
var YearsFilterView = require('../views/years-filter-view');
var Template = fs.readFileSync(__dirname + '/../templates/filters-template.html', 'utf8');


module.exports = BaseControlView.extend({
  id: 'tool-filters',
  title: 'Filters',
  iconClass: 'ampicon-filters',
  description: 'Apply filters to the map.',
  apiURL: '/rest/filters',

  // collection of child views..
  filterViewsInstances:[],

  template: _.template(Template),

  initialize: function(options) {
    this.app = options.app;
    var self = this;
    BaseControlView.prototype.initialize.apply(this, arguments);

    this._getFilterList().done(function(filterList){
      self.filterViewsInstances = filterList;
      self.render();
    });
  },


  render: function() {
    BaseControlView.prototype.render.apply(this);

    // add content
    this.$('.content').html(this.template({title: this.title}));

    var self = this;
    _.each(this.filterViewsInstances, function(filterView) {
      self.$('.filter-list').append(filterView.renderTitle().el);
    });

    // setup filter-panel
    this.$('#filter-popup').draggable({ cancel: '.panel-body, .panel-footer', cursor: 'move'  });

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
          if(APIFilter.ui){
            var view = self._createFilterView(APIFilter);
            filterList.push(view);
          }
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
        view = new YearsFilterView({app:this.app, url:APIFilter.endpoint, modelValues:{title:APIFilter.name}});
        break;
      case 'Sectors':
      case 'Programs':
      case 'Organizations':
        view = new GenericNestedFilterView({app:this.app, url:APIFilter.endpoint, modelValues:{title:APIFilter.name}});
        break;
      case 'Dates':
        view = new YearsFilterView({app:this.app, url:APIFilter.endpoint, modelValues:{title:APIFilter.name}});
        break;
      default:
        view = new GenericFilterView({app:this.app, url:APIFilter.endpoint, modelValues:{title:APIFilter.name}});
    }
    return view;
  }
});

