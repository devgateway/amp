var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var DatasourcesItem = require('./datasources-item-adm-clusters');
var Template = fs.readFileSync(__dirname + '/datasources-table-template.html', 'utf8');

module.exports = Backbone.View.extend({

  className: 'datasourcesTable',

  template: _.template(Template),

  events: {
    'click .load-more': 'loadMoreFromCollection'
  },

  initialize: function(options) {
    this.app = options.app;
    this.collection = this.app.data.activities;
    this.listenTo(this.app.data.filter, 'apply', this.refreshModel);
    this.listenTo(this.app.data.settings, 'change:selected', this.refreshModel);
    _.bindAll(this, 'render');
  },

  // if filters change, fetch
  refreshModel: function() {
    var self = this;
    this.collection.fetch().then(function() {
      self.render();
    });
  },

  render: function() {
    var self = this;

    this.collection.load().then(function() {
      // drs: moved to do this after collection load?
      var tableContent = new DatasourcesItem({
        collection: self.collection,
        app: self.app
      }).render().el;

      self.$el.html(self.template(
        self.collection.getPageDetails()
      ));

      self.app.translator.translateDOM(
        self.template(self.collection.getPageDetails())).then(
        function(newEl) {
          self.$el.html(newEl);
        });
      // drs: review, inconsistant behaviour between chrome and FF
      if (!_.isEmpty(tableContent)) {
        self.$('table', self.$el).append(tableContent);
      } else { //drs added otherwise FF won't add...
        self.$('table').append(tableContent);
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
        self.render(); //TODO: (drs) just append the new ones...?
      });
    }
    self.$el.find('.load-more').text('loading...').addClass('disabled');

  }

});
