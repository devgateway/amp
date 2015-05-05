var map; // the leaflet map
var basemapLayer;
var basemapLabel;
var searchLocationsLayer; // leaflet GEOJson object containing the locations returned from the search
var isDrawActive = false;
var isMenuOpen = false;
var selectedPointEvent;
var circlePoint;
var latitude;
var longitude;
basemapurl = undefined; 
var pointRadious = [8500,8500,8500,8500,8500,8500,8500,7500,5500,3500,2000,1000,600,400,300,200,200,170,150];
var MapConstants = {
		   "MapType": {
				"BASE_MAP" : 1
			},
		   "MapSubType": {
			   "BASE" : 1,
			   "INDICATOR" : 2,
			   "OSM" : 3
		   }
		};
var basemapUrl;
var isOsm = false;

function MapPopup (lat,long) {
	latitude = lat;
	longitude = long;
	initMap();
	startContextMenu();	
}

function initMap() {
	$.getJSON( "/esrigis/datadispatcher.do?getconfig=true", function() {
		  console.log( "Success retrieving Gazeteer map config" );
		})
		  .done(function(jsonData) {
			  if (jsonData.length ===0){
				  isOsm = true;
				  loadBaseMap();
				  return;
			  }
			  $.each( jsonData, function(key,map) {
			   		switch (map.mapType) {
					case MapConstants.MapType.BASE_MAP:
						basemapurl = map.mapUrl;
						if (basemapurl === undefined || map.mapSubType == MapConstants.MapSubType.OSM ){
							isOsm = true;
						}
						break;
					default:
						break;
					}
			   	  });
		  
			  loadBaseMap();
		  })
		  .fail(function() {
		    console.log( "Error retrieving map configuration" );
		  });

}

function loadBaseMap() {
	map = L.map('map').setView([ latitude, longitude ], 7);
	// create the tile layer with correct attribution
	var osmUrl='http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
	if (isOsm) {
		var subdomains = ['a','b','c'];
		if (basemapurl!== undefined && basemapurl.indexOf ("mqcdn")!=-1) {
			subdomains = ['otile1','otile2','otile3','otile4'];
		}
		var osmAttrib='Map data © <a href="http://openstreetmap.org">OpenStreetMap</a> contributors';
		tileLayer = new L.TileLayer(osmUrl, {minZoom: 0, maxZoom: 16, attribution: osmAttrib,
				 subdomains: subdomains });	
	}
	else {
	    tileLayer = L.esri.tiledMapLayer(basemapurl, {maxZoom: 16});
	}
	
	map.addLayer(tileLayer);

	currentZoom = map.getZoom();
	
	// attach listener to basemap select to change the map's basemaps
	var basemaps = $('#basemaps');
	basemaps.on('change', function() {
		setBasemap(this.value);
	});
	map.on('click', onMapClick);
	map.on( "zoomend", function( e ) {
		if (circlePoint != null) {
		circlePoint.setRadius (pointRadious[map.getZoom()+1]);
	    console.log( "zoom level is " + map.getZoom() );
		}
	});
	
}

function onMapClick (e) {
	hideMenu();
	if (!isDrawActive) {
		return;
	}
	if (circlePoint) {
		map.removeLayer (circlePoint);
	}
	circlePoint = L.circle([e.latlng.lat, e.latlng.lng], pointRadious[map.getZoom()+1], {
	    color: 'red',
	    fillColor: '#f03',
	    fillOpacity: 0.5
	}).addTo(map);
	
	circlePoint.on('contextmenu', function(e) {
		selectedPointEvent = e;
		isMenuOpen = true;
	     var y = e.originalEvent.clientY;
	     var x = e.originalEvent.clientX;
		showMenu (y,x);
	});
	
}

function selectLocationCallerShape(selectedGraphic){
	var callerButton = window.opener.callerGisObject;
	//Lat
	callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[1].value = "";
	//Long
	callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[2].value = "";
	callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[1].value = selectedGraphic.latlng.lat;
	callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[2].value =  selectedGraphic.latlng.lng;
	element = callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[1];
	window.opener.postvaluesy(element);
    element = callerButton.parentNode.parentNode.getElementsByTagName("INPUT")[2];
    window.opener.postvaluesx(element);
	window.close();
}


function locate() {
	var location = $("#address").val();
	if (location == null || location.trim() === '' ) {
		return;
	}
	$("#loadingImg").show();
	var location = $("#address").val();
	$.getJSON("/rest/postgis/location/" + location + "/false", function(data) {
		showNewResults(data);
	});
}

