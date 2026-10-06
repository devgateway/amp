var _ = require('underscore');
var localStorageService = {};
var AMP_INDICATOR_LAYERS = 'AMP_INDICATOR_LAYERS';

localStorageService.saveLayer = function(layer){
	var layers = _getLayers();
	layers.push(layer);
	localStorage.setItem(AMP_INDICATOR_LAYERS, JSON.stringify(layers));
};

function _getLayers(){
	var layersString = localStorage.getItem(AMP_INDICATOR_LAYERS) || '[]';
	var layers = [];
	try{
		layers = JSON.parse(layersString);
    }catch(e){
        console.error(e);
    }
    return layers;
		
};

localStorageService.getAll = function(){
	return _getLayers();
};

module.exports = localStorageService;


