<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@taglib uri="/taglib/struts-html" prefix="html"%>
<%@taglib uri="/taglib/digijava" prefix="digi"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script language="javascript" type="text/javascript">
function setActionMethod(methodName) {
  document.getElementsByName('actionMethod')[0].value=methodName;
  return true;
}

function browseProgram(programId) {
  setActionMethod('display');
  document.getElementsByName('currentProgramId')[0].value=programId;
  document.forms['aimNationalPlaningDashboardForm'].submit();
  return false;
}

function showChart(argument) {
  setActionMethod('display');
  document.getElementsByName('showChart')[0].value=argument;
  document.forms['aimNationalPlaningDashboardForm'].submit();
  return false;
}
</script>
<digi:form action="/nationalPlaningDashboard.do">
  <html:hidden property="actionMethod"/>
  <html:hidden property="currentProgramId"/>
  <html:hidden property="showChart"/>
  <TABLE width="100%" cellspacing="1" cellpadding="4" valign="top" align="left">
    <TR>
      <TD>
        <TABLE border=0 cellPadding=0 cellSpacing=0 width="100%">
          <TR>
            <TD>
              <TABLE border=0 cellPadding=0 cellSpacing=0>
                <TR>
                  <TD bgColor=#c9c9c7 class=box-title width=80>                    &nbsp;
                    <digi:link href="/viewPortfolioDashboard.do~actId=-1~indId=-1">
                      <digi:trn key="aim:portfolioDashboard">Dashboard</digi:trn>
                    </digi:link>
                  </TD>
                  <TD background="module/aim/images/corner-r.gif" height=17 width=17>                  </TD>
                  <TD bgColor=#c9c9c7 class=box-title width=220 nowrap="nowrap">                    &nbsp;
                    <digi:trn key="aim:npDashboard">National Planing Dashboard</digi:trn>
                  </TD>
                  <TD background="module/aim/images/corner-r.gif" height=17 width=17>                  </TD>
                </TR>
              </TABLE>
            </TD>
          </TR>
          <TR>
            <TD bgColor=#ffffff class=box-border align=left>
              <table width="100%" cellspacing="5" cellpadding="5">
                <tr>
                  <td bgColor=#c9c9c7 class=box-title width=220 nowrap="nowrap">                    &nbsp;
${aimNationalPlaningDashboardForm.currentProgram.name}                  </td>
                  <td bgColor=#c9c9c7 class=box-title width=220 nowrap="nowrap">                    &nbsp;
                    <digi:trn key="aim:npObjectives">National Planing Objectives</digi:trn>
                  </td>
                </tr>
                <tr>
                  <td valign="top" width="550">
                    <table width="100%">
                      <tr>
                        <td align="center" class="box-title" bgcolor="#c9c9c7">
                          <c:if test="${aimNationalPlaningDashboardForm.showChart}">
                            <a href="#" onclick="return showChart(false)">
                              <digi:trn key="aim:npData">Data</digi:trn>
                            </a>
                          </c:if>
                          <c:if test="${!aimNationalPlaningDashboardForm.showChart}">
                            <digi:trn key="aim:npData">Data</digi:trn>
                          </c:if>
                        </td>
                        <td align="center" class="box-title" bgcolor="#c9c9c7">
                          <c:if test="${!aimNationalPlaningDashboardForm.showChart}">
                            <a href="#" onclick="return showChart(true)">
                              <digi:trn key="aim:npChart">Chart</digi:trn>
                            </a>
                          </c:if>
                          <c:if test="${aimNationalPlaningDashboardForm.showChart}">
                            <digi:trn key="aim:npChart">Chart</digi:trn>
                          </c:if>
                        </td>
                      </tr>
                      <c:if test="${aimNationalPlaningDashboardForm.showChart}">
                      <tr>
                        <td colspan="2" align="center">
                        <digi:context name="showChart" property="/module/moduleinstance/nationalPlaningDashboard.do"/>
                        <c:url var="fullShowChartUrl" scope="page" value="${showChart}">
                          <c:param name="actionMethod" value="displayChart" />
                          <c:param name="currentProgramId" value="${aimNationalPlaningDashboardForm.currentProgramId}" />
                        </c:url>
                        <img alt="chart" src="${fullShowChartUrl}" width="500" />
                        </td>
                      </tr>
                      </c:if>
                      <c:if test="${aimNationalPlaningDashboardForm.showChart}">
                      <tr>
                        <td colspan="2" align="center">
                        </td>
                      </tr>
                      </c:if>
                    </table>
                  </td>
                  <td valign="top" class="highlight" align="left" width="100">
                    <ul style="list-style : none; padding-left : 0px;">
                      <c:forEach var="themeMember" items="${aimNationalPlaningDashboardForm.programs}">
                        <li style="left: 0px;"> <c:if test="${themeMember.level > 0}"> <c:forEach begin="1" end="${themeMember.level}"> &nbsp; </c:forEach> </c:if> <a href="#" onclick="return browseProgram(${themeMember.member.ampThemeId})">${themeMember.member.name}</a> <br/> <c:if test="${themeMember.level > 0}"> <c:forEach begin="1" end="${themeMember.level}"> &nbsp; </c:forEach> </c:if> <ul> <c:forEach var="indicator" items="${themeMember.member.indicators}"> <li>${indicator.name}</li> </c:forEach> </ul> </li>
                      </c:forEach>
                    </ul>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</digi:form>
