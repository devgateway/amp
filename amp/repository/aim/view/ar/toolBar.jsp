<%@page import="org.digijava.module.aim.helper.GlobalSettingsConstants"%>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>	
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>


<c:set var="reportStatement">
	<digi:trn>This Report was created by AMP</digi:trn>
</c:set>
<%
String locale = org.digijava.kernel.util.RequestUtils.getNavigationLanguage(request).getCode();
String countryName = FeaturesUtil.getCurrentCountryName();
String currentDate = java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL, new java.util.Locale(locale)).format(new java.util.Date());
%>
<c:set var="enable">
	<digi:trn key="aim:report:enable">Enable</digi:trn>
</c:set>
<c:set var="disable">
	<digi:trn key="aim:report:disable">Disable</digi:trn>
</c:set>
<c:set var="hheader">
	<digi:trn key="aim:report:header">Header</digi:trn>
</c:set>
<c:set var="footer">
	<digi:trn key="aim:report:footer">Footer</digi:trn>
</c:set>
<c:set var="displayampstatement">
	<digi:trn key="aim:report:displayampstatement">Displays the AMP statement</digi:trn>
</c:set>
<c:set var="displayofficialamplogo">
	<digi:trn key="aim:report:displayofficialamplogo">Displays the Official AMP Logo</digi:trn>
</c:set>
<c:set var="logostatementpaneltitle">
	<digi:trn key="aim:report:logostatementpaneltitle">Excel Export Options</digi:trn>
</c:set>
<c:set var="statement">
	<digi:trn key="aim:report:statement">Statement</digi:trn>
</c:set>
<c:set var="logo">
	<digi:trn key="aim:report:logo">Logo</digi:trn>
</c:set>
<c:set var="publicPortalModeHelp">
	<digi:trn key="aim:report:publicportalmodehelp">No header is exported and column names are machine friendly </digi:trn>
</c:set>

<script type="text/javascript">

function addLoadEvent(func) {
	  var oldonload = window.onload;
	  if (typeof window.onload != 'function') {
	    window.onload = func;
	  } else {
	    window.onload = function() {
	      if (oldonload) {
	        oldonload();
	      }
	      func();
	    }
	  }
	}

function addpanel(){
	YAHOO.namespace("YAHOO.amp");	
	YAHOO.amp.panel = new Array(3);
	YAHOO.amp.panel[0]		= new YAHOO.widget.Panel("aPanel0", {
		width:"400px", 			
		fixedcenter: true, 
		constraintoviewport: true, 
		underlay:"shadow", 
		modal: true,
		close:true, 
		visible:false, 
		draggable:true} );			
	YAHOO.amp.panel[0].setHeader("${logostatementpaneltitle}");
	YAHOO.amp.panel[0].setBody("Empty");
	YAHOO.amp.panel[0].setFooter("");
	YAHOO.amp.panel[0].render(document.body);
	//
	YAHOO.amp.panel[1]		= new YAHOO.widget.Panel("aPanel1", {
		width:"450px", 			
		fixedcenter: true, 
		constraintoviewport: true, 
		underlay:"shadow", 
		modal: true,
		close:true, 
		visible:false, 
		draggable:true} );			
	YAHOO.amp.panel[1].setHeader("${statement}");
	YAHOO.amp.panel[1].setBody("Empty");
	YAHOO.amp.panel[1].setFooter("");
	YAHOO.amp.panel[1].render(document.body);
	//
	YAHOO.amp.panel[2]		= new YAHOO.widget.Panel("aPanel2", {
		width:"300px", 			
		fixedcenter: true, 
		constraintoviewport: true, 
		underlay:"shadow", 
		modal: true,
		close:true, 
		visible:false, 
		draggable:true} );			
	YAHOO.amp.panel[2].setHeader("${logo}");
	YAHOO.amp.panel[2].setBody("Empty");
	YAHOO.amp.panel[2].setFooter("");
	YAHOO.amp.panel[2].render(document.body);
}

addLoadEvent(addpanel);
</script>

