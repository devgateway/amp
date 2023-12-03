<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
	<!--
	function validate() 
	{
		if (trim(document.aimParisIndicatorManagerForm.piQuestionGot.value).length == 0) 
		{
			alert("Please enter Indicator Question");
			document.aimParisIndicatorManagerForm.piQuestionGot.focus();
			return false;
		}	
		if (trim(document.aimParisIndicatorManagerForm.helperQuestTypeId.value).length == 0) 
		{
			alert("Please enter Indicator code");
			document.aimParisIndicatorManagerForm.helperQuestTypeId.focus();
			return false;
		}			
		return true;
	}
	
	function saveIndicator()
	{
		
		<digi:context name="addPIInd" property="context/ampModule/moduleinstance/parisIndicatorAdd.do?editquestion=false" />
		document.aimParisIndicatorManagerForm.action = "<%= addPIInd%>";
		document.aimParisIndicatorManagerForm.target = window.opener.name;
		document.aimParisIndicatorManagerForm.submit();
		window.close();
	}
	function unload(){}

	function closeWindow() 
	{
		window.close();
	}
	-->
</script>

<digi:form action="/parisIndicatorAdd.do" method="post">
<digi:errors/>

<digi:instance property="aimParisIndicatorManagerForm" />
<input type="hidden" name="editquestion">

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="99%" align="center" border="0">
	<tr bgColor="blue"><td height="1" colspan="2"></td></tr>
	<tr bgColor=#dddddb>
		<td bgColor=#dddddb height="15" align="center" colspan="2"><h4>
			EDIT PARIS INDICATOR QUESTION </h4>
		</td>
	</tr>
	<tr bgColor="blue"><td height="1" colspan="2"></td></tr>
	<tr bgColor=#ffffff><td height="10" colspan="2"></td></tr>
	<tr bgColor=#ffffff>
		<td bgColor=#ffffff height="15" align="center" colspan="2"><h5>
			<digi:trn key="aim:EditParisIndicatorQuestion">
				Edit Paris Indicator Question
			</digi:trn></h5>
		</td>
	</tr>
	<tr bgColor=#ffffff><td height="15" colspan="2"></td></tr>
	<tr bgColor=#ffffff>
		<td height="10" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Question  :
		</td>
		<td height="10" align="left">
			<html:textarea name="aimParisIndicatorManagerForm" property="piQuestionGot" cols="35" rows="2"/>
		</td>
	</tr>
	<tr bgcolor=#ffffff><td height="5"></td></tr>
	<tr bgColor=#ffffff>
		<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Question Number :
		</td>
		<td align="left">
			<html:text name="aimParisIndicatorManagerForm" property="piQuestId" size="3"/>	
		</td>
	</tr>
	<tr bgcolor=#ffffff><td height="5"></td></tr>
	<tr bgColor=#ffffff>
		<td height="10" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Question Type :
		</td>
		<td height="10" align="left">
			Yes/no
			<html:radio disabled="true"   property="piQuestTypeId" value="1"/>&nbsp;&nbsp;&nbsp;&nbsp;
			Calculated
			<html:radio  disabled="true" property="piQuestTypeId" value="2"/>
<%--		<bean:write name="aimParisIndicatorManagerForm" property ="piQuestTypeId"/>--%>
		</td>
	</tr>
	<tr bgcolor=#ffffff><td height="5"></td></tr>	
	<tr bgColor=#ffffff><td height="30" colspan="2"></td></tr>
	<tr bgColor=#dddddb>
		<td bgColor=#dddddb height="25" align="center" colspan="2">
			<input styleClass="dr-menu" type="button" name="addBtn" value="Save" onclick="saveIndicator()">&nbsp;&nbsp;
			<input styleClass="dr-menu" type="button" name="close" value="Close" onclick="closeWindow()">			
		</td>
	</tr>	
</table>
</digi:form>