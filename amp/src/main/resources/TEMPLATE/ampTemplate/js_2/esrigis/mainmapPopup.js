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
var pointRadious = [ 8500, 8500, 8500, 8500, 8500, 8500, 8500, 7500, 5500, 3500, 2000, 1000, 600, 400, 300, 200, 200, 170, 150 ];
var MapConstants = {
	"MapType" : {
		"BASE_MAP" : 1
	},
	"MapSubType" : {
		"BASE" : 1,
		"INDICATOR" : 2,
		"OSM" : 3
	},
	"SHAPE" : {
		"POINT" : "Point",
		"POLYGON" : "Polygon",
		"POLYLINE" : "Polyline"
	}
};
var basemapUrl;
var isOsm = false;
var isFirstSelect = true;
var tempId = 1;
var labels = {};
var DEFAULT_STRUCTURE_COLOR = '#3388ff';

function MapPopup(lat, long) {
	latitude = lat;
	longitude = long;
	initMap();
	startContextMenu();
}

function initMap() {
	$.getJSON("/esrigis/datadispatcher.do?getconfig=true", function() {
		console.log("Success retrieving Gazeteer map config");
	}).done(function(jsonData) {
		if (jsonData.length === 0) {
			isOsm = true;
			loadBaseMap();
			return;
		}
		$.each(jsonData, function(key, map) {
			switch (map.mapType) {
			case MapConstants.MapType.BASE_MAP:
				basemapurl = map.mapUrl;
				if (basemapurl === undefined || map.mapSubType == MapConstants.MapSubType.OSM) {
					isOsm = true;
				}
				break;
			default:
				break;
			}
		});

		loadBaseMap();
	}).fail(function() {
		console.log("Error retrieving map configuration");
	});

}

function loadBaseMap() {
    map = L.map('map').setView([latitude, longitude], 7);
    // create the tile layer with correct attribution
    var osmUrl = 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
    if (isOsm) {
        var subdomains = ['a', 'b', 'c'];
        if (basemapurl !== undefined && basemapurl.indexOf("mqcdn") != -1) {
            subdomains = ['otile1', 'otile2', 'otile3', 'otile4'];
        }
        var osmAttrib = 'Map data © <a href="http://openstreetmap.org">OpenStreetMap</a> contributors';
        tileLayer = new L.TileLayer(osmUrl, {
            minZoom: 0,
            maxZoom: 16,
            attribution: osmAttrib,
            subdomains: subdomains
        });
    } else {
        tileLayer = L.esri.tiledMapLayer({
            url: basemapurl,
            maxZoom: 16
        });
    }

    map.addLayer(tileLayer);
    currentZoom = map.getZoom();

    // attach listener to basemap select to change the map's basemaps
    var basemaps = $('#basemaps');

    basemaps.on('change', function () {
        setBasemap(this.value);
    });
    var drawnItems = L.featureGroup().addTo(map);

    addDrawControls(drawnItems);

    // draw an existing activity structure
    if (window.opener.structuresData.structure) {
        drawStructure(window.opener.structuresData.structure);
    }

}

function drawStructure(structure) {
    var structureImage;
    var labelLatLng;

    if (structure.shape === MapConstants.SHAPE.POINT) {
        structureImage = generatePointStructureImage(structure);
        labelLatLng = L.latLng(structure.latitude, structure.longitude);
    } else {
        var colorValue = structure["color-value"];
        if (structure.shape === MapConstants.SHAPE.POLYLINE) {
            structureImage = generatePolylineStructureImage(structure, colorValue);
            labelLatLng = structureImage.getBounds().getCenter();
        } else if (structure.shape == MapConstants.SHAPE.POLYGON) {
            structureImage = generatePolygonStructureImage(structure, colorValue);
            labelLatLng = structureImage.getBounds().getCenter();
        }
    }

    if (structureImage) {
        structureImage.on('contextmenu', contextMenu);

        if (!structureImage.tempId) {
            structureImage.tempId = tempId++;
        }

        structureImage.addTo(map);
        drawLabel(structureImage, labelLatLng, structure.title);
    }
}

function generatePointStructureImage(structure) {
    return L.marker([structure.latitude, structure.longitude]);
}

function generatePolylineStructureImage(structure, colorValue) {
    return new L.Polyline(getPointList(structure), {color : colorValue});
}

function generatePolygonStructureImage(structure, colorValue) {
    return new L.Polygon(getPointList(structure), {color : colorValue});
}

