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
      <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/tabrightcorner.gif" align="right" hspace="0"/>
      <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/tableftcorner.gif" align="left" hspace="0"/>
      <div class="longTab">
          <digi:trn>Breakdown by sector</digi:trn>
      </div>


		<table cellpadding="0" cellspacing="0" width="100%" style="border-collapse:collapse;background:#FFFFFF" border="1" bordercolor="#000000">
			<tr>
				<td width="120" class="tableMainHeader">Sector</td>
				<td>&nbsp;<bean:write name="sectorDonorWidgetReportForm" property="sectorName"/></td>
			</tr>
			
                        <tr>
				<td class="tableMainHeader">Donor</td>
				<td>&nbsp;<bean:write name="sectorDonorWidgetReportForm" property="donorName"/></td>
			</tr>
			<tr>
				<td class="tableMainHeader">Year Range</td>
				<td>&nbsp;<bean:write name="sectorDonorWidgetReportForm" property="startYear"/> - <bean:write name="sectorDonorWidgetReportForm" property="endYear"/></td>
			</tr>
			<tr>
				<td class="tableMainHeader">Actual Commitments</td>
				<td>&nbsp;<bean:write name="sectorDonorWidgetReportForm" property="actualCommitmentsStr"/></td>
			</tr>
			<tr>
				<td class="tableMainHeader">Actual Disbursements</td>
				<td>&nbsp;<bean:write name="sectorDonorWidgetReportForm" property="actualDisbursementsStr"/></td>
			</tr>
			<tr>
				<td class="tableMainHeader tableHeadColorStyle">Actual Expenditures</td>
				<td>&nbsp;<bean:write name="sectorDonorWidgetReportForm" property="actualExpendituresStr"/></td>
			</tr>
			<tr>
                            <td colspan="2" align="center"><font color="red" style="font-size:11px"><digi:trn>Note: all numbers are in USD</digi:trn>, <br/><digi:trn>Report shows activities only from the current workspace </digi:trn>.</font></td>
			</tr>
		</table>
	
</div>

		<br>


		<logic:present name="sectorDonorWidgetReportForm" property="actSectorDonorFundingInfo">
			<logic:notEmpty name="sectorDonorWidgetReportForm" property="actSectorDonorFundingInfo">



					<table width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;" border="0">
						<thead>

						<tr>
								<td colspan="4" class="tableHeader" >
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell">
											<digi:trn>Overall information</digi:trn>
										</div>
									</div>
								</td>
								<td colspan="3" class="tableHeader">
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell">
                                                                                    <digi:trn>For selected sector/donor</digi:trn>
										</div>
									</div>
								</td>
						</tr>


						<tr>
								<td width="30%" class="tableHeader">

										<div class="gisReportTableBevelCell">
											<digi:trn>Activity</digi:trn>
										</div>

								</td>
								<td width="20%" class="tableHeader">

										<div class="gisReportTableBevelCell">
											<digi:trn>Sector(s)</digi:trn>
										</div>

								</td>
								<td width="20%" class="tableHeader">

										<div class="gisReportTableBevelCell">
											Donor(s)
										</div>

								</td>
								<td width="10%"  class="tableHeader">

										<div class="gisReportTableBevelCell">
											Commitments
										</div>

								</td>
								<td width="10%" class="tableHeader">

										<div class="gisReportTableBevelCell">
											Disbursements
										</div>

								</td>
								<td width="10%" class="tableHeader">

										<div class="gisReportTableBevelCell">
											Expenditures
										</div>

								</td>
								
						</tr>
						</thead>
                                                
                                                <tbody>
                                              
						<logic:iterate name="sectorDonorWidgetReportForm" property="actSectorDonorFundingInfo" id="activitySectorDonorFunding">
							<tr>
								<td width="30%"  height="40px">
                                    <div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal"  style="overflow: auto">
											<a title="<bean:write name="activitySectorDonorFunding" property="activity.name"/>" href="javascript:showSelActivity(<bean:write name="activitySectorDonorFunding" property="activity.ampActivityId"/>);">
												<bean:write name="activitySectorDonorFunding" property="activity.name"/>
											</a>
										</div>
									</div>
								</td>
							
								<td width="20%" align="left" height="40px">
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal" style="overflow: auto">
											<logic:present name="activitySectorDonorFunding" property="sectors">
												<logic:notEmpty name="activitySectorDonorFunding" property="sectors">
													<ul>
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
                                <td width="20%" align="left"  height="40px" >
									<div class="gisReportTableBevelCellContainer">
										<div class="gisReportTableBevelCell gisReportTableBevelCellBgNormal" style="overflow: auto">
											<logic:present name="activitySectorDonorFunding" property="donorOrgs">
												<logic:notEmpty name="activitySectorDonorFunding" property="donorOrgs">
													<ul>
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
								<td width="10%" height="40px">
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
								<td width="10%"  height="40px">
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
								<td width="10%"  height="40px">
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


