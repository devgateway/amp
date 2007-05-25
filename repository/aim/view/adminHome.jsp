<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=757 align="left">
  <tr>
    <td class=r-dotted-lg width=5>&nbsp;</td>
    <td align=left class=r-dotted-lg vAlign=top width=750>
      <table cellPadding=5 cellSpacing=0 width="100%">
        <tr>
          <td height=15>
            <span class=crumb>
              <digi:trn key="aim:AmpAdminHome">
              Admin Home
              </digi:trn>
            </span>
          </td>
        </tr>
        <tr>
          <td height=10 vAlign="middle" align=center>
            <span class=subtitle-blue>
            Admin Tools
            </span>
          </td>
        </tr>
        <tr>
          <td noWrap vAlign="top" align="center">
            <table bgColor=#ffffff cellPadding=20 cellSpacing=0 class=box-border-nopadding width="100%">
              <tr align="center" bgcolor="#E1E1E1" vAlign="top">
                <td class="f-names" nowrap="nowrap">
                  <table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="300" >
                    <tr bgcolor="#B7B7B7" align="center">
                      <td class=f-names noWrap>
                      <digi:trn key="aim:administrative">
                      Administrative
                      </digi:trn>
                      </td>
                    </tr>
                    <tr align="center" bgcolor="#f4f4f2" height="120" valign="top">
                      <td class="f-names">
                        <table cellPadding=0 cellSpacing=0>
                          <tr>
                            <td class=f-names noWrap>
                              <digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="trnEventTypes">
                                <digi:trn key="aim:clickToViewEventTypes">Click here to view Event Type Manager</digi:trn>
                              </c:set>
                              <digi:link module="calendar" href="/eventTypes.do" title="${trnEventTypes}" >
                                <digi:trn key="aim:eventTypeManager">Event Type Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          <tr>
                            <td class=f-names noWrap>
                              <digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="trnFeatureManager">
                                <digi:trn key="aim:clickToViewFeatureManager">Click here to view Feature Manager</digi:trn>
                              </c:set>
                              <digi:link href="/featureManager.do" title="${trnFeatureManager}" >
                                <digi:trn key="aim:featureManager">Feature Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          <tr>
                            <td class=f-names noWrap>
                              <digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="trnUploadFlags">
                                <digi:trn key="aim:clickToUploadFlags">Click here to upload and select flags</digi:trn>
                              </c:set>
                              <digi:link href="/flagUploader.do" title="${trnUploadFlags}" >
                                <digi:trn key="aim:flagUploaderSelector">
                                Flag uploader/selector
                                </digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          <tr>
                            <td class=f-names noWrap>
                              <digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="trnSystemSettings">
                                <digi:trn key="aim:clickToViewSystemSettings">Click here to view System Settings</digi:trn>
                              </c:set>
                              <digi:link href="/GlobalSettings.do" title="${trnSystemSettings}" >
                                <digi:trn key="aim:GlobalSettings">Global Settings</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          <tr>
                            <td class=f-names noWrap>
                              <digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="trnTranslationManager">
                                <digi:trn key="aim:clickToViewTranslationManager">Click here to view Translation Manager</digi:trn>
                              </c:set>
                              <digi:link href="/translationManager.do" title="${trnTranslationManager}" >
     				                <digi:trn key="aim:translationManager">Translation Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          
                          <tr>
                            <td class=f-names noWrap>
                              <digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="trnWorkspaceManager">
                                <digi:trn key="aim:clickToViewWorkspaceManager">Click here to view Workspace Manager</digi:trn>
                              </c:set>
                              <digi:link href="/workspaceManager.do~page=1" title="${trnWorkspaceManager}" >
                                <digi:trn key="aim:workspaceManager">Workspace Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          
                          <tr>
                            <td class=f-names noWrap>
                              <digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
                              <bean:define id="translation">
                                <digi:trn key="aim:clickToViewCategoryManager">Click here to view Category Manager</digi:trn>
                              </bean:define>
                              <digi:link href="/categoryManager.do" title="<%=translation%>" >
                                <digi:trn key="aim:categoryManager">Category Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
                <td>
                  <table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="300">
                    <tr bgcolor="#B7B7B7" align="center">
                      <td class=f-names noWrap>
                      <digi:trn key="aim:dataFields">
                      Data Fields
                      </digi:trn>
                      </td>
                    </tr>
                    <tr align="center" bgcolor="#f4f4f2" height="120" valign="top">
                      <td class="f-names">
                        <table cellPadding=0 cellSpacing=0>
                          <tr>
                            <td class=f-names noWrap>
                              <digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
                              <bean:define id="translation">
                                <digi:trn key="aim:clickToViewFiscalCalendarManager">Click here to view Fiscal Calendar Manager</digi:trn>
                              </bean:define>
                              <digi:link href="/fiscalCalendarManager.do" title="<%=translation%>" >
                                <digi:trn key="aim:CalendarManager">Calendar Manager</digi:trn>
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
                                <digi:trn key="aim:clickToViewStatusManager">Click here to view Status Manager</digi:trn>
                              </bean:define>
                              <digi:link href="/statusManager.do" title="<%=translation%>" >
                                <digi:trn key="aim:statusManager">Status Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>

              <tr align="center" bgcolor="#E1E1E1" valign="top">
                <td>
                  <table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="300">
                    <tr bgcolor="#B7B7B7" align="center">
                      <td class=f-names noWrap>
                      <digi:trn key="aim:programMonitoring">
                      Program Monitoring
                      </digi:trn>
                      </td>
                    </tr>
                    <tr bgcolor="#f4f4f2" align="center" height="80" valign="top">
                      <td class="f-names">
                        <table cellPadding=0 cellSpacing=0>
                          <tr>
                            <td class=f-names noWrap>
                              <digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
                              <bean:define id="translation">
                                <digi:trn key="aim:clickToViewcomponentsManager">Click here to view Components Manager</digi:trn>
                              </bean:define>
                              <digi:link href="/getComponents.do" title="<%=translation%>" >
                                <digi:trn key="aim:componentsManager">Components Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>

                          <tr>
                            <td class=f-names noWrap>
                              <digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
                              <bean:define id="translation">
                                <digi:trn key="aim:clickToViewIndicatorManager">Click here to view Indicator Manager</digi:trn>
                              </bean:define>
                              <digi:link href="/overallIndicatorManager.do" title="<%=translation%>" >
                                <digi:trn key="aim:ProgramProjectManager">
                                Program/Project Indicator Manager
                                </digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          <tr>
                            <td class=f-names noWrap>
                              <digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
                              <bean:define id="translation">
                                <digi:trn key="aim:clickToViewProgramTypeManager">Click here to view Program Type Manager</digi:trn>
                              </bean:define>
                              <digi:link href="/programTypeManager.do" title="<%=translation%>" >
                                <digi:trn key="aim:ProgramTypeManager">
                                Program Type Manager
                                </digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
                <td>
                  <table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="300" title="">
                    <tr bgcolor="#B7B7B7" align="center">
                      <td class=f-names noWrap align="center">
                      <digi:trn key="aim:projectsActivities"> Projects / Activites</digi:trn>
                      </td>
                    </tr>
                    <tr bgcolor="#f4f4f2" align="center" height="80" valign="top">
                      <td>
                        <table cellPadding=0 cellSpacing=0>
                          <tr>
                            <td class=f-names noWrap>
                              <digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="trnProgramTypeManager">
                                <digi:trn key="aim:clickToViewProgramTypeManager">Click here to view Program Type Manager</digi:trn>
                              </c:set>
                              <digi:link href="/programTypeManager.do" title="${trnProgramTypeManager}" >
                                <digi:trn key="aim:ProgramTypeManager">
                                Program Type Manager
                                </digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          <tr>
                            <td class=f-names noWrap>
                              <digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="trnActivityManager">
                                <digi:trn key="aim:clickToViewActivityManager">Click here to view Activity Manager</digi:trn>
                              </c:set>
                              <digi:link href="/activityManager.do" title="${trnActivityManager}" >
                                <digi:trn key="aim:activityManager">Activity Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          <tr>
                            <td class=f-names noWrap>
                              <digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
                              <c:set var="trnOrganizationManager">
                                <digi:trn key="aim:clickToViewOrganizationManager">Click here to view Organization Manager</digi:trn>
                              </c:set>
                              <digi:link href="/organisationManager.do?orgSelReset=true" title="${trnOrganizationManager}" >
                                <digi:trn key="aim:organizationManager">Organization Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
            </table>
            <br />
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
