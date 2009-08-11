<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>


<script langauage="JavaScript">
	function onDelete() {
	<c:set var="translation">
		<digi:trn key="aim:ConfirmDelete">Delete this Scheme ?</digi:trn>
	</c:set>
		var flag = confirm("${translation}");
		return flag;
	}
	function updateScheme(id) {
		
			<digi:context name="addScheme" property="context/module/moduleinstance/updateSectorSchemes.do?event=updateScheme" />
			
			document.aimAddSectorForm.action = "<%= addScheme%>&editSchemeId="+id;
			document.aimAddSectorForm.target = "_self";
			document.aimAddSectorForm.submit();
	
	}
</script>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script type="text/javascript">
	var idOfFolderTrees = ['dhtmlgoodies_tree'];
</script>
<link rel="stylesheet" href="<digi:file src="module/aim/css/css_dhtmlsuite/folder-tree-static.css" />" />
<link rel="stylesheet" href="<digi:file src="module/aim/css/css_dhtmlsuite/context-menu.css" />" />
<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>


<digi:instance property="aimAddSectorForm" />
<digi:context name="digiContext" property="context" />
<digi:form action="/viewSectorDetails.do" method="post">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
<html:hidden property="idGot"/>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<c:set var="clickToViewAdmin">
						<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${clickToViewAdmin}" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="schemes">
							<digi:trn key="aim:schemes">Click here to Sector Schemes</digi:trn>
						</c:set>
						<digi:link href="/getSectorSchemes.do" styleClass="comment" title="${schemes}" >
						<digi:trn key="aim:schemes">
						Schemes
						</digi:trn>
						</digi:link>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
					<digi:trn key="aim:sectorSchemeTree">Sector Scheme Tree</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<digi:errors />
					</td>
				</tr>				
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1 border=0>
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#d7eafd cellPadding=1 cellSpacing=1 width="100%" valign="top">
							<tr bgColor=#ffffff>
								<td vAlign="top" width="100%">
									
									<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left>	
										
										<field:display name="Level 1 Sectors List" feature="Sectors">
										<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<!-- Table title -->
											<bean:write  name ="aimAddSectorForm" property="secSchemeName"/>
											<!-- end table title -->										
										</td></tr>
										<tr><td>
											<table width="100%" cellspacing=1 cellpadding=4 valign=top align=left bgcolor="#d7eafd">
													<logic:empty name="aimAddSectorForm" property="schemeTree">
													<tr bgcolor="#ffffff">
														<td colspan="5" align="center"><b>
															<digi:trn key="aim:noSectorPresent">
															No Sector present
															</digi:trn>
														</b></td>
													</tr>
													</logic:empty>

													<!-- end page logic -->													
											</table>
										</td></tr>
										<logic:notEmpty name="aimAddSectorForm" property="schemeTree">
										<tr><td>
											 <font size="2">
										 		<ul id="dhtmlgoodies_tree" class="dhtmlgoodies_tree">
										 			<li id="rootnode" noDrag="true" noSiblings="true" noDelete="true" noRename="true">
										 				<a>Root node</a>
										 				<ul id = "rootNod">
										 				</ul>
										 			</li>
										 		</ul>
											 	<script type="text/javascript">
											 		function insertRoot(id, name){
												 		var parent = document.getElementById("rootNod");
														var newItem = document.createElement('li');
														newItem.setAttribute("id","nod" + id);
														newItem.setAttribute("noDelete", "true");
														var newA = document.createElement('a');
													    newItem.appendChild(newA);
														var newTxt=document.createTextNode(name);
														newA.appendChild(newTxt);
												 		
												 		parent.appendChild(newItem);
											 		}
													function insertChild(pid, id, name){
														var parent = document.getElementById("nod" + pid);
														var newItem = document.createElement('li');
														newItem.setAttribute("id","nod" + id);
														newItem.setAttribute("noDelete", "true");
														var newA = document.createElement('a');
													    newItem.appendChild(newA);
														var newTxt=document.createTextNode(name);
														newA.appendChild(newTxt);

														var parentBody = parent.getElementsByTagName("ul");
																		
														if (parentBody.length == 0){
															var newUl = document.createElement("ul");
															parent.appendChild(newUl);
															parentBody = parent.getElementsByTagName("ul");
														}
														var ulBody = parentBody[0];
														ulBody.appendChild(newItem);
													}
											 		
											 		<logic:notEmpty name="aimAddSectorForm" property="schemeTree">
														<logic:iterate name="aimAddSectorForm" property="schemeTree" id="sector"
																		type="org.digijava.module.aim.dbentity.AmpSector	">
															<logic:empty name="sector" property="parentSectorId">
																insertRoot('<bean:write name="sector" property="ampSectorId"/>','<bean:write name="sector" property="name"/>'+' ['+'<bean:write name="sector" property="sectorCodeOfficial"/>'+']');
															</logic:empty>
															<logic:notEmpty name="sector" property="parentSectorId">
																insertChild('<bean:write name="sector" property="parentSectorId.ampSectorId"/>','<bean:write name="sector" property="ampSectorId"/>','<bean:write name="sector" property="name"/>'+' ['+'<bean:write name="sector" property="sectorCodeOfficial"/>'+']');
															</logic:notEmpty>
														</logic:iterate>
													</logic:notEmpty>
												</script>
										 		

												<script type="text/javascript">
													treeObj = new DHTMLSuite.JSDragDropTree();
													treeObj.setTreeId('dhtmlgoodies_tree');
													treeObj.setMaximumDepth(7);
													treeObj.setMessageMaximumDepthReached('Maximum depth reached');
													treeObj.init();
												</script>
												
											</font>
										</td></tr>
										<tr><td>
										</td></tr>
										</logic:notEmpty>
									</field:display>									
										</table>
									
								</td>
							</tr>
						</table>
					</td>
					
					<td noWrap width=100% vAlign="top">
						<table align=center cellPadding=0 cellSpacing=0 width="90%" border=0>	
							<tr>
								<td>
									<!-- Other Links -->
									<table cellPadding=0 cellSpacing=0 width=100>
										<tr>
											<td bgColor=#c9c9c7 class=box-title>
												<digi:trn key="aim:otherLinks">
												Other links
												</digi:trn>
											</td>
											<td background="module/aim/images/corner-r.gif" height="17" width=17>
												&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellSpacing=1 width="100%">
										<field:display name="Add Sector Level 1 Link" feature="Sectors">
										<tr>
											<td>
											<jsp:useBean id="urlParams5" type="java.util.Map" class="java.util.HashMap"/>
												<c:set target="${urlParams5}" property="ampSecSchemeId">
													<bean:write name="aimAddSectorForm" property="secSchemeId" />
												</c:set>
												<c:set target="${urlParams5}" property="parent" value="scheme"/>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="clickToAddSector">
												<digi:trn key="aim:clickToAddSector">Click here to Add a Sector</digi:trn>
												</c:set>
												<digi:link href="/addSector.do" name="urlParams5" title="${clickToAddSector}" >
												<digi:trn key="aim:addSector">
												Add Sector
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										</field:display>
										<field:display name="View Schemes Link" feature="Sectors">
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="clickToViewSchemes">
												<digi:trn key="aim:clickToViewSchemes">Click here to the Schemes</digi:trn>
												</c:set>
												<digi:link href="/getSectorSchemes.do" title="${clickToViewSchemes}" >
												<digi:trn key="aim:viewSchemes">
												View Schemes
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										</field:display>
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="clickToViewTree">
													<digi:trn key="aim:clickToViewTree">Click here to view sector Tree</digi:trn>
												</c:set>
												<digi:link href="/viewSchemeTree.do" name="urlParams5" title="${clickToViewTree}" >
													<digi:trn key="aim:AmpAdminHome">
														Tree View
													</digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="clickToViewAdmin">
												<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</c:set>
												<digi:link href="/admin.do" title="${clickToViewAdmin}" >
												<digi:trn key="aim:AmpAdminHome">
												Admin Home
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										<!-- end of other links -->
									</table>
								</td>
							</tr>
						</table>
					</td></tr>
					</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</digi:form>


