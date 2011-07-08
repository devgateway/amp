dojo.require("dijit.dijit"); // Optimize: load dijit layer
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("esri.map");
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
dojo.require("dijit.Menu");
dojo.require("esri.dijit.Legend");
dojo.require("esri.layers.FeatureLayer");
/*----variables---------*/
var map, navToolbar,geometryService,findTask,findParams;
var totallocations = 0;
var features = new Array();
var structures = new Array();
var timer_on = 0;
var activitiesarray = new Array();
var loading;
var cL;
var basemapGallery;
var activitieson;
var structureson;
var maxExtent;
var basemap;
var structureGraphicLayer;
/*---- Search  ----*/ 
var searchactive=new Boolean();
var searchdistance;
var foundstr = new Array();
var searchpoint;
var rangegraphicLayer;
/*-----------------Indicators Layers------------*/

/**
 * 
 */
function init() {
	//This have to be replaced with Global Settings values
	loading = dojo.byId("loadingImg");
	var basemapUrl = "http://4.79.228.117:8399/arcgis/rest/services/World_Street_Map/MapServer";
	var mapurl = "http://4.79.228.117:8399/arcgis/rest/services/Liberia_Map_Test/MapServer";
	var povertyratesurl = "http://4.79.228.117:8399/arcgis/rest/services/Liberia_Pop_Density_and_Poverty/MapServer/9";
	var population = "http://4.79.228.117:8399/arcgis/rest/services/LiberiaPopulaitionDensity/MapServer";
	
	basemap = new esri.layers.ArcGISTiledMapServiceLayer(basemapUrl, {id:'base'}); // Levels at which this layer will be visible);
	liberiamap = new esri.layers.ArcGISDynamicMapServiceLayer(mapurl, {opacity : 0.90,id:'liberia'});
	
	
	povertyratesmap = new esri.layers.FeatureLayer(povertyratesurl, {
		mode: esri.layers.FeatureLayer.MODE_ONDEMAND,outFields: ["*"],
		id:'indicator',opacity : 0.50,
		visible:false
      });
	
	populationmap = new esri.layers.ArcGISDynamicMapServiceLayer(population,{opacity : 0.80,visible:false,id:'census'});
	
	geometryService = new esri.tasks.GeometryService("http://4.79.228.117:8399/arcgis/rest/services/Geometry/GeometryServer");
	esriConfig.defaults.io.proxyUrl = "/esrigis/esriproxy.do";
	
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

/**
 * Create a map, set the extent, and add the services to the map.
 * @param myService1
 * @param myService2
 */

function createMapAddLayers(myService1, myService2) {
	
	map = new esri.Map("map", {extent : esri.geometry.geographicToWebMercator(myService2.fullExtent)});
	dojo.connect(map, 'onLoad', function(map) {
		dojo.connect(dijit.byId('map'), 'resize', resizeMap);
        dojo.byId('map_zoom_slider').style.top = '95px';
        getActivities(false);
        getStructures(false);
    });
	
	//add the legend
    dojo.connect(map,'onLayersAddResult',function(results){
      var layerInfo = dojo.map(results, function(layer,index){
    	  return {layer:layer.layer,title:layer.layer.name};
      });
      if(layerInfo.length > 0){
        var legendDijit = new esri.dijit.Legend({map:map,layerInfos:layerInfo},"legendDiv");
        legendDijit.startup();
      }
    });
   
    navToolbar = new esri.toolbars.Navigation(map);
	dojo.connect(navToolbar, "onExtentHistoryChange",extentHistoryChangeHandler);
	
	dojo.connect(map, "onClick", doBuffer);
	dojo.connect(map, "onMouseMove", selectionFunction);
	
	
	map.addLayer(myService1);
	map.addLayer(myService2);
	map.addLayers([povertyratesmap,populationmap]);
	
	//dojo.connect(map, "onExtentChange", showExtent);
	
	createBasemapGalleryEsri();
	createBasemapGallery();
	maxExtent = map.extent;
	searchactive = false;
}

/**
 * 
 * @param id
 */
function toggleindicatormap(id) {
	  var layer = map.getLayer(id);
	  var functionalayer = map.getLayer('liberia');
	  if (layer.visible) {
	    layer.hide();
	    $('#legendDiv').hide('slow');
	    functionalayer.show();
	  } else {
	    layer.show();
	    $('#legendDiv').show('slow');
	    functionalayer.hide();
	  }
	}

function extentHistoryChangeHandler() {
	dijit.byId("zoomprev").disabled = navToolbar.isFirstExtent();
	dijit.byId("zoomnext").disabled = navToolbar.isLastExtent();
}

/**
 *  resize the map when the browser resizes - view the 'Resizing and
 *	repositioning the map' section in
 *	the following help topic for more details
 *	http://help.esri.com/EN/webapi/javascript/arcgis/help/jshelp_start.htm#jshelp/inside_faq.htm
 */
function resizeMap() {
	var resizeTimer;
	clearTimeout(resizeTimer);
	resizeTimer = setTimeout(function() {
		map.resize();
		map.reposition();
	}, 500);
}

/** 
 *  show map on load
 */
	dojo.addOnLoad(init);

/**
 * 
 * @param evt
 */
function doBuffer(evt) {
	if (searchactive && searchdistance){
		showLoading();
	    map.graphics.clear();
	    var params = new esri.tasks.BufferParameters();
	    params.geometries = [ evt.mapPoint ];
	    
	    //buffer in linear units such as meters, km, miles etc.
	    params.distances = [ 1, searchdistance ];
	    params.unit = esri.tasks.GeometryService.UNIT_KILOMETER;
	    params.outSpatialReference = map.spatialReference;
		geometryService.buffer(params, showBuffer);
		findbydistance(evt);
	}
	if(addActivityMode)
	{
		MapFindPoint(implementationLevel[1], evt);
	}
}

/**
 * 
 */
function clearbuffer(){
	try{
		if (rangegraphicLayer){
			map.removeLayer(map.getLayer("rangelayer"));
		}
		map.infoWindow.hide;
	}catch(e){
		console.log(e);
	}
}

/**
 * Create a buffered point to show the search range
 * @param geometries
 */
function showBuffer(geometries) {
	  var symbol = new esri.symbol.SimpleFillSymbol( esri.symbol.SimpleFillSymbol.STYLE_SOLID,new esri.symbol.SimpleLineSymbol(
    		esri.symbol.SimpleLineSymbol.STYLE_DASH,new dojo.Color([112,0,0,0.60]), 2),new dojo.Color([112,0,0,0.15])
   );
   try{
	   if (rangegraphicLayer){
		   map.removeLayer(map.getLayer("rangelayer"));
	   }
	}
	catch(e){
		console.log(e);
	}
	rangegraphicLayer = esri.layers.GraphicsLayer({displayOnPan: false, id: "rangelayer", visible: true}); 
    dojo.forEach(geometries, function(geometry) {
	      var graphic = new esri.Graphic(geometry,symbol);
	      rangegraphicLayer.add(graphic);
    });
   
    map.addLayer(rangegraphicLayer);
    map.reorderLayer(map.getLayer("rangelayer"),0);
}

/**
 * Iterate structures and query the geometry server to calculate the distance. 
 * @param evt
 */
function findbydistance(evt){
	searchpoint = evt;
	foundstr = [];
	if (structures.length>0){
		var count=0;
		var tasktime = structures.length * 5 * 1000 ;
		var t=setTimeout("showStInfoWindow();",tasktime);
	   
		var distParams = new esri.tasks.DistanceParameters();
		for ( var int = 0; int < structures.length; int++) {
			distParams.distanceUnit = esri.tasks.GeometryService.UNIT_KILOMETER;    
			distParams.geometry1 = evt.mapPoint;
			distParams.geometry2 = structures[int].geometry;
			distParams.geodesic = true;
			
			geometryService.distance(distParams,function(distance){
				if (distance <=searchdistance){
					//console.log("Results: " + distance + " " + structures[count].attributes.Activity );
					foundstr.push(structures[count]);
				}
				count++;
				if (count+1 >structures.length ){
					showStInfoWindow();
					}
				});
		}
	}
}

/**
 *  Show the search structures resutl in a infowindow
 */
function showStInfoWindow () {
	 searchactive = false;
	 hideTooltip();
	 var content = "<table border='1' width='100%'>" 
	 			+ "<tr>" 
	 			+ "<td align='center' width='200px'>Name</td>" 
	 			+ "<td align='center' width='100px'>Type</td>" 
	 			+ "<td align='center' width='300px'>Activity</td>"
	 			+ "</tr>";
		if (map.infoWindow.isShowing) {
	        map.infoWindow.hide();
		}
    map.infoWindow.setTitle("Structures");
    for ( var int = 0; int < foundstr.length; int++) {
    	    content = content + "<tr><td>" + foundstr[int].attributes["Structure Name"] + "</a></td>" ;
    	    content = content + "<td align='left'>" + foundstr[int].attributes["Structure Type"] + "</td>" ;
    	    content = content + "<td>" + foundstr[int].attributes["Activity"] + "</td></tr>" ;
     }
    content = content + "</table>";
    if (foundstr.length>0){
    	map.infoWindow.setContent(content);
    	map.infoWindow.resize(600, 200);
    	map.infoWindow.show(searchpoint.screenPoint,map.getInfoWindowAnchor(searchpoint.screenPoint));
    }else{
    	clearbuffer();
    }
    dojo.connect(map.infoWindow, "onHide", clearbuffer);
    hideLoading();
}

/**
 * Use the clear parameter to remove all activities fron the map 
 * @param clear
 */
function getActivities(clear) {
	if (clear && cL){
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

/**
 * 
 * @param activity
 */
var donorArray = new Array();

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
    		if (location.exactlocation){
    			var pt = new esri.geometry.Point(location.exactlocation_lat,location.exactlocation_lon,new esri.SpatialReference({"wkid":4326}));
    		}else{
    			var pt = new esri.geometry.Point(location.lat,location.lon,new esri.SpatialReference({"wkid":4326}));
    		}
    		//Create a graphic point based on the x y coordinates wkid(Well-known ID) 4326 for GCS_WGS_1984 projection
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
    		
    		var donorname;
    		var donorCode;
    		dojo.forEach(activity.donors,function(donor) {
    			if(!containsDonor(donor, donorArray)){
    	  			donorArray.push(donor);
    				
    			}
    			if (donorname==null){
    				donorname = donor.donorname;
    				donorCode = donor.donorCode;
    			}else{
    				donorname = donorname +","+ donor.donorname;
    			}
    		});
    		pgraphic.setAttributes( {
    			  "Activity":'<a href="/aim/viewActivityPreview.do~pageId=2~activityId='+activity.id+'~isPreview=1" target="_blank">'+activity.activityname+'</a>',	
    			  "Donors":'<b>'+donorname+'</b>',
    			  "Location":'<b>'+location.name+'</b>',
    			  "Primary Sector":'<b>'+primarysector+'</b>',
    			  "Total commitments":'<b>'+activity.commitments+' '+activity.currecycode+'</b>',
  				  "Total disbursements":'<b>'+activity.disbursements+' '+activity.currecycode+'</b>',
  				  "Commitments for this location":'<b>'+location.commitments+' '+activity.currecycode+'</b>',
  				  "Disbursements for this location":'<b>'+location.disbursements+' '+activity.currecycode+'</b>',
  				  "Code":''+donorCode+''
  				  });
  			location.isdisplayed = true;
  			features.push(pgraphic);
    	}
    });
}

