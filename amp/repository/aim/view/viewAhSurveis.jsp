<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<digi:instance property="aimViewAhSurveisForm" />

<jsp:include page="teamPagesHeader.jsp" flush="true" />

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
  <tr>
    <td class=r-dotted-lg width=14>&nbsp;</td>
    <td align=left class=r-dotted-lg vAlign=top width=750>
      <table cellPadding=5 cellSpacing=0 width="100%">
        <tr>
          <td valign="bottom" class="crumb" >
            <c:set var="translation">
              <digi:trn key="aim:clickToViewMyDesktop">Click here to view admin home page</digi:trn>
            </c:set>
            <digi:link href="/admin.do" styleClass="comment" title="${translation}" >
              <digi:trn key="aim:adminHomeLink">Admin home</digi:trn>
            </digi:link>
            &gt;
            <digi:trn key="aim:allParisReportsList">Paris Indicator Reports List</digi:trn>
          </td>
        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td height=16 align="center" vAlign="middle">
            <span class=subtitle-blue>
              <digi:trn key="aim:parisIndcReports">Paris Indicator Reports</digi:trn>
            </span>
          </td>
        </tr>
        <tr>
          <td noWrap width=650 vAlign="top">
            <table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
              <tr bgColor=#f4f4f2>
                <td>&nbsp;</td>
              </tr>
              <tr bgColor=#f4f4f2>
                <td valign="top">
                  <table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%">
                    <tr>
                      <td bgColor=#ffffff class=box-border>
                        <table border=0 cellPadding=3 cellSpacing=5 class=box-border width="100%" >
                          <tr bgColor=#dddddb>
                            <td bgColor=#dddddb align="center" height="20">
                              <b>
                                <digi:trn key="aim:parisIndcReportsList">
                                List of Paris Indicator reports
                                </digi:trn>
                              </b>
                            </td>
                          </tr>
                          <c:if test="${empty aimViewAhSurveisForm.surveis}">
                            <tr>
                              <td>
                                <b>
                                  <font color="red">
                                    <digi:trn key="aim:noParisIndcRecordFound">No Aid Effectiveness Indicator found</digi:trn>
                                  </font>
                                </b>
                              </td>
                            </tr>
                          </c:if>
                          <c:if test="${!empty aimViewAhSurveisForm.surveis}">
                            <c:forEach var="report" items="${aimViewAhSurveisForm.surveis}">
                              <c:if test="${report.indicatorCode != '4' && report.indicatorCode != '5aii' && report.indicatorCode != '5bii' && report.indicatorCode != '9'&& report.indicatorCode != '10a' && report.indicatorCode != '10b'}">
                                <tr>
                                  <td>
                                    <IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10 />
                                    <strong>[${report.indicatorCode}]</strong>
                                    <c:set var="translation">
                                      <digi:trn key="aim:clickToViewReport">Click here view Report</digi:trn>
                                    </c:set>
                                    <a href="/aim/viewAhSurveyFormulas.do?indId=${report.ampIndicatorId}">
                                      <digi:trn key="aim:${report.nameTrn}">
                                      ${report.name}
                                      </digi:trn>
                                    </a>
                                  </td>
                                </tr>
                              </c:if>
                            </c:forEach>
                          </c:if>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td bgColor=#f4f4f2>
                &nbsp;
                </td>
              </tr>
            </table>
</td>
        </tr>
      </table>
</td>
  </tr>
</table>




