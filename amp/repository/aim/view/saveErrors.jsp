<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %> 
<%@ taglib uri="/taglib/category" prefix="category" %>
            

<!-- <META http-equiv="refresh" content="10; URL=/aim/viewMyDesktop.do"> -->
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>

<script language="JavaScript">
<!--
    var continueExecution = true;
    var stepNo = 0;
    var maxStep = 11;
    var countDown = 15;
    function errdisplay(){
    	var div = document.getElementById("sverr"+stepNo);

		div.style.visibility="visible";
	    stepNo = stepNo + 1;
		if (stepNo < maxStep)
    		setTimeout("errdisplay()", 500);
    	else{
    		setTimeout("redirect()", 50);
			toggleLayer("redirectCancel");
    	}
    }
    
    function redirect(){
    	var staticRedirectMsg1 = 'You are being redirected to the <html:link href="/aim/viewMyDesktop.do"> <digi:trn key="aim:saveErrors:Desktop">Desktop</digi:trn></html:link> in ';
    	var staticRedirectMsg2 = ' seconds ...';
    	var staticRedirectDisabled = 'Redirect disabled please click <html:link href="/aim/viewMyDesktop.do"> <digi:trn key="aim:saveErrors:Desktop">Desktop</digi:trn></html:link> when you are done!';
    	var elem = document.getElementById("redirectMsg");
    	var msg;
    	if (elem == null)
    		countDown = -1;
    	if (countDown > 0){
    		msg = staticRedirectMsg1 + countDown + staticRedirectMsg2;
    		elem.innerHTML = msg;
    		setTimeout("redirect()", 1000);
    		countDown--;
    	}
    	else
    	if (countDown == 0){
    		window.location="/aim/viewMyDesktop.do";
    	}
    	else{
    		if (elem != null)
    			elem.innerHTML = staticRedirectDisabled; 
    	}
    }
    
    function cancelRedirect(){
    	countDown=-1;
		toggleLayer("redirectImg");    		
    	toggleLayer("redirectCancel");
    	redirect();
    }
    
    function toggleLayer(whichLayer){
		var elem, vis;
		if( document.getElementById ) // this is the way the standards work
			elem = document.getElementById( whichLayer );
		else 
			if( document.all ) // this is the way old msie versions work
				elem = document.all[whichLayer];
			else 
				if( document.layers ) // this is the way nn4 works
					elem = document.layers[whichLayer];
		vis = elem.style;
		// if the style.display value is blank we try to figure it out here
		if(vis.display==''&&elem.offsetWidth!=undefined&&elem.offsetHeight!=undefined)
			vis.display = (elem.offsetWidth!=0&&elem.offsetHeight!=0)?'block':'none';
		vis.display = (vis.display==''||vis.display=='block')?'none':'block';
}
    
    var current = window.onload;
	
	window.onload = function() {
        //current.apply(current);
		errdisplay();
   	};
-->    
</script>


<digi:instance property="aimEditActivityForm" />

<input type="hidden" name="edit" value="true">

<digi:errors/>
<p align="center">
<br/><br/>
<table width="70%" height="70%" border="0" cellspacing="2" cellpadding="2" align="center" class="box-border-nopadding">
	<tr>
		<td colspan="2" bgcolor="#FA0505" class="textalb" align="center">
			<b><digi:trn key="aim:saveErrors:recoverySave">Recovery Save</digi:trn></b>
		</td>
	</tr>
	<tr>
		<td>
			<p align="center"> 
				<table align="center">
					<tr>
						<td>
							<div id="redirectImg">
								<img align="top" width="16px" height="16px" src="/repository/aim/view/scripts/ajaxtabs/save-loader.gif" />
							</div>
						</td>
						<td>
							<div id="redirectMsg"> RECOVERY SAVE </div>
						</td>
						<td>
							<div id="redirectCancel" style="display: none;"> &nbsp; &nbsp; &nbsp; &nbsp; <a href="javascript:cancelRedirect();">CANCEL</a></div>
						</td>
					</tr>
				</table>
			</p>
			<br/><br/>
			<p align="center">
			<table align="center" width="50%">
			<tr>
				<td>
					<img align="top" width="48px" height="48px" src="/repository/aim/view/scripts/ajaxtabs/info.gif" />			
				</td>
				<td>
					<font size="3px">Activity Save failed and recovery save managed to save only the steps marked with
					<img align="top" width="16px" height="16px" src="/repository/aim/view/scripts/ajaxtabs/ok.gif" />. Also your activity was 
					saved as draft</font>
				</td>
			</tr>
			</table>
			</p>
			<br/>
			
			<p align="center">
			<table align="center" cellspacing="2" cellpadding="2">
			<logic:iterate id="stepState" name="aimEditActivityForm" property="stepFailure" type="java.lang.Boolean" indexId="idx" >
				<tr id='sverr<c:out value="${idx}" />' style="visibility: hidden">
					<c:if test="${stepState==false}" >
						<td>				 		
							<b><font size="2px">
							Step <c:out value="${aimEditActivityForm.stepText[idx]}" />
							</font></b>
						</td>
						<td>
					 		<img align="top" width="24px" height="24px" src="/repository/aim/view/scripts/ajaxtabs/ok.gif" />
					 	</td>
					 	
					</c:if>
					<c:if test="${stepState!=false}">
						<td>
							<b><font size="2px">
							Step <c:out value="${aimEditActivityForm.stepText[idx]}" />
							</font></b>
							<div id="errMsg<c:out value="${idx}"/>" style="display: none;" >
								<c:out value="${aimEditActivityForm.stepFailureText[idx]}" />
							</div>
						</td>
						<td valign="top">
							<a onclick="javascript:toggleLayer('errMsg<c:out value="${idx}" />');">
								<img align="top" width="24px" height="24px" src="/repository/aim/view/scripts/ajaxtabs/failed.gif" />
							</a>
						</td>
					</c:if>
				</tr>
			</logic:iterate>			
			</table>
			</p>
			
		</td>
	</tr>
</table>
</p>
