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
dojo.require("esri.arcgis.utils");
dojo.require("esri.toolbars.draw");
dojo.require("esri.toolbars.edit");
dojo.require("dijit.Menu");
dojo.require("dijit.layout.BorderContainer");
dojo.require("esri.dijit.BasemapGallery");
dojo.require("esri.layers.osm");

var map, navToolbar, tb;
var loading;
var basemapurl;
var mapurl;
var locatorurl;
var rooturl;
var stpoints = new Array();
var isOsm = false;

var MapConstants = {
		   "MapType": {
				"BASE_MAP" : 1,
				"MAIN_MAP" : 2,
				"GEOMETRY_SERVICE" : 4,
				"ARCGIS_API" : 5,
				"GEOLOCATOR_SERVICE" : 7,
				"BASEMAPS_ROOT" : 8,
				"NATIONAL_LAYER" : 9,
				"INDICATOR_LAYER" : 10
			},
		   "MapSubType": {
			   "BASE" : 1,
			   "INDICATOR" : 2,
			   "OSM" : 3
		   }
		};
function init() {
	//This have to be replaced with Global Settings values
	loading = dojo.byId("loadingImg");
	loading.hidden = true;
	initializeTranslations();
	
	var xhrArgs = {
			url : "/esrigis/datadispatcher.do?getconfig=true",
			handleAs : "json",
			sync:true,
			load: function(jsonData) {
				   dojo.forEach(jsonData,function(map) {
			        	switch (map.mapType) {
						case MapConstants.MapType.BASE_MAP:
							basemapurl = map.mapUrl;
							if (map.mapSubType == MapConstants.MapSubType.OSM){
								isOsm = true;
							}
							break;
						case MapConstants.MapType.MAIN_MAP:
							mapurl = map.mapUrl;
							break;
						case MapConstants.MapType.GEOLOCATOR_SERVICE:
							locatorurl = map.mapUrl;
							break;
						case MapConstants.MapType.BASEMAPS_ROOT:
							rooturl = map.mapUrl;
							break;
						default:
							break;
						}
			        });
				},
			error : function(error) {
				console.log(error);
			}
		}
		// Call the asynchronous xhrGet
		var deferred = dojo.xhrGet(xhrArgs);
	
	if (!isOsm){
		basemap = new esri.layers.ArcGISTiledMapServiceLayer(basemapurl, {id : 'base'}); // Levels at which this layer will be visible);
	}else{
		basemap = new esri.layers.OpenStreetMapLayer({id : 'base'});
    }
	var mainmap = new esri.layers.ArcGISDynamicMapServiceLayer(mapurl, {opacity : 0.90});
	if (!locatorurl){
		$("#address").attr("disabled","disabled");
		$("#localebtn").attr("disabled","disabled");
		$("#localebtn").css('backgroundColor','gray');
	}else{
		locator = new esri.tasks.Locator(locatorurl);
		dojo.connect(locator, "onAddressToLocationsComplete", showResults);
	}
	
	var layerLoadCount = 0;
	if (basemap.loaded) {
		layerLoadCount += 1;
		if (layerLoadCount === 2) {
			createMapAddLayers(basemap, mainmap);
		}
	} else {
		dojo.connect(basemap, "onLoad", function(service) {
			layerLoadCount += 1;
			if (layerLoadCount === 2) {
				createMapAddLayers(basemap, mainmap);
			}
		});
	}
	if (mainmap.loaded) {
		layerLoadCount += 1;
		if (layerLoadCount === 2) {
			createMapAddLayers(basemap, mainmap);
		}
	} else {
		dojo.connect(mainmap, "onLoad", function(service) {
			layerLoadCount += 1;
			if (layerLoadCount === 2) {
				createMapAddLayers(basemap, mainmap);
			}
		});
	}
	esri.hide(loading);

}



