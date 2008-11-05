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
</style>

<div id="content" class="yui-skin-sam" style="width:100%;">
  <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
    <ul class="yui-nav">
      <li class="selected">
        <a href="#">
        <div>
          <digi:trn key="gis:regionalview">Regional View</digi:trn>
        </div>
        </a>
      </li>
    </ul>
    <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">


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



	
<table>
	<tr>
		<td colspan="2">
			<!-- onscroll="mapScroll(this)"-->
			<div id="mapCanvasContainer" style="border:1px solid black; width:700px; height:450px; overflow:hidden;"><img onLoad="ajaxInit(); initMouseOverEvt(); getImageMap(); checkIndicatorValues();" useMap="#areaMap" id="testMap" border="0" src="/gis/getFoundingDetails.do?action=paintMap&mapCode=TZA&mapLevel=2&year=-1&width=700&height=700"></div>
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
		<select id="mapLevelCombo" onchange="mapLevelChanged(this.value)">
			<option value="2">Region view</option>
			<option value="3">District view</option>
		</select>
		<div id="imageMapContainer" style="visibility:hidden;"></div>
		</td>
	</tr>
	
	<tr>
            <td width="15%" nowrap>
                <digi:trn key="gis:selectSector">Select Sector</digi:trn>:
            </td>
		<td>
		<select id="sectorsMapCombo" onChange="sectorSelected(this)">
			<option value="-1">All</option>
			<logic:iterate name="gisDashboardForm" property="sectorCollection" id="sec">
				<option value="<bean:write name="sec" property="ampSectorId"/>"><bean:write name="sec" property="name"/></option>
			</logic:iterate>
		</select>
		<div id="imageMapContainer" style="visibility:hidden;"></div>
		</td>
	</tr>
	<tr>
            <td width="15%" nowrap>
                <digi:trn key="gis:selectIndicator">Select Indicator</digi:trn>:
            </td>
		<td>
		<select id="indicatorsCombo" onchange="indicatorSelected(this)">
			<option value=-1>None</option>
		</select>
		</td>
	</tr>
	<tr>
        <td width="15%" nowrap>
            <digi:trn key="gis:selectIndicatorYear">Select year for indicator data</digi:trn>:
        </td>
		<td>
		<select id="indicatorYearCombo" onChange="yearSelected(this)">
			<option value="-1">None</option>
			<logic:iterate name="gisDashboardForm" property="availYears" id="indYares">
				<option value="<bean:write name="indYares"/>"><bean:write name="indYares"/></option>
			</logic:iterate>
		</select>
		<div id="imageMapContainer" style="visibility:hidden;"></div>
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
			<td nowrap width="50%">Region</td>
			<td width="50%" id="tooltipRegionContainer">&nbsp;</td>
		</tr>
		<tr>
			<td nowrap bgcolor="#D9DAC9" colspan="2">Total funding</td>
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
			<td nowrap bgcolor="#D9DAC9" colspan="2">For this region</td>
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



