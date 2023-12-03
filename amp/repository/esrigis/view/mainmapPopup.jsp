<%@page import="org.digijava.ampModule.aim.helper.GlobalSettingsConstants"%>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ page import="org.digijava.ampModule.aim.util.FeaturesUtil" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <digi:instance property="datadispatcherform" />
   <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
    <!--The viewport meta tag is used to improve the presentation and behavior of the samples 
      on iOS devices-->
    <meta name="viewport" content="initial-scale=1, maximum-scale=1,user-scalable=no"/> 
    <digi:ref href="/TEMPLATE/ampTemplate/css_2/leaflet.css" type="text/css" rel="stylesheet" />
    <digi:ref href="/TEMPLATE/ampTemplate/tabs/css/jquery-ui.min.css" type="text/css" rel="stylesheet" />
    <digi:ref href="/TEMPLATE/ampTemplate/css_2/mappopupstyles.css" type="text/css" rel="stylesheet" />
    <digi:ref href="/TEMPLATE/ampTemplate/css_2/amp-wicket.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>
    <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/script/common/TranslationManager.js"/>"></script>    
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/leaflet/leaflet.js"/>"></script>
    <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/leaflet/esri-leaflet.js"/>"></script>      
     <!--Leaflet Draw-->      
     <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/Leaflet.draw.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/Leaflet.Draw.Event.js"></script>
    <link rel="stylesheet" href="/TEMPLATE/ampTemplate/js_2/leaflet-draw/leaflet.draw.css"/>

    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/Toolbar.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/Tooltip.js"></script>

    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/ext/GeometryUtil.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/ext/LatLngUtil.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/ext/LineUtil.Intersect.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/ext/Polygon.Intersect.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/ext/Polyline.Intersect.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/ext/TouchEvents.js"></script>

    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/draw/DrawToolbar.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/draw/handler/Draw.Feature.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/draw/handler/Draw.SimpleShape.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/draw/handler/Draw.Polyline.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/draw/handler/Draw.Circle.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/draw/handler/Draw.Marker.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/draw/handler/Draw.Polygon.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/draw/handler/Draw.Rectangle.js"></script>


    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/edit/EditToolbar.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/edit/handler/EditToolbar.Edit.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/edit/handler/EditToolbar.Delete.js"></script>

    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/Control.Draw.js"></script>

    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/edit/handler/Edit.Poly.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/edit/handler/Edit.SimpleShape.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/edit/handler/Edit.Circle.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/edit/handler/Edit.Rectangle.js"></script>
    <script src="/TEMPLATE/ampTemplate/js_2/leaflet-draw/edit/handler/Edit.Marker.js"></script>
    
    <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>
    <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-ui-1.9.2.custom.min.js"/>"></script>
    <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/esrigis/mainmapPopup.js"/>"></script>
   	    
   	 <script type="text/javascript">
   	$(document).ready(function() {
	new MapPopup (<%=FeaturesUtil.getGlobalSettingDouble(GlobalSettingsConstants.COUNTRY_LATITUDE)%>,<%=FeaturesUtil.getGlobalSettingDouble(GlobalSettingsConstants.COUNTRY_LONGITUDE)%>);
   	});
   	 </script> 
  </head>
  <body>
  	<img id="loadingImg" src="/TEMPLATE/ampTemplate/img_2/ajax-loader.gif" style="position:absolute;left:50%;top:50%; z-index:100;display: none;" />
    <div id="map"></div>
    <span id="location-wrapper">
    <input type="text" id="address" size="10"/>
    <button id="localeBtn"  onclick="locate()"><digi:trn>Locate</digi:trn></button>
	 <select id="fclList" style="width:90px;" onchange="filterLocation(this.value)">
	    	<option><digi:trn>All</digi:trn></option>
	  </select>
	</span>
	<div id="basemaps-wrapper" class="leaflet-bar">
	  <select name="basemaps" id="basemaps">
	    <option value="None" selected="selected"><digi:trn>Default</digi:trn><options>
	    <option value="Topographic"><digi:trn>Topographic</digi:trn><options>
	    <option value="Streets"><digi:trn>Streets</digi:trn></option>
	    <option value="NationalGeographic"><digi:trn>National Geographic</digi:trn><options>
	    <option value="Oceans"><digi:trn>Oceans</digi:trn><options>
	    <option value="Gray"><digi:trn>Gray</digi:trn><options>
	    <option value="DarkGray"><digi:trn>Dark Gray</digi:trn><options>
	    <option value="Imagery"><digi:trn>Imagery</digi:trn><options>
	    <option value="ShadedRelief"><digi:trn>Shaded Relief</digi:trn><options>
	  </select>
	</div>
   <span id="custom-menu-wrapper">
   <ul class='custom-menu'>
	  <li data-action="select"><digi:trn>Select</digi:trn></li>
	  <li data-action="remove"><digi:trn>Remove</digi:trn></li>	  
	</ul>
  </span>
   <div id="locationTitleDialog"> 
   <div id="errorMsg"></div>
   
    <label><digi:trn>Title</digi:trn></label><span class="required"> * </span><input id="locationTitle" type="text"/> </br>
    <div>
     
     <div id="colors-section">
     <label><digi:trn>Select a color</digi:trn></label>
     <ul class="colors">	  
	   
	 </ul>
     </div>
    
    </div>
    
      
   </div>
  </body>
</html>