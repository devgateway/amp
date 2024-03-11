<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/resources/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>



<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>			

<digi:instance property="aimRelatedLinksForm" />

<table width="100%" cellspacing="0" cellpadding="0" valign="top" align="left">
<tr><td width="100%" valign="top" align="left">
<jsp:include page="teamPagesHeader.jsp"  />
</td></tr>
<tr><td>

			<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
			
							<tr>
								<td vAlign="top" width="100%">
									<c:set var="selectedTab" value="4" scope="request"/>
                  <c:set var="selectedSubTab" value="-1" scope="request"/>

<span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
						</c:set>
						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
							<digi:trn key="aim:portfolio">Portfolio</digi:trn>
						</digi:link>
						&nbsp;&gt;&nbsp;
						<c:set var="translation">
							<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here to view Team Workspace Setup</digi:trn>
						</c:set>
						<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="${translation}" >
							<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
						</digi:link>
						&nbsp;&gt;&nbsp;						
						<c:set var="translation">
							<digi:trn key="aim:clickToViewRelatedLinksList">Click here to view Related Links List</digi:trn>
						</c:set>
						<c:choose>
							<c:when test="${aimRelatedLinksForm.document.isFile}">
								<digi:link href="/relatedLinksList.do~subtab=0" styleClass="comment" title="${translation}" >						
                                    <digi:trn key="aim:relatedDocumentsList">
                                    Related Documents List
                                    </digi:trn>
								</digi:link>						
							</c:when>
							<c:otherwise>
								<digi:link href="/relatedLinksList.do~subtab=1" styleClass="comment" title="${translation}" >						
                                    <digi:trn key="aim:relatedLinksList">
                                    Related Links List
                                    </digi:trn>
								</digi:link>						
							</c:otherwise>
						</c:choose>
						&nbsp;&gt;&nbsp;
						<digi:trn key="aim:viewDocument">
						View Document
						</digi:trn>						
					</span>


									<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
									<jsp:include page="teamSetupMenu.jsp"  />								
									<jsp:include page="viewDocumentDetails.jsp" flush="true" />
									
                                                    	
                  </div>
										</div>											
												
											</td>
										</tr>
									</table>
</td></tr>
</table>



