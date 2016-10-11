<%@page import="org.digijava.module.aim.helper.GlobalSettingsConstants"%>
<%@page import="org.digijava.module.aim.util.FeaturesUtil"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7,IE=9" />
<!--The viewport meta tag is used to improve the presentation and behavior of the samples on iOS devices-->
<meta name="viewport"
	content="initial-scale=1, maximum-scale=1,user-scalable=no" />
<style type="text/css">
a {
	color: blue;
}
</style>
<digi:ref
	href="/TEMPLATE/ampTemplate/gisModule/dev/node_modules/leaflet/dist/leaflet.css"
	type="text/css" rel="stylesheet" />
<script type="text/javascript"
	src="<digi:file src="/TEMPLATE/ampTemplate/gisModule/dev/node_modules/leaflet/dist/leaflet.js"/>"></script>
<script type="text/javascript"
	src="<digi:file src="/TEMPLATE/ampTemplate/gisModule/dev/node_modules/esri-leaflet/dist/esri-leaflet.js"/>"></script>

<script type="text/javascript">
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
      
      var dialogBox, tooltipDialog;
      var map;
    
     var myPanelMap = new YAHOO.widget.Panel("mapPanel", {
			width :"407px",
			height:"380px",
			fixedcenter :true,
			constraintoviewport :true,
			underlay :"none",
			close :true,
			visible :false,
			modal :true,
			draggable :true
		});
	 function openPopup () {
		 myPanelMap.hideEvent.subscribe(function(o) {
			    $('#locationPopupMap').css("visibility", 'hidden');
			    $('#ashowmap').html('<digi:trn jsFriendly="true">Show Map</digi:trn>')
			});
		var element = document.getElementById("locationPopupMap");
		myPanelMap.setBody(element);
		myPanelMap.render();
		myPanelMap.center();
		$('#locationPopupMap').css("visibility", 'visible');
		$('#locationPopupMap').width(400);
		$('#locationPopupMap').height(350);
		map._onResize();
		
		myPanelMap.show();
	 }
      
      function showMapInPopup(node) {
         node.innerHTML = '<digi:trn jsFriendly="true">Hide Map</digi:trn>';
		 openPopup();
      }
      
      function createMap() {
        var basemapurl = null;
    	
    	$.getJSON( "/esrigis/datadispatcher.do?getconfig=true", function() {
  		  console.log( "Success retrieving map config" );
  		})
  		  .done(function(jsonData) {
  			  $.each( jsonData, function(key,map) {
  			   		switch (map.mapType) {
  					case MapConstants.MapType.BASE_MAP:
  						basemapurl = map.mapUrl;
  						if (map.mapSubType == MapConstants.MapSubType.OSM){
  							isOsm = true;
  						}
  						break;
  					default:
  						break;
  					}
  			   	  });
  			  loadBaseMap(basemapurl);
  		  })
  		  .fail(function() {
  		    console.log( "Error retrieving map configuration" );
  		  });

	 }

	function loadBaseMap(basemapurl) {
		map = L.map('locationPopupMap').setView([<%=FeaturesUtil.getGlobalSettingDouble(GlobalSettingsConstants.COUNTRY_LATITUDE)%>,<%=FeaturesUtil.getGlobalSettingDouble(GlobalSettingsConstants.COUNTRY_LONGITUDE)%> ], 7);
		var tileLayer;
		if (isOsm) {
			var osmAttrib='Map data � <a href="http://openstreetmap.org">OpenStreetMap</a> contributors';
			tileLayer = new L.TileLayer(basemapurl, {minZoom: 0, maxZoom: 16, attribution: osmAttrib});
		}
		else {
			tileLayer = new L.TileLayer(basemapurl, {minZoom: 0, maxZoom: 16});
		}
		map.addLayer(tileLayer);
        for ( var i = 0; i < coordinates.length; i++) {
            coord = coordinates[i].split(";");
            if (coord[0] != '' && coord[1] != '') {
                var circle = L.circleMarker([coord[0], coord[1]], 500, {
                    color: 'red',
                    fillColor: 'red',
                    fillOpacity: 0.5
                }).addTo(map);
                circle.bindPopup("<b>Location Information</b>:<br>Location: " + coord[2]);
            }
        }

    }
	
	YAHOO.amptab.initPanels	= function () {
		createMap ();
		var msg='\n<digi:trn jsFriendly="true">Location Map</digi:trn>';
		myPanelMap.setHeader(msg);
		myPanelMap.setBody("Example");
		myPanelMap.render(document.body);
		$('#locationPopupMap').css("visibility", 'hidden');

	};
 
 YAHOO.util.Event.addListener(window, "load", YAHOO.amptab.initPanels) ;
     
 </script>
</head>

<body>
	<a id="ashowmap" onclick="showMapInPopup(this);"
		style="cursor: pointer;"><digi:trn>Show Map</digi:trn></a>
</body>
</html>