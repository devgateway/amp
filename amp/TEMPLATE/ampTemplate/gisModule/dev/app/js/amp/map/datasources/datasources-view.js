var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var DatasourcesItem = require('./datasources-item-adm-clusters');
var Template = fs.readFileSync(__dirname + '/datasources-template.html', 'utf8');

module.exports = Backbone.View.extend({

  className: 'datasources',

  template: _.template(Template),

  events: {
    'click a[href="#toggle-datasources-collapse"]': 'toggleDatasources',
    'click .load-more': 'loadMoreFromCollection'
  },

  initialize: function(options) {
    this.app = options.app;
    this.collection = this.app.data.activities;
    this.listenTo(this.app.data.filter, 'apply', this.applyFilters);
    _.bindAll(this, 'render');
    window.renA = this.collection;
    window.renB = this.render;
  },

  // if filters change, fetch
  applyFilters: function() {
      var self = this;
      this.collection.fetch().then(function() {
        self.render();
      });
  },
  render: function() {
    var self = this;
    var content = new DatasourcesItem({
        collection: this.app.data.activities,
        app: this.app
      }).render().el;

    this.collection.load().then(function() {
      console.log(self.collection.getPageDetails());
      self.$el.html(self.template(
        self.collection.getPageDetails()
      ));

      if (!_.isEmpty(content)) {
        /*self.$el.addClass('expanded');*/
        self.$('.datasources-content table',self.$el).append(content);
      }
    });

    return this;
  },

  toggleDatasources: function() {
    this.$el.toggleClass('expanded');
  },

  /*TODO(thadk) do not redraw entire view and lose the user their scrolling *
   *
   **/
  loadMoreFromCollection: function() {
    var self = this;
    if (!self.$el.find('.load-more').hasClass('disabled')) {
      this.collection.fetchMore().done(function() {
        self.render();
      });
    }
    self.$el.find('.load-more').text('loading...').addClass('disabled');

  }

});
