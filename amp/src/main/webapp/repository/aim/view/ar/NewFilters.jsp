<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>


	<digi:form action="/viewNewAdvancedReport.do">
	<html:hidden property="createdReportId" />
		
      <bean:define id="fcount" name="aimAdvancedReportForm" property="filterCnt" type="java.lang.Integer" /> 
      <bean:define id="reportCurrencyCode" name="aimAdvancedReportForm" toScope="session" property="ampCurrencyCode" type="java.lang.String" /> 

		<%
			int fcnt = fcount.intValue();
		%>              
      <table border="0" cellspacing="0" cellpadding="2" width="100%">
			<tr bgcolor="#c0c0c0" height=30><td>
				<logic:equal name="aimAdvancedReportForm" property="filterFlag" value="true">

						<html:select styleClass="dr-menu" property="perspectiveFilter">
						<html:option value="DN">Donor View</html:option>
						<html:option value="MA"><digi:trn key="aim:MOFED">MOFED</digi:trn></html:option>
						</html:select>
				</logic:equal>
				<logic:notEmpty name="aimAdvancedReportForm" property="currencyColl">
						<html:select property="ampCurrencyCode" styleClass="dr-menu" >
							<html:optionsCollection name="aimAdvancedReportForm" property="currencyColl" value="currencyCode" label="currencyName"/> 
						</html:select>
				</logic:notEmpty>

				<logic:notEmpty name="aimAdvancedReportForm" property="fiscalYears">		
						<html:select property="fiscalCalId" styleClass="dr-menu">
							<html:optionsCollection name="aimAdvancedReportForm" property="fiscalYears" value="ampFiscalCalId" label="name"/> 
						</html:select>
				</logic:notEmpty>

				<logic:notEmpty name="aimAdvancedReportForm" property="ampFromYears">		
						<html:select property="ampFromYear" styleClass="dr-menu">
							<html:option value="0">From Year</html:option>
							<html:options name="aimAdvancedReportForm" property="ampFromYears" /> 
						</html:select> 
	 			</logic:notEmpty>

				<logic:notEmpty name="aimAdvancedReportForm" property="ampToYears">		
						<html:select property="ampToYear" styleClass="dr-menu">
							<html:option value="0">To Year</html:option>
							<html:options name="aimAdvancedReportForm" property="ampToYears" /> 
						</html:select> 
	 			</logic:notEmpty>
				<logic:notEmpty name="aimAdvancedReportForm" property="modalityColl">
						<html:select property="ampModalityId" styleClass="dr-menu" >
							<option value="0">All Financing Instruments</option>
							<html:optionsCollection name="aimAdvancedReportForm" property="modalityColl" value="ampModalityId" label="name" /> 
						</html:select>
		 		</logic:notEmpty>



				</td></tr>
				<tr bgcolor="#c0c0c0"><td>
				<logic:notEmpty name="aimAdvancedReportForm" property="regionColl">		
						<html:select property="regions" styleClass="dr-menu" multiple="true" size="3">
							<html:optionsCollection name="aimAdvancedReportForm" property="regionColl" value="name" label="name"/> 
						</html:select>
				</logic:notEmpty>
				
				<logic:notEmpty name="aimAdvancedReportForm" property="donorColl" >
						<html:select property="donors" styleClass="dr-menu" multiple="true" size="3">
							<html:optionsCollection name="aimAdvancedReportForm" property="donorColl" value="ampOrgId" label="acronym"/> 
						</html:select>
				</logic:notEmpty>

				<logic:notEmpty name="aimAdvancedReportForm" property="statusColl">
						<html:select property="statuses" styleClass="dr-menu" multiple="true" size="3">
							<html:optionsCollection name="aimAdvancedReportForm" property="statusColl" value="ampStatusId" label="name"  /> 
						</html:select>
				</logic:notEmpty>

				<logic:notEmpty name="aimAdvancedReportForm" property="sectorColl">
						<html:select property="sectors" styleClass="dr-menu" multiple="true" size="3">
							<html:optionsCollection name="aimAdvancedReportForm" property="sectorColl" value="ampSectorId" label="name" /> 
						</html:select>
				</logic:notEmpty>
				<logic:notEmpty name="aimAdvancedReportForm" property="pdfPageSize">
						<html:select property="pdfPageSize" styleClass="dr-menu">
						<html:option value="default">PDF Page Size</html:option>
						<html:option value="A4">A4</html:option>
						<html:option value="A3">A3</html:option>
						<html:option value="A2">A2</html:option>
						<html:option value="A1">A1</html:option>						
						<html:option value="A0">A0</html:option>						
						</html:select>
				</logic:notEmpty>
				<logic:equal name="aimAdvancedReportForm" property="goFlag" value="true">
						<input type="button" name="GoButton" value=" GO " class="dr-menu" onclick="submitFilter()">
						<input type="button" name="reset" value="Reset" class="dr-menu" onclick="clearFilter()">

				</logic:equal>
          </td></tr>

      	<tr>
      		<td>
      	
      		</td>
      	</tr>    
      
        </table>
    </tr>
	<script language="JavaScript">
	function submitFilter()
	{
		<digi:context name="clearVal" property="context/module/moduleinstance/viewNewAdvancedReport.do" />
		
		url = "<%=clearVal %>?ampReportId=" + document.getElementsByName("aimAdvancedReportForm")[1].createdReportId.value;
		document.getElementsByName("aimAdvancedReportForm")[1].action = url;
		document.getElementsByName("aimAdvancedReportForm")[1].target = "_self";
		document.getElementsByName("aimAdvancedReportForm")[1].submit();
	}

	function clearFilter()
	{
		<digi:context name="clearVal" property="context/module/moduleinstance/viewNewAdvancedReport.do" />
		url = "<%=clearVal %>?view=reset&ampReportId=" + document.getElementsByName("aimAdvancedReportForm")[1].createdReportId.value;
		document.getElementsByName("aimAdvancedReportForm")[1].action = url;
		document.getElementsByName("aimAdvancedReportForm")[1].target = "_self";
		document.getElementsByName("aimAdvancedReportForm")[1].submit();
	}


	function popup_pdf() {
		<digi:context name="pdf" property="context/module/moduleinstance/advancedReportPdf.do?docType=pdf" />
		openResizableWindowWithURL(800, 600, "<%= pdf %>", document.getElementsByName('aimAdvancedReportForm')[1]);
	}

	function popup_xls() {
		<digi:context name="xls" property="context/module/moduleinstance/advancedReportPdf.do?docType=excel" />
		openResizableWindowWithURL(800, 600, "<%= xls %>", document.getElementsByName('aimAdvancedReportForm')[1]);
	}

	function popup_csv() {
		<digi:context name="csv" property="context/module/moduleinstance/advancedReportPdf.do?docType=csv" />
		openResizableWindowWithURL(800, 600, "<%= csv %>", document.getElementsByName('aimAdvancedReportForm')[1]);
	}

	function popup_warn() {
		alert("Year Range selected should NOT be Greater than 4 Years.");
	}
</script>
			


</digi:form>