function showNewResults(candidates) {
	var geojsonMarkerOptions = {
		radius : 8,
		fillColor : "#ff7800",
		color : "#000",
		weight : 1,
		opacity : 1,
		fillOpacity : 0.8
	};
	// remove all options from the select that filters the type of locations
	$('#fclList').empty();
	if (searchLocationsLayer) {
		searchLocationsLayer.clearLayers();
	}
	var typeValue = $("#fclList option:selected").val();
	searchLocationsLayer = L.geoJson(
			candidates.features,
			{
				pointToLayer : function(feature, latlng) {
					return L.circleMarker(latlng, geojsonMarkerOptions);
			},
				filter : function(feature, layer) {
				var validType = true;
				if (typeValue && feature.geometry.type != typeValue) {
					validType = false;
				}
				return validType && feature.properties.score > 90;
				},
				onEachFeature : function(feature, layer) {
					layer.bindPopup("Location: " + feature.properties.name
							+ "<br>Score: " + feature.properties.score
							+ "<br>FCL:" + feature.geometry.type);
					layer.on('contextmenu', function(e) {
						var y = e.originalEvent.clientY;
					    var x = e.originalEvent.clientX;
					    showMenu (y,x);
						isMenuOpen = true;
						selectedPointEvent = e;
					});
					if (!isInList(feature.geometry.type)) {
						$("#fclList").append($('<option>', {
							value : feature.geometry.type
						}).text(feature.geometry.type));
					}
				}
			}).addTo(map);
	$("#loadingImg").hide();

}

/**
 * Changes the basemap for the map, based on the selection
 * @param basemap, the basemap to set into the map
 */
function setBasemap(basemap) {
	if (basemapLayer) {
		map.removeLayer(basemapLayer);
	}
	if (basemap!= 'None') {
		basemapLayer = L.esri.basemapLayer(basemap);
		map.addLayer(basemapLayer);
		if (basemapLabel) {
			map.removeLayer(basemapLabel);
		}
		if (basemap === 'ShadedRelief' || basemap === 'Oceans'
				|| basemap === 'Gray' || basemap === 'DarkGray'
				|| basemap === 'Imagery' || basemap === 'Terrain') {
			basemapLabel = L.esri.basemapLayer(basemap + 'Labels');
			map.addLayer(basemapLabel);
		}
	}
}

/**
 * Checks if the geometry's type combobox contains the option value received as a parameter
 * @param searchtext, the select options's value to search for
 * @returns {Boolean} true if it contains the option with that value, false otherwise
 */
function isInList(searchtext) {
	var inList = false;
	$("#fclList option").each(function(i) {
		if ($(this).text() == searchtext) {
			inList = true;
			return;
		}
	});
	return inList;
}

/**
 * Removes from the map the locations whose type is different from the one received as a paremeter
 * @param value the fcl for the location, for example: Point
 */
function filterLocation (value) {
	searchLocationsLayer.eachLayer (function (layer) {
		if (layer.feature.geometry.type != value) {
			searchLocationsLayer.removeLayer (layer);
		} 
	});

}

function createPoint () {
	isDrawActive = true;
	$('#map').css("cursor","pointer");
	$('#pointBtn').addClass("button-pressed");
	$('#deactivateBtn').removeClass("button-pressed");
}

function deactivate () {
	isDrawActive = false;
	$('#map').css("cursor","-webkit-grab");
	$('#deactivateBtn').addClass("button-pressed");
	$('#pointBtn').removeClass("button-pressed");

}

function startContextMenu () {
	$(document).bind("mousedown", function (e) {
	    // If the clicked element is not the menu
	    if (!$(e.target).parents(".custom-menu").length > 0) {
	        
	        // Hide it
	        $(".custom-menu").hide(100);
	    }
	});
	// If the menu element is clicked
	$(".custom-menu li").click(function(){
	    // This is the triggered action name
	    switch($(this).attr("data-action")) {
	        // A case for each action
	        case "select": 	selectLocationCallerShape (selectedPointEvent); break;
	        case "remove": map.removeLayer(selectedPointEvent.target); circlePoint = null; break;
	    }
	  
	    // Hide it AFTER the action was triggered
	    $(".custom-menu").hide(100);
	    isMenuOpen = false;
	  });
}

function hideMenu () {
	if ($(".custom-menu").css("display") !="none") {
		$(".custom-menu").hide(100);
		isMenuOpen = false;
	}
}
function showMenu (top,left) {
// Show contextmenu
$(".custom-menu").toggle(100).css({
    top: top + "px",
    left: left + "px"
});
}