<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

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
	function saveProgram(id)
	{
			var temp = validate();
			if(temp == true)
			{
				<digi:context name="editThm" property="context/module/moduleinstance/editTheme.do?event=update"/>
				document.aimThemeForm.action = "<%=editThm%>&themeId=" + id;
				document.aimThemeForm.target = "_self";
				document.aimThemeForm.submit();
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

<digi:errors/>
<digi:instance property="aimThemeForm" />
<digi:form action="/themeManager.do" method="post">
<digi:context name="digiContext" property="context" />
<input type="hidden" name="event">
<%--  AMP Admin Logo --%>
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<%-- End of Logo --%>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=757>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=300>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<%-- Start Navigation --%>
					<td height=33><span class=crumb>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</bean:define>
						<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewProgramManager">Click here to goto Program Manager</digi:trn>
						</bean:define>
						<digi:link href="/themeManager.do" styleClass="comment" title="<%=translation%>">
						<digi:trn key="aim:programManager">
							Program Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:editProgram">
							Edit Program
						</digi:trn>
					</td>
					<%-- End navigation --%>
				</tr>
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="10" align="center" colspan="2"><h4>
						<digi:trn key="aim:editProgram">
									Edit Program
						</digi:trn> </h4>
				</td>
				</tr>
				<tr bgColor=#ffffff><td height="10" colspan="2"></td></tr>

				<tr bgColor=#ffffff>
						<td height="10" align="center">
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
				<td height="20" align="center">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programDescription">
						Description</digi:trn>
				</td>
				<td align="left">
						<html:textarea property="programDescription" cols="35" rows="2" styleClass="inp-text"/>
				</td>
				</tr>	
				<tr bgColor=#ffffff>
				<td height="20" align="center">
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
				<td height="20" align="center">
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
						<input styleClass="dr-menu" type="button" name="addBtn" value="Save" onclick="return saveProgram('<bean:write name="aimThemeForm" property="themeId"/>')">&nbsp;&nbsp;
						<input styleClass="dr-menu" type="reset" value="Cancel">&nbsp;&nbsp;
				</td>
				</tr>	
		</table>
</digi:form>
