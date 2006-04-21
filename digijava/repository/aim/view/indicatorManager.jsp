<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">
	<!--
		function addingIndicators()
		{
			openNewWindow(500, 300);
			<digi:context name="addIndicator" property="context/module/moduleinstance/addIndicator.do?create=true" />
			document.aimIndicatorForm.currUrl.value = "<%= addIndicator %>";
			document.aimIndicatorForm.action = "<%= addIndicator %>";
			document.aimIndicatorForm.target = popupPointer.name;
			document.aimIndicatorForm.submit();
			return true;			
		}
		function deleteIndicator()
		{
			return(confirm(" Do you want to delete the Indicator ? Please check whether the indicator is being used by some Activity."));
		}
	-->
</script>

<digi:errors/>
<digi:instance property="aimIndicatorForm" />
<digi:form action="/indicatorManager.do" method="post">

<digi:context name="digiContext" property="context" />
<input type="hidden" name="currUrl">
<input type="hidden" name="create">

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<%-- Start Navigation --%>
					<td height=33><span class=crumb>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</bean:define>
						<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:AmpAdminHome">
							Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:indicatorManager">
							Indicator Manager
						</digi:trn>
					</td>
					<%-- End navigation --%>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<digi:trn key="aim:indicatorManager">
							Indicator Manager
						</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1>
					<tr><td noWrap width=600 vAlign="top">
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
														<%-- header --%>
														<td bgColor=#dddddb height="20" align="center" colspan="5"><B>
															<digi:trn key="aim:indicatorList">
															Indicator List
															</digi:trn></B>
														</td>
														<%-- end header --%>
													</tr>
													
													<logic:notEmpty name="aimIndicatorForm" property="indicators">
														<tr><td>
															<table width="100%" bgColor=#f4f4f2 cellspacing=2 cellpadding=1>
																<tr><td colspan="2"><b>
																	Monitoring and Evaluation : AMP Indicators</b>
																</td></tr>
																<logic:iterate name="aimIndicatorForm" property="indicators" id="indicators"
																type="org.digijava.module.aim.helper.AmpMEIndicatorList">	

																	<tr>
																	<td width="100%">
																		<bean:write name="indicators" property="name"/>&nbsp;&nbsp;(
																							 <bean:write name="indicators" property="code"/>)
																	</td>
																	<td align="left" width="12">
																		<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																		<c:set target="${urlParams}" property="id">
																			<bean:write name="indicators" property="ampMEIndId" />
																		</c:set>
																		<c:set target="${urlParams}" property="action" value="delete"/>
																		<bean:define id="translation">
																			<digi:trn key="aim:clickToDeleteIndicator">Click here to Delete Indicator</digi:trn>
																		</bean:define>
																		<digi:link href="/indicatorManager.do" name="urlParams" 
																		onclick=" return deleteIndicator()" title="<%=translation%>" >
																		<img src= "../ampTemplate/images/trash_12.gif" border=0>
																		</digi:link>
																	</td>
																	</tr>
																</logic:iterate>
															</table>
														</td></tr>
													</logic:notEmpty>
													<logic:empty name="aimIndicatorForm" property="indicators">
														<tr align="center"><td><b>
															No indicators present</b></td>
														</tr>
													</logic:empty>
													
													<tr bgColor=#dddddb>
														<%-- Add Indicator Button --%>
														<td bgColor=#dddddb height="20" align="center" colspan="5"><B>
															<input class="buton" type="button" name="addIndicator" value="Add a New Indicator" onclick="addingIndicators()">
														</td>
													</tr>
													<%-- Page Logic --%>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td></tr>
					</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</digi:form>
