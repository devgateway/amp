<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ page import="org.digijava.module.gis.util.GisUtil" %>

<digi:instance property="gisDashboardForm"/>
<html:hidden property="selectedCurrency" styleId="selCurr"/>

<style>
	div.navHiden{
		background-color : #5e8ad1;
		color : white;
		border-left: 1px solid white;
		border-top: 1px solid white;
		border-right: 1px solid black;
		border-bottom: 1px solid black;
		cursor:pointer;
	}
	
	div.navVisible{
		background-color : #C4C4C4;
		color : black;
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
	
	
</style>

<!-- Individual YUI CSS files --> 
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css">

<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicContent.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-modalMessage.js"/>"></script>


<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/dragdrop/dragdrop-min.js"></script> 

<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script>


<!-- CSS -->
<link href="/TEMPLATE/ampTemplate/css_2/amp.css" rel="stylesheet" type="text/css"></link>
<link href="/TEMPLATE/ampTemplate/css_2/yui_tabs.css" rel="stylesheet" type="text/css"></link>
<link href="/TEMPLATE/ampTemplate/css_2/yui_popins.css" rel="stylesheet" type="text/css"></link>
<link href="/TEMPLATE/ampTemplate/css_2/yui_datatable.css" rel="stylesheet" type="text/css"></link>
<link href="/TEMPLATE/ampTemplate/css_2/desktop_yui_tabs.css" rel="stylesheet" type="text/css"></link>
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/tabview-core.css"> 


<link href="TEMPLATE/ampTemplate/css_2/amp.css" rel="stylesheet" type="text/css">
<link type="text/css" rel="stylesheet" href="/TEMPLATE/ampTemplate/css_2/yui_tabs.css">


<bean:define id="mapMode" name="gisDashboardForm" property="gisDashboardMode"/>
<bean:define id="isDevInfoMode">true</bean:define>
<bean:define id="finMode"><%= org.digijava.module.gis.util.GisUtil.GIS_MODE_FUNDINGS %></bean:define>
<c:if test="${finMode == mapMode}">
	<c:set var="isDevInfoMode">false</c:set>
</c:if>


<c:set var="validatedRegPercentage">
		<field:display name="Validate Mandatory Regional Percentage" feature="Location">true</field:display>
</c:set>
<c:if test="${validatedRegPercentage==''}">
	<c:set var="validatedRegPercentage">false</c:set>
</c:if>

<c:set var="displayeRegPercentage">
		<field:display name="Regional Percentage" feature="Location">true</field:display>
</c:set>
<c:if test="${displayeRegPercentage==''}">
	<c:set var="displayeRegPercentage">false</c:set>
</c:if>


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
    <logic:equal name="isDevInfoMode" value="true">
        showDevinfo = true;
    </logic:equal>
    
    var isPublic = false;
    <logic:present parameter="public">
			isPublic = true;
		</logic:present>
</script>


<script language="JavaScript">
	var sectorTitleTrn = "<digi:trn>Sector</digi:trn>";
	var programTitleTrn = "<digi:trn>Program</digi:trn>";
	
	var allTrn = "<digi:trn>All</digi:trn>";
	var multyTrn = "<digi:trn>Multiple</digi:trn>";
</script>




<jsp:include page="gisFilterScripts.jsp" />
<jsp:include page="gisFilter.jsp" />



<%--

<div id="filter_dialog" class="sec_map_filter_outer_frame" style="position:absolute;z-index:101;display: none;overflow: auto;">
	<jsp:include page="gisFilter.jsp"></jsp:include>
		
	</div>



<div class="filter_wnd_background_holder" style="display:none;">
	
	<div class="filter_wnd_background">&nbsp;</div>
	
	
</div>

 --%>





<fieldset>
	<legend>
		<span class="legend_label longTab">
			<digi:trn key="gis:regionalview">Regional View</digi:trn>
		</span>
	</legend>

            


<div id="ctrlContainer" style="display:none; ">
	<div id="navMapContainer" style="width:300px; height:300px; position:absolute; border:1px solid black; z-index:3;">
		<img id="navCursorMap" width="300" height="300" border="0" src="/gis/getFoundingDetails.do?action=paintMap&noCapt=true&width=300&height=300&mapLevel=2&mapCode=TZA">
	</div>
	<div id="navCursor" style="width:1px; height:1px; position: absolute; left: 172px; top: 165px; border: 1px solid white; cursor:pointer; z-index:3;">
		<div style="width:100%; height:100%; background:white; filter:alpha(opacity=30); opacity:0.3;"></div>
	</div>
	
		<div title="Zoom 1.0X" onClick="" id="mapZoom10" class="navVisible zoomBt" style="z-index:3; width:30px; position: absolute; top:430px; left:180px; font-size:12px;" align="center">1.0X</div>
		<div title="Zoom 1.5X" onClick="" id="mapZoom15" class="navHiden zoomBt" style="z-index:3; width:30px; position: absolute; top:430px; left:215px; font-size:12px;" align="center">1.5X</div>
		<div title="Zoom 2.0X" onClick="" id="mapZoom20" class="navHiden zoomBt" style="z-index:3; width:30px; position: absolute; top:430px; left:250px; font-size:12px;" align="center">2.0X</div>
		<div title="Zoom 3.0X" onClick="" id="mapZoom30" class="navHiden zoomBt" style="z-index:3; width:30px; position: absolute; top:430px; left:285px; font-size:12px;" align="center">3.0X</div>
	
	

</div>
<!-- 
<div class="navHiden" align="center" style="position: absolute; left:10px; top:32px; width:150px;" onClick="showNavigation(this)"><digi:trn>Map navigation</digi:trn></div>
 -->
 <!--
<div class="navHiden" id="mapNav" align="center" style="position: absolute; left: 165px; top: 160px; width:160px; font-size:12px;" onClick=""><digi:trn>Map navigation</digi:trn></div>
-->

	
<table cellpadding="5" cellspacing="1" border="0">
	<tr>
		<td colspan="3" align="left">
			<div class="navHiden" id="mapNav" align="center" style="font-size:12px;width:150px;"><digi:trn>Map navigation</digi:trn></div>
		</td>
	</tr>	
	<tr>
		<td colspan="3" align="left">
		  <!--
			<div id="mapCanvasContainer" style="border:1px solid black; width:500px; height:500px; overflow:hidden;"><img onLoad="initMouseOverEvt(); getImageMap(); checkIndicatorValues(); actionImgLoading = false; setBusy(false);" useMap="#areaMap" id="testMap" border="0" src="/gis/getFoundingDetails.do?action=paintMap&mapCode=TZA&mapLevel=2&uniqueStr=0&year=-1&width=500&height=500"></div>
		 -->
		 <div id="mapCanvasContainer" style="border:1px solid black; width:500px; height:500px; overflow:hidden;margin-left: auto;margin-right: auto;">
		 	<img style="position:relative; top:200px; left:200px; visibility:hidden; z-index:4;" id="imgLoadingIndicator" src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-circle.gif">
			<c:if test="${isDevInfoMode == true}">
				<img style="position:relative; top:-100px;" useMap="#areaMap" id="testMap" border="0" src="/gis/getFoundingDetails.do?action=paintMap&mapCode=TZA&mapLevel=2&uniqueStr=0&year=-1&width=500&height=500">
			</c:if>
			<c:if test="${isDevInfoMode == false}">
				<img style="position:relative; top:-100px;" useMap="#areaMap" id="testMap" border="0" src="/gis/getFoundingDetails.do?action=paintMap&mapCode=TZA&mapLevel=2&uniqueStr=0&width=500&height=500">
			</c:if>
			</div>
		  
		</td>
	</tr>
	
	<tr>
		<td colspan="3" align="left">
			<digi:img src="/gis/showAdditionalGraphics.do?actionType=legendGradient&width=500&height=20" border="0"/>
			
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
			<div id="imageMapContainer" style="visibility:hidden;"></div>
		</td>
	</tr>	

    
    
    
    
    
    
    <!-- DevInfo block -->
<c:if test="${isDevInfoMode == true}">
	<tr>
		<td colspan="3">
			<span style="font-size: 12px;">
				<digi:trn key="gis:minmax:message">
				Regions with the lowest (MIN) values for the selected indicator are shaded darker color. 
				Regions with the highest (MAX) value are shaded lighter color. 
				For some indicators (such as mortality rates), having the MAX value indicates the lowest performance.
				</digi:trn>
			</span>
			<br>
			<br>
			<span style="font-size: 12px;">
				<digi:trn key="gis:datasource:message">
					Data Source: Dev Info
				</digi:trn>
			</span>
		</td>
	</tr>
	<%--
	<tr>
		<td colspan="3">
			<img style="visibility:hidden" id="busyIndicator" src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif">
		</td>
	</tr>
	--%>
	
	
	<feature:display name="Show DevInfo data" module="GIS DASHBOARD">
	  <tr>
	    <td width="200" nowrap style="font-size:12px">
	      <digi:trn>Map Mode</digi:trn>:
	    </td>
	    
	    
	    
			<td width="90%">
				<c:choose>
			    <c:when test="${empty param.public}">
		        <html:form action="gis/index.do">
							<html:select style="width:250px" name="gisDashboardForm" property="gisDashboardMode" onchange="this.form.submit()">
								<html:option value="<%=GisUtil.GIS_MODE_FUNDINGS%>"><digi:trn>Activity Funding Data</digi:trn></html:option>
								<html:option value="<%=GisUtil.GIS_MODE_DEVINFO%>"><digi:trn>DevInfo Data</digi:trn></html:option>
							</html:select> 
						</html:form>
			    </c:when>
			    <c:otherwise>
		        <html:form action="gis/showPublicGis.do?public=true">
							<html:select style="width:250px" name="gisDashboardForm" property="gisDashboardMode" onchange="this.form.submit()">
								<html:option value="<%=GisUtil.GIS_MODE_FUNDINGS%>"><digi:trn>Activity Funding Data</digi:trn></html:option>
								<html:option value="<%=GisUtil.GIS_MODE_DEVINFO%>"><digi:trn>DevInfo Data</digi:trn></html:option>
							</html:select> 
						</html:form>
			    </c:otherwise>
				</c:choose>
			</td>
		</tr>
	</feature:display>
    
	<feature:display name="GIS DASHBOARD" module="GIS DASHBOARD">	
		<field:display name="Map Level Switch" feature="GIS DASHBOARD">
		<tr>
	        <td width="200" nowrap style="font-size:12px">
	                <b><digi:trn key="gis:selectMalLevel">Select Map Level</digi:trn>:</b>
	        </td>
			<td width="90%">
			 <!-- 
				<input title="Region view" type="Radio" value="2" name="mapLevelRadio" checked onClick="mapLevelChanged(this.value)">Region view &nbsp;
				<input title="District view" type="Radio" value="3" name="mapLevelRadio" onClick="mapLevelChanged(this.value)">District view
				-->
				<input title="Region view" type="Radio" value="2" name="mapLevelRadio" checked ><span style="font-size:12px"><digi:trn>Region view</digi:trn></span> &nbsp;
				<input title="District view" type="Radio" value="3" name="mapLevelRadio" ><span style="font-size:12px"><digi:trn>District view</digi:trn></span>
				
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
	
	<c:if test="${validatedRegPercentage == false || displayeRegPercentage == false}">
			<tr>
				<td colspan="2">
					<font color="red">
						<digi:trn key="gis:funding_msg">(*) Project funding is not disaggregated by region or district, and therefore reflect activity totals.</digi:trn>
					</font>
				</td>
			</tr>

	</c:if>
	
</c:if>
    
<!-- Financial data block -->    
<c:if test="${isDevInfoMode == false}">
    <tr>
        <td colspan="3">
            <span style="font-size: 12px;">
				<img alt="" style="width: 16px; height: 16px; vertical-align: middle;" src="/TEMPLATE/ampTemplate/images/info.png"/>&nbsp;
                <digi:trn key="gis:minmax:message_fin">Regions with the lowest (MIN) values for the selected funding type are shaded darker color. Regions with the highest (MAX) value are shaded lighter color.</digi:trn>
            </span>
            <br>
            <br>
            <span>
				<img style="width: 16px; height: 16px; vertical-align: middle;" src="/TEMPLATE/ampTemplate/images/info.png"/>
				&nbsp;<span style="font-size: 12px;"><digi:trn>Click on a region to view list of projects implemented in that Region</digi:trn></span>
            </span>
            <br>
            <br>
            <span style="font-size: 12px;">
            	<digi:trn key="gis:datasource:message_fin">
                Data Source AMP
	            </digi:trn>
	          </span>
        </td>
    </tr>
    
    <feature:display name="Show DevInfo data" module="GIS DASHBOARD">
    <tr>
	    <td nowrap style="font-size:12px">
	      <b><digi:trn>Map Mode</digi:trn>:</b>
	    </td>
			<td colspan="2">
				<c:choose>
			    <c:when test="${empty param.public}">
		        <html:form action="gis/index.do">
							<html:select style="width:250px" name="gisDashboardForm" property="gisDashboardMode" onchange="this.form.submit()">
								<html:option value="<%=GisUtil.GIS_MODE_FUNDINGS%>"><digi:trn>Activity Funding Data</digi:trn></html:option>
								<html:option value="<%=GisUtil.GIS_MODE_DEVINFO%>"><digi:trn>DevInfo Data</digi:trn></html:option>
							</html:select> 
						</html:form>
			    </c:when>
			    <c:otherwise>
		        <html:form action="gis/showPublicGis.do?public=true">
							<html:select style="width:250px" name="gisDashboardForm" property="gisDashboardMode" onchange="this.form.submit()">
								<html:option value="<%=GisUtil.GIS_MODE_FUNDINGS%>"><digi:trn>Activity Funding Data</digi:trn></html:option>
								<html:option value="<%=GisUtil.GIS_MODE_DEVINFO%>"><digi:trn>DevInfo Data</digi:trn></html:option>
							</html:select> 
						</html:form>
			    </c:otherwise>
				</c:choose>
			</td>
		</tr>
	</feature:display>
    
    <tr>
        <td colspan="3">
            <img style="visibility:hidden" id="busyIndicator" src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif">
        </td>
    </tr>
    
    
    <%--
    <feature:display name="GIS DASHBOARD" module="GIS DASHBOARD">    
        <field:display name="Map Level Switch" feature="GIS DASHBOARD">
        <tr>
            <td width="200" nowrap style="font-size:12px">
                    <b><digi:trn key="gis:selectMalLevel">Select Map Level</digi:trn>:</b>
            </td>
            <td width="90%" colspan="2">
                <input title="Region view" type="Radio" value="2" name="mapLevelRadio" checked ><span style="font-size:12px"><digi:trn>Region view</digi:trn></span> &nbsp;
								<input title="District view" type="Radio" value="3" name="mapLevelRadio" ><span style="font-size:12px"><digi:trn>District view</digi:trn></span>
                
            </td>
        </tr>
        </field:display>
    </feature:display>
		--%>
    
    
    <tr>
    	<td><input type="button" value="<digi:trn>Show filters</digi:trn>" onClick = "showFilter()" class="buttonx"></td>
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
</fieldset>   



<c:if test="${isDevInfoMode == true}">
    <div id="tooltipContainer"  style="display:none; position: absolute; left:50px; top: 50px; background-color: #dcd8c1; color:white; border: 1px solid silver;z-index: 2; width:200px;">
 	    <div style="border-top: 1px solid white; border-left: 1px solid white; border-bottom: 1px solid Black; border-right: 1px solid Black;">
	    
	    <table class="tableElement" border="1" color="white" bgcolor="#dcd8c1" bordercolor="#c3b7a1" cellpadding="3" cellspacing="2" width="100%" style="border-collapse:collapse; style="font-size: 12px;"">
		    <tr>
			    <td nowrap width="50%" id="reg_district_caption" style="font-size: 12px; color:#373735;"><digi:trn>Region</digi:trn></td>
			    <td width="50%" id="tooltipRegionContainer" style="font-size: 12px; color:#373735;">&nbsp;</td>
		    </tr>

		    <tr>
			    <td nowrap bgcolor="#c3b6a5" colspan="2" style="font-size: 12px; color:#373735;"><digi:trn>Funding details</digi:trn></td>
		    </tr>
		    <tr>
			    <td colspan="2" nowrap id="tooltipCurencyYearRange" style="font-size: 12px; color:#373735;">&nbsp;</td>
		    </tr>
		    <tr>
			    <td nowrap bgcolor="#c3b6a5" colspan="2" style="font-size: 12px; color:#373735;"><digi:trn>Total funding for this sector</digi:trn></td>
		    </tr>
		    <field:display name="Measure Commitment" feature="GIS DASHBOARD">
		    <tr>
			    <td nowrap width="50%" style="font-size: 12px; color:#373735;"><digi:trn>Commitment</digi:trn></td>
			    <td width="50%" id="tooltipTotalCommitmentContainer" style="font-size: 12px; color:#373735;">&nbsp;</td>
		    </tr>
		    </field:display>
		    <field:display name="Measure Disbursement" feature="GIS DASHBOARD">
		    <tr>
			    <td nowrap width="50%" style="font-size: 12px; color:#373735;"><digi:trn>Disbursement</digi:trn></td>
			    <td width="50%" id="tooltipTotalDisbursementContainer" style="font-size: 12px; color:#373735;">&nbsp;</td>
		    </tr>
		    </field:display>
		    <field:display name="Measure Expenditure" feature="GIS DASHBOARD">
		    <tr>
			    <td nowrap width="50%" style="font-size: 12px; color:#373735;"><digi:trn>Expenditure</digi:trn></td>
			    <td width="50%" id="tooltipTotalExpenditureContainer" style="font-size: 12px; color:#373735;">&nbsp;</td>
		    </tr>
		    </field:display>
		    <tr>
			    <td nowrap bgcolor="#c3b6a5" colspan="2" id="reg_district_caption_for" style="font-size: 12px; color:#373735;"><digi:trn>For this region</digi:trn></td>
		    </tr>
		     <field:display name="Measure Commitment" feature="GIS DASHBOARD">
		    <tr>
			    <td nowrap width="50%" style="font-size: 12px; color:#373735;"><digi:trn>Commitment</digi:trn></td>
			    <td width="50%" id="tooltipCurrentCommitmentContainer" style="font-size: 12px; color:#373735;">&nbsp;</td>
		    </tr>
		    </field:display>
		     <field:display name="Measure Disbursement" feature="GIS DASHBOARD">
		    <tr>
			    <td nowrap width="50%" style="font-size: 12px; color:#373735;"><digi:trn>Disbursement</digi:trn></td>
			    <td width="50%" id="tooltipCurrentDisbursementContainer" style="font-size: 12px; color:#373735;">&nbsp;</td>
		    </tr>
		    </field:display>
		     <field:display name="Measure Expenditure" feature="GIS DASHBOARD">
		    <tr>
			    <td nowrap width="50%" style="font-size: 12px; color:#373735;"><digi:trn>Expenditure</digi:trn></td>
			    <td width="50%" id="tooltipCurrentExpenditureContainer" style="font-size: 12px; color:#373735;">&nbsp;</td>
		    </tr>
		    </field:display>
		    <tr>
			    <td nowrap bgcolor="#c3b6a5" colspan="2" style="font-size: 12px; color:#373735;"><digi:trn>Indicator</digi:trn></td>
		    </tr>
		    <tr>
			    <td nowrap width="50%" id="tooltipIndVal" style="font-size: 12px; color:#373735;"><digi:trn>value</digi:trn></td>
			    <td width="50%" id="tooltipIndUnit" style="font-size: 12px; color:#373735;">&nbsp;</td>
		    </tr>
		    <tr>
			    <td nowrap width="50%" style="font-size: 12px; color:#373735;">Source</td>
			    <td id="tooltipIndSrc" style="width:100px; overflow-x: hidden; font-size: 12px; color:#373735;">&nbsp;</td>
		    </tr>
	    </table>
	    </div> 
    </div>
</c:if>


<c:if test="${isDevInfoMode == false}">
    <div id="tooltipContainer"  style="display:none; position: absolute; left:50px; top: 50px; background-color: #dcd8c1; color:white; border: 1px solid silver;z-index: 2; width:200px;">
         <div style="border-top: 1px solid white; border-left: 1px solid white; border-bottom: 1px solid Black; border-right: 1px solid Black; style="font-size: 12px;"">
        
        <table class="tableElement" bgcolor="#dcd8c1" color="white" border="1" bordercolor="#c3b7a1" cellpadding="3" cellspacing="2" width="100%" style="border-collapse:collapse">
            <tr>
                <td color="white" nowrap width="50%" id="reg_district_caption" style="font-size: 12px; color:#373735;"><digi:trn>Region</digi:trn></td>
                <td color="white" width="50%" id="tooltipRegionContainer" style="font-size: 12px; color:#373735;">&nbsp;</td>
            </tr>
            <tr>
                <td color="white" nowrap width="50%" id="donor" style="font-size: 12px; color:#373735;"><digi:trn>Donor</digi:trn></td>
                <td color="white" width="50%" id="tooltipDonorContainer" style="font-size: 12px; color:#373735;">&nbsp;</td>
            </tr>
            
            <tr>
                <td color="white" nowrap bgcolor="#c3b6a5" colspan="2" style="font-size: 12px; color:#373735;"><digi:trn>Funding details</digi:trn></td>
            </tr>
            <tr>
                <td color="white" colspan="2" nowrap bgcolor="#c3b6a5" id="tooltipCurencyYearRange" style="font-size: 12px; color:#373735;">&nbsp;</td>
            </tr>
            <tr>
                <td color="white" bgcolor="#c3b6a5" colspan="2" style="font-size: 12px; color:#373735;"><digi:trn>Total funding for this sector</digi:trn></td>
            </tr>
            <field:display name="Measure Commitment" feature="GIS DASHBOARD">
            <tr>
                <td color="white" nowrap width="50%" style="font-size: 12px; color:#373735;"><digi:trn>Commitment</digi:trn></td>
                <td color="white" width="50%" id="tooltipTotalCommitmentContainer" style="font-size: 12px; color:#373735;">&nbsp;</td>
            </tr>
            </field:display>
            <field:display name="Measure Disbursement" feature="GIS DASHBOARD">
            <tr>
                <td color="white" nowrap width="50%" style="font-size: 12px; color:#373735;"><digi:trn>Disbursement</digi:trn></td>
                <td color="white" width="50%" id="tooltipTotalDisbursementContainer" style="font-size: 12px; color:#373735;">&nbsp;</td>
            </tr>
            </field:display>
            <field:display name="Measure Expenditure" feature="GIS DASHBOARD">
            <tr>
                <td color="white" nowrap width="50%" style="font-size: 12px; color:#373735;"><digi:trn>Expenditure</digi:trn></td>
                <td color="white" width="50%" id="tooltipTotalExpenditureContainer" style="font-size: 12px; color:#373735;">&nbsp;</td>
            </tr>
            </field:display>
            <tr>
                <td color="white" nowrap bgcolor="#c3b6a5" colspan="2" id="reg_district_caption_for" style="font-size: 12px; color:#373735;"><digi:trn>For this region</digi:trn></td>
            </tr>
            <field:display name="Measure Commitment" feature="GIS DASHBOARD">
            <tr>
                <td color="white" nowrap width="50%" style="font-size: 12px; color:#373735;"><digi:trn>Commitment</digi:trn></td>
                <td color="white" width="50%" id="tooltipCurrentCommitmentContainer" style="font-size: 12px; color:#373735;">&nbsp;</td>
            </tr>
            </field:display>
            <field:display name="Measure Disbursement" feature="GIS DASHBOARD">
            <tr>
                <td color="white" nowrap width="50%" style="font-size: 12px; color:#373735;"><digi:trn>Disbursement</digi:trn></td>
                <td color="white" width="50%" id="tooltipCurrentDisbursementContainer" style="font-size: 12px; color:#373735;">&nbsp;</td>
            </tr>
            </field:display>
            <field:display name="Measure Expenditure" feature="GIS DASHBOARD">
            <tr>
                <td color="white" nowrap width="50%" style="font-size: 12px; color:#373735;"><digi:trn>Expenditure</digi:trn></td>
                <td color="white" width="50%" id="tooltipCurrentExpenditureContainer" style="font-size: 12px; color:#373735;">&nbsp;</td>
            </tr>
            </field:display>
        </table>
        </div> 
    </div>
</c:if>

<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>
<script language="JavaScript" src="/repository/gis/view/js/gisMap.js"></script>


<script language="JavaScript" type="text/javascript">
	var selectIndicatorTxt = "<digi:trn>Select indicator</digi:trn>";
	var selectSubgroupTxt = "<digi:trn>Select subgroup</digi:trn>";
	var selectYearTxt = "<digi:trn>Select year</digi:trn>";
	

	YAHOO.util.Event.addListener(window, "load", initScripts) ;
</script>