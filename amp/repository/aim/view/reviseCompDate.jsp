<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp"  />
</script>
<digi:instance property="aimEditActivityForm" />

<script language="JavaScript">
	function revised() {
		if (document.aimEditActivityForm.revisedCompDate.value == "") {
			alert("Please enter the revised date");
			return false;
		}
		document.aimEditActivityForm.target = window.opener.name;
	   document.aimEditActivityForm.submit();
		window.close();
		return true;
	}

	function load() {
	}

	function unload() {
		window.opener.document.aimEditActivityForm.currUrl.value="";
	}
</script>

<digi:form action="/compDateRevised.do">

<input type="hidden" name="selectedDate" value="">
<input type="hidden" name="edit" value="true">
<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
	<tr>
		<td align=left valign="top">
			<table bgcolor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>
				<tr bgcolor="#006699">
					<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
						<digi:trn key="aim:reviseCompletionDate">Revise Completion Date</digi:trn>
					</td></tr>
				<tr>
					<td align="center" bgcolor=#ECF3FD>
						<table border="0" cellpadding="5" cellspacing="5" width="100%">
							<tr>
								<td align="right" valign="middle" width="50%">
									<digi:trn key="aim:enterRevisedCompDate">Enter Revised Completion date
									</digi:trn>
								</td>								
								<td align="left" valign="middle">
									<table cellpadding="0" cellspacing="0">
										<tr>
											<td>
												<html:text name="aimEditActivityForm" property="revisedCompDate" 
												styleId="revisedCompDate" size="10" styleClass="inp-text" readonly="true"/>
											</td>
											<td align="left" vAlign="center">&nbsp;
												<a id="date1" href='javascript:pickDate("date1",document.aimEditActivityForm.revisedCompDate)'>
													<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
												</a>
											</td>
										</tr>
									</table>									
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<table width="100%" cellpadding="3" cellspacing="3" border="0">
										<tr>
											<td align="right">
												<input type="button" value="Revise" onclick="return revised()" style="dr-menu">
											</td>
											<td align="left">
												<input type="button" value="Close" onclick="window.close()" style="dr-menu">
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
