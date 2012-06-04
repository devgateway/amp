<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7,IE=9" />
    <!--The viewport meta tag is used to improve the presentation and behavior of the samples on iOS devices-->
    <meta name="viewport" content="initial-scale=1, maximum-scale=1,user-scalable=no"/>
    <style type="text/css"> a { color: blue; } </style>

    
    <script type="text/javascript">
    if( typeof dojo != 'undefined' )
    	{
    	  dojo.require("esri.map");
          dojo.require("dijit.Dialog");
          dojo.require("dijit.TooltipDialog");
    	
    	}
    
      
      var dialogBox, tooltipDialog;
      var map;
    
      /****************
       * TooltipDialog
       ****************/
      
      function showMapInTooltipDialog(node) {
        if (!tooltipDialog) {
          var htmlFragment = '<div></div>';
          htmlFragment += '<div id="map" style="width:400px; height:350px; border: 1px solid #A8A8A8;"></div>';

          // CREATE TOOLTIP DIALOG
          tooltipDialog = new dijit.TooltipDialog({
            content: htmlFragment,
            autofocus: !dojo.isIE, // NOTE: turning focus ON in IE causes errors when reopening the dialog
            refocus: !dojo.isIE
          });
          
          // DISPLAY TOOLTIP DIALOG AROUND THE CLICKED ELEMENT
          dijit.popup.open({ popup: tooltipDialog, around: node });
          tooltipDialog.opened_ = true;
          node.innerHTML = '<digi:trn>Hide Map</digi:trn>';

          // CREATE MAP
          createMap();
        }
        else {
          if (tooltipDialog.opened_) {
            dijit.popup.close(tooltipDialog);
            tooltipDialog.opened_ = false;
            node.innerHTML = '<digi:trn>Show Map</digi:trn>';
          }
          else {
            dijit.popup.open({ popup: tooltipDialog, around: node });
            tooltipDialog.opened_ = true;
            node.innerHTML = "Hide";
          }
        }
      }
      
      function createMap() {
		// ADD LAYERS
        var basemapUrl;
    	var mapurl;
    	
    	
    	var xhrArgs = {
    			url : "/esrigis/datadispatcher.do?getconfig=true",
    			handleAs : "json",
    			sync:true,
    			load: function(jsonData) {
    				   dojo.forEach(jsonData,function(map) {
    			        	switch (map.maptype) {
    						case 1:
    							basemapUrl = map.mapurl;
    							break;
    						case 2:
    							mapurl = map.mapurl;
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
    	
    	var basemap = new esri.layers.ArcGISTiledMapServiceLayer(basemapUrl, {id:'base'}); // Levels at which this layer will be visible);
    	countrymap = new esri.layers.ArcGISDynamicMapServiceLayer(mapurl, {opacity : 0.90,id:'liberia'});


    	var layerLoadCount = 0;
		if (basemap.loaded) {
			layerLoadCount += 1;
			if (layerLoadCount === 2) {
				createMapAddLayers(basemap, countrymap);
			}
		} else {
			dojo.connect(basemap, "onLoad", function(service) {
				layerLoadCount += 1;
				if (layerLoadCount === 2) {
					createMapAddLayers(basemap, countrymap);
				}
			});
		}
		if (countrymap.loaded) {
			layerLoadCount += 1;
			if (layerLoadCount === 2) {
				createMapAddLayers(basemap, countrymap);
			}
		} else {
			dojo.connect(countrymap, "onLoad", function(service) {
				layerLoadCount += 1;
				if (layerLoadCount === 2) {
					createMapAddLayers(basemap, countrymap);
				}
			});
		}
	 }

	function createMapAddLayers(myService1, myService2) {
    		// create map
    		// convert the extent to Web Mercator
    		map = new esri.Map("map", {
    			extent : esri.geometry.geographicToWebMercator(myService2.fullExtent)
    		});

    		dojo.connect(map, 'onLoad', function(map) {
    			for ( var i = 0; i < coordinates.length; i++) {
    		 		coord = coordinates[i].split(";");
    		 		addPoints(coord[1],coord[0]);
    			 }
    			 map.infoWindow.resize(200, 100);
        	});	
    		map.addLayer(myService1);
    		map.addLayer(myService2);
    	}
    		
  	
      // ADD LOCATIONS TO THE MAP
     function addPoints(xloc,yloc){
    	 var pt = new esri.geometry.Point(xloc,yloc,new esri.SpatialReference({"wkid":4326}));
    	 var sms = new esri.symbol.SimpleMarkerSymbol().setStyle(esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE).setColor(new dojo.Color([255,0,0,0.5]));
     	 var attr = {};
     	 var infoTemplate = new esri.InfoTemplate("Activity Information");   
     	 var transpt = esri.geometry.geographicToWebMercator(pt);
     	 var graphic = new esri.Graphic(transpt,sms,attr,infoTemplate);
     	 map.graphics.add(graphic);
 	 }
     
 </script>
</head>
  
  <body class="claro" style="font-family: Arial Unicode MS,Arial,sans-serif;">
   	<a onclick="showMapInTooltipDialog(this);" style="cursor: pointer;"><digi:trn>Show Map</digi:trn></a>
  </body>
</html>