<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@taglib uri="/taglib/struts-html" prefix="html"%>
<%@taglib uri="/taglib/digijava" prefix="digi"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript">
	function newWin(val) {
		<digi:context name="selectLoc" property="context/module/moduleinstance/viewOrganisation.do" />
		var url = "<%= selectLoc %>?ampOrgId=" + val;
		openNewWindow(635, 600);
		//obj.target = popupPointer.name;
		popupPointer.document.location.href = url;
		//obj.href = url;
	}
</script>

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
  setActionMethod('displayWithFilter');
  document.getElementsByName('showChart')[0].value=argument;
  document.forms['aimNationalPlaningDashboardForm'].submit();
  return false;
}

function doFilter() {
  setActionMethod('displayWithFilter');
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
                  <td align="center" class="textalb" height="20" bgcolor="#336699" width=220 nowrap="nowrap">
                    ${aimNationalPlaningDashboardForm.currentProgram.name}
                  </td>
                  <td align="center" class="textalb" height="20" bgcolor="#336699" width=220 nowrap="nowrap">
                    <digi:trn key="aim:npObjectives">National Planing Objectives</digi:trn>
                  </td>
                </tr>
                <tr>
                  <td valign="top" width="550">
                    <table width="100%">
                      <tr>
                        <td colspan="2" align="center">
                          <input type="button" value="GO" onclick="return doFilter()" />
                        </td>
                      </tr>
                      <tr>
                        <td align="center" class="textalb" height="20" bgcolor="#336699">
                          <c:if test="${aimNationalPlaningDashboardForm.showChart}">
                            <a href="#" onclick="return showChart(false)">
                              <digi:trn key="aim:npData">Data</digi:trn>
                            </a>
                          </c:if>
                          <c:if test="${!aimNationalPlaningDashboardForm.showChart}">
                            <digi:trn key="aim:npData">Data</digi:trn>
                          </c:if>
                        </td>
                        <td align="center" class="textalb" height="20" bgcolor="#336699">
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
                      <!-- chart -->
                      <c:if test="${aimNationalPlaningDashboardForm.showChart}">
                      <tr>
                        <td colspan="2" align="center">
                        <digi:context name="showChart" property="/module/moduleinstance/nationalPlaningDashboard.do"/>
                        <c:url var="fullShowChartUrl" scope="page" value="${showChart}">
                          <c:param name="actionMethod" value="displayChart" />
                          <c:param name="currentProgramId" value="${aimNationalPlaningDashboardForm.currentProgramId}" />
                          <c:forEach var="selVal" items="${aimNationalPlaningDashboardForm.selectedIndicators}">
                            <c:param name="selectedIndicators" value="${selVal}" />
                          </c:forEach>
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
                      <!-- end of chart-->
                    </table>
                  </td>
                  <td valign="top" class="highlight" align="left" width="100">
                    <ul style="list-style : none; padding-left : 0px;">
                      <c:forEach var="themeMember" items="${aimNationalPlaningDashboardForm.programs}">
                        <li style="left: 0px;">
                          <c:if test="${themeMember.level > 0}">
                            <c:forEach begin="1" end="${themeMember.level}"> &nbsp; </c:forEach>
                          </c:if>
                          <a href="#" onclick="return browseProgram(${themeMember.member.ampThemeId})">${themeMember.member.name}</a>
                          <br/>
                          <c:if test="${themeMember.level > 0}">
                            <c:forEach begin="1" end="${themeMember.level}"> &nbsp; </c:forEach>
                          </c:if>
                          <c:if test="${aimNationalPlaningDashboardForm.currentProgramId != themeMember.member.ampThemeId}">
                          <ul style="margin-left : 10px;">
                            <c:forEach var="indicator" items="${themeMember.member.indicators}">
                              <li>${indicator.name}</li>
                            </c:forEach>
                          </ul>
                          </c:if>
                          <c:if test="${aimNationalPlaningDashboardForm.currentProgramId == themeMember.member.ampThemeId}">
                          <ul style="list-style : none; margin-left : 10px; padding-left : 0px;">
                            <c:forEach var="indicator" items="${themeMember.member.indicators}">
                              <li>
                              <html:multibox property="selectedIndicators" value="${indicator.ampThemeIndId}" />${indicator.name}
                              </li>
                            </c:forEach>
                          </ul>
                          </c:if>
                        </li>
                      </c:forEach>
                    </ul>
                  </td>
                </tr>
                <tr>
                  <td colspan="2">
                    <table width="100%" cellpadding="4" cellSpacing="1" bgcolor="#ffffff" valign="top" align="left">
                      <tr bgcolor="#DDDDDD">
                        <td class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"
                        onMouseOut="this.className='colHeaderLink'" width="40%" nowrap="nowrap">
                        <digi:trn key="aim:npPlannedActivitiesFor">Planned activities for</digi:trn>: ${aimNationalPlaningDashboardForm.currentProgram.name}
                        </td>

                        <td class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"
                        onMouseOut="this.className='colHeaderLink'" width="40%" nowrap="nowrap">
                        <digi:trn key="aim:npDonor">Donor</digi:trn>
                        </td>

                        <td class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"
                        onMouseOut="this.className='colHeaderLink'" width="40%" nowrap="nowrap">
                        <digi:trn key="aim:npFromYear">From Year</digi:trn>
                        </td>

                        <td class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"
                        onMouseOut="this.className='colHeaderLink'" width="40%" nowrap="nowrap">
                        <digi:trn key="aim:npToYear">To Year</digi:trn>
                        </td>

                        <td class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"
                        onMouseOut="this.className='colHeaderLink'" width="40%" nowrap="nowrap">
                        <digi:trn key="aim:npAmountAndCurrency">Amount&amp;Currency</digi:trn>
                        </td>
                      </tr>
                      <c:forEach var="activity" items="${aimNationalPlaningDashboardForm.activities}">
                        <tr>
                          <td valign="top">${activity.name}</td>
                          <td>
								<c:if test="${!empty activity.funding}">
											<TABLE cellspacing=1 cellpadding=1>
												<c:forEach var="dnr" items="${activity.funding}">
													<TR><TD>
														<a href="javascript:newWin('${dnr.ampDonorOrgId.ampOrgId}')">
														${dnr.ampDonorOrgId.name}</a>
													</TD></TR>
												</c:forEach>
											</TABLE>
										</c:if>
										<c:if test="${empty activity.funding}">
											<digi:trn key="aim:unspecified">Unspecified</digi:trn>
										</c:if>
                          </td>
                          <td valign="top">${activity.activityStartDate}</td>
                          <td valign="top">${activity.originalCompDate}</td>
                          <td valign="top">
                          <fmt:formatNumber var="funAmount" maxFractionDigits="2" minFractionDigits="2" groupingUsed="true" value="${activity.funAmount}" />
                          ${funAmount} ${activity.currencyCode}
                          </td>
                        </tr>
                      </c:forEach>
                    </table>
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
