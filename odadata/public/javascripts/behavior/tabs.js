var checkOnUnload = true;

function highlightTabsWithErrors(element) {
	var tabs = element.children('li');
	tabs.each(function() {
		divId = $(this).children('a').attr('href');
		correspondingDiv = $(divId);
		
		if (correspondingDiv.find('.fieldWithErrors').length > 0) {
			$(this).addClass('tab_with_errors');
		}
	});
}

$(document).ready(function(){
	window.onbeforeunload = checkFirst;
	$("div#tab_container").tabs();
	highlightTabsWithErrors($("ul#tabs"));
});

 function checkFirst(){
   if(checkOnUnload)
     return '';
 }

 $(window).scroll(function() {
    $('#command_box').animate({top:$(window).scrollTop()+27+"px" },{queue: false, duration: 350});
 });