<script language="JavaScript">

	var canvasWidth = 700;
	var canvasHeight = 700;
	
	var canvasContainerWidth = 700;
	var canvasContainerHeight = 450;
	
	var navigationWidth = 300;
	var navigationHeight = 300;
	
	
	
	var navCursorWidth = 1;
	var navCursorHeight = 1;
	
	var currentZoomRatio = 1;
	var currentZoomObj = document.getElementById("mapZoom10");

	
	function initMouseOverEvt() {
		document.onmousemove = mouseMoveTranslatorIE;
	}
	
	function mouseMoveTranslatorIE(e) {
			if (e == null) {
				e = window.event;
			}
						
			document.getElementById("tooltipContainer").style.left = e.clientX  + 2 + "px";
			document.getElementById("tooltipContainer").style.top = e.clientY + document.body.scrollTop + 2 + "px";
	}
	
	
	var mouseX, mouseY;
	var evt;
	var xmlhttp = null;

	
	function ajaxInit() {
		xmlhttp = new XMLHttpRequest();
	
		if (xmlhttp == null) {
			xmlhttp = ActiveXObject("Microsoft.XMLHTTP")
		}
	}

	
	var imageMapLoaded = false;
	
	var fundingDataByRegion = new Array();
	var indicatorDataByRegion = new Array();
	var selIndicatorUnit = "N/A";

	var totalCommitmentFund = "0";
	var totalDisbursementFund = "0";
	var totalExpenditureFund = "0";

	var selSector = 0;
	
	var getIndValuesAction = false;
	
	function sectorSelected(sec) {

		var selSector = sec.value;
		setBusy(true);
		var mapLevel = document.getElementById("mapLevelCombo").value;
		var indYear = document.getElementById("indicatorYearCombo").value;
		var uniqueStr = (new Date()).getTime();
		document.getElementById("testMap").src = "../../gis/getFoundingDetails.do?action=getDataForIndicator&mapCode=TZA&mapLevel=" + mapLevel + "&indYear=" + indYear + "&sectorId=" + selSector + "&indicatorId=-1" + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight;
		getDataForSector(sec);
	}
	
	function indicatorSelected(ind) {
			selIndicator = ind.value;
			var mapLevel = document.getElementById("mapLevelCombo").value;
			var sec = document.getElementById("sectorsMapCombo").value;
			var uniqueStr = (new Date()).getTime();
			xmlhttp.open("GET", "../../gis/getFoundingDetails.do?action=getAvailIndicatorYears&mapCode=TZA&mapLevel=" + mapLevel + "&sectorId=" + sec + "&indicatorId=" + selIndicator + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight, true);
			xmlhttp.onreadystatechange = availYearsReady;
			xmlhttp.send(null);
			
			
			document.getElementById("testMap").src = "../../gis/getFoundingDetails.do?action=getDataForIndicator&mapCode=TZA&mapLevel=" + mapLevel + "&sectorId=" + sec + "&indicatorId=-1" + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight;
			
			
			setBusy(true);
	
	
		/*
		setBusy(true);
		var mapLevel = document.getElementById("mapLevelCombo").value;
		var indYear = document.getElementById("indicatorYearCombo").value;
		var uniqueStr = (new Date()).getTime();
		document.getElementById("testMap").src = "../../gis/getFoundingDetails.do?action=getDataForIndicator&mapCode=TZA&mapLevel=" + mapLevel + "&indYear=" + indYear + "&sectorId=" + selSector + "&indicatorId=" + ind.value + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight;
		getIndValuesAction = true;
		*/
	}
	
	function availYearsReady () {
		if (xmlhttp.readyState == 4) {
			var availYearList = xmlhttp.responseXML.getElementsByTagName('year');
			var yearIndex = 0;
			
			var selectCmb = document.getElementById("indicatorYearCombo");
			selectCmb.innerHTML = null;
			var noneOpt = document.createElement("OPTION");
			noneOpt.value="-1";
			noneOpt.text="none";
			selectCmb.options.add(noneOpt);			
			
			
			for (yearIndex = 0; yearIndex < availYearList.length; yearIndex ++) {
				var yearData = availYearList[yearIndex];
				var opt = document.createElement("OPTION");
				opt.value=yearData.attributes.getNamedItem("value").value;
				opt.text=yearData.attributes.getNamedItem("value").value;
				selectCmb.options.add(opt);				
			}

			/*
			initIndicatorValues();
			window.setTimeout(getSectorIndicators, 100);
			*/
			setBusy(false);
		}
		
	}

	
	function getImageMap() {
		if (!imageMapLoaded) {
			var mapLevel = document.getElementById("mapLevelCombo").value;
			var indYear = document.getElementById("indicatorYearCombo").value;
			var uniqueStr = (new Date()).getTime();
			xmlhttp.open("GET", "../../gis/getFoundingDetails.do?action=getImageMap&mapCode=TZA&mapLevel=" + mapLevel + "&indYear=" + indYear + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight, true);
			xmlhttp.onreadystatechange = addImageMap;
			xmlhttp.send(null);
		} else {
			setBusy(false);
		}
	}
	
	function addImageMap() {
		if (xmlhttp.readyState == 4) {
			imageMapLoaded = true;
			document.getElementById("imageMapContainer").innerHTML = null;
			document.getElementById("imageMapContainer").innerHTML = generateImageMap (xmlhttp.responseXML);
			
			document.getElementById("testMap").removeAttribute("useMap");
			document.getElementById("testMap").setAttribute("useMap", "#areaMap");
		}
	}
	
	function getDataForSector(sec) {
			selSector = sec.value;
			var mapLevel = document.getElementById("mapLevelCombo").value;
			var indYear = document.getElementById("indicatorYearCombo").value;
			var uniqueStr = (new Date()).getTime();
			xmlhttp.open("GET", "../../gis/getFoundingDetails.do?action=getSectorDataXML&mapCode=TZA&mapLevel=" + mapLevel + "&indYear=" + indYear + "&sectorId=" + sec.value + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight, true);
			xmlhttp.onreadystatechange = dataForSectorReady;
			xmlhttp.send(null);
	}
	
	function dataForSectorReady () {
		if (xmlhttp.readyState == 4) {
			totalCommitmentFund = xmlhttp.responseXML.getElementsByTagName('funding')[0].attributes.getNamedItem("totalCommitment").value;
			totalDisbursementFund = xmlhttp.responseXML.getElementsByTagName('funding')[0].attributes.getNamedItem("totalDisbursement").value;
			totalExpenditureFund = xmlhttp.responseXML.getElementsByTagName('funding')[0].attributes.getNamedItem("totalExpenditure").value;
			
			
			var regionDataList = xmlhttp.responseXML.getElementsByTagName('region');
			fundingDataByRegion = new Array();
			var regIndex = 0;
			for (regIndex = 0; regIndex < regionDataList.length; regIndex ++) {
				var regData = regionDataList[regIndex];
				var regionDataMap = new Array();
				regionDataMap[0] = regData.attributes.getNamedItem("reg-code").value;
				regionDataMap[1] = regData.attributes.getNamedItem("fundingCommitment").value;
				regionDataMap[2] = regData.attributes.getNamedItem("fundingDisbursement").value;
				regionDataMap[3] = regData.attributes.getNamedItem("fundingExpenditure").value;

				fundingDataByRegion[fundingDataByRegion.length] = regionDataMap;
			}
			initIndicatorValues();
			window.setTimeout(getSectorIndicators, 100);
		}
		
	}

	function getSectorIndicators(sec) {
			var mapLevel = document.getElementById("mapLevelCombo").value;
			var indYear = document.getElementById("indicatorYearCombo").value;
			var uniqueStr = (new Date()).getTime();
			xmlhttp.open("GET", "../../gis/getFoundingDetails.do?action=getIndicatorNamesXML&mapCode=TZA&mapLevel=" + mapLevel + "&indYear=" + indYear + "&sectorId=" + selSector + "&uniqueStr=" + uniqueStr, true);
			xmlhttp.onreadystatechange = SectorIndicatorsReady;
			xmlhttp.send(null);
	}

	function SectorIndicatorsReady () {
		if (xmlhttp.readyState == 4) {
			var indicators = xmlhttp.responseXML.getElementsByTagName('indicator');
			var indIndex = 0;
			
			var selectCmb = document.getElementById("indicatorsCombo");
			
			selectCmb.innerHTML = null;
			
			var noneOpt = document.createElement("OPTION");
			noneOpt.value="-1";
			noneOpt.text="none";
			selectCmb.options.add(noneOpt);
			
			
			for (indIndex = 0; indIndex < indicators.length; indIndex ++) {
				var indicatorData = indicators[indIndex];

				var opt = document.createElement("OPTION");
				opt.value=indicatorData.attributes.getNamedItem("id").value;
				opt.text=indicatorData.attributes.getNamedItem("name").value;
				selectCmb.options.add(opt);
				
			}
			initIndicatorValues();
			setBusy(false);
		}
	}
	
	function getIndicatorsValues() {
			var mapLevel = document.getElementById("mapLevelCombo").value;
			var indYear = document.getElementById("indicatorYearCombo").value;
			var uniqueStr = (new Date()).getTime();
			xmlhttp.open("GET", "../../gis/getFoundingDetails.do?action=getIndicatorValues&mapCode=TZA&mapLevel=" + mapLevel + "&indYear=" + indYear + "&uniqueStr=" + uniqueStr, true);
			xmlhttp.onreadystatechange = indicatorsValuesReady;
			xmlhttp.send(null);
	}
	
	function indicatorsValuesReady () {
		if (xmlhttp.readyState == 4) {
			selIndicatorUnit  = xmlhttp.responseXML.getElementsByTagName('indicatorData')[0].attributes.getNamedItem("indUnit").value;
			
			if (selIndicatorUnit == null) {
				selIndicatorUnit = "N/A";
			}
			
			var regionIndicatorList = xmlhttp.responseXML.getElementsByTagName('indVal');
			indicatorDataByRegion = new Array();
			var regIndex = 0;
			
			
			
			for (regIndex = 0; regIndex < regionIndicatorList.length; regIndex ++) {
				var indData = regionIndicatorList[regIndex];
				var regionIndicatorMap = new Array();
				regionIndicatorMap[0] = indData.attributes.getNamedItem("reg").value;
				regionIndicatorMap[1] = indData.attributes.getNamedItem("val").value;

				indicatorDataByRegion[indicatorDataByRegion.length] = regionIndicatorMap;
			}
			setBusy(false);
		}
	}
	
	function initIndicatorValues() {
		indicatorDataByRegion = new Array();
		selIndicatorUnit = "N/A";
		
	}
	
	function checkIndicatorValues() {
		if (getIndValuesAction) {
			getIndValuesAction = false;
			getIndicatorsValues();
		}
	}
	
	function generateImageMap (XmlObj) {
		var retVal = "<map name=\"areaMap\">";
		var segments = XmlObj.getElementsByTagName('segment');
		var segmentIndex = 0;
		for (segmentIndex = 0; segmentIndex < segments.length; segmentIndex ++) {
			var segment = segments[segmentIndex];
			var shapeIndex = 0;
			for (shapeIndex = 0; shapeIndex < segment.childNodes.length; shapeIndex ++) {
				var shape = segment.childNodes[shapeIndex];
				retVal += "<area shape=\"polygon\" coords=\""
				var pointIndex = 0;
				for (pointIndex = 0; pointIndex < shape.childNodes.length; pointIndex ++) {
					var point = shape.childNodes[pointIndex];
					retVal += point.attributes.getNamedItem("x").value;
					retVal += ",";
					retVal += point.attributes.getNamedItem("y").value;
					if (pointIndex < shape.childNodes.length - 1) {
						retVal += ",";
					}
				}
				retVal += "\"";
				retVal += " onMouseOut=\"hideRegionTooltip()\"";
				retVal += " onMouseOver=\"showRegionTooltip('" + segment.attributes.getNamedItem("code").value + "')\">";
			}
		}
		retVal += "</map>";
		return retVal;
	}
	
	function showRegionTooltip(regCode) {
		if (regCode.indexOf("Lake")<0){
			var mouseEvent = null;
			document.getElementById("tooltipRegionContainer").innerHTML = regCode;
		
			document.getElementById("tooltipTotalCommitmentContainer").innerHTML = totalCommitmentFund + " k";
			document.getElementById("tooltipTotalDisbursementContainer").innerHTML = totalDisbursementFund + " k";
			document.getElementById("tooltipTotalExpenditureContainer").innerHTML = totalExpenditureFund + " k";
		
			var regData = getRegFounding(regCode);
		
			document.getElementById("tooltipCurrentCommitmentContainer").innerHTML = regData[0];
			document.getElementById("tooltipCurrentDisbursementContainer").innerHTML = regData[1];
			document.getElementById("tooltipCurrentExpenditureContainer").innerHTML = regData[2];
			
			
			document.getElementById("tooltipIndUnit").innerHTML = selIndicatorUnit;
			document.getElementById("tooltipIndVal").innerHTML = getRegIndicatorValue(regCode);
		
			
		
			document.getElementById("tooltipContainer").style.display = "block";
		}
	}
	
	function hideRegionTooltip() {
		document.getElementById("tooltipContainer").style.display = "none";
	}
	
	function getRegFounding (regCode) {
		var retVal = new Array (0, 0, 0);
		var dataIndex = 0;
		for (dataIndex = 0; dataIndex < fundingDataByRegion.length; dataIndex ++) {
			var dataItem = fundingDataByRegion[dataIndex];
			if (dataItem[0] == regCode) {
				retVal = new Array (dataItem[1], dataItem[2], dataItem[3]);
				break;
			}
		}
		return retVal;
	}
	
	function getRegIndicatorValue (regCode) {
		var retVal = "N/A";
		var dataIndex = 0;
		for (dataIndex = 0; dataIndex < indicatorDataByRegion.length; dataIndex ++) {
			var dataItem = indicatorDataByRegion[dataIndex];
			if (dataItem[0] == regCode) {
				retVal = dataItem[1];
				break;
			}
		}
		return retVal;
	}
	
	
		
	//Map level functions
	
	function mapLevelChanged(newVal){
		
		var mapObj = document.getElementById("testMap");
		var mapSrc = mapObj.src;
		var newUrl = modifyMapLevelURL (mapSrc, newVal);
		
		setBusy(true);
		mapObj.src = newUrl;
		
		
		document.getElementById("navCursorMap").src = modifyMapLevelURL (document.getElementById("navCursorMap").src, newVal);
		
	}
	
	function modifyMapLevelURL (url, newLevel) {
		var retVal = null;
		
		var levelStartPos = url.indexOf ("mapLevel");
		var levelEndtPos = url.indexOf ("&", levelStartPos);
		if (levelEndtPos == -1) {
			levelEndtPos = url.length;
		}
			retVal = url.substring(0, levelStartPos) +
			"mapLevel=" + newLevel + url.substring(levelEndtPos, url.length);
		
		return retVal;
	}
	//end of Map level functions
	
	//Year functions
	function yearSelected(year) {
		setBusy(true);
		var mapLevel = document.getElementById("mapLevelCombo").value;
		var ind = document.getElementById("indicatorsCombo").value;
		var uniqueStr = (new Date()).getTime();
		document.getElementById("testMap").src = "../../gis/getFoundingDetails.do?action=getDataForIndicator&mapCode=TZA&mapLevel=" + mapLevel + "&indYear=" + year.value + "&sectorId=" + selSector + "&indicatorId=" + ind + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight;
		getIndValuesAction = true;

	
	
		/*
		var mapObj = document.getElementById("testMap");
		var mapSrc = mapObj.src;
		var newUrl = modifyYearURL (mapSrc, year.value);
		setBusy(true);
		mapObj.src = newUrl;
		*/
	}
	
	function modifyYearURL (url, newYear) {
		var retVal = null;
		
		var yearStartPos = url.indexOf ("indYear");
		var yearEndtPos = url.indexOf ("&", yearStartPos);
		if (yearEndtPos == -1) {
			yearEndtPos = url.length;
		}
			retVal = url.substring(0, yearStartPos) +
			"indYear=" + newYear + url.substring(yearEndtPos, url.length);
		
		return retVal;
	}
	
	//end fo Year functions
	
	
	function zoomMap (pressedObj, zoomRation) {
		if (pressedObj != currentZoomObj) {
			currentZoomObj.className = "navHiden";
			pressedObj.className = "navVisible";
			currentZoomObj = pressedObj;
			
			reloadZoomedMap (zoomRation);
		}
	}
	
	function reloadZoomedMap (zoomRation) {
		var mapObj = document.getElementById("testMap");
		var mapSrc = mapObj.src;
		
		if (zoomRation == 1) {
			canvasWidth = 700;
			canvasHeight = 700;
		} else if (zoomRation == 1.5) {
			canvasWidth = 1050;
			canvasHeight = 1050;
		} else if (zoomRation == 2) {
			canvasWidth = 1400;
			canvasHeight = 1400;
		}
		
		var newUrl = modifyZoomedMapURL (mapSrc, canvasWidth, canvasHeight);
		
		
		setBusy(true);
		
		initNavCursorSize();
		imageMapLoaded = false;
		mapObj.src = newUrl;
		getImageMap();
		
	}
	
	function modifyZoomedMapURL (url, newWidth, newHeight) {
		var retVal = null;
		
		var widthStartPos = url.indexOf ("width");
		var widthEndtPos = url.indexOf ("&", widthStartPos);
		if (widthEndtPos == -1) {
			widthEndtPos = url.length;
		}
		
		var heightStartPos = url.indexOf ("height");
		var heightEndPos = url.indexOf ("&", heightStartPos);
		if (heightEndPos == -1) {
			heightEndPos = url.length;
		}

		if (widthEndtPos < heightStartPos) { 
			retVal = url.substring(0, widthStartPos) +
			"width=" + newWidth + url.substring(widthEndtPos, heightStartPos) + 
			"height=" + newHeight;
		}
		
		return retVal;
	}
	
	
	function setBusy(busy) {
		if (busy) {
			document.getElementById("busyIndicator").style.visibility = "visible";
		} else {
			document.getElementById("busyIndicator").style.visibility = "hidden";
		}
	}
	
	function mapScroll(obj) {
		
	}

	var navAreaLeft=0;
	var navAreaTop=0;

	var isKeyPressed = false;
	var initialOffsetX, initialOffsetY;

	function navCursorKeyDown (evt) {
		if (evt == null) {
			evt = window.event;
		}
		isKeyPressed = true;
		if (evt.offsetX != null) {
			initialOffsetX = evt.offsetX;
			initialOffsetY = evt.offsetY;
		} else {
			initialOffsetX = evt.layerX + 10;
			initialOffsetY = evt.layerY + 10;
		}
	}
	
	function navCursorKeyUp (evt) {
		isKeyPressed = false;
	}
	
	
	
	function navCursorOnMove (evt) {
			if (evt == null) {
				evt = window.event;
			} else {
				evt.stopPropagation();
			}
			
			if (isKeyPressed) {
				
				var pLeft = 0;
				var pTop = 0;
				
				if (evt.x != null) {
					pLeft = evt.x - initialOffsetX; 
					pTop = evt.y - initialOffsetY; 
				} else {
					pLeft = evt.pageX - initialOffsetX;
					pTop = evt.pageY - initialOffsetY - 100;
					evt.stopPropagation();
				}

				if (pLeft >= 23 && pLeft -23 + navCursorWidth <= navigationWidth - 14) {
					navAreaLeft = pLeft - 23;
				} else if (pLeft < 23) {
					navAreaLeft = 0;
				} else {
					navAreaLeft = navigationWidth - navCursorWidth - 14;
				}
				
				if (pTop >= 43 && pTop - 43 + navCursorHeight <= navigationHeight - 14) {
					navAreaTop = pTop - 43;
				} else if (pTop < 43) {
					navAreaTop = 0;
				} else {
					navAreaTop = navigationHeight - navCursorHeight - 14;
				}
				
				

				document.getElementById("navCursor").style.left = navAreaLeft + 23 + "px";
				document.getElementById("navCursor").style.top = navAreaTop + 43 + "px";
				
				
				scrollMap (navAreaLeft, navAreaTop);
				
			}
	}
	
	function initNavCursorEvents() {
		var navCursorObj = document.getElementById("navCursor");
		navCursorObj.onmousedown = navCursorKeyDown;
		navCursorObj.onmouseup = navCursorKeyUp;
		navCursorObj.onmousemove = navCursorOnMove;
		navCursorObj.onmouseout = navCursorKeyUp;
	}

	function initNavCursorSize() {
		navCursorWidth = Math.round(navigationWidth * canvasContainerWidth / canvasWidth) - 14;
		navCursorHeight = Math.round(navigationHeight * canvasContainerHeight / canvasHeight) - 14;
		document.getElementById("navCursor").style.width = navCursorWidth + "px";
		document.getElementById("navCursor").style.height = navCursorHeight + "px";
		
		navAreaLeft=0;
		navAreaTop=0;
		scrollMap(0, 0);
		document.getElementById("navCursor").style.left = 23 + "px";
		document.getElementById("navCursor").style.top = 43 + "px";
	}
	
	function scrollMap(x, y){
		
		var deltaCanvasWidth = canvasWidth - canvasContainerWidth;
		var deltaCursorWidth = navigationWidth - 14 - navCursorWidth;
		
		var deltaCanvasHeight = canvasHeight - canvasContainerHeight;
		var deltaCursorHeight = navigationHeight - 14 - navCursorHeight;
		
		
		document.getElementById("mapCanvasContainer").scrollLeft = x * deltaCanvasWidth / deltaCursorWidth;
		document.getElementById("mapCanvasContainer").scrollTop = y * deltaCanvasHeight / deltaCursorHeight;
	}
	
	initNavCursorSize();
	initNavCursorEvents();
	
	function showNavigation(obj){
		if (obj.className=="navHiden") {
			document.getElementById("ctrlContainer").style.display="block";
			obj.className = "navVisible";
		} else {
			document.getElementById("ctrlContainer").style.display="none";
			obj.className="navHiden";
		}
	}
	
</script>



