<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>

<script langauage="JavaScript" type="text/javascript">
	function validateValues(){
		 var errmsg='';
                 var ampTeamId=document.getElementById('ampTeamId').value;
		 var width=document.getElementById('width').value;
		 var height=document.getElementById('height').value;
		 var angle=document.getElementById('angle').value;
         var pageSize=document.getElementById('pageSize').value;
		 //*** Validate width
		 if(parseInt(width)==(width-0)){		   	
			 if(parseInt(width)<10 || parseInt(width)>1000){
			 	errmsg+='\n<digi:trn>Width must be in range from 10 to 1000</digi:trn>';
			 }
		 }else{
		 	errmsg+='\n<digi:trn>Please enter correct width</digi:trn>';
		 }		 
		 //***Validate height
		 if(parseInt(height)==(height-0)) {
		 	if(parseInt(height)<10 || parseInt(height)>1000){
			 	errmsg+='\n<digi:trn>Height must be in range from 10 to 1000</digi:trn>';
		 	}
		 }else{
		 	errmsg+='\n<digi:trn>Please enter correct height</digi:trn>';
		 }		 
		 //***Validate angle	

		if(angle!=''){
			if(parseInt(angle)==(angle-0)) {
		 		if(parseInt(angle)<0 || parseInt(angle)>90){
			 		errmsg+='\n<digi:trn>Angle of inclination must be in range from 0 to 90</digi:trn>';
			 	}
		 	}else{
		 		errmsg+='\n<digi:trn>Please enter correct angle</digi:trn>';
		 	}
		} 
		 //***Validate page size
		 if(parseInt(pageSize)==(pageSize-0)) {
		 	if(parseInt(pageSize)<0){
			 	errmsg+='\n<digi:trn>Activities per page should be grather than 0</digi:trn>';
		 	}
		 }		 
		 
		 //***Validate error messages
		 if (errmsg==''){
			saveSettings(ampTeamId,width,height,angle,pageSize);
		 } else{
			alert(errmsg);
			return false;
		 }
	}

        function saveSettings(ampTeamId,width,height,angle,pageSize){
		lastTimeStamp = new Date().getTime();
                <digi:context name="changeSett" property="context/module/moduleinstance/npdSettingsAction.do?actionType=changeSettings"/>
                var params="&ampTeamId="+ampTeamId+"&width="+width+"&height="+height+"&angle="+angle+"&pageSize="+pageSize;
		var url = "${changeSett}"+params+'&timeStamp='+lastTimeStamp;
		var async=new Asynchronous();
		async.complete=closeWindow;
		async.call(url);
	}
        function closeWindow(status, statusText, responseText, responseXML){
            window.close();
        }
      		
</script>

<digi:instance property="npdSettingsForm"/>
<digi:form action="/npdSettingsAction.do?actionType=changeSettings">
 <html:hidden property="ampTeamId" styleId="ampTeamId"/>
<table bgcolor="#f4f4f2" cellPadding="5" cellSpacing="5" width="100%" class="box-border-nopadding">
	<tr>
	<td align="left" vAlign="top">
		<table bgcolor="#aaaaaa" cellPadding="0" cellSpacing="0" width="100%" class="box-border-nopadding">
			<tr bgcolor="#aaaaaa">
				<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
					<digi:trn>Change Npd Settings</digi:trn>
				</td>
			</tr>
			<tr>
			 <td align="center">
				<table border="0" cellpadding="2" cellspacing="1" width="100%">
					<tr bgcolor="#f4f4f2">
						<td align="right" valign="middle" width="50%">
							<digi:trn>Width</digi:trn>
						</td>
						<td align="left" valign="middle"><html:text property="width" styleClass="inp-text" size="7" styleId="width"/></td>
					</tr>
					<tr bgcolor="#f4f4f2">
						<td align="right" valign="middle" width="50%">
							<digi:trn>Height</digi:trn>
						</td>
						<td align="left" valign="middle" nowrap="nowrap"><html:text property="height" styleClass="inp-text" size="7" styleId="height"/></td>
					</tr>
					<tr bgcolor="#f4f4f2">
						<td align="right" valign="middle" width="50%">
							<digi:trn>Angle</digi:trn>
						</td>
						<td align="left" valign="middle"><html:text property="angle" styleClass="inp-text" size="7" styleId="angle"/>&nbsp;
							<small>
								<digi:trn>Blank value will be generated automatically</digi:trn>
							</small>
						</td>
					</tr>
					<tr bgcolor="#f4f4f2">
						<td align="right" valign="middle" width="50%">
							<digi:trn>Activities per page</digi:trn>
						</td>
						<td align="left" valign="middle">
                                                    <html:text property="pageSize" styleClass="inp-text" size="7" styleId="pageSize"/>
						</td>
					</tr>
				
					<tr bgcolor="#ffffff">
						<td colspan="2">
							<table width="100%" cellpadding="3" cellspacing="3" border="0">
								<tr>
						  			<td align="right" width="50%">
						  				<c:set var="trnSaveBtn">
						  					<digi:trn>Save</digi:trn>
						  				</c:set>
						  				<input type="button" value="${trnSaveBtn}" onclick="validateValues()" class="dr-menu"/>
						  			</td>
						  			<td>
						  				<c:set var="trnCancelBtn">
						  					<digi:trn>Cancel</digi:trn>
						  				</c:set>
						  			 	<input type="button" value="${trnCancelBtn}" onclick="window.close();" class="dr-menu"/>
						  			</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
	</td>
	</tr>
</table>
</digi:form>
