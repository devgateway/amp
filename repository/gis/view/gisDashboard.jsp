<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="gisDashboardForm"/>
<html:hidden property="selectedCurrency" styleId="selCurr"/>

<style>
	div.navHiden{
		background-color : #8C8C8C;
		color : #494949;
		border-left: 1px solid white;
		border-top: 1px solid white;
		border-right: 1px solid black;
		border-bottom: 1px solid black;
		cursor:pointer;
	}
	
	div.navVisible{
		background-color : #C4C4C4;
		color : Black;
		border-left: 1px solid black;
		border-top: 1px solid black;
		border-right: 1px solid white;
		border-bottom: 1px solid white;
		cursor:pointer;
	}
	
	#content{
		height: 100%;
	}
	#demo{
		height: 100%;
	}
	#div1{
		height: 95,5%;
	}
	
	option.dsbl {
		color:gray;;
	}
	
	option.enbl {
		color:Black;;
	}
	
	
	div.sec_map_filter_outer_frame {
		border: 1px solid gray;
	}
	
	div.sec_map_filter_container {
		border: 1px solid;
		border-top-color: white;
		border-left-color: white;
		border-bottom-color: black;
		border-right-color: black;
		border-right-width: 2px;
		border-bottom-width: 2px;
	}
	
	td.sec_map_filter_title {
		background-color: #222e5d;
		color:white;
		font-weight:bold;
	}
		
	td.sec_selector_tree {
		height: 100%;
		background-color: white;
		background-repeat: repeat-y;
		width: 13px;
	}

	td.sec_selector_tree_plain {
		background-image: url(/repository/gis/view/images/tree-vertical-plain.png);
	}
	
	td.sec_selector_tree_has_child {
		background-image: url(/repository/gis/view/images/tree-vertical-with-child.png);
	}
	
	td.sec_selector_tree_has_child_is_last {
		background-image: url(/repository/gis/view/images/tree-vertical-with-child-last.png);
	}
	
	td.sec_selector_tree_has_child_is_first {
		background-image: url(/repository/gis/view/images/tree-vertical-with-child-first.png);
	}
	
	div.sec_selector_item {
		background-color: white;
		border: 1px solid white;
		cursor:pointer;
		color:black;
		
	}	
	
	div.sec_selector_item:hover {
		background-color: #d2e1e8;
		border: 1px solid #96b8c6;
	}
	
	div.sec_selector_item_selected {
		background-color: #222e5d;
		border: 1px solid #3c788e;
		color: white;
		cursor:pointer;
	}
	
	div.sec_selector_item_disabled {
		background-color: white;
		color:gray;
	}
	
	div.sec_scheme_selector_outer_frame {
		border: 1px solid silver;
	}
	
	div.sec_scheme_selector {
		border: 1px solid;
		border-top-color: white;
		border-left-color: white;
		border-bottom-color: #a8a8a8;
		border-right-color: #a8a8a8;
		padding: 3px;
		cursor:pointer;
		text-align:center;
	}
	
	div.sec_scheme_selector_default {
		background-color: #edf5ff;
	}
	
	div.sec_scheme_selector_default:hover {
		background-color: #d2e1e8;
	}
	
	div.sec_scheme_selector_selected {
		background-color: #222e5d;
		color:white;
	}
	
	div.sec_scheme_selector_active {
		background-color: #314388;
		color:white;
	}
	
	
	div.filter_wnd_background {
		background-color:black;
		position:absolute;
		left:0px;
		top:0px;
		opacity:0.5;
		filter:alpha(opacity=50);                              
		z-index: 100;
		width:100%;
		height:100%;
		/*
		display:none;
		*/
	}
	
</style>

<script language="JavaScript" type="text/javascript" src="<digi:file src="TEMPLATE/ampTemplate/script/yui/dom-min.js"/>"></script>

<script language="JavaScript">
	var validatedRegPercentage = false;
	<field:display name="Validate Mandatory Regional Percentage" feature="Location">
		validatedRegPercentage = true;
	</field:display>
	
	var displayeRegPercentage = false;
	<field:display name="Regional Percentage" feature="Location">
		displayeRegPercentage = true;
	</field:display>
