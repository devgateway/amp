<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
 
<!-- Individual YUI CSS files --> 
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/skins/sam/tabview.css"> 
<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script> 
 
<script language="JavaScript">

	function toggleFeature(id) {
		<digi:context name="urlVal" property="context/module/moduleinstance/featureManager.do" />
		document.aimFeatureManagerForm.action = "<%= urlVal %>?toggle=true&fId="+id;
		document.aimFeatureManagerForm.submit();		
	}

</script>

	<h1 class="admintitle">
						<digi:trn key="aim:featureManager">
							Feature Manager
						</digi:trn>
						</h1>
						
<digi:instance property="aimVisibilityManagerForm" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="1000" align="center">
	<tr>
		<td align=left valign="top" width=1000>
	
			<table cellPadding=5 cellspacing="0" width="100%" border="0" style="font-size:12px;">

				<tr>
					<td height=16 valign="center" width=571 colspan=2>
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
					<td noWrap width="80%" vAlign="top">
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
				
				<table align="center" cellpadding="0" cellspacing="0" width="90%" border="0" style="font-size:12px;">
								<tr>
									<td style="border-bottom:1px solid #ccc;">
										<!-- Other Links -->
										<table cellpadding="0" cellspacing="0" width="120">
											<tr>
												<td bgColor=#c9c9c7 class=box-title style="font-size:12px;">
													<digi:trn key="aim:otherLinks">
													Other links
													</digi:trn>
												</td>
												<td background="module/aim/images/corner-r.gif" height="17" width="17"></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td bgColor=#ffffff>
										<table cellPadding=5 cellspacing="1" width="100%" class="inside">
										
											<tr>
												<td>
													<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
													<c:set var="trnViewAdmin">
														<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
													</c:set> <digi:link href="/admin.do" title="${trnViewAdmin}">
														<digi:trn key="aim:AmpAdminHome">
																Admin Home
															</digi:trn>
													</digi:link></td>
											</tr>
										
											<tr>
												<td class="inside">
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
												<td class="inside">
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
												<td class="inside">
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
												<td class="inside">
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
</td>
</tr>
</table>

