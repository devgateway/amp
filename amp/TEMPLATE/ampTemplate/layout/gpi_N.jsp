<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/aim.tld" prefix="aim" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/fmt.tld" prefix="fmt" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" href="<digi:file src="/repository/gpi/view/css/gpi_styles.css"/>">

<jsp:include page="/repository/aim/view/teamPagesHeader.jsp"  />

<%
String auxReportId = request.getParameter("reportId");
request.getParameter("print");
%>

<digi:form action="/gpi.do" type="org.digijava.module.gpi.form.GPIForm" name="gpiForm">
	<table border="0" cellpadding="10" cellspacing="0" bgcolor="#FFFFFF">
	    <tr>
	        <td width="1000" align="left" valign="top" border="1" style="padding-left: 5px;">
	            <table width="100%" border="0" cellpadding="5" cellspacing="0">
		            <tr>
		                <td class="subtitle-blue-3" style="width: 70%; text-align: left;">
		                    <digi:trn>Global Partnership Indicator</digi:trn> <digi:trn key="aim:report">Report</digi:trn>&nbsp;<bean:write name="gpiForm" property="gpiReport.indicatorCode"/>
		                </td>
		            </tr>
		            <tr>
		                <td colspan="3" class="box-title" align="center"></td>
		            </tr>
		            <tr>
		                <td width="90%" class="td_top1" style="padding-left:2px;" >
		                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
		                        <tr>
		                            <td colspan="2" ></td>
		                        </tr>
		                        <tr>
		                            <td style="padding:5">
		                                <strong><p><img src="/TEMPLATE/ampTemplate/images/info.png" width="15" height="15">&nbsp;<bean:write name="gpiForm" property="gpiReport.name"/></p></strong>
		                            </td>
		                        </tr>
		                    </table>
		                </td>
		            </tr>
		            <tr>
		                <td class="td_right_left1">
                            <logic:equal name="gpiForm" property="gpiReport.indicatorCode" value="1">
                                <jsp:include page="/repository/gpi/view/gpi_1.jsp"></jsp:include>
                            </logic:equal>
                            <logic:equal name="gpiForm" property="gpiReport.indicatorCode" value="5a">
                                <jsp:include page="/repository/gpi/view/gpi_5a.jsp"></jsp:include>
                            </logic:equal>
                            <logic:equal name="gpiForm" property="gpiReport.indicatorCode" value="6">
                                <jsp:include page="/repository/gpi/view/gpi_6.jsp"></jsp:include>
                            </logic:equal>
                            <logic:equal name="gpiForm" property="gpiReport.indicatorCode" value="9b">
                                <jsp:include page="/repository/gpi/view/gpi_9b.jsp"></jsp:include>
                            </logic:equal>
	                    </td>
	                </tr>
	                <tr>
                        <td class="td_bottom1">&nbsp;</td>
                    </tr>
				</table>
			</td>
		</tr>
	</table>
</digi:form>