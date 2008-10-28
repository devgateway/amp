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
<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>

<script language="JavaScript">
<!--

	function addPhysicalProgress()
	{
		var titleFlag = isEmpty(document.aimEditActivityForm.phyProgTitle.value);
		var dateFlag = isEmpty(document.aimEditActivityForm.phyProgRepDate.value);
		if(titleFlag == true & dateFlag == true)
		{
			alert("Please enter Title and Reporting Date");
			document.aimEditActivityForm.phyProgTitle.focus();
		}
		else
		{
			if(titleFlag == true)
			{
				alert(" Please enter title");
				document.aimEditActivityForm.phyProgTitle.focus();
			}
			if(dateFlag == true)
			{
				alert(" Please enter Reporting Date");
				document.aimEditActivityForm.phyProgRepDate.focus();
			}
		}
		if(titleFlag == false && dateFlag == false)
		{
			<digi:context name="addPhyProg" property="context/module/moduleinstance/phyProgSelected.do?edit=true"/>
		   document.aimEditActivityForm.action = "<%= addPhyProg %>";
			document.aimEditActivityForm.target = window.opener.name;
		   document.aimEditActivityForm.submit();
			window.close();
		}
	}

	function load() {
		document.aimEditActivityForm.phyProgTitle.focus();
	}

	function unload() {
	}

	function closeWindow() {
		window.close();
	}
-->
</script>

<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<digi:instance property="aimEditActivityForm" />
<%--
<digi:form action="/phyProgSelected.do" method="post"> --%>

<form name="aimEditActivityForm" action="/phyProgSelected.do" method="post">

<html:hidden property="phyProgReset" value="false"/>

<input type="hidden" name="selectedDate">
<html:hidden property="phyProgId"/>
<html:hidden property="componentId"/>

<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left vAlign=top>
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:add</a>PhysicalProgress">
								Add Physical Progress</digi:trn>
							</td></tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table cellSpacing=2 cellPadding=2>
									<tr>
										<td>
											<FONT color=red>*</FONT>
											<a title="<digi:trn key="aim:TitleForPhysicalActivity">Title of the physical activity</digi:trn>">
											<digi:trn key="aim:title">Title</digi:trn>
											</a>
										</td>
										<td>
											<a title="<digi:trn key="aim:TitleForPhysicalActivity">Title of the physical activity</digi:trn>">
											<html:textarea property="phyProgTitle" cols="50" rows="1" styleClass="inp-text" tabindex="1"/>
											</a>
										</td>
									</tr>
									<tr>
										<td>
										<a title="<digi:trn key="aim:ComponentDescribe">Descriptive text as to the component objectives and tasks</digi:trn>">
										<digi:trn key="aim:description">Description</digi:trn>
										</a>
										</td>
										<td>
											<a title="<digi:trn key="aim:ComponentDescribe">Descriptive text as to the component objectives and tasks</digi:trn>">
											<html:textarea property="phyProgDesc" cols="50" rows="4" styleClass="inp-text" tabindex="2"/>
											</a>
										</td>
									</tr>
									<tr>
										<td>
											<FONT color=red>*</FONT>
											<a title="<digi:trn key="aim:DateofReporting">Date the activity was initiated</digi:trn>">
											<digi:trn key="aim:reportingDate">Reporting Date</digi:trn></a>
										</td>
										<td>
											<table cellSpacing=0 cellPadding=0 vAlign="top" align="left" border=0>
												<tr>
													<td>
														<html:text property="phyProgRepDate" readonly="true" size="10" styleClass="inp-text"
														tabindex="7" styleId="phyProgRepDate"/>
													</td>
													<td>
														&nbsp;
														<a id="date1" href='javascript:pickDate("date1",document.aimEditActivityForm.phyProgRepDate)'>
															<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
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
														<input type="button" value="<digi:trn key='btn:close'>Close</digi:trn>" class="dr-menu" onclick="closeWindow()"
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
