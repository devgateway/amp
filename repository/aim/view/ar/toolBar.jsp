<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>	

<style>
#tablacss{
	width:800px;
	text-align:center;
	background-color:#FFFFFF;
	border-collapse:collapse
}
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
<div style="margin-left:5px;margin-bottom: 40px;margin-top: 25px">
	<table style="tablacss">
  		<tr>
    		<td height="25px" align="center" nowrap="nowrap" style="border:#EAEAEA 1px solid;margin: 5px;padding: 6px">
    			<digi:link href="<%=viewParamPDF%>" paramName="ampReportId" paramId="ampReportId" target="_blank" style="text-decoration: none">
						<digi:img hspace="5px" align="middle" width="21px" height="24px" src="module/aim/images/pdf.gif" border="0" alt="Export to PDF"/>
						<digi:trn key="rep:tool:pdf">PDF</digi:trn>
				</digi:link>
			</td>
    		<td align="center" nowrap="nowrap" style="border:#EAEAEA 1px solid;margin: 5px;padding: 6px;">
    			<digi:link  href="<%=viewParamXLS%>" paramName="ampReportId" paramId="ampReportId" target="_blank" style="text-decoration: none">
					<digi:img hspace="5px" align="middle" src="module/aim/images/excel.gif" border="0" alt="Export to Excel" />
					<digi:trn key="rep:tool:excel">EXCEL</digi:trn>
				</digi:link>
    		</td>
    		<td align="center" nowrap="nowrap" style="border:#EAEAEA 1px solid;margin: 5px;padding: 6px">
    			<digi:link href="<%=viewParamCSV%>" paramName="ampReportId" paramId="ampReportId" target="_blank" style="text-decoration: none">
					<digi:img hspace="5px" align="middle" src="module/aim/images/csv.gif" border="0" alt="Export to CSV" />
					<digi:trn key="rep:tool:csv">CSV</digi:trn>
				</digi:link>
			</td>
    		<td align="center" nowrap="nowrap" style="border:#EAEAEA 1px solid;margin: 5px;padding: 6px">
    			<digi:link  href="<%=viewParamPrint%>" paramName="ampReportId" paramId="ampReportId" target="_blank" style="text-decoration: none">
					<digi:img hspace="5px" align="middle" width="21px" height="24px" src="module/aim/images/printer.gif" border="0" alt="Printer Friendly" />
					<digi:trn key="aim:tool:printer">PRINTER</digi:trn>
				</digi:link>
    		</td>
  		</tr>
	</table>
</div>
