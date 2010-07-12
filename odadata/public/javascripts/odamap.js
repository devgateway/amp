// General JS extensions
Array.prototype.remove = function(from, to) {
  var rest = this.slice((to || from) + 1 || this.length);
  this.length = from < 0 ? this.length + from : from;
  return this.push.apply(this, rest);
};

// Locations Object
function Locations() {
    this.regions = new Array();
}

Locations.prototype.add_region = function(region) {
    this.regions.push(region);
    //vectors.addFeatures(region.feature);
}

Locations.prototype.remove_region = function(idx) {
    region = this.regions[idx];

    // Remove eventually existing vector features
    //for (i = 0; i < region.municipalities.length; i++) {
        //vectors.removeFeatures(region.municipalities[i].feature);
    //}

    // Remove this region's overlay
    //vectors.removeFeatures(region.feature);

    this.regions.remove(idx);
}



// Region Object
function Region(loc_obj, loc_geom) {
	this.id = loc_obj.id;
	this.name = loc_obj.name;
	this.municipalities = new Array();
	this.dom_id = "region_" + this.id;
  
	// Generate HTML elements
	var input = $('<input type="hidden" />')
	  .attr("name", "provinces[]")
	  .attr("value", this.id);
	var muc_list_item = $('<li class="all">All Municipalities</li>').append(input);

	this.muc_list = $('<ul></ul>').append(muc_list_item);
	this.dom_element = 
	    $("<li></li>")
	        .attr("id", this.dom_id)
	        .text(this.name)
	        .append(this.muc_list);
  
    // Generate vector overlay
    //this.feature = new OpenLayers.Format.WKT().read(loc_geom);
}

Region.prototype.add_municipality = function(muc) {
    // Add to municipalities array
    this.municipalities.push(muc);
    // Remove All Municipalities placeholder
    this.muc_list.children("li.all").remove();
    // Remove region vector overlay
    //vectors.removeFeatures(this.feature);
    // Append to list
    this.muc_list.append(muc.dom_element); 
    // Add vector overlay
    //vectors.addFeatures(muc.feature);
}

Region.prototype.remove_municipality = function(idx) {
    // Remove DOM element
    this.municipalities[idx].dom_element.remove();
    // Remove vector overlay
    //vectors.removeFeatures(this.municipalities[idx].feature);

    this.municipalities.remove(idx);
}



// Municipality Object
function Municipality(loc_obj, loc_geom) {
    this.id = loc_obj.id;
    this.name = loc_obj.name;

    // Generate HTML element
    var input = $('<input type="hidden" />')
  	  .attr("name", "districts[]")
  	  .attr("value", this.id);

    this.dom_id = "district_" + this.id;
    this.dom_element = $("<li></li>").text(this.name).append(input);

    // Generate vector overlay
    //this.feature = new OpenLayers.Format.WKT().read(loc_geom);
}

var map, layer;
var locations = new Locations();

function show_loading_indicator() {
    $("#loading").show();
}

function hide_loading_indicator() {
    $("#loading").hide();
}

function update_locations(loc) {
	if (!loc.level1 || !loc.level2) {
	    alert("Sorry, no location has been found for these coordinates! Please try again.");
	    return;
	}
	
	var province = loc.level1.province;
	var district = loc.level2.district;
	var parent = false;
	var muc_found = false;
    
	// Open Location Box
	$('#location_trigger').click();
    
	// Check whether Region has been added already
	$.each(locations.regions, function(i, region) {
		if((region.id == province.id)) {
		    // Remove if only regions are shown
		    if (map.getResolution() > 0.004) {
		 	    locations.remove_region(i);
		 	}
			parent = region;
		}
	});

	// Location has not been added, yet
	if (parent == false) {
	    parent = new Region(province, loc.level1_geom); 
	    locations.add_region(parent);
	}

	// Municipalities are shown - add or remove
	if (map.getResolution() <= 0.004) {
	    // Check whether Municipio has been added already
    	$.each(parent.municipalities, function(i, municipio) {
    		if((municipio.id == district.id)) {
    		    // Remove Municipality if found, and region if no Municipalities are left
    		 	parent.remove_municipality(i);
    		 	if (parent.municipalities.length == 0) {
    		 	    var id;
    		 	    for (i = 0; i < locations.regions.length; i++) {
    		 	        if (locations.regions[i].id == parent.id) {
    		 	            id = i;
    		 	        }
    		 	    }

    		 	    locations.remove_region(id);
    		 	}
    		 	muc_found = true;
    		}
    	});

    	// Add if it has not been found
    	if (muc_found == false) {
    	    var muc = new Municipality(district, loc.level2_geom);
    	    parent.add_municipality(muc);
    	}    	
    }

	$("#locations").children().remove();

	$.each(locations.regions, function(i, region) {
		$("#locations").append(region.dom_element);
	});		
}

var initAttributeSliders = function() {
	$('div#criteria> div:not(.default)').hide(); 
	$('div#criteria> h3').click(function() {			
    	$(this).next('div:not(:visible)').slideToggle('fast')
    	.siblings('div:visible').slideUp('fast');
		$('h3.selected').removeClass('selected');
		$(this).addClass('selected');
  	}).hover(
		function () {
			$(this).addClass('hover');
		}, 
		function () {
	  		$(this).removeClass('hover');
		}
   );
};

var initMap = function() {
	var options = {
		controls: [new OpenLayers.Control.MouseDefaults()],
		maxExtent: new OpenLayers.Bounds(26.684477, -27.278632, 44.372215, -9),
		resolutions: [0.0468748232421875, 0.0234374116210938, 0.0117187058105469, 0.00585935290527344, 0.00292967645263672],
		center: new OpenLayers.LonLat(34.95, -18,20)
	}


	map = new OpenLayers.Map( 'map', options );
	layer = new OpenLayers.Layer.WMS( "Nicaragua",
	        "/odamap/wxs", {layers: 'basic', format: 'png'},
			{singleTile: false, transitionEffect: 'resize'});		

	//map.addControl( new OpenLayers.Control.LayerSwitcher() );
	map.addControl(new OpenLayers.Control.PanZoomBar());
	//vectors = new OpenLayers.Layer.Vector("Selected Features");
	//map.addLayers([layer, vectors]);
	map.addLayer(layer);

	// Add locations to list
	map.events.register('click', map, function (e) {
		var coord = new OpenLayers.Pixel(e.xy.x, e.xy.y);
		var lonlat = map.getLonLatFromPixel(coord);

		$.ajax({
		  async: false,
			cache: false,
			data: { lon: lonlat.lon, lat: lonlat.lat },
			dataType: "json",
			url: "/odamap/query",
			beforeSend: function(XMLHttpRequest) {
			  show_loading_indicator();
			},
			success: function(data, textStatus) {
				update_locations(data);
			},
			complete: function(XMLHttpRequest, textStatus) {
				hide_loading_indicator();
			}
		});
	});

	map.zoomToMaxExtent();
};