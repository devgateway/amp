/**
 * 
 */
function hideTooltip() {
	var tooltipHolder = dojo.byId("tooltipHolder");
	tooltipHolder.style.display = "none";
}

/**
 * Returns true is the value is numeric and positive
 * 
 * @param n
 * @returns {Boolean}
 */

function isNumber(n) {
	return !isNaN(parseFloat(n)) && isFinite(n) && parseFloat(n) >= 0;
}

/**
 * 
 */
function showLoading() {
	esri.show(loading);
	map.disableMapNavigation();
	map.hideZoomSlider();
}

/**
 * 
 * @param error
 */
function hideLoading(error) {
	esri.hide(loading);
	map.enableMapNavigation();
	map.showZoomSlider();
}

/**
 * Hide legend div and remove the highlight
 */
function closeHide(divId) {
	$('#' + divId).hide('slow');
	if (divId == "highlightLegend") {
		try {
			map.removeLayer(map.getLayer("highlightMap"));
		} catch (e) {
		}
	}
}

var redGreenHighlightColors = [ new dojo.Color([ 26, 150, 65, 0.9 ]),  // Malawi / DRC colours
                        		new dojo.Color([ 166, 217, 106, 0.9 ]),
                        		new dojo.Color([ 255, 242, 0, 0.9 ]),
                        		new dojo.Color([ 255, 127, 39, 0.9 ]),
                        		new dojo.Color([ 215, 25, 28, 0.9 ]),
                        		new dojo.Color([ 195, 195, 195, 0.9 ]) ];

//var brownBlueHighlightColors = [new dojo.Color([109, 89, 74, 0.9]), // Moldova colours
//                                new dojo.Color([202, 197, 176, 0.9]),
//                                new dojo.Color([150, 163, 153, 0.9]),
//                                new dojo.Color([184, 191, 198, 0.9]),
//                                new dojo.Color([139, 164, 181, 0.9]),
//                                new dojo.Color([200, 200, 230, 0.85]) ];

var brownBlueHighlightColors = [new dojo.Color([139, 164, 181, 0.9]), // Moldova colours
                                new dojo.Color([184, 191, 198, 0.9]),
                                new dojo.Color([150, 163, 153, 0.9]),
                                new dojo.Color([202, 197, 176, 0.9]),
                                new dojo.Color([109, 89, 74, 0.9]),                                                                 
                                new dojo.Color([235, 235, 255, 0.9]) ];

var allHighlightColors = [redGreenHighlightColors, brownBlueHighlightColors];
var breaksColors = allHighlightColors[highlightColorsSelectedOption]; // HIGHEST VALUE... LOWEST VALUE, NO_VALUE

var colorsOrange = [ new dojo.Color([ 229, 252, 120, 0.8 ]),
		new dojo.Color([ 224, 231, 102, 0.9 ]),
		new dojo.Color([ 220, 211, 93, 0.9 ]),
		new dojo.Color([ 216, 190, 80, 0.9 ]),
		new dojo.Color([ 212, 170, 66, 0.9 ]),
		new dojo.Color([ 207, 149, 53, 0.9 ]),
		new dojo.Color([ 203, 129, 40, 0.9 ]),
		new dojo.Color([ 199, 109, 26, 0.9 ]),
		new dojo.Color([ 195, 88, 13, 0.9 ]),
		new dojo.Color([ 191, 68, 6, 0.9 ]) ];

var colorsCualitative = [ new dojo.Color([ 166, 206, 227, 1 ]),
		new dojo.Color([ 31, 120, 180, 1 ]),
		new dojo.Color([ 178, 223, 138, 1 ]),
		new dojo.Color([ 51, 160, 44, 1 ]),
		new dojo.Color([ 251, 154, 153, 1 ]),
		new dojo.Color([ 227, 26, 28, 1 ]),
		new dojo.Color([ 253, 191, 111, 1 ]),
		new dojo.Color([ 255, 127, 0, 1 ]),
		new dojo.Color([ 202, 178, 214, 1 ]),
		new dojo.Color([ 106, 61, 154, 1 ]),
		new dojo.Color([ 255, 255, 153, 1 ]) ];

/**
 * 
 * @param array
 * @param measure
 * @returns {Number}
 */
function getMaxValue(array, measure) {
	var maxValue = 0;
	for ( var i = 0; i < array.length; i++) {
		var currentMeasure = parseFloat(array[i][measure]);
		if (currentMeasure > maxValue)
			maxValue = currentMeasure;
	}

	return maxValue;
}

/**
 * 
 * @param array
 * @param measure
 * @returns {Number}
 */
