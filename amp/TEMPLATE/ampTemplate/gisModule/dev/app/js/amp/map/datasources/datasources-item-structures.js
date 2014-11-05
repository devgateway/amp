var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/datasources-item-adm-clusters.html', 'utf8');


module.exports = Backbone.View.extend({
  PAGE_SIZE: 15,
  _currentPage:0,
  filteredIds: [],
  pagedIds: [],

  template: _.template(Template),
  initialize: function(options) {
    this.app = options.app;

    _.bindAll(this, 'render', '_loadMoreProjects', '_generateProjectList');
  },

  render: function() {
    var self = this;
    this.model.load().then(function() {
      self.filteredIds = _.chain(self.model.get('features'))
        .pluck('properties')
        .pluck('activityid')
        .flatten()
        .unique()
        .value();

      self._generateProjectList();

    });
    //this.app.data.activities;
    return this;
  },

  _generateProjectList: function() {
    var self = this;
    this._currentPage = 0;
    this.pagedIds = [];

    var drawnDeferred = this._loadMoreProjects();

    return drawnDeferred;
  },


  //TODO: should be done in data.adm cluster..then we can render full list on second or third click...
  _loadMoreProjects: function() {
    var self = this;
    var startIndex = this._currentPage * this.PAGE_SIZE;
    var activityIDs = this.filteredIds.slice(startIndex, startIndex + this.PAGE_SIZE);


    return this.app.data.activities.getActivities(activityIDs).then(function(activityCollection) {
      self.pagedIds = self.pagedIds.concat(activityCollection);
      self.$el.html(self.template({
          activities: self.pagedIds,
          title: 'Project Data',
          totalCount: self.filteredIds.length,
          currentPage: self._currentPage + 1,
          startIndex: self.pagedIds.length
        }));

      self.$el.find('.load-more').click(function() {
        if (!self.$el.find('.load-more').hasClass('disabled')) {
          self._currentPage++;
          self._loadMoreProjects();
        }
        self.$el.find('.load-more').text('loading...').addClass('disabled');
      });

       // hide load more button if all activities loaded.
      if (self.pagedIds.length >= self.filteredIds.length) {
        self.$el.find('.load-more').hide();
      } else {
        self.$el.find('.load-more').text('load more ' + (self.pagedIds.length + self.PAGE_SIZE) + '/' +
                                             self.filteredIds.length);
      }

    });
  }


});