function getPointList(structure) {
    var pointList = [];

    structure.coordinates.forEach(function(coordinate) {
        pointList.push(new L.LatLng(coordinate.latitude, coordinate.longitude));
    });

    return pointList;
}

function drawLabel(graphic, latLng, title) {
    if (latLng) {
        var label = L.marker(latLng, {
            icon: L.divIcon({
                iconSize: null,
                className: 'label',
                html: '<div>' + title + '</div>'
            })
        }).addTo(map);

        labels[graphic.tempId] = label;
    }
}

function onMapClick(e) {
	hideMenu();
	if (!isDrawActive) {
		return;
	}
	if (circlePoint) {
		map.removeLayer(circlePoint);
	}
	circlePoint = L.circle([ e.latlng.lat, e.latlng.lng ], pointRadious[map.getZoom() + 1], {
		color : 'red',
		fillColor : '#f03',
		fillOpacity : 0.5
	}).addTo(map);

	circlePoint.on('contextmenu', contextMenu);

}

function contextMenu(e) {
    selectedPointEvent = e;
    isMenuOpen = true;
    var y = e.originalEvent.clientY;
    var x = e.originalEvent.clientX;
    showMenu(y, x);
}

function createColorCheckboxes(selectedGraphic) {
	if (window.opener.structuresData) {
		$('.colors').html('');
		window.opener.structuresData.structureColors.forEach(function(c) {
			appendColor(c);
		});

		$('.color-checkbox').click(function(event ){

			if ($(this).data('wasChecked')) {
				$(this).prop('checked', false);
			}

			$(this).data('wasChecked', event.target.checked);
		    if (event.target.checked === true) {
		    	selectedGraphic.target.setStyle({color: event.target.dataset.color});
		    } else {
		    	selectedGraphic.target.setStyle({color: DEFAULT_STRUCTURE_COLOR});
		    }
		});
	}
}
function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}

/**
 * TODO replace the places we are searching by index to search by input name
 * @param row
 * @param elementName
 * @returns {*}
 */
function getRowElement(row, elementName) {
	var elements = row.getElementsByTagName("INPUT");
	var result;
	for (var i = 0; i < elements.length; i++) {

		if (elements[i].name.indexOf(elementName) >= 0) {
			result = elements[i];
			break;
		}
	}
	return result;
}
function selectLocationCallerShape(selectedGraphic) {
	$("#errorMsg").html("");
	var callerButton = window.opener.callerGisObject;
	var row = findRow(selectedGraphic);

	//set temporary client side id used for identifying structures and rows
	if (selectedGraphic.target.tempId == null) {
		selectedGraphic.target.tempId = tempId++;
	}

	if (selectedGraphic.target instanceof L.Marker || selectedGraphic.target instanceof L.CircleMarker) {
		$("#colors-section").hide();
	} else {
		$("#colors-section").show();
	}

	 $("#locationTitleDialog").dialog({
		"title" : TranslationManager.getTranslated("Select Structure"),
		open : function(event, ui) {
			$("#locationTitle").val('');
			createColorCheckboxes(selectedGraphic);
			if (row) {
				var title = row.getElementsByTagName("INPUT")[0];
				$("#locationTitle").val(title.value);
				var structureColor = getRowElement(row, 'structureColorId');
				if (structureColor && structureColor.value) {
					$(".color-checkbox:radio[value='"+ structureColor.value +"']").attr("checked", true);
				}
			}
		},
		buttons : [ {
			text : TranslationManager.getTranslated('Close'),
			click : function() {
				$(this).dialog("close");
			}
		}, {
			text : TranslationManager.getTranslated('Submit'),
			click : function() {
				if (isBlank($("#locationTitle").val()) ) {
					$("#errorMsg").html(TranslationManager.getTranslated('Title is a required field'));
					return;
				}

				updateStructure(selectedGraphic, $("#locationTitle").val());
				isFirstSelect = false;
				// if row does not exist, trigger click on add structure button to add row on structures table in AF
                //this confirmation is added just in case the user closes the window before the content is submited
				//and after pressing the submit button
				window.onbeforeunload = function() {
                    return confirm('Are you sure you want to leave the current page?');
                }
				if (row == null) {
						callerButton.ownerDocument.getElementsByClassName('addStructure')[0].click();
						setTimeout(function() {
							var rows = callerButton.ownerDocument.getElementsByClassName('structureRow');
							row = rows[rows.length - 1];
							updateActivityForm(row, selectedGraphic);
						}, 3000);
				} else {
						updateActivityForm(row, selectedGraphic);
				}
				$(this).dialog("close");
			}
		} ]
	});
}

