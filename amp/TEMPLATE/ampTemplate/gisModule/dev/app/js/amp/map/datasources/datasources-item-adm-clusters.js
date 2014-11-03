var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/datasources-item-adm-clusters.html', 'utf8');


module.exports = Backbone.View.extend({
  PAGE_SIZE: 15,
  _currentPage:0,

  template: _.template(Template),
  initialize: function(options) {
    this.app = options.app;
  },

  render: function() {
    var self = this;
    debugger;
    this.model.load().then(function() {
      self.$el.html(self.template(self.model.toJSON()));
    });
    this.app.data.activities;
    return this;
  },

  _generateProjectList: function() {
    var self = this;
    this._currentPage = 0;

    this.$el.find('.load-more').click(function() {
      self._currentPage++;
      self._loadMoreProjects();
    });

    return this._loadMoreProjects();
  },


  //TODO: should be done in data.adm cluster..then we can render full list on second or third click...
  _loadMoreProjects: function() {
    var self = this;
    var startIndex = this._currentPage * this.PAGE_SIZE;
    var activityIDs = this.cluster.properties.activityid.slice(startIndex, startIndex + this.PAGE_SIZE);

    // hide load more button if all activities loaded.
    if (startIndex + this.PAGE_SIZE >= this.cluster.properties.activityid.length) {
      this.tempDOM.find('.load-more').hide();
    } else {
      this.tempDOM.find('.load-more').text('load more ' + (startIndex + this.PAGE_SIZE) + '/' +
                                           this.cluster.properties.activityid.length);
    }

    return this.app.data.activities.getActivites(activityIDs).then(function(activityCollection) {
      self.tempDOM.find('.project-list').append(
        self.projectListTemplate({activities: activityCollection})
        );
    });
  }


});