// Create a map, set the extent, and add the services to the map.
function createMapAddLayers(myService1, myService2) {
	customLods = [
		   {"level" : 0,"resolution" : 19567.87924099992,"scale" : 7.3957190948944E7}, 
		   {"level" : 1,"resolution" : 4891.96981024998,"scale" : 18489297.737236}, 
		   {"level" : 2,"resolution" : 2445.98490512499,"scale" : 9244648.868618}, 
		   {"level" : 3,"resolution" : 1222.99245256249,"scale" : 4622324.434309}, 
		   {"level" : 4,"resolution" : 611.49622628138,"scale" : 2311162.217155}, 
		   {"level" : 5,"resolution" : 305.748113140558,"scale" : 1155581.108577}, 
		   {"level" : 6,"resolution" : 152.874056570411,"scale" : 577790.554289}, 
		   {"level" : 7,"resolution" : 76.4370282850732,"scale" : 288895.277144}, 
		   {"level" : 8,"resolution" : 38.2185141425366,"scale" : 144447.638572}, 
		   {"level" : 9,"resolution" : 19.1092570712683,"scale" : 72223.819286}, 
		   {"level" : 10,"resolution" : 9.55462853563415,"scale" : 36111.909643}, 
		   {"level" : 11,"resolution" : 4.77731426794937,"scale" : 18055.954822} ];
	
	// create map
	// convert the extent to Web Mercator
	map = new esri.Map("map", {lods : customLods,
		extent : esri.geometry.geographicToWebMercator(myService2.fullExtent)
	});

	dojo.connect(map, 'onLoad', function(map) {
		dojo.connect(dijit.byId('map'), 'resize', resizeMap);
        dojo.byId('map_zoom_slider').style.top = '80px';
        createToolbarAndContextMenu();
        initToolbar(map);
        try {
        	selectCurrentLocation();
        }
        catch(e){
        	
        	console.log("Couldn't position current structure:" + e);
        }
    });
	map.addLayer(myService1);
	map.addLayer(myService2);
	navToolbar = new esri.toolbars.Navigation(map);
	dojo.connect(navToolbar, "onExtentHistoryChange",extentHistoryChangeHandler);
	createBasemapGalleryEsri('basemapGalleryesripopup');
}


function showLoading() {
    esri.show(loading);
  }

  function hideLoading() {
    esri.hide(loading);
  }

