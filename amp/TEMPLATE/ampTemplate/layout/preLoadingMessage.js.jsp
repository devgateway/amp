<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@taglib uri="/taglib/digijava" prefix="digi"%>

document.write('<div id="loading" class="loading"><p class="load">' + 
		'<br/><img src="/TEMPLATE/ampTemplate/images/amploading.gif" border="0"/>&nbsp;&nbsp;<digi:trn>Loading, please wait ...</digi:trn><br/><br/></p></div>');

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
