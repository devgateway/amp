var _ = require('underscore');
var MessageView = require('../map/views/message-view');
var IndicatorLayerLocalStorage = {};
var HOURS_TO_STORE	 = 12;
var MILLISECONDS_IN_AN_HOUR = (1000 * 60 * 60);
var AMP_INDICATOR_LAYERS = 'AMP_INDICATOR_LAYERS';
IndicatorLayerLocalStorage.findAll = function(){	
	var layersString = localStorage.getItem(AMP_INDICATOR_LAYERS) || '[]';
	var layers = [];
	try{
		layers = JSON.parse(layersString);
    } catch(e){
        console.error(e);
		MessageView.getInstance().trigger('MESSAGE', {
			title: 'Error',
			description: 'Could not find local layers'
		});
    }
    return layers;
};
IndicatorLayerLocalStorage.findById = function(id){
	var layers = IndicatorLayerLocalStorage.findAll();
	var layer = _.findWhere(layers,{id: id});
	return layer;			
};

IndicatorLayerLocalStorage.save = function(layer){
	var layers = IndicatorLayerLocalStorage.findAll();
	var index = _.indexOf(_.pluck(layers, 'id'), layer.id);
	layers[index] = layer;
	localStorage.setItem(AMP_INDICATOR_LAYERS, JSON.stringify(layers));				
};

IndicatorLayerLocalStorage.updateLastUsedTime = function(layer){	
	layer.lastUsed = Date.now();
	IndicatorLayerLocalStorage.save(layer);
};
IndicatorLayerLocalStorage.cleanUp = function(){	
	var now = Date.now();
	var layers = IndicatorLayerLocalStorage.findAll();
	var validLayers = []; //layers used in the last 12 hours
	_.each(layers,function(layer){
		if(layer.lastUsed){
			var hoursStoredSinceLastUse = (now - layer.lastUsed)/MILLISECONDS_IN_AN_HOUR;
			if(hoursStoredSinceLastUse < HOURS_TO_STORE){
				validLayers.push(layer);
			}
		}		
	});	
	localStorage.setItem(AMP_INDICATOR_LAYERS, JSON.stringify(validLayers));
};
module.exports = IndicatorLayerLocalStorage;