<%@page import="org.digijava.module.aim.dbentity.AmpLocation"%>
<%@page import="org.digijava.module.aim.dbentity.AmpActivityLocation"%>
<%@page import="org.digijava.module.aim.dbentity.AmpSector"%>


<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>




<digi:instance property="sectorDonorWidgetReportForm"/>



<digi:ref href="/repository/gis/view/css/gisReport.css" type="text/css" rel="stylesheet" />
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />
<digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />







<div class="yui-skin-sam" style="width:100%;height:100%;overflow:auto">
  <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;width:100%;">
      <digi:img src="images/tabrightcorner.gif" align="right" hspace="0"/>
      <digi:img src="images/tableftcorner.gif" align="left" hspace="0"/>
      <div class="longTab">
          <digi:trn>Breakdown by sector</digi:trn>
      </div>


		<table cellpadding="0" cellspacing="0" width="100%" style="border-collapse:collapse;background:#FFFFFF" border="1" bordercolor="#000000">
			<tr>
				<td width="120" style="color:#FFFFFF;font-weight:bold;background:#222E5D;font-size:11px">Sector</td>
				<td>&nbsp;<bean:write name="sectorDonorWidgetReportForm" property="sectorName"/></td>
			</tr>
			
                        <tr>
				<td style="color:#FFFFFF;font-weight:bold;background:#222E5D;font-size:11px">Donor</td>
				<td>&nbsp;<bean:write name="sectorDonorWidgetReportForm" property="donorName"/></td>
			</tr>
			<tr>
				<td style="color:#FFFFFF;font-weight:bold;background:#222E5D;font-size:11px">Year Range</td>
				<td>&nbsp;<bean:write name="sectorDonorWidgetReportForm" property="startYear"/> - <bean:write name="sectorDonorWidgetReportForm" property="endYear"/></td>
			</tr>
			<tr>
				<td style="color:#FFFFFF;font-weight:bold;background:#222E5D;font-size:11px">Actual Commitments</td>
				<td>&nbsp;<bean:write name="sectorDonorWidgetReportForm" property="actualCommitmentsStr"/></td>
			</tr>
			<tr>
				<td style="color:#FFFFFF;font-weight:bold;background:#222E5D;font-size:11px">Actual Disbursements</td>
				<td>&nbsp;<bean:write name="sectorDonorWidgetReportForm" property="actualDisbursementsStr"/></td>
			</tr>
			<tr>
				<td style="color:#FFFFFF;font-weight:bold;background:#222E5D;font-size:11px">Actual Expenditures</td>
				<td>&nbsp;<bean:write name="sectorDonorWidgetReportForm" property="actualExpendituresStr"/></td>
			</tr>
			<tr>
                            <td colspan="2" align="center"><font color="red" style="font-size:11px">Note: all numbers are in USD</font></td>
			</tr>
		</table>
	
