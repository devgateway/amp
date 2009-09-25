document.write("<div id='loading'><p class='load'>" + 
		"<br/><img src='/TEMPLATE/ampTemplate/images/amploading.gif' border='0'/>"+"&nbsp;&nbsp;Loading, please wait ..."+"<br/><br/></p></div>");

function delBody(){
  document.getElementById("loading").style.display="none";
}
	 
function addLoadEvent(func) {
  var oldonload = window.onload;
  if (typeof window.onload != 'function') {
    window.onload = func;
  } else {
    window.onload = function () {
      if (oldonload) {
        oldonload();
      }
      func();
    }
  }
}
addLoadEvent(delBody);
