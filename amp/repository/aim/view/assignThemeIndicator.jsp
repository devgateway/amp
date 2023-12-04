<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">
	<!--

	function assignIndTo(id)
	{
		<digi:context name="indAssign" property="context/module/moduleinstance/assignThemeIndicator.do"/>
		document.aimThemeForm.action = "<%= indAssign %>?indicatorId=" +id;
		document.aimThemeForm.target = window.opener.name;
		document.aimThemeForm.submit();
		window.close();
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

<digi:instance property="aimThemeForm" />
<digi:form action="/assignThemeIndicator.do" method="post">
<digi:context name="digiContext" property="context"/>
		<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="100%" align="center" border="0">
				<tr bgColor=#dddddb width="150">
				<td bgColor=#dddddb height="15" align="center"><h4>
						Assign the Indicator to a different Program </h4>
				</td>
				</tr>
				<tr bgColor=#ffffff width="150"><td bgColor=#ffffff height="15" align="center"></td></tr>
				<tr bgColor=#ffffff width="150">
				<td bgColor=#ffffff align="center">
						<html:select property="selectTheme" styleClass="inp-text">
							<html:option value="-1">Assign to Program</html:option>
							<html:optionsCollection name="aimThemeForm" property="themes" value="ampThemeId" label="name" />
						</html:select>							
				</td>
				</tr>
				<tr bgColor=#ffffff width="150"><td bgColor=#ffffff height="15" align="center"></td></tr>
				<tr bgColor=#ffffff width="150"><td bgColor=#ffffff height="15" align="center"></td></tr>
				<tr bgColor=#ffffff width="150">
				<td bgColor=#ffffff align="center">
						<input styleClass="dr-menu" type="button" name="assignButton" value="Assign" onclick="return assignIndTo('<bean:write name="aimThemeForm" property="indicatorId"/>')">&nbsp;&nbsp;
						<input styleClass="dr-menu" type="button" name="close" value="Close" onclick="closeWindow()">
				</td>
				</tr>
		</table>
</digi:form>
