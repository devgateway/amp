var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var LayerIndicatorCollection = require('../collections/layers-collections');
var AccessTypesCollection = require('../collections/access-types-collection.js');
var WorkspaceCollection = require('../collections/workspace-collection.js');
var Events = require('../utils/events.js');
var UserModel = require('../models/user-model.js');
var ShareWorkspaceView = require('./share-workspace-view');
var Constants = require('../utils/constants');
var Template = fs.readFileSync(__dirname + '/../templates/admin-template.html', 'utf8');
module.exports = Backbone.View.extend({
  id: 'layer-manager-admin',
  events: {
      'click .glyphicon-edit': 'editLayer',
      'click .glyphicon-trash': 'removeLayer',
      'click .glyphicon-lock': 'makePrivateLayer',
      'click .glyphicon-globe': 'makePublicLayer',
      'click .glyphicon-share': 'clickShare',
      'click .page-item': 'changePage',
      'click .new-layer-btn': 'createLayer',
      'click .set-population-layer-btn': 'showPopulationLayerView',
      'click .sort': 'sortBy'
  },
  template: _.template(Template),  
  initialize:function(options) {
      this.layers = new LayerIndicatorCollection();
      this.layers.page.set('recordsPerPage', Constants.DEFAULT_LAYERS_PER_PAGE);
      this.accessTypes = new AccessTypesCollection();
      this.workspaceCollection = new WorkspaceCollection();      
      this.user = new UserModel();
      this.EventsBus = options.EventsBus;
      this.translate = options.translate;
      this.translator = options.translator;
      this.settings = options.settings;
      this.def = [];
      this.def.push(this.accessTypes.fetch());
      this.def.push(this.user.fetch());      
  },
  render: function() {
	  var self = this;
      this.def.push(this.layers.fetch());
      $.when.apply($, this.def).then(function () {
    	  var language = self.settings.get('language');
          self.$el.html(self.template({
              layers: self.layers,
              page: self.layers.page,
              user: self.user,
              accessTypes: self.accessTypes,
              columns: {name: 'Name', email: 'Username', createdOn: 'Created On', description: 'Description', accessType: 'Access Type'},
              language:language,
              settings: self.settings
          }));
          self.shareWorkspaceView = new ShareWorkspaceView({
              el: self.$('#workspace-select'),
              workspaceCollection: self.workspaceCollection,
              accessTypes: self.accessTypes
          });
          if(!self.user.get('administratorMode')){
        	  self.$('.set-population-layer-btn').hide();
          } 
          self.translate(self.$el);
          self.$el.find('[data-toggle="tooltip"]').tooltip();
          self.$el.show();
      });
      return this;
  },
  editLayer: function(e) {
      var id = $(e.target).data("id");
      var selectedModel = this.layers.findWhere({id: id});
      this.EventsBus.trigger(Events.UPDATE_LAYER_EVENT, selectedModel);
  },
  createLayer: function() {
	  this.EventsBus.trigger(Events.CREATE_LAYER_EVENT);
  },
  showPopulationLayerView: function(){
	 this.EventsBus.trigger(Events.OPEN_POPULATION_VIEW_EVENT); 
  },
  removeLayer: function(e) {	  
      if(confirm(this.translator.translateSync('amp.gis-layers-manager:Admin-ConfirmDelete','Are you sure you want to delete?'))) {
          var id = $(e.target).data("id");
          var selectedModel = this.layers.findWhere({id: id});
          var self = this;
          selectedModel.deleteLayer({
              success: function() {
                  self.render();
                  self.trigger('removeLayer');
              }
          });          
      }
  },
  changePage: function(e) {
      var currentPage = $(e.target).data("page");
      if(currentPage === '+') {
          currentPage = this.layers.page.get('currentPageNumber') + 1;
      } else if(currentPage === '-') {
          currentPage = this.layers.page.get('currentPageNumber') - 1;
      }
      if(currentPage > 0 && currentPage <= this.layers.page.get('totalPageCount')) {
          this.layers.page.set('currentPageNumber', currentPage);
          this.render();
      }
  },
  makePrivateLayer: function(e) {
	  this.changeStateLayer(e, Constants.AccessType.PRIVATE); // make it private
  },
  makePublicLayer: function(e) {
	  this.changeStateLayer(e, Constants.AccessType.PUBLIC); // make it public
  },
  changeStateLayer: function(e, type) {
      var id = $(e.target).data("id");
      var selectedModel = this.layers.findWhere({id: id});     
      selectedModel.set('accessTypeId', type);      
      var self = this;
      selectedModel.update(function() {
          self.render();
          self.trigger('changeStateLayer');
      });
      
  },
  clickShare: function(e) {
      var id = $(e.target).data("id");
      var selectedModel = this.layers.findWhere({id: id});
      if(this.workspaceCollection.length == 0) {
          var self = this;
          this.workspaceCollection.fetch({
                success: function () {
                    self.showShare(selectedModel);
                }
          });
      } else {
          this.showShare(selectedModel);
      }
  },
  showShare: function (selectedModel) {
      this.shareWorkspaceView.reset();
      this.shareWorkspaceView.setLayer(selectedModel);
      this.shareWorkspaceView.render();
      var self = this;
      this.shareWorkspaceView.on('update', function() {
          self.render();
      });
      this.$('#workspace-select').show();
  },

  sortBy: function (e) {
      var columnHeader = $(e.target);
      var property = columnHeader.data("prop");
      var order = columnHeader.hasClass(Constants.ASC) ? Constants.DESC : columnHeader.hasClass(Constants.DESC) ? Constants.ASC : Constants.DESC;
      this.layers.page.set({
          orderBy: property,
          sort: order
      });
      this.render();
  }

});