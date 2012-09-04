<%@page import="org.digijava.module.aim.dbentity.AmpLocation"%>
<%@page import="org.digijava.module.aim.dbentity.AmpActivityLocation"%>
<%@page import="org.digijava.module.aim.dbentity.AmpSector"%>


<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"></script>

<digi:instance property="gisRegReportForm"/>

<%
        String countryName = "";
        String ISO = null;
        java.util.Iterator itr1 = org.digijava.module.aim.util.FeaturesUtil.getDefaultCountryISO().iterator();
        while (itr1.hasNext()) {
            org.digijava.module.aim.dbentity.AmpGlobalSettings ampG = (org.digijava.module.aim.dbentity.AmpGlobalSettings) itr1.next();
            ISO = ampG.getGlobalSettingsValue();
        }

        if (ISO != null && !ISO.equals("")) {
            org.digijava.kernel.dbentity.Country cntry = org.digijava.module.aim.util.DbUtil.getDgCountry(ISO);
            countryName = " " + cntry.getCountryName();
        } else {
            countryName = "";
        }
%>

<digi:ref href="/repository/gis/view/css/gisReport.css" type="text/css" rel="stylesheet" />






<div class="yui-skin-sam" style="width:100%;height:100%;">
  <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;width:100%;">
      <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/tabrightcorner.gif" align="right" hspace="0"/>
      <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/tableftcorner.gif" align="left" hspace="0"/>
      <div class="longTab">
          <digi:trn key="gis:regionalview">Regional View</digi:trn>
      </div>
            
    <div id="div1" class="yui-content" style="font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
		<table cellpadding="0" cellspacing="0" width="100%" style="border-collapse:collapse;" border="1" bordercolor="#CCCCCC">
			<tr>
				<td nowrap width="170" style="color:#5E5E5E;font-weight:bold" class="tableHeader">
					<digi:trn>Country</digi:trn>
				</td>
				<td>&nbsp;<%=countryName%></td>
			</tr>
			<tr>
				<td nowrap style="color:#5E5E5E;font-weight:bold" class="tableHeader">
					<digi:trn>Region</digi:trn>
				</td>
				<td>&nbsp;<bean:write name="gisRegReportForm" property="regName"/></td>
			</tr>
			<tr>
				<td nowrap style="color:#5E5E5E;font-weight:bold" class="tableHeader">
					<digi:trn>Sector(s)</digi:trn>
				</td>
				<td>&nbsp;
					<logic:equal name="gisRegReportForm" property="filterAllSectors" value="true">
						<digi:trn>All</digi:trn>
					</logic:equal>
					<logic:notEqual name="gisRegReportForm" property="filterAllSectors" value="true">
						<bean:size id="secCount" name="gisRegReportForm" property="selSectorNames"/>
						<logic:greaterThan name="secCount" value="1">
							<digi:trn><span style="text-decoration:underline;" onMouseOver="showMultySectors(true)" onMouseOut="showMultySectors(false)">Multiple</span></digi:trn>
							<script language="javascript">
								function showMultySectors(val) {
									if (val) {
										$('#multySectorDiv').css('display','block')
									} else {
										$('#multySectorDiv').css('display','none')
									}
								}
							</script>
									
							<div id="multySectorDiv" style="padding:5px; display:none; position:absolute; border:1px solid silver; background:white;">
							<logic:iterate name="gisRegReportForm" property="selSectorNames" id="topSectorName">
								<span style="text-decoration:none;font-size:10px">
									<bean:write name="topSectorName"/><br>
								</span>
							</logic:iterate>
						</div>
						</logic:greaterThan>
						<logic:lessEqual name="secCount" value="1">
							<logic:iterate name="gisRegReportForm" property="selSectorNames" id="topSectorName">
								<bean:write name="topSectorName"/>
							</logic:iterate>
						</logic:lessEqual>
						
							
					</logic:notEqual>
					<%--
					<bean:write name="gisRegReportForm" property="selSectorName"/>
					--%>
				</td>
			</tr>
			<tr>
				<td nowrap style="color:#5E5E5E;font-weight:bold" class="tableHeader">
					<digi:trn>Year Range</digi:trn>
				</td>
				<td>&nbsp;<bean:write name="gisRegReportForm" property="startYear"/> - <bean:write name="gisRegReportForm" property="endYear"/></td>
			</tr>
			<tr>
				<td nowrap style="color:#5E5E5E;font-weight:bold" class="tableHeader">
					<digi:trn>Actual Commitments</digi:trn>
				</td>
				<td>&nbsp;<bean:write name="gisRegReportForm" property="actualCommitmentsStr"/></td>
			</tr>
			<tr>
				<td nowrap style="color:#5E5E5E;font-weight:bold" class="tableHeader">
					<digi:trn>Actual Disbursements</digi:trn>
				</td>
				<td>&nbsp;<bean:write name="gisRegReportForm" property="actualDisbursementsStr"/></td>
			</tr>
			<tr>
				<td nowrap style="color:#5E5E5E;font-weight:bold" class="tableHeader">
					<digi:trn>Actual Expenditures</digi:trn>
				</td>
				<td>&nbsp;<bean:write name="gisRegReportForm" property="actualExpendituresStr"/></td>
			</tr>
			<tr>
				<td nowrap style="color:#5E5E5E;font-weight:bold" class="tableHeader">
					<digi:trn>Plannet Disbursements</digi:trn>
				</td>
				<td>&nbsp;<bean:write name="gisRegReportForm" property="actualDisbursementsStr"/></td>
			</tr>
			<tr>
				<td nowrap colspan="2" align="center">
					<font color="red">
						<digi:trn>Note: all numbers are in</digi:trn> ${gisRegReportForm.selectedCurrency}
					</font>
				</td>
			</tr>
		</table> 
	</div>