</div>

		<br>


		<logic:present name="sectorDonorWidgetReportForm" property="actSectorDonorFundingInfo">
			<logic:notEmpty name="sectorDonorWidgetReportForm" property="actSectorDonorFundingInfo">



					<table width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;" border="0">
						<thead>

						<tr>
								<td colspan="4" style="overflow:auto; background-color: #4A5A80; color:#FFFFFF;" >
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell">
											<digi:trn>Overall information</digi:trn>
										</div>
									</div>
								</td>
								<td colspan="3" style="overflow:auto; background-color: #4A5A80; color:#FFFFFF;">
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell">
                                                                                    <digi:trn>For selected sector/donor</digi:trn>
										</div>
									</div>
								</td>
						</tr>


						<tr>
								<td width="30%" style="overflow:auto; background-color: #222E5D; color:#FFFFFF;">

										<div class="gisReportTableBevelCell">
											<digi:trn>Activity</digi:trn>
										</div>

								</td>
								<td width="20%" style="overflow:auto; background-color: #222E5D; color:#FFFFFF;">

										<div class="gisReportTableBevelCell">
											<digi:trn>Sector(s)</digi:trn>
										</div>

								</td>
								<td width="20%" style="overflow:auto; background-color: #222E5D; color:#FFFFFF;">

										<div class="gisReportTableBevelCell">
											Donor(s)
										</div>

								</td>
								<td width="10%" style="overflow:auto; background-color: #222E5D; color:#FFFFFF;">

										<div class="gisReportTableBevelCell">
											Commitments
										</div>

								</td>
								<td width="10%" style="overflow:auto; background-color: #222E5D; color:#FFFFFF;">

										<div class="gisReportTableBevelCell">
											Disbursements
										</div>

								</td>
								<td width="10%" style="overflow:auto; background-color: #222E5D; color:#FFFFFF;">

										<div class="gisReportTableBevelCell">
											Expenditures
										</div>

								</td>
								
						</tr>
						</thead>
                                                
                                                <tbody>
                                              
						<logic:iterate name="sectorDonorWidgetReportForm" property="actSectorDonorFundingInfo" id="activitySectorDonorFunding">
							<tr>
								<td width="30%" valign="top" style="overflow:auto;" height="20px">
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal">
											<a title="<bean:write name="activitySectorDonorFunding" property="activity.name"/>" href="javascript:showSelActivity(<bean:write name="activitySectorDonorFunding" property="activity.ampActivityId"/>);">
												<bean:write name="activitySectorDonorFunding" property="activity.name"/>
											</a>
										</div>
									</div>
								</td>
							
								<td width="20%" align="left" style="overflow:auto;">
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal">
											<logic:present name="activitySectorDonorFunding" property="sectors">
												<logic:notEmpty name="activitySectorDonorFunding" property="sectors">
													<ul style="margin:0 0 0 20px; padding:0;">
                                                                                                            <c:forEach var="sector" items="${activitySectorDonorFunding.sectors}">
														<li>
                                                                                                                    <c:out value="${sector.name}"/>
												
														</li>
                                                                                                                </c:forEach>
													</ul>
												</logic:notEmpty>
											</logic:present>
										</div>
									</div>
								</td>
								<td width="20%" align="left" style="overflow:auto;">
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal">
											<logic:present name="activitySectorDonorFunding" property="donorOrgs">
												<logic:notEmpty name="activitySectorDonorFunding" property="donorOrgs">
													<ul style="margin:0 0 0 20px; padding:0;">
													 <c:forEach var="donor"   items="${activitySectorDonorFunding.donorOrgs}">
														<li>
														 <c:out value="${donor.name}"/>
														</li>
                                                                                                                </c:forEach>
													</ul>
												</logic:notEmpty>
											</logic:present>
										</div>
									</div>
								</td>
								<td width="10%" style="overflow:auto;">
									<div class="gisReportTableBevelCellContainer">
									<div class="gisReportTableBevelCell
									<logic:present name="activitySectorDonorFunding" property="fmtCommitment">
									gisReportTableBevelCellBgNormal
									</logic:present>
									<logic:notPresent name="activitySectorDonorFunding" property="fmtCommitment">
									gisReportTableBevelCellBgDash
									</logic:notPresent>
									">
										<bean:write name="activitySectorDonorFunding" property="fmtCommitment"/>
									</div>

									</div>
								</td>
								<td width="10%" style="overflow:auto;">
									<div class="gisReportTableBevelCellContainer">
									<div class="gisReportTableBevelCell
									<logic:present  name="activitySectorDonorFunding" property="fmtDisbursement">
									gisReportTableBevelCellBgNormal
									</logic:present>
									<logic:notPresent  name="activitySectorDonorFunding" property="fmtDisbursement">
									gisReportTableBevelCellBgDash
									</logic:notPresent>
									">
										<bean:write name="activitySectorDonorFunding" property="fmtDisbursement"/>
									</div>
									</div>
								</td>
								<td width="10%" style="overflow:auto;">
									<div class="gisReportTableBevelCellContainer">
									<div class="gisReportTableBevelCell
									<logic:present name="activitySectorDonorFunding" property="fmtExpenditure">
									gisReportTableBevelCellBgNormal
									</logic:present>
									<logic:notPresent name="activitySectorDonorFunding" property="fmtExpenditure">
									gisReportTableBevelCellBgDash
									</logic:notPresent>
									">
										<bean:write name="activitySectorDonorFunding" property="fmtExpenditure"/>
									</div>
									</div>
								</td>
				
							</tr>
						</logic:iterate>
                                     
						<tr>
							<td colspan="6" height="100%" style="border-top:1px solid black;">
									&nbsp;
							</td>
							
						</tr>
						</tbody>
                                            
					</table>


			</logic:notEmpty>
		</logic:present>
		
</div>

		<script language="JavaScript">
		function showSelActivity(activityId) {
			var actUrl = "/aim/selectActivityTabs.do~ampActivityId=" + activityId;
			if (window.opener.opener == null) {
				window.open(actUrl, null, null);
			} else {
				window.opener.opener.location.href = actUrl;
				window.opener.opener.focus();
			}

		}
		</script>