</script>

<script language="JavaScript">
    var showDevinfo = false;
    <feature:display name="Show DevInfo data" module="GIS DASHBOARD">
        showDevinfo = true;
    </feature:display>
</script>


<bean:define id="isDevInfoMode">true</bean:define>
	<c:set var="isDevInfoMode"><feature:display name="Show DevInfo data" module="GIS DASHBOARD">true</feature:display>
</c:set>

<c:if test="${isDevInfoMode==''}">
<c:set var="isDevInfoMode">false</c:set>
</c:if>


<div class="filter_wnd_background_holder" style="display:none;">
	
	<div class="filter_wnd_background">&nbsp;</div>
	
	<div id="filter_dialog" class="sec_map_filter_outer_frame" style="position:absolute;z-index:101;">
		<div class="sec_map_filter_container">
			<table bgcolor="white" border="0" cellspacing="5" cellpadding="5" style="border-collapse:collapse;">
				<tr><td colspan="3" class="sec_map_filter_title"><digi:trn>Map filters</digi:trn></td></tr>
				<field:display name="Source of Data" feature="GIS DASHBOARD">
				<tr>
					<td>
						<digi:trn>Source of Data</digi:trn>
					</td>
					<td align="right">
	            <select id="mapModeFin" style="width:250px" value="commitment">
	            	<option value="fundingData"><digi:trn>Activity Funding Data</digi:trn></option>
	            	<option value="pledgesData"><digi:trn>Pledges Data</digi:trn></option>
	        	</select>
	        </td>
	        <td width="10">
			        &nbsp;
					</td>
				</tr>
			</field:display>
				
				
				<tr id="fundingTypeRow">
					<td>
						<digi:trn>Funding type</digi:trn>
					</td>
					<td align="right">
	            <select id="fundingType" onChange="" style="width:250px" value="commitment">
	            <field:display name="Measure Commitment" feature="GIS DASHBOARD">
	            	<option value="commitment"><digi:trn>Commitment</digi:trn></option>
	            </field:display>
	            <field:display name="Measure Disbursement" feature="GIS DASHBOARD">
	            	<option value="disbursement"><digi:trn>Disbursement</digi:trn></option>
	            </field:display>
	            <field:display name="Measure Expenditure" feature="GIS DASHBOARD">
	            	<option value="expenditure"><digi:trn>Expenditure</digi:trn></option>
	            </field:display>
	        	</select>
	        </td>
	        <td width="10">
			        <img style="width: 16px; height: 16px; vertical-align: middle;" src="/TEMPLATE/ampTemplate/images/info.png" title="<digi:trn>Select whether the map should be highlighted according to Donor Commitments or Donor Disbursements</digi:trn>" />
					</td>
				</tr>
				
				<tr>
	        <td>
	             <digi:trn>Select Donor</digi:trn>:
	        </td>
					<td align="right">
						<select id="donorsCombo" onChange="" style="width:250px">
							<option value="-1"><digi:trn>All Donors</digi:trn></option>
							<logic:iterate name="gisDashboardForm" property="allDonorOrgs" id="donor">
								<option value="<bean:write name="donor" property="value"/>"><bean:write name="donor" property="label"/></option>
							</logic:iterate>
						</select>
					</td>
					<td width="10">
			        <img style="width: 16px; height: 16px; vertical-align: middle;" src="/TEMPLATE/ampTemplate/images/info.png" title="${translation}" />
			
					</td>
				</tr>
				
				<field:display name="Show fundings only from curent WS" feature="GIS DASHBOARD">
				<tr>
					<td>
						<digi:trn>Show data only for workspace selected</digi:trn>
					</td>
					<td align="right">
            <input id="showOnlyCurentWS" type="checkbox" value="true">
	        </td>
	        <td width="10">
			        &nbsp;
					</td>
				</tr>
			</field:display>
				
				
				
				<tr>
					<td colspan="3">
						<hr>
					</td>
				</tr>
						
						
				<tr>
					<td colspan="2">
						<digi:trn>Select Sector</digi:trn>
					</td>
					<td width="10">
		        <img style="width: 16px; height: 16px; vertical-align: middle;" src="/TEMPLATE/ampTemplate/images/info.png" title="<digi:trn>Select the OECD/DAC sector for which you'd like to see the funding information on the map</digi:trn>" />
					</td>
				</td>
				<tr>
					<td colspan="3">
						<div id="sector_selector_hider">
					    <div id="filtrSectorSelectorContainer" style="background-color:#edf5ff; width:504px; border: 1px solid black;"></div>
					  </div>
					</td>
				</tr>
				
				<tr><td align='right' colspan="3">
				<input type='button' value='<digi:trn>Apply</digi:trn>' onClick='applySectorFilter()'>
					&nbsp;
				<input type='button' value='<digi:trn>Cancel</digi:trn>' onClick='closeSectorFilter()'>
				</td></tr>
				
				
			</table>
	  </div>
	</div>
