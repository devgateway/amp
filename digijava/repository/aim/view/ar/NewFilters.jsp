<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript">
<!--
	function clearFilter()
	{
		<digi:context name="clearVal" property="context/module/moduleinstance/viewNewAdvancedReport.do" />
		url = "<%=clearVal %>?ampReportId=" + document.aimAdvancedReportForm.createdReportId.value;
		document.aimAdvancedReportForm.action = url;
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}

	function popup_pdf() {
		openResisableWindow(800, 600);
		<digi:context name="pdf" property="context/module/moduleinstance/advancedReportPdf.do?docType=pdf" />
		document.aimAdvancedReportForm.action = "<%= pdf %>";
		document.aimAdvancedReportForm.target = popupPointer.name;
		document.aimAdvancedReportForm.submit();
	}

	function popup_xls() {
		openResisableWindow(800, 600);
		<digi:context name="xls" property="context/module/moduleinstance/advancedReportPdf.do?docType=excel" />
		document.aimAdvancedReportForm.action = "<%= xls %>";
		document.aimAdvancedReportForm.target = popupPointer.name;
		document.aimAdvancedReportForm.submit();
	}

	function popup_csv() {
		openResisableWindow(800, 600);
		<digi:context name="csv" property="context/module/moduleinstance/advancedReportPdf.do?docType=csv" />
		document.aimAdvancedReportForm.action = "<%= csv %>";
		document.aimAdvancedReportForm.target = popupPointer.name;
		document.aimAdvancedReportForm.submit();
	}

	function popup_warn() {
		alert("Year Range selected should NOT be Greater than 4 Years.");
	}
