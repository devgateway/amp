Saiku.events.bind('session:new', function(session) {
	$("#header").removeClass("hide");
	$("#ui-datepicker-div").hide();
});

Saiku.events.bind('render:end', function(session) {
	$("#ui-datepicker-div").hide();
	if(Settings.AMP_REPORT_API_BRIDGE)
		$("#header").find(".newtab").hide();
});
