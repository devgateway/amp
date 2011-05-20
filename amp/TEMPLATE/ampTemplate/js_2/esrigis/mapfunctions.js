dojo.require("dijit.dijit"); // Optimize: load dijit layer
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
dojo.require("esri.dijit.BasemapGallery");
dojo.require("esri.arcgis.utils");
dojo.require("dijit.TitlePane");

var map, navToolbar,geometryService,findTask,findParams;
var totallocations = 0;
var features = new Array();
var timer_on = 0;
var activitiesarray = new Array();
var loading;
var cL;
var basemapGallery;

function init() {
	//This have to be replaced with Global Settings values
	loading = dojo.byId("loadingImg");
	var basemapUrl = "http://4.79.228.117:8399/arcgis/rest/services/World_Physical_Map/MapServer";
	var mapurl = "http://4.79.228.117:8399/arcgis/rest/services/Liberia_Map_Test/MapServer";
	var indicatorurl = "http://4.79.228.117:8399/arcgis/rest/services/Liberia_Pop_Density_and_Poverty/MapServer";
	var basemap = new esri.layers.ArcGISTiledMapServiceLayer(basemapUrl, {opacity : 0.90}); // Levels at which this layer will be visible);
	liberiamap = new esri.layers.ArcGISDynamicMapServiceLayer(mapurl, {opacity : 0.90});
	povertymap = new esri.layers.ArcGISDynamicMapServiceLayer(indicatorurl, {opacity : 0.40});
	
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
		//dojo.connect(map, "onMouseMove", showCoordinates);
		//dojo.connect(map, "onMouseDrag", showCoordinates);
		//dojo.connect(map,"onUpdateStart",showLoading);
		//dojo.connect(map,"onUpdateEnd",hideLoading);
        dojo.byId('map_zoom_slider').style.top = '80px';
        getActivities();
    });
	map.addLayer(myService1);
	map.addLayer(myService2);
	//map.addLayer(povertymap);
	navToolbar = new esri.toolbars.Navigation(map);
	dojo.connect(navToolbar, "onExtentHistoryChange",extentHistoryChangeHandler);
	
	createBasemapGalleryEsri();
	createBasemapGallery();
}

function toggle(id) {
	  var layer = map.getLayer(id);
	  if (layer.visible) {
	    layer.hide();
	  } else {
	    layer.show();
	  }
	}

function showLoading() {
    esri.show(loading);
    map.disableMapNavigation();
    map.hideZoomSlider();
  }

  function hideLoading(error) {
    esri.hide(loading);
    map.enableMapNavigation();
    map.showZoomSlider();
  
  }