<script type="text/javascript">
	YAHOO.namespace("YAHOO.amp");

	function showMyPanel(panelNum, elementId) {
		setPanelBody(panelNum, elementId);
		showPanel(panelNum);
	}
	function setPanelBody(panelNum, elementId){
		var element				= document.getElementById(elementId);
		element.style.display	= "inline";
		element.parentNode.removeChild(element);
		YAHOO.amp.panel[panelNum].setBody(element);
	}
	function showPanel (panelNum) {
		YAHOO.amp.panel[panelNum].show();
	}
	function hidePanel (panelNum) {
		YAHOO.amp.panel[panelNum].hide();
	}
</script>
<style>
<!--
.toolbar{
	/* width: 140px; */
	background: #f2f2f2; 
	background-color: #f2f2f2f; 
	padding: 3px 3px 3px 3px; 
	position: relative; 
	top: 10px; 
	left: 10px;
	bottom: 100px;	
	border:1px solid #CCCCCC;
	display: inline-block;
	margin-bottom: 15px;
}
.toolbartable{
}
-->
</style>
<%
	// ugly copy-paste from RangePicker.jsp
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
	if (request.getParameter("ampReportId") == null)
	{
		// try to save the day, else we are screwed: some actions do not keep ampReportId
		if (ReportContextData.getFromRequest().getReportMeta() != null && ReportContextData.getFromRequest().getReportMeta().getAmpReportId() != null)
			request.setAttribute("ampReportId", ReportContextData.getFromRequest().getReportMeta().getAmpReportId());
		else
			request.setAttribute("ampReportId", ReportContextData.getFromRequest().getContextId()); // last resort
	}
	else
		request.setAttribute("ampReportId", request.getParameter("ampReportId"));
	
	String ampReportId = request.getAttribute("ampReportId").toString();
				
	String viewParam="";
	if("reset".equals(request.getParameter("view")))
	viewParam="?view=reset";
	
	String rcidParam = "&reportContextId=" + ReportContextData.getCurrentReportContextId(request, true);
				
	String viewParamXLS="/xlsExport.do";
	String viewParamRichXLS="/xlsExport.do?richReport=true";
	String viewParamPlainXLS="/xlsExport.do?plainReport=true";
	String viewParamPDF="/pdfExport.do";
	String viewParamCSV="/csvExport.do"+viewParam;
	String viewParamTree="/viewNewAdvancedReport.do"+(viewParam.equals("") || viewParam.equals("?view=reset")?"?":"&")+"viewFormat=tree" + rcidParam;
	String viewParamPrint="/viewNewAdvancedReport.do"+(viewParam.equals("") || viewParam.equals("?view=reset")?"?":"&")+"viewFormat=print" + rcidParam;
%>

<script type="text/javascript">
	function toggleActionForm(type) {
		var options = false;
		if (type == null) return false;
		var form=document.getElementById("exportSettingsForm");
		$(form).find('#richExportRow').css('display', type == 'xls' ? 'table-row' : 'none'); // hide the "rich export" option for the non-Excel-export options
		if (type == "xls") {
			form.action = "/aim"+"<%=viewParamXLS%>";
		} else if (type == 'richXls'){
			form.action = "/aim"+"<%=viewParamRichXLS%>";
		}
		if (type == "plainXls") {
			form.action = "/aim"+"<%=viewParamPlainXLS%>";
		} else if (type == "pdf") {
			form.action = "/aim"+"<%=viewParamPDF%>";
		}
		<feature:display name="Show Options on Export" module="Report and Tab Options">
			if (type != 'richXls')
				options = true;
		</feature:display>
		
		if (options){
			showPublicPortalModeOption(type == "xls");
			showMyPanel(0, 'logoStatement');
			return false;
		}else{
			form.submit();
		}
		
		form.submit();
	}
	
	function showPublicPortalModeOption(visible){
		if (visible){
			$("#publicPortalModeOptionRow").css("visibility","visible");
		}else{
			$("#publicPortalModeOptionRow").css("visibility","hidden");
		}
	}
</script>
<style>
@-moz-document url-prefix() {

    .imagecsv{margin-bottom:5px;}

}

