<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
<!--

	function validate() {
		if(isEmpty(document.aimEditActivityForm.issue.value) == true) {
			alert("Please enter issue");			
			document.aimEditActivityForm.issue.focus();
			return false;
		}
		return true;
	}

	function addIssue() {
		var flag = validate();
		if (flag == true) {
			document.aimEditActivityForm.target = window.opener.name;
			document.aimEditActivityForm.submit();
			window.close();
			return flag;
		} else {
			return false;
		}
	}

	function load() {}

	function unload() {}

-->
</script>

<digi:instance property="aimEditActivityForm" />

<digi:form action="/addIssue.do" method="post">

<html:hidden property="issueId"/>
<html:hidden property="event"/>
<html:hidden property="edit" value="true"/>
<html:hidden property="editAct"/>

<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left vAlign=top>
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:addIssue">Add Issue</digi:trn>
							</td>
						</tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table cellSpacing=2 cellPadding=2>
									<tr>
										<td valign="top">
											<digi:trn key="aim:issue">Issue</digi:trn>
										</td>
										<td valign="top">
											 <a title="<digi:trn key="aim:issuesForTheActivity">The issues for the activity</digi:trn>">
												<html:textarea property="issue" styleClass="inp-text" rows="3" cols="60"/>
											 </a>
										</td>
									</tr>								
									<tr>
										<td align="center" colspan=2>
											<table cellPadding=5>
												<tr>
													<td>
														<input type="button" value="Add" class="dr-menu" onclick="return addIssue()">
													</td>
													<td>
														<input type="reset" value="Clear" class="dr-menu">
													</td>
													<td>
														<input type="button" value="Close" class="dr-menu" onclick="window.close()">
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
	</td></tr>
</table>
</digi:form>
