var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var DatasourcesItem = require('./datasources-item-adm-clusters');
const GisSettings = require("../../services/gis_settings");
var Template = fs.readFileSync(__dirname + '/datasources-table-template.html', 'utf8');
var gisSettings = new GisSettings();

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
    this.listenTo(this.app.data.settingsWidget, 'applySettings', this.refreshModel);
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
        app: self.app,
        gisSettings: gisSettings.gisSettings
      }).render().el;

      var collection = self.collection.getPageDetails();
      collection['gisSettings']= gisSettings;
      self.app.translator.translateDOM(
        self.template(collection)).then(
        function(newEl) {
          self.$el.html(newEl);
          self.updatePlannedActualUI();
        });
      
      if((self.collection.getPageDetails().currentPage + 1) >= self.collection.getPageDetails().totalPageCount){
    	  self.$el.find('.load-more').addClass('load-more-hide');
      }else{    	  
    	  self.$el.find('.load-more').removeClass('load-more-hide');
      }
      
      // drs: review, inconsistant behaviour between chrome and FF
      if (!_.isEmpty(tableContent)) {
        self.$('table', self.$el).append(tableContent);
      } else { //drs added otherwise FF won't add...
        self.$('table').append(tableContent);
      }
    });

    return this;
  },

  // If any of the 'planned' funding types are selected then the
  // table should show planned comitments and dispursements,
  // otherwise show actual values.
  updatePlannedActualUI: function() {
	  var self = this;
	  var selected = self.app.data.settingsWidget.definitions.getSelectedOrDefaultFundingTypeId();
      self.$('.setting-scc').hide();
      self.$('.setting-executings').hide();
	  if (selected.toLowerCase().indexOf('ssc') >= 0) {
          self.$('.setting-actual').hide();
          self.$('.setting-planned').hide();
          self.$('.setting-donors').hide();
          self.$('.setting-scc').show();
          self.$('.setting-executings').show();
      } else if (selected.toLowerCase().indexOf('planned') >= 0) {
		  self.$('.setting-actual').hide();
		  self.$('.setting-planned').show();
	  } else {
		  self.$('.setting-actual').show();
		  self.$('.setting-planned').hide();
	  }    
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
