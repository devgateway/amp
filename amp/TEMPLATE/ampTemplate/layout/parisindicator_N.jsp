<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%@ taglib uri="http://digijava.org/digi" prefix="digi"%>
<%@ taglib uri="http://digijava.org/aim" prefix="aim" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" href="<digi:file src="/WEB-INF/jsp/parisindicator/view/css/pi_styles.css"/>">

<jsp:include page="/WEB-INF/jsp/aim/view/teamPagesHeader.jsp"  />

<%
String auxReportId = request.getParameter("reportId");
request.getParameter("print");
%>

<digi:form action="/parisindicator.do" type="org.digijava.module.parisindicator.form.PIForm" name="parisIndicatorForm">
	<table border="0" cellpadding="10" cellspacing="0" bgcolor="#FFFFFF">
	    <tr>
	        <td width="1000" align="left" valign="top" border="1" style="padding-left: 5px;">
	            <table width="100%" border="0" cellpadding="5" cellspacing="0">
		            <tr>
		                <td class="subtitle-blue-3" style="width: 70%; text-align: left;">
		                    <digi:trn key="aim:parisIndicator">Paris Indicator</digi:trn> <digi:trn key="aim:report">Report</digi:trn>&nbsp;<bean:write name="parisIndicatorForm" property="piReport.indicatorCode"/>
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
		                                <strong><p><img src="/TEMPLATE/ampTemplate/images/info.png" width="15" height="15">&nbsp;<bean:write name="parisIndicatorForm" property="piReport.name"/></p></strong>
		                            </td>
		                        </tr>
		                    </table>
		                </td>
		            </tr>
		            <tr>
		                <td class="td_right_left1">
                            <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="3">
                                <jsp:include page="/WEB-INF/jsp/parisindicator/view/parisindicator_3.jsp"></jsp:include>
                            </logic:equal>
                            <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="4">
                                <jsp:include page="/WEB-INF/jsp/parisindicator/view/parisindicator_4.jsp"></jsp:include>
                            </logic:equal>
                            <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="5a">
                                <jsp:include page="/WEB-INF/jsp/parisindicator/view/parisindicator_5a.jsp"></jsp:include>
                            </logic:equal>
                            <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="5b">
                                <jsp:include page="/WEB-INF/jsp/parisindicator/view/parisindicator_5b.jsp"></jsp:include>
                            </logic:equal>
                            <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="6">
                                <jsp:include page="/WEB-INF/jsp/parisindicator/view/parisindicator_6.jsp"></jsp:include>
                            </logic:equal>
                            <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="7">
                                <jsp:include page="/WEB-INF/jsp/parisindicator/view/parisindicator_7.jsp"></jsp:include>
                            </logic:equal>
                            <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="9">
                                <jsp:include page="/WEB-INF/jsp/parisindicator/view/parisindicator_9.jsp"></jsp:include>
                            </logic:equal>
                            <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="10a">
                                <jsp:include page="/WEB-INF/jsp/parisindicator/view/parisindicator_10a.jsp"></jsp:include>
                            </logic:equal>
                            <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="10b">
	                        	<jsp:include page="/WEB-INF/jsp/parisindicator/view/parisindicator_10b.jsp"></jsp:include>
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