/**
 * 
 * @param searchText
 */
function execute(searchText) {
    //set the search text to find parameters
    findParams.searchText = searchText;
    findTask.execute(findParams, showResults);
    var t=setTimeout("drawpoints()",10000);
    timer_on = 1;
  }

/**
 * 
 * @param results
 */
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



/**
 * 
 */
function drawpoints(){
	if (timer_on){
	 if(cL!=null){ 
		 cL._features=[];
		 cL.levelPointTileSpace = [];
	 }
	 //Create palette for individual donors
	 var pointSymbolBank = new Array();
	 for(var i=0; i < donorArray.length; i++){
		 var pointObject = new esri.symbol.SimpleMarkerSymbol( esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 15, new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, colorsCualitative[i], 1), colorsCualitative[i]);
		 pointSymbolBank[donorArray[i].donorCode] = pointObject;
	 }
	 //Add standard symbols
	 pointSymbolBank["single"] = new esri.symbol.SimpleMarkerSymbol(
             esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE,
             10,
             new esri.symbol.SimpleLineSymbol(
                     esri.symbol.SimpleLineSymbol.STYLE_SOLID,
                     new dojo.Color([0, 0, 0, 1]),
                     1
                 ),
             new dojo.Color([255, 215, 0, 1]));
	 
	 pointSymbolBank["less16"] =  new esri.symbol.SimpleMarkerSymbol(
             esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE,
             20,
             new esri.symbol.SimpleLineSymbol(
                     esri.symbol.SimpleLineSymbol.STYLE_SOLID,
                     new dojo.Color([0, 0, 0, 1]),
                     1
                 ),
             new dojo.Color([255, 215, 0, 1]));
	 pointSymbolBank["less30"] = new esri.symbol.SimpleMarkerSymbol(
             esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE,
             30,
             new esri.symbol.SimpleLineSymbol(
                     esri.symbol.SimpleLineSymbol.STYLE_NULL,
                     new dojo.Color([0, 0, 0, 0]),
                     1
                 ),
             new dojo.Color([100, 149, 237, .85]));
	 pointSymbolBank["less50"] = new esri.symbol.SimpleMarkerSymbol(
             esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE,
             30,
             new esri.symbol.SimpleLineSymbol(
                     esri.symbol.SimpleLineSymbol.STYLE_NULL,
                     new dojo.Color([0, 0, 0, 0]),
                     1
                 ),
             new dojo.Color([65, 105, 225, .85]));
	 pointSymbolBank["over50"] = new esri.symbol.SimpleMarkerSymbol(esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 45,
             new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_NULL,
                     new dojo.Color([0, 0, 0]), 0),
                     new dojo.Color([255, 69, 0, 0.65]));
     var symbolBank = pointSymbolBank;
	 
	  	cL = new esri.ux.layers.ClusterLayer({
			displayOnPan: false,
			map: map,
			symbolBank: symbolBank,
			id: "activitiesMap",
			features: features,
			infoWindow: {
				template: new esri.InfoTemplate("Activity Details", "<table style='font-size: 11px;'>" +
						"<tr><td><b>Activity<b></td><td> ${Activity}</td></tr>" +
						"<tr><td nowrap><b>Donors<b></td><td>${Donors}</td></tr>" +
						"<tr><td nowrap><b>Location<b></td><td>${Location}</td></tr>" +
						"<tr><td nowrap><b>Primary Sector<b></td><td>${Primary Sector}</td></tr>" +
						"<tr><td nowrap><b>Total commitments<b></td><td>${Total commitments}</td></tr>" +
						"<tr><td nowrap><b>Total disbursements<b></td><td>${Total disbursements}</td></tr>" +
						"<tr><td nowrap><b>Commitments for this location<b></td><td>${Commitments for this location}</td></tr>" +
						"<tr><td nowrap><b>Disbursements for this location<b></td><td>${Disbursements for this location}</td></tr></table>"),
			width: 400,
			height: 260
		},
		flareLimit: 15,
		flareDistanceFromCenter: 30
	});
	map.addLayer(cL);
    showLegendClusterDonor(pointSymbolBank);
	timer_on=0;
	}
}

