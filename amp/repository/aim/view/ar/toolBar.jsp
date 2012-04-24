<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>	
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>



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
	<digi:trn key="aim:report:logostatementpaneltitle">Logo/Statement Panel</digi:trn>
</c:set>
<c:set var="statement">
	<digi:trn key="aim:report:statement">Statement</digi:trn>
</c:set>
<c:set var="logo">
	<digi:trn key="aim:report:logo">Logo</digi:trn>
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
	width: 115px;
	background: #f2f2f2; 
	background-color: #f2f2f2f; 
	padding: 3px 3px 3px 3px; 
	position: relative; 
	top: 10px; 
	left: 10px;
	bottom: 100px;	
	border:1px solid #CCCCCC;	
}
.toolbartable{
}
-->
</style>
<% String ampReportId=request.getParameter("ampReportId");
	if(ampReportId==null) ampReportId=(String)request.getAttribute("ampReportId");
	 request.setAttribute("ampReportId",ampReportId);
   String viewParam="";
   if("reset".equals(request.getParameter("view"))) viewParam="?view=reset";
   String viewParamXLS="/xlsExport.do";      
   String viewParamPDF="/pdfExport.do";
   String viewParamCSV="/csvExport.do"+viewParam;
   String viewParamTree="/viewNewAdvancedReport.do"+(viewParam.equals("") || viewParam.equals("?view=reset")?"?":"&")+"viewFormat=tree";
   String viewParamPrint="/viewNewAdvancedReport.do"+(viewParam.equals("") || viewParam.equals("?view=reset")?"?":"&")+"viewFormat=print";
%>
<script type="text/javascript">
	function toggleActionForm(type) {
		var options = false;
		if (type == null) return false;
		var form=document.getElementById("exportSettingsForm");
		if (type == "xls") {
			form.action = "/aim"+"<%=viewParamXLS%>";
		} else if (type == "pdf") {
			form.action = "/aim"+"<%=viewParamPDF%>";
		}
		<feature:display name="Show Options on Export" module="Report and Tab Options">
			options = true;
		</feature:display>
		
		if (options){
			showMyPanel(0, 'logoStatement');
			return false;
		}else{
			form.submit();
		}
		
		form.submit();
	}
</script>
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

		<td noWrap align=left valign="center">		
			<a href="#" target="_blank" onclick="toggleActionForm('pdf'); return false;" title="<digi:trn>Download as PDF</digi:trn>">
				<digi:img hspace="2" vspace="2"src="module/aim/images/pdf_icon.gif" border="0" alt="Export to PDF" />
			</a>
		</td>

		<td noWrap align=left valign="center">		
			<a href="#" target="_blank" onclick="toggleActionForm('xls'); return false;" title="<digi:trn>Download as XLS</digi:trn>">
				<digi:img hspace="2" vspace="2" src="module/aim/images/xls_icon.jpg" border="0" alt="Export to Excel" />
			</a>
		</td>

		<td noWrap align=left valign="center">
			<digi:link href="<%=viewParamCSV%>" paramName="ampReportId" paramId="ampReportId" target="_blank" title="Download as CSV">
				<digi:img style="margin-bottom:5px" hspace="2" vspace="2" src="module/aim/images/csv_icon.png" border="0" alt="Export to CSV" />
			</digi:link>
		</td>
		
		<feature:display name="Show Printer Friendly option" module="Public Reports">
			<td noWrap align=left valign="center">
				<digi:link href="#" paramName="ampReportId" paramId="ampReportId" onclick="javascript:openPrinter(); return false;" title="Print">
					<digi:img width="17" height="20" hspace="2" vspace="2" src="img_2/ico-print.png" border="0" alt="Printer Friendly" />
				</digi:link>
			</td>
		</feature:display>
					
		<module:display name="Map Module">
			<td noWrap align=left valign="center">
				<a href="/esrigis/mainmap.do?exportreport=true" target="_blank" title="<digi:trn>Export to Map</digi:trn>">
					<img alt="Export to Map" src="module/aim/images/globe-icon_2.png" hspace="2" vspace="2" border="0">
				</a>
			</td>
		</module:display>
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
				<input type="hidden" name="ampReportId" value="<%=ampReportId%>" />
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
					<tr>
						<td align="center" colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td align="center" colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td align="center" colspan="2">
							<html:submit styleClass="dr-menu buton" style="padding-bottom: 2px; padding-top: 2px;" 
								onclick="javascript:hidePanel(0);">
								<digi:trn key="rep:pop:ApplyLogoStatement">Apply</digi:trn>
							</html:submit>
							<html:reset styleClass="dr-menu buton" style="padding-bottom: 2px; padding-top: 2px;" 
								onclick="javascript:hidePanel(0);">
								<digi:trn key="rep:pop:CancelLogoStatement">Cancel</digi:trn>
							</html:reset>
						</td>
					</tr>
				</table>
			</digi:form>
		</div>			        
	</div>		        
</div>
