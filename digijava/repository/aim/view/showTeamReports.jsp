<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimTeamReportsForm" />

<jsp:include page="teamPagesHeader.jsp" flush="true" />
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
	
		<td align=left class=r-dotted-lg vAlign=top width=750>
		
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
				<td valign="bottom" class="crumb" >
<bean:define id="translation">
	<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
</bean:define>
                <digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>" >
                	My Desktop
                </digi:link> &gt; All Reports</td></tr>
				<tr>
		<td>&nbsp;</td></tr>
				<tr>
					<td height=16 align="center" vAlign=center><span class=subtitle-blue>
						<digi:trn key="aim:teamReports">
							Team Reports
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td noWrap width=650 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
							<tr bgColor=#f4f4f2>
								<td>&nbsp;
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%">	
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table border=0 cellPadding=3 cellSpacing=3 class=box-border width="100%" >
													<tr bgColor=#dddddb>
														<td bgColor=#dddddb align="center" height="20">
															<b>
															<digi:trn key="aim:publicTeamReportsList">
																List of public team reports
															</digi:trn></b>
														</td>
													</tr>
													<logic:iterate name="aimTeamReportsForm"  property="reports" id="reports"
											type="org.digijava.module.aim.dbentity.AmpReports"> 
											
											<TR><TD>
				                 	 			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" 
												width=10>
												<bean:define name="reports" id="link"  property="description" type="java.lang.String"/>
												<bean:define id="translation">
													<digi:trn key="aim:clickToViewReport">Click here view Report</digi:trn>
												</bean:define>
												<digi:link href="<%=link%>" styleClass="h-box" title="<%=translation%>" >
												<bean:write name="reports" property="name"/>
												</digi:link>
											</TD>
										</TR>
										
										
										</logic:iterate>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>
								&nbsp;
							</td></tr>
						</table>			
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