//--------------------------------------FFerreyra Functions
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


var currentLevel;
function MapFindLocation(level){
	showLoading();
	var queryTask = new esri.tasks.QueryTask("http://4.79.228.117:8399/arcgis/rest/services/Liberia/MapServer/" + level.mapId);
    var query = new esri.tasks.Query();
    query.where = level.mapField + " <> ''";
    query.outSpatialReference = {wkid:map.spatialReference.wkid};
    query.returnGeometry = true;
    query.outFields = ["COUNT", level.mapField, "GEO_ID"];
    currentLevel = level;
    queryTask.execute(query, addResultsToMap);
}

/**
 * 
 * @param featureSet
 */
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
    	//Read current attributes and assign a new set
    	var count = feature.attributes["COUNT"];
    	var county = feature.attributes["COUNTY"];
    	var geoId = feature.attributes["GEO_ID"];
    	feature.setAttributes( {
			  "COUNT": count,
			  "AMOUNT": count,
			  "COUNTY": county,
			  "GEO_ID": geoId
			  });
    	//TODO: Should support all types of funding
    	feature.setInfoTemplate(new esri.InfoTemplate("Funding", "County: ${COUNTY} <br/>Commitments: ${AMOUNT}<br/> "));
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

/**
 * 
 * @param rangeColors
 * @param colors
 */
