var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var WocatItem = require('./wocat-item-adm-clusters');
var Template = fs.readFileSync(__dirname + '/wocat-table-template.html', 'utf8');
var ProjectCollection = require('./project-collection');

module.exports = Backbone.View.extend({

  className: 'wocatTable',

  template: _.template(Template),

  // events: {
  //   'click .load-more': 'loadMoreFromCollection'
  // },

  initialize: function(options) {
    this.app = options.app;
    this.collection = new ProjectCollection();
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

    this.collection.fetch().then(function() {
      // drs: moved to do this after collection load?
      var tableContent = new WocatItem({
        collection: self.collection,
        app: self.app
      }).render().el;

      // var collection = self.collection.getPageDetails();
      // self.app.translator.translateDOM(
      //   self.template(collection)).then(
      //   function(newEl) {
      //     self.$el.html(newEl);
      //   });

      // if((self.collection.getPageDetails().currentPage + 1) >= self.collection.getPageDetails().totalPageCount){
    	//   self.$el.find('.load-more').addClass('load-more-hide');
      // }else{
    	//   self.$el.find('.load-more').removeClass('load-more-hide');
      // }

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
   *
   **/
  loadMoreFromCollection: function() {
    var self = this;
    if (self.collection.getPageDetails().currentPosition < self.collection.getPageDetails().totalCount) {
      if (!self.$el.find('.load-more').hasClass('disabled')) {
        self.collection.fetchMore().done(function() {
          self.render(); //TODO: (drs) just append the new ones...?
        });
      }
      self.$el.find('.load-more').text('Loading...').addClass('disabled');
    } else {
    	self.$el.find('.load-more').addClass('load-more-hide');
    }
  }

});
