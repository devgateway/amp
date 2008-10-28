<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script langauage="JavaScript">
	function validateValues(){
		 var errmsg='';	
		 var width=document.getElementsByName('width')[0].value;
		 var height=document.getElementsByName('height')[0].value;
		 var angle=document.getElementsByName('angle')[0].value;
		 //*** Validate width
		 if(parseInt(width)==(width-0)){		   	
			 if(parseInt(width)<10 || parseInt(width)>1000){
			 	errmsg+='\n<digi:trn key="aim:npdSettings:warningWidth">Width must be in range from 10 to 1000</digi:trn>';
			 }
		 }else{
		 	errmsg+='\n<digi:trn key="aim:npdSettings:warningWidthRange">Please enter correct width</digi:trn>';
		 }		 
		 //***Validate height
		 if(parseInt(height)==(height-0)) {
		 	if(parseInt(height)<10 || parseInt(height)>1000){
			 	errmsg+='\n<digi:trn key="aim:npdSettings:warningHeight">Height must be in range from 10 to 1000</digi:trn>';
		 	}
		 }else{
		 	errmsg+='\n<digi:trn key="aim:npdSettings:warningHeightRange">Please enter correct height</digi:trn>';
		 }		 
		 //***Validate angle	

			if(angle!=''){
		  		if(parseInt(angle)==(angle-0)) {
		 		if(parseInt(angle)<0 || parseInt(angle)>90){
			 	errmsg+='\n<digi:trn key="aim:npdSettings:warningAngle">Angle of inclination must be in range from 0 to 90</digi:trn>';
			}
		 }else{
		 	errmsg+='\n<digi:trn key="aim:npdSettings:warningAngleRange">Please enter correct angle</digi:trn>';
		 }
				} 
		 
		 //***Validate error messages
		 if (errmsg==''){
			 window.close();
		 	 return true;
		 }		
		 
		alert(errmsg);
		return false;
	}
</script>
<digi:instance property="npdSettingsForm"/>
<digi:form action="/npdSettingsAction.do?actionType=changeSettings">
<html:hidden property="ampTeamId"/>
<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
	<tr>
	<td align=left vAlign=top>
		<table bgcolor=#aaaaaa cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
			<tr bgcolor="#aaaaaa">
				<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
					<digi:trn key="aim:npdSettings:changeNpdSettings">Change Npd Settings</digi:trn>
				</td>
			</tr>
			<tr>
			 <td align="center">
				<table border="0" cellpadding="2" cellspacing="1" width="100%">
					<tr bgcolor="#f4f4f2">
						<td align="right" valign="middle" width="50%">
							<digi:trn key="aim:npdSettings:width">Width</digi:trn>
						</td>
						<td align="left" valign="middle"><html:text property="width" styleClass="inp-text" size="7"/></td>
					</tr>
					<tr bgcolor="#f4f4f2">
						<td align="right" valign="middle" width="50%">
							<digi:trn key="aim:npdSettings:height">Height</digi:trn>
						</td>
						<td align="left" valign="middle" nowrap="nowrap"><html:text property="height" styleClass="inp-text" size="7"/></td>
					</tr>
					<tr bgcolor="#f4f4f2">
						<td align="right" valign="middle" width="50%">
							<digi:trn key="aim:npdSettings:angle">Angle</digi:trn>
						</td>
						<td align="left" valign="middle"><html:text property="angle" styleClass="inp-text" size="7"/>&nbsp;
							<small>
								<digi:trn key="aim:npdSettings:blankAngle">Blank value will be generated automatically</digi:trn>
							</small>
						</td>
					</tr>
					<tr bgcolor="#f4f4f2">
						<td align="right" valign="middle" width="50%">
							<digi:trn key="aim:npdSettings:actvitiesPerPage">Activities per page</digi:trn>
						</td>
						<td align="left" valign="middle">
							<html:text property="pageSize" styleClass="inp-text" size="7"/>
						</td>
					</tr>
				
					<tr bgcolor="#ffffff">
						<td colspan="2">
							<table width="100%" cellpadding="3" cellspacing="3" border="0">
								<tr>
						  			<td align="right" width="50%">
						  				<c:set var="trnSaveBtn">
						  					<digi:trn key="aim:npdSettings:btnSave">Save</digi:trn>
						  				</c:set>
						  				<html:submit value="${trnSaveBtn}" onclick="return validateValues();"/>
						  			</td>
						  			<td>
						  				<c:set var="trnCancelBtn">
						  					<digi:trn key="aim:npdSettings:btnCancel">Cancel</digi:trn>
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