</div>




<div id="content" class="yui-skin-sam" style="width:527px;height:100%;max-width: 527x;">
  <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;width: 527px;">
      <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/tabrightcorner.gif" align="right" hspace="0"/>
      <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/tableftcorner.gif" align="left" hspace="0"/>
      <div class="longTab">
          <digi:trn key="gis:regionalview">Regional View</digi:trn>
      </div>
            
    <div id="div1" class="yui-content" style="font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">


<div id="ctrlContainer" style="display:none">
	<div id="navMapContainer" style="width:300px; height:300px; position: absolute; left: 15px; top: 35px; border: 1px solid black"><img id="navCursorMap" width="300" height="300" border="0" src="/gis/getFoundingDetails.do?action=paintMap&noCapt=true&width=300&height=300&mapLevel=2&mapCode=TZA"></div>
	<div id="navCursor" style="width:1px; height:1px; position: absolute; left: 23px; top: 43px; border: 1px solid white; cursor:pointer;">
		<div style="width:100%; height:100%; background:white; filter:alpha(opacity=30); opacity:0.3;"></div>
	</div>
	
		<div title="Zoom 1.0X" onClick="" id="mapZoom10" class="navVisible zoomBt" style="width:30px; position: absolute; top:310px; left:25px;" align="center">1.0X</div>
		<div title="Zoom 1.5X" onClick="" id="mapZoom15" class="navHiden zoomBt" style="width:30px; position: absolute; top:310px; left:60px;" align="center">1.5X</div>
		<div title="Zoom 2.0X" onClick="" id="mapZoom20" class="navHiden zoomBt" style="width:30px; position: absolute; top:310px; left:95px;" align="center">2.0X</div>
		<div title="Zoom 3.0X" onClick="" id="mapZoom30" class="navHiden zoomBt" style="width:30px; position: absolute; top:310px; left:130px;" align="center">3.0X</div>
	
	

</div>
<!-- 
<div class="navHiden" align="center" style="position: absolute; left:10px; top:32px; width:150px;" onClick="showNavigation(this)"><digi:trn>Map navigation</digi:trn></div>
 -->
