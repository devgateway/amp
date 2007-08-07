<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

	function openPage()
	{
	 	//openResisableWindow(1000, 768);
		<digi:context name="clearVal" property="context/module/moduleinstance/htmlPhysicalComponentReport.do" />
		document.aimMulitlateralbyDonorForm.action = "<%= clearVal %>";
		document.aimMulitlateralbyDonorForm.target = "_blank";
		document.aimMulitlateralbyDonorForm.submit();
	}

		function clearFilter()
	{
		<digi:context name="clearVal" property="context/module/moduleinstance/viewPhysicalComponentReport.do" />
		document.aimMulitlateralbyDonorForm.action = "<%= clearVal %>";
		document.aimMulitlateralbyDonorForm.target = "_self";
		document.aimMulitlateralbyDonorForm.submit();
	}

	function popup_pdf() {
		openResisableWindow(800, 600);
		<digi:context name="pdf" property="context/module/moduleinstance/physicalComponentReportPdf.do" />
		document.aimMulitlateralbyDonorForm.action = "<%= pdf %>";
		document.aimMulitlateralbyDonorForm.target = popupPointer.name;
		document.aimMulitlateralbyDonorForm.submit();
	}

	
	/* CSV function start  */

		function popup_csv() {
		openResisableWindow(800, 600);
		<digi:context name="csv" property="context/module/moduleinstance/physicalComponentReportXls.do?docType=csv" />
		document.aimMulitlateralbyDonorForm.action = "<%= csv %>";
		document.aimMulitlateralbyDonorForm.target = popupPointer.name;
		document.aimMulitlateralbyDonorForm.submit();
	}
	/* CSV function end  */

	function popup_xls() {
		openResisableWindow(800, 600);
		<digi:context name="xls" property="context/module/moduleinstance/physicalComponentReportXls.do?docType=xls" />
		document.aimMulitlateralbyDonorForm.action = "<%= xls %>";
		document.aimMulitlateralbyDonorForm.target = popupPointer.name;
		document.aimMulitlateralbyDonorForm.submit();
	}

	


	function popup_warn() {
		alert("Year Range selected should NOT be Greater than 3 Years.");
	}


</script>

<digi:errors/>
<digi:instance property="aimMulitlateralbyDonorForm" />

<jsp:include page="teamPagesHeader.jsp" flush="true" />
<table width="772" border="0" cellpadding="10" cellspacing="0" bgcolor="#FFFFFF">
<tr>
	<td width="14" class="r-dotted-lg">&nbsp;</td>
	<td width="750" align="left" valign="top" >
	<table width="100%"  border="0" cellpadding="5" cellspacing="0">
           <tr>
              <td valign="bottom" class="crumb" >
<c:set var="translation">
	<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
</c:set>
                <digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
				<digi:trn key="aim:MyDesktop">My Desktop</digi:trn>
				</digi:link> &gt; 
					 <!--<digi:link href="/viewMyDesktop.do" styleClass="comment">Reports</digi:link> &gt;-->
					 <c:set var="translation">
						<digi:trn key="aim:clickToViewAllReports">Click here to view list of all Reports </digi:trn>
	  				 </c:set>
					 <digi:link href="/viewTeamReports.do" styleClass="comment" title="${translation}">
					<digi:trn key="aim:AllReports">All Reports</digi:trn>
					 </digi:link> &gt; &nbsp;
					<bean:write name="aimMulitlateralbyDonorForm" property="perspective"/>&nbsp;
					<digi:trn key="aim:perspective">perspective</digi:trn></td>
              <td width="2">&nbsp;</td>
            </tr>
			<tr>
				<td colspan=3 class=subtitle-blue align=center>
					<digi:trn key="aim:physicalComponentReport">
					<bean:write name="aimMulitlateralbyDonorForm" property="reportName" />
					</digi:trn>
				</td>
			</tr>
			<tr>
				<td colspan=3 class=box-title align=center>
					<bean:write name="aimMulitlateralbyDonorForm" property="workspaceType" />&nbsp; <bean:write name="aimMulitlateralbyDonorForm" property="workspaceName" />&nbsp; 
				<!--	<digi:trn key="aim:team">Team</digi:trn> -->
				</td>
			</tr>

			<logic:notEmpty name="aimMulitlateralbyDonorForm"  property="multiReport">
			<tr>
				<td>
						<img src="../ampTemplate/images/print_icon.gif">
				<digi:link href="/htmlPhysicalComponentReport.do" target="_blank">
					Print
				</digi:link>
				</td>
			</tr>
			</logic:notEmpty>


