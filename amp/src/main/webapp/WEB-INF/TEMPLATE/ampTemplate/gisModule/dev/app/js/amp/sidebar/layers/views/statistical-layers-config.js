var _ = require('underscore');

var layerTypes = {
		JOIN_BOUNDARIES : 'joinBoundaries',
		WMS: 'wms',
		ARCGIS: 'arcgis',
		INDICATOR_LAYERS: 'Indicator Layers'
};

var accessTypes = {
		'PUBLIC_STORED_IN_BROWSER': 0,
		'PRIVATE': 1,
		'PUBLIC': 2,
		'STANDARD': 3,
		'SHARED': 4
};

module.exports = {
	MY_LAYERS:{
		id: 'tool-my-layers-indicators',
		title: 'My Layers',
		iconClass: 'ampicon-layers',
		description: 'View layers created by current user',
		filterLayers:function(layers){	
			
			return layers.models.filter(function(layer){				
				return layer.attributes.type === layerTypes.JOIN_BOUNDARIES && (layer.attributes.accessTypeId === accessTypes.PRIVATE || layer.attributes.accessTypeId === accessTypes.PUBLIC_STORED_IN_BROWSER);
				
			});				
		}
		
	},
	STANDARD:{
		id: 'tool-standard-layers-indicators',
		title: 'Standard Layers',
		iconClass: 'ampicon-layers',
		description: 'View indicators on sub-national needs.',
		filterLayers:function(layers){
			return layers.models.filter(function(layer){				
				return (layer.attributes.type === layerTypes.INDICATOR_LAYERS || 
				layer.attributes.type === layerTypes.WMS ||
				layer.attributes.type === layerTypes.ARCGIS || 
				layer.attributes.accessTypeId == accessTypes.STANDARD);
				
			});		
			
		}
		
	},
	SHARED:{
		id: 'tool-shared-layers-indicators',
		title: 'Shared Layers',
		iconClass: 'ampicon-layers',
		description: 'View shared layers',
		filterLayers:function(layers){			
			return layers.models.filter(function(layer){ 
				return layer.attributes.type === layerTypes.JOIN_BOUNDARIES && (layer.attributes.accessTypeId == accessTypes.PUBLIC || layer.attributes.accessTypeId == accessTypes.SHARED);				
			});	
						
		}
	}	
};