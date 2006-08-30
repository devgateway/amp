<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<% String ampReportId=request.getParameter("ampReportId");
	 request.setAttribute("ampReportId",ampReportId);
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
				<digi:link href="/pdfExport.do" paramName="ampReportId" paramId="ampReportId">
				Export to PDF
				</digi:link>
				</TD>
				
				

              </TR>
                  
                  </TBODY></TABLE>
</TD>
</TR>
</TBODY>
</TABLE>
                  