function showCoordinates(evt) {
    //get mapPoint from event
    //The map is in web mercator - modify the map point to display the results in geographic
    var mp = esri.geometry.webMercatorToGeographic(evt.mapPoint);
    //display mouse coordinates
    //console.log(mp.x + ", " + mp.y);
     
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

function getActivities(clear) {
	if (clear){
		cL.clear();
	}
	var xhrArgs = {
		url : "/esrigis/datadispatcher.do?showactivities=true",
		handleAs : "json",
		   load: function(jsonData) {
		        // For every item we received...
			   totallocations=0;
		       features = []; 
			   dojo.forEach(jsonData,function(activity) {
		        	activitiesarray.push(activity);
		        	MapFind(activity);
		        });
			   if (totallocations==0){
					timer_on = 1;
					drawpoints();
				}
		   },
		error : function(error) {
			console.log(error);
		}
	}
	// Call the asynchronous xhrGet
	var deferred = dojo.xhrGet(xhrArgs);
	showLoading();
}

function MapFind(activity){
	dojo.forEach(activity.locations,function(location) {
    	//If the location has lat and lon not needs to find the point in the map
		if (location.islocated==false){
    		findTask = new esri.tasks.FindTask("http://4.79.228.117:8399/arcgis/rest/services/Liberia/MapServer");
    	    //create find parameters and define known values
    	    findParams = new esri.tasks.FindParameters();
    	    findParams.returnGeometry = true;
    	    findParams.layerIds = [0,1];
    	    findParams.contains = false;
    	    //TODO:Configure the search field in global settings
    	    findParams.searchFields = ["GEO_ID"];
    		execute(location.geoId);
    		totallocations ++;
    	}else{
    		//Create a graphic point based on the x y coordinates wkid(Well-known ID) 4326 for GCS_WGS_1984 projection
    		var pt = new esri.geometry.Point(location.lat,location.lon,new esri.SpatialReference({"wkid":4326}));
    		var sms = new esri.symbol.SimpleMarkerSymbol().setStyle(esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE).setColor(new dojo.Color([255,0,0,0.5]));
    		var attr = {"Temp":"Temporal Attribute"};
    		var infoTemplate = new esri.InfoTemplate("");   
    		var pgraphic = new esri.Graphic(pt,sms,attr,infoTemplate);
    		var exit = false;
    		var primarysector;
    		var primarysectorschema;
    		var primarypercentage;
    		
    		dojo.forEach(activity.sectors,function(sector) {
    			primarysector = sector.sectorName;
    			primarysectorschema = sector.sectorScheme;
    			primarypercentage = sector.sectorPercentage;
    		});
    		
    		pgraphic.setAttributes( {
    			  "Activity":'<b>'+activity.activityname+'</b>',
    			  "Activity Preview":'<a href="/aim/selectActivityTabs.do~ampActivityId='+activity.id+'" target="_blank">Click here</a>',	
    			  "AMP Activity ID":activity.ampactivityid,	
    			  "Activity commitments":'<b>'+activity.commitments+'</b>',
  				  "Activity disbursements":'<b>'+activity.disbursements+'</b>',
  				  "Activity expenditures":'<b>'+activity.expenditures+'</b>',
  				  "Location":'<b>'+location.name+'</b>',
  				  "Location commitments":'<b>'+location.commitments+'</b>',
  				  "Location disbursements":'<b>'+location.disbursements+'</b>',
  				  "Location expenditures":'<b>'+location.expenditures+'</b>',
  				  "Location percentage":location.percentage,
  				  "Primary Sector":'<b>'+primarysector+'</b>',
  				  "Primary Schema":primarysectorschema,
  				  "Primary Percentage":primarypercentage
  				  });
  			location.isdisplayed = true;
  			features.push(pgraphic);
    	}
    });
}

function execute(searchText) {
    //set the search text to find parameters
    findParams.searchText = searchText;
    findTask.execute(findParams, showResults);
    var t=setTimeout("drawpoints()",10000);
    timer_on = 1;
  }


function showResults(results) {
    //find results return an array of findResult.
    dojo.forEach(results, function(result) {
      var graphic = result.feature;
      console.log("Found : " + result.layerName + "," + result.foundFieldName + "," + result.value);
      var point =  graphic.geometry.getExtent().getCenter();
      var sms = new esri.symbol.SimpleMarkerSymbol().setStyle(esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE).setColor(new dojo.Color([255,0,0,0.5]));
      var attr = {"Location":result.value};
      var infoTemplate = new esri.InfoTemplate("");   
      var pgraphic = new esri.Graphic(point,sms,attr,infoTemplate);
      
      //Iterate over the activities array and assign the attributes to each point
      var exit = false;
      for ( var int = 0; int < activitiesarray.length; int++) {
    	var activity = activitiesarray[int];
    	for ( var int2 = 0; int2 < activitiesarray[int].locations.length; int2++) {
			var loc = activitiesarray[int].locations[int2];
			if (loc.name==result.value && loc.isdisplayed!=true){
  			  pgraphic.setAttributes( {
  				  "Activity":activity.activityname,
  				  "Location":loc.name});
  			  loc.isdisplayed = true;
  			  exit = true;
  			}
		}
    	if (exit==true){
			break;
		}
      }
      features.push(pgraphic);
  	  totallocations--;

  	  if (totallocations == 0){
  		  drawpoints();
  	  }
  	});
}

function drawpoints(){
	if (timer_on){
	 hideLoading();
	  	cL = new esri.ux.layers.ClusterLayer({
			displayOnPan: false,
			map: map,
			id: "activitiesMap",
			features: features,
			infoWindow: {
				template: new esri.InfoTemplate("Activity Details", "${*}"),
			width: 300,
			height: 300
		},
		flareLimit: 15,
		flareDistanceFromCenter: 20
	});
	map.addLayer(cL);
	timer_on=0;
	}
}

//FFerreyra Functions
var locations = new Array();
var implementationLevel = [{"name": "Region", "mapId": "0", "mapField": "COUNTY"},
                           {"name": "Zone", "mapId": "1", "mapField": "DISTRICT"}
                          ];
function getHighlights(level) {
	closeHide();
	var xhrArgs = {
			url : "/esrigis/datadispatcher.do?showhighlights=true&level=" + implementationLevel[level].name,
			handleAs : "json",
			   load: function(jsonData) {
			        // For every item we received...
			        dojo.forEach(jsonData,function(location) {
			        	locations.push(location);
			        });
		        	MapFindLocation(implementationLevel[level]);
			    },
			error : function(error) {
				// Error handler
			}
		};
	// Call the asynchronous xhrGet
	var deferred = dojo.xhrGet(xhrArgs);
}

function closeHide(){
	$('#legenddiv').hide('slow');
	try{
		map.removeLayer(map.getLayer("highlightMap"));
	}
	catch(e){}
//	cL.clear();
}

function MapFindLocation(level){
	showLoading();
	var queryTask = new esri.tasks.QueryTask("http://4.79.228.117:8399/arcgis/rest/services/Liberia/MapServer/" + level.mapId);
    var query = new esri.tasks.Query();
    query.where = level.mapField + " <> ''";
    query.outSpatialReference = {wkid:map.spatialReference.wkid};
    query.returnGeometry = true;
    query.outFields = ["COUNT", level.mapField, "GEO_ID"];
    queryTask.execute(query, addResultsToMap);
}

function addResultsToMap(featureSet) {
    var border = new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([150,150,150]), 1);
    var symbol = new esri.symbol.SimpleFillSymbol(esri.symbol.SimpleFillSymbol.STYLE_SOLID, border, new dojo.Color([150, 150, 150, 0.5]));
    var colors = colorsOrange;
    var numRanges = colors.length;
    var localGraphicLayer = esri.layers.GraphicsLayer({displayOnPan: true, id: "highlightMap", visible: true});

    //Using logarithmic scale
    var maxLog = Math.log(getMaxValue(locations, "commitments"));
    var minLog = Math.log(getMinValue(locations, "commitments"));

    var max = getMaxValue(locations, "commitments");
    var min = getMinValue(locations, "commitments");

    var breaksLog = (maxLog - minLog) / numRanges;
    var breaks = (max - min) / numRanges;

    var rangeColors = new Array();
    var renderer = new esri.renderer.ClassBreaksRenderer(symbol, "COUNT");
    for (var i=0; i<numRanges; i++) {
    	rangeColors.push([parseFloat(min + (i*breaks)), parseFloat(min + ((i+1)*breaks))]);
        renderer.addBreak(parseFloat(minLog + (i*breaksLog)),
                parseFloat(minLog + ((i+1)*breaksLog)),
                new esri.symbol.SimpleFillSymbol(esri.symbol.SimpleFillSymbol.STYLE_SOLID, border, colors[i]));
      }

    dojo.forEach(featureSet.features,function(feature){
    	localGraphicLayer.add(feature);
    });
    
    localGraphicLayer = updateLocationAttributes(localGraphicLayer);
    localGraphicLayer.setRenderer(renderer);
    map.addLayer(localGraphicLayer);
    map.reorderLayer(map.getLayer("highlightMap"),0);
    map.setExtent(map.extent.expand(1.01));
    hideLoading();
    showLegend(rangeColors, colors);
  }