function showLegend(rangeColors, colors){
	//TODO: Should support currencies
	var currencyString = "USD";
	//TODO: Should support all types of funding
	var typeOfFunding = "Commitments";
	var htmlDiv = "";
	htmlDiv += "<div onclick='closeHide()' style='color:white;float:right;cursor:pointer;'>X</div>";
	htmlDiv += "<div class='legendHeader'>Showing " + typeOfFunding + " for " + currentLevel.name + "<br/><hr/></div>";
	for(var i=0; i< rangeColors.length; i++){
		htmlDiv += "<div class='legendContentContainer'>"
				+ "<div class='legendContentValue' style='background-color:rgba(" + colors[i].toRgba() + ");' ></div>"
				+"</div>"
				+ "<div class='legendContentLabel'>" + Math.ceil(rangeColors[i][0]) + " " + currencyString + " - " + Math.floor(rangeColors[i][1]) + " " + currencyString + " </div><br/>";
	}
	$('#legenddiv').html(htmlDiv);
	$('#legenddiv').show('slow');
}


/**
 * 
 * @param graphicLayer
 * @returns
 */
function updateLocationAttributes(graphicLayer){
    var count = graphicLayer.graphics.length;
    for(var i=0;i<count;i++) {
      var g = graphicLayer.graphics[i];
      for(var j=0;j<locations.length;j++){
    	  var currentLocation = locations[j];
          if(g.attributes["GEO_ID"] == currentLocation.geoId){
        	  g.attributes["COUNT"] = Math.log(currentLocation.commitments);
        	  g.attributes["AMOUNT"] = currentLocation.commitments;
        	  break;
          }
      }
    }
    return graphicLayer;
}

