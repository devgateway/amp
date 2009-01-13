<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:instance property="gisDashboardForm"/>

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
</style>

<div id="content" class="yui-skin-sam" style="width:100%;height:100%">
  <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
    <ul class="yui-nav">
        <li class="selected">
            <div class="nohover">
                <a style="cursor:default">
                    <div>
                        <digi:trn key="gis:regionalview">Regional View</digi:trn>
                    </div>
                </a>
            </div>
        </li>
    </ul>
    <div id="div1" class="yui-content" style="font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">


<div id="ctrlContainer" style="display:none">
	<div style="width:300px; height:300px; position: absolute; left: 15px; top: 35px; border: 1px solid black"><img id="navCursorMap" width="300" height="300" border="0" src="/gis/getFoundingDetails.do?action=paintMap&noCapt=true&width=300&height=300&mapLevel=2&mapCode=TZA"></div>
	<div id="navCursor" style="width:1px; height:1px; position: absolute; left: 23px; top: 43px; border: 1px solid white; cursor:pointer;">
		<div style="width:100%; height:100%; background:white; filter:alpha(opacity=30); -moz-opacity:0.3;"></div>
	</div>
	
		<div title="Zoom 1.0X" onClick="zoomMap (this, 1)" id="mapZoom10" class="navVisible" style="width:30px; position: absolute; top:310px; left:25px;" align="center">1.0X</div>
		<div title="Zoom 1.5X" onClick="zoomMap (this, 1.5)" id="mapZoom15" class="navHiden" style="width:30px; position: absolute; top:310px; left:60px;" align="center">1.5X</div>
		<div title="Zoom 2.0X" onClick="zoomMap (this, 2)" id="mapZoom20" class="navHiden" style="width:30px; position: absolute; top:310px; left:95px;" align="center">2.0X</div>
	
	

</div>

<div class="navHiden" align="center" style="position: absolute; left:10px; top:32px; width:150px;" onClick="showNavigation(this)">Map navigation</div>



	
<table cellpadding="5" cellspacing="1">
	<tr>
		<td colspan="2">
			<!-- onscroll="mapScroll(this)"-->
			<div id="mapCanvasContainer" style="border:1px solid black; width:500px; height:500px; overflow:hidden;"><img onLoad="ajaxInit(); initMouseOverEvt(); getImageMap(); checkIndicatorValues(); actionImgLoading = false; setBusy(false);" useMap="#areaMap" id="testMap" border="0" src="/gis/getFoundingDetails.do?action=paintMap&mapCode=TZA&mapLevel=2&uniqueStr=0&year=-1&width=500&height=500"></div>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<digi:img src="module/gis/images/fundingLegend.png" border="0"/>
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
	<%--
	<tr>
		<td colspan="2">
			<textarea cols="50" rows="3" id="debugtxt">oeee</textarea>
		</td>
	</tr>
	--%>
	<tr>
		<td colspan="2">
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
		<td colspan="2">
			<img style="visibility:hidden" id="busyIndicator" src="/TEMPLATE/ampTemplate/images/amploading.gif">
		</td>
	</tr>
	
	<tr>
            <td width="15%" nowrap>
                <digi:trn key="gis:selectMalLevel">Select Map Level</digi:trn>:
            </td>
		<td>
		<%--
		<select id="mapLevelCombo" onchange="mapLevelChanged(this.value)">
			<option value="2">Region view</option>
			<option value="3">District view</option>
		</select>
		--%>
		<input title="Region view" type="Radio" value="2" name="mapLevelRadio" checked onchange="mapLevelChanged(this.value)">Region view &nbsp;
		<input title="District view" type="Radio" value="3" name="mapLevelRadio" onchange="mapLevelChanged(this.value)">District view
		
		<div id="imageMapContainer" style="visibility:hidden;"></div>
		</td>
	</tr>
	
	<tr>
            <td width="15%" nowrap>
                <digi:trn key="gis:selectSector">Select Sector</digi:trn>:
            </td>
		<td>
		<select id="sectorsMapCombo" onChange="sectorSelected(this)">
			<option value="-1">Select sector</option>
			<logic:iterate name="gisDashboardForm" property="sectorCollection" id="sec">
				<option value="<bean:write name="sec" property="ampSectorId"/>"><bean:write name="sec" property="name"/></option>
			</logic:iterate>
		</select>

		</td>
	</tr>
	<tr>
            <td width="15%" nowrap>
                <digi:trn key="gis:selectIndicator">Select Indicator</digi:trn>:
            </td>
		<td>
		<select id="indicatorsCombo" onchange="indicatorSelected(this)">
			<option value=-1>Select indicator</option>
		</select>
		</td>
	</tr>
	<tr>
        <td width="15%" nowrap>
            <digi:trn key="gis:selectIndicatorSubgroups">Select subgroup for indicator data</digi:trn>:
        </td>
		<td>
		<select id="indicatorSubgroupCombo" onChange="subgroupSelected(this)">
			<option value="-1">Select subgroup</option>
		</select>
		</td>
	</tr>
	<tr>
        <td width="15%" nowrap>
            <digi:trn key="gis:selectIndicatorYear">Select year for indicator data</digi:trn>:
        </td>
		<td>
		<select id="indicatorYearCombo" onChange="yearSelected(this)">
			<option value="-1">Select year</option>
		</select>

		</td>
	</tr>
	
	
