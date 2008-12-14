jQuery.fn.inspect = function(output) {
  jQuery.inspect($(this), output);
  return this;
}
 
jQuery.inspect = function(obj, output) {  
  output = (output ? output : 'alert')
  var text = "";
  var _build = null;
  var _dump = null;
 
  switch (output) {
    case 'console':
      _build = jQuery.inspect._buildText;
      _dump = jQuery.inspect._console;
      break;
    case 'window':
      _build = jQuery.inspect._buildHTML;
      _dump = jQuery.inspect._window;
      break;
    default:
     _build = jQuery.inspect._buildText;
      _dump = jQuery.inspect._alert;
  }
 
  switch (typeof obj) {
    case 'string':
      text = "String: " + obj;
      break;
    case 'number':
      text = "Number: " + obj;
      break;
    case 'boolean':
      text = "Boolean: " + obj;
      break;
    case 'undefined':
      alert('Object is undefined');
      return true;
    default:
      text = jQuery.inspect._parseObject(obj, _build);
  }
 
  _dump(text);
}
 
jQuery.inspect._parseObject = function(obj, _dumpTo) {
  var text = ""
  for (field in obj) {
    try {
      text += _dumpTo(field, obj[field]);
   }
    catch (err) {
      // do nothing
   }
  }
 
  return text;
}
 
jQuery.inspect._buildText = function(key, value) {
  return key + ":" + value + "\n";
}
 
jQuery.inspect._buildHTML = function(key, value) {
  return "<tr><td>" + key + "</td><td>" + value + "</td></tr>\n";
}
 
jQuery.inspect._console = function(text) {
  console.log(text);
}
 
jQuery.inspect._alert = function(text) {
  alert(text);
}
 
jQuery.inspect._window = function(text) {
  text = "<html><head>" + jQuery.inspect._windowSettings.styles + "</head><body><table>" + text + "</table></body></html>";
  dump_window = window.open('', '', jQuery.inspect._windowSettings.config);
  dump_window.document.write(text);
  dump_window.document.close();
  dump_window.focus();
}
 
jQuery.inspect._windowSettings = jQuery.extend({
  width: 800,
  height: 600
});
 
jQuery.inspect._windowSettings.styles = "\
<style> \
  * { \
   margin: 0; \
  } \
  html, body { \
   height: 100%; \
   text-align: center; \
   margin-bottom: 1px; \
   font-family: verdana,helvetica,sans-serif; \
  } \
  table { \
    width: " + (jQuery.inspect._windowSettings.width - 20) + "px; \
    border: 1px solid black; \
} \
td { \
vertical-align: top; \
} \
</style>";
 
jQuery.inspect._windowSettings.config = "width=" + jQuery.inspect._windowSettings.width + ",height=" + jQuery.inspect._windowSettings.height + ",scrollbars=yes,location=no,menubar=no,toolbar=no";

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
	var inputname = "geo_level1s[" + this.id + "]";
	var input = $('<input type="hidden" value="1" />').attr("name", inputname);
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
    var inputname = "geo_level2s[" + this.id + "]";
	  var input = $('<input type="hidden" value="1" />').attr("name", inputname);

    this.dom_id = "municipality_" + this.id;
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
