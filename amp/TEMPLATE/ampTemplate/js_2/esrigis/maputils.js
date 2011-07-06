
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