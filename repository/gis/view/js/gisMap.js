	
	
	var canvasWidth = 500;
	var canvasHeight = 500;
	
	var canvasContainerWidth = 500;
	var canvasContainerHeight = 500;
	
	var navigationWidth = 300;
	var navigationHeight = 300;
	
	
	
	var navCursorWidth = 1;
	var navCursorHeight = 1;
	
	var currentZoomRatio = 1;
	var currentZoomObj = document.getElementById("mapZoom10");
	
	var mouseX, mouseY;
	var evt;
	var xmlhttp = null;
	
	
	var imageMapLoaded = false;
	
	var fundingDataByRegion = new Array();
	var indicatorDataByRegion = new Array();
	var selIndicatorUnit = "N/A";

	var totalCommitmentFund = "0";
	var totalDisbursementFund = "0";
	var totalExpenditureFund = "0";

	var selSector = 0;
	
	var getIndValuesAction = false;
	
	
	
	//Action progress flag
	var actionImgLoading = false;
	var actionGetImageMap = false;
	var actionSectorData = false;
	var actionGetIndicators = false;
	var actionGetIndicatorValues = false;
	var actionGetSubgroups = false;
	var actionGetYears = false;
	
	
	
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
	
	
	

	
	function ajaxInit() {
		xmlhttp = new XMLHttpRequest();
	
		if (xmlhttp == null) {
			xmlhttp = ActiveXObject("Microsoft.XMLHTTP")
		}
	}

	
	
	
	function sectorSelected(sec) {
		var selSector = sec;
		initIndicatorCombo();
		initSubgroupCombo();
		initYearCombo();
		
		setBusy(true);
		var mapLevel = getRadioValue("mapLevelRadio");
		
		var fromYear = document.getElementsByName('selectedFromYear')[0].value;
		var toYear = document.getElementsByName('selectedToYear')[0].value;
		
		var indYear = document.getElementById("indicatorYearCombo").value;
		var uniqueStr = (new Date()).getTime();
		
		actionImgLoading = true;
		document.getElementById("testMap").src = "../../gis/getFoundingDetails.do?action=getDataForIndicator&mapCode=TZA&mapLevel=" + mapLevel + "&fromYear=" + fromYear + "&toYear=" + toYear + "&indYear=" + indYear + "&sectorId=" + sec + "&indicatorId=-1" + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight;
		
		if (sec > 0) {
			getDataForSector(sec);
		} else {
			fundingDataByRegion = new Array();
		}
	}
	
	function indicatorSelected(ind) {
	
		initSubgroupCombo();
		initYearCombo();
	
		selIndicator = ind.value;
		var mapLevel = getRadioValue("mapLevelRadio");
		var sec = document.getElementById("sectorsMapCombo").value;
		var fromYear = document.getElementsByName('selectedFromYear')[0].value;
		var toYear = document.getElementsByName('selectedToYear')[0].value;
		
		var uniqueStr = (new Date()).getTime();
		xmlhttp.open("POST", "../../gis/getFoundingDetails.do?action=getAvailIndicatorSubgroups&mapCode=TZA&mapLevel=" + mapLevel + "&fromYear=" + fromYear + "&toYear=" + toYear + "&sectorId=" + sec + "&indicatorId=" + selIndicator + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight, true);
		xmlhttp.onreadystatechange = availSubgroupsReady
		actionGetSubgroups = true;
		xmlhttp.send(null);
		
		actionImgLoading = true;
		document.getElementById("testMap").src = "../../gis/getFoundingDetails.do?action=getDataForIndicator&mapCode=TZA&mapLevel=" + mapLevel + "&fromYear=" + fromYear + "&toYear=" + toYear + "&sectorId=" + sec + "&indicatorId=-1" + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight;
		initIndicatorValues();
		
		setBusy(true);
	}
	
	function availSubgroupsReady () {
		if (xmlhttp.readyState == 4) {
		
			actionGetSubgroups = false;
			var availSubgroupList = xmlhttp.responseXML.getElementsByTagName('subgroup');
			var subgroupIndex = 0;
			
			var selectCmb = document.getElementById("indicatorSubgroupCombo");
			selectCmb.innerHTML = null;
			var noneOpt = document.createElement("OPTION");
			noneOpt.value="-1";
			noneOpt.text="Select subgroup";
			selectCmb.options.add(noneOpt);			
			
			
			for (subgroupIndex = 0; subgroupIndex < availSubgroupList.length; subgroupIndex ++) {
				var subgroupData = availSubgroupList[subgroupIndex];
				var opt = document.createElement("OPTION");
				opt.value=subgroupData.attributes.getNamedItem("id").value;
				opt.text=subgroupData.attributes.getNamedItem("name").value;
				selectCmb.options.add(opt);				
			}
			setBusy(false);
		}
		
	}
	
	function subgroupSelected(sbgr) {
	
		initYearCombo();
	
		selSubgroup = sbgr.value;

		var mapLevel = getRadioValue("mapLevelRadio");
		var sec = document.getElementById("sectorsMapCombo").value;
		var selIndicator = document.getElementById("indicatorsCombo").value;
		var fromYear = document.getElementsByName('selectedFromYear')[0].value;
		var toYear = document.getElementsByName('selectedToYear')[0].value;

		var uniqueStr = (new Date()).getTime();

		xmlhttp.open("POST", "../../gis/getFoundingDetails.do?action=getAvailIndicatorYears&mapCode=TZA&mapLevel=" + mapLevel + "&fromYear=" + fromYear + "&toYear=" + toYear + "&subgroupId=" + selSubgroup + "&sectorId=" + sec + "&indicatorId=" + selIndicator + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight, true);
		xmlhttp.onreadystatechange = availYearsReady;
		actionGetYears = true;
		xmlhttp.send(null);
		
		
		document.getElementById("testMap").src = "../../gis/getFoundingDetails.do?action=getDataForIndicator&mapCode=TZA&mapLevel=" + mapLevel + "&fromYear=" + fromYear + "&toYear=" + toYear + "&sectorId=" + sec + "&indicatorId=-1" + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight;
		initIndicatorValues();
		
		setBusy(true);
	}
	
	function availYearsReady () {
		if (xmlhttp.readyState == 4) {
			actionGetYears = false;
			var availYearList = xmlhttp.responseXML.getElementsByTagName('interval');
			var yearIndex = 0;
			
			var selectCmb = document.getElementById("indicatorYearCombo");
			selectCmb.innerHTML = null;
			var noneOpt = document.createElement("OPTION");
			noneOpt.value="-1";
			noneOpt.text="Select year";
			selectCmb.options.add(noneOpt);			
			
			
			for (yearIndex = 0; yearIndex < availYearList.length; yearIndex ++) {
				var yearData = availYearList[yearIndex];
				var opt = document.createElement("OPTION");
				opt.value = yearData.attributes.getNamedItem("start-value").value + "-" + yearData.attributes.getNamedItem("end-value").value;
				opt.text = yearData.attributes.getNamedItem("start-caption").value + " - " + yearData.attributes.getNamedItem("end-caption").value;
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
			actionGetImageMap = true;
			var mapLevel = getRadioValue("mapLevelRadio");
			var indYear = document.getElementById("indicatorYearCombo").value;
			var uniqueStr = (new Date()).getTime();
			xmlhttp.open("POST", "../../gis/getFoundingDetails.do?action=getImageMap&mapCode=TZA&mapLevel=" + mapLevel + "&indYear=" + indYear + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight, true);
			xmlhttp.onreadystatechange = addImageMap;
			xmlhttp.send(null);
		} else {
			setBusy(false);
		}
	}
	
	function addImageMap() {
		if (xmlhttp.readyState == 4) {
			actionGetImageMap = false;
			imageMapLoaded = true;
			document.getElementById("imageMapContainer").innerHTML = null;
			document.getElementById("imageMapContainer").innerHTML = generateImageMap (xmlhttp.responseXML);
			
			document.getElementById("testMap").removeAttribute("useMap");
			document.getElementById("testMap").setAttribute("useMap", "#areaMap");
			setBusy(false);
		}
	}
	
	function getDataForSector(sec) {
			var mapLevel = getRadioValue("mapLevelRadio");
			var indYear = document.getElementById("indicatorYearCombo").value;
			var fromYear = document.getElementsByName('selectedFromYear')[0].value;
			var toYear = document.getElementsByName('selectedToYear')[0].value;
			var uniqueStr = (new Date()).getTime();
			xmlhttp.open("POST", "../../gis/getFoundingDetails.do?action=getSectorDataXML&mapCode=TZA&mapLevel=" + mapLevel + "&fromYear=" + fromYear + "&toYear=" + toYear + "&indYear=" + indYear + "&sectorId=" + sec + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight, true);
			xmlhttp.onreadystatechange = dataForSectorReady;
			actionSectorData = true;
			xmlhttp.send(null);
	}
	
	function dataForSectorReady () {
		if (xmlhttp.readyState == 4) {
			actionSectorData = false;
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
			actionGetIndicators = true;
			window.setTimeout(getSectorIndicators, 100);
		}
		
	}

	function getSectorIndicators(sec) {
			var mapLevel = getRadioValue("mapLevelRadio");
			var indYear = document.getElementById("indicatorYearCombo").value;
			var selSector = document.getElementById("sectorsMapCombo").value;
			var uniqueStr = (new Date()).getTime();
			xmlhttp.open("POST", "../../gis/getFoundingDetails.do?action=getIndicatorNamesXML&mapCode=TZA&mapLevel=" + mapLevel + "&indYear=" + indYear + "&sectorId=" + selSector + "&uniqueStr=" + uniqueStr, true);
			xmlhttp.onreadystatechange = SectorIndicatorsReady;
			actionGetIndicators = true;
			xmlhttp.send(null);
	}

	function SectorIndicatorsReady () {
		if (xmlhttp.readyState == 4) {
			actionGetIndicators = false;
			var indicators = xmlhttp.responseXML.getElementsByTagName('indicator');
			var indIndex = 0;
			var selectCmb = document.getElementById("indicatorsCombo");
			selectCmb.innerHTML = null;
			var noneOpt = document.createElement("OPTION");
			noneOpt.value="-1";
			noneOpt.text="Select indicator";
			selectCmb.options.add(noneOpt);
			
			for (indIndex = 0; indIndex < indicators.length; indIndex ++) {
				var indicatorData = indicators[indIndex];
				var opt = document.createElement("OPTION");
				opt.value=indicatorData.attributes.getNamedItem("id").value;
				opt.text=indicatorData.attributes.getNamedItem("name").value;
				selectCmb.options.add(opt);
				if (indicatorData.attributes.getNamedItem("enbl").value=="false") {
					opt.className="dsbl";
				} else {
					opt.className="enbl";
				}
			}
			initIndicatorValues();
			setBusy(false);
		}
	}
	
	function getIndicatorsValues() {
			var mapLevel = getRadioValue("mapLevelRadio");
			var indYear = document.getElementById("indicatorYearCombo").value;
			var uniqueStr = (new Date()).getTime();
			xmlhttp.open("POST", "../../gis/getFoundingDetails.do?action=getIndicatorValues&mapCode=TZA&mapLevel=" + mapLevel + "&indYear=" + indYear + "&uniqueStr=" + uniqueStr, true);
			xmlhttp.onreadystatechange = indicatorsValuesReady;
			actionGetIndicatorValues = true;
			xmlhttp.send(null);
	}
	
	function indicatorsValuesReady () {
		if (xmlhttp.readyState == 4) {
		
			actionGetIndicatorValues = false;
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
				retVal += " onMouseOver=\"showRegionTooltip('" + segment.attributes.getNamedItem("code").value + "','" + segment.attributes.getNamedItem("name").value + "')\">";
			}
		}
		retVal += "</map>";
		return retVal;
	}
	
	function showRegionTooltip(regCode, regName) {
		if (regCode.indexOf("Lake")<0){
			var mouseEvent = null;
			document.getElementById("tooltipRegionContainer").innerHTML = regName;
		
			document.getElementById("tooltipTotalCommitmentContainer").innerHTML = totalCommitmentFund;
			document.getElementById("tooltipTotalDisbursementContainer").innerHTML = totalDisbursementFund;
			document.getElementById("tooltipTotalExpenditureContainer").innerHTML = totalExpenditureFund;
		
			var regData = getRegFounding(regCode);
		
			document.getElementById("tooltipCurrentCommitmentContainer").innerHTML = regData[0];
			document.getElementById("tooltipCurrentDisbursementContainer").innerHTML = regData[1];
			document.getElementById("tooltipCurrentExpenditureContainer").innerHTML = regData[2];
			
			
			document.getElementById("tooltipIndUnit").innerHTML = selIndicatorUnit;
			document.getElementById("tooltipIndVal").innerHTML = getRegIndicatorValue(regCode);
		
			
		
			document.getElementById("tooltipContainer").style.display = "block";
			
			//Set year caption
			var fromYear = getComboSelectedText(document.getElementsByName('selectedFromYear')[0]);
			var toYear = getComboSelectedText(document.getElementsByName('selectedToYear')[0]);
			var newCapt = "(USD) " + fromYear + " - " + toYear;
			document.getElementById('tooltipCurencyYearRange').innerHTML = newCapt;
			
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
	
	function initSubgroupCombo(){
		var cmb = document.getElementById("indicatorSubgroupCombo");
		cmb.innerHTML = null;
		var noneOpt = document.createElement("OPTION");
		noneOpt.value="-1";
		noneOpt.text="Select subgroup";
		cmb.options.add(noneOpt);
	}
	
	function initYearCombo(){
		var cmb = document.getElementById("indicatorYearCombo");
		cmb.innerHTML = null;
		var noneOpt = document.createElement("OPTION");
		noneOpt.value="-1";
		noneOpt.text="Select year";
		cmb.options.add(noneOpt);
	}
	
	function initIndicatorCombo(){
		var cmb = document.getElementById("indicatorsCombo");
		cmb.innerHTML = null;
		var noneOpt = document.createElement("OPTION");
		noneOpt.value="-1";
		noneOpt.text="Select indicator";
		cmb.options.add(noneOpt);
	}
	
	function initSectorCombo(){
		var cmb = document.getElementById("sectorsMapCombo");
		cmb.selectedIndex=0;
	}
		
	//Map level functions
	
	function mapLevelChanged(newVal){
		initIndicatorCombo();
		initSubgroupCombo();
		initYearCombo();
//		initSectorCombo();
	
		var sec = document.getElementById("sectorsMapCombo").value;

/*
		var mapObj = document.getElementById("testMap");
		var mapSrc = mapObj.src;
		
		var fromYear = document.getElementsByName('selectedFromYear')[0].value;
		var toYear = document.getElementsByName('selectedToYear')[0].value;
		
		setBusy(true);
		imageMapLoaded = false;
		var uniqueStr = (new Date()).getTime();
		
		var newUrl = "../../gis/getFoundingDetails.do?action=getDataForIndicator&mapCode=TZA&mapLevel=" + newVal + "&fromYear=" + fromYear + "&toYear=" + toYear + "&sectorId=" + sec + "&indicatorId=-1" + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight;
		mapObj.src = newUrl;
		
		actionImgLoading = true;
		*/
		
		sectorSelected(sec);
		
		
		document.getElementById("navCursorMap").src = modifyMapLevelURL (document.getElementById("navCursorMap").src, newVal);
		
		if (newVal==2) {
			document.getElementById("reg_district_caption").innerHTML="Region";
			document.getElementById("reg_district_caption_for").innerHTML="For this region";
		} else if (newVal==3) {
			document.getElementById("reg_district_caption").innerHTML="District";
			document.getElementById("reg_district_caption_for").innerHTML="For this district";
		}
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
	
	function modifyUniqueStringURL (url) {
		var uniqueStartPos = url.indexOf ("uniqueStr");
		var uniqueEndtPos = url.indexOf ("&", uniqueStartPos);
		if (uniqueEndtPos == -1) {
			uniqueEndtPos = url.length;
		}
			retVal = url.substring(0, uniqueStartPos) +
			"uniqueStr=" + (new Date()).getTime() + url.substring(uniqueEndtPos, url.length);
		
		
		
		return retVal;
	}
	
	
	//end of Map level functions
	
	//Year functions
	function yearSelected(year) {
		setBusy(true);
		var mapLevel = getRadioValue("mapLevelRadio");
		var ind = document.getElementById("indicatorsCombo").value;
		var subgroupId = document.getElementById("indicatorSubgroupCombo").value;
		var fromYear = document.getElementsByName('selectedFromYear')[0].value;
		var toYear = document.getElementsByName('selectedToYear')[0].value;
		var sec = document.getElementById("sectorsMapCombo").value;
		
		var uniqueStr = (new Date()).getTime();
		
		actionImgLoading = true;
		document.getElementById("testMap").src = "../../gis/getFoundingDetails.do?action=getDataForIndicator&mapCode=TZA&mapLevel=" + mapLevel + "&fromYear=" + fromYear + "&toYear=" + toYear + "&subgroupId=" + subgroupId + "&indYear=" + year.value + "&sectorId=" + sec + "&indicatorId=" + ind + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight;
		getIndValuesAction = true;
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
	
	
	function mapYearChanged(){
		sectorSelected(document.getElementById('sectorsMapCombo'));
	}
	
	function getComboSelectedText(obj) {
		var retVal = null;
		var optIndex = 0;
		for (optIndex = 0; optIndex < obj.options.length; optIndex++) {
			if (obj.options[optIndex].selected) {
				retVal = obj.options[optIndex].text;
				break;
			}
		}
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
		
		/*
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
		*/
		
		if (zoomRation == 1) {
			canvasWidth = 500;
			canvasHeight = 500;
		} else if (zoomRation == 1.5) {
			canvasWidth = 750;
			canvasHeight = 750;
		} else if (zoomRation == 2) {
			canvasWidth = 1000;
			canvasHeight = 1000;
		} else if (zoomRation == 3) {
			canvasWidth = 1500;
			canvasHeight = 1500;
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
	/*
			alert (actionImgLoading + " - " +
				   actionGetImageMap  + " - " +
				   actionSectorData  + " - " +
				   actionGetIndicators + " - " +
				   actionGetIndicatorValues + " - " +
				   actionGetSubgroups+ " - " +
				   actionGetYears);*/

		if (busy) {
			document.getElementById("busyIndicator").style.visibility = "visible";
			
			document.getElementsByName("mapLevelRadio")[0].disabled = true;
			document.getElementsByName("mapLevelRadio")[1].disabled = true;
			document.getElementById("sectorsMapCombo").disabled = true;
			document.getElementById("indicatorsCombo").disabled = true;
			document.getElementById("indicatorSubgroupCombo").disabled = true;
			document.getElementById("indicatorYearCombo").disabled = true;
			
			
		} else if (!actionImgLoading &&
				   !actionGetImageMap &&
				   !actionSectorData &&
				   !actionGetIndicators &&
				   !actionGetIndicatorValues &&
				   !actionGetSubgroups &&
				   !actionGetYears) {
			document.getElementById("busyIndicator").style.visibility = "hidden";
			
			document.getElementsByName("mapLevelRadio")[0].disabled = false;
			document.getElementsByName("mapLevelRadio")[1].disabled = false;
			document.getElementById("sectorsMapCombo").disabled = false;
			document.getElementById("indicatorsCombo").disabled = false;
			document.getElementById("indicatorSubgroupCombo").disabled = false;
			document.getElementById("indicatorYearCombo").disabled = false;
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
		} else {
			evt.stopPropagation();
			evt.preventDefault();
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
			evt.preventDefault();
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
		navCursorObj.onclick = navCursorKeyUp;
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
	
	function getRadioValue(radioName) {
		var retVal = null;
		var radioGroup = document.getElementsByName(radioName);
		var iterIndex = 0;
		for (iterIndex = 0; iterIndex < radioGroup.length; iterIndex ++) {
			if (radioGroup[iterIndex].checked) {
				retVal = radioGroup[iterIndex].value;
				break;
			}
		}
		
		return retVal;
	}