function showCoordinates(evt) {
    //get mapPoint from event
    //The map is in web mercator - modify the map point to display the results in geographic
    var mp = esri.geometry.webMercatorToGeographic(evt.mapPoint);
    //display mouse coordinates
    console.log(mp.x + ", " + mp.y);
     
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

var editToolbar;
var ctxMenuForGraphics, ctxMenuForMap;
var selected, currentLocation;


function createToolbarAndContextMenu() {
  // Create and setup editing tools
  editToolbar = new esri.toolbars.Edit(map);

  dojo.connect(map,"onClick", function(evt){
    editToolbar.deactivate();
  });

  createMapMenu();
  createGraphicsMenu();
}



function createMapMenu() {
  // Creates right-click context menu for map
  ctxMenuForMap = new dijit.Menu({
    onOpen: function(box) {
      // Lets calculate the map coordinates where user right clicked.
      // We'll use this to create the graphic when the user clicks
      // on the menu item to "Add Point"
      currentLocation = getMapPointFromMenuPosition(box);          
      editToolbar.deactivate();
    }
  });

  ctxMenuForMap.addChild(new dijit.MenuItem({ 
    label: "Select this point",
    onClick: function(evt) {
    		(currentLocation);
    }
  }));


  ctxMenuForMap.startup();
  ctxMenuForMap.bindDomNode(map.container);
}


function createGraphicsMenu() {
  // Creates right-click context menu for GRAPHICS

  ctxMenuForGraphics = new dijit.Menu({});
  ctxMenuForGraphics.addChild(new dijit.MenuItem({ 
	    label: "Select this structure",
	    onClick: function(evt) {
	    	selectLocationCallerShape(selected);
	    }
	  }));
  ctxMenuForGraphics.addChild(new dijit.MenuItem({ 
    label: trnEdit,
    onClick: function() {
      if(selected.geometry.type !== "point"){
        editToolbar.activate(esri.toolbars.Edit.EDIT_VERTICES, selected);
      }
      else{
        alert(trnNotImplemented);
      }
    } 
  }));


  ctxMenuForGraphics.addChild(new dijit.MenuItem({ 
    label: trnMove,
    onClick: function() {
      editToolbar.activate(esri.toolbars.Edit.MOVE, selected);
    } 
  }));

  ctxMenuForGraphics.addChild(new dijit.MenuItem({ 
    label: trnRotateScale,
    onClick: function() {
    if(selected.geometry.type !== "point"){
        editToolbar.activate(esri.toolbars.Edit.ROTATE | esri.toolbars.Edit.SCALE, selected);
      }
      else{
        alert(trnNotImplemented);
      }
    }
  }));

  ctxMenuForGraphics.addChild(new dijit.MenuSeparator());
  ctxMenuForGraphics.addChild(new dijit.MenuItem({ 
    label: trnDelete,
    onClick: function() {
      map.graphics.remove(selected);
    }
  }));

  ctxMenuForGraphics.startup();

  dojo.connect(map.graphics, "onMouseOver", function(evt) {
    // We'll use this "selected" graphic to enable editing tools
    // on this graphic when the user click on one of the tools
    // listed in the menu.
    selected = evt.graphic;

    // Let's bind to the graphic underneath the mouse cursor           
    ctxMenuForGraphics.bindDomNode(evt.graphic.getDojoShape().getNode());
  });


  dojo.connect(map.graphics, "onMouseOut", function(evt) {
    ctxMenuForGraphics.unBindDomNode(evt.graphic.getDojoShape().getNode());
  });
}

/*****************
 * Helper Methods
 *****************/      

function getMapPointFromMenuPosition(box) {
  var x = box.x, y = box.y;

  switch(box.corner) {
    case "TR":
      x += box.w;
      break;
    case "BL":
      y += box.h;
      break;
    case "BR":
      x += box.w;
      y += box.h;
      break;
  }

  var screenPoint = new esri.geometry.Point(x - map.position.x, y - map.position.y);
  return map.toMap(screenPoint);
}

function selectCurrentLocation(){
	//Hack to get the Ids for the fields lan/lon
	var callerButton = window.opener.callerGisObject;
	var pgraphic;
	var sms;
	var shapeValue = callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[3].value;
	var typeSelect = callerButton.parentNode.parentNode.getElementsByTagName("SELECT")[0];
	var typeText = typeSelect.options[typeSelect.selectedIndex].text; 
	var typeValue = typeSelect.options[typeSelect.selectedIndex].value; 
	if (typeValue){
		sms = new esri.symbol.PictureMarkerSymbol('/esrigis/structureTypeManager.do~action=displayIcon~id=' + typeValue, 21, 25);
	}else{
		sms = new esri.symbol.SimpleMarkerSymbol();
		sms.setStyle(esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE);
		sms.setColor(new dojo.Color([255,0,0,0.75]));
	}
	if(shapeValue == ""){
		//This takes an identifier for the element shape (the sibling of the Map button) and replaces shape with latitude and longitude or any other attribute we need
		var latitude = callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[1].value;
		var longitude = callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[2].value;

		var pt = new esri.geometry.Point(longitude,latitude,map.spatialReference);
		var transpt = esri.geometry.geographicToWebMercator(pt);
		var infoTemplate = new esri.InfoTemplate("");   
		var attr = {"Temp":"Temporal Attribute"};
		pgraphic = new esri.Graphic(transpt,sms,attr,infoTemplate);
		pgraphic.setAttributes( {
			  "Structure Type":typeText
			  });
		var exit = false;
	}
	else
	{
		var jsonObject = eval('(' + shapeValue + ')');
		if(jsonObject.geometry != null){ //If it's a complete Graphic object
			pgraphic = new esri.Graphic(jsonObject);
			if(jsonObject.symbol.style == "esriSMSCircle") //If it's a point, put the appropriate icon
			{
				pgraphic.setAttributes( {
					  "Structure Type":typeText
					  });
				pgraphic.setInfoTemplate(new esri.InfoTemplate(""));
				pgraphic.setSymbol(sms);
			}
			
		}
		else
		{
			pt = new esri.geometry.Point(jsonObject);
//			var transpt = esri.geometry.geographicToWebMercator(pt);
			var infoTemplate = new esri.InfoTemplate("");   
			var attr = {"Temp":"Temporal Attribute"};
			pgraphic = new esri.Graphic(pt,sms,attr,infoTemplate);
			pgraphic.setAttributes( {
				  "Structure Type":typeText
				  });
			
		}
	}
	map.graphics.add(pgraphic);
	
}


function selectLocationCaller(currentLocation){
	//Hack to get the Ids for the fields lan/lon from parent window
	var callerButton = $(window.opener.callerGisObject).parent().parent();
	//Lat
    var element = callerButton.find('input').eq(1);
    element.val(esri.geometry.webMercatorToGeographic(currentLocation).y);
    window.opener.postvaluesy(element);
    //Long
    element = callerButton.find('input').eq(2);
    element.val(esri.geometry.webMercatorToGeographic(currentLocation).x);
    window.opener.postvaluesx(element);
    
    $(window.opener.callerGisObject).blur();
    $(window.opener.callerGisObject).focus();
	//Very experimental, storing the point as Json Object
	//var shapeField = callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[4]; //this is the "shape" field
	//shapeField.value = JSON.stringify(currentLocation.toJson());
	window.close();
}

function initToolbar(map) {
    tb = new esri.toolbars.Draw(map);
    dojo.connect(tb, "onDrawEnd", addGraphic);
}

function addGraphic(geometry) {
  var type = geometry.type;
  if (type === "point" || type === "multipoint") {
    symbol = tb.markerSymbol;
  }
  else if (type === "line" || type === "polyline") {
    symbol = tb.lineSymbol;
  }
  else {
    symbol = tb.fillSymbol;
  }
    map.graphics.clear();
    map.graphics.add(new esri.Graphic(geometry, symbol));
  }

function selectLocationCallerShape(selectedGraphic){
	//Hack to get the Ids for the fields lan/lon from parent window
	var callerButton = window.opener.callerGisObject;
	//This takes an identifier for the element shape (the sibling of the Map button) and replaces shape with latitude and longitude
	//Very experimental, storing the point as Stringified Json Object
	
	//Since this is a shape that we have stored, we'll wipe the lat/lon values to avoid confusion
	//The lat/lon fields can be used if they are known before hand and entered manually
	//For quicker data entry.
	//The function that renders this in the esrimap/mainmap.do draws it either coming from shape or lat/lon

	//Lat
	callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[1].value = "";
	//Long
	callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[2].value = "";
	//Except in the case of a point which has coordinates
	if(selectedGraphic.geometry.type === "point"){
		callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[1].value = esri.geometry.webMercatorToGeographic(selectedGraphic.geometry).y;
		callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[2].value = esri.geometry.webMercatorToGeographic(selectedGraphic.geometry).x;
	}else{
		var shapeField = callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[3]; //this is the "shape" field
		shapeField.value = JSON.stringify(selectedGraphic.toJson());
        window.opener.postvaluesy(shapeField);
	}
	
	element = callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[1];
	window.opener.postvaluesy(element);
    element = callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[2];
    window.opener.postvaluesx(element);
    
	window.close();
}

function locate() {
	showLoading();
    map.graphics.clear();
    var address = {"PlaceName":dojo.byId("address").value};
    locator.outSpatialReference= map.spatialReference;
    var options = {
      address:address,
      outFields:["Name","type"]
    }
    locator.addressToLocations(options);
  }

  var symbol;
   function showResults(candidates) {
    var candidate;
    //var symbol = new esri.symbol.SimpleMarkerSymbol();
    var callerButton = window.opener.callerGisObject;
    var typeSelect = callerButton.parentNode.parentNode.getElementsByTagName("SELECT")[0];
	var typeText = typeSelect.options[typeSelect.selectedIndex].text; 
	var typeValue = typeSelect.options[typeSelect.selectedIndex].value;
  
  
	
	if (typeValue!=""){
		symbol = new esri.symbol.PictureMarkerSymbol('/esrigis/structureTypeManager.do~action=displayIcon~id=' + typeValue, 21, 25);
	}else{
		symbol = new esri.symbol.SimpleMarkerSymbol();
		symbol.setStyle(esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE);
		symbol.setColor(new dojo.Color([255,0,0,0.75]));
	}
    var infoTemplate = new esri.InfoTemplate("Location", "Location: ${address}<br/>Score: ${score}<br/>FCL:${Type}");
    
    var points =  new esri.geometry.Multipoint(map.spatialReference);
    stpoints = [];
    clearOptions("fclList");
    for (var i=0, il=candidates.length; i<il; i++) {
      candidate = candidates[i];
      if (candidate.score > 80) {
        var attributes = { address: candidate.attributes.Name, score:candidate.score,Type:candidate.attributes.type, locatorName:candidate.attributes.Loc_name };
        
        var pointCoordinates = null;
        
        if(candidate.location.spatialReference != null) {
        	pointCoordinates = candidate.location;
        } else {
        	pointCoordinates = esri.geometry.geographicToWebMercator(candidate.location);
        }
        
        var graphic = new esri.Graphic(pointCoordinates, symbol, attributes, infoTemplate);
        map.graphics.add(graphic);
        
//        map.graphics.add(new esri.Graphic(candidate.location, new esri.symbol.TextSymbol(candidate.attributes.Name).setOffset(0, 8)));
        stpoints.push(graphic);
        points.addPoint(candidate.location);
        
        
        
        if (!isInList(candidate.attributes.type)){
        	var select = document.getElementById("fclList");
        	select.options[select.options.length] = new Option(candidate.attributes.type, candidate.attributes.type);
        }
      }
    }

/*    
    if (points.getExtent()){
    	map.setExtent(points.getExtent().expand(3));
    }
*/    
    hideLoading();
  }

  function filter(type){
	  map.graphics.clear();
	  for ( var int = 0; int < stpoints.length; int++) {
		if (stpoints[int].attributes.Type==type){
			map.graphics.add(stpoints[int]);
		}
	}
  }
   
  function clearOptions(id)
  {
  	var selectObj = document.getElementById(id);
  	var selectParentNode = selectObj.parentNode;
  	var newSelectObj = selectObj.cloneNode(false); 
  	selectParentNode.replaceChild(newSelectObj, selectObj);
  	return newSelectObj;
  }
  
  function isInList(seachtext) {
	for ( var i = 0; i < document.getElementById('fclList').options.length; i++) {
		if (document.getElementById('fclList').options[i].text == seachtext) {
			return true;
			break;
		}
	}
}

  