function showLegend(rangeColors, colors){
	var htmlDiv = "";
	for(var i=0; i< rangeColors.length; i++){
		htmlDiv += "<div class='legendContentValue' style='background-color:rgba(" + colors[i].toRgba() + ");' ></div>"
				+ "<div class='legendContentLabel'>" + Math.ceil(rangeColors[i][0]) + "-" + Math.floor(rangeColors[i][1]) + "</div><br/>";
	}
	htmlDiv += "<div onclick='closeHide()' style='color:white;'>Close</div>";
	$('#legenddiv').html(htmlDiv);
	$('#legenddiv').show('slow');
}

var colorsBlue = [
		new dojo.Color([ 222, 235, 247, 0.7]),
		new dojo.Color([ 198, 219, 239, 0.7]),
		new dojo.Color([ 158, 202, 225, 0.7]),
		new dojo.Color([ 107, 174, 214, 0.7]),
		new dojo.Color([ 66, 146, 198, 0.7]),
		new dojo.Color([ 33, 113, 181, 0.7]),
		new dojo.Color([ 8, 81, 156, 0.7]),
		new dojo.Color([ 8, 48, 107, 0.7])];

var colorsOrange = [
		new dojo.Color([255, 255, 229, 0.8]),
		new dojo.Color([255, 247, 188, 0.8]),
		new dojo.Color([254, 227, 145, 0.8]), 
		new dojo.Color([254, 196, 79, 0.8]), 
		new dojo.Color([254, 153, 41, 0.8]), 
		new dojo.Color([236, 112, 20, 0.8]), 
		new dojo.Color([204, 76, 2, 0.8]), 
		new dojo.Color([153, 52, 4, 0.8]), 
		new dojo.Color([102, 37, 6 , 0.8])];


