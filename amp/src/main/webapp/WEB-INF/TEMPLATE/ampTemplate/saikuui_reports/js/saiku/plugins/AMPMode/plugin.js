Saiku.events.bind('session:new', function(session) {
	$("#header").removeClass("hide");
	$("#ui-datepicker-div").hide();
});

Saiku.events.bind('render:end', function(session) {
	$("#ui-datepicker-div").hide();
	$("#header").find(".newtab").hide();
});
