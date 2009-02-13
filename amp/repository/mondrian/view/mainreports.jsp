<%@page contentType="text/html"%>
<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@page import="org.digijava.module.aim.helper.TeamMember"%><html>
<head>
<link rel="stylesheet" href="stylesheet.css" type="text/css" />
</head>

<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>
<body>
<digi:instance property="MainReportsForm"/>
<digi:context name="digiContext" property="context"/>
<digi:form  action="/showreport.do" method="post">
<c:set var="pageTitle">
		<digi:trn key="aim:offlinereporslist">
			List Of Offline Reports in Workspace
		</digi:trn>
</c:set>
<c:set var="titleColumn">
	<digi:trn key="aim:reportTitle">
		Report Title
	</digi:trn>
</c:set>

<input type="hidden" name="query">
<input type="hidden" name="name">
<input type="hidden" name="id">
<jsp:include page="../../aim/view/teamPagesHeader.jsp" flush="true" />

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
    	<td width=14>&nbsp;</td>
		<td align=left vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
    			<tr>
        			<td valign="bottom" class="crumb" >
        				<c:set var="translation">
          					<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
        				</c:set>
          				<digi:link href="../../viewMyDesktop.do" styleClass="comment" title="${translation}" >
            				<digi:trn key="aim:MyDesktop">My Desktop</digi:trn>
          				</digi:link> &gt; ${pageTitle}
          			</td>
          		</tr>
        <tr>
        	<td height=16 align="left" vAlign=center>
          		<digi:errors/>
          		<span class=subtitle-blue>
            		${pageTitle}
            	</span>
          	</td>
       </tr>
		<tr>
			<td>
				<table align=center cellPadding=0 cellSpacing=0 width="100%">
					<tr>                    
    					<td>
        					<table border=0 cellPadding=3 cellSpacing=3 width="100%" >
            					<tr bgColor=#999999>
                					<td bgColor=#999999 align="center" height="20">
                    					<b>
                        					${titleColumn}
                        				</b>
                    				</td>
                    				<td bgColor=#999999 align="center" height="20">
                    					<b>
                    						<digi:trn key="aim:reportOwnerName">Owner</digi:trn>
                    					</b>
                    				</td>
                    				<td bgColor=#999999 align="center" height="20">
                    					<b>
                    						<digi:trn key="aim:reportType">Type</digi:trn>
                   	 					</b>
                    				</td>
                    				<logic:present name="currentMember" scope="session">
                    				<bean:define id="member" name="currentMember" scope="session" />
                            		<c:if test="${member.teamHead == true && member.teamAccessType == 'Management'}">
									<td bgColor=#999999 align="center" height="20">
                    					<b>
                        				<digi:trn key="aim:reportAction">Action</digi:trn>
										</b>
                    				</td>
                    				</c:if>
                    				</logic:present>
								</tr>                          
               		<c:if test="${reports.size == 0}">
                		<tr>
                			<td colspan="4">
                    			<digi:trn key="aim:noreportspresent">No reports present</digi:trn>
							</td>
                		</tr>
                	</c:if>
               		<logic:iterate name="MainReportsForm" indexId="idx" id="report"  property="reports" type="org.digijava.module.mondrian.dbentity.OffLineReports">
						<tr bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" onmouseout="setPointer(this, <%=idx.intValue()%>, 'out', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" 
							onmouseover="setPointer(this, <%=idx.intValue()%>, 'over', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" style="" >                           
                    		<td bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" class="reportsBorderTD">
                    			<p style="max-width: 400px;white-space: normal" title="${report.name}">
									<a href="/mondrian/showreport.do?id=${report.id}&pagename=query" title="${report.name}">
									<c:if test="${fn:length(report.name) > 300}" >
										<c:out value="${fn:substring(report.name, 0, 300)}" />...
									</c:if>
									<c:if test="${fn:length(report.name) < 300}" >
										<c:out value="${report.name}" />
									</c:if>
									</a>
	                       		</p> 		
                     		</td>
	                        <td align="center">
                        		<p style="white-space: nowrap">
                                	<logic:present name="report" property="ownerId">
                                   		<i>
                                   			<bean:write name="report" property="ownerId.user.name" />
                                   		</i>
                                	</logic:present>
                            	</p>
                         	</td>
                         	<td align="center">
                                <p style="white-space: nowrap">
                                  <li>
                                  	<digi:trn key="aim:donorType">donor</digi:trn>
	                        	  </li>
	                        </td>
                        	<logic:present name="currentMember" scope="session">
                            <c:if test="${member.teamHead == true && member.teamAccessType == 'Management'}">
								<td align="center">
									<c:if test="${report.publicreport==false}">
									<a href="/mondrian/mainreports.do?id=${report.id}&action=public" title="<digi:trn key="aim:clicktomakethispublic">Click here to make this public</digi:trn>">
		                        		<img src= "/repository/contentrepository/view/images/make_public.gif" vspace="2" border="0" align="middle" />
		                            </a>
		                            </c:if>
		                            <c:if test="${report.publicreport==true}">
										<a href="/mondrian/mainreports.do?id=${report.id}&action=nopublic" title="<digi:trn key="aim:clicktomakethisprivate">Click here to make this private</digi:trn>">
											<img src= "/repository/contentrepository/view/images/make_private.gif" border="0" align="middle" />
		                            	</a>
		                            </c:if>
                                 </td>
                            </c:if>
                           </logic:present>
                        </tr>
					</logic:iterate>
 				</table>
			</td>
		</tr>      
 	</table>
</td>
</tr>
 </table>
</digi:form>
</body>
</html>