function getMaxValue(array, measure){
	var maxValue = 0;
	for(var i=0; i < array.length; i++){
		var currentMeasure = parseFloat(array[i][measure]);
		if(currentMeasure > maxValue)
			maxValue = currentMeasure; 
	}
	return maxValue+10;
}

function getMinValue(array, measure){
	var minValue = 0;
	for(var i=0; i < array.length; i++){
		var currentMeasure = parseFloat(array[i][measure]);
		if(minValue == 0) minValue = currentMeasure; 
		if(currentMeasure < minValue)
			minValue = currentMeasure; 
	}
	return minValue-10;
}

function updateLocationAttributes(graphicLayer){
    var count = graphicLayer.graphics.length;
    for(var i=0;i<count;i++) {
      var g = graphicLayer.graphics[i];
      for(var j=0;j<locations.length;j++){
    	  var currentLocation = locations[j];
          if(g.attributes["GEO_ID"] == currentLocation.geoId){
        	  g.attributes["COUNT"] = Math.log(currentLocation.commitments);
        	  break;
          }
      }
    }
    return graphicLayer;
}

function getStructures(clear) {
	try {
		map.removeLayer(map.getLayer("structuresMap"));
		map.removeLayer(map.getLayer("activitiesMap"));
	}catch(e){}
    var structureGraphicLayer = esri.layers.GraphicsLayer({displayOnPan: true, id: "structuresMap", visible: true});

    var xhrArgs = {
		url : "/esrigis/datadispatcher.do?showstructures=true",
		handleAs : "json",
		   load: function(jsonData) {
			   dojo.forEach(jsonData,function(activity) {
		        	MapFindStructure(activity, structureGraphicLayer);
		        });
			    map.addLayer(structureGraphicLayer);
			    map.setExtent(map.extent.expand(1.01));
				hideLoading();
		   },
		error : function(error) {
			console.log(error);
		}
	}
	// Call the asynchronous xhrGet
	var deferred = dojo.xhrGet(xhrArgs);
	showLoading();
}

function MapFindStructure(activity, structureGraphicLayer){
	dojo.forEach(activity.structures,function(structure) {
		var pt = new esri.geometry.Point(structure.lat,structure.lon,map.spatialReference);
		var transpt = esri.geometry.geographicToWebMercator(pt);
		
		var sms = new esri.symbol.PictureMarkerSymbol('/TEMPLATE/ampTemplate/img_2/gis/' + structure.type +'.png', 32, 37);
				
//		var sms = new esri.symbol.SimpleMarkerSymbol().setStyle(esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE).setColor(new dojo.Color([255,0,0,0.5]));
		var attr = {"Temp":"Temporal Attribute"};
		var infoTemplate = new esri.InfoTemplate("");   
		var pgraphic = new esri.Graphic(transpt,sms,attr,infoTemplate);
		var exit = false;
		pgraphic.setAttributes( {
			  "Structure":structure.name,
			  "Activity":activity.activityname
			  });
		structureGraphicLayer.add(pgraphic);
    });
}
