<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>	
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>" > .</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/event-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>" >.</script>
<script type="text/javascript" src="<digi:file src="script/yui/yahoo-dom-event.js"/>"></script>
<script type="text/javascript" src="<digi:file src="script/yui/element-beta-min.js"/>"></script>
<script type="text/javascript" src="<digi:file src="script/yui/tabview-min.js"/>"></script>

<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/container.css'/>"> 
<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/tab/assets/tabview.css'/>">

<c:set var="reportStatement">
	<digi:trn key="aim:report:reportstatement">This Report was created by AMP</digi:trn>
</c:set>

<script type="text/javascript">
	YAHOO.namespace("YAHOO.amp");	
	YAHOO.amp.panel		= new YAHOO.widget.Panel("aPanel0", {
			width:"400px", 			
			fixedcenter: true, 
			constraintoviewport: true, 
			underlay:"shadow", 
			modal: true,
			close:true, 
			visible:false, 
			draggable:true} );			
	YAHOO.amp.panel.setHeader("Logo/Statement Panel");
	YAHOO.amp.panel.setBody("Empty");
	YAHOO.amp.panel.setFooter("");
	YAHOO.amp.panel.render(document.body);

</script>

<script type="text/javascript">
	YAHOO.namespace("YAHOO.amp");

	function showMyPanel(panelNum, elementId) {
		setPanelBody(elementId);
		showPanel();
	}
	function setPanelBody(elementId){
		var element				= document.getElementById(elementId);
		element.style.display	= "inline";
		element.parentNode.removeChild(element);
		YAHOO.amp.panel.setBody(element);
	}
	function showPanel () {
		YAHOO.amp.panel.show();
	}
	function hidePanel () {
		YAHOO.amp.panel.hide();
	}
</script>
<style>
<!--
.toolbar{
	width: 90px;
	background: #addadd; 
	background-color: #addadd; 
	padding: 3px 3px 3px 3px; 
	position: relative; 
	top: 10px; 
	left: 10px;
	bottom: 100px;		
}
.toolbartable{
	border-color: #FFFFFF;
	border-width: 2px;
	border-bottom-width: 2px; 
	border-right-width: 2px;"
	border-left-width: 2px;
	border-style: solid;
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
		if (type == null) return false;
		if (type == "xls") {
			document.forms[4].action = "/aim"+"<%=viewParamXLS%>";
		} else if (type == "pdf") {
			document.forms[4].action = "/aim"+"<%=viewParamPDF%>";
		}
		showMyPanel(0, 'logoStatement');
	}
</script>
<div class="toolbar" align="center">
<table border="0" align="center" bgcolor="#addadd" class="toolbartable">
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
			<a href="#" target="_blank" onclick="toggleActionForm('pdf'); return false;">
				<digi:img width="17" height="20" hspace="2" vspace="2"src="module/aim/images/pdf.gif" border="0" alt="Export to PDF" />
			</a>
		</td>

		<td noWrap align=left valign="center">
			<a href="#" target="_blank" onclick="toggleActionForm('xls'); return false;">
				<digi:img width="17" height="20" hspace="2" vspace="2" src="module/aim/images/excel.gif" border="0" alt="Export to Excel" />
			</a>
		</td>

		<td noWrap align=left valign="center">
			<digi:link href="<%=viewParamCSV%>" paramName="ampReportId" paramId="ampReportId" target="_blank">
				<digi:img width="17" height="20" hspace="2" vspace="2" src="module/aim/images/csv.gif" border="0" alt="Export to CSV" />
			</digi:link>
		</td>
		
		<td noWrap align=left valign="center">
			<digi:link href="#" paramName="ampReportId" paramId="ampReportId" onclick="javascript:openPrinter(); return false;">
				<digi:img width="17" height="20" hspace="2" vspace="2" src="module/aim/images/printer.gif" border="0" alt="Printer Friendly" />
			</digi:link>
		</td>
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


<div id="logoStatement" style="display: none">
	<div align="center">
			<digi:form action="<%=viewParamPDF%>" method="post">			
				<input type="hidden" name="viewParam" value="<%=viewParam%>" />
				<input type="hidden" name="ampReportId" value="<%=ampReportId%>" />
				<table cellpadding="5" cellspacing="5" border="0" width="100%">
					<tr>
						<td> 
							<digi:img hspace="2" vspace="2" src="module/aim/images/help.gif" border="0" alt="Statement Options" />
							<digi:trn key="rep:pop:StatementOptions">Statement Options</digi:trn>
						</td>
						<td>
							<html:select property="statementOptions">
								<html:option value="0">Disable</html:option>
								<html:option value="1">Enable</html:option>
							</html:select>
						</td>
					</tr>
					<tr>
						<td> 
							&nbsp;
							<digi:trn key="rep:pop:StatementPositionOptions">Statement Position Options</digi:trn>														
						</td>
						<td>
							<html:select property="statementPositionOptions">
								<html:option value="0">Header</html:option>
								<html:option value="1">Footer</html:option>
							</html:select>
						</td>
					</tr>
					<tr>
						<td>
							<digi:img hspace="2" vspace="2" src="module/aim/images/help.gif" border="0" alt="Logo Options" />
							<digi:trn key="rep:pop:LogoOptions">Logo Options</digi:trn>
						</td>
						<td>
							<html:select property="logoOptions">
								<html:option value="0">Disable</html:option>
								<html:option value="1">Enable</html:option>
							</html:select>
						</td>
					</tr>
					<tr>
						<td> 
							&nbsp;
							<digi:trn key="rep:pop:LogoPositionOptions">Logo Position Options</digi:trn>														
						</td>
						<td>
							<html:select property="logoPositionOptions">
								<html:option value="0">Header</html:option>
								<html:option value="1">Footer</html:option>
							</html:select>
						</td>
					</tr>
					<tr>
						<td>
							<digi:img hspace="2" vspace="2" src="module/aim/images/help.gif" border="0" alt="Date Options" />
							<digi:trn key="rep:pop:DateOptions">Date Options</digi:trn>
						</td>
						<td>
							<html:select property="dateOptions">
								<html:option value="0">Disable</html:option>
								<html:option value="1">Enable</html:option>
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
								onclick="javascript:hidePanel();">
								<digi:trn key="rep:pop:ApplyLogoStatement">Apply</digi:trn>
							</html:submit>
							<html:reset styleClass="dr-menu buton" style="padding-bottom: 2px; padding-top: 2px;" 
								onclick="javascript:hidePanel();">
								<digi:trn key="rep:pop:CancelLogoStatement">Cancel</digi:trn>
							</html:reset>
						</td>
					</tr>
				</table>
			</digi:form>
		</div>			        
	</div>		        
</div>
