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
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
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
		<digi:trn >
			Pre-loaded Multidimensional Reports
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

<DIV id="TipLayer"
  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="1000" align="center">
	<tr>
		<td align=left valign="top" width=750>
			<table cellpadding="0" cellspacing="0" width="100%">
    			<tr>
        			<td valign="bottom" class="crumb" style="padding-bottom:15px;">
        				<c:set var="translation">
          					<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
        				</c:set>
          				<digi:link href="../../viewMyDesktop.do" styleClass="comment" title="${translation}" >
            				<digi:trn key="aim:MyDesktop">My Desktop</digi:trn>
          				</digi:link> &gt; ${pageTitle}
          			</td>
          		</tr>
        <feature:display name="Default Reports" module="Multi-dimensional Reports">
        <tr>
        	<td height=16 align="left" valign="center" style="font-size:12px;">
          		<digi:errors/>
          		<span class=subtitle-blue><b>
            		${pageTitle}
            	</b></span>
          	</td>
       </tr>
		<tr>
			<td>
				<table align="center" cellpadding="0" cellspacing="0" width="100%">
					<tr>
    					<td>
        					<table border="0" cellpadding="0" cellspacing="0" width="100%" class="inside" style="margin-top:10px;">
            					<tr bgColor=#C0D6E2>
                					<td bgColor=#C0D6E2 align="center" class="inside_report">
                    					<b>
                        					${titleColumn}
                        				</b>
                    				</td>
                    				<td bgColor=#C0D6E2 align="center" class="inside_report">
                    					<b>
                    						<digi:trn key="aim:reportType">Type</digi:trn>
                   	 					</b>
                    				</td>
                    				<td bgColor=#C0D6E2 align="center" class="inside_report">
                    				</td>
                    				<logic:present name="currentMember" scope="session">
                    				<bean:define id="member" name="currentMember" scope="session" />
                            		<c:if test="${member.teamHead == true && member.teamAccessType == 'Management'}">
									<td bgColor=#999999 align="center" height="20" class="inside_report">
                    					<b>
                        				<digi:trn key="aim:reportAction">Action</digi:trn>
										</b>
                    				</td>
                    				</c:if>
                    				</logic:present>
								</tr>                          
               					<c:if test="${reports.size == 0}">
                				<tr>
                					<td colspan="4" class="inside_report">
                    					<digi:trn key="aim:noreportspresent">No reports present</digi:trn>
									</td>
                				</tr>
                				</c:if>
                	<logic:iterate name="MainReportsForm" indexId="idx" id="report"  property="reports" type="org.digijava.module.mondrian.dbentity.OffLineReports">
						<c:if test="${!empty report.ownerId && report.type==1}">
							<c:set var="custonflag" value="true"/>
						</c:if>
						<c:if test="${empty report.ownerId && report.type==1}">
						<tr bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" onMouseOut="setPointer(this, <%=idx.intValue()%>, 'out', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" 
							onmouseover="setPointer(this, <%=idx.intValue()%>, 'over', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" style="" >                           
                    		
                    		<td bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" class="reportsBorderTD">
                    			<p style="max-width: 300px;white-space: normal" title="${report.name}">
									<a href="/mondrian/showreport.do?id=${report.id}&pagename=query" title="${report.name}">
									<digi:trn key="${report.name}">
										${report.name}
									</digi:trn>
									</a>
	                       		</p> 		
                     		</td>
                         	<td align="center">
                                <p style="white-space: nowrap">
                                  <li>
                                  	<digi:trn key="aim:donorType">donor</digi:trn>
	                        	  </li>
	                        </td>
	                         <td width=150>  
	                                <div style='position:relative;display:none;' id='report-<bean:write name="report" property="id"/>'> 
	                                	<c:set var="columns">
	                                  		<bean:write name="report" property="columns"/>
	                                  	</c:set>
	                                  	<c:forEach var="column" items="${fn:split(columns, ',')}">
	                                  <li>
	                                    		<digi:trn>${column}</digi:trn>                                    
	                                  </li>
	                                  	</c:forEach>
	                                </div>
	                                <span align="center" style="text-transform: capitalize;" onMouseOver="stm(['<digi:trn key="aim:teamreports:columns">columns</digi:trn>',document.getElementById('report-<bean:write name="report" property="id"/>').innerHTML],Style[0])" onMouseOut="htm()">[ <u style="text-transform:capitalize;" ><digi:trn key="aim:teamreports:columns">Columns</digi:trn></u> ]&nbsp;
	                                </span>
	                                
	                               	<div style='position:relative;display:none;' id='measure-<bean:write name="report" property="id"/>'> 
	                                  <c:set var="measures">
	                                  	<bean:write name="report" property="measures"/>
	                                  </c:set>	
	                                  <c:forEach var="measure" items="${fn:split(measures, ',')}">
	                                  <li>
	                                    	<digi:trn>${measure}</digi:trn>                                    
	                                   </li>
	                                  </c:forEach>
	                                </div>
	                                <span align="center" style="text-transform: capitalize;white-space: no-wrap;"  onMouseOver="stm(['<digi:trn key="aim:teamreports:measures">measures</digi:trn>',document.getElementById('measure-<bean:write name="report" property="id"/>').innerHTML],Style[1])" onMouseOut="htm()">[ <u><digi:trn key="aim:teamreports:measures">Measures</digi:trn></u> ]<br />
	                                </span>
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
                        </c:if>
					</logic:iterate>
					</table>
					<c:if test="${custonflag==true}">
					<table align="center" cellpadding="0" cellspacing="0" width="100%" class="inside">
					<tr>
						<td align="left" valign="center" class="inside_report">
							<span class=subtitle-blue><b>
            					<digi:trn>Custom Multidimensional reports</digi:trn>
								</b>
            				</span>
						</td>
					</tr>
					<tr bgColor=#C0D6E2>
                		<td bgColor=#C0D6E2 align="center" class="inside_report">
                    		<b>
                        		${titleColumn}
                        	</b>
                    	</td>
                    	<td bgColor=#C0D6E2 align="center" class="inside_report">
                    		<b>
                    			<digi:trn key="aim:reportType">Type</digi:trn>
                   	 		</b>
                    	</td>
                    	<td bgColor=#C0D6E2 align="center" class="inside_report">
                    		<b>
                    			<digi:trn key="aim:creationDateLogger">Creation Date</digi:trn>
                   	 		</b>
                    	</td>
                    	<td bgColor=#C0D6E2 align="center" class="inside_report">
                    	</td>
                    	<td bgColor=#C0D6E2 align="center" class="inside_report">
                    	</td>
                    	<logic:present name="currentMember" scope="session">
                    	<bean:define id="member" name="currentMember" scope="session" />
                        <c:if test="${member.teamHead == true && member.teamAccessType == 'Management'}">
							<td bgColor=#999999 align="center" class="inside_report">
                    			<b>
                        			<digi:trn key="aim:reportAction">Action</digi:trn>
								</b>
                    		</td>
                    	</c:if>
                    	</logic:present>
						</tr>                          
               			<c:if test="${reports.size == 0}">
                			<tr>
                				<td colspan="4" class="inside_report">
                    				<digi:trn key="aim:noreportspresent">No reports present</digi:trn>
								</td>
                			</tr>
                		</c:if>
					<logic:iterate name="MainReportsForm" indexId="idx" id="report"  property="reports" type="org.digijava.module.mondrian.dbentity.OffLineReports">
					<c:if test="${!empty report.ownerId && report.type==1 }">
						<tr bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" onMouseOut="setPointer(this, <%=idx.intValue()%>, 'out', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" 
							onmouseover="setPointer(this, <%=idx.intValue()%>, 'over', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" style="" >                           
                    		<td bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" class="reportsBorderTD inside">
                    			<p style="max-width: 250px;white-space: normal" title="${report.name}">
									<a href="/mondrian/showreport.do?id=${report.id}&pagename=query" title="${report.name}">
									<digi:trn key="${report.name}">
										${report.name}
									</digi:trn>
									</a>
	                       		</p> 		
                     		</td>
                         	<td align="center" class="inside_report">
                                <p style="white-space: nowrap">
                                  <li>
                                  	<digi:trn key="aim:donorType">donor</digi:trn>
	                        	  </li>
	                        </td>
	                        <td width="100" align="center" title="${report.creationdate}" class="inside_report">
                            	<bean:write name="report" property="creationdate" format="dd/MM/yyyy"/>
	                        </td>
	                         <td width="150" class="inside_report">  
	                                <div style='position:relative;display:none;' id='report-<bean:write name="report" property="id"/>'> 
	                                  	<c:set var="columns">
	                                  		<bean:write name="report" property="columns"/>
	                                  	</c:set>
	                                  	<c:forEach var="column" items="${fn:split(columns, ',')}">
	                                  <li>
	                                    		<digi:trn>${column}</digi:trn>                                    
	                                  </li>
	                                  	</c:forEach>
	                                </div>
	                                <span align="center" style="text-transform: capitalize;" onMouseOver="stm(['<digi:trn key="aim:teamreports:columns">columns</digi:trn>',document.getElementById('report-<bean:write name="report" property="id"/>').innerHTML],Style[0])" onMouseOut="htm()">[ <u style="text-transform:capitalize;" ><digi:trn key="aim:teamreports:columns">Columns</digi:trn></u> ]&nbsp;
	                                </span>
	                                
	                               	<div style='position:relative;display:none;' id='measure-<bean:write name="report" property="id"/>'> 
	                                 	<c:set var="measures">
		                                  	<bean:write name="report" property="measures"/>
		                                </c:set>	
		                                <c:forEach var="measure" items="${fn:split(measures, ',')}">
	                                  <li>
		                                    	<digi:trn>${measure}</digi:trn>                                    
	                                   </li>
		                                </c:forEach>
	                                </div>
	                                <span align="center" style="text-transform: capitalize;white-space: no-wrap;"  onMouseOver="stm(['<digi:trn key="aim:teamreports:measures">measures</digi:trn>',document.getElementById('measure-<bean:write name="report" property="id"/>').innerHTML],Style[1])" onMouseOut="htm()">[ <u><digi:trn key="aim:teamreports:measures">Measures</digi:trn></u> ]<br />
	                                </span>
	                            </td>
                                <td align="center" class="inside_report">
                            		<c:if test="${!empty report.ownerId}">
	                         			<p style="white-space: nowrap">
	                         				<a href="/mondrian/mainreports.do?id=${report.id}&action=delete" title="<digi:trn key="aim:ClickDeleteReport">Click on this icon to delete report&nbsp;</digi:trn>">
	                            			<img src= "/repository/message/view/images/trash_12.gif" vspace="2" border="0" align="absmiddle" /></a>
	                            		</p>
	                            	</c:if>
                             	</td>
                        	<logic:present name="currentMember" scope="session">
                            <c:if test="${member.teamHead == true && member.teamAccessType == 'Management'}">
								<td align="center" class="inside_report">
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
                        </c:if>
					</logic:iterate>
 				</table>
 				</c:if>
 				<br>
 				<br>
 				<tr>
 				</feature:display>
 				<feature:display name="Pledges Default Reports" module="Pledge Reports">
					<td align="left" valign="center" style="font-size:12px; padding-bottom:10px;">
							<span class=subtitle-blue><b>
            					<digi:trn>Pre-loaded Pledges Reports</digi:trn>
								</b>
            				</span>
						</td>
					</tr>
				 				
 				<table align="center" cellpadding="0" cellspacing="0" width="100%" class="inside">
					<tr bgColor=#C0D6E2>
                		<td bgColor=#C0D6E2 align="center" class="inside_report">
                    		<b>
                        		${titleColumn}
                        	</b>
                    	</td>
                    	<td bgColor=#C0D6E2 align="center" class="inside_report">
                    		<b>
                    			<digi:trn key="aim:reportType">Type</digi:trn>
                   	 		</b>
                    	</td>
                    	
                    	<td bgColor=#C0D6E2 align="center" class="inside_report">
                    	</td>
                    	<td bgColor=#C0D6E2 align="center" class="inside_report">
                    	</td>
                    	<logic:present name="currentMember" scope="session">
                    	<bean:define id="member" name="currentMember" scope="session" />
                        <c:if test="${member.teamHead == true && member.teamAccessType == 'Management'}">
							<td bgColor=#C0D6E2 align="center" class="inside_report">
                    			<b>
                        			<digi:trn key="aim:reportAction">Action</digi:trn>
								</b>
                    		</td>
                    	</c:if>
                    	</logic:present>
						</tr>                          
               			<c:if test="${reports.size == 0}">
                			<tr>
                				<td colspan="4" class="inside_report" bgColor=#C0D6E2>
                    				<digi:trn key="aim:noreportspresent">No reports present</digi:trn>
								</td>
                			</tr>
                		</c:if>
					<logic:iterate name="MainReportsForm" indexId="idx" id="report"  property="reports" type="org.digijava.module.mondrian.dbentity.OffLineReports">
					<c:if test="${!empty report.ownerId && report.type==2}">
							<c:set var="custonflagpledge" value="true"/>
						</c:if>
					<c:if test="${empty report.ownerId && report.type==2 }">
						<tr bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" onMouseOut="setPointer(this, <%=idx.intValue()%>, 'out', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" 
							onmouseover="setPointer(this, <%=idx.intValue()%>, 'over', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" style="" >                           
                    		<td bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" class="reportsBorderTD inside_report">
                    			<p style="max-width: 300px;white-space: normal" title="${report.name}">
									<a href="/mondrian/showreport.do?id=${report.id}&pagename=query" title="${report.name}">
									<digi:trn key="${report.name}">
										${report.name}
									</digi:trn>
									</a>
	                       		</p> 		
                     		</td>
                         	<td align="center" class="inside_report" bgColor=#C0D6E2>
                                <p style="white-space: nowrap">
                                  <li>
                                  	<digi:trn>Pledge</digi:trn>
	                        	  </li>
	                        </td>
	                        
	                         <td width="150" class="inside_report" bgColor=#C0D6E2>  
	                                <div style='position:relative;display:none;' id='report-<bean:write name="report" property="id"/>'> 
	                            	<c:set var="columns">
	                               		<bean:write name="report" property="columns"/>
	                                </c:set>
	                                <c:forEach var="column" items="${fn:split(columns, ',')}">
	                                  <li>
	                                    	<digi:trn>${column}</digi:trn>                                    
	                                  </li>
	                                </c:forEach>
	                                </div>
	                                <span align="center" style="text-transform: capitalize;" onMouseOver="stm(['<digi:trn key="aim:teamreports:columns">columns</digi:trn>',document.getElementById('report-<bean:write name="report" property="id"/>').innerHTML],Style[0])" onMouseOut="htm()">[ <u style="text-transform:capitalize;" ><digi:trn key="aim:teamreports:columns">Columns</digi:trn></u> ]&nbsp;
	                                </span>
	                                
	                               	<div style='position:relative;display:none;' id='measure-<bean:write name="report" property="id"/>'> 
		                            	<c:set var="measures">
		                                  	<bean:write name="report" property="measures"/>
		                                </c:set>	
		                                <c:forEach var="measure" items="${fn:split(measures, ',')}">
	                                  <li>
		                                    	<digi:trn>${measure}</digi:trn>                                    
	                                   </li>
		                                </c:forEach>
	                                </div>
	                                <span align="center" style="text-transform: capitalize;white-space: no-wrap;"  onMouseOver="stm(['<digi:trn key="aim:teamreports:measures">measures</digi:trn>',document.getElementById('measure-<bean:write name="report" property="id"/>').innerHTML],Style[1])" onMouseOut="htm()">[ <u><digi:trn key="aim:teamreports:measures">Measures</digi:trn></u> ]<br />
	                                </span>
	                            </td>
                                <td align="center" class="inside_report" bgColor=#C0D6E2>
                            		<c:if test="${!empty report.ownerId}">
	                         			<p style="white-space: nowrap">
	                         				<a href="/mondrian/mainreports.do?id=${report.id}&action=delete" title="<digi:trn key="aim:ClickDeleteReport">Click on this icon to delete report&nbsp;</digi:trn>">
	                            			<img src= "/repository/message/view/images/trash_12.gif" vspace="2" border="0" align="absmiddle" /></a>
	                            		</p>
	                            	</c:if>
                             	</td>
                        	<logic:present name="currentMember" scope="session">
                            <c:if test="${member.teamHead == true && member.teamAccessType == 'Management'}">
								<td align="center" class="inside_report" bgColor=#C0D6E2>
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
                        </c:if>
					</logic:iterate>
 				</table>
 				<c:if test="${custonflagpledge==true}">
 					<table align="center" cellpadding="0" cellspacing="0" width="100%">
						<tr>
							<td height=30 align="left" valign="center">
								<span class=subtitle-blue>
            						<digi:trn>Custom Pledges Reports</digi:trn>
            					</span>
							</td>
					</tr>
					<tr bgColor=#999999>
                		<td bgColor=#999999 align="center" height="20">
                    		<b>
                        		${titleColumn}
                        	</b>
                    	</td>
                    	<td bgColor=#999999 align="center" height="20">
                    		<b>
                    			<digi:trn key="aim:reportType">Type</digi:trn>
                   	 		</b>
                    	</td>
                    	<td bgColor=#999999 align="center" height="20">
                    		<b>
                    			<digi:trn key="aim:creationDateLogger">Creation Date</digi:trn>
                   	 		</b>
                    	</td>
                    	<td bgColor=#999999 align="center" height="20">
                    	</td>
                    	<td bgColor=#999999 align="center" height="20">
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
					<c:if test="${!empty report.ownerId && report.type==2 }">
						<tr bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" onMouseOut="setPointer(this, <%=idx.intValue()%>, 'out', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" 
							onmouseover="setPointer(this, <%=idx.intValue()%>, 'over', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" style="" >                           
                    		<td bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" class="reportsBorderTD">
                    			<p style="max-width: 250px;white-space: normal" title="${report.name}">
									<a href="/mondrian/showreport.do?id=${report.id}&pagename=query" title="${report.name}">
									<digi:trn key="${report.name}">
										${report.name}
									</digi:trn>
									</a>
	                       		</p> 		
                     		</td>
                         	<td align="center">
                                <p style="white-space: nowrap">
                                  <li>
                                  	<digi:trn>Pledge</digi:trn>
	                        	  </li>
	                        </td>
	                        <td width="100" align="center" title="${report.creationdate}">
                            	<bean:write name="report" property="creationdate" format="dd/MM/yyyy"/>
	                        </td>
	                         <td width="150">  
	                                <div style='position:relative;display:none;' id='report-<bean:write name="report" property="id"/>'> 
	                                  <li>
	                                  	<bean:write name="report" property="columns"/>                                      
	                                  </li>
	                                </div>
	                                <span align="center" style="text-transform: capitalize;" onMouseOver="stm(['<digi:trn key="aim:teamreports:columns">columns</digi:trn>',document.getElementById('report-<bean:write name="report" property="id"/>').innerHTML],Style[0])" onMouseOut="htm()">[ <u style="text-transform:capitalize;" ><digi:trn key="aim:teamreports:columns">Columns</digi:trn></u> ]&nbsp;
	                                </span>
	                                
	                               	<div style='position:relative;display:none;' id='measure-<bean:write name="report" property="id"/>'> 
	                                  <li>
	                                  	<bean:write name="report" property="measures"/>  	                                      
	                                   </li>
	                                </div>
	                                <span align="center" style="text-transform: capitalize;white-space: no-wrap;"  onMouseOver="stm(['<digi:trn key="aim:teamreports:measures">measures</digi:trn>',document.getElementById('measure-<bean:write name="report" property="id"/>').innerHTML],Style[1])" onMouseOut="htm()">[ <u><digi:trn key="aim:teamreports:measures">Measures</digi:trn></u> ]<br />
	                                </span>
	                            </td>
                                <td align="center">
                            		<c:if test="${!empty report.ownerId}">
	                         			<p style="white-space: nowrap">
	                         				<a href="/mondrian/mainreports.do?id=${report.id}&action=delete" title="<digi:trn key="aim:ClickDeleteReport">Click on this icon to delete report&nbsp;</digi:trn>">
	                            			<img src= "/repository/message/view/images/trash_12.gif" vspace="2" border="0" align="absmiddle" /></a>
	                            		</p>
	                            	</c:if>
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
                        </c:if>
					</logic:iterate>
 				</table>
 				</c:if>
 				</feature:display>
			</td>
		</tr>      
 	</table>
 	
</td>
</tr>
 </table>
</digi:form>
</body>
</html>
