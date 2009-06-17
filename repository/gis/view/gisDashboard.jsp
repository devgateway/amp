<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
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
	
	option.dsbl {
		color:gray;;
	}
	
	option.enbl {
		color:Black;;
	}
	
</style>

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

<div id="content" class="yui-skin-sam" style="width:600px;height:100%;max-width: 600x;">
  <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;width: 600;">
      <digi:img src="images/tabrightcorner.gif" align="right" hspace="0"/>
      <digi:img src="images/tableftcorner.gif" align="left" hspace="0"/>
      <div class="longTab">
          <digi:trn key="gis:regionalview">Regional View</digi:trn>
      </div>
            
    <div id="div1" class="yui-content" style="font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">


<div id="ctrlContainer" style="display:none">
	<div style="width:300px; height:300px; position: absolute; left: 15px; top: 35px; border: 1px solid black"><img id="navCursorMap" width="300" height="300" border="0" src="/gis/getFoundingDetails.do?action=paintMap&noCapt=true&width=300&height=300&mapLevel=2&mapCode=TZA"></div>
	<div id="navCursor" style="width:1px; height:1px; position: absolute; left: 23px; top: 43px; border: 1px solid white; cursor:pointer;">
		<div style="width:100%; height:100%; background:white; filter:alpha(opacity=30); -moz-opacity:0.3;"></div>
	</div>
	
		<div title="Zoom 1.0X" onClick="zoomMap (this, 1)" id="mapZoom10" class="navVisible" style="width:30px; position: absolute; top:310px; left:25px;" align="center">1.0X</div>
		<div title="Zoom 1.5X" onClick="zoomMap (this, 1.5)" id="mapZoom15" class="navHiden" style="width:30px; position: absolute; top:310px; left:60px;" align="center">1.5X</div>
		<div title="Zoom 2.0X" onClick="zoomMap (this, 2)" id="mapZoom20" class="navHiden" style="width:30px; position: absolute; top:310px; left:95px;" align="center">2.0X</div>
		<div title="Zoom 3.0X" onClick="zoomMap (this, 3)" id="mapZoom30" class="navHiden" style="width:30px; position: absolute; top:310px; left:130px;" align="center">3.0X</div>
	
	

</div>

<div class="navHiden" align="center" style="position: absolute; left:10px; top:32px; width:150px;" onClick="showNavigation(this)"><digi:trn>Map navigation</digi:trn></div>



	
<table cellpadding="5" cellspacing="1">
	<tr>
		<td colspan="2">
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
	
	<feature:display name="GIS DASHBOARD" module="GIS DASHBOARD">	
		<field:display name="Map Level Switch" feature="GIS DASHBOARD">
		<tr>
	        <td width="200" nowrap style="font-size:12px">
	                <digi:trn key="gis:selectMalLevel">Select Map Level</digi:trn>:
	        </td>
			<td width="90%">
				<input title="Region view" type="Radio" value="2" name="mapLevelRadio" checked onClick="mapLevelChanged(this.value)">Region view &nbsp;
				<input title="District view" type="Radio" value="3" name="mapLevelRadio" onClick="mapLevelChanged(this.value)">District view
			</td>
		</tr>
		</field:display>
	</feature:display>
	
	<div id="imageMapContainer" style="visibility:hidden;"></div>	
	
	<tr>
        <td nowrap width="200" style="font-size:12px">
             <digi:trn>Select Sector</digi:trn>:
        </td>
		<td>
			<select id="sectorsMapCombo" onChange="sectorSelected(this.value)" style="width:350px">
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
		<td>
		<select id="indicatorsCombo" onchange="indicatorSelected(this)" style="width:350px">
			<option value=-1><digi:trn>Select Indicator</digi:trn></option>
		</select>
		</td>
	</tr>
	<tr>
        <td width="200" style="font-size:12px">
            <digi:trn>Select Subgroup</digi:trn>:
        </td>
		<td>
			<select id="indicatorSubgroupCombo" onChange="subgroupSelected(this)" style="width:350px">
				<option value="-1"><digi:trn>Select subgroup</digi:trn></option>
			</select>
		</td>
	</tr>
	<tr>
        <td nowrap width="200" style="font-size:12px">
            <digi:trn>Select Time Interval</digi:trn>:
        </td>
		<td>
			<select id="indicatorYearCombo" onChange="yearSelected(this)" style="width:350px">
				<option value="-1"><digi:trn>Select Time Interval</digi:trn></option>
			</select>
		</td>
	</tr>
	
	<script language="JavaScript">
		if (!validatedRegPercentage || !displayeRegPercentage) {
			document.write('<tr><td nowrap colspan="2"><font color="red">');
			document.write('(*) Project funding is not disaggregated by region or district, and therefore reflect activity totals.');
			document.write('</font></td></tr>');
		}
	</script>
	
</table>
    </div>
  </div>
</div>   

<div id="tooltipContainer" style="display:none; width:200; position: absolute; left:50px; top: 50px; background-color: #d9ceba; border: 1px solid silver;z-index: 2;">
	<div style="border-top: 1px solid white; border-left: 1px solid white; border-bottom: 1px solid Black; border-right: 1px solid Black;">
	
	<table border="1" bordercolor="#c3b7a1" cellpadding="3" cellspacing="2" width="100%" style="border-collapse:collapse">
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
		<tr>
			<td nowrap width="50%"><digi:trn>Commitment</digi:trn></td>
			<td width="50%" id="tooltipTotalCommitmentContainer">&nbsp;</td>
		</tr>
		<tr>
			<td nowrap width="50%"><digi:trn>Disbursement</digi:trn></td>
			<td width="50%" id="tooltipTotalDisbursementContainer">&nbsp;</td>
		</tr>
		<tr>
			<td nowrap width="50%"><digi:trn>Expenditure</digi:trn></td>
			<td width="50%" id="tooltipTotalExpenditureContainer">&nbsp;</td>
		</tr>
		
		<tr>
			<td nowrap bgcolor="#D9DAC9" colspan="2" id="reg_district_caption_for"><digi:trn>For this region</digi:trn></td>
		</tr>
		<tr>
			<td nowrap width="50%"><digi:trn>Commitment</digi:trn></td>
			<td width="50%" id="tooltipCurrentCommitmentContainer">&nbsp;</td>
		</tr>
		<tr>
			<td nowrap width="50%"><digi:trn>Disbursement</digi:trn></td>
			<td width="50%" id="tooltipCurrentDisbursementContainer">&nbsp;</td>
		</tr>
		<tr>
			<td nowrap width="50%"><digi:trn>Expenditure</digi:trn></td>
			<td width="50%" id="tooltipCurrentExpenditureContainer">&nbsp;</td>
		</tr>
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
<script language="JavaScript" src="/repository/gis/view/js/gisMap.js"></script>