function getMinValue(array, measure) {
	var minValue = 0;
	for ( var i = 0; i < array.length; i++) {
		var currentMeasure = parseFloat(array[i][measure]);
		if (minValue == 0)
			minValue = currentMeasure;
		if (currentMeasure < minValue)
			minValue = currentMeasure;
	}
	return minValue;
}

function containsDonor(donor, donorArray) {
	var i;
	for (i = 0; i < donorArray.length; i++) {
		if (donorArray[i].donorCode == donor.donorCode) {
			return true;
		}
	}
	return false;
}

function getCheckedValue(radioObj) {
	if (!radioObj)
		return "";
	var radioLength = radioObj.length;
	if (radioLength == undefined)
		if (radioObj.checked)
			return radioObj.value;
		else
			return "";
	for ( var i = 0; i < radioLength; i++) {
		if (radioObj[i].checked) {
			return radioObj[i].value;
		}
	}
	return "";
}

function filldatasourcetable() {
	deleteRow("sourcecontent");
	var table = document.getElementById("sourcecontent");
	var rowCount = table.rows.length;
	var cell;
	var strlength = 100; // set to the number of characters you want to keep
	var trimmedPathname;
	var donors = '';

	for ( var int = 0; int < activitiesarray.length; int++) {
		donors = '';
		for ( var x = 0; x < activitiesarray[int].donors.length; x++) {
			if (donors == '') {
				donors = activitiesarray[int].donors[x].donorCode;
			} else {
				donors = donors + ', '
						+ activitiesarray[int].donors[x].donorCode;
			}
		}

		trimmedPathname = activitiesarray[int].activityname.substring(0, Math
				.min(strlength, activitiesarray[int].activityname.length));

		row = table.insertRow(rowCount + int);
		cell = row.insertCell(0);
		var url = '<a title="'
				+ activitiesarray[int].activityname
				+ '"href="/aim/viewActivityPreview.do~activityId='
				+ activitiesarray[int].id
				+ '~isPreview=1" target="_blank" style="text-decoration:none;">'
				+ trimmedPathname + '...</a>';
		cell.innerHTML = url;
		cell.setAttribute("width", "60%");
		cell0 = row.insertCell(1);
		cell0.innerHTML = activitiesarray[int].ampactivityid;
		cell0.setAttribute("width", "20%");

		cell1 = row.insertCell(2);
		cell1.innerHTML = donors;
		cell1.setAttribute("width", "20%");

		// cell1 = row.insertCell(3);
		// cell1.innerHTML= activitiesarray[int].commitments;

		// cell3 = row.insertCell(4);
		// cell3.innerHTML= activitiesarray[int].disbursements;
	}
}

function filldatasourcetablenational() {
	deleteRow("natsourcecontent");
	var table = document.getElementById("natsourcecontent");
	var rowCount = table.rows.length;
	var cell;
	var strlength = 100; // set to the number of characters you want to keep
	var trimmedPathname;
	var donors = '';

	for ( var int = 0; int < nationalactivitiesarray.length; int++) {
		donors = '';
		for ( var x = 0; x < nationalactivitiesarray[int].donors.length; x++) {
			if (donors == '') {
				donors = nationalactivitiesarray[int].donors[x].donorCode;
			} else {
				donors = donors + ', '
						+ nationalactivitiesarray[int].donors[x].donorCode;
			}
		}

		trimmedPathname = nationalactivitiesarray[int].activityname.substring(
				0, Math.min(strlength,
						nationalactivitiesarray[int].activityname.length));

		row = table.insertRow(rowCount + int);
		cell = row.insertCell(0);
		var url = '<a title="'
				+ nationalactivitiesarray[int].activityname
				+ '"href="/aim/viewActivityPreview.do~activityId='
				+ nationalactivitiesarray[int].id
				+ '~isPreview=1" target="_blank" style="text-decoration:none;">'
				+ trimmedPathname + '...</a>';
		cell.innerHTML = url;
		cell.setAttribute("width", "350px");

		cell0 = row.insertCell(1);
		cell0.innerHTML = nationalactivitiesarray[int].commitments;
		cell0.setAttribute("width", "100px");

		cell1 = row.insertCell(2);
		cell1.innerHTML = nationalactivitiesarray[int].disbursements;
		cell1.setAttribute("width", "100px");

		cell2 = row.insertCell(3);
		cell2.innerHTML = donors;
		cell2.setAttribute("width", "120px");

	}
}

function deleteRow(tableID) {
	try {
		var table = document.getElementById(tableID);
		var rowCount = table.rows.length;
		if (rowCount > 0) {
			for ( var i = 0; i < rowCount; i++) {
				table.deleteRow(i);
				rowCount--;
				i--;
			}
		}
	} catch (e) {
		alert(e);
	}
}
