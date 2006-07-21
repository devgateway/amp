<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>


<jsp:include page="teamPagesHeader.jsp" flush="true" />

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=15><span class=crumb>
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
					</td>
				</tr>
				<tr>
					<td height=10 vAlign=center align=center><span class=subtitle-blue>Admin Tools</span>
					</td>
				</tr>
				<tr>
					<td noWrap vAlign="center" align=center>
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
							<tr bgColor=#f4f4f2>
								<td>&nbsp;</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top" align=center>
									<table cellspacing="10">
										<tr>
											<td class=f-names noWrap>
												<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
												<bean:define id="translation">
													<digi:trn key="aim:clickToViewEventTypes">Click here to view Event Type Manager</digi:trn>
												</bean:define>
												<digi:link module="calendar" href="/eventTypes.do" title="<%=translation%>" >
												<digi:trn key="aim:eventTypeManager">Event Type Manager</digi:trn>
												</digi:link>
											</td>
										</tr>
                                        <tr>
											<td class=f-names noWrap>
												<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
												<bean:define id="translation">
													<digi:trn key="aim:clickToViewRegionManager">Click here to view Region Manager</digi:trn>
												</bean:define>
												<digi:link href="/locationManager.do" title="<%=translation%>" >
												<digi:trn key="aim:regionManager">Region Manager</digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td class=f-names noWrap>
											<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
											<bean:define id="translation">
												<digi:trn key="aim:clickToViewStatusManager">Click here to view Status Manager</digi:trn>
											</bean:define>
											<digi:link href="/statusManager.do" title="<%=translation%>" >
											<digi:trn key="aim:statusManager">Status Manager</digi:trn>
											</digi:link>
											</td>
										</tr>
										
										<tr>
											<td class=f-names noWrap>
											<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
											<bean:define id="translation">
												<digi:trn key="aim:clickToViewSectorManager">Click here to view Sector Manager</digi:trn>
											</bean:define>
											<digi:link href="/getSectorSchemes.do" title="<%=translation%>" >
											<digi:trn key="aim:sectorManager">Sector Manager</digi:trn>	
											</digi:link>
											</td>
										</tr>
										
										<tr>
											<td class=f-names noWrap>
											<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
											<bean:define id="translation">
												<digi:trn key="aim:clickToViewOrganizationManager">Click here to view Organization Manager</digi:trn>
											</bean:define>
											<digi:link href="/organisationManager.do?orgSelReset=true" title="<%=translation%>" >
											<digi:trn key="aim:organizationManager">Organization Manager</digi:trn>
											</digi:link>
											</td>
										</tr>
										<tr>
											<td class=f-names noWrap>
											<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
											<bean:define id="translation">
												<digi:trn key="aim:clickToViewProgramManager">Click here to view Program Manager</digi:trn>
											</bean:define>
											<digi:link href="/themeManager.do" title="<%=translation%>" >
											<digi:trn key="aim:programManager">Program Manager</digi:trn>
											</digi:link>
											</td>
										</tr>
										<tr>
											<td class=f-names noWrap>
											<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
											<bean:define id="translation">
												<digi:trn key="aim:clickToViewFiscalCalendarManager">Click here to view Fiscal Calendar Manager</digi:trn>
											</bean:define>
											<digi:link href="/fiscalCalendarManager.do" title="<%=translation%>" >
											<digi:trn key="aim:fiscalCalendarManager">Fiscal Calendar Manager</digi:trn>
											</digi:link>
											</td>
										</tr>
										<tr>
											<td class=f-names noWrap>
											<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
											<bean:define id="translation">
												<digi:trn key="aim:clickToViewCurrencyManager">Click here to view Currency Manager</digi:trn>
											</bean:define>
											<digi:link href="/currencyManager.do" title="<%=translation%>" >
											<digi:trn key="aim:currencyManager">Currency Manager</digi:trn>
											</digi:link>
											</td>
										</tr>
										<tr>
											<td class=f-names noWrap>
											<bean:define id="translation">
												<digi:trn key="aim:clickToViewCurrencyRatesManager">Click here to view Currency Rates Manager</digi:trn>
											</bean:define>											
											<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
											<digi:link href="/showCurrencyRates.do" title="<%=translation%>">
												<digi:trn key="aim:currencyRateManager">Currency Rate Manager</digi:trn>
											</digi:link>
											</td>
										</tr>
										<tr>
											<td class=f-names noWrap>
											<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
											<bean:define id="translation">
												<digi:trn key="aim:clickToViewWorkspaceManager">Click here to view Workspace Manager</digi:trn>
											</bean:define>
											<digi:link href="/workspaceManager.do~page=1" title="<%=translation%>" >
											<digi:trn key="aim:workspaceManager">Workspace Manager</digi:trn>
											</digi:link>
											</td>
										</tr>
									<%--	<tr>
											<td class=f-names noWrap>
											<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
											<bean:define id="translation">
												<digi:trn key="aim:clickToViewReportsManager">Click here to view Reports Manager</digi:trn>
											</bean:define>
											<digi:link href="/reportsManager.do" title="<%=translation%>" >
											<digi:trn key="aim:reportsManager">Reports Manager</digi:trn>
											</digi:link>
											</td>
										</tr>--%>
								<logic:notEmpty name="ME" scope="application">
										<tr>
											<td class=f-names noWrap>
											<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
											<bean:define id="translation">
												<digi:trn key="aim:clickToViewMEIndicatorManager">Click here to view M&E Indicator Manager</digi:trn>
											</bean:define>
											<digi:link href="/indicatorManager.do" title="<%=translation%>" >
												<digi:trn key="aim:meIndicatorManager">
													M & E Indicator Manager
												</digi:trn>
											</digi:link>
											</td>
										</tr>
									</logic:notEmpty>
										<!--paris indicators report manager-->
									<logic:notEmpty name="PI" scope="application">
										<tr>
											<td class=f-names noWrap>
											<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
											<bean:define id="translation">
												<digi:trn key="aim:clickToViewParisIndicatorsManager">Click here to view Indicator Manager</digi:trn>
											</bean:define>
											<digi:link href="/parisIndicatorManager.do" title="<%=translation%>" >
											<digi:trn key="aim:parisIndicatorManager">Paris Indicator Manager</digi:trn>
											</digi:link>
											</td>
										</tr>
									</logic:notEmpty>

										<tr>
											<td class=f-names noWrap>
											<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
											<bean:define id="translation">
												<digi:trn key="aim:clickToViewFeatureManager">Click here to view Feature Manager</digi:trn>
											</bean:define>
											<digi:link href="/featureManager.do" title="<%=translation%>" >
											<digi:trn key="aim:featureManager">Feature Manager</digi:trn>
											</digi:link>
											</td>
										</tr>

										<tr>
											<td class=f-names noWrap>
												<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
												<bean:define id="translation">
													<digi:trn key="aim:clickToViewActivityManager">Click here to view Activity Manager</digi:trn>
												</bean:define>
												<digi:link href="/activityManager.do" title="<%=translation%>" >
												<digi:trn key="aim:activityManager">Activity Manager</digi:trn>	
												</digi:link>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>
								&nbsp;
							</td></tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
