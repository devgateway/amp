enableComputateVisibleSections = false;

function isScrolledIntoView(docViewTop, docViewBottom, elem){
    var elemTop = $(elem).offset().top;
    var elemBottom = elemTop + $(elem).height();

    return (((elemBottom >= docViewTop) && (elemBottom <= docViewBottom)) 
      || ((elemTop <= docViewBottom) && (elemTop >= docViewTop)) 
      || ((elemTop <= docViewTop) && (elemBottom >= docViewBottom)));
}


function computateVisibleSections(){
	var docViewTop = $(window).scrollTop();
    var docViewBottom = docViewTop + $(window).height();

	if (enableComputateVisibleSections){
		$('#qListItems').find('li').removeClass('quickListHighlight');
		$('span[name=section]').each(function (){
			if (isScrolledIntoView(docViewTop, docViewBottom, this))
				$('#qItem'+$(this).find('a:first').attr('id')).parent().parent().addClass('quickListHighlight');
		});
	}
}

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
	enableComputateVisibleSections = true;
	if (onepagerMode)
		computateVisibleSections();
	else
		highlightQItem($("#qListItems").find('a:first'));
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
	if (onepagerMode)
		computateVisibleSections();
});