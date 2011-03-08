<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript">
<!--

	function load() {
		document.getElementById('phyProgTitle').focus();
	}

	function unload() {
	}

	function closeWindow() {
		window.close();
	}
-->
</script>

<digi:instance property="aimEditActivityForm" />
<%--
<digi:form action="/phyProgSelected.do" method="post"> --%>

<form name="addPhysicalProgressForm" action="/phyProgSelected.do" method="post">

<html:hidden property="phisycalProgress.phyProgReset" value="false"/>
<input type="hidden" name="selectedDate">
<html:hidden property="phisycalProgress.phyProgId"/>
<html:hidden property="components.componentId"/>

<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border="0">
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left valign="top">
					<table bgcolor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:addPhysicalProgress">
								Add Physical Progress</digi:trn>
							</td></tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table cellSpacing=2 cellPadding=2>
									<tr>
										<td>
											<div title="<digi:trn key="aim:TitleForPhysicalActivity">Title of the physical activity</digi:trn>">
											<FONT color=red>*</FONT>
											<digi:trn key="aim:title">Title</digi:trn>
											</div>
										</td>
										<td>
											<div title="<digi:trn key="aim:TitleForPhysicalActivity">Title of the physical activity</digi:trn>">
											<html:textarea property="phisycalProgress.phyProgTitle" cols="50" rows="1" styleClass="inp-text" tabindex="1" styleId="phyProgTitle"/>
											</div>
										</td>
									</tr>
									<tr>
										<td>
										<div title="<digi:trn key="aim:ComponentDescribe">Descriptive text as to the component objectives and tasks</digi:trn>">
										<digi:trn key="aim:description">Description</digi:trn>
										</div>
										</td>
										<td>
											<div title="<digi:trn key="aim:ComponentDescribe">Descriptive text as to the component objectives and tasks</digi:trn>">
											<html:textarea property="phisycalProgress.phyProgDesc" cols="50" rows="4" styleClass="inp-text" tabindex="2"/>
											</div>
										</td>
									</tr>
									<tr>
										<td>
											<div title="<digi:trn key="aim:DateofReporting">Date the activity was initiated</digi:trn>">
											<FONT color=red>*</FONT>
											<digi:trn key="aim:reportingDate">Reporting Date</digi:trn></div>
										</td>
										<td>
											<table cellspacing="0" cellpadding="0" vAlign="top" align="left" border="0">
												<tr>
													<td>
														<html:text property="phisycalProgress.phyProgRepDate" readonly="true" size="10" styleClass="inp-text"
														tabindex="7" styleId="phyProgRepDate"/>
													</td>
													<td>
														&nbsp;
														<a id="date1" href='javascript:pickDate("date1",document.getElementById("phyProgRepDate"))'>
															<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
														</a>
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td align="center" colspan=2>
											<table cellPadding=5>
												<tr>
													<td>
														<input type="button" value="<digi:trn key='btn:add'>Add</digi:trn>" class="dr-menu" onclick="addPhysicalProgress()"
														tabindex="4">
													</td>
													<td>
														<input type="reset" value="<digi:trn key='btn:clear'>Clear</digi:trn>" class="dr-menu" tabindex="5">
													</td>
													<td>
														<input type="button" value="<digi:trn key='btn:close'>Close</digi:trn>" class="dr-menu" onclick="closePopup()"
														tabindex="6">
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
</form>
