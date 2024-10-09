var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var WocatItem = require('./wocat-item-adm-clusters');
var Template = fs.readFileSync(__dirname + '/wocat-table-template.html', 'utf8');
var ProjectCollection = require('./project-collection');
module.exports = Backbone.View.extend({

  className: 'wocatTable',

  template: _.template(Template),

  events: {
    'click .load-more': 'loadMoreFromWocatCollection'
  },

  initialize: function(options) {
    this.app = options.app;
    this.collection = new ProjectCollection([], {size:10, page:1});
    this.render();
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
      if (!_.isEmpty(tableContent)) {
        self.$('table', self.$el).append(tableContent);
      } else { //drs added otherwise FF won't add...
        self.$('table').append(tableContent);
      }
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


    return this;
  },

  toggleDatasources: function() {
    this.$el.toggleClass('expanded');
  },

  /*TODO(thadk) do not redraw entire view and lose the user their scrolling *
   *
   *
   **/
  loadMoreFromWocatCollection: function() {
    console.log('Trying to load more from wocat collection');
    var self = this;
    if (self.collection.totalElements === self.collection.pageSize)
    {
      self.$el.find('.load-more').addClass('load-more-hide');
    }else
    {
      self.$el.find('.load-more').removeClass('load-more-hide');
    }
    self.collection.size=10;
    self.collection.page+=1;

    var currentPage = self.collection.currentPage;
    var totalPages = self.collection.totalPages;

    if (currentPage < totalPages) {
      self.render();
      // self.fetchCollection().then(function(moreProjects) {
      //   var tableContent = new WocatItem({
      //     collection: moreProjects,
      //     app: self.app
      //   }).render().el;
      //
      //   // Append or process `tableContent` as needed
      //   if (!_.isEmpty(tableContent)) {
      //     self.$('table', self.$el).append(tableContent);
      //   } else { // For Firefox compatibility
      //     self.$('table').append(tableContent);
      //   }
      //
      //   if (currentPage + 1 >= totalPages) {
      //     self.$el.find('.load-more').addClass('load-more-hide');
      //   }
      //   else
      //   {
      //     self.$el.find('.load-more').removeClass('load-more-hide');
      //   }
      // }).catch(function(error) {
      //   console.error('Failed to fetch more projects:', error);
      // });
    }




  }



});
