<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/c.tld" prefix="c" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
	<!--
	function validate() 
	{
		if (trim(document.aimParisIndicatorManagerForm.addNewQuestionDescription.value).length == 0) 
		{
			alert("Please enter Indicator Question");
			document.aimParisIndicatorManagerForm.addNewQuestionDescription.focus();
			return false;
		}	
		if (trim(document.aimParisIndicatorManagerForm.addNewQuestionId.value).length == 0) 
		{
			alert("Please enter Indicator code");
			document.aimParisIndicatorManagerForm.addNewQuestionId.focus();
			return false;
		}
		return true;
	}
	
	function saveIndicator()
	{
		<digi:context name="addPIIndNew" property="context/module/moduleinstance/parisIndicatorAddNew.do" />
		document.aimParisIndicatorManagerForm.action = "<%= addPIIndNew%>";
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

<digi:form action="/parisIndicatorManager.do" method="post">
<digi:errors/>

<html:hidden name="aimParisIndicatorManagerForm" property="errorFlag"/>
<digi:errors />

<digi:instance property="aimParisIndicatorManagerForm" />

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="99%" align="center" border="0">
	<tr bgColor="blue"><td height="1" colspan="2"></td></tr>
	<tr bgColor=#dddddb>
		<td bgColor=#dddddb height="15" align="center" colspan="2"><h4>
			ADD PARIS INDICATOR </h4>
		</td>
	</tr>
	<tr bgColor=#ffffff><td height="15" colspan="2"></td></tr>
	<tr bgColor=#ffffff>
		<td height="10" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Indicator Name  :
		</td>
		<td height="10" align="left">
				<html:textarea name ="aimParisIndicatorManagerForm" property="addNewIndicatorText" cols="35" rows="2"/>
		</td>
	</tr>
	<tr bgcolor=#ffffff><td height="5"></td></tr>
	<tr bgColor=#ffffff>
		<td height="10" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Indicator Code :
		</td>
		<td height="10" align="left">
				<html:text name ="aimParisIndicatorManagerForm" property="addNewIndicatorCode" size="5"/>
		</td>
	</tr>
	<tr bgColor=#ffffff><td height="15" colspan="2"></td></tr>
	<tr bgColor=#ffffff>
		<td height="10" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Question  :
		</td>
		<td height="10" align="left">
				<html:textarea name ="aimParisIndicatorManagerForm" property="piQuestionGot" cols="35" rows="2"/>
		</td>
	</tr>
	<tr bgcolor=#ffffff><td height="5"></td></tr>
	<tr bgColor=#ffffff>
		<td height="10" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Question Number :
		</td>
		<td height="10" align="left">
				<html:text name ="aimParisIndicatorManagerForm" property="piQuestId" size="5"/>
		</td>
	</tr>
	<tr bgcolor=#ffffff><td height="5"></td></tr>
	<tr bgColor=#ffffff>
		<td height="10" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Question Type :
		</td>
		<td height="10" align="left">
			
				Yes no
				<html:radio name ="aimParisIndicatorManagerForm" property="piQuestTypeId" value="1"/>&nbsp;&nbsp;&nbsp;&nbsp;
				Calculated
				<html:radio name ="aimParisIndicatorManagerForm" property="piQuestTypeId" value="2"/>
			
		</td>
	</tr>
	<tr bgColor=#dddddb>														
		<td bgColor=#dddddb height="20" align="center" colspan="5"><B>
			<html:button  styleClass="dr-menu" property="addBtn"  onclick="saveIndicator()">
				<digi:trn key="btn:save">Save</digi:trn> 
			</html:button>
			<html:button  styleClass="dr-menu" property="close"  onclick="closeWindow()">
				<digi:trn key="btn:close">Close</digi:trn> 
			</html:button>
		</td>
	</tr>
	
</table>
</digi:form>