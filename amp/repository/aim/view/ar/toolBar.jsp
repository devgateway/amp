<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>	

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
   String viewParamXLS="/xlsExport.do"+viewParam;
   String viewParamPDF="/pdfExport.do"+viewParam;
   String viewParamCSV="/csvExport.do"+viewParam;
   String viewParamTree="/viewNewAdvancedReport.do"+(viewParam.equals("") || viewParam.equals("?view=reset")?"?":"&")+"viewFormat=tree";
   String viewParamPrint="/viewNewAdvancedReport.do"+(viewParam.equals("") || viewParam.equals("?view=reset")?"?":"&")+"viewFormat=print";
%>
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
			<digi:link href="<%=viewParamPDF%>" paramName="ampReportId" paramId="ampReportId" target="_blank">
				<digi:img width="17" height="20" hspace="2" vspace="2"src="module/aim/images/pdf.gif" border="0" alt="Export to PDF" />
			</digi:link>
		</td>

		<td noWrap align=left valign="center">
			<digi:link href="<%=viewParamXLS%>" paramName="ampReportId" paramId="ampReportId" target="_blank">
				<digi:img width="17" height="20" hspace="2" vspace="2" src="module/aim/images/excel.gif" border="0" alt="Export to Excel" />
			</digi:link>
		</td>

		<td noWrap align=left valign="center">
			<digi:link href="<%=viewParamCSV%>" paramName="ampReportId" paramId="ampReportId" target="_blank">
				<digi:img width="17" height="20" hspace="2" vspace="2" src="module/aim/images/csv.gif" border="0" alt="Export to CSV" />
			</digi:link>
		</td>
		
		<td noWrap align=left valign="center">
			<digi:link href="<%=viewParamPrint%>" paramName="ampReportId" paramId="ampReportId" target="_blank">
				<digi:img width="17" height="20" hspace="2" vspace="2" src="module/aim/images/printer.gif" border="0" alt="Printer Friendly" />
			</digi:link>
		</td>
	</tr>
</table>
</div>
<br>