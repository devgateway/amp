var callerGisObject;
function callGisPopup(caller){
	var param = "width=700, height=500, scrollbars=yes,modal=yes, resizable, status";
	callerGisObject = caller;
	window.open("/esrigis/mainmap.do?popup=true", "", param);
}