<logic:notEmpty name="aimMulitlateralbyDonorForm" property="multiReport">
<!--  PDF/XLS Links -->		
			<tr>	

			<logic:greaterThan name="aimMulitlateralbyDonorForm" property="fiscalYrRange" value="3">
				<td valign="bottom" class="crumb">
					<logic:notEmpty name="aimMulitlateralbyDonorForm" property="multiReport">
						<img src="../ampTemplate/images/pdf_icon.gif" border=0>
						<c:set var="translation">
							<digi:trn key="aim:clickToCreateReportInPDF">Click here to Create Report in Pdf </digi:trn>
						</c:set>
						<digi:link href="" onclick="popup_warn(''); return false;" title="${translation}"> 
							<digi:trn key="aim:createReportInPdf">Create Report in Pdf.</digi:trn> 	
						</digi:link>
		        	</logic:notEmpty>
                </td>
            </logic:greaterThan>    

			<logic:lessEqual name="aimMulitlateralbyDonorForm" property="fiscalYrRange" value="3">
				<td valign="bottom" class="crumb" >
					<logic:notEmpty name="aimMulitlateralbyDonorForm" property="multiReport">
					<img src="../ampTemplate/images/pdf_icon.gif" border=0>	
						<c:set var="translation">
							<digi:trn key="aim:clickToCreateReportInPDF">Click here to Create Report in Pdf </digi:trn>
						</c:set>
						<digi:link href="" onclick="popup_pdf(''); return false;" title="${translation}"> 
							<digi:trn key="aim:createReportInPdf">Create Report in Pdf.</digi:trn> 	
						</digi:link>
                	</logic:notEmpty>
                </td>
            </logic:lessEqual>    

           </tr>
			<tr>
						<td valign="bottom" class="crumb">
							<logic:notEmpty name="aimMulitlateralbyDonorForm" property="multiReport">
							<img src="../ampTemplate/images/xls_icon.jpg" border=0>	
								<c:set var="translation">
									<digi:trn key="aim:clickToCreateReportInExcel">Click here to Create Report in Excel </digi:trn>
								</c:set>
								<digi:link href="" onclick="popup_xls(''); return false;" title="${translation}">
									<digi:trn key="aim:createReportInXls">Create Report in Xls.</digi:trn>	
								</digi:link>
		                	</logic:notEmpty>
		                </td>

            </tr>

			<!-- CSV link -->
			<tr>

			        <td valign="bottom" class="crumb" >
					
					<img src="../ampTemplate/images/icon_csv.gif" border=0>
					<c:set var="translation">
						<digi:trn key="aim:clickToCreateReportInCVS">Click here to Create Report in CSV </digi:trn>
					</c:set>
					<digi:link href="" onclick="popup_csv(''); return false;" title="${translation}">
					 	<digi:trn key="aim:createReportInCsv">Create Report in CSV.</digi:trn>
					</digi:link>
					
			
            </td>
            </tr>

<!--  PDF/XLS Links -->		
</logic:notEmpty>

<digi:form action="/viewPhysicalComponentReport.do" >

			<tr>
			<td width="9"></td>

<bean:define id="fcount" name="aimMulitlateralbyDonorForm" property="filterCnt" type="java.lang.Integer" /> 
<%
	int fcnt = fcount.intValue();
	System.out.println("FC = " +fcnt);
	
