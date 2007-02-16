<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>

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

   <TABLE class=toolbar id=toolbartable cellSpacing=0 cellPadding=0
      width="100%" bgColor=#c0c0c0 border=0 name="toolbartable">
        <TBODY>
        <TR align=left>
          <TD width="100%">

            <TABLE cellSpacing=0 cellPadding=0 border=0>
              <TBODY>
              <TR> 
				<TD noWrap align=left valign="center">	
				<digi:img src="module/aim/images/close.gif" border="0" alt="Close Report"/>
				<a href="javascript:window.close();">Close Report</a></TD>				

				<TD noWrap align=left valign="center">	
				<digi:img src="module/aim/images/reload.gif" border="0" alt="Reload Report"/>
				<a href="javascript:window.location.reload();">Reload Report</a></TD>				


				<TD noWrap align=left valign="center">	
				<digi:img src="images/pdf_icon.gif" border="0" alt="Export to PDF"/>
				<digi:link href="<%=viewParamPDF%>" paramName="ampReportId" paramId="ampReportId" target="_blank">
				Export to PDF
				</digi:link>
				</TD>
			
				<TD noWrap align=left valign="center">	
				<digi:img src="images/xls_icon.jpg" border="0" alt="Export to Excel"/>
				<digi:link href="<%=viewParamXLS%>" paramName="ampReportId" paramId="ampReportId" target="_blank">
				Export to Excel
				</digi:link>
				</TD>
				

				<TD noWrap align=left valign="center">	
				<digi:img src="images/icon_csv.gif" border="0" alt="Export to CSV"/>
				<digi:link href="<%=viewParamCSV%>" paramName="ampReportId" paramId="ampReportId" target="_blank"> 
				Export to CSV
				</digi:link>
				</TD>
				

				<TD noWrap align=left valign="center">	
				<digi:img src="images/print_icon.gif" border="0" alt="Printer Friendly"/>
				<digi:link href="<%=viewParamPrint%>" paramName="ampReportId" paramId="ampReportId" target="_blank">
				Printer Friendly
				</digi:link>
				</TD>
				
				<TD noWrap align=left valign="center">	
				<digi:img src="images/folders.png" border="0" alt="Drilldown"/>
				<digi:link href="<%=viewParamTree%>" paramName="ampReportId" paramId="ampReportId" target="_blank">
				Drilldown
				</digi:link>
				</TD>
				


              </TR>
                  
                  </TBODY></TABLE>
</TD>
</TR>
</TBODY>
</TABLE>
