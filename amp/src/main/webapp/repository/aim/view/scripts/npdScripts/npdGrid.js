function exportToExcel(){
		var url = addActionToURL('exportIndicators2xsl.do');
		var params = getInidcatorsParam(true);
		url+=params;
	    document.aimNPDForm.target="_blank";
	    document.aimNPDForm.action=url;
	    document.aimNPDForm.submit();
}

function getInidcatorsParam(submit){
		var params = p1d + 'programId='+curProgId;
		if(!submit){
			if(selYear!=null){
				for (var y=0; y<selYear.length; y++){
					params += pd + 'selYears=' + selYear[y];
				}
			}
			
			if (selIndicatorIDs != null) {
				for (var y=0; y<selIndicatorIDs.length; y++){
					params += pd + 'selIndicators=' + selIndicatorIDs[y];
				}
			}
		}
		
		return params;
	}
var responseSuccess = function(o) {
	var place=document.getElementById("indicatorTable");
	var showLink=document.getElementById("showGridLink");
	var hideLink=document.getElementById("hideGridLink");
	place.innerHTML=o.responseText;
	place.style.display="block";
	showLink.style.display="none";
	hideLink.style.display="inline";
};
 
var responseFailure = function(o) {
	

};
 
var callbackNPDGrid = {
  success:responseSuccess,
  failure:responseFailure
};


function showGridTable(){
	if (curProgId !=null){
		var url=addActionToURL('npdGrid.do');
		var params = getInidcatorsParam(false);
		url+=params;
		var transaction = YAHOO.util.Connect.asyncRequest('GET', url, callbackNPDGrid);
	}
}
function hideGridTable(){
	var place=document.getElementById("indicatorTable");
	var showLink=document.getElementById("showGridLink");
	var hideLink=document.getElementById("hideGridLink");
	place.style.display="none";
	showLink.style.display="inline";
	hideLink.style.display="none";
}