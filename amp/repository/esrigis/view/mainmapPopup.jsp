<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <digi:instance property="datadispatcherform" />
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=7" /> 
    <!--The viewport meta tag is used to improve the presentation and behavior of the samples 
      on iOS devices-->
    <meta name="viewport" content="initial-scale=1, maximum-scale=1,user-scalable=no"/>
    <title>
    </title>
    <link rel="stylesheet" type="text/css" href="<c:out value="${datadispatcherform.apiurl}"/>/jsapi/arcgis/3.0/js/dojo/dijit/themes/soria/soria.css">
    <link rel="stylesheet" type="text/css" href="<c:out value="${datadispatcherform.apiurl}"/>/jsapi/arcgis/3.0/js/dojo/dojox/grid/resources/Grid.css"> 
    <link rel="stylesheet" type="text/css" href="<c:out value="${datadispatcherform.apiurl}"/>/jsapi/arcgis/3.0/js/dojo/dojox/grid/resources/SoriaGrid.css"> 
    <digi:ref href="/TEMPLATE/ampTemplate/css_2/amp.css" type="text/css" rel="stylesheet" />
   	<digi:ref href="/TEMPLATE/ampTemplate/css_2/mappopupstyles.css" type="text/css" rel="stylesheet" />
   
    <script type="text/javascript">
      var djConfig = {
        parseOnLoad: true
      };
      
    </script>
    <script type="text/javascript" src="<c:out value="${datadispatcherform.apiurl}"/>/jsapi/arcgis/?v=3.0"></script>
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/esrigis/mapfunctionsPopup.js"/>"></script>
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>
   	
<!-- Individual YUI CSS files --> 
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css"> 
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/menu/assets/skins/sam/menu.css"> 
<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/dragdrop/dragdrop-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"></script> 

<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/menu/menu-min.js"></script>
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/esrigis/basemapgallery.js"/>"></script>   
   	
   	
   	 <style type="text/css">
      @import "<c:out value="${datadispatcherform.apiurl}"/>/jsapi/arcgis/3.0/js/dojo/dijit/themes/claro/claro.css";
      .zoominIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_zoomin.png); width:16px; height:16px; }
      .zoomoutIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_zoomout.png); width:16px; height:16px; }
      .zoomfullextIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_fullextent.png); width:16px; height:16px; }
      .zoomprevIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_previous.png); width:16px; height:16px; }
      .zoomnextIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_next.png); width:16px; height:16px; }
      .panIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_pan.png); width:16px; height:16px; }
      .deactivateIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_decline.png); width:16px; height:16px; }
    </style>
    

<script type="text/javascript">
var trnSelectThisPoint = "";
var trnSelectThisStructure = "";
var trnEdit = "";
var trnMove = "";
var trnRotateScale = "";
var trnNotImplemented = "";
var trnDelete = "";

function initializeTranslations(){
	trnSelectThisPoint = "<digi:trn jsFriendly='true'>Select this point</digi:trn>";
	trnSelectThisStructure = "<digi:trn jsFriendly='true'>Select this structure</digi:trn>";
	trnEdit = "<digi:trn jsFriendly='true'>Edit</digi:trn>";
	trnMove = "<digi:trn jsFriendly='true'>Move</digi:trn>";
	trnRotateScale = "<digi:trn jsFriendly='true'>Rotate/Scale</digi:trn>";
	trnNotImplemented = "<digi:trn jsFriendly='true'>Not Implemented</digi:trn>";
	trnDelete = "<digi:trn jsFriendly='true'>Delete</digi:trn>";
}
	$(function(){
  		$('#toolsbtn').click(function(){
     		$('#navToolbar').toggle('slow');
     	});
	});
	
	$(function(){
  		$('#basemaps').click(function(){
     		$("#basemapGalleryesri").toggle('slow');
     	});	
	});
	
</script>
 </head> 
  <body class="soria">
   <img id="loadingImg" src="/TEMPLATE/ampTemplate/img_2/ajax-loader.gif" style="position:absolute;left:50%;top:50%; z-index:100;" />
    <div id="mainWindow" dojotype="dijit.layout.BorderContainer" design="headline" gutters="false" style="width:100%; height:100%;">
    	<div id="map" dojotype="dijit.layout.ContentPane" class="roundedCorners" region="center">
	        <div id="drawToolbar"  dojoType="dijit.Toolbar" region="top" style="position:absolute;left:10px;top:2px;z-Index:999;padding: 8px;">
			    <button class="buttonx_sm" onclick="tb.activate(esri.toolbars.Draw.POINT);"><digi:trn>Point</digi:trn></button>
			    <button class="buttonx_sm" onclick="tb.activate(esri.toolbars.Draw.POLYLINE);"><digi:trn>Polyline</digi:trn></button>
			    <button class="buttonx_sm" onclick="tb.activate(esri.toolbars.Draw.POLYGON);"><digi:trn>Polygon</digi:trn></button>
			    <button class="buttonx_sm" onclick="tb.deactivate()"><digi:trn>Deactivate</digi:trn></button> 
			    <input type="text" id="address" size="30"/>
			    <button id="localebtn" class="buttonx_sm" onclick="locate()"><digi:trn>Locale</digi:trn></button>
			    <button class="buttonx_sm" id="basemaps"><digi:trn>Base Maps</digi:trn></button>
			    
	    	</div>
	    	
	   		<div id="navToolbar" dojoType="dijit.Toolbar" region="top" style="position:absolute; right:20px; top:10px; z-Index:999;display: none;margin-top: 45px;">
				<div dojoType="dijit.form.Button" id="zoomin" iconClass="zoominIcon" onClick="navToolbar.activate(esri.toolbars.Navigation.ZOOM_IN);">Zoom In</div>
				<div dojoType="dijit.form.Button" id="zoomout" iconClass="zoomoutIcon" onClick="navToolbar.activate(esri.toolbars.Navigation.ZOOM_OUT);">Zoom Out</div>
				<div dojoType="dijit.form.Button" id="zoomfullext" iconClass="zoomfullextIcon" onClick="navToolbar.zoomToFullExtent();">Full Extent</div>
			    <div dojoType="dijit.form.Button" id="zoomprev" iconClass="zoomprevIcon" onClick="navToolbar.zoomToPrevExtent();">Prev Extent</div>
			    <div dojoType="dijit.form.Button" id="zoomnext" iconClass="zoomnextIcon" onClick="navToolbar.zoomToNextExtent();">Next Extent</div>
			    <div dojoType="dijit.form.Button" id="pan" iconClass="panIcon" onClick="navToolbar.activate(esri.toolbars.Navigation.PAN);">Pan</div>
			    <div dojoType="dijit.form.Button" id="deactivate" iconClass="deactivateIcon" onClick="navToolbar.deactivate()">Deactivate</div>
	 		</div>
	 		<div id="basemapGalleryesri" region="center"></div>
 		</div>
 	</div>  
  </body>
</html>
