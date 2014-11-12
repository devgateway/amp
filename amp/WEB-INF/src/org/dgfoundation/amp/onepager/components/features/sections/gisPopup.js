var callerGisObject;
function gisPopup(caller){
	var param = "width=780, height=500, scrollbars=yes,modal=yes, resizable, status";
	callerGisObject = caller;
	window.open("/esrigis/mainmap.do?popup=true", "", param);
}



function postvaluesx(element){
	element.focus();
	setTimeout(function(){element.select();}, 1000);
	setTimeout(function(){element.blur();}, 1000);
}
function postvaluesy(element){
	setTimeout(function(){element.focus();}, 3000);
	setTimeout(function(){element.select();}, 1000);
	setTimeout(function(){element.blur();}, 3000);
}
