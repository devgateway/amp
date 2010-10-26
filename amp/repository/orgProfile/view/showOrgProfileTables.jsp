<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<digi:instance property="orgProfileNameValueYearForm"/>
<c:set var="colSpan">
    <c:choose>
        <c:when test="${sessionScope.orgProfileFilter.transactionType=='2'&&orgProfileNameValueYearForm.type!= 3}">
            2
        </c:when>
        <c:otherwise>1</c:otherwise>
    </c:choose>
</c:set>
<c:set var="yearRange">
    <c:choose>
     <c:when test="${orgProfileNameValueYearForm.type!= 5&&orgProfileNameValueYearForm.type!= 6}">
        ${sessionScope.orgProfileFilter.yearsInRange}
     </c:when>
        <c:otherwise>
          1
        </c:otherwise>
    </c:choose>
</c:set>
    <table border="0" class="tableElement" bgcolor="#dddddd" width="96%" cellspacing="0" cellpadding="0">
        <tr>
            <th colspan="${6*colSpan}" class="tableHeaderCls">
                <c:choose> 
                    <c:when test="${orgProfileNameValueYearForm.type==2}">
                        <digi:trn>TYPE OF AID</digi:trn>
                    </c:when>
                    <c:when test="${orgProfileNameValueYearForm.type== 3}">
                        <c:set var="charttitle">
                            <c:if test="${sessionScope.orgProfileFilter.pledgeVisible}">
                                Pledges|
                            </c:if>
                             Commitments|Disbursements
                             <c:if test="${sessionScope.orgProfileFilter.expendituresVisible}">
                               |Expenditures
                            </c:if>
                        </c:set>
                        <digi:trn>${charttitle}</digi:trn>
                    </c:when>
                    <c:when test="${orgProfileNameValueYearForm.type==5}">
                        <digi:trn>Sector(s) Breakdown</digi:trn>
                    </c:when>
                    <c:when test="${orgProfileNameValueYearForm.type==6}">
                        <digi:trn>Regional Breakdown</digi:trn>
                    </c:when>
                      <c:when test="${orgProfileNameValueYearForm.type==8}">
                        <digi:trn>Aid Predictability</digi:trn>
                    </c:when>
                    <c:otherwise>
                        <digi:trn>ODA PROFILE</digi:trn>
                    </c:otherwise>
                </c:choose> (${sessionScope.orgProfileFilter.currName}
                <gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS%>" compareWith="true">
                    , <digi:trn>Amounts in Thousands</digi:trn>
                </gs:test>)</th>
        </tr>
        <tr>
            <td class="tableHeaderCls" rowspan="2">
                <c:choose>
                    <c:when test="${orgProfileNameValueYearForm.type==2}">
                        <digi:trn>TYPE OF AID</digi:trn>
                    </c:when>
                     <c:when test="${orgProfileNameValueYearForm.type==4}">
                        <digi:trn>ODA Profile</digi:trn>
                    </c:when>
                     <c:otherwise>
                        <digi:trn>Name</digi:trn>
                    </c:otherwise>
                </c:choose>
            </td>
          <c:set var="startYear">
              <c:choose>
                  <c:when test="${orgProfileNameValueYearForm.type!= 5&&orgProfileNameValueYearForm.type!= 6}">
                      ${sessionScope.orgProfileFilter.year-yearRange}
                  </c:when>
                  <c:otherwise>
                      ${sessionScope.orgProfileFilter.year-yearRange}
                  </c:otherwise>
              </c:choose>
          </c:set>

           <c:forEach var="year" begin="1" end="${yearRange}">
                <td class="tableHeaderCls" colspan="${colSpan}"> <c:out value="${startYear+year}" /></td>
            </c:forEach>

        </tr>
        <tr>
            <c:if test="${orgProfileNameValueYearForm.type!= 3}">
                <c:forEach var="year" begin="0" end="${yearRange-1}">
                    <c:choose>
                        <c:when test="${sessionScope.orgProfileFilter.transactionType=='1'}">
                            <td class="tableHeaderCls"><digi:trn>DISBURSEMENTS</digi:trn></td>
                        </c:when>
                        <c:when test="${sessionScope.orgProfileFilter.transactionType=='0'}">
                            <td class="tableHeaderCls"><digi:trn>COMMITMENTS</digi:trn></td>
                        </c:when>
                        <c:otherwise>
                            <td class="tableHeaderCls"><digi:trn>COMMITMENTS</digi:trn></td>
                            <td class="tableHeaderCls"><digi:trn>DISBURSEMENTS</digi:trn></td>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </c:if>
        </tr>

        <c:forEach items="${orgProfileNameValueYearForm.values}" var="value"  varStatus="status">
            <c:if test="${status.count%2==0}">
                <c:set var="class">
                    tableEven
                </c:set>
                <c:set var="trbgColor">
                    #dbe5f1
                </c:set>
            </c:if>
            <c:if test="${status.count%2==1}">
                <c:set var="class">
                    tableOdd
                </c:set>
                <c:set var="trbgColor">
                    #FFFFFF
                </c:set>
            </c:if>
               <tr class="${class}" onmouseover="this.style.backgroundColor='#a5bcf2'" onmouseout="this.style.backgroundColor='${trbgColor}'">
                <td nowrap>
                    <digi:trn>${value.name}</digi:trn>
                </td>
                <c:forEach var="amount" items="${value.values}">
                    <td align="right">${amount}</td>
                </c:forEach>
            </tr>
        </c:forEach>

    </table>



