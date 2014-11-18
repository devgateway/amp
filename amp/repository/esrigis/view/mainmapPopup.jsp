<%@page import="org.digijava.module.aim.helper.GlobalSettingsConstants"%>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <digi:instance property="datadispatcherform" />
   <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
    <!--The viewport meta tag is used to improve the presentation and behavior of the samples 
      on iOS devices-->
    <meta name="viewport" content="initial-scale=1, maximum-scale=1,user-scalable=no"/>
 
    <digi:ref href="/TEMPLATE/ampTemplate/gisModule/dev/node_modules/leaflet/dist/leaflet.css" type="text/css" rel="stylesheet" />
    <digi:ref href="/TEMPLATE/ampTemplate/css_2/mappopupstyles.css" type="text/css" rel="stylesheet" />
  
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/gisModule/dev/node_modules/leaflet/dist/leaflet.js"/>"></script>
    <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/gisModule/dev/node_modules/esri-leaflet/dist/esri-leaflet.js"/>"></script>
    <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>
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
    <button id="pointBtn" class="gradient"   onclick="createPoint()"><digi:trn>Point</digi:trn></button>
	<button id="deactivateBtn" class="gradient" onclick="deactivate()"><digi:trn>Deactivate</digi:trn></button> 
	<input type="text" id="address" size="10"/>
    <button id="localeBtn"  onclick="locate()"><digi:trn>Locate</digi:trn></button>
	 <select id="fclList" style="width:90px;" onchange="filterLocation(this.value)">
	    	<option><digi:trn>All</digi:trn></option>
	  </select>
	</span>
	<div id="basemaps-wrapper" class="leaflet-bar">
	  <select name="basemaps" id="basemaps">
	    <option value="Topographic"><digi:trn>Topographic</digi:trn><options>
	    <option value="Streets" selected="selected"><digi:trn>Streets</digi:trn></option>
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
	  <li data-action="select"><digi:trn>Select point</digi:trn></li>
	  <li data-action="remove"><digi:trn>Remove</digi:trn></li>
	</ul>
  </span>
  </body>
</html>