-->
</script>


	<digi:form action="/viewNewAdvancedReport.do" >
	<html:hidden property="createdReportId"/>
	
      <bean:define id="fcount" name="aimAdvancedReportForm" property="filterCnt" type="java.lang.Integer" /> 
		<%
			int fcnt = fcount.intValue();
			System.out.println("FC = " +fcnt);
		%>              
      <table border="0" cellspacing="0" cellpadding="2">
			<tr>
				<logic:greaterThan name="aimAdvancedReportForm" property="filterCnt" value="0" >
					<TD bgColor="black" colspan=<%= fcnt+1%> ></TD>
				</logic:greaterThan>
			</tr>
			<tr bgcolor="#c0c0c0" height=30>	
				<logic:equal name="aimAdvancedReportForm" property="filterFlag" value="true">
					<td>			
						<html:select styleClass="dr-menu" property="perspectiveFilter">
						<html:option value="DN">Donor View</html:option>
						<html:option value="MA">MOFED</html:option>
						</html:select>
					</td>
				</logic:equal>

				<logic:notEmpty name="aimAdvancedReportForm" property="currencyColl">
					<td>						
						<html:select property="ampCurrencyCode" styleClass="dr-menu" >
							<html:optionsCollection name="aimAdvancedReportForm" property="currencyColl" value="currencyCode" label="currencyName"/> 
						</html:select>
					</td>
				</logic:notEmpty>

				<logic:notEmpty name="aimAdvancedReportForm" property="fiscalYears">
					<td>	 					
						<html:select property="fiscalCalId" styleClass="dr-menu">
							<html:optionsCollection name="aimAdvancedReportForm" property="fiscalYears" value="ampFiscalCalId" label="name"/> 
						</html:select>
					</td>
				</logic:notEmpty>

				<logic:notEmpty name="aimAdvancedReportForm" property="ampFromYears">
					<td>	 					
						<html:select property="ampFromYear" styleClass="dr-menu">
							<html:options name="aimAdvancedReportForm" property="ampFromYears" /> 
						</html:select> 
					</td>
	 			</logic:notEmpty>

				<logic:notEmpty name="aimAdvancedReportForm" property="ampToYears">
					<td>	 					
						<html:select property="ampToYear" styleClass="dr-menu">
							<html:options name="aimAdvancedReportForm" property="ampToYears" /> 
						</html:select> 
					</td>
	 			</logic:notEmpty>

				<logic:notEmpty name="aimAdvancedReportForm" property="regionColl">
					<td>						
						<html:select property="ampLocationId" styleClass="dr-menu" >
							<option value="All">All Regions</option>
							<html:optionsCollection name="aimAdvancedReportForm" property="regionColl" value="name" label="name"/> 
						</html:select>
					</td>
				</logic:notEmpty>

				<logic:notEmpty name="aimAdvancedReportForm" property="modalityColl">
					<td>						
						<html:select property="ampModalityId" styleClass="dr-menu" >
							<option value="0">All Financing Instruments</option>
							<html:optionsCollection name="aimAdvancedReportForm" property="modalityColl" value="ampModalityId" label="name" /> 
						</html:select>
					</td>
		 		</logic:notEmpty>

				<logic:notEmpty name="aimAdvancedReportForm" property="donorColl">
					<td>						
						<html:select property="ampOrgId" styleClass="dr-menu" >
							<option value="0">All Donors</option>
							<html:optionsCollection name="aimAdvancedReportForm" property="donorColl" value="ampOrgId" label="acronym"/> 
						</html:select>
					</td>
				</logic:notEmpty>

				<logic:notEmpty name="aimAdvancedReportForm" property="statusColl">
					<td>						
						<html:select property="ampStatusId" styleClass="dr-menu" >
							<option value="0">All Status</option>
							<html:optionsCollection name="aimAdvancedReportForm" property="statusColl" value="ampStatusId" label="name"  /> 
						</html:select>
					</td>
				</logic:notEmpty>

				<logic:notEmpty name="aimAdvancedReportForm" property="sectorColl">
					<td>						
						<html:select property="ampSectorId" styleClass="dr-menu" >
							<option value="0">All Sectors</option>
							<html:optionsCollection name="aimAdvancedReportForm" property="sectorColl" value="ampSectorId" label="name" /> 
						</html:select>
					<td>
				</logic:notEmpty>

				<logic:notEmpty name="aimAdvancedReportForm" property="ampStartDays">
					<a title="<digi:trn key="aim:DateofStart">Date (dd/mm/yy) when the project commenced (effective start date)
					</digi:trn>">
						<td>
							<html:select property="startDay" styleClass="dr-menu">
								<option value="0">DD</option>
								<html:options name="aimAdvancedReportForm" 	property="ampStartDays" /> 
							</html:select>
						</td>
						<td>
	 						<html:select property="startMonth" styleClass="dr-menu">
								<option value="0">MON</option>
	 							<html:option value="1">Jan</html:option>
								<html:option value="2">Feb</html:option>
								<html:option value="3">March</html:option>
								<html:option value="4">April</html:option>
								<html:option value="5">May</html:option>
								<html:option value="6">June</html:option>
								<html:option value="7">July</html:option>
								<html:option value="8">Aug</html:option>
								<html:option value="9">Sept</html:option>
								<html:option value="10">Oct</html:option>
								<html:option value="11">Nov</html:option>
								<html:option value="12">Dec</html:option>
							</html:select>
	 					</td>
						<td>
	 						<html:select property="startYear" styleClass="dr-menu">
								<option value="0">YYYY</option>
								<html:options name="aimAdvancedReportForm" property="ampStartYears" /> 
							</html:select> 
		 				</td>
					</a>
				</logic:notEmpty>
				
				<logic:notEmpty name="aimAdvancedReportForm" property="ampCloseDays">
					<a title="<digi:trn key="aim:DateofClose">Date (dd/mm/yy) in which the project ended or is expected to end</digi:trn>">
						<td>
	 						<html:select property="closeDay" styleClass="dr-menu">
								<option value="0">DD</option>
								<html:options name="aimAdvancedReportForm" property="ampCloseDays" /> 
							</html:select> 
	 					</td>
						<td>
	 						<html:select property="closeMonth" styleClass="dr-menu">
	 							<option value="0">MON</option>
	 							<html:option value="1">Jan</html:option>
								<html:option value="2">Feb</html:option>
								<html:option value="3">March</html:option>
								<html:option value="4">April</html:option>
								<html:option value="5">May</html:option>
								<html:option value="6">June</html:option>
								<html:option value="7">July</html:option>
								<html:option value="8">Aug</html:option>
								<html:option value="9">Sept</html:option>
								<html:option value="10">Oct</html:option>
								<html:option value="11">Nov</html:option>
								<html:option value="12">Dec</html:option>
							</html:select>
	 					</td>
						<td>
							<html:select property="closeYear" styleClass="dr-menu">
								<option value="0">YYYY</option>
								<html:options name="aimAdvancedReportForm" property="ampCloseYears" /> 
							</html:select> 
						</td>
					</a>
				</logic:notEmpty>

				<logic:equal name="aimAdvancedReportForm" property="goFlag" value="true">
					<td>						
						<input type="button" name="GoButton" value=" GO " class="dr-menu" onclick="clearFilter()">
					</td>						
				</logic:equal>
          </tr>
        </table>
    </tr>
			


</digi:form>
