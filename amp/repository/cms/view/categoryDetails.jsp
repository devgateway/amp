<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.cms.form.CMSForm"%>

<digi:instance property="cmsForm"/>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/cms/scripts/cms.js"/>"></script>
<link rel="stylesheet" href="<digi:file src="module/cms/css/cms.css"/>">


<digi:form action="/saveCategory.do" >
<html:hidden name="cmsForm" property="parentCategoryId"/>
<html:hidden name="cmsForm" property="categoryId"/>
<html:hidden name="cmsForm" property="processingMode"/>


<script language="JavaScript">
	function showSelectParentCategory() {
	  openNewWindow(450, 377);
	  	 <bean:define id="categoryId" name="cmsForm" property="categoryId"/>
	     <digi:context name="showSelectCategory" property="context/module/moduleinstance/showSelectCategory.do" />
	     document.cmsForm.action = "<%= showSelectCategory %>?categoryId=<%=categoryId%>&selType=<%= CMSForm.SELECT_CAT_FOR_PARENT %>";
		 document.cmsForm.target = popupPointer.name;
	     document.cmsForm.submit();
	}
	
	function showSelectRelatedCategory() {
	  openNewWindow(450, 377);
	  	 <bean:define id="categoryId" name="cmsForm" property="categoryId"/>
	     <digi:context name="showSelectCategory" property="context/module/moduleinstance/showSelectCategory.do" />
	     document.cmsForm.action = "<%= showSelectCategory %>?categoryId=<%=categoryId%>&selType=<%= CMSForm.SELECT_CAT_FOR_RELATED %>";
		 document.cmsForm.target = popupPointer.name;
	     document.cmsForm.submit();
	}	

	function removeRelatedCategory (id) {
	     <digi:context name="removeRelatedCategory" property="context/module/moduleinstance/removeCategory.do" />
	     document.cmsForm.action = "<%= removeRelatedCategory %>?removeCategoryId=" + id + "&selType=<%= CMSForm.SELECT_CAT_FOR_RELATED %>";
	     document.cmsForm.submit();	
	}
	
	function removeParentCategory (id) {
	     <digi:context name="removeParentCategory" property="context/module/moduleinstance/removeCategory.do" />
	     document.cmsForm.action = "<%= removeParentCategory %>?removeCategoryId=" + id + "&selType=<%= CMSForm.SELECT_CAT_FOR_PARENT %>";
	     document.cmsForm.submit();	
	}	
	
	
	function cancel() {
	     <digi:context name="showTaxonomyManager" property="context/module/moduleinstance/showTaxonomyManager.do" />
	     document.cmsForm.action = "<%= showTaxonomyManager %>";
		 document.cmsForm.target = "_self";
	     document.cmsForm.submit();
	}
	
	function save() {
	     <digi:context name="saveCategory" property="context/module/moduleinstance/saveCategory.do" />
	     document.cmsForm.action = "<%= saveCategory %>";
		 document.cmsForm.target = "_self";
	     document.cmsForm.submit();	
	}
</script>


<br>
<digi:errors/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td colspan="3" height="1">
		<%-- Table header --%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="1"><digi:img src="module/cms/images/headerLeftEnd.gif" border="0"/></td>
				<td width="99%" class="mainHeader" align="center" valign="middle">
					<digi:trn key="cms:categoryDetails">Category details</digi:trn>
				</td>
				<td width="1"><digi:img src="module/cms/images/headerRightEnd.gif" border="0"/></td>				
			</tr>
		</table>
		<%-- end of Table header --%>
	</td>
