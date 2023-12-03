<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<DIV id="TipLayer" style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>

<script type="text/javascript">
 function exportXSL(){
        <digi:context name="exportUrl" property="context/ampModule/moduleinstance/exportCalanderManager2XSL.do"/>;
        document.aimFiscalCalendarForm.action="${exportUrl}";
        document.aimFiscalCalendarForm.target="_blank";
        document.aimFiscalCalendarForm.submit();
    }


</script>

<digi:errors/>
<digi:instance property="aimFiscalCalendarForm" />
<digi:context name="digiContext" property="context" />
<digi:form action="/fiscalCalendarManager.do" method="post">

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->


<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center>
  <tr>
    <td width=750 valign=top>
	<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000>
	<tr>
		<td align=left class=r-dotted-lg valign="top" width=750>

			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
						<td align="left">
						<c:set var="translationxls">
							<digi:trn>Export to Excel</digi:trn>
						  </c:set> <c:set var="translationPrinter">
								<digi:trn>Printer Friendly</digi:trn>
							</c:set>
							<div class="toolbar" align="center" style="background: #f2f2f2;">
									<jsp:include
									page="/repository/aim/view/adminXSLExportToolbar.jsp" />
							</div></td>
			</tr>
				<tr>
					<td noWrap width="100%" vAlign="top" align="center">
                      <div>
					<table width="100%" cellspacing="1" cellspacing="1">
					<tr>
						<td noWrap width=600 vAlign="top">
							<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">
								<tr bgColor=#f4f4f2>
									<td vAlign="top" width="100%">&nbsp;</td>
								</tr>
								<tr bgColor=#f4f4f2>
									<td valign="top">
										<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="90%" border="0">
											<tr>
												<td bgColor=#ffffff class=box-border>
													<table border="0" cellpadding="1" cellspacing="1" class=box-border width="100%">
														<tr bgColor=#dddddb>
															<!-- header -->
															<td bgColor=#dddddb height="20" 			align="center" colspan="6"><B>
																<digi:trn key="aim:fiscalCalendars">Fiscal Calendars</digi:trn>
                                                              </b>
														  </td><!-- end header -->
														</tr>
														<!-- Page Logic -->
														<logic:empty name="aimFiscalCalendarForm" property="fiscalCal">
														<tr>
															<td colspan="5">
                                                   				<b><digi:trn key="aim:nofiscalCalendar">No fiscal calendar present</digi:trn></b>
															</td>
														</tr>
														</logic:empty>
														<logic:notEmpty name="aimFiscalCalendarForm" 	property="fiscalCal">
														<tr>
															<td width="100%" class="report">
																<table width="100%" border="0" cellspacing="4"	 bgColor=#f4f4f2>
																<thead>
																	<tr>
																	  <td width="144"><b>
																			<digi:trn key="aim:nameFiscalCalendar">Name</digi:trn></b>
																	  </td>
                                                                        <td nowrap="nowrap" >
                                                                        	<b><digi:trn key="aim:BaseCalendarFiscalCalendar">Base Calendar</digi:trn></b>
																	  	</td>
																	  	<td width="70" nowrap="nowrap" align="center">
																	  		<b><digi:trn key="aim:isFiscalCalendar">Is Fiscal</digi:trn></b>
																	  	</td>
																		<td width="117">
																			<b><digi:trn key="aim:startMonthFiscalCalendar">Start Month</digi:trn></b>
																		</td>
																		<td width="105">
																			<b><digi:trn key="aim:startDayFiscalCalendar">Start Day</digi:trn></b>
																		</td>
																		<td width="245">
																			<b><digi:trn key="aim:offsetFromCurrentYear">Offset (From current year)</digi:trn></b>
																		</td>
																	</tr>
																  </thead>
																		<!--  to export table we are adding class "yui-dt-data" to its tbody-->
															<tbody class="yui-dt-data">
																<logic:iterate name="aimFiscalCalendarForm" property="fiscalCal" id="fiscalCal">
																	<tr>
																		<td height="5" width="593" colspan="4">
                                                              			</td>
																	</tr>
																	<tr>
																	  <td height=20 width="144">
																	  	<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																		<c:set target="${urlParams}" property="fiscalCalId">
																			<bean:write name="fiscalCal" property="ampFiscalCalId" />
																		</c:set>
																		<c:set target="${urlParams}" property="action" value="edit" />
																		<c:set var="translation">
																			<digi:trn key="aim:clickToEditFiscalCalendar">Click here to Edit Fiscal Calendar</digi:trn>
																		</c:set>
																		<digi:link href="/editFiscalCalendar.do" name="urlParams" title="${translation}" >
																			<bean:write name="fiscalCal" property="name"/>
																		</digi:link>
																	  </td>
                                                                      <td nowrap="nowrap">
                                                                      	<bean:write name="fiscalCal" property="baseCal"/>
																	  </td>
																	  <td width="105" align="center">
																	  	<c:if test="${fiscalCal.isFiscal==true}"><digi:trn key="aim:fiscalCalendar:yes">Yes</digi:trn></c:if>
																	  	<c:if test="${fiscalCal.isFiscal==false}"><digi:trn key="aim:fiscalCalendar:no">No</digi:trn></c:if>
																	  </td>
																		<td width="117">
                                                                            <bean:write name="fiscalCal" property="startMonthNum"/>
                                                              			</td>
																		<td width="105">
																			<bean:write name="fiscalCal" property="startDayNum"/>
																		</td>
																		<td width="233">
                                                                 			<bean:write name="fiscalCal" property="yearOffset"/>
																		</td>
																	</tr>
															  </logic:iterate>
																  </tbody>
															  </table>
															</td>
														</tr>
														</logic:notEmpty>
														<!-- end page logic -->
													</table>
												</td>
											</tr>
											<!-- page logic for pagination -->
											<logic:notEmpty name="aimFiscalCalendarForm" property="pages">
											<tr>
												<td colspan="4">
													<digi:trn key="aim:fiscalCalendarPages">
														Pages:</digi:trn>
													<logic:iterate name="aimFiscalCalendarForm" 	property="pages" id="pages" type="java.lang.Integer">
													<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParams1}" property="page"><%=pages%>
													</c:set>
													<c:set var="translation">
														<digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
													</c:set>
													<digi:link href="/fiscalCalendarManager.do" name="urlParams1" title="${translation}" >
														<%=pages%>
													</digi:link> |&nbsp; </logic:iterate>
												</td>
											</tr>
											</logic:notEmpty>
											<!-- end page logic for pagination -->
										</table>
									</td>
								</tr>
							</table>
						</td>
	<td width=20>&nbsp;</td>
    <td width=250 valign=top><table cellpadding="0" cellspacing="0" width="100">
											<tr>
												<td bgColor=#c9c9c7 class=box-title>
													<b style="font-size:12px; color:#000000; padding-left:5px;">
													<digi:trn key="aim:otherLinks">Other links</digi:trn></b>
												</td>
												<td background="ampModule/aim/images/corner-r.gif" 	height="17" width=17>&nbsp;
												
												</td>
											</tr>
										</table>
										<table cellPadding=5 cellspacing="1" width="100%" class="inside">
											<tr>
													<td class="inside">
														<digi:img src="ampModule/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
														<c:set var="translation">
															<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
														</c:set>
														<digi:link href="/admin.do" title="${translation}" >
														<digi:trn key="aim:AmpAdminHome">
														Admin Home
														</digi:trn>
														</digi:link>
													</td>
											</tr>
											<tr>
												<td class="inside">
													<digi:img src="ampModule/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
														<c:set var="translation">
															<digi:trn key="aim:clickToAddFiscalCalendar">Click here to Add Fiscal Calendar</digi:trn>
														</c:set>
														<digi:link href="/editFiscalCalendar.do?action=create"  title="${translation}" >
															<digi:trn key="aim:addNewFiscalCalendar">
																Add a Fiscal Calendar </digi:trn>
														</digi:link>
												</td>
											</tr>
											<!-- end of other links -->
										</table>
										
										</td>
  </tr>
</table>





</digi:form>
