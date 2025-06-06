module.exports = {
		"nameindicators" : {
			next : "definemeasurelabels",
			previous : "nameindicators",
			index : 0
		},
		"definemeasurelabels" : {
			next : "defineadmlevels",
			previous : "nameindicators",
			index : 1
		},
		"defineadmlevels" : {
			next : "exportfile",
			previous : "definemeasurelabels",
			index : 2
		},
		"exportfile" : {
			next : "reimportfile",
			previous : "defineadmlevels",
			index : 3
		},
		"reimportfile" : {
			next : "definecolorscheme",
			previous : "exportfile",
			index : 4
		},
		"definecolorscheme" : {
			next : "addnotes",
			previous : "reimportfile",
			index : 5
		},
		"addnotes" : {
			next : "reviewandsave",
			previous : "definecolorscheme",
			index : 6
		}
	};
