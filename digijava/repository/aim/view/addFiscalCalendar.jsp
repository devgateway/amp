<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">

	function msg() {
		if (confirm("Are you sure about deleting this fiscal calendar ?")) {
			document.aimAddFiscalCalForm.action.value = "delete";
			document.aimAddFiscalCalForm.submit();
		}
		else
			return false;
	}

	function move() {
		<digi:context name="selectLoc" property="context/module/moduleinstance/fiscalCalendarManager.do" />
		url = "<%= selectLoc %>";
		document.location.href = url;
	}

	function check() {

        var sm=document.aimAddFiscalCalForm.startMonthNum.value;
        var sd=document.aimAddFiscalCalForm.startDayNum.value;
        var yr=document.aimAddFiscalCalForm.yearOffset.value;

        var str = document.aimAddFiscalCalForm.fiscalCalName.value;
        str = trim(str);
		if (str.length == 0 || str == null) {
			alert("Please enter name for this Fiscal Calenedar");
			document.aimAddFiscalCalForm.fiscalCalName.focus();
            return false;
        }

        if(sm.match("[^0-9]")){
          alert("Invalid Start Month value");
          document.aimAddFiscalCalForm.startMonthNum.focus();
          return false;
        }else if(sm>12 || sm<1){
          alert("Invalid Start Month value");
          document.aimAddFiscalCalForm.startMonthNum.focus();
          return false;
        }

        if(sd.match("[^0-9]")){
          alert("Invalid Start Day value");
          document.aimAddFiscalCalForm.startDayNum.focus();
          return false;
        }else if(sd>31){
          alert("Invalid Start Day value");
          document.aimAddFiscalCalForm.startDayNum.focus();
          return false;
        }

        if(yr.match("[^0-9]")){
          alert("Invalid Year Offset value");
          document.aimAddFiscalCalForm.yearOffset.focus();
          return false;
        }else if(yr>0){
          alert("Invalid Year Offset value");
          document.aimAddFiscalCalForm.yearOffset.focus();
          return false;
        }

        document.aimAddFiscalCalForm.fiscalCalName.value = str;
        document.aimAddFiscalCalForm.submit();
	}

	function trim ( inputStringTrim ) {
		fixedTrim = "";
		lastCh = " ";
		for (x=0; x < inputStringTrim.length; x++) {
			ch = inputStringTrim.charAt(x);
			if ((ch != " ") || (lastCh != " ")) { fixedTrim += ch; }
				lastCh = ch;
		}
		if (fixedTrim.charAt(fixedTrim.length - 1) == " ") {
			fixedTrim = fixedTrim.substring(0, fixedTrim.length - 1); }
		return fixedTrim
	}

</script>

<digi:instance property="aimAddFiscalCalForm" />
<digi:context name="digiContext" property="context"/>

<digi:form action="/editFiscalCalendar.do" method="post">
<html:hidden property="action" />
<html:hidden property="fiscalCalId" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>

						<bean:define id="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</bean:define>
						<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewFiscalCalendarManager">Click here to view Fiscal Calendar Manager</digi:trn>
						</bean:define>
						<digi:link href="/fiscalCalendarManager.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:fiscalCalendarManager">
						Fiscal Calendar Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<logic:equal name="aimAddFiscalCalForm" property="action" value="create" >
							<digi:trn key="aim:addFiscalCalendar">Add Fiscal Calendar</digi:trn>
						</logic:equal>
						<logic:equal name="aimAddFiscalCalForm" property="action" value="edit" >
							<digi:trn key="aim:editFiscalCalendar">Edit Fiscal Calendar</digi:trn>
						</logic:equal>
                      </span>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<digi:trn key="aim:fiscalCalendarManager">
						Fiscal Calendar Manager
						</digi:trn>
						</span>
						<br>
						<digi:errors/>
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1>
					<tr>
						<td noWrap width=600 vAlign="top">
							<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
								<tr bgColor=#f4f4f2>
									<td vAlign="top" width="100%">
										&nbsp;
									</td>
								</tr>
								<tr bgColor=#f4f4f2>
									<td valign="top">
										<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%" border=0>
											<tr>
												<td bgColor=#ffffff class=box-border>
													<table border=0 cellPadding=1 cellSpacing=1 class=box-border width="100%">
														<tr bgColor=#dddddb>
															<td bgColor=#dddddb height="20" align="center" colspan="5">
																<logic:equal name="aimAddFiscalCalForm" property="action" value="create" >
																	<b><digi:trn key="aim:addFiscalCalendar">Add Fiscal Calendar</digi:trn></b>
																</logic:equal>
																<logic:equal name="aimAddFiscalCalForm" property="action" value="edit" >
																	<b><digi:trn key="aim:editFiscalCalendar">Edit Fiscal Calendar</digi:trn></b>
																</logic:equal>
															</td>
														</tr>
														<!-- Page Logic -->
														<tr>
															<td width="100%">
																<table width="100%" border=0	 bgColor=#f4f4f2>
																	<tr>
																		<td width="30%" align="right">
																		<digi:trn key="aim:fiscalCalendarName">Name</digi:trn>	</td>
																	    <td width="30%" >
																	          <html:text property="fiscalCalName" size="40" />
																	    </td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:fiscalCalendarStMnt">Start Month</digi:trn>
																		</td>
																		<td width="30%">
																           <html:text property="startMonthNum" size="3" />
																		</td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:fiscalCalendarStDay">Start Day</digi:trn>
																		</td>
																		<td width="30%">
																	          <html:text property="startDayNum" size="3" />
																		</td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:fiscalCalendarYrOffset">Year Offset</digi:trn>
																		</td>
																		<td width="30%">
																	          <html:text property="yearOffset" size="3" />
																		</td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:fiscalCalendarDescription">Description</digi:trn>
																		</td>
																	    <td width="30%">
																           <html:textarea property="description" cols="30" rows="3" />
																		</td>
																	</tr>
																	<tr>
																		<td colspan="2" width="60%"  align="center">
																			<table width="100%" cellspacing="5">
																				<tr>
																					<td width="45%" align="right">
																						<input type="button" value="Save" class="dr-menu" onclick="check()">
																					</td>
																					<td width="8%" align="left">
																						<input type="reset" value="Reset" class="dr-menu">
																					</td>
																					<td width="45%" align="left">
																						<input type="button" value="Cancel" class="dr-menu" onclick="move()">
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																	<logic:equal name="aimAddFiscalCalForm" property="flag" value="delete" >
																		<tr>
																			<td colspan="2" width="60%"  align="center">
																				<input type="button" value="Delete this Fiscal Calendar" class="dr-menu" onclick="msg()">
																			</td>
																		</tr>
																	</logic:equal>
																	<logic:equal name="aimAddFiscalCalForm" property="flag" value="orgReferences" >
																		<tr>
																			<td colspan="2" width="60%"  align="center">
																				<b><digi:trn key="aim:cannotDeleteFisCalMsg1"><font color="#FF0000">
																						Can not delete this fiscal calendar since some organization references it !</font>
																					</digi:trn>
																				</b>
																			</td>
																		</tr>
																	</logic:equal>
																</table>
															</td>
														</tr>
													<!-- end page logic -->
													</table>
												</td>
											</tr>

										</table>
									</td>
								</tr>
								<tr>
									<td bgColor=#f4f4f2>
										&nbsp;
									</td>
								</tr>
							</table>
						</td>
						<td noWrap width=100% vAlign="top">
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
