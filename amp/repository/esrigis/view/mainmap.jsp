<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

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
    <digi:ref href="/TEMPLATE/ampTemplate/css_2/amp.css" type="text/css" rel="stylesheet" />
   	<digi:ref href="/TEMPLATE/ampTemplate/css_2/mapstyles.css" type="text/css" rel="stylesheet" />
   	
  
    <script type="text/javascript">
      var djConfig = {
        parseOnLoad: true
      };
    </script>
    <!-- Map Scripts -->
    <script type="text/javascript" src="http://serverapi.arcgisonline.com/jsapi/arcgis/?v=2.2"></script>
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/esrigis/mapfunctions.js"/>"></script>
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/esrigis/Ext.util.DelayedTask-nsRemoved.js"/>"></script>
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/esrigis/esri.ux.layers.ClusterLayer-debug.js"/>"></script>
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/esrigis/basemapgallery.js"/>"></script>
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>

<!-- Individual YUI CSS files --> 
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css"> 
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/menu/assets/skins/sam/menu.css"> 
   	
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
    
<script type="text/javascript">
	$(function(){
  		$('#filterbtn').click(function(){
     		$('#filterdiv').toggle();
     	});
	});
	$(function(){
  		$('#toolsbtn').click(function(){
     		$('#navToolbar').toggle();
     	});
	});

	$(function(){
  		$('#basemap').click(function(){
     		$('#basemapGalleryesri').toggle();
     	});
	});
	
	$(function(){
  		$('#basemaplocal').click(function(){
     		$('#basemapGallery').toggle();
     	});
	});

</script>

 	<!-- Filter Styles -->
   	<link rel="stylesheet" href="css_2/visualization.css" type="text/css" />
	<link rel="stylesheet" type="text/css" href="js_2/yui/tabview/assets/skins/sam/tabview.css">
	<digi:ref href="css_2/visualization_yui_tabs.css" type="text/css" rel="stylesheet" />
	
	<!-- Filter Scripts-->
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo/yahoo-min.js"></script> 
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/event/event-min.js"></script>
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/json/json-min.js"></script> 
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/selector/selector-min.js"></script> 
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script> 
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/dom/dom-min.js"></script> 
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"/>"></script> 
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"/>"></script> 
	
		
	
 </head> 
  <body class="soria">
   <img id="loadingImg" src="/TEMPLATE/ampTemplate/img_2/ajax-loader.gif" style="position:absolute;left:50%;top:50%; z-index:100;" />
    <div id="mainWindow" dojotype="dijit.layout.BorderContainer" design="headline" gutters="false" style="width:100%; height:100%;">
  		<div id="map" dojotype="dijit.layout.ContentPane" class="roundedCorners" region="center">
       </div>
        <div id="basemapGallery"></div>
       	<div id="basemapGalleryesri"></div>
       <div class="headerBackground"> </div>
       <div class="header">
            <table style="height: 100%;">
                <tbody>
                	<tr valign="middle">
                    	<td style="width: 45%;">
                        	<table>
                            	<tbody>
                            		<tr>
                                		<div>
											<img src="/TEMPLATE/ampTemplate/img_2/amp_trans.png" align=left>
											<div class="amp_label">&nbsp;<digi:trn key="aim:aidManagementPlatform">Aid Management Platform (AMP)</digi:trn></div>
										</div>
                            		</tr>
                        		</tbody>
                        	</table>
                    </td>
                </tr>
            </tbody></table>
        </div>
        <div class="headerContent" align="right" style="vertical-align: middle;">
			<table cellspacing="5px" cellpadding="5px" style="height: 100%;">
				<tbody>
					<tr>
						<td id="toolsbtn" valign="middle" align="right" style="cursor: pointer;">Tools</td>
						<td id="filterbtn" valign="middle" align="right" style="cursor: pointer;">Filter</td>
						<td valign="middle" align="center" onclick="getHighlights(0);" style="cursor: pointer;">Highlight regions</td>
						<td valign="middle" align="center" onclick="getHighlights(1);" style="cursor: pointer;">Highlight Zones</td>
						<td valign="middle" align="center" onclick="getActivities(true);" style="cursor: pointer;">Activities</td>
						<td valign="middle" align="center" onclick="getStructures(true);" style="cursor: pointer;">Structures</td>
						<td valign="middle" align="center" onclick="toggleindicatormap('indicator');" style="cursor: pointer;">Indicator</td>
						<field:display name="Use Esri Online Maps" feature="Select Base Map">
							<td id="basemap" valign="middle" align="center" style="cursor: pointer;">
								<img src="/TEMPLATE/ampTemplate/img_2/imgBaseMap.png" align=left height="20px" width="20px" alt="<digi:trn>Select base Map</digi:trn>">
							</td>
						</field:display>
						<field:display name="Use Local Base Maps" feature="Select Base Map">
							<td id="basemaplocal" valign="middle" align="center" style="cursor: pointer;">
								<img src="/TEMPLATE/ampTemplate/img_2/imgBaseMap.png" align=left height="20px" width="20px" alt="<digi:trn>Select base Map</digi:trn>">
							</td>
						</field:display>
					</tr>
			</table>
		</div>
		<div id="filterdiv" style="position:absolute;z-Index:100;margin-left: 700px;margin-top: 50px;display: none;">
 			<jsp:include page="filter.jsp" flush="true"></jsp:include>
 		</div>
 		 
        <div id="legenddiv" class="legendContent">
        </div>
 		<div id="navToolbar" dojoType="dijit.Toolbar" style="position:absolute; right:20px; top:10px; z-Index:999;display: none;margin-top: 45px;">
			<div dojoType="dijit.form.Button" id="zoomin" iconClass="zoominIcon" onClick="navToolbar.activate(esri.toolbars.Navigation.ZOOM_IN);"><digi:trn>Zoom In</digi:trn></div>
			<div dojoType="dijit.form.Button" id="zoomout" iconClass="zoomoutIcon" onClick="navToolbar.activate(esri.toolbars.Navigation.ZOOM_OUT);"><digi:trn>Zoom Out</digi:trn></div>
			<div dojoType="dijit.form.Button" id="zoomfullext" iconClass="zoomfullextIcon" onClick="navToolbar.zoomToFullExtent();"><digi:trn>Full Extent</digi:trn></div>
		    <div dojoType="dijit.form.Button" id="zoomprev" iconClass="zoomprevIcon" onClick="navToolbar.zoomToPrevExtent();"><digi:trn>Prev Extent</digi:trn></div>
		    <div dojoType="dijit.form.Button" id="zoomnext" iconClass="zoomnextIcon" onClick="navToolbar.zoomToNextExtent();"><digi:trn>Next Extent</digi:trn></div>
		    <div dojoType="dijit.form.Button" id="pan" iconClass="panIcon" onClick="navToolbar.activate(esri.toolbars.Navigation.PAN);"><digi:trn>Pan</digi:trn></div>
		    <div dojoType="dijit.form.Button" id="deactivate" iconClass="deactivateIcon" onClick="navToolbar.deactivate()"><digi:trn>Deactivate</digi:trn></div>
 		</div>
    </div>  
  </body>
</html>