function updateActivityForm(row, selectedGraphic) {

	var title = row.getElementsByTagName("INPUT")[0];
	title.value = $("#locationTitle").val();
	window.opener.postvaluesx(title);

	var latitudeInput = row.getElementsByTagName("INPUT")[1];
	latitudeInput.value = "";

	var longitudeInput = row.getElementsByTagName("INPUT")[2];
	longitudeInput.value = "";

	var coordsInput = row.getElementsByTagName("INPUT")[5];
	coordsInput.value = "";

	var shapeInput = row.getElementsByTagName("INPUT")[6];
	shapeInput.value = "";

	var tempIdInput = row.getElementsByTagName("INPUT")[7];
	tempIdInput.value = selectedGraphic.target.tempId;
	window.opener.postvaluesx(tempIdInput);

	var structureColorInput = getRowElement(row, 'structureColorId');

	var selectedColors = document.querySelectorAll('.color-checkbox:checked');
	if (selectedColors.length > 0){
		structureColorInput.value = parseInt(selectedColors[0].value);
	} else {
		structureColorInput.value = null;
	}
	window.opener.postvaluesx(structureColorInput);

	if (selectedPointEvent.target instanceof L.Marker || selectedGraphic.target instanceof L.CircleMarker) {
		latitudeInput.value = selectedGraphic.latlng.lat;
		longitudeInput.value = selectedGraphic.latlng.lng;
		shapeInput.value = MapConstants.SHAPE.POINT;
		window.opener.postvaluesy(latitudeInput);
		window.opener.postvaluesx(longitudeInput);
		window.opener.postvaluesx(shapeInput);
		coordsInput.value = JSON.stringify({
			'coordinates' : []
		});
		window.opener.postvaluesx(coordsInput);
	} else {
		var latLngs = selectedGraphic.target.getLatLngs();
		var data = []
		for (i = 0; i < latLngs.length; i++) {
			var obj = {}
			obj.latitude = latLngs[i].lat;
			obj.longitude = latLngs[i].lng;
			data.push(obj)
		}
		coordsInput.value = JSON.stringify({
			'coordinates' : data
		});
		window.opener.postvaluesx(coordsInput);
		if (selectedPointEvent.target instanceof L.Polyline) {
			if (selectedPointEvent.target instanceof L.Polygon) {
				shapeInput.value = MapConstants.SHAPE.POLYGON;
			} else {
				shapeInput.value = MapConstants.SHAPE.POLYLINE;
			}
		}
		window.opener.postvaluesx(shapeInput);
	}

	fireChangeEvent(coordsInput);
	fireChangeEvent(latitudeInput);
	fireChangeEvent(longitudeInput);
	fireChangeEvent(title);
	fireChangeEvent(tempIdInput);
	fireChangeEvent(shapeInput);
	fireChangeEvent(structureColorInput);
    window.onbeforeunload = null;
}

function fireChangeEvent(element) {
	if ("createEvent" in document) {
		var evt = document.createEvent("HTMLEvents");
		evt.initEvent("change", false, true);
		element.dispatchEvent(evt);
	} else {
		element.fireEvent("onchange");
	}
}

function updateStructure(selectedGraphic, title) {
	var label = labels[selectedGraphic.target.tempId];
	if (label) {
		label.setIcon(L.divIcon({
			iconSize : null,
			className : 'label',
			html : '<div>' + title + '</div>'
		}));

	} else {
		label = L.marker(selectedGraphic.latlng, {
			icon : L.divIcon({
				iconSize : null,
				className : 'label',
				html : '<div>' + title + '</div>'
			})
		}).addTo(map);
	}

	labels[selectedGraphic.target.tempId] = label;
}

function removeStructureLabel(selectedGraphic) {
	var label = labels[selectedGraphic.target.tempId];
	if (label) {
		map.removeLayer(label);
	}
}

function findRow(selectedGraphic) {
	var callerButton = window.opener.callerGisObject;
	var rows = callerButton.ownerDocument.getElementsByClassName('structureRow');

    if (isFirstSelect && rows.length !== 0) {
    	return callerButton.parentNode.parentNode;
	}

	for (var i = 0; i < rows.length; i++) {
		var tempIdInput = rows[i].getElementsByTagName("INPUT")[7];
		if (tempIdInput.value == selectedGraphic.target.tempId) {
			return rows[i];
		}

	}

	return null;
}

