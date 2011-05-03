<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=7" /> 
    <!--The viewport meta tag is used to improve the presentation and behavior of the samples 
      on iOS devices-->
    <meta name="viewport" content="initial-scale=1, maximum-scale=1,user-scalable=no"/>
    <title>
    </title>
    <link rel="stylesheet" type="text/css" href="http://serverapi.arcgisonline.com/jsapi/arcgis/2.2/js/dojo/dijit/themes/soria/soria.css">
    <link rel="stylesheet" type="text/css" href="http://serverapi.arcgisonline.com/jsapi/arcgis/2.2/js/dojo/dojox/grid/resources/Grid.css"> 
    <link rel="stylesheet" type="text/css" href="http://serverapi.arcgisonline.com/jsapi/arcgis/2.2/js/dojo/dojox/grid/resources/SoriaGrid.css"> 
   	<digi:ref href="/TEMPLATE/ampTemplate/css_2/mapstyles.css" type="text/css" rel="stylesheet" />
   
    <script type="text/javascript">
      var djConfig = {
        parseOnLoad: true
      };
    </script>
    <script type="text/javascript" src="http://serverapi.arcgisonline.com/jsapi/arcgis/?v=2.2"></script>
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/esrigis/mapfunctions.js"/>"></script>
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/esrigis/Ext.util.DelayedTask-nsRemoved.js"/>"></script>
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/esrigis/esri.ux.layers.ClusterLayer-debug.js"/>"></script>
    
   	
   	
   	 <style type="text/css">
      @import "http://serverapi.arcgisonline.com/jsapi/arcgis/2.2/js/dojo/dijit/themes/claro/claro.css";
      .zoominIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_zoomin.png); width:16px; height:16px; }
      .zoomoutIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_zoomout.png); width:16px; height:16px; }
      .zoomfullextIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_fullextent.png); width:16px; height:16px; }
      .zoomprevIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_previous.png); width:16px; height:16px; }
      .zoomnextIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_next.png); width:16px; height:16px; }
      .panIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_pan.png); width:16px; height:16px; }
      .deactivateIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_decline.png); width:16px; height:16px; }
    </style>
  </head> 
  <body class="soria">
    <div id="mainWindow" dojotype="dijit.layout.BorderContainer" design="headline" gutters="false" style="width:100%; height:100%;">
  	 	<div style="position:absolute; top:10px; z-Index:999;">
  	 		<input type="button" value="Activities" onclick="getContent();" />
  	 		<input type="button" value="Highlight" onclick="getHighlights();" />
  	 	</div>
  	 	
  	 	<div id="navToolbar" dojoType="dijit.Toolbar" style="position:absolute; right:20px; top:10px; z-Index:999;">
			<div dojoType="dijit.form.Button" id="zoomin" iconClass="zoominIcon" onClick="navToolbar.activate(esri.toolbars.Navigation.ZOOM_IN);">Zoom In</div>
			<div dojoType="dijit.form.Button" id="zoomout" iconClass="zoomoutIcon" onClick="navToolbar.activate(esri.toolbars.Navigation.ZOOM_OUT);">Zoom Out</div>
			<div dojoType="dijit.form.Button" id="zoomfullext" iconClass="zoomfullextIcon" onClick="navToolbar.zoomToFullExtent();">Full Extent</div>
		    <div dojoType="dijit.form.Button" id="zoomprev" iconClass="zoomprevIcon" onClick="navToolbar.zoomToPrevExtent();">Prev Extent</div>
		    <div dojoType="dijit.form.Button" id="zoomnext" iconClass="zoomnextIcon" onClick="navToolbar.zoomToNextExtent();">Next Extent</div>
		    <div dojoType="dijit.form.Button" id="pan" iconClass="panIcon" onClick="navToolbar.activate(esri.toolbars.Navigation.PAN);">Pan</div>
		    <div dojoType="dijit.form.Button" id="deactivate" iconClass="deactivateIcon" onClick="navToolbar.deactivate()">Deactivate</div>
 		</div>
 		
       <div id="map" dojotype="dijit.layout.ContentPane" class="roundedCorners" region="center"></div>
       
    </div>  
  </body>
</html>