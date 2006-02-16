<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:context name="digiContext" property="context" />

<script language="JavaScript">

	function openNewWindow(wndWidth, wndHeight){
		window.name = "opener" + new Date().getTime();
		if (wndWidth == null || 
			wndWidth == 0 || 
			wndHeight == null || 
			wndHeight == 0) {
			
			wndWidth = window.screen.availWidth/2;
			wndHeight = window.screen.availHeight/2;
		
		}
		popupPointer = window.open("about:blank", "forumPopup", "height=" + wndHeight + ",width=" + wndWidth + ",menubar=no");
	}

	function selectFile() {
		  openNewWindow(400, 130);			  
	     <digi:context name="showSelectFile" property="context/module/moduleinstance/showSelectFile.do" />
		  document.aimAddDocumentForm.action = "<%= showSelectFile %>";
		  document.aimAddDocumentForm.target = popupPointer.name;
	     document.aimAddDocumentForm.submit();
	}

	function showSelectCategory() {
	  openNewWindow(450, 375);
	     <digi:context name="showSelectCategory" property="context/module/moduleinstance/addContentItemCategoryFwd.do" />
	     document.aimAddDocumentForm.action = "<%= showSelectCategory %>";
		  document.aimAddDocumentForm.target = popupPointer.name;
	     document.aimAddDocumentForm.submit();
	} 	

	function removeCategory(id) {
      <digi:context name="removeCategory" property="context/module/moduleinstance/removeContentItemCategory.do" />
      document.aimAddDocumentForm.action = "<%= removeCategory%>?parentCategoryId=" + id;
	   document.aimAddDocumentForm.target = "_self";
      document.aimAddDocumentForm.submit();	
	}

  function createDocument() {
      <digi:context name="createItem" property="context/module/moduleinstance/createDocument.do" />
      document.aimAddDocumentForm.action = "<%= createItem%>";
		document.aimAddDocumentForm.target = "_self";	  
      document.aimAddDocumentForm.submit();
  }

</script>


<digi:instance property="aimAddDocumentForm" />
<digi:form action="/addDocument.do" method="post">

<html:hidden name="aimAddDocumentForm" property="processingMode"/>
<html:hidden name="aimAddDocumentForm" property="categoryId"/>


<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlParams}" property="tId" value="-1"/>
<c:set target="${urlParams}" property="dest" value="teamLead"/>			

