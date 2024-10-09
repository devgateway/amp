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

  fetchCollection: function() {
    var self = this;  // Reference to the current view

    return new Promise(function(resolve, reject) {
      self.collection.fetch({
        success: function(collection, response, options) {
          resolve(collection);  // Resolve the promise with the collection
        },
        error: function(collection, response, options) {
          reject(response);  // Reject the promise with the error response
        }
      });
    });
  },

  render: function() {
    var self = this;

    this.fetchCollection().then(function(collection) {
      // Proceed with your logic after the collection has been fetched
      var tableContent = new WocatItem({
        collection: collection,  // Pass the fetched collection
        app: self.app
      }).render().el;

      // Append or process `tableContent` as needed
      self.$el.append(tableContent);
    }).catch(function(error) {
      console.error('Failed to fetch collection:', error);
    });

      var collection = self.collection;
      self.app.translator.translateDOM(
        self.template(collection)).then(
        function(newEl) {
          self.$el.html(newEl);
        });

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