<div class="navHiden" id="mapNav" align="center" style="position: absolute; left:10px; top:32px; width:150px;" onClick=""><digi:trn>Map navigation</digi:trn></div>

	
<table cellpadding="5" cellspacing="1" border="0">
	<tr>
		<td colspan="3" align="left">
		  <!--
			<div id="mapCanvasContainer" style="border:1px solid black; width:500px; height:500px; overflow:hidden;"><img onLoad="initMouseOverEvt(); getImageMap(); checkIndicatorValues(); actionImgLoading = false; setBusy(false);" useMap="#areaMap" id="testMap" border="0" src="/gis/getFoundingDetails.do?action=paintMap&mapCode=TZA&mapLevel=2&uniqueStr=0&year=-1&width=500&height=500"></div>
		 -->
		 <div id="mapCanvasContainer" style="border:1px solid black; width:500px; height:500px; overflow:hidden;margin-left: auto;margin-right: auto;">
			<c:if test="${isDevInfoMode == true}">
				<img onLoad="" useMap="#areaMap" id="testMap" border="0" src="/gis/getFoundingDetails.do?action=paintMap&mapCode=TZA&mapLevel=2&uniqueStr=0&year=-1&width=500&height=500">
			</c:if>
			<c:if test="${isDevInfoMode == false}">
				<img onLoad="" useMap="#areaMap" id="testMap" border="0" src="/gis/getFoundingDetails.do?action=getDataForSectorFin&mapCode=TZA&mapLevel=2&donorId=-1&fromYear=2009&toYear=2010&sectorId=-1&fundingType=commitment&uniqueStr=0&width=500&height=500">
			</c:if>
			</div>
		  
		</td>
	</tr>
	
	<tr>
		<td colspan="3" align="left">
			<digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/fundingLegend.png" border="0"/>
			
			<%--
			<digi:img usemap="#legendMap" src="module/gis/images/fundingLegend.png" border="0"/>

			<MAP NAME="legendMap">
				<AREA TITLE="0-10%" SHAPE=RECT COORDS="0,0,70,20">
				<AREA TITLE="10-20%" SHAPE=RECT COORDS="70,0,140,20">
				<AREA TITLE="20-30%" SHAPE=RECT COORDS="140,0,210,20">
				<AREA TITLE="30-40%" SHAPE=RECT COORDS="210,0,280,20">
				<AREA TITLE="40-50%" SHAPE=RECT COORDS="280,0,350,20">
				<AREA TITLE="50-60%" SHAPE=RECT COORDS="350,0,420,20">
				<AREA TITLE="60-70%" SHAPE=RECT COORDS="420,0,490,20">
				<AREA TITLE="70-80%" SHAPE=RECT COORDS="490,0,560,20">
				<AREA TITLE="80-90%" SHAPE=RECT COORDS="560,0,630,20">
				<AREA TITLE="90-100%" SHAPE=RECT COORDS="630,0,700,20">
			</MAP>
			--%>
		</td>
	</tr>	

    
    <div id="imageMapContainer" style="visibility:hidden;"></div>
    
    <!-- DevInfo block -->
<c:if test="${isDevInfoMode == true}">
	<tr>
		<td colspan="3">
			<span>
				<digi:trn key="gis:minmax:message">
				Regions with the lowest (MIN) values for the selected indicator are shaded dark green. 
				Regions with the highest (MAX) value are shaded light green. 
				For some indicators (such as mortality rates), having the MAX value indicates the lowest performance.
				</digi:trn>
			</span>
			<br>
			<br>
			<digi:trn key="gis:datasource:message">
				Data Source: Dev Info
			</digi:trn>
		</td>
	</tr>
	<tr>
		<td colspan="3">
			<img style="visibility:hidden" id="busyIndicator" src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif">
		</td>
	</tr>
	
    
    
	<feature:display name="GIS DASHBOARD" module="GIS DASHBOARD">	
		<field:display name="Map Level Switch" feature="GIS DASHBOARD">
		<tr>
	        <td width="200" nowrap style="font-size:12px">
	                <digi:trn key="gis:selectMalLevel">Select Map Level</digi:trn>:
	        </td>
			<td width="90%">
			 <!-- 
				<input title="Region view" type="Radio" value="2" name="mapLevelRadio" checked onClick="mapLevelChanged(this.value)">Region view &nbsp;
				<input title="District view" type="Radio" value="3" name="mapLevelRadio" onClick="mapLevelChanged(this.value)">District view
				-->
				<input title="Region view" type="Radio" value="2" name="mapLevelRadio" checked >Region view &nbsp;
				<input title="District view" type="Radio" value="3" name="mapLevelRadio" >District view
				
			</td>
		</tr>
		</field:display>
	</feature:display>

	<tr>
        <td nowrap width="200" style="font-size:12px">
             <digi:trn>Select Sector</digi:trn>:
        </td>
		<td colspan="2">
		<!-- <select id="sectorsMapCombo" onChange="sectorSelected(this.value)" style="width:250px"> -->
			<select id="sectorsMapCombo" onChange="" style="width:250px">
			<option value="-1"><digi:trn>Select sector</digi:trn></option>
			<logic:iterate name="gisDashboardForm" property="sectorCollection" id="sec">
				<option value="<bean:write name="sec" property="ampSectorId"/>"><bean:write name="sec" property="name"/></option>
			</logic:iterate>
		</select>
		</td>
	</tr>
	<tr>
       <td nowrap width="200" style="font-size:12px">
            <digi:trn>Select Indicator</digi:trn>:
		<td colspan="2">
		<!-- <select id="indicatorsCombo" onchange="indicatorSelected(this)" style="width:250px"> -->
		<select id="indicatorsCombo" onchange="" style="width:250px">	
			<option value=-1><digi:trn>Select Indicator</digi:trn></option>
		</select>
		</td>
	</tr>
	<tr>
        <td width="200" style="font-size:12px">
            <digi:trn>Select Subgroup</digi:trn>:
        </td>
		<td colspan="2">
			<!-- <select id="indicatorSubgroupCombo" onChange="subgroupSelected(this)" style="width:250px">  -->
			<select id="indicatorSubgroupCombo" onChange="" style="width:250px">
				<option value="-1"><digi:trn>Select subgroup</digi:trn></option>
			</select>
		</td>
	</tr>
	<tr>
        <td nowrap width="200" style="font-size:12px">
            <digi:trn>Select Time Interval</digi:trn>:
        </td>
		<td colspan="2">
			<select id="indicatorYearCombo" style="width:250px">
				<option value="-1"><digi:trn>Select Time Interval</digi:trn></option>
			</select>
		</td>
	</tr>
	
	<script language="JavaScript">
		if (!validatedRegPercentage || !displayeRegPercentage) {
			
			var msgText = '<digi:trn key="gis:funding_msg">(*) Project funding is not disaggregated by region or district, and therefore reflect activity totals.</digi:trn>'
			
			document.write('<tr><td nowrap colspan="2"><font color="red">');
			document.write(msgText);
			document.write('</font></td></tr>');
		}
	</script>
	
