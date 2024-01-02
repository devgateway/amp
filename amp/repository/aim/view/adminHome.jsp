<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<jsp:include page="teamPagesHeader.jsp"  />
<jsp:include page="allVisibilityTags.jsp" />

<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="1000" align="center">
  <tr>
    <td align="left" class="r-dotted-lg" vAlign="top">
      <table cellPadding="5" cellSpacing="0" width="100%">
        <tr>
          <td style="white-space:normal" vAlign="top" align="center">

            <fieldset style="background-color:#F2F2F2;position:relative;padding-top:25px;">
              <legend class="admtoolsttl"><span class="legend_label"><digi:trn>Administrative Tools</digi:trn></span></legend>
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="admin_landing_page_tbl" style="table-layout:fixed">
                <tr>
                  <td width=33% class="admin_landing_page_box" valign=top>
                    <div class="admin_landing_page_box_cont">
                      <div class="admin_landing_page_box_title"><digi:trn>Global administration and maintenance of AMP</digi:trn></div>
                      <table border="0" cellspacing="2" cellpadding="2">
                        <module:display name="Global Settings" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="trnSystemSettings">
                                <digi:trn invisibleLinks="true">Click here to view System Settings</digi:trn>
                              </c:set>
                              <digi:link href="/GlobalSettings.do" title="${trnSystemSettings}" >
                                <digi:trn>Global Settings</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                        <module:display name="Global Permission Manager" parentModule="ADMINISTRATIVE SECTION">
                        <tr>
                          <c:set var="gatePermLink">
                            <digi:trn invisibleLinks="true">Click here to manage gate permissions and assignments</digi:trn>
                          </c:set>
                          <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                          <td class="admin_box_label">
                            <html:link href="/gateperm/managePermMap.do" title="${gatePermLink}">
                              <digi:trn>Global permission Manager</digi:trn>
                            </html:link>
                          </td>
                        </tr>
                        <tr>
                          <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                          <td class="admin_box_label">
                            <c:set var="permManagerLink">
                              <digi:trn key="aim:permManagerLinkTitle" invisibleLinks="true">Click here to manage permissions</digi:trn>
                            </c:set>
                            <html:link href="/wicket/permmanager/" title="${permManagerLink}">
                              <digi:trn key="aim:permissionManager">Permission Manager</digi:trn>
                            </html:link>
                          </td>
                        <tr>
                          </module:display>
                          <module:display name="Feature Manager" parentModule="ADMINISTRATIVE SECTION">
                        <tr>
                          <td>
                            <img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif" alt="arrow-image">
                          </td>
                          <td class="admin_box_label">
                            <c:set var="trnFeatureManager">
                              <digi:trn invisibleLinks="true">Click here to access Feature Manager</digi:trn>
                            </c:set>
                            <digi:link href="/visibilityManager.do" title="${trnFeatureManager}" >
                              <digi:trn>Global feature Manager</digi:trn>
                            </digi:link>
                          </td>
                        </tr>
                        </module:display>
                        <module:display name="Quartz Job Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="trn">
                                <digi:trn invisibleLinks="true">Click here to view Job Manager</digi:trn>
                              </c:set>
                              <digi:link module="aim" href="/quartzJobManager.do?action=all" title="${trn}">
                                <digi:trn>Job Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>

                        <module:display name="Flag uploader" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="trnUploadFlags">
                                <digi:trn invisibleLinks="true">Click here to upload and select flags</digi:trn>
                              </c:set>
                              <digi:link href="/flagUploader.do" title="${trnUploadFlags}" >
                                <digi:trn>Flag uploader/selector</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                        <module:display name="Data Freeze Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="trnDataFreezeManager">
                                <digi:trn invisibleLinks="true">Click here to access Data Freeze Manager</digi:trn>
                              </c:set>                             
                              <a href="/TEMPLATE/reamp/modules/admin/data-freeze-manager/index.html"
                                 title="${trnDataFreezeManager}">
                                <digi:trn>Data Freeze Manager</digi:trn>
                              </a>
                            </td>
                          </tr>
                        </module:display>                        
                        
                        <module:display name="Project Performance Alerts Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                                <c:set var="trnProjectPerformanceAlertsManager">
                                    <digi:trn invisibleLinks="true">Click here to access Project Performance Alerts Manager</digi:trn>
                                </c:set>
                                <a href="/TEMPLATE/reamp/modules/admin/performance-alert-manager/index.html" 
                                title="${trnProjectPerformanceAlertsManager}">
                                    <digi:trn>Project Performance Alerts Manager</digi:trn>
                                </a>
                            </td>
                          </tr>
                        </module:display>
                      </table>
                    </div></td>
                  <td width=10>&nbsp;</td>
                  <td width=33% class="admin_landing_page_box" valign=top>
                    <div class="admin_landing_page_box_cont">
                      <div class="admin_landing_page_box_title"><digi:trn>User management</digi:trn></div>
                      <table border="0" cellspacing="2" cellpadding="2">
                        <module:display name="Workspace Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td>
                              <img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif">
                            </td>
                            <td class="admin_box_label">
                              <c:set var="trnWorkspaceManager">
                                <digi:trn invisibleLinks="true">Click here to view Workspace Manager</digi:trn>
                              </c:set>
                              <digi:link href="/workspaceManager.do~page=1~reset=true" title="${trnWorkspaceManager}" >
                                <digi:trn>Workspace Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                        <module:display name="User Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to view User Manager</digi:trn>
                              </c:set>
                              <digi:link module="um" href="/viewAllUsers.do?reset=true" title="${translation}">
                                <digi:trn>User Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                      </table>

                    </div></td>
                  <td width=11>&nbsp;</td>
                  <td width=33% class="admin_landing_page_box" valign=top>
                    <div class="admin_landing_page_box_cont">
                      <div class="admin_landing_page_box_title"><digi:trn>Backbone lists of AMP</digi:trn></div>
                      <table border="0" cellspacing="2" cellpadding="2">
                        <module:display name="Category Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to view Category Manager</digi:trn>
                              </c:set>
                              <digi:link href="/categoryManager.do" title="${translation}" contextPath="/categorymanager" >
                                <digi:trn>Category Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                        <module:display name="Dynamic Region Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to view Region Manager</digi:trn>
                              </c:set>
                              <digi:link href="/dynLocationManager.do" title="${translation}" >
                                <digi:trn>Region Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                        <module:display name="Sector Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to view Sector Manager</digi:trn>
                              </c:set>
                              <digi:link href="/getSectorSchemes.do" title="${translation}">
                                <digi:trn>Sector Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                        <module:display name="Fiscal Calendar Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to view Fiscal Calendar Manager</digi:trn>
                              </c:set>
                              <digi:link href="/fiscalCalendarManager.do" title="${translation}" >
                                <digi:trn>Calendar Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                        <feature:display name="Admin - Component Type" module="Components">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to view Components Types Manager</digi:trn>
                              </c:set>
                              <digi:link href="/updateComponentType.do" title="${translation}" >
                                <digi:trn>Component types manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </feature:display>
                        <!-- NO MOUDULE TO CONTROL THIS SECTION -->
                        <tr>
                          <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                          <td class="admin_box_label">
                            <c:set var="trnOrganizationManager">
                              <digi:trn invisibleLinks="true">Click here to view Organization Manager</digi:trn>
                            </c:set>
                            <digi:link href="/organisationManager.do?orgSelReset=true" title="${trnOrganizationManager}" >
                              <digi:trn>Organization manager</digi:trn>
                            </digi:link>
                          </td>
                        </tr>
                        <module:display name="Program Managers" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to view Multi Program Manager</digi:trn>
                              </c:set>
                              <digi:link href="/themeManager.do~view=multiprogram" title="${translation}" >
                                <digi:trn>Multi Program Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to view Multi Program Configuration Page</digi:trn>
                              </c:set>
                              <digi:link href="/programConfigurationPage.do" title="${translation}" >
                                <digi:trn >Multi Program Configuration</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                        <!-- NO MOUDULE TO CONTROL THIS SECTION -->
                        <module:display name="Indicator Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to view Indicator Manager</digi:trn>
                              </c:set>
                              <a href="/TEMPLATE/reampv2/packages/container/build/index.html#/reampv2-app/admin/indicator_manager" title="${translation}" >
                                <digi:trn>Indicator Manager</digi:trn>
                              </a>
                            </td>
                          </tr>
                        </module:display>

                        <module:display name="Structure Types Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to manage GIS Structure Types</digi:trn>
                              </c:set>
                              <digi:link module="esrigis" href="/structureTypeManager.do">
                                <digi:trn>Structure Types Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>

                        <module:display name="Aid Effectiveness Indicators Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to manage Aid Effectiveness Indicators</digi:trn>
                              </c:set>
                              <digi:link href="/aidEffectivenessIndicatorsManager.do">
                                <digi:trn>Aid Effectiveness Indicators Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>

                        <module:display name="Map configuration" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="trnDataExchangeExportTitle">
                                <digi:trn invisibleLinks="true">Click here to view the map configuration section</digi:trn>
                              </c:set>
                              <digi:link module="esrigis"  href="/MapsConfiguration.do" >
                                <digi:trn>Map Configuration</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>

                        <module:display name="Indicator Layer Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                           <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                           <td>
                             <a href="/TEMPLATE/ampTemplate/node_modules/gis-layers-manager/dist/index.html" >
                                <digi:trn>GIS Layers Manager</digi:trn>
                             </a>
                           </td>                          
                          </tr>
                        </module:display>

                        <module:display name="Budget Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn key="aim:clickToViewSectorManager" invisibleLinks="true">Click here to view budget manager</digi:trn>
                              </c:set>
                              <digi:link href="/BudgetManager.do" title="${translation}">
                                <digi:trn key="aim:budgetManager">Budget Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>

                        <module:display name="Program Mapping Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn key="aim:clickToViewNDDMappingManager" invisibleLinks="true">Click here to view the Indirect Programs Mapping for NDD Dashboard</digi:trn>
                              </c:set>
                              <a href="/TEMPLATE/reampv2/build/index.html#/ndd"
                                 title="${translation}">
                                <digi:trn key="aim:nddMappingManager">Program Mapping Manager</digi:trn>
                              </a>
                            </td>
                          </tr>
                        </module:display>

                      </table>
                    </div>
                  </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="admin_landing_page_tbl" style="table-layout:fixed">
                <tr>
                  <td width=33% class="admin_landing_page_box" valign=top>
                    <div class="admin_landing_page_box_cont">
                      <div class="admin_landing_page_box_title"><digi:trn>Currencies and rates</digi:trn></div>
                      <table border="0" cellspacing="2" cellpadding="2">
                        <module:display name="Currency Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to view Currency Manager</digi:trn>
                              </c:set>
                              <digi:link href="/currencyManager.do~clean=true" title="${translation}" >
                                <digi:trn>Currency Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                        <module:display name="Currency Rates Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to view Currency Rates Manager</digi:trn>
                              </c:set>
                              <digi:link href="/showCurrencyRates.do~clean=true~timePeriod=1" title="${translation}">
                                <digi:trn>Currency Rate Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                        <module:display name="Currency deflator" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to view Currency Deflator</digi:trn>
                              </c:set>
                              <a href="/TEMPLATE/reamp/modules/admin/currency/deflator/index.html"
                                 title="${translation}">
                                <digi:trn>Currency Deflator</digi:trn>
                              </a>
                            </td>
                          </tr>
                        </module:display>
                      </table>
                    </div></td>
                  <td width=10>&nbsp;</td>
                  <td width=33% class="admin_landing_page_box" valign=top>
                    <div class="admin_landing_page_box_cont">
                      <div class="admin_landing_page_box_title"><digi:trn>Import and export of data</digi:trn></div>
                      <table border="0" cellspacing="2" cellpadding="2">
                        <module:display name="Structures Importer" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="trnDataExchangeExportTitle">
                                <digi:trn invisibleLinks="true">Click here to view Structures Importer</digi:trn>
                              </c:set>
                              <digi:link module="esrigis"  href="/StructuresImporter.do" >
                                <digi:trn>Structures Importer</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                        <module:display name="Interchange Result" parentModule="ADMINISTRATIVE SECTION">
                        <tr>
                          <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                          <td class="admin_box_label">
                            <c:set var="translation">
                              <digi:trn key="aim:clickToViewInterchangeResult" invisibleLinks="true">Click here to view Interchange Result</digi:trn>
                            </c:set>
                            <digi:link href="/interchangeResult.do" title="${translation}">
                              <digi:trn key="aim:interchangeResult">Data Import Manager Results</digi:trn>
                            </digi:link>
                          </td>
                        </tr>
                        </module:display>
                      </table>

                    </div></td>
                  <td width=11>&nbsp;</td>
                  <td width=33% class="admin_landing_page_box" valign=top>
                    <div class="admin_landing_page_box_cont">
                      <div class="admin_landing_page_box_title"><digi:trn>AMP workflow auditor</digi:trn></div>
                      <table border="0" cellspacing="2" cellpadding="2">
                        <module:display name="Audit Logger Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to view Audit Logger Manager</digi:trn>
                              </c:set>
                              <digi:link href="/auditLoggerManager.do" title="${translation}" >
                                <digi:trn>Audit Logger Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                        <module:display name="Activity Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="trnActivityManager">
                                <digi:trn invisibleLinks="true">Click here to view Activity Manager</digi:trn>
                              </c:set>
                              <digi:link href="/activityManager.do" title="${trnActivityManager}" >
                                <digi:trn>Activity Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                        <module:display name="Scorecard Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="trnScorecardManager">
                                <digi:trn invisibleLinks="true">Click here to view Scorecard Manager</digi:trn>
                              </c:set>
                              <digi:link href="/scorecardManager.do" title="${trnScorecardManager}" >
                                <digi:trn>Scorecard Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                      </table>

                    </div></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="admin_landing_page_tbl" style="table-layout:fixed">

                <tr>
                  <td width=33% class="admin_landing_page_box" valign=top>
                    <div class="admin_landing_page_box_cont">
                      <div class="admin_landing_page_box_title"><digi:trn>Public view toolbox</digi:trn></div>
                      <table border="0" cellspacing="2" cellpadding="2">
                        <module:display name="Public View Content" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to edit public view content</digi:trn>
                              </c:set>
                              <digi:link module="content" href="/contentManager.do">
                                <digi:trn>Public View Content</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                        <module:display name="Public Filter Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to manage public filters</digi:trn>
                              </c:set>
                              <digi:link module="contentrepository" href="/publicDocTabManager.do?action=show">
                                <digi:trn>Public Filter Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                      </table>

                    </div></td>
                  <td width=10>&nbsp;</td>
                  <td width=33% class="admin_landing_page_box" valign=top>
                    <div class="admin_landing_page_box_cont">
                      <div class="admin_landing_page_box_title"><digi:trn>Developer Tools</digi:trn></div>
                      <table border="0" cellspacing="2" cellpadding="2">
                        <feature:display name="Applied Patches" module="Applied Patches">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="trnSystemSettings">
                                <digi:trn invisibleLinks="true">Click here to view the Patches applied</digi:trn>
                              </c:set>
                              <a href="/xmlpatcher/xmlpatches.do?mode=listPatches" title="${trnSystemSettings}">
                                <digi:trn>Database Patches</digi:trn>
                              </a>
                            </td>
                          </tr>
                        </feature:display>
                        <feature:display name="Admin Topics Help" module="HELP">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="trn">
                                <digi:trn key="help:viewSettings" invisibleLinks="true">Click here to view Help Topics Admin</digi:trn>
                              </c:set>
                              <digi:link module="help" href="/helpActions.do~actionType=viewAdmin" title="${trn}">
                                <digi:trn key="help:helpTopicAdmin">Help Topics Admin</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </feature:display>
                        <module:display name="Admin Translation Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                translation
                              </c:set>
                              <c:set var="trnTranslationManager">
                                <digi:trn invisibleLinks="true">Click here to view Translation Manager</digi:trn>
                              </c:set>
                              <digi:link module="translation" href="/importexport.do" title="${trnTranslationManager}">
                                <digi:trn>Translation Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                      </table>

                    </div></td>
                  <td width=11>&nbsp;</td>
                  <td width=33% class="admin_landing_page_box" valign=top>
                    <div class="admin_landing_page_box_cont">
                      <div class="admin_landing_page_box_title"><digi:trn>Paris indicator Tools</digi:trn></div>
                      <table border="0" cellspacing="2" cellpadding="2">
                        <feature:display  name="Paris Indicators Targets Manager" module="Admin Home">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn key="aim:tipViewAhSurveis" invisibleLinks="true">Click here to view Paris Indicator Manager</digi:trn>
                              </c:set>
                              <digi:link module="aim" href="/viewAhSurveis.do" title="${translation}">
                                <digi:trn key="aim:parisIndManager">Paris Indicators Targets Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </feature:display>
                        <feature:display  name="Global Partnership Indicators Manager" module="Admin Home">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn key="aim:tipManagerGPI" invisibleLinks="true">Click here to setup GPI</digi:trn>
                              </c:set>
                              <digi:link module="aim" href="/manageGPI.do?actionType=show" title="${translation}">
                                <digi:trn key="aim:gpiManager">Global Partnership Indicators Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </feature:display>
                      </table>

                    </div></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="admin_landing_page_tbl" style="table-layout:fixed">
                <tr>
                  <td width=33% class="admin_landing_page_box" valign=top>
                    <div class="admin_landing_page_box_cont">
                      <div class="admin_landing_page_box_title"><digi:trn>Message tools</digi:trn></div>
                      <table border="0" cellspacing="2" cellpadding="2">
                        <feature:display name="Message Manager" module="ADMIN">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="trn">
                                <digi:trn invisibleLinks="true">Click here to view Message Settings</digi:trn>
                              </c:set>
                              <digi:link module="message" href="/msgSettings.do?actionType=getSettings" title="${trn}">
                                <digi:trn>Message Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </feature:display>
                      </table>

                    </div></td>
                  <td width=10>&nbsp;</td>
                  <td width=33% class="admin_landing_page_box" valign=top>
                    <div class="admin_landing_page_box_cont">
                      <div class="admin_landing_page_box_title"><digi:trn>Resources tools</digi:trn></div>
                      <table border="0" cellspacing="2" cellpadding="2">
                        <module:display name="Resource Label Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to manage resource labels</digi:trn>
                              </c:set>
                              <digi:link module="contentrepository" href="/labelManager.do?htmlView=true">
                                <digi:trn>Resource Label Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                        <module:display name="Template Documents Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to view template document manager</digi:trn>
                              </c:set>
                              <digi:link module="contentrepository" href="/tempDocManager.do?actType=viewTemplateDocuments">
                                <digi:trn>Template Documents Manager</digi:trn>
                              </digi:link>
                            </td>
                          </tr>
                        </module:display>
                        <module:display name="Resource Manager" parentModule="ADMINISTRATIVE SECTION">
							<tr>
								<td><img width="16" align="left"
									src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
								<td class="admin_box_label">
									<c:set var="translation">
										<digi:trn invisibleLinks="true">Click here to view Document manager admin</digi:trn>
									</c:set> 
									<a href="/TEMPLATE/reamp/modules/admin/resource-manager-admin/index.html"
									title="${translation}"> <digi:trn>Resource Manager</digi:trn>
									</a>
								</td>
							</tr>
                        </module:display>
                      </table>

                    </div></td>
                  <td width=11>&nbsp;</td>
                  <td width=33% class="admin_landing_page_box" valign=top>
                    <div class="admin_landing_page_box_cont">
                      <div class="admin_landing_page_box_title"><digi:trn>Results dashboard tools</digi:trn></div>
                      <table border="0" cellspacing="2" cellpadding="2">
                        <module:display name="Budget Integration" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label"><digi:link module="budgetexport" href="/" title="Budget Integration">
                              <digi:trn>Budget Integration</digi:trn>
                            </digi:link></td>
                          </tr>
                        </module:display>
                        <module:display name="Dashboards Manager" parentModule="ADMINISTRATIVE SECTION">
                          <tr>
                            <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
                            <td class="admin_box_label">
                              <c:set var="translation">
                                <digi:trn invisibleLinks="true">Click here to view Dashboard Manager</digi:trn>
                              </c:set>
                                 <a href="/TEMPLATE/reamp/modules/admin/dashboard/index.html"
                                 title="${translation}">
                                <digi:trn>Dashboard Manager</digi:trn>
                              </a>
                            </td>
                          </tr>
                        </module:display>
					</table>
                    </div></td>
                </tr>
              </table>
            </fieldset>




            <br />
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
