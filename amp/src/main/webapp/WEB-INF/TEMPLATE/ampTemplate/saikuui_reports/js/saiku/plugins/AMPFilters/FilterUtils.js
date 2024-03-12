var FilterUtils = {};

FilterUtils.parseValue = function(elem, v) {
	Saiku.logger.log("FilterUtils.parseValue");
	var theName = elem.valueToName ? elem.valueToName[v] : v;
	return {id: v, name: theName};
};
