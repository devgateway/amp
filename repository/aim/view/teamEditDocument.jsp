<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>

<script language="JavaScript">

	<!--

	function editDocument() {
		<digi:context name="editDoc" property="context/module/moduleinstance/updateDocumentDetails.do" />
		document.aimRelatedLinksForm.action = "<%= editDoc %>";
	 	document.aimRelatedLinksForm.target = "_self";
   	document.aimRelatedLinksForm.submit();
	}

	-->

</script>


<digi:instance property="aimRelatedLinksForm" />
<digi:form action="/updateDocumentDetails.do" method="post" enctype="multipart/form-data">

<html:hidden property="activityId"/>
<html:hidden property="docId"/>
<html:hidden property="valuesSet"/>

<table width="100%" cellSpacing=0 cellPadding=0 valign="top" align="left">
<tr><td width="100%" valign="top" align="left">
<jsp:include page="teamPagesHeader.jsp" flush="true" />
</td></tr>
<tr><td width="100%" valign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>

			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=33><span class=crumb>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
						</bean:define>
						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>" >
							<digi:trn key="aim:portfolio">Portfolio</digi:trn>
						</digi:link>
						&nbsp;&gt;&nbsp;
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here to view Team Workspace Setup</digi:trn>
						</bean:define>
						<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="<%=translation%>" >
							<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
						</digi:link>
						&nbsp;&gt;&nbsp;
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewRelatedLinksList">Click here to view Related Links List</digi:trn>
						</bean:define>
						<digi:link href="/relatedLinksList.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:relatedLinksList">
						Related Links List
						</digi:trn>
						</digi:link>
						&nbsp;&gt;&nbsp;
						<digi:trn key="aim:editDocument">
						Edit Document
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>Team Workspace Setup</span>
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
							<tr bgColor=#3754a1>
								<td vAlign="top" width="100%">
									<jsp:include page="teamSetupMenu.jsp" flush="true" />
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td>&nbsp;
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%">
										<tr><td>
											<digi:errors />
										</td></tr>
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellPadding=0 cellSpacing=0 width=167>
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title width=150>
															<digi:trn key="aim:relatedLinksList">
																Related Links List
															</digi:trn>
														</td>
														<td background="module/aim/images/corner-r.gif" height="17" width=17>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border valign="top">

											<!-- Edit Document form -->

<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#ffffff"
class="box-border-nopadding">
	<tr>
		<td bgcolor="#dddddd" align="center">
			<b><digi:trn key="aim:editDocument">Edit Document</digi:trn></b>
		</td>
	</td>
	<tr>
		<td bgcolor="#ffffff" vAlign="top" align="center">
			<table width="100%%" cellSpacing="1" cellPadding="5" vAlign="top" align="center">
				<tr>
					<td width="20%" align="right" bgcolor="#f4f4f4">
						<b>
						<digi:trn key="aim:nameOfActivity">Activity</digi:trn></b>
					</td>
					<td width="80%" align="left" bgcolor="#f4f4f4">
						${aimRelatedLinksForm.activityName}
					</td>
				</tr>
				<tr>
					<td width="20%" align="right" bgcolor="#f4f4f4">
						<b>
						<digi:trn key="aim:docTitle">Title</digi:trn></b>
					</td>
					<td width="80%" align="left" bgcolor="#f4f4f4">
						<html:text property="title" size="20"  styleClass="inp-text"/>
					</td>
				</tr>
				<tr>
					<td width="20%" align="right" bgcolor="#f4f4f4">
						<b>
						<digi:trn key="aim:docDescription">Description</digi:trn></b>
					</td>
					<td width="80%" align="left" bgcolor="#f4f4f4">
						<html:textarea property="docDescription" rows="3" cols="50"  styleClass="inp-text"/>
					</td>
				</tr>
				<c:if test="${aimRelatedLinksForm.file == true}">
				<tr>
					<td width="20%" align="right" bgcolor="#f4f4f4">
						<b><digi:trn key="aim:fileName">File name</digi:trn></b>
					</td>
					<td width="80%" align="left" bgcolor="#f4f4f4">
						<html:file name="aimRelatedLinksForm" property="docFile" size="30" styleClass="dr-menu"/>
					</td>
				</tr>
				<tr>
					<td width="20%" align="right" bgcolor="#f4f4f4">
						&nbsp;
					</td>
					<td width="80%" align="left" bgcolor="#f4f4f4">
						<c:if test="${!empty aimRelatedLinksForm.fileName}">
							<c:out value="${aimRelatedLinksForm.fileName}" />
						</c:if>
					</td>
				</tr>
				</c:if>
				<c:if test="${aimRelatedLinksForm.file == false}">
				<tr>
					<td width="20%" align="right" bgcolor="#f4f4f4">
							<digi:trn key="aim:url">URL</digi:trn>
					</td>
					<td width="80%" align="left" bgcolor="#f4f4f4">
						<html:text property="url" size="30" styleClass="inp-text"/>
					</td>
				</tr>
				</c:if>
			</table>
		</td>
	</td>
	<tr>
		<td vAlign="top" align="center">
			<table cellSpacing="3" cellPadding="3">
				<tr>
					<td><input type="button" value="  Save  " class="dr-menu"
					onclick="editDocument()"></td>
					<td><input type="reset" value="Cancel" class="dr-menu"></td>
					<td></td>
				</tr>
			</table>
		</td>
	</td>
</table>

											<!-- Edit Document form end -->

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
</td></tr>
</table>
</digi:form>
