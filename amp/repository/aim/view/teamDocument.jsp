<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<style>
.contentbox_border{
	border: 	1px solid #666666;
	width: 		750px;
	background-color: #f4f4f2;
}
</style>

<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>			

<digi:instance property="aimRelatedLinksForm" />

<table width="100%" cellSpacing=0 cellPadding=0 valign="top" align="left">
<tr><td width="100%" valign="top" align="left">
<jsp:include page="teamPagesHeader.jsp"  />
</td></tr>
<tr><td width="100%" valign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=780>
	<tr>
		<td width=14>&nbsp;</td>
		<td align=left vAlign=top width=750>

			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=33>
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
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue><digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%">
							<tr>
								<td vAlign="top" width="100%">
									<c:set var="selectedTab" value="4" scope="request"/>
                                    <c:set var="selectedSubTab" value="-1" scope="request"/>


									<jsp:include page="teamSetupMenu.jsp"  />								
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
                                    <div class="contentbox_border" style="border-top:0px;padding: 20px 0px 20px 0px;">
	                                    <div align="center">
                                	
                                        <table align=center cellPadding=0 cellSpacing=0 width="98%">	
                                            <tr><td>
                                                <digi:errors />
                                            </td></tr>
                                            <tr>
                                                <td valign="top">
												
                                                    <jsp:include page="viewDocumentDetails.jsp"  />
                                                </td>
                                            </tr>
                                        </table>
                                     </div>
                                     </div>
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
</td></tr>
</table>