function locate() {
	var location = $("#address").val();
	if (location == null || location.trim() === '') {
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
	searchLocationsLayer = L.geoJson(candidates.features, {
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
			layer.bindPopup("Location: " + feature.properties.name + "<br>Score: " + feature.properties.score + "<br>FCL:" + feature.geometry.type);
			layer.on('contextmenu', function(e) {
				var y = e.originalEvent.clientY;
				var x = e.originalEvent.clientX;
				showMenu(y, x);
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
	if (basemap != 'None') {
		basemapLayer = L.esri.basemapLayer(basemap);
		map.addLayer(basemapLayer);
		if (basemapLabel) {
			map.removeLayer(basemapLabel);
		}
		if (basemap === 'ShadedRelief' || basemap === 'Oceans' || basemap === 'Gray' || basemap === 'DarkGray' || basemap === 'Imagery' || basemap === 'Terrain') {
			basemapLabel = L.esri.basemapLayer(basemap + 'Labels');
			map.addLayer(basemapLabel);
		}
	}
}

function addDrawControls(drawnItems) {
	map.addControl(new L.Control.Draw({
		edit : {
			featureGroup : drawnItems,
			poly : {
				allowIntersection : false
			}
		},
		draw : {
			polygon : {
				allowIntersection : false,
				showArea : true
			},
			circle : false
		}
	}));

	map.on(L.Draw.Event.CREATED, function(event) {
		var layer = event.layer;
		layer.on('contextmenu', function(e) {
			e.layerz = layer;
			selectedPointEvent = e;
			isMenuOpen = true;
			var y = e.originalEvent.clientY;
			var x = e.originalEvent.clientX;
			showMenu(y, x);

		});
		drawnItems.addLayer(layer);
	});
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
function filterLocation(value) {
	searchLocationsLayer.eachLayer(function(layer) {
		if (layer.feature.geometry.type != value) {
			searchLocationsLayer.removeLayer(layer);
		}
	});

}

function appendColor(categoryValue) {
	var colorHTML = getColorHTMLTemplate();
	colorHTML = colorHTML.replace('{value}', categoryValue.id);
	var translatedValue = TranslationManager.getTranslated(categoryValue.value);
	var splits = translatedValue.split(":");
	if (splits.length == 2) {
		colorHTML = colorHTML.replace('{color}', splits[0]).replace('{color}', splits[0]).replace('{name}', splits[1]);
		$('.colors').append(colorHTML);
	}
}

function getColorHTMLTemplate() {
	return '<li><input type="radio" class="color-checkbox" name="structure-color" id="structure-color" value="{value}" data-color={color}><svg width="24" height="24"> <rect style="fill:{color}" width="24" height="24" x="0" y="5"></rect></svg><label class="color-label">{name}</label></li>';
}

function startContextMenu() {
	$(document).bind("mousedown", function(e) {
		// If the clicked element is not the menu
		if (!$(e.target).parents(".custom-menu").length > 0) {

			// Hide it
			$(".custom-menu").hide(100);
		}
	});
	// If the menu element is clicked
	$(".custom-menu li").click(function() {
		// This is the triggered action name
		switch ($(this).attr("data-action")) {
		// A case for each action
		case "select":
			selectLocationCallerShape(selectedPointEvent);
			break;
		case "remove":
			removeStructure(selectedPointEvent);
			circlePoint = null;
			break;
		}

		// Hide it AFTER the action was triggered
		$(".custom-menu").hide(100);
		isMenuOpen = false;
	});
}

function removeStructure(selectedPointEvent) {
	// trigger click on delete button to remove row on structures table in AF
	var deleteIcon = findDeleteIcon(selectedPointEvent);
	if (deleteIcon) {
		deleteIcon.click();
	}

	removeStructureLabel(selectedPointEvent);
	map.removeLayer(selectedPointEvent.target);
}

function findDeleteIcon(selectedPointEvent) {
	var row = findRow(selectedPointEvent);
	if (row) {
		var images = row.getElementsByTagName('IMG');
		for (var i = 0; i < images.length; i++) {
			if (images[i].src.endsWith("ico_del.gif")) {
				return images[i];
			}
		}
	}

	return null;
}

function hideMenu() {
	if ($(".custom-menu").css("display") != "none") {
		$(".custom-menu").hide(100);
		isMenuOpen = false;
	}
}
function showMenu(top, left) {
	// Show contextmenu
	$(".custom-menu").toggle(100).css({
		top : top + "px",
		left : left + "px"
	});
}