</tr>
<tr>
	<td class="leftBorder" width="1">
		<digi:img src="module/cms/images/tree/spacer.gif" width="9" border="0"/>
	</td>
	<td width="100%" align="center" bgcolor="#F2F4FC">
		<%-- Inner --%>

		<table width="98%" border="0" cellspacing="0" cellpadding="3">
			<tr>
				<td>
					<digi:trn key="cms:id">ID</digi:trn>
				</td>
				<td>
					<logic:equal name="cmsForm" property="processingMode" value="<%= String.valueOf(CMSForm.MODE_NEW) %>">
						<digi:trn key="cms:unsaved">Unsaved</digi:trn>
					</logic:equal>
					<logic:equal name="cmsForm" property="processingMode" value="<%= String.valueOf(CMSForm.MODE_EDIT) %>">
						<bean:write name="cmsForm" property="categoryId"/>
					</logic:equal>					
				</td>
			</tr>
			<tr>
				<td noWrap>
					<digi:trn key="cms:name">Name</digi:trn>
				</td>
				<td>
					<html:text name="cmsForm" property="categoryName" styleClass="rowItemField"/>
				</td>
			</tr>
			<tr>
				<td noWrap>
					<digi:trn key="cms:scopeNote">Scope note</digi:trn>
				</td>
				<td>
					<html:textarea name="cmsForm" property="categoryScopeNote" cols="50" rows="3"/>
				</td>
			</tr>
			
			<tr>
				<td valign="top" width="10%" nowrap>
					<digi:trn key="cms:parentCategories">Parent categories</digi:trn>
				</td>
				<td class="groupContainer" width="90%">
					<table width="100%" cellpadding="3" cellspacing="0" border="0">
						<tr>
							<td colspan="3">
								<table width="100%" height="1" cellpadding="0" cellspacing="0" border="0">
									<tr>
										<td><digi:img src="module/cms/images/rowItemLeft.gif" height="26" border="0"/></td>
										<td class="rowItem" width="99%" valign="middle" nowrap align="left">
											<input type="Button" value="Add category" onclick="javascript:showSelectParentCategory()" class="rowItemButton">
										</td>
										<td><digi:img src="module/cms/images/rowItemRight.gif" height="26" border="0"/></td>
									</tr>
								</table>
							</td>
						</tr>
						

						<logic:present name="cmsForm" property="propertyArray">
								<tr>
									<td width="1">
										<html:radio name="cmsForm" property="primaryParentCategoryId" value="0"/>
									</td>
									<td noWrap width="100%">
										<digi:trn key="cms:none">None</digi:trn>
									</td>
									<td width="1">&nbsp;</td>
								</tr>
						
							<logic:iterate indexId="index" name="cmsForm" property="propertyArray" id="parentId" type="String">
								<tr>
									<td width="1">
										<bean:define id="catId" name="cmsForm" property="<%= "propertyArray[" + index +  "]" %>" type="String"/>
										<html:radio name="cmsForm" property="primaryParentCategoryId" value="<%= catId %>"/>
										<html:hidden name="cmsForm" property="<%= "propertyArray[" + index +  "]" %>"/>
										<html:hidden name="cmsForm" property="<%= "nameArray[" + index +  "]" %>"/>
									</td>
									<td width="100%">
										<bean:write name="cmsForm" property="<%= "nameArray[" + index +  "]" %>"/>
									</td>
									<td width="1">
										<a href="javascript:removeParentCategory(<bean:write name="cmsForm" property="<%= "propertyArray[" + index +  "]" %>"/>)">
											<digi:img src="module/cms/images/removeIcon.gif" border="0"/>
										</a>									
									</td>
								</tr>
							</logic:iterate>
						</logic:present>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td valign="top" width="10%" nowrap>
					<digi:trn key="cms:relatedCategories">Related categories</digi:trn>
				</td>
				<td class="groupContainer" width="90%">
					<table width="100%" cellpadding="3" cellspacing="0" border="0">
						<tr>
							<td colspan="2">
								<table width="100%" height="1" cellpadding="0" cellspacing="0" border="0">
									<tr>
										<td><digi:img src="module/cms/images/rowItemLeft.gif" height="26" border="0"/></td>
										<td class="rowItem" width="99%" valign="middle" nowrap align="left">
											<input type="Button" value="Add category" onclick="javascript:showSelectRelatedCategory()" class="rowItemButton">
										</td>
										<td><digi:img src="module/cms/images/rowItemRight.gif" height="26" border="0"/></td>
									</tr>
								</table>
							</td>
						</tr>
						<logic:present name="cmsForm" property="relatedIdArray">
							<logic:iterate indexId="index" name="cmsForm" property="relatedIdArray" id="relatedId" type="String">
								<tr>
									<td width="100%">
										<html:hidden name="cmsForm" property="<%= "relatedIdArray[" + index +  "]" %>"/>
										<html:hidden name="cmsForm" property="<%= "relatedNameArray[" + index +  "]" %>"/>									
										<bean:write name="cmsForm" property="<%= "relatedNameArray[" + index +  "]" %>"/>
									</td>
									<td width="1">
										<a href="javascript:removeRelatedCategory(<bean:write name="cmsForm" property="<%= "relatedIdArray[" + index +  "]" %>"/>)">
										<digi:img src="module/cms/images/removeIcon.gif" border="0"/>
										</a>
									</td>									
								</tr>
							</logic:iterate>
						</logic:present>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<table width="100%" height="1" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td><digi:img src="module/cms/images/rowItemLeft.gif" height="26" border="0"/></td>
							<td class="rowItem" width="99%" valign="middle" nowrap align="center">
								<input type="Button" value="Save" class="rowItemButton" onclick="save()">
								<input type="Button" value="Cancel" class="rowItemButton" onclick="cancel()">
							</td>
							<td><digi:img src="module/cms/images/rowItemRight.gif" height="26" border="0"/></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>

<%-- Inner --%>
	</td>
	<td class="rightBorder" width="1">
		<digi:img src="module/cms/images/tree/spacer.gif" width="9" border="0"/>
	</td>	
</tr>
<tr>
	<td height="1"><digi:img src="module/cms/images/leftBottom.gif" width="9" height="9" border="0"/></td>
	<td width="100%" class="bottomBorder" height="1"><digi:img src="module/cms/images/tree/spacer.gif" height="9" border="0"/></td>
	<td height="1"><digi:img src="module/cms/images/rightBottom.gif" width="9" height="9" border="0"/></td>	
</tr>
</table>
</digi:form>