<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:instance property="gisDashboardForm"/>

<table>
	<tr>
		<td>
			<img onLoad="getImageMap()" useMap="#areaMap" id="testMap" border="0" src="/gis/getFoundingDetails.do?action=paintMap&mapCode=TZA&segmentData=Tanga/Muheza%20DC/38|Tanga/Korogwe%20DC/15|Tanga/Lushoto%20DC/30|Tanga/Handeni%20DC/5|Tanga/Pangani%20DC/12">
		</td>
	</tr>
	<tr>
		<td>
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

		</td>
	</tr>
	<tr><td><img style="visibility:hidden" id="busyIndicator" src="/TEMPLATE/ampTemplate/images/amploading.gif"></td></tr>
	<tr>
		<td>
		<select onChange="sectorSelected(this)">
			<option value="-1">All</option>
			<logic:iterate name="gisDashboardForm" property="sectorCollection" id="sec">
				<option value="<bean:write name="sec" property="sector.ampSectorId"/>"><bean:write name="sec" property="sector.name"/> (<bean:write name="sec" property="count"/>)</option>
			</logic:iterate>
		</select>
		<div id="imageMapContainer" style="visibility:hidden;"></div>
		</td>
	</tr>
	<tr>
		<td>
		<select id="indicatorsCombo" onchange="indicatorSelected(this)">
			<option value=-1>None</option>
		</select>
		</td>
	</tr>
</table>

<div id="tooltipContainer" style="display:none; width:200; position: absolute; left:50px; top: 50px; background-color: #d9ceba; border: 1px solid silver;">
	<div style="border-top: 1px solid white; border-left: 1px solid white; border-bottom: 1px solid Black; border-right: 1px solid Black;">
	
	<table border="1" bordercolor="#c3b7a1" cellpadding="3" cellspacing="2" width="100%" style="border-collapse:collapse">
		<tr>
			<td nowrap width="50%">Region</td>
			<td width="50%" id="tooltipRegionContainer">&nbsp;</td>
		</tr>
		<tr>
			<td nowrap width="50%">Total funding</td>
			<td width="50%" id="tooltipTotalContainer">&nbsp;</td>
		</tr>
		<tr>
			<td nowrap width="50%">For this region</td>
			<td width="50%" id="tooltipCurrentContainer">&nbsp;</td>
		</tr>
	</table>
	</div>
</div>



<script language="JavaScript">

	document.onmousemove = mouseMoveTranslatorIE;
	
	function mouseMoveTranslatorIE(e) {
			if (e == null) {
				e = window.event;
			}
			document.getElementById("tooltipContainer").style.left = e.clientX  + 2 + "px";
			document.getElementById("tooltipContainer").style.top = e.clientY + document.body.scrollTop + 2 + "px";
	}
	
	
	var mouseX, mouseY;
	var evt;
	


	var xmlhttp =  new XMLHttpRequest();
	var imageMapLoaded = false;
	
	var fundingDataByRegion = new Array();
	var totalFund = "0";
	var selSector = 0;
	
	
	function sectorSelected(sec) {
		selSector = sec.value;
		setBusy(true);
		var uniqueStr = (new Date()).getTime();
		document.getElementById("testMap").src = "../../gis/getFoundingDetails.do?action=getDataForIndicator&mapCode=TZA&sectorId=" + selSector + "&indicatorId=-1" + "&uniqueStr=" + uniqueStr;
		getDataForSector(sec);
	}
	
	function indicatorSelected(ind) {
		setBusy(true);
		var uniqueStr = (new Date()).getTime();
		document.getElementById("testMap").src = "../../gis/getFoundingDetails.do?action=getDataForIndicator&mapCode=TZA&sectorId=" + selSector + "&indicatorId=" + ind.value + "&uniqueStr=" + uniqueStr;
	}

	
	function getImageMap() {
		if (!imageMapLoaded) {
			var uniqueStr = (new Date()).getTime();
			xmlhttp.open("GET", "../../gis/getFoundingDetails.do?action=getImageMap&mapCode=TZA&uniqueStr=" + uniqueStr, true);
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
		}
	}
	
	function getDataForSector(sec) {
			selSector = sec.value;
			var uniqueStr = (new Date()).getTime();
			xmlhttp.open("GET", "../../gis/getFoundingDetails.do?action=getSectorDataXML&mapCode=TZA&sectorId=" + sec.value + "&uniqueStr=" + uniqueStr, true);
			xmlhttp.onreadystatechange = dataForSectorReady;
			xmlhttp.send(null);
	}
	
	function dataForSectorReady () {
		if (xmlhttp.readyState == 4) {
			totalFund = xmlhttp.responseXML.getElementsByTagName('funding')[0].attributes[0].value;
			var regionDataList = xmlhttp.responseXML.getElementsByTagName('region');
			fundingDataByRegion = new Array();
			var regIndex = 0;
			for (regIndex = 0; regIndex < regionDataList.length; regIndex ++) {
				var regData = regionDataList[regIndex];
				var regionDataMap = new Array();
				regionDataMap[0] = regData.attributes.getNamedItem("reg-code").value;
				regionDataMap[1] = regData.attributes.getNamedItem("funding").value;
				fundingDataByRegion[fundingDataByRegion.length] = regionDataMap;
			}
			
			window.setTimeout(getSectorIndicators, 100);
		}
		
	}

	function getSectorIndicators(sec) {
			var uniqueStr = (new Date()).getTime();
			xmlhttp.open("GET", "../../gis/getFoundingDetails.do?action=getIndicatorNamesXML&mapCode=TZA&sectorId=" + selSector + "&uniqueStr=" + uniqueStr, true);
			xmlhttp.onreadystatechange = SectorIndicatorsReady;
			xmlhttp.send(null);
	}

	function SectorIndicatorsReady () {
		if (xmlhttp.readyState == 4) {
			var indicators = xmlhttp.responseXML.getElementsByTagName('indicator');
			var indIndex = 0;
			
			var selectCmb = document.getElementById("indicatorsCombo");
			
			
			var innerHtmlText = "<option value=\"-1\">None</option>";
			/*			
			var innerHtmlText = ""
			if (indicators.length == 0) {
				innerHtmlText = "<option value=\"-1\">None</option>";
			}
			*/
			
			for (indIndex = 0; indIndex < indicators.length; indIndex ++) {
				var indicatorData = indicators[indIndex];
				innerHtmlText += "<option value=\"" + indicatorData.attributes.getNamedItem("id").value + "\">" + indicatorData.attributes.getNamedItem("name").value + "</option>";
				
				//alert (indicatorData.attributes.getNamedItem("name").value + " - " + indicatorData.attributes.getNamedItem("id").value)
			}
			
			selectCmb.innerHTML = innerHtmlText;
			setBusy(false);
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
		var mouseEvent = null;
		document.getElementById("tooltipRegionContainer").innerHTML = regCode;
		document.getElementById("tooltipTotalContainer").innerHTML = totalFund + " k";
		document.getElementById("tooltipCurrentContainer").innerHTML = getRegFounding (regCode);
		document.getElementById("tooltipContainer").style.display = "block";
		
	}
	
	function hideRegionTooltip() {
		document.getElementById("tooltipContainer").style.display = "none";
	}
	
	function getRegFounding (regCode) {
		var retVal = "0";
		var dataIndex = 0;
		for (dataIndex = 0; dataIndex < fundingDataByRegion.length; dataIndex ++) {
			var dataItem = fundingDataByRegion[dataIndex];
			if (dataItem[0] == regCode) {
				retVal = dataItem[1];
				break;
			}
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
	
</script>