<table cellSpacing=0 cellPadding=0 vAlign="top" align="left" width="100%">
<tr><td width="100%">
<jsp:include page="teamPagesHeader.jsp" flush="true" />
</td></tr>
<tr><td>
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
						<digi:trn key="aim:portfolio">
						Portfolio
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here to view Team Workspace Setup</digi:trn>
						</bean:define>
						<digi:link href="/workspaceOverview.do" name="urlParams" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:teamWorkspaceSetup">
						Team Workspace Setup
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewRelatedLinksList">Click here view Related Links List</digi:trn>
						</bean:define>
						<digi:link href="/relatedLinks.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:relatedLinksList">
						Related Links List
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:addDocument">
						Add Document
						</digi:trn>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>Team Workspace Setup</span>
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%" 
						valign="top" align="left">
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
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellPadding=0 cellSpacing=0 width=167>
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title width=150><b>
															<digi:trn key="aim:relatedLinksList">
																Related Links List
															</digi:trn></b>
														</td>
														<td background="module/aim/images/corner-r.gif" height="17" width=17>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border valign="top">
												<table border=0 cellPadding=3 cellSpacing=1 class=box-border width="100%">
													<tr bgColor=#dddddb>
														<td bgColor=#dddddb align="center"><b>
															<digi:trn key="aim:addDocuments">
															Add Documents
															</digi:trn></b>
														</td>
													</tr>
													<tr>
														<td valign="top">
															<table width="100%">
																<tr><td colspan="2">
																	<digi:errors/>
																</td></tr>
																<tr>
																	<td width="30%" align="right">
																		<digi:trn key="aim:docTitle">
																		Title
																		</digi:trn>
																	</td>
																	<td>
																		<html:text property="title" size="30" />
																	</td>
																</tr>
																<tr>
																	<td width="30%" align="right">
																		<digi:trn key="aim:docDescription">
																		Description
																		</digi:trn>
																	</td>
																	<td>
																		<html:text property="description" size="50" />
																	</td>
																</tr>
																<tr>
																	<td width="30%" align="right">
																		<digi:trn key="aim:docURL">
																		URL 
																		</digi:trn>
																	</td>
																	<td width="100%">
																		<table width="100%">
																			<tr>
																				<td width="32">
																					<html:text property="url" size="30" />
																				</td>																				
																				<td>
																					<digi:trn key="aim:or">
																						OR
																					</digi:trn>
																				</td>
																				<td>
																					<input type="Button" onclick="selectFile()" 
																					value="Select file" class="dr-menu">
																				</td>
																			</tr>	
																		</table>
																	</td>
																	<td>
																	</td>
																</tr>
																<tr>
																	<td width="30%" align="right">
																		<digi:trn key="aim:docFileName">File name</digi:trn>
																	</td>
																	<td>
																		<logic:present name="aimAddDocumentForm" property="formFile">
									   								<bean:define id="fileName" name="aimAddDocumentForm" 
																		property="formFile.fileName" />
																	    <%
																			int index2;
																			String extension = null;
																			index2 = ((String)fileName).lastIndexOf(".");	
																			if( index2 >= 0 ) {
																			   extension = "module/cms/images/extensions/" + 
																					((String)fileName).substring(
																									index2 + 1,((String)fileName).length()) + ".gif";
																			}
																	    %>
																	    <digi:img skipBody="true" src="<%=extension%>" border="0" 
																		 align="absmiddle"/>					
																		<bean:write name="aimAddDocumentForm" property="formFile.fileName"/>
																		</logic:present>
																		<logic:notPresent name="aimAddDocumentForm" property="formFile">
																			<digi:trn key="aim:fileNotSelected">File not selected</digi:trn>
																		</logic:notPresent>								
																	</td>
																</tr>	
																<tr>
																	<td width="30%" align="right">
																		<digi:trn key="aim:language">
																		Language
																		</digi:trn>
																	</td>
																	<td>
																		<logic:present name="aimAddDocumentForm" property="languages">
																			<html:select property="language">
																			<bean:define id="lid" name="aimAddDocumentForm" 
																			property="languages" type="java.util.List"/>
																			<html:options collection="lid" property="code" 
																			labelProperty="name"/></html:select>
																	    </logic:present>			
																	</td>
																</tr>
																<tr>
																	<td width="30%" align="right">
																		<digi:trn key="aim:country">
																		Country
																		</digi:trn>
																	</td>
																	<td>
																		<logic:present name="aimAddDocumentForm" property="countries">
																			<html:select property="country">
																			<bean:define id="countries" name="aimAddDocumentForm" 
																			property="countries" type="java.util.List"/>
																			<html:options collection="countries" property="iso" 
																			labelProperty="name"/></html:select>
																	    </logic:present>
																	</td>
																</tr>	
																<tr>
																	<td width="30%" align="right" valign="top">
																		<digi:trn key="aim:docRelatedCategories">
																		Related Categories
																		</digi:trn>
																	</td>
																	<td class="groupContainer" width="90%">
																		<table width="100%" cellpadding="3" cellspacing="0" border="0"
																		class="box-border-nopadding">
																			<tr>
																				<td colspan="2">	
																					<table width="100%" cellpadding="2" 
																					cellspacing="2" border="0">
																						<tr>
																							<td>
																								<input type="Button" onclick="showSelectCategory()"
																								value="Add Category" class="dr-menu">
																								&nbsp;
																								<a href="<%=digiContext%>/cms/addCategory.do" target="_new">
																								Create a new category
																								</a>
																							</td>
																						</tr>
																					</table>
																				</td>
																			</tr>
																			<logic:present name="aimAddDocumentForm" 
																			property="categoryIdList">
																			<tr><td>
																			<table width="100%" cellpadding="2" cellspacing="2" border="0">
																			<logic:iterate indexId="index" name="aimAddDocumentForm" 
																			property="categoryIdList" id="categoryId" type="String">
																			<tr>
																				<td width="100%">
																					<bean:write name="aimAddDocumentForm" 
																					property="<%= "categoryNameList[" + index +  "]" %>"/>
																				</td>
																				<td width="1">
																					<a href="javascript:removeCategory(<bean:write name="aimAddDocumentForm" property="<%= "categoryIdList[" + index +  "]" %>"/>)">
																					<digi:img src="module/cms/images/removeIcon.gif" border="0"/>
																					</a>
																				</td>									
																			</tr>
																			</logic:iterate>
																			</table>
																			</td></tr>
																			</logic:present>
																		</table>
																	</td>
																</tr>
																<tr>
																	<td colspan="2" align="center">
																		<table cellspacing="5">
																			<tr>
																				<td>
																					<input type="button" value="Submit" class="dr-menu" 
																					onclick="createDocument()">
																				</td>
																				<td>
																					<input type="reset" value="Cancel" class="dr-menu"  onclick="javascript:history.go(-1)">
																				</td>
																			</tr>
																		</table>
																	</td>
																</tr>
																
															</table>
														<td>
													</tr>
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
</td></tr>
</table>
</digi:form>
