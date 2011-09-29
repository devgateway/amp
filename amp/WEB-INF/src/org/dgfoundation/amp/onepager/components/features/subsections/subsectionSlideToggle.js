$(document).ready(function(){
	$("a.slider").click(function(){
		$(this).siblings("div:first").slideToggle();
		return false;
		});
	var mainContentTop = $('#mainContent').offset().top - 23;
	var mainContentLeft = $('#mainContent').offset().left + 800;
	$('#rightMenu').css('top', mainContentTop + "px");
	$('#rightMenu').css('left', mainContentLeft + "px");
	$('#rightMenu').css('display', 'block');
});

$(window).resize(function() {
	var mainContentTop = $('#mainContent').offset().top - 23;
	var mainContentLeft = $('#mainContent').offset().left;
	mainContentLeft = mainContentLeft + 800;
	$('#rightMenu').css('top', mainContentTop + "px");
	$('#rightMenu').css('left', mainContentLeft + "px");
});

$(window).scroll(function() {
	var mainContentTop = $('#mainContent').offset().top - 23;
	var mainContentLeft = $('#mainContent').offset().left;
	var currentScrollLeft = $(window).scrollLeft();
	mainContentLeft = mainContentLeft + 800 - currentScrollLeft;
	var currentScrollTop = mainContentTop - $(window).scrollTop();
	if (currentScrollTop < 0)
		currentScrollTop = 2;
	$('#rightMenu').css('top', currentScrollTop + "px");
	$('#rightMenu').css('left', mainContentLeft + "px");
});