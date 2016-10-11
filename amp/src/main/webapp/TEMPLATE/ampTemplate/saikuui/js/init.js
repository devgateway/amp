require.config({
	'waitSeconds' : 0,
	baseUrl : '/TEMPLATE/ampTemplate/saikuui/js',
	paths : {
		filtersWidget : '/TEMPLATE/ampTemplate/node_modules/amp-filter/dist/amp-filter'
	},
	shim : {
		filtersWidget : {
			exports : 'filtersWidget'
		}
	}
});

var app = {};
app.SaikuApp = {};

define([ 'filtersWidget' ], function(FiltersWidget) {
	var containerName = "#filter-popup";
	var container = jQuery(containerName);
	// Create the FilterWidget instance.
	app.SaikuApp.filtersWidget = new FiltersWidget({
		el : containerName,
		draggable : true,
		caller: 'REPORTS' 
	});
	app.SaikuApp.filtersWidget.showFilters();
	
	container.show();
});