</c:if>
    
<!-- Financial data block -->    
<c:if test="${isDevInfoMode == false}">
    <tr>
        <td colspan="3">
            <span>
				<img alt="" style="width: 16px; height: 16px; vertical-align: middle;" src="/TEMPLATE/ampTemplate/images/info.png"/>&nbsp;
                <digi:trn key="gis:minmax:message_fin">
                Regions with the lowest (MIN) values for the selected funding type are shaded dark green. 
                Regions with the highest (MAX) value are shaded light green. 
                </digi:trn>
            </span>
            <br>
            <br>
            <span>
				<img style="width: 16px; height: 16px; vertical-align: middle;" src="/TEMPLATE/ampTemplate/images/info.png"/>&nbsp;<digi:trn>Click on a region to view list of projects implemented in that Region</digi:trn>
            </span>
            <br>
            <br>
            <digi:trn key="gis:datasource:message_fin">
                Data Source AMP
            </digi:trn>
        </td>
    </tr>
    <tr>
        <td colspan="3">
            <img style="visibility:hidden" id="busyIndicator" src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif">
        </td>
    </tr>
    
    
    
    <feature:display name="GIS DASHBOARD" module="GIS DASHBOARD">    
        <field:display name="Map Level Switch" feature="GIS DASHBOARD">
        <tr>
            <td width="200" nowrap style="font-size:12px">
                    <digi:trn key="gis:selectMalLevel">Select Map Level</digi:trn>:
            </td>
            <td width="90%" colspan="2">
             <!-- 
                <input title="Region view" type="Radio" value="2" name="mapLevelRadio" checked onClick="mapLevelChanged(this.value)">Region view &nbsp;
                <input title="District view" type="Radio" value="3" name="mapLevelRadio" onClick="mapLevelChanged(this.value)">District view
                -->
                <input title="Region view" type="Radio" value="2" name="mapLevelRadio" checked >Region view &nbsp;
                <input title="District view" type="Radio" value="3" name="mapLevelRadio" >District view
                
            </td>
        </tr>
        </field:display>
    </feature:display>

    
    <tr>
        <td nowrap width="200" style="font-size:12px">
             <digi:trn>Selected Funding type</digi:trn>&nbsp;: 
             <br>
        </td>
        <td id="fundingTypeSelected">&nbsp;
        	<%--
            <select id="fundingType" onChange="" style="width:250px" value="commitment">
            <field:display name="Measure Commitment" feature="GIS DASHBOARD">
            	<option value="commitment"><digi:trn>Commitment</digi:trn></option>
            </field:display>
            <field:display name="Measure Disbursement" feature="GIS DASHBOARD">
            	<option value="disbursement"><digi:trn>Disbursement</digi:trn></option>
            </field:display>
            <field:display name="Measure Expenditure" feature="GIS DASHBOARD">
            	<option value="expenditure"><digi:trn>Expenditure</digi:trn></option>
            </field:display>
        </select>
        
        </td>
		<td>
	        <img style="width: 16px; height: 16px; vertical-align: middle;" src="/TEMPLATE/ampTemplate/images/info.png" title="<digi:trn>Select whether the map should be highlighted according to Donor Commitments or Donor Disbursements</digi:trn>" />
		</td>--%>
    </tr>
    
    
    
    <tr>
        <td nowrap width="200" style="font-size:12px">
             <digi:trn>Selected Sector/Program</digi:trn>:
             <input type="hidden" id="sectorsMapComboFin" value="-1">
        </td>
        <td id="sectorSelected">&nbsp;
        		<%--
            <select id="sectorsMapComboFin" onChange="" style="width:250px">
            <option value="-1"><digi:trn>All Sectors</digi:trn></option>
            <logic:iterate name="gisDashboardForm" property="sectorCollection" id="sec">
                <option value="<bean:write name="sec" property="ampSectorId"/>"><bean:write name="sec" property="name"/></option>
            </logic:iterate>
        		</select>
        		
        
        </td>
			<td>
	        <img style="width: 16px; height: 16px; vertical-align: middle;" src="/TEMPLATE/ampTemplate/images/info.png" title="<digi:trn>Select the OECD/DAC sector for which you'd like to see the funding information on the map</digi:trn>" />
			--%></td>
    </tr>

    <%--
    <tr>
    	<td nowrap width="200" style="font-size:12px">
    		<digi:trn>Select Sector</digi:trn>:
    	</td>
    	<td>
    		<input type="hidden" id="sectorsMapComboFin" value="-1">
    		<div style="font-size:12px; width:250px; border: 1px solid; background-color:white; border-top-color:#abadb3; border-bottom-color:#e3e9ef; border-left-color:#e2e3ea; border-right-color:#dbdfe6;">
	    		<table id="sector_selector_expander" width="100%" style="cursor:pointer;">
	    			<tr>
	    				<td width="234"  nowrap >
	    					<div id="sel_sec_filter_display" style="color:black; height:12px; overflow-x: hidden;">&nbsp;</div>
	    				</td>
	    				<td width="16">
	    					<img src="/repository/gis/view/images/sec_filter_expand.png" style="float:top;">
	    				</td>
	    			</tr>
	    		</table>
    		</div>
    		
    		
    	</td>
    </tr>	
    --%>
    
    
    <tr>
       <td nowrap width="200" style="font-size:12px">
            <digi:trn>Selected Donor</digi:trn>:
        </td>    
        <td id="donorSelected">&nbsp;
        	<%--
        <select id="donorsCombo" onchange="" style="width:250px">    
            <option value=-1><digi:trn>All donors</digi:trn></option>
        </select>
        --%>
        </td>
    </tr>
    
    <tr>
    	<td><input type="button" value="<digi:trn>Show filters</digi:trn>" onClick = "showFinFilters()"></td>
    </tr>
    
    <c:set var="translation">
			<digi:trn>Select the funding organization for which you'd like to see the funding information on the map</digi:trn>
		</c:set>
	
		<%--
    <tr>
        <td nowrap width="200" style="font-size:12px">
             <digi:trn>Select Donor</digi:trn>:
        </td>
		<td>
		<!-- <select id="sectorsMapCombo" onChange="sectorSelected(this.value)" style="width:250px"> -->
			<select id="donorsCombo" onChange="" style="width:250px">
			<option value="-1"><digi:trn>All Donors</digi:trn></option>
			<logic:iterate name="gisDashboardForm" property="allDonorOrgs" id="donor">
				<option value="<bean:write name="donor" property="value"/>"><bean:write name="donor" property="label"/></option>
			</logic:iterate>
		</select>
		</td>
		<td>
	        <img style="width: 16px; height: 16px; vertical-align: middle;" src="/TEMPLATE/ampTemplate/images/info.png" title="${translation}" />

		</td>
	</tr>
	--%>
