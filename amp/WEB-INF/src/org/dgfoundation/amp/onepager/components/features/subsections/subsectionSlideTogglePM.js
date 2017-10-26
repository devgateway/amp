$(document).ready(function(){
	$("a.sliderPM").click(function(){
		$(this).siblings("div:first").slideToggle();
		return false;
		});
})