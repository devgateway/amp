var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var JoinIndicator = require('../models/indicator-join-model');
var ArcGISDynamicLayerIndicator = require('../models/indicator-arcgis-dynamicLayer-model');
var WMSIndicator = require('../models/indicator-wms-model');
var LoadOnceMixin = require('../../mixins/load-once-mixin');

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
      self.fetch({remove: false}).then(function() {
        deferred.resolve();
      });
    });

    return deferred;
  },

  parse: function(data) {
    var self = this;
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
    	   var language = self.settings.findWhere({id:'language'}).get('defaultId'); 
    	   layer.title = self.getMultilangString(layer,'name', language);
    	   layer.description = self.getMultilangString(layer,'description', language);    	   
    	 });   	 
        layer.type = 'joinBoundaries';
        layer.adminLevel = self._magicConversion(layer.admLevelId);
        layer.tooltip = self._createTooltip(layer);
        return true;
      }

      return false;
    });

    return parsedData;
  },
  getMultilangString: function(layer,field,language){
	  var result = '';
	  if(layer[field] === Object(layer[field]))
	  {
		  result = layer[field][language];
		  if(_.isUndefined(result) || _.isNull(result)){
			  _.each(_.keys(layer[field]),function(lang){
				  if(layer[field][lang]){
					  result = layer[field][lang] || '';  
				  }
				  
			  });
			   
		  }    		  
	  }else{
		  result = layer[field] || '';
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
	       tooltip += obj.description ;
	     }
	     if(obj.createdBy){
	      tooltip += '&#013; Created by ' + obj.createdBy;
	     }
	     if(obj.createdOn){
	      tooltip += '&#013; Created on ' + obj.createdOn;
	     }
	     return tooltip;
  }

});