</c:if>
</table>
    </div>
  </div>
</div>   


<c:if test="${isDevInfoMode == true}">
    <div id="tooltipContainer"  style="display:none; position: absolute; left:50px; top: 50px; background-color: #d9ceba; border: 1px solid silver;z-index: 2; width:200px;">
 	    <div style="border-top: 1px solid white; border-left: 1px solid white; border-bottom: 1px solid Black; border-right: 1px solid Black;">
	    
	    <table class="tableElement" border="1" bgcolor="#d9ceba" bordercolor="#c3b7a1" cellpadding="3" cellspacing="2" width="100%" style="border-collapse:collapse">
		    <tr>
			    <td nowrap width="50%" id="reg_district_caption"><digi:trn>Region</digi:trn></td>
			    <td width="50%" id="tooltipRegionContainer">&nbsp;</td>
		    </tr>

		    <tr>
			    <td nowrap bgcolor="#D9DAC9" colspan="2"><digi:trn>Funding details</digi:trn></td>
		    </tr>
		    <tr>
			    <td colspan="2" nowrap bgcolor="#D9DAC9" id="tooltipCurencyYearRange">&nbsp;</td>
		    </tr>
		    <tr>
			    <td nowrap bgcolor="#D9DAC9" colspan="2"><digi:trn>Total funding for this sector</digi:trn></td>
		    </tr>
		    <field:display name="Measure Commitment" feature="GIS DASHBOARD">
		    <tr>
			    <td nowrap width="50%"><digi:trn>Commitment</digi:trn></td>
			    <td width="50%" id="tooltipTotalCommitmentContainer">&nbsp;</td>
		    </tr>
		    </field:display>
		    <field:display name="Measure Disbursement" feature="GIS DASHBOARD">
		    <tr>
			    <td nowrap width="50%"><digi:trn>Disbursement</digi:trn></td>
			    <td width="50%" id="tooltipTotalDisbursementContainer">&nbsp;</td>
		    </tr>
		    </field:display>
		    <field:display name="Measure Expenditure" feature="GIS DASHBOARD">
		    <tr>
			    <td nowrap width="50%"><digi:trn>Expenditure</digi:trn></td>
			    <td width="50%" id="tooltipTotalExpenditureContainer">&nbsp;</td>
		    </tr>
		    </field:display>
		    <tr>
			    <td nowrap bgcolor="#D9DAC9" colspan="2" id="reg_district_caption_for"><digi:trn>For this region</digi:trn></td>
		    </tr>
		     <field:display name="Measure Commitment" feature="GIS DASHBOARD">
		    <tr>
			    <td nowrap width="50%"><digi:trn>Commitment</digi:trn></td>
			    <td width="50%" id="tooltipCurrentCommitmentContainer">&nbsp;</td>
		    </tr>
		    </field:display>
		     <field:display name="Measure Disbursement" feature="GIS DASHBOARD">
		    <tr>
			    <td nowrap width="50%"><digi:trn>Disbursement</digi:trn></td>
			    <td width="50%" id="tooltipCurrentDisbursementContainer">&nbsp;</td>
		    </tr>
		    </field:display>
		     <field:display name="Measure Expenditure" feature="GIS DASHBOARD">
		    <tr>
			    <td nowrap width="50%"><digi:trn>Expenditure</digi:trn></td>
			    <td width="50%" id="tooltipCurrentExpenditureContainer">&nbsp;</td>
		    </tr>
		    </field:display>
		    <tr>
			    <td nowrap bgcolor="#D9DAC9" colspan="2"><digi:trn>Indicator</digi:trn></td>
		    </tr>
		    <tr>
			    <td nowrap width="50%" id="tooltipIndVal"><digi:trn>value</digi:trn></td>
			    <td width="50%" id="tooltipIndUnit">&nbsp;</td>
		    </tr>
		    <tr>
			    <td nowrap width="50%">Source</td>
			    <td id="tooltipIndSrc" style="width:100px; overflow-x: hidden;">&nbsp;</td>
		    </tr>
	    </table>
	    </div> 
    </div>