</style>
<div class="toolbar" align="center">
<table border="0" align="center">
	<tr>
		<!-- 
			<td noWrap align=left valign="center">	
				<digi:img src="module/aim/images/close.gif" border="0" alt="Close Report"/>
					<a href="javascript:window.close();"><digi:trn key="rep:tool:CloseReport">Close Report</digi:trn></a>
				</td>					
			<td noWrap align=left valign="center">	
				<digi:img src="module/aim/images/reload.gif" border="0" alt="Reload Report"/>
			<a href="javascript:window.location.reload();"><digi:trn key="rep:tool:ReloadReport">Reload Report</digi:trn></a></td>				
		-->

		<td noWrap align=left valign="middle">	
			<logic:notEqual name="viewable" property="totalUniqueRows" value="0">	
				<a href="#" target="_blank" onclick="toggleActionForm('pdf'); return false;" title="<digi:trn>Download as PDF</digi:trn>">
				    <c:set var="title"><digi:trn>Export to PDF</digi:trn></c:set>
					<digi:img hspace="2" vspace="2" src="module/aim/images/pdf_icon.gif" border="0" alt="${title}" />
				</a>
			</logic:notEqual>
			<logic:equal name="viewable" property="totalUniqueRows" value="0">
			    <c:set var="title"><digi:trn>Report is empty. Nothing to export</digi:trn></c:set>
				<digi:img hspace="2" vspace="2" src="module/aim/images/pdf_icon_gray.gif" border="0" title="${title}" />
			</logic:equal>
		</td>

		<td noWrap align=left valign="middle">
			<logic:notEqual name="viewable" property="totalUniqueRows" value="0">			
				<a href="#" target="_blank" onclick="toggleActionForm('xls'); return false;" title="<digi:trn>Download as XLS</digi:trn>">
				    <c:set var="title"><digi:trn>Export to Excel</digi:trn></c:set>
					<digi:img hspace="2" vspace="2" src="module/aim/images/xls_icon.jpg" border="0" title="${title}" />
				</a>
			</logic:notEqual>
			<logic:equal name="viewable" property="totalUniqueRows" value="0">
				<c:set var="title"><digi:trn>Report is empty. Nothing to export</digi:trn></c:set>
				<digi:img hspace="2" vspace="2" src="module/aim/images/xls_icon_gray.gif" border="0" title="${title}" />
			</logic:equal>
		</td>

		<td noWrap align=left valign="middle">		
			<logic:notEqual name="viewable" property="totalUniqueRows" value="0">	
				<a href="#" target="_blank" onclick="toggleActionForm('plainXls'); return false;" title="<digi:trn>Download as Plain XLS</digi:trn>">
				    <c:set var="title"><digi:trn>Export to Excel as Plain Report</digi:trn></c:set>
					<digi:img hspace="2" vspace="2" src="module/aim/images/xls_plain_icon.jpg" border="0" alt="${title}" />
				</a>
			</logic:notEqual>
			<logic:equal name="viewable" property="totalUniqueRows" value="0">
				<c:set var="title"><digi:trn>Report is empty. Nothing to export</digi:trn></c:set>
				<digi:img hspace="2" vspace="2" src="module/aim/images/xls_plain_icon_gray.gif" border="0" title="${title}" />
			</logic:equal>
		</td>

		<%--<td noWrap align=left valign="middle" style="background-color: #CFCFCF">		
			<a href="#" target="_blank" onclick="toggleActionForm('richXls'); return false;" title="<digi:trn>Download as Rich XLS</digi:trn>">
			    <c:set var="title"><digi:trn>Export to Excel</digi:trn></c:set>
				<img src="/TEMPLATE/ampTemplate/module/aim/images/xls_icon.jpg" border="0" hspace="2" vspace="2" alt="Export as Rich Excel" />
			</a>
		</td> --%>
				
		<c:set var="downloadAsCsv">
			<digi:trn>Download as CSV</digi:trn>
		</c:set>

		<c:set var="exportToCsv">
			<digi:trn>Export to CSV</digi:trn>
		</c:set>

		<c:set var="printTrn">
			<digi:trn>Print</digi:trn>
		</c:set>

		<c:set var="printerFriendly">
			<digi:trn>Printer Friendly</digi:trn>
		</c:set>
								
		<td noWrap align=left valign="center">
			<logic:notEqual name="viewable" property="totalUniqueRows" value="0">
				<digi:link href="<%=viewParamCSV%>" paramName="ampReportId" paramId="ampReportId" target="_blank" title="${downloadAsCsv}">
					<digi:img styleClass="imagecsv" hspace="2" vspace="2" src="module/aim/images/csv_icon.png" border="0" alt="${exportToCsv}" />
				</digi:link>
			</logic:notEqual>
			<logic:equal name="viewable" property="totalUniqueRows" value="0">
			    <c:set var="title"><digi:trn>Report is empty. Nothing to export</digi:trn></c:set>
				<digi:img styleClass="imagecsv" hspace="2" vspace="2" src="module/aim/images/csv_icon_gray.png" border="0" title="${title}"/>
			</logic:equal>
		</td>
		
		<feature:display name="Show Printer Friendly option" module="Public Reports">
			<td noWrap align=left valign="center">
				<logic:notEqual name="viewable" property="totalUniqueRows" value="0">
					<digi:link href="#" paramName="ampReportId" paramId="ampReportId" onclick="javascript:openPrinter(); return false;" title="${printTrn}">
						<digi:img width="17" height="20" hspace="2" vspace="2" src="img_2/ico-print.png" border="0" alt="${printerFriendly}" />
					</digi:link>
				</logic:notEqual>
				<logic:equal name="viewable" property="totalUniqueRows" value="0">
					<digi:img width="17" height="20" hspace="2" vspace="2" src="img_2/ico-print_gray.png" border="0" title="Report is empty. Nothing to export"/>
				</logic:equal>
			</td>
		</feature:display>

	</tr>
