<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimViewIndicatorForm"/>

<TABLE cellspacing="0" cellpadding="0" align="center" vAlign="top" border="0" width="100%">
<TR>
	<TD vAlign="top" align="center">
		<!-- contents -->
			<TABLE width="99%" cellspacing="0" cellpadding="0" vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
			<TR><TD bgcolor="#f4f4f4">
			<TABLE width="100%" cellspacing="1" cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4">
				<TR bgColor=#f4f4f2>
            	<TD align=left>
						<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">
							<TR>
								<TD align="left">
									<SPAN class=crumb>
									<jsp:useBean id="url" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${url}" property="ampActivityId">
										<bean:write name="aimViewIndicatorForm" property="ampActivityId"/>
									</c:set>
									<c:set target="${url}" property="tabIndex" value="7"/>
									<c:set var="translation">
										<digi:trn key="aim:clickToActivityDashboard">Click here to view activity dashboard</digi:trn>
									</c:set>
									<digi:link href="/viewActivityDashboard.do" name="url" styleClass="comment"
									title="${translation}" >
									<digi:trn key="aim:activityDashboard">Activity Dashboard</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:trn key="aim:indicatorValues">Indicator Values</digi:trn>
									</SPAN>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
				<TR bgColor=#f4f4f2>
					<TD vAlign="top" align="center" width="100%">
						<TABLE width="98%" cellpadding="0" cellspacing="0" vAlign="top" align="center" bgColor=#f4f4f2>
							<TR>
								<TD width="100%" bgcolor="#F4F4F2" height="17">
									<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="17">
                           	<TR bgcolor="#F4F4F2" height="17">
				                 		<TD bgColor=#c9c9c7 class=box-title width=150>
												&nbsp;<digi:trn key="aim:indicatorValues">Indicator Values</digi:trn>
											</TD>
	                              <TD><IMG src="../ampTemplate/images/corner-r.gif" width="17" height="17"></TD>
   	                        </TR>
      	                  </TABLE>
								</TD>
							</TR>
							<TR>
								<TD width="100%" bgcolor="#F4F4F2" align="left">
					<TABLE border="0" cellpadding="0" cellspacing="0" width="510" class="box-border-nopadding">
						<logic:notEmpty name="aimViewIndicatorForm" property="indicators">
							<logic:iterate name="aimViewIndicatorForm" property="indicators" id="indicator">
								<TR><TD>
									<TABLE cellPadding=5 cellspacing="1" border="0">
										<TR bgcolor="#c9c9c7">
											<TD colspan="2" align="left">
												<b><bean:write name="indicator" property="indicatorName" /></b>
											</TD>
											<TD colspan="2" align="right">
												<b><bean:write name="indicator" property="indicatorCode" /></b>
											</TD>
										</TR>
										<TR bgcolor="#ffffff">
											<TD width="125">
												<b><digi:trn key="aim:meBaseValue">Base Value</digi:trn></b>
											</TD>
											<TD width="125">
												<bean:write name="indicator" property="baseVal" />
											</TD>
											<TD width="125">
												<b><digi:trn key="aim:meDate">Date</digi:trn></b>
											</TD>
											<TD width="125">
												<bean:write name="indicator" property="baseValDate" />
											</TD>
										</TR>
										<TR bgcolor="#ffffff">
											<TD width="125">
												<b><digi:trn key="aim:meBaseValueComments">Comments</digi:trn></b>
											</TD>
											<TD colspan="3">
												<bean:write name="indicator" property="baseValComments" />
											</TD>
										</TR>
										<TR bgcolor="#ffffff">
											<TD>
												<b><digi:trn key="aim:meTargetValue">Target Value</digi:trn></b>
											</TD>
											<TD>
												<bean:write name="indicator" property="revisedTargetVal" />
											</TD>
											<TD>
												<b><digi:trn key="aim:meDate">Date</digi:trn></b>
											</TD>
											<TD>
												<bean:write name="indicator" property="targetValDate" />
											</TD>
										</TR>
										<TR bgcolor="#ffffff">
											<TD width="125">
												<b><digi:trn key="aim:meTargetValueComments">Comments</digi:trn></b>
											</TD>
											<TD colspan="3">
												<bean:write name="indicator" property="revisedTargetValComments" />
											</TD>
										</TR>
										<TR bgcolor="#ffffff">
											<TD>
												<b><digi:trn key="aim:meActualValue">Actual Value</digi:trn></b>
											</TD>
											<TD>
												<bean:write name="indicator" property="actualVal" />
											</TD>
											<TD>
												<b><digi:trn key="aim:meDate">Date</digi:trn></b>
											</TD>
											<TD>
												<bean:write name="indicator" property="actualValDate" />
											</TD>
										</TR>
										<TR bgcolor="#ffffff">
											<TD width="125">
												<b><digi:trn key="aim:meActualValueComments">Comments</digi:trn></b>
											</TD>
											<TD colspan="3">
												<bean:write name="indicator" property="actualValComments" />
											</TD>
										</TR>
										<logic:notEmpty name="indicator" property="priorValues">
										<TR bgcolor="#ffffff">
											<TD colspan="4">
												<b><digi:trn key="aim:mePriorValues">Prior Values</digi:trn></b>
											</TD>
										</TR>
										<logic:iterate name="indicator" property="priorValues" id="pValue">
										<TR bgcolor="#f4f4f2">
											<TD>
												&nbsp;
											</TD>
											<TD>
												<bean:write name="pValue" property="currValue" />
											</TD>
											<TD>
												<b><digi:trn key="aim:meDate">Date</digi:trn></b>
											</TD>
											<TD>
												<bean:write name="pValue" property="currValDate" />
											</TD>
										</TR>
										<TR bgcolor="#f4f4f2">
											<TD>
												&nbsp;
											</TD>
											<TD colspan="3">
												&nbsp;<bean:write name="pValue" property="comments" />
											</TD>
										</TR>
										</logic:iterate>
										</logic:notEmpty>
										<TR bgcolor="#ffffff">
											<TD>
												<b><digi:trn key="aim:meRisk">Risk</digi:trn></b>
											</TD>
											<TD colspan="3">
												<c:if test="${!empty indicator.riskName}">
												<digi:trn key="aim:${indicator.riskName}"><c:out value="${indicator.riskName}"/></digi:trn>
                                                                                                </c:if>
												<%--

												<bean:write name="indicator" property="riskName" />

												--%>
											</TD>
										</TR>
										<TR bgcolor="#ffffff">
											<TD>
												<b><digi:trn key="aim:meProgress">Progress</digi:trn></b>
											</TD>
											<TD colspan="3">
												<bean:write name="indicator" property="progress" />
											</TD>
										</TR>
									</TABLE>
								</TD></TR>
							</logic:iterate>
						</logic:notEmpty>
					</TABLE>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
				<TR><TD>&nbsp;</TD></TR>
			</TABLE>
			</TD></TR>
			</TABLE>
		<!-- end -->
	</TD>
</TR>

</TABLE>
</table>
<tr><td class="td_bottom1">&nbsp;</td></tr>
</table>
<TR><TD>&nbsp;</TD></TR>