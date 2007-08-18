<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/advanceReportManager.js"/>"></script>

<script language="JavaScript">
function check(){
	option = -1;
	for (i = document.aimAdvancedReportForm.reportType.length-1; i > -1; i--){
		if (document.aimAdvancedReportForm.reportType[i].checked){
			option = i;
			i = -1;
		}
	}
	if (option == -1){ 
		alert("You must choose a report type!");
		return false;
	}	
	return true;
}
function gotoStep() {
		if (!check())
			return false; 
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=SelectCols" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
		return true;
//	}
}

</script>

<digi:instance property="aimAdvancedReportForm" />
<digi:form action="/advancedReportManager.do" method="post">


<html:hidden property="moveColumn"/>
											<feature:display name="Identification" module="Project ID and Planning">
												<field:display name="Actual Approval Date" feature="Identification" ></field:display>
												<field:display name="Actual Completion Date" feature="Identification"></field:display>
												<field:display name="Actual Start Date" feature="Identification"></field:display>
												<field:display name="Description" feature="Identification"></field:display>
												<field:display name="Objective" feature="Identification"></field:display>					
												<field:display name="Project Id" feature="Identification"></field:display>
												<field:display name="Project Title" feature="Identification"></field:display>
												<field:display name="Status" feature="Identification"></field:display>
												<field:display name="Team" feature="Identification"></field:display>
												<field:display name="A.C. Chapter" feature="Identification"></field:display>
												<field:display name="Accession Instrument" feature="Identification"></field:display>
											</feature:display>
												
											<feature:display name="Location" module="Project ID and Planning">
												<field:display name="Implementation Level" feature="Location"></field:display>
												<field:display name="Region" feature="Location"></field:display>
												<field:display name="Sector" feature="Location"></field:display>
											</feature:display>
											
											<feature:display  name="Funding Organizations" module="Funding">
												<field:display name="Cumulative Commitment" feature="Funding Organizations"></field:display>
												<field:display name="Cumulative Disbursement" feature="Funding Organizations"></field:display>
												<field:display name="Donor Agency" feature="Funding Organizations"></field:display>
												<field:display name="Donor Commitment Date" feature="Funding Organizations"></field:display>
												<field:display name="Donor Group" feature="Funding Organizations"></field:display>
												<field:display name="Financing Instrument" feature="Funding Organizations"></field:display>
												<field:display name="Type Of Assistance" feature="Funding Organizations"></field:display>
											</feature:display>
											
											<feature:display name="Issues" module="Issues">
												<field:display name="Actors" feature="Issues"></field:display>
												<field:display name="Component description" feature="Issues"></field:display>
												<field:display name="Component Name" feature="Issues"></field:display>
												<field:display name="Issues" feature="Issues"></field:display>
												<field:display name="Measures Taken" feature="Issues"></field:display>
												<field:display name="Physical Progress" feature="Issues"></field:display>
												<field:display name="Physical progress description" feature="Issues"></field:display>
												<field:display name="Physical progress title" feature="Issues"></field:display>
											</feature:display>	
											
											<feature:display name="Contact Information" module="Contact Information">
												<field:display name="Contact Name" feature="Contact Information"></field:display>
											</feature:display>
												
											<feature:display name="M & E" module="Trend Analysis and Forecasting">
												<field:display name="Indicator Base Value" feature="M & E"></field:display>
												<field:display name="Indicator Current Value" feature="M & E"></field:display>
												<field:display name="Indicator Target Value" feature="M & E"></field:display>
												<field:display name="Indicator Description" feature="M & E"></field:display>
												<field:display name="Indicator ID" feature="M & E"></field:display>
												<field:display name="Indicator Name" feature="M & E"></field:display>
											</feature:display>
											<feature:display name="Costing" module="Activity Costing">
												<field:display name="Costing Donor" feature="Costing"></field:display>
												<field:display name="Total Costs" feature="Costing"></field:display>
											</feature:display>

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
<tr>
	<td>
		<jsp:include page="teamPagesHeader.jsp" flush="true" />
	</td>
</tr>



<tr>

