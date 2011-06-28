<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>




<digi:instance property="sectorDonorWidgetReportForm"/>



<digi:ref href="/repository/gis/view/css/gisReport.css" type="text/css" rel="stylesheet" />
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />
<digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />







<div class="yui-skin-sam" style="width:100%;height:100%;overflow:auto">
  <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;width:100%;">
      <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/tabrightcorner.gif" align="right" hspace="0"/>
      <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/tableftcorner.gif" align="left" hspace="0"/>
      <div class="longTab">
          <digi:trn>Breakdown by sector</digi:trn>
      </div>


		<table cellpadding="0" cellspacing="0" width="100%" style="border-collapse:collapse;background:#FFFFFF" border="1" bordercolor="#000000">
			<tr>
				<td width="120" class="tableHeader"><digi:trn>Sector</digi:trn></td>
				<td>&nbsp;<bean:write name="sectorDonorWidgetReportForm" property="sectorName"/></td>
			</tr>
			
                        <tr>
				<td class="tableHeader"><digi:trn>Donor</digi:trn></td>
				<td>&nbsp;<bean:write name="sectorDonorWidgetReportForm" property="donorName"/></td>
			</tr>
			<tr>
				<td class="tableHeader"><digi:trn>Year Range</digi:trn></td>
				<td>&nbsp;<bean:write name="sectorDonorWidgetReportForm" property="startYear"/> - <bean:write name="sectorDonorWidgetReportForm" property="endYear"/></td>
			</tr>
            <feature:display module="Funding" name="Commitments">
			<tr>
				<td class="tableHeader"><digi:trn>Actual Commitments</digi:trn></td>
				<td>&nbsp;<bean:write name="sectorDonorWidgetReportForm" property="actualCommitmentsStr"/></td>
			</tr>
            </feature:display>
            <feature:display module="Funding" name="Disbursement">
			<tr>
				<td class="tableHeader"><digi:trn>Actual Disbursements</digi:trn></td>
				<td>&nbsp;<bean:write name="sectorDonorWidgetReportForm" property="actualDisbursementsStr"/></td>
			</tr>
            </feature:display>
            <feature:display module="Funding" name="Expenditures">
			<tr>
				<td class="tableHeader"><digi:trn>Actual Expenditures</digi:trn></td>
				<td>&nbsp;<bean:write name="sectorDonorWidgetReportForm" property="actualExpendituresStr"/></td>
			</tr>
            </feature:display>
			<tr>
                            <td colspan="2" align="center">
                            	<font color="red" style="font-size:11px"><digi:trn>Note: all numbers are in</digi:trn> ${sectorDonorWidgetReportForm.selectedCurrency}</font>
                            </td>
			</tr>
		</table>
	
</div>

		<br>


		<logic:present name="sectorDonorWidgetReportForm" property="actSectorDonorFundingInfo">
			<logic:notEmpty name="sectorDonorWidgetReportForm" property="actSectorDonorFundingInfo">

<div style='width:100%; height:280px; border: 0px; overflow-y:scroll;'>

					<table width="100%" cellpadding="0" cellspacing="0" cellpadding="0" cellspacing="0" style="width:100%;height:100%" border="0">
						<thead>

						<tr>
								<td colspan="3" class="gisContentTableMainHeader" >
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell">
											<digi:trn>Overall information</digi:trn>
										</div>
									</div>
								</td>
								<td colspan="3" class="gisContentTableMainHeader">
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell">
											<digi:trn>For selected sector/donor</digi:trn>
										</div>
									</div>
								</td>
						</tr>


						<tr>
								<td width="30%" class="gisContentTableHeader">

										<div class="gisReportTableBevelCell">
											<digi:trn>Activity</digi:trn>
										</div>

								</td>
								<td width="20%" class="gisContentTableHeader">

										<div class="gisReportTableBevelCell">
											<digi:trn>Sector(s)</digi:trn>
										</div>

								</td>
								<td width="20%" class="gisContentTableHeader">

										<div class="gisReportTableBevelCell">
											<digi:trn>Donor(s)</digi:trn>
										</div>

								</td>
                                <feature:display module="Funding" name="Commitments">
                                        <td width="10%"  class="gisContentTableHeader">

                                        <div class="gisReportTableBevelCell">
                                            <digi:trn>Commitments</digi:trn>
                                        </div>

                                    </td>
                                </feature:display>
                                <feature:display module="Funding" name="Disbursement">
								<td width="10%" class="gisContentTableHeader">
                                  
                                        <div class="gisReportTableBevelCell">
                                            <digi:trn>Disbursements</digi:trn>
                                        </div>
                                  
								</td>
                                </feature:display>
                                <feature:display module="Funding" name="Expenditures">
								<td width="10%" class="gisContentTableHeader">
                                    
                                        <div class="gisReportTableBevelCell">
                                            <digi:trn>Expenditures</digi:trn>
                                        </div>
                                    
								</td>
                                </feature:display>
								
						</tr>
						</thead>
                                                
						<tbody>
                                              
						<logic:iterate name="sectorDonorWidgetReportForm" property="actSectorDonorFundingInfo" id="activitySectorDonorFunding">
							<tr>
								<td width="30%" valign="top" style="overflow-x:hidden;">
                                    <div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal">
											<a title="<bean:write name="activitySectorDonorFunding" property="activity.name"/>" href="javascript:showSelActivity(<bean:write name="activitySectorDonorFunding" property="activity.ampActivityId"/>);">
												<bean:write name="activitySectorDonorFunding" property="activity.name"/>
											</a>
										</div>
									</div>
								</td>
							
								<td width="20%"  align="left" style="overflow-x:hidden;">
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal" >
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
                                <td width="20%" align="left" align="left" style="overflow-x:hidden;">
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal" >
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
                                 <feature:display module="Funding" name="Commitments">
								<td width="10%" align="left" align="left" style="overflow-x:hidden;">
									<div class="gisReportTableBevelCellContainer">
									<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal">
										<bean:write name="activitySectorDonorFunding" property="fmtCommitment"/>
									</div>
									</div>
								</td>
                                </feature:display>
                                <feature:display module="Funding" name="Disbursement">
								<td width="10%" align="left" align="left" style="overflow-x:hidden;">
									<div class="gisReportTableBevelCellContainer">
									<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal">
										<bean:write name="activitySectorDonorFunding" property="fmtDisbursement"/>
									</div>
									</div>
								</td>
                                 </feature:display>
                                <feature:display module="Funding" name="Expenditures">
								<td width="10%" align="left" align="left" style="overflow-x:hidden;">
									<div class="gisReportTableBevelCellContainer">
									<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal">
										<bean:write name="activitySectorDonorFunding" property="fmtExpenditure"/>
									</div>
									</div>
								</td>
                                </feature:display>
				
							</tr>
						</logic:iterate>
                                     
						<tr>
							<td colspan="6" height="100%" style="border-top:1px solid black;">
									&nbsp;
							</td>
							
						</tr>
						</tbody>
                                            
					</table>
</div>

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


