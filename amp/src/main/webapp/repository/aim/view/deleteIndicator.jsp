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
	function load()	{}

	function unload() {}

	function deleteMEIndicator(id)
	{
		<digi:context name="delInd" property="context/module/moduleinstance/deleteIndicator.do"/>
		document.aimIndicatorForm.action = "<%= delInd %>?id="+id;
		document.aimIndicatorForm.target = window.opener.name;
		document.aimIndicatorForm.submit();
		window.close();
	}

	function closeWindow() 
	{
		window.close();
	}
	-->
</script>

<digi:instance property="aimIndicatorForm" />
<digi:form action="/deleteIndicator.do" method="post">

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="100%" align="center" border="0">
	<tr bgColor="blue"><td height="1" colspan="2"></td></tr>
	<tr bgColor=#dddddb>
		<td bgColor=#dddddb height="15" align="center" valign="center" colspan="2"><h4>
			Monitoring and Evaluation : Deleting an Indicator</h4>
		</td>
	</tr>
		<table width="100%" cellspacing="0" cellpadding="1" valign="top" align=left>
			<tr bgColor=#f4f4f4><td height="1"></td></tr>
			<tr><td bgColor=#d7eafd class=box-title height="20" align="center"><b>
				<bean:write name="aimIndicatorForm" property="indicatorName"/></b>
			</td></tr>
			<tr bgColor=#f4f4f4><td height="1"></td></tr>
			<tr bgColor=#ffffff><td height="10"></td></tr>	
			<tr><td>
				<table width="100%" cellspacing="1" cellpadding=4 valign="top" align=left bgcolor="#ffffff">
					<tr><td>
						<table width="100%" cellspacing="1" cellpadding="1" bgcolor="#d7eafd">
							<logic:notEmpty name="aimIndicatorForm" property="meIndActList">
							<tr bgColor="red"><td height="1" colspan="3"></td></tr>
							<tr bgColor=#ffffff>
								<td bgColor=#ffffff height="15" align="center" colspan="3">
									<font color="red"> 
										The Indicator is being used by the following activities, hence cannot be deleted.
									</font>
								</td>
							</tr>
							<tr bgColor="red"><td height="1" colspan="3"></td></tr>
							<tr bgcolor="#ffffff">
								<td width="9"><b>
									No.</b>
								</td>																	
								<td align="left"><b>
									Activity name</b>
								</td>
								<td align="left"><b>
									Amp ID</b>
								</td>																	
							</tr>
							<% int count=1; %>
							<logic:iterate name="aimIndicatorForm" property="meIndActList" id="meIndActList" type="org.digijava.module.aim.dbentity.AmpActivity">
							<tr bgColor=#ffffff>
								<td width="9">
									<%	out.print(count++);	%>.
								</td>
								<td align="left">
									<bean:write name="meIndActList" property="name"/>
								</td>
								<td align="left">
									<bean:write name="meIndActList" property="ampId"/>
								</td>
							</tr>
							</logic:iterate>
							<tr bgColor=#ffffff>
							<td bgColor=#00FFFF height="15" align="center" colspan="3">
								<html:button styleClass="dr-menu" property="closeButton" onclick="closeWindow()">
									Close
								</html:button>
							</td>
							</tr>
							</logic:notEmpty>
							<logic:empty name="aimIndicatorForm" property="meIndActList">
							<tr bgColor="blue"><td height="1" colspan="3"></td></tr>
							<tr bgColor=#ffffff>
								<td bgColor=#ffffff height="15" align="center" colspan="3">
									<font color="blue"> 
										The indicator is not assigned to any activity, hence can be deleted.
									</font>
								</td>
							</tr>
							<tr bgColor="blue"><td height="1" colspan="3"></td></tr>
							<tr bgColor=#ffffff>
								<td bgColor=#00FFFF align="right">
									<a href="javascript:deleteMEIndicator('<bean:write name="aimIndicatorForm" property="indId"/>')">
										<html:button property="deleteButton" value="Delete"/>
									</a>
								</td>
								<td bgColor=#00FFFF align="left">
									<html:button styleClass="dr-menu" property="closeButton" onclick="closeWindow()">
										Close
									</html:button>
								</td>
							</tr>
							</logic:empty>
						</table>
					</td></tr>
				</table>
			</td></tr>
		</table>
</table>
</digi:form>
