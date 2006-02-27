<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimMyDesktopForm" />

<table width="100%" valign="top" align="left" cellpadding=0 cellSpacing=0 border=0>
<tr><td width="100%" valign="top" align="left">
<jsp:include page="teamPagesHeader.jsp" flush="true" />			
</td>
</tr>
<tr><td width="100%" valign="top" align="left">
<table bgColor=#ffffff border=0 cellPadding=0 cellSpacing=0 width=772 height="201">
	<tr>
		<td class=r-dotted-lg width=4>&nbsp;
		</td>
		<td align=left class=r-dotted-lg vAlign=top width=600><br>
			<table border=0 cellPadding=5 cellSpacing=3 width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33 colspan="7" width="867"><span class=crumb>
						<digi:link href="/viewMyDesktop.do" styleClass="comment">
						<digi:trn key="aim:viewPortfolio">
						Portfolio
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:viewMyTaskList"> My Task List
						</digi:trn>
                      </span>
					</td>
					<!-- End navigation -->
				</tr>
				<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<span class="page-title">
					<digi:trn key="aim:activityApprovalList">Activity Approval</digi:trn></span>
				</td></tr>			
				<c:forEach var="activity" items="${aimMyDesktopForm.myTasksColl}">
					<tr>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<IMG height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
							<jsp:useBean id="urlChannelOverview" type="java.util.Map" class="java.util.HashMap"/>
								<c:set target="${urlChannelOverview}" property="ampActivityId">
									<bean:write name="activity" property="ampActivityId"/>
								</c:set>
								<c:set target="${urlChannelOverview}" property="tabIndex" value="0"/>
								<bean:define id="translation">
									<digi:trn key="aim:clickToViewProjectDetails">Click here to view Project Details</digi:trn>
								</bean:define>
								<digi:link href="/viewChannelOverview.do" name="urlChannelOverview" title="<%=translation%>" >
									<bean:write name="activity" property="name"/>
								</digi:link>
					</td></tr>
				</c:forEach>
			</table>
		</td>
	</tr>
</table>
</td></tr>
</table>