</table>
    </div>
  </div>
</div>   

<div id="tooltipContainer" style="display:none; width:200; position: absolute; left:50px; top: 50px; background-color: #d9ceba; border: 1px solid silver;z-index: 2;">
	<div style="border-top: 1px solid white; border-left: 1px solid white; border-bottom: 1px solid Black; border-right: 1px solid Black;">
	
	<table border="1" bordercolor="#c3b7a1" cellpadding="3" cellspacing="2" width="100%" style="border-collapse:collapse">
		<tr>
			<td nowrap width="50%" id="reg_district_caption">Region</td>
			<td width="50%" id="tooltipRegionContainer">&nbsp;</td>
		</tr>

		<tr>
			<td nowrap bgcolor="#D9DAC9" colspan="2">Funding details</td>
		</tr>
		<tr>
			<td colspan="2" nowrap bgcolor="#D9DAC9" id="tooltipCurencyYearRange">&nbsp;</td>
		</tr>
		<tr>
			<td nowrap bgcolor="#D9DAC9" colspan="2">Total funding for this sector</td>
		</tr>
		<tr>
			<td nowrap width="50%">Commitment</td>
			<td width="50%" id="tooltipTotalCommitmentContainer">&nbsp;</td>
		</tr>
		<tr>
			<td nowrap width="50%">Disbursement</td>
			<td width="50%" id="tooltipTotalDisbursementContainer">&nbsp;</td>
		</tr>
		<tr>
			<td nowrap width="50%">Expenditure</td>
			<td width="50%" id="tooltipTotalExpenditureContainer">&nbsp;</td>
		</tr>
		
		<tr>
			<td nowrap bgcolor="#D9DAC9" colspan="2" id="reg_district_caption_for">For this region</td>
		</tr>
		<tr>
			<td nowrap width="50%">Commitment</td>
			<td width="50%" id="tooltipCurrentCommitmentContainer">&nbsp;</td>
		</tr>
		<tr>
			<td nowrap width="50%">Disbursement</td>
			<td width="50%" id="tooltipCurrentDisbursementContainer">&nbsp;</td>
		</tr>
		<tr>
			<td nowrap width="50%">Expenditure</td>
			<td width="50%" id="tooltipCurrentExpenditureContainer">&nbsp;</td>
		</tr>
		<tr>
			<td nowrap bgcolor="#D9DAC9" colspan="2">Indicator</td>
		</tr>
		<tr>
			<td nowrap width="50%" id="tooltipIndVal">value</td>
			<td width="50%" id="tooltipIndUnit">&nbsp;</td>
		</tr>
	</table>
	</div>
</div>


<script language="JavaScript" src="/repository/gis/view/js/gisMap.js"></script>