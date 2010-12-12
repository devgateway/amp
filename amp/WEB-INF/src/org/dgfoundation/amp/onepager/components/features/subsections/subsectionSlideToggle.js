$(document).ready(function(){
	$("a.slider").click(function(){
		$(this).siblings("div:first").slideToggle();
		return false;
		});
})