</table>
</div>
<br>

<script type="text/javascript">
function openPrinter(){
	//alert('<%=viewParamPrint%>');
	window.open('<%=viewParamPrint%>','mywindow','toolbar=yes,location=yes,directories=yes,status=yes,menubar=yes,scrollbars=yes,copyhistory=yes,resizable=yes');
}
</script>


<div id="statementPopup" class="invisible-item">
	<table cellpadding="5" cellspacing="5" border="0" width="100%">
		<tr>
			<td align="center"> 
				${reportStatement}&nbsp;<%=countryName%>&nbsp;<%=currentDate%>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td align="center">
				<html:reset styleClass="dr-menu buton" style="padding-bottom: 2px; padding-top: 2px;" 
					onclick="javascript:hidePanel(1);">
					<digi:trn key="rep:pop:CloseLogoStatement">Close</digi:trn>
				</html:reset>
			</td>
		</tr>
	</table>
</div>

<div id="logoPopup" class="invisible-item">
	<table cellpadding="5" cellspacing="5" border="0" width="100%">
		<tr>
			<td align="center"> 
				<digi:img hspace="2" vspace="2" src="module/aim/images/dgf_logo.jpg" border="0" />
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td align="center">
				<html:reset styleClass="dr-menu buton" style="padding-bottom: 2px; padding-top: 2px;" 
					onclick="javascript:hidePanel(2);">
					<digi:trn key="rep:pop:CloseLogoStatement">Close</digi:trn>
				</html:reset>
			</td>
		</tr>
	</table>
</div>

