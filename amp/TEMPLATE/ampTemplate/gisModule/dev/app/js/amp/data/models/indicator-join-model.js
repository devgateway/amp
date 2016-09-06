var $ = require('jquery');
var when = require('jquery').when;
var _ = require('underscore');
var Backbone = require('backbone');
var husl = require('husl');

var TopojsonLibrary = require('../../../libs/local/topojson.js');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var Palette = require('../../colours/colour-palette');
var IndicatorLayerLocalStorage = require('../indicator-layer-localstorage');

// http://www.w3schools.com/ajax/ajax_xmlhttprequest_onreadystatechange.asp
const readyStateNotInitialized = 0;
const readyStateConnectionEstablished = 1;
const readyStateRequestReceived = 2;
const readyStateProcessingRequest = 3;
const readyStateResponseReady = 4;

module.exports = Backbone.Model
.extend(LoadOnceMixin).extend({

  initialize: function() {
    this.listenTo(this, 'change:selected', function(blah, show) {
      this.trigger(show ? 'show' : 'hide', this);
    });
    
    this.listenTo(this, 'change:selectedGapAnalysis', function(blah, show) {
        this.trigger('sync', this);
    });
       
    // We listen to the "apply" event on filter widget and trigger a "filter" event that will be listened by our view.
    // This way the view will also receive this model as parameter.
    this.listenTo(app.data.filter, 'apply', function(blah, show) {
        this.trigger('applyFilter', this);
    });
    this.listenTo(app.data.settings, 'applySettings', function(blah, show) {
        this.trigger('applySettings', this);
    });

    var numStops = this.get('classes') || 5;
    var values = this.get('values') || [];

    this.palette = new Palette.FromRange({stops: numStops, seed: this.get('id'), values: values });

    // set color based on ramp, if one is provided.
    if (this.get('colorRamp') && this.get('colorRamp').length > 0) {
      var colorHex = this.get('colorRamp')[0].color; //choose last or first colour from ramp.
      this.palette.set('rootHue', husl.fromHex(colorHex)[0]);//Math.floor(seedrandom(options.seed)() * 360));
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
        self.updatePaletteRange();

        self._joinDataWithBoundaries(boundaries);
      });

    return boundaryLoaded;
  },

  loadAll: function(options) {
    return when(this.load(options), this.loadBoundary()).promise().done(function() {
      $('#map-loading').hide();
    });
  },
  
  fetch: function(){	
	  console.log('fetch');
	  var self = this;
	  	  
	  var filter = {otherFilters: {}};
	  if (app.data.filter) {
		  _.extend(filter, app.data.filter.serialize());
	  }
	  var settings = app.data.settings.serialize();	  	  
	  
	  if(this.attributes.isStoredInLocalStorage === true){		  
		  this.deferredForLocalStorage = new $.Deferred();
		  IndicatorLayerLocalStorage.cleanUp();
		  //debugger
		  var layer = IndicatorLayerLocalStorage.findById(this.attributes.id);
		  if(!_.isUndefined(layer)){
			  IndicatorLayerLocalStorage.updateLastUsedTime(layer);
			  // If Gap analysis selected we call the EP to reprocess the local data.
			  if (app.mapView.headerGapAnalysisView.model.get('isGapAnalysisSelected')) {
				  console.log('resolve con gap');
				  this.url = '/rest/gis/do-gap-analysis';
				  var params = {};
				  params.type = 'POST';
				  params.data = {indicator: layer, settings: settings, isGapAnalysis: true};
				  params.data = JSON.stringify(_.extend(params.data, filter));
				  this.lastFetchXhr = Backbone.Model.prototype.fetch.call(this, params);
				  //debugger;
				  return this.lastFetchXhr;				  
			  } else {				  
				  console.log('resolve sin gap');
				  // esto anda pero no recarga el localstorage.
				  /*layer._changing = true;
				  console.log(layer);
				  var test=this.deferredForLocalStorage.resolve(layer);
				  debugger;
				  return test*/
				  
				  // esto anda pero no me gusta porque es ir al backend.
				  /*this.url = '/rest/gis/do-gap-analysis';
				  var params = {};
				  params.type = 'POST';
				  params.data = {indicator: layer, settings: settings, isGapAnalysis: false};
				  params.data = JSON.stringify(_.extend(params.data, filter));
				  this.lastFetchXhr = Backbone.Model.prototype.fetch.call(this, params);
				  return this.lastFetchXhr;*/
				  
				  // esto carga bien el layer la primera vez pero sigue sin volver a mostrar el localstorage luego del gap.
				  /*var ajax = $.ajax({
					  url: "/",
					}).done(function() {
					    return layer;
					});
				  debugger
				  return ajax;*/
				  
				  // esto tambien carga el layer la primera vez!!!
				  /*this.url = "/rest/gis/indicators"
				  this.lastFetchXhr = Backbone.Model.prototype.fetch.call(this, params);
				  this.on('sync', function() {
					  console.log('listo el ep falso');
					  return layer;
				  }, this);
				  //debugger;
				  return this.lastFetchXhr;*/
				  
				  layer._changing = true;
				  console.log(layer);
				  var test=this.deferredForLocalStorage.resolveWith(this, [layer]);
				  //debugger;
				  return test;
			  }			  
		  } else {
			  alert('problema');
		  }
		  //console.log('devuelvo promise');
		  //return this.deferredForLocalStorage.resolve(layer);
		  //return this.deferredForLocalStorage.promise();
	  } else {
		// By adding this section here in fetch we are sure any call made over /rest/indicators/id will have the right parameters without duplicating code.  
		if (this.lastFetchXhr && this.lastFetchXhr.readyState > this.readyStateNotInitialized && this.lastFetchXhr.readyState < this.readyStateResponseReady) {
			alert('abort');
			return this.lastFetchXhr.abort();
		}
		filter.gapAnalysis = app.mapView.headerGapAnalysisView.model.get('isGapAnalysisSelected');
		filter.settings = settings;
		var params = {};
	    params.type = 'POST';
	    params.data = JSON.stringify(filter);
	    // "params" will set the right type + filters + settings + gap analysis.
	    this.lastFetchXhr = Backbone.Model.prototype.fetch.call(this, params);
	    return this.lastFetchXhr;
	  }	  
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

  _joinDataWithBoundaries: function(boundaryGeoJSON) {
    var self = this;
    var indexedValues = _.indexBy(this.get('values'), 'geoId');
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
        feature.id = feature.properties[admKey + '_CODE'];
        feature.properties.name = feature.properties[admKey + '_NAME'] || '';

        if (!indexedValues[feature.id]) {
          indexedValues[feature.id] = {value: 0};
          self.palette.set({min: 0});
        }

        return _.extend(feature, {
          properties: _.extend(feature.properties, {
            value: indexedValues[feature.id].value
          })
        });
      })
    });

    this.set('geoJSON', geoJSON);
  }

});
