<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
	<!--
	function validate() 
	{
		if (trim(document.aimThemeForm.programName.value).length == 0) 
		{
			alert("Please enter Program name");
			document.aimThemeForm.programName.focus();
			return false;
		}	
		if (trim(document.aimThemeForm.programCode.value).length == 0) 
		{
			alert("Please enter Program code");
			document.aimThemeForm.programCode.focus();
			return false;
		}			
		if (trim(document.aimThemeForm.programType.value).length == 0) 
		{
			alert("Please enter Program type");
			document.aimThemeForm.programType.focus();
			return false;
		}
		return true;
	}
	function saveProgram(rutId,id,level,name)
	{
			var temp = validate();
			if (temp == true) 
			{
				document.aimThemeForm.addBtn.disabled = true;	  	
				<digi:context name="addThm" property="context/module/moduleinstance/addSubPrgInd.do?event=save"/>
				document.aimThemeForm.action = "<%=addThm%>&themeId=" + id + "&indlevel=" + level + "&indname=" + name + "&rootId=" + rutId;
				document.aimThemeForm.target = window.opener.name;
				document.aimThemeForm.submit();
				window.close();
			}
			return true;
	}
	function load(){}

	function unload(){}

	function closeWindow() 
	{
		window.close();
	}
	-->
</script>

<digi:form action="/themeManager.do" method="post">
		<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" align="center" border="0">
				<tr bgColor="blue"><td height="1" colspan="2"></td></tr>
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="15" align="center" colspan="2"><h4>
						Program Manager </h4>
				</td>
				</tr>
				<tr bgColor="blue"><td height="1" colspan="2"></td></tr>
				<tr bgColor=#ffffff><td height="10" colspan="2"></td></tr>
				<tr bgColor=#ffffff>
					<td bgColor=#ffffff height="15" align="center" colspan="2"><h5>
							<digi:trn key="aim:CreatingNewProgram">
									Create a New Program
							</digi:trn></h5>
					</td>
				</tr>

				<tr bgColor=#ffffff>
						<td height="10" align="left">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<digi:trn key="aim:programName">
										Program Name</digi:trn>
								<font color="red">*</font>
						</td>
						<td height="10" align="left">
								<html:text property="programName" size="20"/>
						</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5"></td></tr>
				<tr bgColor=#ffffff>
				<td height="20" align="left">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programDescription">
						Description</digi:trn>
				</td>
				<td align="left">
						<html:textarea property="programDescription" cols="35" rows="2" styleClass="inp-text"/>
				</td>
				</tr>	
				<tr bgColor=#ffffff>
				<td height="20" align="left">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programCode">
						Program Code</digi:trn>
						<font color="red">*</font>
				</td>
				<td align="left">
						<html:text property="programCode" size="20" styleClass="inp-text"/>
				</td>
				</tr>
				<tr bgColor=#ffffff>
				<td height="20" align="left">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programType">
						Program Type</digi:trn>
						<font color="red">*</font>
				</td>
				<td align="left">
						<html:text property="programType" size="20" styleClass="inp-text"/>
				</td>
				</tr>	
				<tr bgcolor=#ffffff><td height="5"></td></tr>	
				<tr bgColor=#ffffff><td height="30" colspan="2"></td></tr>
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="25" align="center" colspan="2">
						<input styleClass="dr-menu" type="button" name="addBtn" value="Save" onclick="return saveProgram('<bean:write name="aimThemeForm" property="rootId" />','<bean:write name="aimThemeForm" property="prgParentThemeId" />','<bean:write name="aimThemeForm" property="prgLevel"/>','<bean:write name="aimThemeForm" property="name"/>')">&nbsp;&nbsp;
						<input styleClass="dr-menu" type="reset" value="Cancel">&nbsp;&nbsp;
						<input styleClass="dr-menu" type="button" name="close" value="Close" onclick="closeWindow()">			
				</td>
				</tr>	
		</table>
</digi:form>
