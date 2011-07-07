
/**
 * 
 */
function hideTooltip(){
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
function closeHide() {
	$('#legenddiv').hide('slow');
	try {
		map.removeLayer(map.getLayer("highlightMap"));
	} catch (e) {
	}
}

var colorsBlue = [ new dojo.Color([ 222, 235, 247, 0.7 ]),
		new dojo.Color([ 198, 219, 239, 0.7 ]),
		new dojo.Color([ 158, 202, 225, 0.7 ]),
		new dojo.Color([ 107, 174, 214, 0.7 ]),
		new dojo.Color([ 66, 146, 198, 0.7 ]),
		new dojo.Color([ 33, 113, 181, 0.7 ]),
		new dojo.Color([ 8, 81, 156, 0.7 ]),
		new dojo.Color([ 8, 48, 107, 0.7 ]) ];

var colorsOrange = [ new dojo.Color([ 255, 255, 229, 0.8 ]),
		new dojo.Color([ 255, 247, 188, 0.8 ]),
		new dojo.Color([ 254, 227, 145, 0.8 ]),
		new dojo.Color([ 254, 196, 79, 0.8 ]),
		new dojo.Color([ 254, 153, 41, 0.8 ]),
		new dojo.Color([ 236, 112, 20, 0.8 ]),
		new dojo.Color([ 204, 76, 2, 0.8 ]),
		new dojo.Color([ 153, 52, 4, 0.8 ]),
		new dojo.Color([ 102, 37, 6, 0.8 ]) ];

var colorsDiverge = [new dojo.Color([165, 0, 38, 0.8 ]),
                     new dojo.Color([215, 48, 39, 0.8 ]),
                     new dojo.Color([244, 109, 67, 0.8 ]),
                     new dojo.Color([253, 174, 97, 0.8 ]),
                     new dojo.Color([254, 224, 144, 0.8 ]),
                     new dojo.Color([255, 255, 191, 0.8 ]),
                     new dojo.Color([224, 243, 248, 0.8 ]),
                     new dojo.Color([171, 217, 233, 0.8 ]),
                     new dojo.Color([116, 173, 209, 0.8 ]),
                     new dojo.Color([69, 117, 180, 0.8 ]),
                     new dojo.Color([49, 54, 149, 0.8 ])];

var colorsCualitative = [
new dojo.Color([166, 206, 227, 1]),
new dojo.Color([31, 120, 180, 1]),
new dojo.Color([178, 223, 138, 1]),
new dojo.Color([51, 160, 44, 1]),
new dojo.Color([251, 154, 153, 1]),
new dojo.Color([227, 26, 28, 1]),
new dojo.Color([253, 191, 111, 1]),
new dojo.Color([255, 127, 0, 1]),
new dojo.Color([202, 178, 214, 1]),
new dojo.Color([106, 61, 154, 1]),
new dojo.Color([255, 255, 153, 1])
];

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
	return maxValue + 10;
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
	return minValue - 10;
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


