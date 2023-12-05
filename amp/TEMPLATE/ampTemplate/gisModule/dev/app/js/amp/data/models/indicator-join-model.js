var $ = require('jquery');
var when = require('jquery').when;
var _ = require('underscore');
var Backbone = require('backbone');
var husl = require('husl');

var TopojsonLibrary = require('../../../libs/local/topojson.js');
var StringUtil = require('../../../libs/local/string-util');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var Palette = require('../../colours/colour-palette');
var IndicatorLayerLocalStorage = require('../indicator-layer-localstorage');

// http://www.w3schools.com/ajax/ajax_xmlhttprequest_onreadystatechange.asp
const readyStateNotInitialized = 0;
const readyStateConnectionEstablished = 1;
const readyStateRequestReceived = 2;
const readyStateProcessingRequest = 3;
const readyStateResponseReady = 4;
const JOIN_BOUNDARIES_PREFIX = 'J';
const EMPTY_JSON_FILE = 'empty.json';
const GAP_ANALYSIS_URL = '/rest/gis/do-gap-analysis';
const INDICATOR_LAYER_URL = '/rest/gis/indicators/';

module.exports = Backbone.Model
.extend(LoadOnceMixin).extend({

  initialize: function() {
    this.listenTo(this, 'change:selected', function(blah, show) {
      this.trigger(show ? 'show' : 'hide', this);
    });
    
    this.listenTo(this, 'change:values', function() {   
    	this.analyzeValues(); 
    	this.updatePaletteRange();
        this.trigger('valuesChanged', this);
     });
    
    this.listenTo(this, 'change:selectedGapAnalysis', function(blah, show) {
        this.trigger('sync', this);
    });
       
    // We listen to the "apply" event on filter widget and trigger a "filter" event that will be listened by our view.
    // This way the view will also receive this model as parameter.
    this.listenTo(app.data.filter, 'apply', function(blah, show) {
        this.trigger('applyFilter', this);
    });
    this.listenTo(app.data.settingsWidget, 'applySettings', function(blah, show) {
        this.trigger('applySettings', this);
    });
    this.initializePalette();        
  },
  initializePalette: function() {
	  var numStops = this.get('classes') || 5;
	  var values = this.get('values') || [];
	  this.palette = new Palette.FromRange({stops: numStops, seed: this.get('id'), values: values, zeroCategoryEnabled: this.get('zeroCategoryEnabled') });

	  if (this.get('colorRamp') && this.get('colorRamp').length > 0) {
		  //use the colors provided, if the colorRamp has multiple colors. By default it uses shades of the same color generated in colour-palette.js
		  if (this.get('isMultiColor') === true && numStops <= this.get('colorRamp').length) {
			  var multiColorSet = [];
			  _.each(this.get('colorRamp'),function(colorRamp) {
				  multiColorSet.push(husl.fromHex(colorRamp.color));
			  });           	
			  this.palette.set('multiColorSet', multiColorSet);         
		  } else {
			  var colorHex = this.get('colorRamp')[0].color; //choose last or first colour from ramp.
			  this.palette.set('rootHue', husl.fromHex(colorHex)[0]);//Math.floor(seedrandom(options.seed)() * 360));
		  }      
	  } 
  },
  loadBoundary: function() {
    // Phil's ideal way of being able to join with non-hosted boundaries.:
    // var boundaryLink = this.get('joinBoundariesLink');  // TODO: handle IDs vs links consitently
    // var boundaryId = boundaryLink.split('gis/boundaries/')[1];  // for now, (for ever?,) they are all local
    // var boundary = this.collection.boundaries.find(function(boundary) { return boundary.id === boundaryId; });

    var boundary = this.collection.boundaries.findWhere({id: this.get('adminLevel')});
    if (!boundary) {  // sanity check
      throw new Error('No boundary found for indicator layer:', this.get('title'));
    }

    var boundaryLoaded = boundary.load();
    when(boundaryLoaded, this.load())         // Order is important...
      .done(function(boundaryModel, self) {  // ...args follow "when" order
        var topoboundaries = boundaryModel.toJSON();
        var topoJsonObjectsIndex = _.chain(topoboundaries.objects)
                                 .keys()
                                 .first()
                                 .value();
        var boundaries = TopojsonLibrary.feature(topoboundaries, topoboundaries.objects[topoJsonObjectsIndex]);
        self._joinDataWithBoundaries(boundaries);               
      });

    return boundaryLoaded;
  },

loadAll: function(options) {
	  if(this.get('type') === 'joinBoundaries' && this.get('colorRamp')){		  	  
		  this.url = INDICATOR_LAYER_URL + this.getId();
	  }else if(this.get('type') === 'Indicator Layers'){
		  this.url = '/rest/gis/indicator-layers/' + this.get('id');
	  }	
	  return when(this.load(options), this.loadBoundary()).promise().done(function() {
		  $('#map-loading').hide();
	  });
  },
  getId: function(){
	  var id = this.get('id');
	  if(typeof this.get('id') === 'string' || this.get('id') instanceof String){
		  id = parseInt(this.get('id').replace( /^\D+/g, ''));
      }	
	  return id
  },
  fetch: function(){	
	  var self = this;
	  var isGapAnalysis = app.mapView.headerGapAnalysisView.model.get('isGapAnalysisSelected');
	  var httpMethod = isGapAnalysis || this.attributes.isStoredInLocalStorage ? 'POST' : 'GET';
	  var settings = app.data.settingsWidget.toAPIFormat();	  
	  var filter = {};
	  
	  if (app.data.filter) {
		  _.extend(filter, app.data.filter.serialize());
	  }	  	  	  
	  
	  if (this.attributes.isStoredInLocalStorage === true) {		  
	     return this._fetchLocalLayer(httpMethod, filter, settings, isGapAnalysis);
	  } else {
		return this._fetchServerLayer(httpMethod, filter, settings, isGapAnalysis);
	  }	  
  },
  _fetchLocalLayer: function(httpMethod, filter, settings, isGapAnalysis) {
	  IndicatorLayerLocalStorage.cleanUp();
	  var layer = IndicatorLayerLocalStorage.findById(this.getId());
	  if (!_.isUndefined(layer)) {
		  IndicatorLayerLocalStorage.updateLastUsedTime(layer);			  
		  var params = {};
		  params.type = httpMethod;
		  if (isGapAnalysis) {
			// If Gap analysis selected we call the EP to reprocess the local data.
			  this.url = GAP_ANALYSIS_URL;
			  params.data = {indicator: layer, settings: settings};
			  params.data = JSON.stringify(_.extend(params.data, filter));
		  } else {
              /* If gap analysis is NOT selected then we use the data from localStorage instead of going to an EP,
              so we set the url to an empty .json file and then the parse function will use the data we already have.*/
              this.url = EMPTY_JSON_FILE;

			  layer.unit = StringUtil.getMultilangString(layer,'unit', app.data.generalSettings); // Needed preprocess for popups.
			  layer.description = StringUtil.getMultilangString(layer,'description', app.data.generalSettings);
			  params.data = JSON.stringify(layer);
		  }
		  this.lastFetchXhr = Backbone.Model.prototype.fetch.call(this, params);
		  return this.lastFetchXhr;
	  } else {
		  console.error('Invalid layer.');
	  }
  },
  _fetchServerLayer: function(httpMethod, filter, settings, isGapAnalysis) {
	   // By adding this section here in fetch we are sure any call made over /rest/indicators/id will have the right parameters without duplicating code.
		if (this.lastFetchXhr && this.lastFetchXhr.readyState > this.readyStateNotInitialized && this.lastFetchXhr.readyState < this.readyStateResponseReady) {
			return this.lastFetchXhr.abort();
		}

		var params = {};
		params.type = httpMethod;
		if (isGapAnalysis) {
			this.url = GAP_ANALYSIS_URL + '/' + this.getId() ;
			params.data = JSON.stringify(filter);
			filter.settings = settings;
		} else {
			this.url = INDICATOR_LAYER_URL + this.getId() ;
		}

	    this.lastFetchXhr = Backbone.Model.prototype.fetch.call(this, params);
	    return this.lastFetchXhr;
  },
  parse: function(response, options){
      /* This is a special case where we dont need to go to the backend but use data we already have. */
      if (options && options.url === EMPTY_JSON_FILE) {
          response = JSON.parse(options.data);
      } else if (!_.isFunction(this.url) &&
          !_.isUndefined(this.url) && this.url.indexOf(INDICATOR_LAYER_URL) !== -1) {
          //if from /rest/gis/indicators/ add prefix to id prevent collision
          response.id = app.constants.JOIN_BOUNDARIES_PREFIX + response.id;
      }
	  if (typeof response.title !== "string") {
          response.title = StringUtil.getMultilangString(response, 'name', app.data.generalSettings);
          response.description = StringUtil.getMultilangString(response, 'description', app.data.generalSettings);
          response.unit = StringUtil.getMultilangString(response, 'unit', app.data.generalSettings);
      }
      return response;
  },
  updatePaletteRange: function() {
    var min = +Infinity,
        max = -Infinity;

    _.each(this.get('values'), function(value) {
      if (value.value < min) {
        min = value.value;
      }
      if (value.value > max) {
        max = value.value;
      }
    });
    this.palette.set({min: min, max: max, values: this.get('values')});
  },
  //check if all values are integers
  _valuesAreIntegers: function(){
	  if(this.get('values')){
		  var integerValues = this.get('values').filter(function(item){
			  return item.value % 1 === 0;
		  });
		  return integerValues.length === this.get('values').length;
	  }
	  return false
   },
   //find max value
   _getMaxValue: function(){
	   var result = -Infinity;
	   if(this.get('values')){
		   var values = _.pluck(this.get('values'),'value');
		   result =  _.max(values);
	   }
	   return result;
   },
   _getMinValue: function(){
	   var result = +Infinity;
	   if(this.get('values')){
		   var values = _.pluck(this.get('values'),'value');
		   result =  _.min(values);
	   }
	   return result;
   },
   analyzeValues: function(){
	   this.maxValue = this._getMaxValue();
   	   this.minValue = this._getMinValue();
   	   this.valuesAreIntegers = this._valuesAreIntegers();
   },
  _joinDataWithBoundaries: function(boundaryGeoJSON) {
    var self = this;
    var values = _.map(this.get('values'), function(value){
    	value.geoId = value.geoId ? $.trim(value.geoId) : value.geoId;
    	return value;
    });

    var indexedValues = _.indexBy(values, 'geoId');
    if(indexedValues["null"]) {
        indexedValues[0] = indexedValues["null"]; //hack for some countries the geoId is null.
    }

    var admKey = this.get('adminLevel').replace('-', '').toUpperCase();

    // copy boundary geoJSON, and inject data
    var geoJSON = _.extend({}, boundaryGeoJSON, {
      features: _.map(boundaryGeoJSON.features, function(feature) {
        // replace boundary properties with {value: value}
        // TODO... keep the existing properties and just add value?
        // replacing for now, to save weight
    	var admCode = feature.properties[admKey + '_CODE'];
    	feature.id = admCode ? $.trim(admCode) : admCode;
        feature.properties.name = feature.properties[admKey + '_NAME'] || '';

        var value = null;
        if (!_.isUndefined(indexedValues[feature.id]) && !_.isNull(indexedValues[feature.id])) {
        	value = indexedValues[feature.id].value;
        }

        return _.extend(feature, {
          properties: _.extend(feature.properties, {
            value: value
          })
        });
      })
    });
    this.set('geoJSON', geoJSON);
  }

});
