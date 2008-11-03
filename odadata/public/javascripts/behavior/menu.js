jQuery(function() {
	jQuery("#nav li").hover(function() {
		jQuery(this).addClass("over");
	}, function() {
		jQuery(this).removeClass("over");
	});
});