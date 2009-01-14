<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<digi:instance property="widgetOrgProfileWidgetForm" />
<digi:form action="/orgProfileManager.do~actType=viewAll">



<table width="60%" border="0" cellpadding="15">
	<tr>
		<td>
			<span class="crumb">
              <c:set var="translation">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>
              <html:link  href="/aim/admin.do" styleClass="comment" title="${translation}" >
                <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
              </html:link>&nbsp;&gt;&nbsp;
                <digi:trn key="admin:Navigation:orgProfileManager">Org Profile Manager</digi:trn>
			</span>
		</td>
                <tr>
                    <td>
                        <digi:errors/>
                    </td>
                </tr>
	</tr>
	<tr>
		<td>
			<span class="subtitle-blue"><digi:trn key="widget:orgProfileManager:pageHeader">Org Profile Manager</digi:trn></span>
		</td>
	</tr>
	<tr>
		<td>
			<a href="/widget/orgProfileManager.do~actType=create"><digi:trn key="widget:orgProfileManager:createNew">Create new</digi:trn></a>
		</td>
	</tr>
	<tr>
		<td>
			
			<table border="0" width="60%" align="center" style="font-family:verdana;font-size:11px;">
				<tr bgColor="#d7eafd">
					
					<td nowrap="nowrap" width="80%">
						<strong><digi:trn key="widget:orgProfileManager:type">Type</digi:trn></strong>
                                            </td>
                                            <td nowrap="nowrap">
						<strong><digi:trn key="widget:orgProfileManager:operations">Operations</digi:trn></strong>
					</td>
				</tr>
				<c:forEach var="orgProfile" items="${widgetOrgProfileWidgetForm.orgProfilePages}" varStatus="stat">
					<tr>
			
						<td nowrap="nowrap">
                                                    <c:choose>
                                                        <c:when test="${orgProfile.type==1}">
                                                            Summary
                                                        </c:when>
                                                         <c:when test="${orgProfile.type==2}">
                                                            Type of Aid
                                                        </c:when>
                                                         <c:when test="${orgProfile.type==3}">
                                                            Pledges/Comm/Disb
                                                        </c:when>
                                                         <c:when test="${orgProfile.type==4}">
                                                            ODA Profile
                                                        </c:when>
                                                         <c:when test="${orgProfile.type==5}">
                                                            Sector Breakdown
                                                        </c:when>
                                                        <c:when test="${orgProfile.type==6}">
                                                            Regional Breakdown
                                                        </c:when>   
                                                        <c:otherwise>
                                                            Paris Declaration
                                                        </c:otherwise>
                                                    </c:choose>
				
						</td>
                                               
						<td nowrap="nowrap">
							<a href="/widget/orgProfileManager.do~actType=update~id=${orgProfile.id}">
								<digi:trn key="widget:editLink">Edit</digi:trn>
							</a>
                                                        
							|&nbsp;
							<a href="/widget/orgProfileManager.do~actType=delete~id=${orgProfile.id}">
								<img border="0" src='<digi:file src="images/deleteIcon.gif"/>'>
							</a>
						</td>
					</tr>
				</c:forEach>
                               
			</table>


		</td>
	</tr>
</table>

</digi:form>