/**
 * 
 * @param clear
 */
function getStructures(clear) {
	if (clear){
		try {
			structureGraphicLayer = null;
			structures = [];
			map.removeLayer(map.getLayer("structuresMap"));
		}catch(e){
			console.log(e);
		}
	}
	if (structureGraphicLayer){
		if (structureGraphicLayer.visible){
			structureGraphicLayer.hide();
		}else{
			structureGraphicLayer.show();
		}
	}else{
	    structureGraphicLayer = esri.layers.GraphicsLayer({displayOnPan: false, id: "structuresMap", visible: false});
	    structureson =true;
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
}

/**
 * 
 * @param activity
 * @param structureGraphicLayer
 */
function MapFindStructure(activity, structureGraphicLayer){
	dojo.forEach(activity.structures,function(structure) {
		var sms = new esri.symbol.PictureMarkerSymbol('/esrigis/structureTypeManager.do~action=displayIcon~id=' + structure.typeId, 32, 37);
		var pgraphic;
		if(structure.shape == ""){
			var pt = new esri.geometry.Point(structure.lat,structure.lon,map.spatialReference);
			var transpt = esri.geometry.geographicToWebMercator(pt);
			var infoTemplate = new esri.InfoTemplate("");   
			var attr = {"Temp":"Temporal Attribute"};
			pgraphic = new esri.Graphic(transpt,sms,attr,infoTemplate);
			
			pgraphic.setAttributes( {
				  "Structure Name":structure.name,
				  "Activity":'<a href="/aim/viewActivityPreview.do~pageId=2~activityId='+activity.ampactivityid+'~isPreview=1" target="_blank">'+activity.activityname+'</a>',
				  "Structure Type":structure.type,
				  "Coordinates":pt.x + " " + pt.y
				  });
			structures.push(pgraphic);
		}
		else
		{
			var jsonObject = eval('(' + structure.shape + ')');
			if(jsonObject.geometry != null){ //If it's a complete Graphic object
				pgraphic = new esri.Graphic(jsonObject);
				pgraphic.setAttributes( {
					  "Structure Name":structure.name,
					  "Structure Type":structure.type,
					  "Activity":'<a href="/aim/viewActivityPreview.do~pageId=2~activityId='+activity.ampactivityid+'~isPreview=1" target="_blank">'+activity.activityname+'</a>',
					  "Coordinates":pgraphic.geometry.x + " " + pgraphic.geometry.y 
					  });
				pgraphic.setInfoTemplate(new esri.InfoTemplate(""));
				if(jsonObject.symbol.style == "esriSMSCircle") //If it's a point, put the appropriate icon
				{
					pgraphic.setSymbol(sms);
				}
				
			}
			else
			{
				pt = new esri.geometry.Point(jsonObject);
				var infoTemplate = new esri.InfoTemplate("");   
				var attr = {"Temp":"Temporal Attribute"};
				pgraphic = new esri.Graphic(pt,sms,attr,infoTemplate);
				pgraphic.setAttributes( {
					  "Structure Name":structure.name,
					  "Structure Type":structure.type,
					  "Activity":'<a href="/aim/viewActivityPreview.do~pageId=2~activityId='+activity.ampactivityid+'~isPreview=1" target="_blank">'+activity.activityname+'</a>',
					  "Coordinates":pgraphic.geometry.x + " " + pgraphic.geometry.y
					  });
				
			}
		}
		structureGraphicLayer.add(pgraphic);
		structures.push(pgraphic);
	});
}

/**
 * Section for adding activities 
 */

var addActivityMode = false;
function addActivity(){
	addActivityMode = true;
}

/**
 * 
 * @param evt
 */
function selectionFunction(evt){
	if(addActivityMode || searchactive){
		var tooltipHolder = dojo.byId("tooltipHolder");
		tooltipHolder.innerHTML = "Select a point";
		tooltipHolder.style.display = "block";
		tooltipHolder.style.top = evt.pageY+5 + "px";
		tooltipHolder.style.left = evt.pageX+5 + "px";
	}
}

/**
 * 
 * @param results
 */
function showAddActivityInfoWindow (results) {
	hideTooltip();
	
	addActivityMode = false;
	var county = results.features[0].attributes["COUNTY"];
	var district = results.features[0].attributes["DISTRICT"];
	var geoId = results.features[0].attributes["GEO_ID"];
	var content = "<table border='0' width='100%'>" 
		+ "<tr>" 
		+ "<td align='center' width='200px'>Name</td>" 
		+ "<td align='center' width='100px'><input id=\"activityName\" type=\"text\"></td>" 
		+ "</tr>"
		+ "<tr>" 
		+ "<td align='center' width='200px'>Location</td>" 
		+ "<td align='center' width='100px'><input type=\"text\" READONLY value=\"" + county + ", " + district + "\"></td>" 
		+ "</tr>"
		+ "<tr>" 
		+ "<td align='center' width='200px'>Geo Id</td>" 
		+ "<td align='center' width='100px'><input type=\"text\" READONLY id=\"geoId\" value=\"" + geoId + "\"></td>" 
		+ "</tr>"
		+ "<tr>" 
		+ "<td align='center' width='200px'>Latitude</td>" 
		+ "<td align='center' width='100px'><input type=\"text\" READONLY id=\"lat\" value=\"" + esri.geometry.webMercatorToGeographic(searchpoint.mapPoint).y + "\"></td>" 
		+ "</tr>"
		+ "<tr>" 
		+ "<td align='center' width='200px'>Longitude</td>" 
		+ "<td align='center' width='100px'><input type=\"text\" READONLY id=\"lon\" value=\"" + esri.geometry.webMercatorToGeographic(searchpoint.mapPoint).x+ "\"></td>" 
		+ "</tr>"
		+ "<tr>" 
		+ "<td align='center' width='300px' colspan=\"2\"><input type=\"button\" value=\"Create new activity\" onclick=\"submitActivity()\"></td>" 
		+ "</tr>";
	if (map.infoWindow.isShowing) {
		map.infoWindow.hide();
	}
	map.infoWindow.setTitle("Add new activity");
	content = content + "</table>";
	map.infoWindow.setContent(content);
	map.infoWindow.resize(400, 200);
	map.infoWindow.show(searchpoint.screenPoint,map.getInfoWindowAnchor(searchpoint.screenPoint));
//	dojo.connect(map.infoWindow, "onHide", clearbuffer);
	hideLoading();
}

/**
 * 
 * @param level
 * @param evt
 */
function MapFindPoint(level, evt){
	searchpoint = evt;
	showLoading();
	var queryTask = new esri.tasks.QueryTask("http://4.79.228.117:8399/arcgis/rest/services/Liberia/MapServer/" + level.mapId);
    var query = new esri.tasks.Query();
    query.geometry = evt.mapPoint;
    query.outSpatialReference = {wkid:map.spatialReference.wkid};
    query.returnGeometry = false;
    query.outFields = ["COUNT", "COUNTY", "DISTRICT", "GEO_ID"];
    queryTask.execute(query, showAddActivityInfoWindow);
}

/**
 * 
 */
function submitActivity(){
	var name, lat, lon, geoid;
	name = dojo.byId("activityName");
	lat = dojo.byId("lat");
	lon = dojo.byId("lon");
	geoId = dojo.byId("geoId");
	//Removing district, problem with Locations
	geoIdShort = geoId.value.substring(0,4)
	window.open("/wicket/onepager/activity/new/name/" + name.value + "/lat/"+lat.value+"/lon/"+lon.value+"/geoId/"+geoIdShort);
	
}

function showLegendCluster(pointSymbolBank){
	var htmlDiv = "";
	htmlDiv += "<div onclick='closeHide()' style='color:white;float:right;cursor:pointer;'>X</div>";
	htmlDiv += "<div class='legendHeader'>Cluster color reference<br/><hr/></div>";
	for (i in pointSymbolBank) {
		htmlDiv += "<div class='legendContentContainer'>"
				+ "<div class='legendContentValue' style='background-color:rgba(" + pointSymbolBank[i].color.toRgba() + ");' ></div>"
				+"</div>"
				+ "<div class='legendContentLabel'>" + i + " </div><br/>";
	}
	$('#legenddiv').html(htmlDiv);
	$('#legenddiv').show('slow');
}
function showLegendClusterDonor(pointSymbolBank){
	var htmlDiv = "";
	htmlDiv += "<div onclick=\"$('#legenddiv').hide('slow');\" style='color:white;float:right;cursor:pointer;'>X</div>";
	htmlDiv += "<div class='legendHeader'>Point color reference<br/><hr/></div>";
	for (var i=0; i < donorArray.length; i++) {
		htmlDiv += "<div class='legendContentContainer'>"
				+ "<div class='legendContentValue' style='background-color:rgba(" + pointSymbolBank[donorArray[i].donorCode].color.toRgba() + ");' ></div>"
				+"</div>"
				+ "<div class='legendContentLabel'>" + donorArray[i].donorname + " </div><br/>";
	}
	$('#legenddiv').html(htmlDiv);
	$('#legenddiv').show('slow');
}