%>              
            <table border="0" cellspacing="0" cellpadding="2">
			<TR>
			<logic:greaterThan name="aimMulitlateralbyDonorForm" property="filterCnt" value="0" >
			<TD bgColor="black" colspan=<%= fcnt+1%> ></TD>
			</logic:greaterThan>
			</TR>

			<tr bgcolor="#c0c0c0" height=30>

						<logic:equal name="aimMulitlateralbyDonorForm" property="filterFlag" value="true">
						<td>
							<html:select styleClass="dr-menu" property="perspectiveFilter">
								<html:option value="DN">Donor View</html:option>
								<html:option value="MA"><digi:trn key="aim:MOFED">Mofed</digi:trn></html:option>
							</html:select>
						</td>
						</logic:equal>

				<!--		<logic:equal name="aimMulitlateralbyDonorForm" property="adjustmentFlag" value="true">
						<td>
							<html:select styleClass="dr-menu" property="ampAdjustmentId">
								<html:option value="1">Actual</html:option>
								<html:option value="0">Planned</html:option>
							</html:select>
						</td>
						</logic:equal>-->

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="currencyColl">
						<td>
							<html:select property="ampCurrencyCode" styleClass="dr-menu" >
								<html:optionsCollection name="aimMulitlateralbyDonorForm" property="currencyColl" value="currencyCode" label="currencyName"/> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="fiscalYears">
						<td>
							<html:select property="fiscalCalId" styleClass="dr-menu">
								<html:optionsCollection name="aimMulitlateralbyDonorForm" property="fiscalYears" value="ampFiscalCalId" label="name"/> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="ampFromYears">
						<td>
							<html:select property="ampFromYear" styleClass="dr-menu">

								<html:options name="aimMulitlateralbyDonorForm" property="ampFromYears" /> 
							</html:select>
						</td>							
	 					</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="ampToYears">
						<td>
							<html:select property="ampToYear" styleClass="dr-menu">

								<html:options name="aimMulitlateralbyDonorForm" property="ampToYears" /> 
							</html:select>
						</td>							
	 					</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="regionColl">
						<td>
							<html:select property="ampLocationId" styleClass="dr-menu" >
								<option value="All">All Regions</option>
									<html:optionsCollection name="aimMulitlateralbyDonorForm" property="regionColl" value="name" 
									label="name"  /> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="modalityColl">
						<td>						
							<html:select property="ampModalityId" styleClass="dr-menu" >
								<option value="0">All Financing Instruments</option>
								<html:optionsCollection name="aimMulitlateralbyDonorForm" property="modalityColl" value="ampModalityId" label="name" /> 
							</html:select>
						</td>
		 				</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="donorColl">
						<td>
							<html:select property="ampOrgId" styleClass="dr-menu" >
								<option value="0">All Donors</option>
								<html:optionsCollection name="aimMulitlateralbyDonorForm" property="donorColl" value="ampOrgId" label="acronym"/> 
							</html:select>
						<td>
						</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="statusColl">
						<td>
							<html:select property="ampStatusId" styleClass="dr-menu" >
								<option value="0">All Status</option>
								<html:optionsCollection name="aimMulitlateralbyDonorForm" property="statusColl" value="ampStatusId" label="name"  /> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="sectorColl">
						<td>
							<html:select property="ampSectorId" styleClass="dr-menu" >
								<option value="0">All Sectors</option>
								<html:optionsCollection name="aimMulitlateralbyDonorForm" property="sectorColl" value="ampSectorId" label="name" /> 
							</html:select>
						</td>
						</logic:notEmpty>
						<logic:equal name="aimMulitlateralbyDonorForm" property="goFlag" value="true">
						<td>
						<input type="button" name="GOButton" value=" GO " class="dr-menu" onclick="clearFilter()">
						</td>
						</logic:equal>
                </tr>
              </table></td>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
            </tr>
       <!--     <tr>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
			<td width="2" valign="middle">&nbsp;</td>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
            </tr>-->
		
			<tr>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
              <td >
			  <table width="100%"  border="0" cellpadding="0" cellspacing="1" bgcolor="8B8B83">
            <tr bgcolor="#FFFFFF">
              <td colspan="100"><table border="0" cellspacing="0" cellpadding="0">
                  <tr bgcolor="#C9C9C7">
                  <td bgcolor="#C9C9C7" class="box-title">&nbsp;
					<digi:trn key="aim:physicalComponentReport">Physical Component Report </digi:trn>
                  </td>
                 <td bgcolor="#FFFFFF"><img src="../ampTemplate/images/corner-r.gif" width="17" height="17"></td>
                 </tr>
      				</table>

</tr>
<tr bgcolor="#F4F4F2">
	<td align="center" height="21"><div align="center"><strong>
					Serial No
					</strong></div>
					</td>
					<td align="center" height="21" ><div align="center"><strong> 
					Program/Project Component
					</strong></div>
					</td>
					
					<td align="center" height="21" > <div align="center"><strong>
					Commitment Date
					</strong>
					</td>
					<td align="center" height="21" > <div align="center"><strong>
					Completion Date
					</strong></div>
					</td>
					<td align="center" height="21"> <div align="center"><strong>
					Total Commitments
					</strong></div>
					</td>
					<td align="center" height="21" ><div align="center"> <strong>
					Cumulative Disbursements
					</strong></div>
					</td>
					<td align="center" height="21"> <div align="center"><strong>
						Remaining Balance
					</strong></div>
					</td>
					<td align="center" height="21"> <div align="center"><strong>
					
					Status
					</strong></div>
					</td>
					<td align="center" height="21"><div align="center"><strong>
					
					Issues
					</strong></div>
					</td>
					<td align="center" height="21"><div align="center"><strong>
					
					Measures to Be Taken
					</strong></div>
					</td>
					<td align="center" height="21"><div align="center"><strong>
					Responsible Actors
					</strong></div>
					</td>
		
