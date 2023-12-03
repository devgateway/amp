<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/addFunding.js"/>"></script>
<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp"  />
</script>

<script language="JavaScript">
	<!--
	function validate() 
	{
		if (trim(document.aimThemeForm.name.value).length == 0) 
		{
			alert("Please enter Indicator name");
			document.aimThemeForm.name.focus();
			return false;
		}	
		if (trim(document.aimThemeForm.code.value).length == 0) 
		{
			alert("Please enter Indicator code");
			document.aimThemeForm.code.focus();
			return false;
		}			
		if (trim(document.aimThemeForm.type.value).length == 0) 
		{
			alert("Please enter Indicator type");
			document.aimThemeForm.type.focus();
			return false;
		}
		return true;
	}

	function saveProgram(id)
	{
			var temp = validate();
			if (temp == true) 
			{
				<digi:context name="addThmInd" property="context/ampModule/moduleinstance/addThemeIndicator.do?event=save"/>
				document.aimThemeForm.action = "<%=addThmInd%>&themeId=" +id;
				document.aimThemeForm.target = "_self";
				document.aimThemeForm.submit();
			}
			return true;	
	}
	
	function addIndVal(id)
	{
			<digi:context name="addIndVal" property="context/ampModule/moduleinstance/addThemeIndicator.do?event=indValue"/>
			document.aimThemeForm.action = "<%=addIndVal%>&themeId=" +id;
			document.aimThemeForm.target = "_self";
			document.aimThemeForm.submit();
			return true;
	}

	function load(){}

	function unload(){}

	function closeWindow() 
	{
			<digi:context name="closeInd" property="context/ampModule/moduleinstance/closeThemeIndicator.do"/>
			document.aimThemeForm.action = "<%=closeInd%>";
			document.aimThemeForm.submit();
			window.close();
			return true;
	}

	function trim(s) {
		return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
  	}
	-->
</script>

<digi:instance property="aimThemeForm" />
<digi:form action="/addThemeIndicator.do" method="post">
<digi:context name="digiContext" property="context"/>
<input type="hidden" name="event">
		<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="100%" align="center" border="0">
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="15" align="center" colspan="4"><h4>
						Program M&E Indicators </h4>
				</td>
				</tr>
				<tr bgColor=#ffffff><td height="10" colspan="4"></td></tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgColor=#ffffff>
				<td height="20" align="right">
						<b>Indicator Name</b><font color="red">*</font>&nbsp;
				</td>
				<td align="left" colspan="3">
						<html:text name="aimThemeForm" property="name" size="30"/>
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgColor=#ffffff>
				<td height="20" align="right">
						<b>Indicator Code</b><font color="red">*</font>&nbsp;
				</td>
				<td align="left">
						<html:text name="aimThemeForm" property="code" size="20" styleClass="inp-text"/>
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgColor=#ffffff>
				<td height="20" align="right">
						<b>Indicator Type</b><font color="red">*</font>&nbsp;
				</td>
				<td align="left">
						<html:text name="aimThemeForm" property="type" size="20" styleClass="inp-text"/>
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgColor=#ffffff>
				<td height="20" align="right">
						<b>Creation Date</b>&nbsp;
				</td>
				<td vAlign="bottom">
						<bean:write name="aimThemeForm" property="creationDate"/>
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgcolor=#ffffff>
				<td height="20" align="right">
					<b>Category</b>&nbsp;
				</td>
				<td align="left" valign="bottom">
					<html:select name="aimThemeForm" property="category" styleClass="inp-text">
						<html:option value="0">Input</html:option>	
						<html:option value="1">Output</html:option>
						<html:option value="2">Process</html:option>
						<html:option value="3">Outcomes</html:option>
					</html:select>&nbsp;&nbsp;
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgcolor=#ffffff>
				<td height="20" align="right"><b>
					National Planning Indicator</b>&nbsp;
				</td>
				<td align="left" valign="botto">
					<html:checkbox name="aimThemeForm" property="npIndicator" title="Tick to mark this indicator as an National Planning Indicator"/>
				</td>
				</tr>
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="25" align="center" colspan="4">
						<input styleClass="dr-menu" type="button" name="addValBtn" value="Add Indicator Values" onclick="return addIndVal('<bean:write name="aimThemeForm" property="themeId"/>')">&nbsp;&nbsp;
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="15" colspan="4"></td></tr>
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="25" align="center" colspan="4">
						<input styleClass="dr-menu" type="button" name="addBtn" value="Save" onclick="return saveProgram('<bean:write name="aimThemeForm" property="themeId"/>')">&nbsp;&nbsp;
						<input styleClass="dr-menu" type="reset" value="Cancel">&nbsp;&nbsp;
						<input styleClass="dr-menu" type="button" name="close" value="Close" onclick="closeWindow()">			
				</td>
				</tr>	
		</table>
</digi:form>
