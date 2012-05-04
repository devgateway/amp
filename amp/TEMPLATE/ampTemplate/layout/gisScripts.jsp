<script language="javascript">
window.onload = function(){
	resizeDivs();
}

  function exportPDF() {
  
  	var divs = document.getElementsByTagName("select");
	var matchingDivs = new Array();
	var currentDiv;
	
	var tableId = new Array();
	var columnId = new Array();
	var itemId = new Array();
	var columnquerystring="";

	// Iterate through all selects in the page
	for(var idx=0;idx < divs.length; idx++){
		if(divs[idx].id.search("tableWidgetDropDown") > -1){
			var tableId;
			var columnId;
			var itemId;
			tableId.push(divs[idx].getAttribute("idtable"));
			columnId.push(divs[idx].getAttribute("idcolumn"));
			itemId.push(divs[idx].value);
		}
	}
	var tableIdStr = "-1";
	var columnIdStr = "-1";
	var itemIdStr = "-1";
	
	for(var idx=0;idx < tableId.length; idx++)
	{
		if(tableIdStr == "-1"){
			tableIdStr = tableId[idx];
			columnIdStr = columnId[idx];
			itemIdStr = itemId[idx];
		}
		else
		{
			tableIdStr += "," + tableId[idx];
			columnIdStr += "," + columnId[idx];
			itemIdStr += "," + itemId[idx];
		}
	}
	columnquerystring += "&tableId="+tableIdStr+"&columnId="+columnIdStr+"&itemId="+itemIdStr;

	var selectedDonor = document.getElementsByName("selectedDonor")[0].value;
	var selectedFromYear = document.getElementsByName("selectedFromYear")[0].value;
	var selectedToYear = document.getElementsByName("selectedToYear")[0].value;
	var showLabels = document.getElementsByName("showLabels")[0].checked;
	var showLegends = document.getElementsByName("showLegends")[0].checked;

	//Get donor name also
	var selectDonors = document.getElementsByName("selectedDonor")[0];
	var selectDonorsStr = " ";
	for(var idx = 0; idx < selectDonors.length;idx++)
	{
		if(selectDonors.options[idx].value == selectDonors.value)
			selectDonorsStr = escape(selectDonors.options[idx].text);
	}


	var mapLevel = $("[name='mapLevelRadio']:checked").val();
	mapLevel = (mapLevel==null)?2:mapLevel;
	
	if (showDevinfo) {
			var sectorId =  document.getElementById("sectorsMapCombo").value;
			var indicatorId = document.getElementById("indicatorsCombo").value;	
			var subgroupId = document.getElementById("indicatorSubgroupCombo").value;
			var timeInterval = document.getElementById("indicatorYearCombo").value;
			var href="/gis/pdfExport.do?mapMode=DevInfo&selectedDonor=" + selectedDonor + "&mapLevel=" + mapLevel + "&selectedFromYear=" + selectedFromYear+ "&selectedToYear=" + selectedToYear + "&showLabels=" + showLabels + "&showLegends=" + showLegends + "&sectorId=" + sectorId + "&indicatorId=" + indicatorId + "&subgroupId=" + subgroupId + ""+ columnquerystring + "&selectedDonorName=" +selectDonorsStr + "&indYear=" + timeInterval;
					
			if (navigator.appName.indexOf('Microsoft Internet Explorer') > -1) { //Workaround to allow HTTP REFERER to be sent in IE (AMP-12638)
						var popupName = "popup" + new Date().getTime();
						var popupWindow = window.open(href, popupName,
								"height=780,width=500");
						var referLink = document.createElement('a');
						referLink.href = href;
						referLink.target = popupName;
						document.body.appendChild(referLink);
						referLink.click();
					} else {
						openURLinWindow(href, 780, 500);
			}
		} else {
			var mapModeFin = document.getElementById("mapModeFin") != null ? document
					.getElementById("mapModeFin").value
					: "fundingData";

			var popup = window
					.open(
							"about:blank",
							"regReportWnd",
							"height=500,width=780,status=yes,resizable=yes,toolbar=no,menubar=no,location=no");
			var filterForm = $("#gisFilterForm");
			filterForm.attr("method", "post");
			filterForm.attr("action",
					"/gis/pdfExport.do?mapMode=FinInfo&selectedDonor="
							+ selectedDonor + "&mapModeFin=" + mapModeFin
							+ "&selectedFromYear=" + selectedFromYear
							+ "&selectedToYear=" + selectedToYear
							+ "&showLabels=" + showLabels + "&showLegends="
							+ showLegends + "" + columnquerystring
							+ "&selectedDonorName=" + selectDonorsStr);
			filterForm.attr("target", "regReportWnd");

			var allSectors = false;
			if ($("input[name='selectedSectors']:checked").length
					+ $("input[name='selectedSecondarySectors']:checked").length
					+ $("input[name='selectedTertiarySectors']:checked").length == 0) {
				$("input[name='selectedSectors']").attr("checked", "true");
				$("input[name='selectedSecondarySectors']").attr("checked",
						"true");
				$("input[name='selectedTertiarySectors']").attr("checked",
						"true");
				allSectors = true;
			}

			$("#filterAllSectors").val(allSectors);

			filterForm[0].submit();

			if (allSectors) {
				$("input[name='selectedSectors']").removeAttr("checked");
				$("input[name='selectedSecondarySectors']").removeAttr(
						"checked");
				$("input[name='selectedTertiarySectors']")
						.removeAttr("checked");
			}

			popup.focus();

			//openURLinWindow("/gis/pdfExport.do?mapMode=FinInfo&selectedDonor=" + selectedDonor + "&mapModeFin=" + mapModeFin + "&selectedFromYear=" + selectedFromYear+ "&selectedToYear=" + selectedToYear + "&showLabels=" + showLabels + "&showLegends=" + showLegends + ""+ columnquerystring + "&selectedDonorName=" +selectDonorsStr, 780, 500);

		}
	}
	function resizeDivs() {
		var tables = document.getElementsByTagName("table");
		var matchingTables = new Array();
		var currentTable;
		for ( var idx = 0; idx < tables.length; idx++) {
			if (tables[idx].id.search("tableWidget") > -1) {
				//Resize the container
				table = tables[idx];
				tempTable = table;
				var counter = 0;

				while (tempTable.id != "content" && counter < 10) {
					if (tempTable.id != "content")
						tempTable = tempTable.parentNode;
					counter++;
				}
				if (tempTable.id == "content") {
					if (tempTable.style.width == "1000px"
							|| tempTable.alreadyResized) {
						if (tempTable.alreadyResized == true) {
							if ((table.offsetWidth + 20) > tempTable.style.width)
								tempTable.style.width = (table.offsetWidth + 20);
						} else {
							tempTable.style.width = (table.offsetWidth + 20);
							tempTable.alreadyResized = true;
						}
					}

				}
			}
		}
	}
	function applyStyle(table) {
		$("#" + table.id).addClass("inside");
		$("#" + table.id + " >tbody>tr:first-child").css("background-color",
				"#c7d4db");
		$("#" + table.id + " >tbody>tr>td").addClass("inside");
	}
</script>