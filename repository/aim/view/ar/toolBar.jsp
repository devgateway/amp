<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>	

<style>
<!--
.toolbartable{
	border-top: #222E5D 3px solid;
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
<div class="yuiamp-skin-amp" style="clear:both;width: 285px;margin-left:10px;margin-bottom: 25px;margin-top: 25px">
	<div id="mainmenuExport" class="yuiampmenu">
		<div class="bd">
			<ul class="first-of-type">
				<li class="yuiampmenuitem" style="float:left;">
					<digi:link styleClass="yuiampmenuitemlabel" style="border-right:1px solid white;" href="<%=viewParamPDF%>" paramName="ampReportId" paramId="ampReportId" target="_blank">
						<digi:trn key="rep:tool:pdf">PDF</digi:trn>
					</digi:link>
				</li>
				<li class="yuiampmenuitem" style="float:left;">
					<digi:link styleClass="yuiampmenuitemlabel" style="border-right:1px solid white;" href="<%=viewParamXLS%>" paramName="ampReportId" paramId="ampReportId" target="_blank">
						<digi:trn key="rep:tool:excel">EXCEL</digi:trn>
					</digi:link>
				</li>
				<li class="yuiampmenuitem" style="float:left;">
					<digi:link styleClass="yuiampmenuitemlabel" style="border-right:1px solid white;" href="<%=viewParamCSV%>" paramName="ampReportId" paramId="ampReportId" target="_blank">
						<digi:trn key="rep:tool:csv">CSV</digi:trn>
					</digi:link>
				</li>
				<li class="yuiampmenuitem" style="float:left;">
					<digi:link styleClass="yuiampmenuitemlabel" style="border-right:1px solid white;" href="<%=viewParamPrint%>" paramName="ampReportId" paramId="ampReportId" target="_blank">
						<digi:trn key="aim:tool:printer">PRINTER</digi:trn>
					</digi:link>
				</li>
			</ul>
		</div>
	</div>
</div>

<script language="javascript">
//Run initialization for menu
	var oMenuBar = new YAHOOAmp.widget.MenuBar("mainmenuExport", {
	autosubmenudisplay: true});
	oMenuBar.render();
</script>