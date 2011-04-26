
dojo.require("dijit.dijit"); // optimize: load dijit layer
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("esri.map");
dojo.require("esri.layers.FeatureLayer");
dojo.require("dojox.grid.DataGrid");
dojo.require("dojo.data.ItemFileReadStore");
dojo.require("esri.toolbars.navigation");
dojo.require("dijit.form.Button");
dojo.require("dijit.Toolbar");
dojo.require("esri.tasks.find");
dojo.require("esri.tasks.geometry");



var map, navToolbar,geometryService;
function init() {
	//This have to be replaced with Global Settings values
	var basemapUrl = "http://4.79.228.117:8399/arcgis/rest/services/World_Physical_Map/MapServer";
	var mapurl = "http://4.79.228.117:8399/arcgis/rest/services/Liberia/MapServer";
	var basemap = new esri.layers.ArcGISTiledMapServiceLayer(basemapUrl, {opacity : 0.90}); // Levels at which this layer will be visible);
	liberiamap = new esri.layers.ArcGISDynamicMapServiceLayer(mapurl, {opacity : 0.50});

	//Geometry service for operations with graphics
	geometryService = new esri.tasks.GeometryService("http://4.79.228.117:8399/arcgis/rest/services/Geometry/GeometryServer");
	
	var layerLoadCount = 0;
	if (basemap.loaded) {
		layerLoadCount += 1;
		if (layerLoadCount === 2) {
			createMapAddLayers(basemap, liberiamap);
		}
	} else {
		dojo.connect(basemap, "onLoad", function(service) {
			layerLoadCount += 1;
			if (layerLoadCount === 2) {
				createMapAddLayers(basemap, liberiamap);
			}
		});
	}
	if (liberiamap.loaded) {
		layerLoadCount += 1;
		if (layerLoadCount === 2) {
			createMapAddLayers(basemap, liberiamap);
		}
	} else {
		dojo.connect(liberiamap, "onLoad", function(service) {
			layerLoadCount += 1;
			if (layerLoadCount === 2) {
				createMapAddLayers(basemap, liberiamap);
			}
		});
	}

}

// Create a map, set the extent, and add the services to the map.
function createMapAddLayers(myService1, myService2) {
	// create map
	// convert the extent to Web Mercator
	map = new esri.Map("map", {
		extent : esri.geometry.geographicToWebMercator(myService2.fullExtent)
	});

	dojo.connect(map, 'onLoad', function(map) {
		dojo.connect(dijit.byId('map'), 'resize', resizeMap);
	});

	map.addLayer(myService1);
	map.addLayer(myService2);
	navToolbar = new esri.toolbars.Navigation(map);
	dojo.connect(navToolbar, "onExtentHistoryChange",extentHistoryChangeHandler);
}

function extentHistoryChangeHandler() {
	dijit.byId("zoomprev").disabled = navToolbar.isFirstExtent();
	dijit.byId("zoomnext").disabled = navToolbar.isLastExtent();
}

function resizeMap() {
	// resize the map when the browser resizes - view the 'Resizing and
	// repositioning the map' section in
	// the following help topic for more details
	// http://help.esri.com/EN/webapi/javascript/arcgis/help/jshelp_start.htm#jshelp/inside_faq.htm
	var resizeTimer;
	clearTimeout(resizeTimer);
	resizeTimer = setTimeout(function() {
		map.resize();
		map.reposition();
	}, 500);
}
// show map on load
dojo.addOnLoad(init);

function getContent() {
	var xhrArgs = {
		url : "/esrigis/maphelper.do?showactivities=true",
		handleAs : "json",
		   load: function(jsonData) {
		        // For every item we received...
		        dojo.forEach(jsonData,function(activity) {
		        	MapFind(activity);
		        });
		    },
		error : function(error) {
			// Error hanlder
		}
	}

	// Call the asynchronous xhrGet
	var deferred = dojo.xhrGet(xhrArgs);
}
function MapFind(activity){
	
    findTask = new esri.tasks.FindTask("http://4.79.228.117:8399/arcgis/rest/services/Liberia/MapServer");
    //create find parameters and define known values
    findParams = new esri.tasks.FindParameters();
    findParams.returnGeometry = true;
    findParams.layerIds = [0,1];
    findParams.searchFields = ["FIRST_FIRS"];
    dojo.forEach(activity.locations,function(location) {
    	execute(location.name);
    });
}

function execute(searchText) {
    //set the search text to find parameters
    findParams.searchText = searchText;
    findTask.execute(findParams, showResults);
  }

function showResults(results) {
    //symbology for graphics
    var markerSymbol = new esri.symbol.SimpleMarkerSymbol(esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE, 10, new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([255, 0, 0]), 1), new dojo.Color([0, 255, 0, 0.25]));
    var lineSymbol = new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_DASH, new dojo.Color([255, 0, 0]), 1);
    var polygonSymbol = new esri.symbol.SimpleFillSymbol(esri.symbol.SimpleFillSymbol.STYLE_NONE, new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_DASHDOT, new dojo.Color([255, 0, 0]), 2), new dojo.Color([255, 255, 0, 0.25]));

    //find results return an array of findResult.
    
    dojo.forEach(results, function(result) {
      var graphic = result.feature;
      geometryService.simplify([graphic.geometry ],scallback);
     // var pgraphic = new esri.Graphic(point, markerSymbol);
      //map.graphics.add(pgraphic);
    });
}

function scallback(geometries) {
    alert("Number of Rings returned by Simplify operation = " + geometries[0].rings.length);
  }  