</tr>
	
	<logic:empty name="aimMulitlateralbyDonorForm" property="multiReport"> 
		<tr><td bgcolor="#ffffff" align="center" colspan="11">
		<b>
		<digi:trn key="aim:noRecords">No Records</digi:trn>
		</b>
		</td></tr>
		</logic:empty>
		
	<logic:notEmpty name="aimMulitlateralbyDonorForm"  property="multiReport">
	<logic:iterate name="aimMulitlateralbyDonorForm"  property="multiReport" id="multiReport" type="org.digijava.module.aim.helper.multiReport">
	<tr bgcolor="white">
		
			<td align="left" colspan="11" height="21"> 
				<strong><bean:write name="multiReport" property="teamName"/></strong>
			</td>
		</tr>
	<logic:iterate name="multiReport"  property="donors" id="donors" type="org.digijava.module.aim.helper.AmpTeamDonors">
		<tr bgcolor="white">
		<td align="center"><bean:write name="donors" property="donorCount"/></td>
			<td align="left" colspan="11" height="21"> 
				<strong><bean:write name="donors" property="donorAgency"/></strong>
			</td>
		</tr>

		<logic:iterate name="donors"  property="project" id="project" type="org.digijava.module.aim.helper.Project">
			<tr bgcolor="white">
			<td align="center"><bean:write name="donors" property="donorCount"/>.<bean:write name="project" property="count"/></td>
					<td align="left" height="21" > 
						<bean:write name="project" property="name"/> <br>
						<b>Description:</b>
						<bean:write name="project" property="descriptionPDFXLS"/>
					</td>
				
					<td align="left" height="21" width="39" valign="top">
						<bean:write name="project" property="signatureDate" />
					</td>
					<td align="left" height="21" width="40" valign="top"> 
						<bean:write name="project" property="actualCompletionDate" />
					</td>
					
					<td align="right" height="21" width="45" valign="top"> 
						<bean:write name="project" property="acCommitment" />
					</td>
					<td align="right" height="21" width="40" valign="top"> 
						<bean:write name="project" property="acDisbursement" />
					</td>
					<td align="right" height="21" width="77" valign="top">
						<bean:write name="project" property="acUnDisbursement" />
					</td>
					<td align="left" height="21" width="91" valign="top"><b>
						<bean:write name="project" property="status"/></b> :<br>
						<logic:empty name="project" property="progress">&nbsp;
					</logic:empty>
					<logic:notEmpty name="project" property="progress">
					<logic:iterate name="project" id="progress" property="progress"> <%=progress%>	
					<br><br>
					</logic:iterate>
					</logic:notEmpty>
					</td>
					<td align="left" height="21" >
						<logic:empty name="project" property="issues">&nbsp;
					</logic:empty>
					<logic:notEmpty name="project" property="issues">
					<logic:iterate name="project" id="issues" property="issues"> <%=issues%>	
					<br>
					</logic:iterate>
					</logic:notEmpty>
					</td>
					<td align="left" height="21" >
						<logic:empty name="project" property="measures">&nbsp;
					</logic:empty>
					<logic:notEmpty name="project" property="measures">
					<logic:iterate name="project" id="measures" property="measures"> <%=measures%>	
					<br>
					</logic:iterate>
					</logic:notEmpty>
					</td>
					<td align="left" height="21" >
					<logic:empty name="project" property="responsibleActor">&nbsp;
					</logic:empty>
					<logic:notEmpty name="project" property="responsibleActor">
					<logic:iterate name="project" id="responsibleActor" property="responsibleActor"> <%=responsibleActor%>	
					<br>
					</logic:iterate>
					</logic:notEmpty>
					</td>
			</tr>
		</logic:iterate>
		</logic:iterate>
	</logic:iterate>
	</logic:notEmpty>			
</table>
            </td>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
            </tr>

		  </td>
		</tr>

</table>

</digi:form>



