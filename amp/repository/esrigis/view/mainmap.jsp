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
<%@page import="org.digijava.module.aim.helper.FormatHelper"%>
<%@page import="org.digijava.module.aim.helper.GlobalSettingsConstants"%>
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
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/amp/DecimalFormat.js"/>"></script>
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/esrigis/maputils.js"/>"></script>
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
      .zoominIcon { background:url(/TEMPLATE/ampTemplate/img_2/gis/nav_zoomin.png) no-repeat right; width:16px; height:16px;}
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
			if (filterenable()){
     			$('#filterdiv').toggle();
			}else{
				alert('If you click filters, you will lose all your previus filter from reports - Filter is disable');
			}
     	});
	});
	$(function(){
  		$('#toolsbtn').click(function(){
     		$('#navToolbar').toggle();
     	});
	});

	$(function(){
  		$('#search').click(function(){
     		$('#distancediv').toggle();
     	});
	});

	$(function(){
  		$('#datasource').click(function(){
     		$('#sourcediv').toggle();
     		filldatasourcetable();
     	});
	});

	$(function(){
  		$('#sbyd').click(function(){
  	  		var value = $('#distance').val();
  	  		if (isNumber(value)){
  				searchdistance = value;
     			$('#distancediv').toggle();
     			searchactive = true;
  	  		}else{
				alert('The value must be numeric and positive');
  	  	  	}
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
	$(function(){
  		$('#minmenu').click(function(){
  			$('#divmenucontent').toggle('slow');
     	});
	});
	
	var currentFormat = "<%=org.digijava.module.aim.util.FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.GlobalSettingsConstants.NUMBER_FORMAT) %>";
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
  <body class="soria gisbody">
   <img id="loadingImg" src="/TEMPLATE/ampTemplate/img_2/ajax-loader.gif" style="position:absolute;left:50%;top:50%; z-index:200;border: 8px solid white;-moz-border-radius: 8px;" />
    <div id="mainWindow" dojotype="dijit.layout.BorderContainer" design="headline" gutters="false" style="width:100%; height:100%;">
  		<div id="map" dojotype="dijit.layout.ContentPane" class="roundedCorners" region="center">
       </div>
        <div id="basemapGallery"></div>
       	<div id="basemapGalleryesri"></div>
       <div class="headerBackground"> </div>
       <div class="header"style="float:left;">
	 		<div style="margin-left:-85px;padding:8px 0px 0px 0px;">
				<img src="/TEMPLATE/ampTemplate/img_2/amp_trans.png" align=left>
				<div class="amp_label">&nbsp;<digi:trn key="aim:aidManagementPlatform">Aid Management Platform (AMP)</digi:trn></div>
			</div>
        </div>
        <div class="headerContent" align="right" style="vertical-align: middle; position:relative;" id="mainmenu">
      	<table cellspacing="5px" cellpadding="5px" style="height: 100%;">
				<tbody>
					<tr>
						<td id="toolsbtn" class="mapMenuItem" valign="middle" align="left" style="cursor: pointer;"><digi:trn>Tools</digi:trn></td>
						<td id="filterbtn" class="mapMenuItem" valign="middle" align="left" style="cursor: pointer;"><digi:trn>Filter</digi:trn></td>
						
						<field:display name="Use Esri Online Maps" feature="Select Base Map">
							<td id="basemap" valign="middle" align="center" style="cursor: pointer;">
								<img src="/TEMPLATE/ampTemplate/img_2/imgBaseMap.png" align=left height="20px" width="20px" alt="<digi:trn>Select base Map</digi:trn>" style="background:#fff;border:1px solid #fff;">
							</td>
						</field:display>
						<field:display name="Use Local Base Maps" feature="Select Base Map">
							<td id="basemaplocal" valign="middle" align="center" style="cursor: pointer;">
								<img src="/TEMPLATE/ampTemplate/img_2/imgBaseMap.png" align=left height="20px" width="20px" alt="<digi:trn>Select base Map</digi:trn>" style="background:#fff;border:1px solid #fff;">
							</td>
						</field:display>
					</tr>
			</table>
			<div id="mainGisMenu">
				<img src='/TEMPLATE/ampTemplate/img_2/gis/minimize.gif' id="minmenu" style="margin-right:5px;cursor: pointer;">
				<div class="gisBoxHeader">
					<h3 style="line-height:1em;">Main Menu</h3>
	            </div>
	            <div id="divmenucontent">
		            <ul>
		              	<li class="mapMenuItem"  id="search" style="cursor: pointer;"><digi:trn>Search  Structures</digi:trn></li>
						<li id="hlight" align="left" onclick="getHighlights(0);" style="cursor: pointer;"><digi:trn>Highlight regions</digi:trn></li>
						<li id="hlightz" onclick="getHighlights(1);" style="cursor: pointer;"><digi:trn>Highlight Zones</digi:trn></li>
						<li id="add" onclick="addActivity();" style="cursor: pointer;"><digi:trn>Add Activity</digi:trn></li>
						<!-- 
						<li onclick="getActivities(true);" style="cursor: pointer;"><digi:trn>Activities</digi:trn></li>
						-->
						<li id="structures" onclick="getStructures(false);" style="cursor: pointer;"><digi:trn>Structures</digi:trn></li>
						<li id="povmap" onclick="toggleindicatormap('indicator');" style="cursor: pointer;"><digi:trn>Poverty Map</digi:trn></li>
						<li id="censusmap" onclick="toggleindicatormap('census');" style="cursor: pointer;"><digi:trn>Census Map</digi:trn></li>
						<li id="datasource" style="cursor: pointer;"><digi:trn>Data Source</digi:trn></li>
				     </ul>
			     </div>
		    </div>
        	<div style="background:url(/TEMPLATE/ampTemplate/img_2/gis/shade.png) no-repeat center top;height:10px;width:100%;border-top:1px solid #fff;"></div>
		</div>
		<div id="filterdiv" style="position:absolute;z-Index:100;top:50px;display: none;cursor: pointer;left:50%">
 			<jsp:include page="filter.jsp" flush="true"></jsp:include>
 		</div>
 		<div class='legendHeader' id="fakecolor">Color reference<br/><hr/></div>
 		<div id="pointsLegend" class="legendContent" style="left:15px;"></div>
        <div id="highlightLegend" class="legendContent" style="left:240px;"></div>
        <div id="legendDiv" class="legendContent" style="top:320px;left:470px;"></div>
       
        <div class="usaidlogo">
        	<table>
        		<tr>
        			<td align="right" style="font-size: 11px;color: white;">
        				<b>
        					<digi:trn>Funding Provided By</digi:trn>
        				</b> 
        			</td>
        		</tr>
        		<tr>
        			<td>
        				<img alt="USAID" src="/TEMPLATE/ampTemplate/img_2/gis/usaid_horizontal_150.png" border="0">
        			</td>
        		</tr>
        	</table>
        </div>
        
        <!-- Filter -->
        <div id="selectedfilter" class="legendContent" style="top:80px;left:100px;display:none;width: 35%;"> 
        	<div onclick="$('#selectedfilter').hide('slow');" style="color:white;float:right;cursor:pointer;">X</div>
        	<div class="legendHeader">Selected Filters<br/><hr/></div>
        	<table width="90%" cellspacing="0" cellpadding="0" border="0">
        		<tbody>
					<tr>
						<td valign="top" style="font-size:11px;font-family:Arial,Helvetica,sans-serif" id="sfilterid">
						</td>
					</tr>
					<tr>
						<td valign="top" style="font-size:11px;font-family:Arial,Helvetica,sans-serif">
					</tr>
				</tbody>
        	</table>
        </div>
        <!-- Data Source -->
        <div id="sourcediv" class="legendContent" style="top:55px;left:30px;width: 75%;display:none;"> 
        	<div onclick="$('#sourcediv').hide('slow');" style="color:white;float:right;cursor:pointer;">X</div>
        	<div class="legendHeader">Data Source<br/><hr/></div>
        	<table id="sourcecontent" width="100%" cellspacing="0" cellpadding="0" border="0" style="font-size:11px;font-family:Arial,Helvetica,sans-serif">
        		<tbody>
					<tr>
						<td valign="top" style="font-weight: bolder;">
							<digi:trn>Activity Name</digi:trn>
						</td>
						<td valign="top" style="font-weight: bolder;">
							<digi:trn>Activity Id</digi:trn>
						</td>
						<td valign="top" style="font-weight: bolder;">
							<digi:trn>Commitments</digi:trn>
						</td>
						<td valign="top" style="font-weight: bolder;">
							<digi:trn>Disbursements</digi:trn>
						</td>
					</tr>
				</tbody>
        	</table>
        </div>
        <!-- Search Structures-->
        <div id="distancediv" class="searchContent">
        	<table>
        		<tr>
        			<td style="color: white;">
        				<digi:trn>Distance in Km</digi:trn>
        			</td>
        			<td>
        				<input type="text" id="distance" style="width: 50px;"/> 
        			</td>
        			<td>
        				<input type="button" id="sbyd" width="5px" value="Go"/> 
        			</td>
        		</tr>
        	</table>
        </div>
        <div id="navToolbar" dojoType="dijit.Toolbar" style="position:absolute; right:180px; top:20px; z-Index:999;display: none;margin-top: 40px;">
        <div class="toolscontainer" style="margin:5px 0px 0px 0px;">
        	<div class="gisBoxHeader">
			  	<h3>Tools panel</h3><a href="#"></a>
            </div>
			<div class="mapButton" dojoType="dijit.form.Button" id="zoomin" iconClass="zoominIcon" onClick="navToolbar.activate(esri.toolbars.Navigation.ZOOM_IN);"><digi:trn>Zoom In</digi:trn></div>
			<div class="mapButton" dojoType="dijit.form.Button" id="zoomout" iconClass="zoomoutIcon" onClick="navToolbar.activate(esri.toolbars.Navigation.ZOOM_OUT);"><digi:trn>Zoom Out</digi:trn></div>
			<div class="mapButton" dojoType="dijit.form.Button" id="zoomfullext" iconClass="zoomfullextIcon" onClick="navToolbar.zoomToFullExtent();"><digi:trn>Full Extent</digi:trn></div>
		    <div class="mapButton" dojoType="dijit.form.Button" id="zoomprev" iconClass="zoomprevIcon" onClick="navToolbar.zoomToPrevExtent();"><digi:trn>Prev Extent</digi:trn></div>
		    <div class="mapButton" dojoType="dijit.form.Button" id="zoomnext" iconClass="zoomnextIcon" onClick="navToolbar.zoomToNextExtent();"><digi:trn>Next Extent</digi:trn></div>
		    <div class="mapButton" dojoType="dijit.form.Button" id="pan" iconClass="panIcon" onClick="navToolbar.activate(esri.toolbars.Navigation.PAN);"><digi:trn>Pan</digi:trn></div>
		    <div class="mapButton" dojoType="dijit.form.Button" id="deactivate" iconClass="deactivateIcon" onClick="navToolbar.deactivate()"><digi:trn>Deactivate</digi:trn></div>
 		</div></div>
    </div>  
	<div class="tooltip" style="position: absolute; display: block;z-index:100;" id="tooltipHolder"></div>

  </body>
</html>
