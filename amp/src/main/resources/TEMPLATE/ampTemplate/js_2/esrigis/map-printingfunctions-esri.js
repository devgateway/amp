var printIndex=0;

var originalWindowsSize=$(window).width();
var originalWindowsHeightSize=$(window).height();

$(window).resize(function(){
	createCarouSelAndReize();
});



function esriPrinting(){
	
	printTask = new esri.tasks.CustomPrintTask("http://gis.devgateway.org/arcgis/rest/services/Utilities/PrintingTools/GPServer/Export%20Web%20Map%20Task", {async: false});
    params = new esri.tasks.PrintParameters();
    params.map = map;
    params.outSpatialReference = map.spatialReference; 
	var ptemplate = new esri.tasks.PrintTemplate();
    ptemplate.preserveScale = false;
    ptemplate.format = "PDF";
    ptemplate.layout = "Letter ANSI A Landscape";
    params.template = ptemplate;
    var layoutOptions = {TitleText: "Test Map", scalebarUnit: 'Miles'};
    ptemplate.layoutOptions = layoutOptions;
    ptemplate.preserveScale = true;
    printTask.execute(params, function(retVal){
    	document.location=retVal.url;
    	//alert("complete");
    });

	
}