<div id="logoStatement" class="invisible-item" >
	<div align="center">
			<digi:form action="<%=viewParamPDF%>" method="post" styleId="exportSettingsForm">			
				<input type="hidden" name="viewParam" value="<%=viewParam%>" />
				<input type="hidden" name="ampReportId" value="${ampReportId}" />
				<table cellpadding="5" cellspacing="5" border="0" width="100%">
					<tr>
						<td> 
							<digi:img hspace="2" vspace="2" src="module/aim/images/help.gif" border="0" onclick="javascript:showMyPanel(1, 'statementPopup');" title="${displayampstatement}"  />
							<digi:trn key="rep:pop:StatementOptions">Statement Options</digi:trn>
						</td>
						<td>
							<html:select property="statementOptions">
								<html:option value="0">${disable}</html:option>
								<html:option value="1">${enable}</html:option>
							</html:select>
						</td>
					</tr>
					<tr>
						<td> 
							<digi:img hspace="2" vspace="2" width="12px" height="12px" src="module/aim/images/spacer.gif" border="0" />
							<digi:trn key="rep:pop:StatementPositionOptions">Statement Position Options</digi:trn>														
						</td>
						<td>
							<html:select property="statementPositionOptions">
								<html:option value="0">${hheader}</html:option>
								<html:option value="1">${footer}</html:option>
							</html:select>
						</td>
					</tr>
					<tr>
						<td>
							<digi:img hspace="2" vspace="2" width="12px" height="12px" src="module/aim/images/spacer.gif" border="0" />
							<digi:trn key="rep:pop:DateOptions">Date Options</digi:trn>
						</td>
						<td>
							<html:select property="dateOptions">
								<html:option value="0">${disable}</html:option>
								<html:option value="1">${enable}</html:option>
							</html:select>
						</td>
					</tr>
					<tr>
						<td>
							<digi:img hspace="2" vspace="2" src="module/aim/images/help.gif" border="0" onclick="javascript:showMyPanel(2, 'logoPopup');" title="${displayofficialamplogo}" />
							<digi:trn key="rep:pop:LogoOptions">Logo Options</digi:trn>
						</td>
						<td>
							<html:select property="logoOptions">
								<html:option value="0">${disable}</html:option>
								<html:option value="1">${enable}</html:option>
							</html:select>
						</td>
					</tr>
					<tr>
						<td> 
							<digi:img hspace="2" vspace="2" width="12px" height="12px" src="module/aim/images/spacer.gif" border="0" />
							<digi:trn key="rep:pop:LogoPositionOptions">Logo Position Options</digi:trn>														
						</td>
						<td>
							<html:select property="logoPositionOptions">
								<html:option value="0">${hheader}</html:option>
								<html:option value="1">${footer}</html:option>
							</html:select>
						</td>
					</tr>
					<tr id="publicPortalModeOptionRow">
						<td>
							<digi:img hspace="2" vspace="2" src="module/aim/images/help.gif" border="0" title="${publicPortalModeHelp}" />
							<digi:trn key="rep:pop:publicPortalMode">Public Portal Mode</digi:trn>
						</td>
						<td>
							<select name="publicPortalMode">
								<option value="false">${disable}</option>
								<option value="true">${enable}</option>
							</select>
						</td>
					</tr>	
								
					<tr id="richExportRow">
						<td>
							<digi:img hspace="2" vspace="2" width="12px" height="12px" src="module/aim/images/spacer.gif" border="0" />
							<digi:trn key="rep:pop:publicPortalMode">Rich Export Format</digi:trn>
						</td>
						<td>
							<c:set var="richExportEnabled"><%=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.RICH_EXCEL_EXPORT_ENABLED_BY_DEFAULT) %></c:set>
							<select name="richReport">
								<option <c:if test="${richExportEnabled eq 'true'}">selected="selected" </c:if> value="true">${enable}</option>							
								<option <c:if test="${richExportEnabled ne 'true'}">selected="selected" </c:if> value="false">${disable}</option>
							</select>
						</td>
					</tr>				
					<tr><td align="center" colspan="2">&nbsp;</td></tr>
					<tr><td align="center" colspan="2">&nbsp;</td></tr>
					
					<tr>
						<td align="center" colspan="2">
							<html:submit styleClass="dr-menu buton" style="padding-bottom: 2px; padding-top: 2px;" 
								onclick="javascript:showPublicPortalModeOption(false);hidePanel(0);">
								<digi:trn key="rep:pop:ApplyLogoStatement">Apply</digi:trn>
							</html:submit>
							<html:reset styleClass="dr-menu buton" style="padding-bottom: 2px; padding-top: 2px;" 
								onclick="javascript:showPublicPortalModeOption(false);hidePanel(0);">
								<digi:trn key="rep:pop:CancelLogoStatement">Cancel</digi:trn>
							</html:reset>
						</td>
					</tr>
				</table>
			</digi:form>
		</div>			        
	</div>		        
</div>