<td width="100%" vAlign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="770" vAlign="top" align="left" border=0>
	<tr>

	<td class=r-dotted-lg align=left vAlign=top >	&nbsp;</td>
	<td>
		<table>
			<tr>
				<td>
					<table cellPadding=5 cellSpacing=0 width="100%">
						<tr>
							<td height=33><span class=crumb>
								<c:set var="translation">
									<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
								</c:set>
								<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
								<digi:trn key="aim:portfolio">
									Portfolio
								</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp;
								<digi:trn key="aim:reportBuilder:selectReportType">
									Report Builder : Select Report Type
								</digi:trn>					
								&gt;</span>		
							</td>
						</tr>
					</table>	
				</td>
			</tr>
		 	<tr>

				<td height=16 vAlign=right align=center>
					<span class=subtitle-blue>
					<digi:trn key="aim:reportBuilder:selectReportType">
						Report Builder : Select Report Type
					</digi:trn>	
					</span>
				</td>
			</tr>
			<tr colspan="2">
				<td class=box-title align="right" valign="top">
					<img src="module/aim/images/arrow-014E86.gif"><digi:trn key="aim:reportBuilder:reportTypePleaseSelect">Report Type : Please Select</digi:trn>
				<td>
			</tr>
			<TR>
			<TD vAlign="top" align="center">
				<TABLE width="100%" cellSpacing=0 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4" class="box-border-nopadding">
					<TR>
						<TD bgcolor="#f4f4f4">
							<TABLE width="100%" cellSpacing=1 cellPadding=0 valign="top" align="left" bgcolor="#f4f4f4">
								<jsp:include page="AdvancedReportManagerMenu.jsp" flush="true"/>
								<TR bgColor=#f4f4f2>
									<TD vAlign="top" align="left" width="100%">
									</TD>
								</TR>				

								<TR bgColor=#f4f4f2>
									<TD vAlign="top" align="center" width="100%" bgColor=#f4f4f2>
										<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2 >
											<TR>
												<TD width="100%" align="center"  valign=top>
                                                    <br>
                                                    <br>
                                                    <br>
<!-- Radios -->
		
			<table cellPadding=0 cellSpacing=0 vAlign="top" align="center" width="400" bgColor=#f4f4f2 border="0">
			<tr height="120">
			<td>&nbsp;&nbsp;&nbsp;
			</td>
			<td>

			<table cellPadding=0 cellSpacing=1 bgColor=#f4f4f2 border="0">
				<tr>
					<td>
						<html:radio property="reportType" value="donor" >
							<digi:trn key="aim:donorReport">
								Donor Report (Donor Funding)
							</digi:trn>
						</html:radio>
					</td>
				</tr>
				<tr>
					<td>
						<html:radio property="reportType" value="regional" >
							<digi:trn key="aim:regionalReport">
								Regional Report (Regional Funding)
							</digi:trn>
						</html:radio>
					</td>
				</tr>
				<tr>
					<td>
						<html:radio property="reportType" value="component" >
							<digi:trn key="aim:componentReport">
								Component Report (Component Funding)
							</digi:trn>
						</html:radio>
					</td>
				</tr>
				<module:display name="Reports">
				<feature:display module="Reports" name="Contribution Report">
					<tr>
					<td>
						<html:radio property="reportType" value="contribution" >
							<digi:trn key="aim:contributionReport">
								Contribution Report (Activity Contributions)
							</digi:trn>
						</html:radio>
					</td>
					</tr>
				</feature:display>
				</module:display>
			</table>
			</td>
			</tr>
			</table>

                                                    <p>&nbsp;</p>
                                                    <p>&nbsp;</p>
												</TD>
											</TR>
											<tr>
												<td align="right">
													<html:button  styleClass="dr-menu" property="submitButton"  onclick="return quitAdvRptMngr()">
														<digi:trn key="btn:cancel">Cancel</digi:trn> 
													</html:button>
													
													<html:button  styleClass="dr-menu" property="submitButton"  onclick="javascript:gotoStep()">
														<digi:trn key="btn:next">Next</digi:trn> 
													</html:button>

												</td>
											</tr>
										</TABLE>
									</TD>
								</TR>	
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</TD>
			</TR>
		</table>
	</td>	
	<td class=r-dotted-lg align=left vAlign=top >	&nbsp;</td>
</tr>
</table>
</td>	
</TR>
</TABLE>


</digi:form>



