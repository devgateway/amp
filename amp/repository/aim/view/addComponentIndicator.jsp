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

function onDelete() {
  var flag = confirm('<digi:trn jsFriendly="true" key="aim:deleteconfirm">Are you sure?</digi:trn>');
  return flag;
}
	function validate() 
	{
		if (trim(document.aimComponentsForm.compIndicatorName.value).length == 0) 
		{
			alert('<digi:trn jsFriendly="true" key="aim:enterindicatorname">Please enter Indicator name</digi:trn>');
			document.aimIndicatorForm.compIndicatorName.focus();
			return false;
		}	
		if (trim(document.aimComponentsForm.compIndicatorCode.value).length == 0) 
		{
			alert('<digi:trn jsFriendly="true" key="aim:enterindicatcode">Please enter Indicator code</digi:trn>');
			document.aimIndicatorForm.compIndicatorCode.focus();
			return false;
		}			
		return true;
	}
	
	function saveCompIndicator(id)
	{	if(trim(document.aimComponentsForm.compIndicatorName.value).length == 0 || trim(document.aimComponentsForm.compIndicatorCode.value).length == 0) {
			alert('Please fill all mandatory data');
			return false;
		} else {
		<digi:context name="saveIndi" property="context/module/moduleinstance/addCompIndicator.do?event=save" />
		document.aimComponentsForm.action = "<%= saveIndi %>&id="+id;
		document.aimComponentsForm.target = "_self";
		document.aimComponentsForm.submit();	
		}
		
	}
	
	function onload(){
	if(document.aimComponentsForm.duplicate.value=="save"){
	<digi:context name="refreshIndi" property="context/module/moduleinstance/componentIndicatorManager.do" />
		document.aimComponentsForm.action = "<%= refreshIndi %>";
		document.aimComponentsForm.target = window.opener.name;
		document.aimComponentsForm.submit();
	closeWindow();
		}
	}
	
	function unload(){}

	function closeWindow() 
	{
		window.close();
	}
	
	-->
</script>

<digi:form action="/addCompIndicator.do" method="post">
<html:hidden styleId="duplicate" property="duplicate"/>
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="100%" align="center" border="0">
	<tr bgColor="blue"><td height="1" colspan="2"></td></tr>
	<tr bgColor=#dddddb>
		<td bgColor=#dddddb height="15" align="center" colspan="2"><h4>
			Component Indicator </h4>
		</td>
	</tr>
	<tr bgColor="blue"><td height="1" colspan="2"></td></tr>
	<tr bgColor=#ffffff><td height="10" colspan="2"></td></tr>
	<tr bgColor=#ffffff>
		<td bgColor=#ffffff height="15" align="center" colspan="2"><h5>
			<digi:trn key="aim:CreatingNewIndicator">
				Create a New Indicator
			</digi:trn></h5>
		</td>
	</tr>
	<tr bgColor=#ffffff><td height="15" colspan="2" align="center">
		<digi:errors />
	</td></tr>

	<tr bgColor=#ffffff>
		<td height="10" align="left">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<digi:trn key="aim:compIndicatorName">
			Indicator Name</digi:trn>
			<font color="red">*</font>
		</td>
		<td height="10" align="left">
			<html:text property="compIndicatorName" size="20"/>
		</td>
		
		
		<tr><td height="10" align="left">
			<bean:write name="aimComponentsForm" property="duplicate"/>
		</td>
		</tr>
	
	
	
	</tr>
	<tr bgcolor=#ffffff><td height="5"></td></tr>
	<tr bgColor=#ffffff>
		<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<digi:trn key="aim:compIndicatorDescription">
			Description</digi:trn>
		</td>
		<td align="left">
			<html:textarea property="compIndicatorDesc" cols="35" rows="2" styleClass="inp-text"/>
		</td>
	</tr>	
	<tr bgColor=#ffffff>
		<td height="20" align="left">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<digi:trn key="aim:compIndicatorCode">
			Indicator Code</digi:trn>
			<font color="red">*</font>
		</td>
		<td align="left">
			<html:text property="compIndicatorCode" size="20" styleClass="inp-text"/>
		</td>
	</tr>
	<tr bgColor=#ffffff><td height="30" colspan="2"></td></tr>
	<tr bgColor=#dddddb>
		<td bgColor=#dddddb height="25" align="center" colspan="2">
			<input type="button" value='<digi:trn jsFriendly="true" key="btn:save">Save</digi:trn>' class="dr-menu" onclick="return saveCompIndicator('<bean:write name="aimComponentsForm" property="indicatorId"/>')">&nbsp;&nbsp;
			<html:reset  styleClass="dr-menu" property="submitButton" >
				<digi:trn key="btn:cancel">Cancel</digi:trn> &nbsp;&nbsp;
			</html:reset>
			<html:button  styleClass="dr-menu" property="submitButton"  onclick="closeWindow()">
				<digi:trn key="btn:close">Close</digi:trn>
			</html:button>
		</td>
	</tr>	
</table>
</digi:form>
