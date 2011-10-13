/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */

/**
 * @author aartimon@dginternational.org
 * since Oct 13, 2011
 */

$(document).ready(function(){
	$('#imgGroupMode').attr("title", $("#imgGroupModeTitle").html());
	$('#imgOnepagerMode').attr("title", $("#imgOnepagerModeTitle").html());
	if(onepagerMode){
		$('#imgGroupMode').show();
	}
	else{
		$('#imgOnepagerMode').show();
	}	
});