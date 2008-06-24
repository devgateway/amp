<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<%@ page language="java" import="org.digijava.module.aim.helper.TeamMember" %>

<!-- this is for the nice tooltip widgets -->
<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>

<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
	
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>
<digi:instance property="aimTeamReportsForm" />
<c:set var="translation">
	<digi:trn key="aim:confirmDeleteReport">
		Are you sure you want to delete the selected report ?
	</digi:trn>
</c:set>
<SCRIPT TYPE="text/javascript">
<!--
function popup(mylink, windowname)
{
if (! window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;

  window.open(href,windowname,'channelmode=no,directories=no,menubar=no,resizable=yes,status=no,toolbar=no,scrollbars=yes,location=yes');

return false;
}
//-->

function confirmFunc() {
	return confirm('${translation}');
}
</SCRIPT>

<jsp:include page="teamPagesHeader.jsp" flush="true" />
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>

		<td align=left class=r-dotted-lg vAlign=top width=750>

			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
				<td valign="bottom" class="crumb" >
<c:set var="translation">
	<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
</c:set>
                <digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
                	<digi:trn key="aim:MyDesktop">My Desktop</digi:trn>
                </digi:link> &gt; <digi:trn key="aim:AllReports">All Reports</digi:trn></td></tr>
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
				<logic:present name="currentMember">
	        		<tr>
	        			<td title='<digi:trn key="aim:createReport">Create Report</digi:trn>' align="left">
							<digi:link href="/advancedReportManager.do?clear=true&reportEdit=false">
								<b><digi:trn key="aim:reportGenerator">Report Generator</digi:trn></b>
							</digi:link>
						</td>
					</tr>
				</logic:present>
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
										<digi:errors/>
											<td bgColor=#ffffff class=box-border>
												<table border=0 cellPadding=3 cellSpacing=3 class=box-border width="100%" >
													<tr bgColor=#dddddb>
														<td>
														</td>
														<td bgColor=#dddddb align="left" height="20">
															<b>
															<digi:trn key="aim:publicTeamReportsList">
																List of public team reports
															</digi:trn>
															</b>
														</td>
														<!--
														<td bgColor=#dddddb align="center" height="20">
															<b>
															<digi:trn key="aim:reportDescription">
																Description
															</digi:trn>
															</b>
														</td>
														 -->
														<td bgColor=#dddddb align="left" height="20">
															<b>
															<digi:trn key="aim:reportOwnerName">
																Owner
															</digi:trn>
															</b>
														</td>
														<td bgColor=#dddddb align="left" height="20">
															<b>
															<digi:trn key="aim:reportCreationDate">
																Creation Date 
															</digi:trn>
															</b>
														</td>
														<td bgColor=#dddddb align="left" height="20">
															<b>
															<digi:trn key="aim:reportType">
																Type 
															</digi:trn>
															</b>
														</td>
														
														<td bgColor=#dddddb align="left" height="20">
															<b>
															<digi:trn key="aim:reportColumns">
															Report Columns
															</digi:trn>
															</b>
														</td>
														<td bgColor=#dddddb align="left" height="20">
															<b>
															<digi:trn key="aim:reportHierarchies">
															Report Hierarchies
															</digi:trn>
															</b>
														</td>
														<td bgColor=#dddddb align="left" height="20">
															<b>
															<digi:trn key="aim:reportMeasures">
															Report Measures
															</digi:trn>
															</b>
														</td>
														<% String s = (String)session.getAttribute("teamLeadFlag");
														   TeamMember tm = (TeamMember) session.getAttribute("currentMember");
														 if(tm!=null)
														  {
														%>
															<td bgColor=#dddddb align="left" height="20">
																<b>
																<digi:trn key="aim:reportAction">
																	Action
																</digi:trn>
																</b>
															</td>
														<% } %>
													</tr>
													<c:set var="count" value="${fn:length(aimTeamReportsForm.reports)}">
													</c:set>
													<c:if test="${count == 0}">
													<tr>
														<td colspan="4">
														<digi:trn key="aim:noreportspresent">
														No reports present
														</digi:trn>
														</td>
													</tr>
													</c:if>
													<logic:iterate name="aimTeamReportsForm"  property="reports" id="report" indexId="idx"
														type="org.digijava.module.aim.dbentity.AmpReports">
                                                         <tr bgcolor="<%=(idx.intValue()%2==1?"#eeeeee":"#ffffff")%>" onmouseout="setPointer(this, <%=idx.intValue()%>, 'out', <%=(idx.intValue()%2==1?"\'#eeeeee\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" 
                                                         	onmouseover="setPointer(this, <%=idx.intValue()%>, 'over', <%=(idx.intValue()%2==1?"\'#eeeeee\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" style="" >														
															<td bgcolor="<%=(idx.intValue()%2==1?"#eeeeee":"#ffffff")%>" class="reportsBorderTD">
															<p style="white-space: nowrap">
							                 	 			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10>
															<c:set var="translation">
																<digi:trn key="aim:clickToViewReport">Click here view Report</digi:trn>
															</c:set>
															</td>		
															<td bgcolor="<%=(idx.intValue()%2==1?"#eeeeee":"#ffffff")%>" class="reportsBorderTD">
															<digi:link href="/viewNewAdvancedReport.do?view=reset&widget=false" paramName="report"  paramId="ampReportId" paramProperty="ampReportId" styleClass="h-box" onclick="return popup(this,'');">
															<b>
																<p style="max-width: 400px;white-space: normal">
																	<bean:write name="report" property="name"/>
																</p>	
															</b>
															</digi:link>
															</p>
															<logic:present name="report" property="reportDescription" >
																<p style="max-width: 400px;white-space: normal">
															 		<bean:write name="report" property="reportDescription" />
															 	</p>
														 	</logic:present>
															</td>

														 	<td bgcolor="<%=(idx.intValue()%2==1?"#eeeeee":"#ffffff")%>" class="reportsBorderTD">
														 		<p style="white-space: nowrap">
														 		<logic:present name="report" property="ownerId">
														 			 <i><bean:write name="report" property="ownerId.user.name" /></i>
														 			 <br>
														 			 <bean:write name="report" property="ownerId.ampTeam.name" />
														 		</logic:present>
														 		</p>
														 	</td>
														 	<td bgcolor="<%=(idx.intValue()%2==1?"#eeeeee":"#ffffff")%>" class="reportsBorderTD">
														 		<p style="white-space: nowrap">
														 			<logic:present name="report" property="updatedDate">
														 	    		<bean:write name="report" property="formatedUpdatedDate" />
														 	    	</logic:present>
														 	    </p>
														 	</td>
														 	<td bgcolor="<%=(idx.intValue()%2==1?"#eeeeee":"#ffffff")%>" class="reportsBorderTD">
														 		<p style="white-space: nowrap">
														 		 <%
                                                                             if (report.getType()!=null && report.getType().equals(new Long(1))) {
                                                                         %>
                                                                             <li>
                                                                                 <digi:trn key="aim:donorType">donor</digi:trn>
                                                                             </li>
                                                                         <%
                                                                             }
                                                                             else
                                                                                 if (report.getType()!=null && report.getType().equals(new Long (3))){
                                                                         %>
                                                                                 <li>
                                                                                     <digi:trn key="aim:regionalType">regional</digi:trn>
                                                                                 </li>
                                                                         <%
                                                                                 }
                                                                                 else
                                                                                     if (report.getType()!=null && report.getType().equals(new Long(2))){
                                                                         %>
                                                                                     <li>
                                                                                         <digi:trn key="aim:componentType">component</digi:trn>
                                                                                     </li>
                                                                        <%
                                                                                 }
                                                                                 else
                                                                                     if (report.getType()!=null && report.getType().equals(new Long(4))){
                                                                         %>
                                                                                         <li>
                                                                                             <digi:trn key="aim:contributionType">contribution</digi:trn>
                                                                                         </li>
                                                                         <%
                                                                                     }
                                                                         %>
                                                                        
                                                                     </li>


													 				<logic:equal name="report" property="drilldownTab" value="true">
															 			<li>
														 					<digi:trn key="aim:typeDrilldownTab">Desktop Tab</digi:trn>
															 			</li>
													 				</logic:equal>
													 				<logic:equal name="report" property="publicReport" value="true">
															 			<li>
														 					<digi:trn key="aim:typePublicReport">Public Report</digi:trn>
															 			</li>
													 				</logic:equal>
													 				<logic:equal name="report" property="hideActivities" value="true">
															 			<li>
														 					<digi:trn key="aim:typeSummaryReport">Summary Report</digi:trn>
															 			</li>
													 				</logic:equal>
															 		
															 		<logic:equal name="report" property="options" value="A">
																 		<li>
																 			Annual
																 		</li>
															 		</logic:equal>
															 		<logic:equal name="report" property="options" value="Q">
																 		<li>
																 			Quarterly
																 		</li>
															 		</logic:equal>
															 		<logic:equal name="report" property="options" value="M">
																 		<li>
																 			Monthly
																 		</li>
															 		</logic:equal>
														 		</p>
														 	</td>

														 												
															<td bgcolor="<%=(idx.intValue()%2==1?"#eeeeee":"#ffffff")%>" class="reportsBorderTD">
																<div style='position:relative;display:none;' id='report-<bean:write name="report" property="ampReportId"/>'> 
																	<logic:iterate name="report" property="columns" id="column" indexId="index"  >
																		<%if (index.intValue()%2==0){ %>
																			<li>																			
																		 		<bean:write name="column" property="column.columnName" />
																			
																		<% } else {%>
																			,
																			<bean:write name="column" property="column.columnName" />
																			</li>
																		<%} %>
																 	</logic:iterate>
															 	</div>
															 	
															 	<div align="center" onMouseOver="stm(['<digi:trn key="aim:teamreports:columns">columns</digi:trn>',document.getElementById('report-<bean:write name="report" property="ampReportId"/>').innerHTML],Style[1])" onMouseOut="htm()">[<u><digi:trn key="aim:reportbuilder:list">list...</digi:trn></u>]
																</div>
															</td>
															
															<td bgcolor="<%=(idx.intValue()%2==1?"#eeeeee":"#ffffff")%>" class="reportsBorderTD">
																<logic:iterate name="report" property="hierarchies" id="hierarchy" >
																	<%-- <bean:write name="hierarchy" property="column.columnName"/> --%>
																	<li>
																	${hierarchy.column.columnName}
																	</li>
																</logic:iterate>
															</td>
															
															<td bgcolor="<%=(idx.intValue()%2==1?"#eeeeee":"#ffffff")%>" class="reportsBorderTD">
																<logic:iterate name="report" property="measures" id="measure" >
																	<%-- <bean:write name="hierarchy" property="column.columnName"/> --%>
																	<li>
																	${measure.measure.measureName}
																	</li>
																</logic:iterate>
															</td>
															
																<%
														 	if(tm!=null)
														 	{ %>

															<td bgcolor="<%=(idx.intValue()%2==1?"#eeeeee":"#ffffff")%>" class="reportsBorderTD">  
																<p style="white-space: nowrap">
																<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${urlParams}" property="rid">
																	<bean:write name="report" property="ampReportId" />
																</c:set>
																<c:set target="${urlParams}" property="event" value="edit" />
																<logic:equal name="teamLeadFlag" scope="session" value="true"> 
																	[ <digi:link href="/deleteAllReports.do" name="urlParams" onclick="return confirmFunc()" >
																		<digi:trn key="aim:reportDelete">Delete</digi:trn>
																	</digi:link> ]
																	<module:display name="New Report Wizard" parentModule="REPORTING">
																		[ <digi:link href="/reportWizard.do?editReportId=${report.ampReportId}" >
																			<digi:trn key="aim:reportEdit">Edit</digi:trn>
																		</digi:link> ]
																	</module:display>
																	<module:display name="Old Report Wizard" parentModule="REPORTING">
																		[ <digi:link href="/editReport.do?reportEdit=true" name="urlParams" >
																			<digi:trn key="aim:reportEdit">Edit</digi:trn>
																		</digi:link> ]
																	</module:display>
																</logic:equal>														
																<logic:equal name="teamLeadFlag" scope="session" value="false">
																	<logic:present name="report" property="ownerId">
																		<logic:equal  name="report" property="ownerId.ampTeamMemId" value="${aimTeamReportsForm.currentMemberId}"> 
																			[ <digi:link href="/deleteAllReports.do" name="urlParams" onclick="return confirmFunc()" >
																				<digi:trn key="aim:reportDelete">Delete</digi:trn>
																			</digi:link> ]
																			<module:display name="New Report Wizard" parentModule="REPORTING">
																				[ <digi:link href="/reportWizard.do?editReportId=${report.ampReportId}" >
																					<digi:trn key="aim:reportEdit">Edit</digi:trn>
																				</digi:link> ]
																			</module:display>
																			<module:display name="Old Report Wizard" parentModule="REPORTING">
																				[ <digi:link href="/editReport.do?reportEdit=true" name="urlParams">
																					<digi:trn key="aim:reportEdit">Edit</digi:trn>
																				</digi:link> ]
																			</module:display>
																		</logic:equal>		
																	</logic:present>																																																
																</logic:equal>															
																</p>
															</td>
															<% } %>		
															
														</TR>

													</logic:iterate>

                            						    </table>
                                <table>
                                <tr>
                                <td>	
                                
                                    <digi:trn key="aim:pages">
                                                Pages :
                                    </digi:trn>
                                   	<c:set var="translation">
        
                                            <digi:trn key="aim:clickToViewThisPage">Click here to show this page</digi:trn>

									</c:set>
                                  <c:forEach var="page" begin="1" end="${aimTeamReportsForm.totalPages}">
                                            <c:if test="${aimTeamReportsForm.currentPage==page}">
                                                <font color="#ff0000">
                                                 <c:out value="${page}"/>
                                                </font>
                                            </c:if>
                                        <c:if test="${aimTeamReportsForm.currentPage!=page}">
                                            <digi:link title="${translation}" href="/viewTeamReports.do?currentPage=${page}" style="text-decoration:none;color:#000000">
                                            
                                                <c:out value="${page}"/>
                                          </digi:link>
                                          </c:if>
                                          
                                          <c:if test="${page < aimTeamReportsForm.totalPages}">
                                                        |&nbsp; 
                                         </c:if>
                                        </c:forEach>
                                        </td>
                                        </tr>
                                </table>

											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>&nbsp;
								
							</td></tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>



