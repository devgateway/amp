// Author: Rahul
// Date : 09 Nov. 2005
// Use: Advance Report Manager
//
function quitAdvRptMngr() {
		var temp = confirm("Do you really want to quit Advanced Report Manager? \nWarning: All your Current Data Will be Lost... press OK to QUIT Advanced Report Manager.");
		if(temp.toString()=="true"){
//		alert("----"+temp);
//		<digi:context name="step" property="context/module/moduleinstance/viewMyDesktop.do" />
		document.aimAdvancedReportForm.action = "/viewMyDesktop.do";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
		}
		else{
			return(false);
		}
}