</c:if>


<c:if test="${isDevInfoMode == false}">
    <div id="tooltipContainer"  style="display:none; position: absolute; left:50px; top: 50px; background-color: #d9ceba; border: 1px solid silver;z-index: 2; width:200px;">
         <div style="border-top: 1px solid white; border-left: 1px solid white; border-bottom: 1px solid Black; border-right: 1px solid Black;">
        
        <table class="tableElement" bgcolor="#d9ceba" border="1" bordercolor="#c3b7a1" cellpadding="3" cellspacing="2" width="100%" style="border-collapse:collapse">
            <tr>
                <td nowrap width="50%" id="reg_district_caption"><digi:trn>Region</digi:trn></td>
                <td width="50%" id="tooltipRegionContainer">&nbsp;</td>
            </tr>
            <tr>
                <td nowrap width="50%" id="donor"><digi:trn>Donor</digi:trn></td>
                <td width="50%" id="tooltipDonorContainer">&nbsp;</td>
            </tr>
            
            <tr>
                <td nowrap bgcolor="#D9DAC9" colspan="2"><digi:trn>Funding details</digi:trn></td>
            </tr>
            <tr>
                <td colspan="2" nowrap bgcolor="#D9DAC9" id="tooltipCurencyYearRange">&nbsp;</td>
            </tr>
            <tr>
                <td bgcolor="#D9DAC9" colspan="2"><digi:trn>Total funding for this sector</digi:trn></td>
            </tr>
            <field:display name="Measure Commitment" feature="GIS DASHBOARD">
            <tr>
                <td nowrap width="50%"><digi:trn>Commitment</digi:trn></td>
                <td width="50%" id="tooltipTotalCommitmentContainer">&nbsp;</td>
            </tr>
            </field:display>
            <field:display name="Measure Disbursement" feature="GIS DASHBOARD">
            <tr>
                <td nowrap width="50%"><digi:trn>Disbursement</digi:trn></td>
                <td width="50%" id="tooltipTotalDisbursementContainer">&nbsp;</td>
            </tr>
            </field:display>
            <field:display name="Measure Expenditure" feature="GIS DASHBOARD">
            <tr>
                <td nowrap width="50%"><digi:trn>Expenditure</digi:trn></td>
                <td width="50%" id="tooltipTotalExpenditureContainer">&nbsp;</td>
            </tr>
            </field:display>
            <tr>
                <td nowrap bgcolor="#D9DAC9" colspan="2" id="reg_district_caption_for"><digi:trn>For this region</digi:trn></td>
            </tr>
            <field:display name="Measure Commitment" feature="GIS DASHBOARD">
            <tr>
                <td nowrap width="50%"><digi:trn>Commitment</digi:trn></td>
                <td width="50%" id="tooltipCurrentCommitmentContainer">&nbsp;</td>
            </tr>
            </field:display>
            <field:display name="Measure Disbursement" feature="GIS DASHBOARD">
            <tr>
                <td nowrap width="50%"><digi:trn>Disbursement</digi:trn></td>
                <td width="50%" id="tooltipCurrentDisbursementContainer">&nbsp;</td>
            </tr>
            </field:display>
            <field:display name="Measure Expenditure" feature="GIS DASHBOARD">
            <tr>
                <td nowrap width="50%"><digi:trn>Expenditure</digi:trn></td>
                <td width="50%" id="tooltipCurrentExpenditureContainer">&nbsp;</td>
            </tr>
            </field:display>
        </table>
        </div> 
    </div>
</c:if>

<script type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<script language="JavaScript" src="/repository/gis/view/js/gisMap.js"></script>


<script language="JavaScript" type="text/javascript">
	var selectIndicatorTxt = "<digi:trn>Select indicator</digi:trn>";
	var selectSubgroupTxt = "<digi:trn>Select subgroup</digi:trn>";
	var selectYearTxt = "<digi:trn>Select year</digi:trn>";
	
	<c:if test="${isDevInfoMode == false}">
		getSectorHierarchy();
	</c:if>
	
</script>