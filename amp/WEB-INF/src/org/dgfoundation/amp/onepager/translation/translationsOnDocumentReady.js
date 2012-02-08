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
	$('#wicketRightMenu').replaceWith($('#rightMenu'));
	
	$('#imgGroupMode').attr("title", $("#imgGroupModeTitle").html());
	$('#imgOnepagerMode').attr("title", $("#imgOnepagerModeTitle").html());
	if (typeof(onepagerMode) !== 'undefined') {
		if(onepagerMode){
			$('#imgGroupMode').show();
		}
		else{
			$('#imgOnepagerMode').show();
		}
	}

	
	window.onbeforeunload = function (e) {
		  e = e || window.event;
		  
		  // For IE and Firefox prior to version 4
		  if (e) {
			  if (navigator.appName == 'Microsoft Internet Explorer') {
				  return 'Are you sure you want to navigate away from this page?';
			  }else{
				  e.returnValue = ' ';
			  }
		  }
	}
});

//////////////////////////////////////////////////////////////
//
// functions for button label
//
//////////////////////////////////////////////////////////////


