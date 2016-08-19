var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var JoinIndicator = require('../models/indicator-join-model');
var ArcGISDynamicLayerIndicator = require('../models/indicator-arcgis-dynamicLayer-model');
var WMSIndicator = require('../models/indicator-wms-model');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var IndicatorLayerLocalStorage = require('../indicator-layer-localstorage');

/* Backbone Collection IndicatorLayers (RENAME FILE) */
module.exports = Backbone.Collection
.extend(LoadOnceMixin).extend({

  url: '/rest/gis/indicator-layers',

  model: function(attrs) {
    var typeName = attrs.type;

    switch (typeName) {
      case 'joinBoundaries':
        return new JoinIndicator(attrs);
      case 'wms':
        return new WMSIndicator(attrs);
      case 'arcgis':
      case 'Indicator Layers':
        return new ArcGISDynamicLayerIndicator(attrs);
      default:
        console.error('Unrecognized indicator type. Check parse function.: ' + attrs.type);
        return new Backbone.Model();
    }
  },

  initialize: function(models, options) {
    this.boundaries = options.boundaries;
    this.settings = options.settings;
  },

  loadAll: function() {
    var self = this;
    var deferred = $.Deferred();
    this.load().then(function() {
      self.url = '/rest/gis/indicators';
      self.fetch().then(function() {
        deferred.resolve();
      });
    });

    return deferred;
  },
  loadFromLocalStorage: function(data){	 
	  if(this.url === '/rest/gis/indicators'){
		  IndicatorLayerLocalStorage.cleanUp();
		  var layers = IndicatorLayerLocalStorage.findAll();	  
		  layers.forEach(function(localLayer){			  
			  data.push(localLayer);
		  });
	  }	  
  },
  parse: function(data) {	
    var self = this;
    self.loadFromLocalStorage(data);
    var parsedData = data;
    parsedData = _.filter(data, function(layer) {
      switch (layer.type) {
        case 'joinBoundaries':
        case 'wms':
        case 'arcgis':
        case 'Indicator Layers':
          return true;
        default:
      }

      // this is a custom one. API is a bit messy so we do fair bit of manual work.
      if (layer.colorRamp) {
    	 self.settings.load().then(function() {
    	    
    	   layer.title = self.getMultilangString(layer,'name');
    	   layer.description = self.getMultilangString(layer,'description');  
    	   layer.unit = self.getMultilangString(layer,'unit');
    	 });   	 
        layer.type = 'joinBoundaries';
        layer.adminLevel = self._magicConversion(layer.admLevelId);
        layer.tooltip = self._createTooltip(layer); 
        layer.classes = layer.numberOfClasses;
        return true;
      }

      return false;
    });

    return parsedData;
  },
  getMultilangString: function(layer,field){  
	  var currentLanguage = this.settings.findWhere({id:'language'}).get('defaultId');
	  var defaultLanguage = this.settings.findWhere({id: 'default-language'}).get('defaultId');	    
	  var result = '';
	  if(!_.isUndefined(layer[field])){
		  result = layer[field][currentLanguage];
		  if(_.isUndefined(result) || _.isNull(result)){
			  result = layer[field][defaultLanguage] || '';
		  } 
	  }	 
	  return result;  	
  },
  getSelected: function() {
    return this.chain()
      .filter(function(model) { return model.get('selected'); });
  },

  _magicConversion: function(textAdm) {
    var magicWords = {
      Country: 'adm-0',
      Region: 'adm-1',
      Zone: 'adm-2',
      District: 'adm-3'
    };

    return magicWords[textAdm];
  },
  _createTooltip: function(obj){
	     var tooltip  = '';
	     if(obj.description){
	       tooltip += obj.description + '. ' ;
	     }
	     
	     if(obj.createdOn){
	      tooltip += '&#013; Created on ' + obj.createdOn + '. ';
	     }
	     if(obj.createdBy){
		      tooltip += '&#013; Created by ' + obj.createdBy + '. ';
		 }
	     return tooltip;
}

});
