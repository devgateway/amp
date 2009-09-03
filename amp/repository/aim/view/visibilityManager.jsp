<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
 
<script language="JavaScript">

	function toggleFeature(id) {
		<digi:context name="urlVal" property="context/module/moduleinstance/featureManager.do" />			  
		document.aimFeatureManagerForm.action = "<%= urlVal %>?toggle=true&fId="+id;
		document.aimFeatureManagerForm.submit();		
	}

</script>

<digi:instance property="aimVisibilityManagerForm" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
	
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:AmpAdminHome">
							Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:featureManager">
							Feature Manager
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<span class=subtitle-blue>
						<digi:trn key="aim:featureManager">
							Feature Manager
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
				 		<font color="red">
					 		<logic:iterate id="element" name="aimVisibilityManagerForm" property="errors">
								<digi:trn key="${element.key}">
									<bean:write name="element" property="value"/>
								</digi:trn>
							
							</logic:iterate>
							
							<logic:iterate id="element" name="aimVisibilityManagerForm" property="messages">
								<digi:trn key="${element.key}">
									<bean:write name="element" property="value"/>
								</digi:trn>
							</logic:iterate>
						</font>
					</td>
				</tr>
				
				
				<tr>
					<td noWrap width=80% vAlign="top">
						<logic:equal name="aimVisibilityManagerForm" property="mode" value="editTemplateTree">			
							<jsp:include page="manageTreeVisibility.jsp" />
						</logic:equal>
							<logic:equal name="aimVisibilityManagerForm" property="mode" value="manageTemplates">
							<jsp:include page="manageTemplatesVisibility.jsp" />
							</logic:equal>
							<logic:equal name="aimVisibilityManagerForm" property="mode" value="addNew">
								<jsp:include page="newTemplateVisibility.jsp" />
							</logic:equal>
							<logic:equal name="aimVisibilityManagerForm" property="mode" value="viewFields">
								<jsp:include page="viewFieldsVisibility.jsp" />
							</logic:equal>
						
					</td>
				
				<td valign="top">
				<!-- start  -->
				
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
												<td background="module/aim/images/corner-r.gif" 	height="17" width=17>
												&nbsp;
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td bgColor=#ffffff class=box-border>
										<table cellPadding=5 cellSpacing=1 width="100%">
											<tr>
												<td>
													<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
														<c:set var="translation">
															<digi:trn key="aim:clickToManageTemplates">Click here to Manage the Templates </digi:trn>
														</c:set>
													<jsp:useBean id="urlParamsMngTempl" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParamsMngTempl}" property="action" value="manageTemplates"/>
													 <digi:link href="/visibilityManager.do" name="urlParamsMngTempl" 
													title="${translation}" ><digi:trn key="aim:manageTheTemplates">Manage the Templates</digi:trn></digi:link>
												</td>
											</tr>
											<tr>
												<td>
													<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
														<c:set var="translation">
															<digi:trn key="aim:addNewTemplate">Click here to Add a New Template </digi:trn>
														</c:set>
													<jsp:useBean id="urlParams12" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParams12}" property="action" value="add"/>
													 <digi:link href="/visibilityManager.do" name="urlParams12" 
													title="${translation}" ><digi:trn key="aim:addVisibilityTemplate">Add a New Template</digi:trn></digi:link>
												</td>
											</tr>
											<tr>
												<td>
													<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
													<c:set var="translation">
														<digi:trn key="aim:clickToManageFieldsFeaturesModule">Click here to Field, Features and Modules </digi:trn>
													</c:set>
													<jsp:useBean id="urlParams13" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParams13}" property="action" value="viewFields"/>
													 <digi:link href="/visibilityManager.do" name="urlParams13" 
													title="${translation}" ><digi:trn key="aim:manageFieldsFeaturesModules">List of Fields, Features, Modules</digi:trn></digi:link>
													
												</td>
											</tr>
											
									
											<tr>
												<td>
													<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
													<c:set var="translation">
														<digi:trn key="aim:setTemplateInUse">Click here to Set the Template in use</digi:trn>
													</c:set>
													<digi:link href="/GlobalSettings.do" title="${translation}" ><digi:trn key="aim:GlobalSettings">Global Settings</digi:trn></digi:link>
												</td>
											</tr>


											<!-- end of other links -->
										</table>
									</td>
								</tr>
							</table>
				
				<!--  end -->
				</td>
					
				</tr>
				
</table>
