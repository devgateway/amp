var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/../templates/population-layer-template.html', 'utf8');
var Events = require('../utils/events.js');
var MessageUtils = require('../utils/message-util');
var LayerIndicatorCollection = require('../collections/layers-collections');
var ADMCollection = require('../collections/adm-levels-collection.js');
var Constants = require('../utils/constants');
module.exports = Backbone.View.extend({
  id: 'id-population-layer',
  events: {     
      'click .btn-save': 'saveAndClose',
      'click .btn-close': 'close'
  },
  template: _.template(Template),  
  initialize:function(options) {
	  this.EventsBus = options.EventsBus;
	  this.translate = options.translate;
	  this.translator = options.translator;	  
	  this.settings = options.settings;
	  _.bindAll(this, 'render','saveAndClose','close');
	  var self = this;
	  this.loadData().then(function() {
		  self.render();
	  });
  },
  render: function() {
      this.$el.html(this.template({layers: this.layers,admLevels: this.admLevels,validLayerIds: this.validLayerIds, settings: this.settings}));
      this.$el.hide();
      this.translate(self.$el);
      this.$el.show();
      return this;
  },
  loadData: function(){
	  var deffered = [];	  
	  this.admLevels = new ADMCollection();
	  this.layers = new LayerIndicatorCollection();
	  this.layers.page.set('recordsPerPage', Constants.POPULATION_LAYERS_PER_PAGE);	  
	  deffered.push(this.layers.fetch());
	  deffered.push(this.fetchValidLayerIds());
	  deffered.push(this.admLevels.fetch());
	  return $.when.apply($,deffered);
  },  
  fetchValidLayerIds: function(){
	  var self = this;
	  return $.ajax({
	         url : '/rest/indicator/population-layers-options',
	         type : 'GET',	         
	         success : function(data) {	        	 
	        	 self.validLayerIds = data; 
	         },
	         error: function(data){        	 
	         }
	  });  
  },
  saveAndClose: function () {
	  var self  = this;
	  var selectedIds = [];
	  _.each(this.$el.find('.adm-level'),function(adm){
		  var value  = $(adm).val();
		  if(!_.isNull(value) && value != 'null'){
		      selectedIds.push(parseInt($(adm).val()));
	      }		  
	  });
	  var dataToSubmit = {'layersIds':selectedIds};
	  $.ajax({
			 url : '/rest/indicator/population-layers',
			 type : 'POST',
			 data: JSON.stringify(dataToSubmit),
			 contentType: "application/json; charset=utf-8",
			 success : function(data) {
				 MessageUtils.displaySuccessMessage(self.$el, self.translator.translateSync('amp.gis-layers-manager:PopulationLayer-SaveSuccessful','Population layers set successfully'));				 
			 },
			 error: function(data){				 
				 MessageUtils.displayErrors(self.$el, [self.translator.translateSync('amp.gis-layers-manager:PopulationLayer-ErrorSaving','A server error occurred while setting the population layers')]);
			 }
	  });


  },
  close: function () {
	  this.EventsBus.trigger(Events.POPULATION_VIEW_CLOSED_EVENT);      
  }

});