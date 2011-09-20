/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */

/**
 * @author aartimon@dginternational.org
 * since Oct 4, 2010
 */

$(document).ready(function(){
	$("#switchTranslationMode").attr('href', 'javascript:wicketSwitchTranslationMode()');
	$("#switchFMMode").css("display", "block");
	$('#wicketFooter').replaceWith($('#footer'));
	$('#wicketRightMenu').replaceWith($('#rightMenu'));
	$('#footer').css("display", "inline"); 
	
	window.onbeforeunload = function() {
	    return 'Are you sure you want to navigate away from this page?';
	};
});

//////////////////////////////////////////////////////////////
//
// functions for button label
//
//////////////////////////////////////////////////////////////