</div>
		
		<br>
		
		
		<logic:present name="gisRegReportForm" property="activityLocationFundingList">
			<logic:notEmpty name="gisRegReportForm" property="activityLocationFundingList">

			

					<div id="listContainerDiv" style='width:100%; height:300px; border: 0px; overflow-y:scroll;'>
			
			
					<table width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;" border="0">
						<thead>
			
						<tr>
								<td colspan="4" style="overflow-x:hidden; background-color: #C7D4DB; color:#000000;" >
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell">
											<digi:trn><b>Overall information</b></digi:trn>
										</div>
									</div>
								</td>
								<td colspan="4" style="overflow-x:hidden; background-color: #C7D4DB; color:#000000;">
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell">
											<digi:trn><b>For selected sector/region</b></digi:trn>
										</div>
									</div>
								</td>
						</tr>
			
						
						<tr>
								<td width="20%" style="overflow-x:hidden; background-color: #CDCDCD; color:#000000;">
										<div class="gisReportTableBevelCell">
											<digi:trn>Activity</digi:trn>
										</div>
											</td>
								<td width="20%" style="overflow-x:hidden; background-color: #CDCDCD; color:#000000;">
										<div class="gisReportTableBevelCell">
											<digi:trn>Region</digi:trn>(s)
										</div>
								</td>
								<td width="20%" style="overflow-x:hidden; background-color: #CDCDCD; color:#000000;">
										<div class="gisReportTableBevelCell">
											<digi:trn>Sector</digi:trn>(s)
										</div>
								</td>
								<td width="8%" style="overflow-x:hidden; background-color: #CDCDCD; color:#000000;">
										<div class="gisReportTableBevelCell">
											<digi:trn>Donor</digi:trn>(s)
										</div>
								</td>
								<td width="8%" style="overflow-x:hidden; background-color: #CDCDCD; color:#000000;">
										<div class="gisReportTableBevelCell">
											<digi:trn>Commitments</digi:trn>
										</div>
								</td>
								<td width="8%" style="overflow-x:hidden;background-color: #CDCDCD; color:#000000;">
										<div class="gisReportTableBevelCell">
											<digi:trn>Disbursements</digi:trn>
										</div>
								</td>
								<td width="8%" style="overflow-x:hidden; background-color: #CDCDCD; color:#000000;">
										<div class="gisReportTableBevelCell">
											<digi:trn>Expenditures</digi:trn>
										</div>
								</td>
								<td width="8%" style="overflow-x:hidden;background-color: #CDCDCD; color:#000000;">
										<div class="gisReportTableBevelCell">
											<digi:trn>Planned Disb.</digi:trn>
										</div>
								</td>
						</tr>
						</thead>
						
						<tbody>
							<%--
							<bean:define name="gisRegReportForm" property="primarySectorSchemeId" id="primarySectorScheme"/>
							--%>
						
						<logic:iterate name="gisRegReportForm" property="activityLocationFundingList" id="activityLocationFunding">
							<tr>
								<td valign="top" style="overflow-x:hidden; overflow-y:hidden;" height="20">
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal">
											<c:choose>
												<c:when test="${gisRegReportForm.fromPublicView}">
													<bean:write name="activityLocationFunding"
														property="activityName" />
												</c:when>
												<c:otherwise>
													<a
														title="<bean:write name="activityLocationFunding" property="activityName"/>"
														href="javascript:showSelActivity(<bean:write name="activityLocationFunding" property="activityId"/>);">
														<bean:write name="activityLocationFunding"
															property="activityName" />
													</a>
												</c:otherwise>
											</c:choose>
										</div>
									</div>
								</td>
								<td align="left" style="overflow-x:hidden;">
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal">
											<logic:present name="activityLocationFunding" property="locations">
												<logic:notEmpty name="activityLocationFunding" property="locations">
													<ul style="margin:0 0 0 20px; padding:0;">
														<logic:iterate name="activityLocationFunding" property="locations" id="iterLocation">
															<li>
																<bean:write name="iterLocation"/>
															</li>
														</logic:iterate>
													</ul>
												</logic:notEmpty>
											</logic:present>
										</div>
									</div>
								</td>
								<td align="left" style="overflow-x:hidden;">
								<%--
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal">
											<logic:present name="activityLocationFunding" property="activity.sectors">
												<logic:notEmpty name="activityLocationFunding" property="activity.sectors">
													<ul style="margin:0 0 0 20px; padding:0;">
													<logic:iterate name="activityLocationFunding" property="activity.sectors" id="iterSector">
														<bean:define id="ampSec" name="iterSector" property="sectorId" type="org.digijava.module.aim.dbentity.AmpSector"/>
														<logic:equal name="ampSec" property="ampSecSchemeId.ampSecSchemeId" value="<%=primarySectorScheme.toString()%>">
															<li>
															<bean:write name="ampSec" property="name"/>
															</li>
														</logic:equal>
													</logic:iterate>
													</ul>
												</logic:notEmpty>
											</logic:present>
										</div>
									</div>
								--%>
								<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal">
											<logic:present name="activityLocationFunding" property="topSectors">
												<logic:notEmpty name="activityLocationFunding" property="topSectors">
													<ul style="margin:0 0 0 20px; padding:0;">
														<logic:iterate name="activityLocationFunding" property="topSectors" id="iterTopSectors">
															<li>
																<bean:write name="iterTopSectors"/>
															</li>
														</logic:iterate>
													</ul>
												</logic:notEmpty>
											</logic:present>
										</div>
									</div>
								</td>
								<td align="left" style="overflow-x:hidden;">
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal">
											<logic:present name="activityLocationFunding" property="donorOrgs">
												<logic:notEmpty name="activityLocationFunding" property="donorOrgs">
													<ul style="margin:0 0 0 20px; padding:0;">
														<logic:iterate name="activityLocationFunding" property="donorOrgs" id="iterOrgrole">
															<li>
																<bean:write name="iterOrgrole"/>
															</li>
														</logic:iterate>
													</ul>
												</logic:notEmpty>
											</logic:present>
										</div>
									</div>
								</td>
								<td style="overflow-x:hidden;">
									<div class="gisReportTableBevelCellContainer">
									<div class="gisReportTableBevelCell 
									<logic:present name="activityLocationFunding" property="fmtCommitment">
									gisReportTableBevelCellBgNormal
									</logic:present>
									<logic:notPresent name="activityLocationFunding" property="fmtCommitment">
									gisReportTableBevelCellBgDash
									</logic:notPresent>
									">
										<bean:write name="activityLocationFunding" property="fmtCommitment"/>
									</div></div>
								</td>
								<td style="overflow-x:hidden;">
									<div class="gisReportTableBevelCellContainer">
									<div class="gisReportTableBevelCell 
									<logic:present  name="activityLocationFunding" property="fmtDisbursement">
									gisReportTableBevelCellBgNormal
									</logic:present>
									<logic:notPresent  name="activityLocationFunding" property="fmtDisbursement">
									gisReportTableBevelCellBgDash
									</logic:notPresent>
									">
										<bean:write name="activityLocationFunding" property="fmtDisbursement"/>
									</div>
									</div>
								</td>
								<td style="overflow-x:hidden;">
									<div class="gisReportTableBevelCellContainer">
									<div class="gisReportTableBevelCell 
									<logic:present name="activityLocationFunding" property="fmtExpenditure">
									gisReportTableBevelCellBgNormal
									</logic:present>
									<logic:notPresent name="activityLocationFunding" property="fmtExpenditure">
									gisReportTableBevelCellBgDash
									</logic:notPresent>
									">
										<bean:write name="activityLocationFunding" property="fmtExpenditure"/>
									</div>
									</div>
								</td>
								
								<td style="overflow-x:hidden;">
									<div class="gisReportTableBevelCellContainer">
									<div class="gisReportTableBevelCell 
									<logic:present  name="activityLocationFunding" property="fmtPlannedDisbursement">
									gisReportTableBevelCellBgNormal
									</logic:present>
									<logic:notPresent  name="activityLocationFunding" property="fmtPlannedDisbursement">
									gisReportTableBevelCellBgDash
									</logic:notPresent>
									">
										<bean:write name="activityLocationFunding" property="fmtPlannedDisbursement"/>
									</div>
									</div>
								</td>
								
								<td style="border-left:1px solid black;">&nbsp;</td>
							</tr>
						</logic:iterate>
						<tr>
							<td colspan="8" height="100%" style="border-top:1px solid #CCCCCC;">&nbsp;
									
							</td>
							<!--
							<td>&nbsp;</td>
							-->
						</tr>
						</tbody>
					</table>
					
					</div>
				

		
			</logic:notEmpty>
		</logic:present>
		<logic:notPresent name="gisRegReportForm" property="activityLocationFundingList">
			<div style="width:100%" align="center">
				<div class="gisReportWarning" style="width:85%">
				 <digi:trn>
				 	The report contains no activities. Please adjust your filter criteria.
				 </digi:trn>
				</div>
			</div>
		</logic:notPresent>
</div>		
		
		<script language="JavaScript">
		function showSelActivity(activityId) {
			//var actUrl = "/aim/selectActivityTabs.do~pageId=2~ampActivityId=" + activityId;
			var actUrl = "/aim/viewActivityPreview.do~isPreview=1~pageId=2~activityId=" + activityId;
			
			if (window.opener.opener == null) {
				window.open(actUrl, null, null);
			} else {
				window.opener.opener.location.href = actUrl;
				window.opener.opener.focus();
			}
			
		}
		
		resizeListContainer = function(o) {
			var wndHeight = $(window).height();
			if(wndHeight >= 499) {
				var newHeight = (wndHeight - 200) + "px";
				$("#listContainerDiv").css("height", newHeight);
			}
		}
		
		$(document).ready(resizeListContainer);
		$(window).resize(resizeListContainer);
		
		
		
		</script>


